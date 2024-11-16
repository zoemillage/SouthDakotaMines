/** ***************************************************************************
* @file
* 
* @mainpage m0051 - Basic Vectors
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
* This program takes in a numeric data file and reads it into a vector 
* Then, it outputs the number of data items, minimum and maximum values, and
* the data to another file.
* Data is output in the format
* number of elements
* minimum - maximum
* 15 - 0 spaces, number with 3 decimal places; 5 per row
* empty line.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0051.exe inputFile outputFile
*
******************************************************************************/

#include <algorithm>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <vector>

using namespace std;

int main ( int argc, char* argv[] )
{
    ifstream fin;
    ofstream fout;

    int checking = 0;      /*will be used to check for the size for the 
                            *vector and when to output an endl*/
    int size;

    vector<float> data;

    vector<float>::iterator it;
    vector<float>::iterator max;
    vector<float>::iterator min;


    //error check command line arguments
    if ( argc != 3 )
    {
        cout << "Usage: m0051.exe inputfile outputfile" << endl;

        return 0;
    }

    //open files and error check
    fin.open ( argv[1] );

    if ( !fin.is_open ( ) )
    {
        cout << "Unable to open input file: " << argv[1] << endl;

        fin.close ( );

        return 0;
    }


    fout.open ( argv[2] );

    if ( !fout.is_open ( ) )
    {
        cout << "Unable to open output file: " << argv[2] << endl;

        fin.close ( );
        fout.close ( );

        return 0;
    }

    //get the size of the file and resize the vector
    fin >> size;
    data.resize ( size );

    //read the file
    it = data.begin ( );
    while ( it != data.end ( ) && fin >> *it )
    {
        it++;
        checking++;
    }

    //check to see if indicated size and real size of files match
    if ( size != checking )
    {
        size = checking;
        data.resize ( size );
    }

    //find the max and min
    max = max_element ( data.begin ( ), data.end ( ) );
    min = min_element ( data.begin ( ), data.end ( ) );

    //set the decimal precision for the file; output the min and max
    fout << fixed << showpoint << setprecision ( 3 );
    fout << size << endl << *min << " - " << *max << endl;
    fout << right;

    //reset checking to be used for endl conditions
    checking = -1;

    //print data points with a setw of 15 with spaces 
    for ( it = data.begin ( ); it != data.end ( ); it++ )
    {
        checking++;

        fout << setw ( 15 ) << setfill ( ' ' );
        fout << *it;

        //set a condition for printing endl
        if ( checking != 0 && ( checking + 1 ) % 5 == 0 )
            fout << endl;
    }

    //check to see if an endl needs to be printed after the last data point
    if ( (checking + 1) % 5 != 0 )
        fout << endl;

    fin.close ( );
    fout.close ( );

    return 0;
}