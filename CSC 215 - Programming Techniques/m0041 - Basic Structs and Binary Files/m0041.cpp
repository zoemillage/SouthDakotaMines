/** ***************************************************************************
* @file
* 
* @mainpage m0041 - Basic Structs and Binary Files
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
* This program reads a binary file as a list of structs of employee data, 
* prints the employee data, outputs whether or not a given employee id was
* found, applies a given raise to the applicable employee, and prints 
* the (possibly) updated employee data.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0041.exe binaryData employeeID salaryRaise
*
******************************************************************************/

#include <cmath>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <stdio.h>

using namespace std;


/** 
* @brief the storage container for each employee's data
*/
struct empData
{
    int id;             /**< The employee's unique id */
    char firstName[20]; /**< The employee's first name */
    char lastName[40];  /**< The employee's last name*/
    double salary;      /**< The employee's current salary */
    double bonus;       /**< The employee's yearly bonus */
};



//function prototypes
bool applyRaise ( fstream& file, int employeeID, float salaryRaise );
void printFile ( fstream& file );



int main ( int argc, char* argv[] )
{
    bool idFound = false;

    char* extraData;	//used for strtof

    float bonus;

    fstream file;

    int employeeID;


    //error check command line arguments
    if ( argc != 4 )
    {
        cout << "Usage: m0041.exe binaryData employeeID salaryRaise" << endl;

        return 0;
    }

    //open the file and error check opening
    file.open ( argv[1], ios::in | ios::out | ios::ate | ios::binary );

    if ( !file.is_open ( ) )
    {
        cout << "Unable to open binary file: " << argv[1] << endl;

        file.close ( );

        return 0;
    }

    //convert the necessary data
    employeeID = atoi ( argv[2] );
    bonus = strtof ( argv[3], &extraData );

    //print the file
    printFile ( file );
    idFound = applyRaise ( file, employeeID, bonus );

    //print the result of applyRaise
    if ( idFound == true )
        cout << "Employee ID " << employeeID << " has been updated." << endl
        << endl;

    else
        cout << "Employee ID " << employeeID << " was not found." << endl
        << endl;

    //print the file again
    printFile ( file );

    //close the file
    file.close ( );

    return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Runs through a binary file of employee data, applying the given raise to
 * the employee of the given employee id.
 *
 * @param[in, out] file - the file containing employee data
 * @param[in] employeeID - the id of the employee to give a raise
 * @param[in] salaryRaise - the raise to give to the employee
 *
 * @returns true - the raise was applied
 * @returns false - the employee with the given id was not found
 *
 *****************************************************************************/
bool applyRaise ( fstream& file, int employeeID, float salaryRaise )
{
    empData employee;

    int count = 0;


    //output an endl for formatting
    cout << endl;

    //read the file while checking for the given employeeID
    while ( file.read ( ( char* ) &employee, sizeof ( empData ) ) )
    {
        count++;

        //apply the employee's salary raise if their data is found
        if ( employee.id == employeeID )
        {
            employee.salary = floor ( employee.salary + salaryRaise );

            file.seekp ( ( ( long long int )count - 1 ) *
                sizeof ( empData ), ios::beg );

            file.write ( ( char* ) &employee, int ( sizeof ( empData ) ) );

            //clear potential error flags and return to file's beginning
            file.clear ( );
            file.seekg ( 0, ios::beg );

            return true;
        }

    }

    //clear error flags and return to file's beginning
    file.clear ( );
    file.seekg ( 0, ios::beg );

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints the contents of a binary file of employee data in the format
 * 0-7 spaces, employee id, 1 space, 
 * employee first name, 1-21 spaces, employee last name, 1-41 spaces, 
 * "Salary: ", 0-10 spaces, employee salary, 
 * " Bonus: ", 0-10 spaces, employee bonus. 
 * 1 entry is printed per line.
 *
 * @param[in, out] file - the binary file containing the employee data 
 *
 *****************************************************************************/
void printFile ( fstream& file )
{
    empData employee;


    //make sure file is read from the beginning
    file.seekg ( 0, ios::beg );

    //print the file
    while ( file.read ( ( char* ) &employee, sizeof ( empData ) ) )
    {
        cout << fixed << showpoint << setprecision ( 2 )
             << setw ( 7 ) << employee.id << " "
             << left << setw ( 20 ) << employee.firstName
             << setw ( 40 ) << employee.lastName << right
             << " Salary: " << setw ( 10 ) << employee.salary
             << " Bonus: " << setw ( 10 ) << employee.bonus << endl;
    }

    //clear error flags and return to file's beginning
    file.clear ( );
    file.seekg ( 0, ios::beg );

    return;
}