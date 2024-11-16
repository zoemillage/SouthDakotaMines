/** ***************************************************************************
 * @file
 * 
 * @brief Analyzes data to determine if a product lot is defective
 *
 * @mainpage program 3 - Accept or Reject the Lot?
 *
 * @section course_section Course Information
 *
 * @author Zoe Millage
 *
 * @date Novermber 2020
 *
 * @par Instructors:
 *          Christina Bergevin,
 *          Dr. Daniel de Castro,
 *          Dr. Mengyu Qiao
 *
 * @par Course:
 *         CSC 150 - Computer Science 1
 *
 * @section program_section Program Information
 *
 * @details You are consulting for LLC Inc. who manufactures wireless 
 * routers. Within their current production process, sample routers are taken
 * from each production run and tested for their performance on various
 * attributes. However, the company does not have a way to quickly analyze 
 * the resulting data, thus the data is currently not very useful. LLC has 
 * asked you to create a program that can read in their data files and 
 * determine if, based on the samples tested, the production run meets minimum
 * requirements and should be accepted. If the sample shows too much variation
 * in one or more of the measured attributes, the associated lot should be
 * rejected. LLC also needs to knowquickly which attributes are not up to
 * standards, so that they can find and fix the problem in their 
 * production line.
 *
 *
 * @section compile_section Compiling and Usage
 *
 * @par Compiling Instructions:
 *      None
 *
 * @par Usage:
	@verbatim
   c:\> prog3.exe
   d:\> c:\bin\prog3.exe
   @endverbatim
 *
 *****************************************************************************/

#define _CRT_SECURE_NO_WARNINGS

#include <cmath>
#include <cstdlib>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <string.h>

using namespace std;

/******************************************************************************
 *             Constant Variables, defines and Enums
 *****************************************************************************/
 /*!
  * @brief Maximum median of C
  */
const double MAX_CURRENT_MEDIAN = 3.6;

/*!
 * @brief Maximum standard deviation of IV
 */
const double MAX_GAIN_VARIATION = 1.5;

/*!
 * @brief Maximum failing percentage for a given attribute
 */
const double MAX_PERCENTAGE = 1.0;        //1%

 /*!
  * @brief maximum standard deviation of A
  */
const double MAX_VOLTAGE_VARIATION = 0.2;


/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
void calc_A_stdev(double data[][5], int num_values, double calculations[]);
void calc_G_stdev(double data[][5], int num_values, double calculations[]);
void calc_median(double data[][5], int num_values, double calculations[]);
void calc_pass_fail(double calculations[], int pass_fail[]);
void calc_percentage(double data[][5], int num_values, int column, 
					 double calculations[]);
void create_report(ofstream & out, int lot_id, double calculations[5], 
				   int pass_fail[]);
void get_output_file_name(char output_file[], char* argv[]);
int read_measurements(ifstream & fin, int & lot_id, double data[][5]);
void sort_array(double data[][5], int num_values);



