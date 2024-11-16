/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the main function, which runs catch, checks 
 * argc values, and initiates the record validity checks and result output.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage Program 1 - Database Migration
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
* This program simulates a database migration wherein every employee's data
* is stored in a struct and packed into a binary file. This program reads the
* data from the binary file into a struct then checks that each member of the 
* struct is able to be transferred to a new database. If the whole struct is
* transferrable, it will be written into a binary file with other structs 
* without errors. If any member of the struct is invalid, the struct will be
* written to the screen along with errors messages showing what is wrong with
* the data. Conditions detailing what qualifies as "valid" for any given
* struct member cna be found in their respective "valid<member>" type 
* functions. 
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     prog1.exe inputfile noerrorfile errorfile
*
******************************************************************************/


#define CATCH_CONFIG_RUNNER

#include "catch.hpp"
#include "prog1.h"
#include "prog1a.cpp"
#include "prog1Given.cpp"


/******************************************************************************
 *             Constant Variables, defines and Enums
 *****************************************************************************/
/** 
* @brief Tells the program to run catch
*/
const bool RUNCATCH = true;


/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
void makeCATCH(int &myargc, char **&myargv);



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This is the starting point of the program. This will test for command line
 * arguments, open the input file and output files (exiting if file opening is
 * unsuccessful), and read in a struct from the input binary file. Then, main
 * will call functions that will examine the data to determine whether or not
 * the data is valid. A function will then be called to write the data to one 
 * of two new binary files; one for structs without errors and another for
 * structs that need to be changed to become valid. The files are then closed
 * and the program is exited. 
 *
 * @param[in] argc - The number of arguments for the prog1; should equal 4
 * @param[in] argv - Array that holds the names of the input and output files
 *
 * @returns 0 - The program ran successfully
 * 
 *****************************************************************************/
int main(int argc, char** argv)
{
    // declare variables here
    ifstream fin;

    ofstream fout1;
    ofstream fout2;

    Record data;

    vector<string> errors;

    //////////////////////////////////// Do not edit this section
    int result = 1;
    int myargc;
    char** myargv;
    if (RUNCATCH)
    {
        makeCATCH(myargc, myargv);
        result = Catch::Session().run(myargc, myargv);
        if (result != 0 )
        {
            cout << "You have errors in your functions" << endl;
            return 0;
        }
    }
    //////////////////////////////////// Place your code here

    //error check command line arguments
    if ( argc != 4 )
    {
        cout << "Usage: prog1.exe inputfile noerrorfile errorfile" << endl;

        return 0;
    }

    //open files and error check
    openFiles ( fin, fout1, fout2, argv );


    //make sure file is read from the beginning
    fin.seekg ( 0, ios::beg );

    //read a struct
    while ( fin.read ( ( char* ) &data, sizeof ( Record ) ) )
    {
        //check validity of each struct member
        checkValidity ( data, errors );

        writeFiles ( fout1, fout2, errors, data );

        //output endls to separate different structs and errors
        if ( errors.size ( ) != 0 )
            cout << endl;

    }

    //close the files
    closeFiles ( fin, fout1, fout2 );

    return 0;
}