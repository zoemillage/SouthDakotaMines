/** ***************************************************************************
* @file
* 
* @mainpage m0020 - Basic Dynamic Arrays
* 
* @section course_section Course Information
*
* @author Zoe Millage
*
* @date Spring 2021
*
* @par Instructor
*     Roger Schrader
*
* @par Course
*     CSC 215 - Programming Techniques
*
* @section program_section Program Information
*
* @details
* This program takes in a numeric data file to dynamically allocate an array to 
* fit the amount of data specified in the file and read in the data itself. 
* Then, it outputs the actual number of data items (if smaller than 
* the value specified in the input file), minimum and maximum values, and
* the data to another file. 
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0020.exe inputFile outputFile
*
******************************************************************************/

#include <fstream>
#include <iomanip>
#include <iostream>


using namespace std;



void allocated1darr(float*& arrptr, int size);
float* findMaximum( float* arr, int size );
float* findMinimum( float* arr, int size );
void outputToFile( float* arr, float* max, float* min, int size, 
                   ofstream& fout );
void readFile( float arr[], ifstream& fin, int& size );



int main( int argc, char* argv[] )
{
    float* arrptr = nullptr;
    float* maxptr = nullptr;
    float* minptr = nullptr;

    ifstream fin;
    ofstream fout;

    int size = 0;

    //error check command line arguments
    if (argc != 3)
    {
        cout << "Usage: m0020.exe inputfile outputfile" << endl;

        return 0;
    }

    //open files and error check
    fin.open( argv[1] );

    if ( !fin.is_open() )
    {
        cout << "Unable to open input file: " << argv[1] << endl;

        fin.close();

        return 0;
    }


    fout.open(argv[2]);

    if ( !fout.is_open() )
    {
        cout << "Unable to open output file: " << argv[2] << endl;

        fin.close();
        fout.close();

        return 0;
    }

    //get the size of the file
    fin >> size;

    //create a dynamic array
    allocated1darr( *&arrptr, size );

    //error check for array memory space
    if (arrptr == nullptr)
    {
        cout << "Unable to allocate memory" << endl;

        //close files if check fails
        fin.close();
        fout.close();

        return 0;
    }

    //call a function to read from the input file
    readFile( arrptr, fin, size );

    //call functions to get the max and min
    maxptr = findMaximum(arrptr, size);
    minptr = findMinimum(arrptr, size);

    //output to the output file
    outputToFile( arrptr, maxptr, minptr, size, fout );

    //delete the array to free up memory
    delete[] arrptr;

    //close the files
    fin.close();
    fout.close();

    return 0;
}




/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Allocates a 1 dimension array of the given size.
 *
 * @param[in, out] arrptr - the pointer to the allocated array
 * @param[in] size - the size of the array
 *
 *****************************************************************************/
void allocated1darr( float*& arrptr, int size )
{
    //allocate a dynamic array
        arrptr = new ( nothrow) float[size]; 

        return;
 }



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Tries to read the given number of datapoints from a file into an array,
 * updating the size if it is smaller than the given. 
 *
 * @param[in, out] arr - holds the data 
 * @param[in] fin - a stream for the file to read from
 * @param[in, out] size - the number of elements read
 *
 *****************************************************************************/
void readFile(float arr[], ifstream& fin, int& size)
{
    int i = 0;       //loop variable

    //check for adequate space in the array, then fill it
    while (i < size && fin >> arr[i] )
        i++;

    //check to see if indicated size and real size of files match
    if ( size != i )
        size = i;

    return;
}




/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Runs through an array to find the maximum value.
 *
 * @param[in] arr - the data array
 * @param[in] size - the size of the array
 *
 * @returns the maximum value in the array
 *
 *****************************************************************************/
float* findMaximum( float* arr, int size )
{
    float* maxptr = nullptr;

    int i;
    int max = 0;

    //compare each data point to determine the maximum
    for (i = 0; i < size; i++)
    {
        if (arr[max] < arr[i])
            max = i;
    }

    //set maxptr to the address of the maximum valued data point 
    maxptr = &arr[max];

    return maxptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Runs through an array to find the minimum value.
 *
 * @param[in] arr - the data array
 * @param[in] size - the size of the array
 *
 * @returns the minimum value in the array
 *
 *****************************************************************************/
float* findMinimum( float* arr, int size )
{
    float* minptr = nullptr;

    int i;
    int min = 0;

    //compare each data point to determine the minimum
    for (i = 0; i < size; i++)
    {
        if (arr[min] > arr[i])
            min = i;
    }

    //set minptr to the address of the minimum valued data point
    minptr = &arr[min];

    return minptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Outputs data to a file in the format
 * size
 * minimum - maximum
 * 15 - 0 spaces, number with 3 decimal places; 5 per row
 * empty line.
 * 
 * @param[in] arr - an array of the data
 * @param[in] max - the maximum value
 * @param[in] min - the minimum value
 * @param[in] size - the amount of data entries
 * @param[in, out] fout - a stream for outputting to a file
 *
 *****************************************************************************/
void outputToFile(float* arr, float* max, float* min, int size,
                  ofstream& fout)
{
    int i;

    //set the decimal precision, print min/max, and set justification to right
    fout << fixed << showpoint << setprecision(3);
    fout << size << endl << *min << " - " << *max << endl;
    fout << right;

    //print data points with a setw of 15 with spaces 
    for (i = 0; i < size; i++)
    {
        fout << setw(15) << setfill(' ');
        fout << arr[i];

        //set a condition for printing endl
        if ( i != 0 && ( i + 1)  % 5 == 0 )
          
        fout << endl;
    }

    //check to see if an endl needs to be printed after the last data point
    if ( i % 5 != 0 )
        fout << endl;

    return;
}