int main(int argc, char* argv[])
{
	double data[100][5]; 
	double calculations[5]; /*[][0] holds Arithmetic standard deviation, 
	                          [][1] median of C, 
							  [][2] Geometric standard deviation, 
							  [][3] percentage failed for MT24, 
							  [][4] percentage failed for MT5*/

	char output_file[257] = "report_";  /*256 seems to be a fairly common 
										max potential file name length*/
	
	ifstream fin;

	int lot_id;
	int num_values;
	int pass_fail[5] = { 0 };    /*stores whether each attribute passed;
	                              automatically set to all fail*/

	ofstream fout;

	//error check command line arguments
	if ( argc != 2 )
	{
		cout << "Usage: prog3.exe <in filename>" << endl;

		return 1;
	}

	//append "report_" to beginning of arvg[1] to get fout's file name
	get_output_file_name( output_file, argv );

	//open files
	fin.open( argv[1] );
	fout.open( output_file );

	//error check for proper file opening
    if ( !fin.is_open() || !fout.is_open() )
    {
        cout << "File opening error; program ending" << endl;

        fin.close();
        fout.close();

        return 2;
    }

    //get data
	num_values = read_measurements( fin, lot_id, data );

	//calculate parameters
	calc_A_stdev( data, num_values, calculations );
	calc_median( data, num_values, calculations );
	calc_G_stdev( data, num_values, calculations );
	calc_percentage( data, num_values, 3, calculations );
	calc_percentage( data, num_values, 4, calculations );

	//calculate if attributes passed or failed
	calc_pass_fail( calculations, pass_fail );

	//output results
	create_report( fout, lot_id, calculations, pass_fail );

	//close the files
	fin.close();
	fout.close();

    return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the standard deviation of IV.
 *
 * @param[in] data - holds the data from the input file
 * @param[in] num_values - number of rows in the "data" array
 * @param[in,out] calculations - holds mathematical calculations
 *
 * @returns none
 *****************************************************************************/
void calc_A_stdev(double data[][5], int num_values, double calculations[])
{
	double asd = 0;
	double mean = 0;

	int i;
	int j;

	/* formula for Arithmetic standard deviation:
	asd = sqrt(1/N * summation from i = 1 to N of (x[i] - m[X])^2)
    where m[X] = 1/N * summation from i = 1 to N of x[i]

    X is a list; = {x[1],x[2],...x[N]}
    N = numer of values in list X */

	//calculate mean
	for (i = 0; i < num_values; i++)
		mean += data[i][0];

	mean /= (double)num_values;

	//calculate arithmetic standard deviation
	for (j = 0; j < num_values; j++)
		asd += pow( ( data[j][0] - mean ), 2 );

	asd /= (double)num_values;

	asd = pow( asd, 0.5 );

	//input result into an array
	calculations[0] = asd;

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the standard deviation of A.
 *
 * @param[in] data - holds the data from the input file
 * @param[in] num_values - number of rows in the "data" array
 * @param[in,out] calculations - holds mathematical calculations
 *
 * @returns none
 *****************************************************************************/
void calc_G_stdev(double data[][5], int num_values, double calculations[])
{
	double mean = 1;
	double gsd = 0;

	int i;

	/*formula for geometric standard deviation:
    for log-normal distributions expressed in log base 10,
    
	gsd = 10 ^ ( sqrt( (summation from 1 = 1 to N of
		   (log10 (x[i] / m(X) ) )^2 ) / N )

    where m(X) = (multiplication from i = 1 to N of x[i] ) ^ ( 1 / N )

    X is a list; = {x[1],x[2],...x[N]}
    N = numer of values in list X */

	//calculate geometric mean
	for (i = 0; i < num_values; i++)
	{
		if(data[i][2] != 0)
		mean *= data[i][2];
	}

	mean = pow( mean, ( 1.0 / num_values ) );

	//calculate geometric standard deviation
	for (i = 0; i < num_values; i++)
		gsd += pow( log10( data[i][2] / mean ), 2 );

	gsd /= (double)num_values;

	gsd = pow( gsd, 0.5 );

	gsd = (double)pow( 10, gsd );

	//input result into array
	calculations[2] = gsd;

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the median of C.
 *
 * @param[in] data - holds the data from the input file
 * @param[in] num_values - number of rows in the "data" array
 * @param[in,out] calculations - holds mathematical calculations
 *
 * @returns none
 *****************************************************************************/
void calc_median(double data[][5], int num_values, double calculations[])
{
    /* formulas to compute median:
    med(X') = X'[N / 2] / 2 if N is even,
    ( X'[( N - 1 ) / 2 ] + X'[( N + 1 ) / 2 ] ) / 2 if N is odd

    X' is a sorted list; = {x[1],x[2],...x[N]}
    N = numer of values in list X  */

	//sort the first column of the array
	sort_array(data, num_values);

	if (num_values % 2 == 0)
	{
		for(int i = 0; i < num_values; i++)

		calculations[1] = data[(num_values - 1) / 2][1];
	}

	else
	{
		calculations[1] = (data[ ( ( num_values - 2 ) / 2 ) ][1] +
						   data[ ( ( num_values ) / 2 ) ][1]) / 2.0;
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates whether a given attribute passes or fails standards.
 *
 * @param[in] calculations - holds mathematical calculations
 * @param[in,out] pass_fail - determines whether an attribute passes or fails
 *                each attribute
 *
 * @returns none
 *****************************************************************************/
void calc_pass_fail(double calculations[], int pass_fail[])
{
	//calculates whether each attribute passes or fails
	if (calculations[0] < MAX_VOLTAGE_VARIATION)
	{
		pass_fail[0] = 1;
	}

	if (calculations[1] < MAX_CURRENT_MEDIAN)
	{
		pass_fail[1] = 1;
	}

	if (calculations[2] < MAX_GAIN_VARIATION)
	{
		pass_fail[2] = 1;
	}

	if (calculations[3] < MAX_PERCENTAGE)
	{
		pass_fail[3] = 1;
	}

	if (calculations[4] < MAX_PERCENTAGE)
	{
		pass_fail[4] = 1;
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the percentage of failing units for a given attribute.
 *
 * @param[in] data - holds the data from the input file
 * @param[in] num_values - number of rows in the "data" array
 * @param[in] column - the data column to be analyzed
 * @param[in,out] calculations - holds mathematical calculations
 *
 * @returns none
 *****************************************************************************/
void calc_percentage(double data[][5], int num_values, int column, 
					 double calculations[])
{
	int i;
	int num_fail = 0;

	//finds number of failing models
	for (i = 0; i < num_values; i++)
	{
		if (data[i][column] == 0) //1 = pass, 0 = fail
			++num_fail;
	}

	//calculates failing percentage
	calculations[column] = ( (double)num_fail / (double)num_values ) * 100.0;

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints the results of the previous functions.
 *
 * @param[in] out - the output file
 * @param[in] lot_id - the id of the lot being tested
 * @param[in] calculations - holds mathematical calculations
 * @param[in] pass_fail - determines whether an attribute passes or fails
 *
 * @returns none
 *****************************************************************************/
void create_report(ofstream & out, int lot_id, double calculations[5],
				   int pass_fail[])
{
	//output lot ID
	out << "LotID: " << lot_id << endl << endl;

	//set formatting for numbers
	out << fixed << showpoint << setprecision(4);

	/*output the results for each attribute
	Standards Deviation (IV):*/
	out << setw(29) << left << "Standard Deviation of IV: " << right
		<< setfill(' ') << calculations[0] << setw(10) << setfill(' ');
	
	if (pass_fail[0] == 1)
		out << "Pass" << endl;

	else
		out << "Fail" << endl;

	//Median
	out << setw(29) << left << "Median of C: " << right
		<< setfill(' ') << calculations[1] << setw(10) << setfill(' ');

	if (pass_fail[1] == 1)
		out << "Pass" << endl;

	else
		out << "Fail" << endl;


	//Standard Deviation (A):
	out << setw(29) << left << "Standard Deviation of A: " << right
		<< setfill(' ') << calculations[2] << setw(10) << setfill(' ');

	if (pass_fail[2] == 1)
		out << "Pass" << endl;

	else
		out << "Fail" << endl;


	//Failing % (MT24):
	out << setw(29) << left << "Percentage of failed MT24: " << right
		<< setfill(' ') << calculations[3] << setw(10) << setfill(' ');

	if (pass_fail[3] == 1)
		out << "Pass" << endl;

	else
		out << "Fail" << endl;


	//Failing % (MT5):
	out << setw(29) << left << "Percentage of failed MT5: " << right
		<< setfill(' ') << calculations[4] << setw(10) << setfill(' ');

	if (pass_fail[4] == 1)
		out << "Pass" << endl;

	else
		out << "Fail" << endl;


	//determine whether to accept or reject the lot
		out << endl << "Action: ";

	if (pass_fail[0] == 1 && pass_fail[1] == 1 && pass_fail[2] == 1 &&
		pass_fail[3] == 1 && pass_fail[4] == 1)
		out << "Accept";

	else
		out << "Reject";


	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Gets the name of the output file; appends "report_" to the beginning of the
 * input file's name to get the output file.
 *
 * @param[in,out] output_file - an array holding the name of the output file
 * @param[in] argv - array that holds the name of the input file
 *
 * @returns 0 - program ran successfully
 *****************************************************************************/
void get_output_file_name(char output_file[], char* argv[])
{
	// gets output file name
	strncat(output_file, argv[1], 249);

	return;
}



/** ***************************************************************************
 *  * @author Zoe Millage
 *
 * @par Description:
 * Reads measurements from the input file and passes them into an array.
 *
 * @param[in] fin - variable name for the input file
 * @param[in, out] lot_id - the id of the lot being tested
 * @param[in, out] data - holds the data from the input file
 *
 * @returns The numnber of rows filled
 *****************************************************************************/
int read_measurements(ifstream & fin, int & lot_id, double data[][5])
{
	int i = 0;
	int j = 0;
	int elements_filled = -1;

    //get lot ID
    fin >> lot_id;

    //read data to array
	for (i = 0; i < 100; i++)
	{
		
		if (fin)
		{

			for (j = 0; j < 5; j++)
			{
				fin >> data[i][j];
			}

			++elements_filled;

		}

		else
			return elements_filled;
	}

    //return number of rows read
	return elements_filled;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Sorts the "data" array.
 *
 * @param[in, out] data - holds the data from the input file
 * @param[in] num_values - number of rows in the "data" array
 *
 * @returns none
 *****************************************************************************/
void sort_array(double data[][5], int num_values)
{
	double temp;   //temporary variable, used for swapping values

	int i, j;
	int swap_index;

	// sort
	for (i = 0; i < num_values - 1; i++)
	{
		swap_index = i;
		for (j = i + 1; j < num_values; j++)
		{
			if (data[j][1] < data[swap_index][1])
			{
				swap_index = j;
			}

			temp = data[i][1];
			data[i][1] = data[swap_index][1];
			data[swap_index][1] = temp;
		
		}
	}

	return;
}