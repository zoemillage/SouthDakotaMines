/** ***************************************************************************
 * @file
 *
 * @brief Gets and compiles score and name data in two files
 *
 * @mainpage p1013 - IO and Command Line Options
 *
 * @section course_section Course Information
 *
 * @authors Zoe Millage
 *
 * @date Fall 2020
 *
 * @par Course:
 *         CSC 150 - Computer Science 1
 *
 * @section program_section Program Information
 *
 * @details This program takes two files, 1 input and 1 output, tries to open 
 * them, and, if successful, reads the input file in the format
 * # of entries
 * scores, separated by whitespace (with # scores)
 * names, separated by whitespace (with # names)
 * 
 * and outputs all the entires to the output file in the format
 * name :score.
 *
 *****************************************************************************/

#include <fstream>
#include <iomanip>
#include <iostream>

using namespace std;

//function prototypes
void print_records_to_file(ofstream & fout, double scores[], char names[][81], int num_elements);
void read_names(ifstream & fin, char names[][81], int num_elements);
void read_scores(ifstream & fin, double scores[], int num_elements);


int main( int argc, char* argv[] )
{
	ifstream fin;
	ofstream fout;

	int num_elements = 0;
	double scores[101];
	char names[100][81];

	// check command line arguments
	if (argc != 3)
	{
		cout << "Usage: p1013.exe <in filename> <out filename>" << endl;

		return 0;
	}

	// open/check files
	fin.open(argv[1]);
	fout.open(argv[2]);

	if (!fin.is_open() || !fout.is_open())
	{
		cout << "File opening error; program ending" << endl;

		fin.close();
		fout.close();

		return 0;
	}


	fin >> num_elements;

	//Calls function to read in the scores
	read_scores(fin, scores, num_elements);


	//Because we are switching between reading numbers
	// and reading lines, there is a newline
	// left in the buffer. This code will skip over the extra 
	// newline, so we can get on to reading in the names.
	fin.ignore();

	//Calls function to read in the names
	read_names(fin, names, num_elements);


	//Calls function to print the new file
	print_records_to_file(fout, scores, names, num_elements);


	fin.close();
	fout.close();

	return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints all the records to the given file.
 *
 * @param[in, out] fout - the stream for file output
 * @param[in] scores - the scores to print
 * @param[in] names - the names to print
 * @param[in] num_elements - the number of records
 *
 *****************************************************************************/
void print_records_to_file(ofstream & fout, double scores[], char names[][81], int num_elements)
{
	int i;
	int j = 0;

	// show decimal part, to 3 decimal places
	fout << fixed << showpoint << setprecision(3);

	// print name :score for each entry
	for (i = 0; i < num_elements; i++)
	{
		// print a name
		while (names[i][j] != '\0' && j < 81)
		{
			fout << names[i][j];

			++j;
		}

		// print the score
		fout << " :" << scores[i] << endl;

		j = 0;
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Reads in a given number of names from a given file.
 *
 * @param[in] fin - stream for the file to read from
 * @param[in, out] names - holds the data once it's been read in
 * @param[in] num_elements - the number of names to read
 *
 *****************************************************************************/
void read_names(ifstream & fin, char names[][81], int num_elements)
{
	int i;

	// read the names
	for (i = 0; i < num_elements; i++)
		fin.getline( names[i], 81 );

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Reads in a given number of scores from a given file.
 *
 * @param[in] fin - the file stream to read from
 * @param[in, out] scores - holds the date once it's been read in
 * @param[in] num_elements - the number of scores to read
 *
 *****************************************************************************/
void read_scores(ifstream & fin, double scores[], int num_elements)
{
	int i;

	// read the scores
	for (i = 0; i < num_elements; i++)
		fin >> scores[i];

	return;
}