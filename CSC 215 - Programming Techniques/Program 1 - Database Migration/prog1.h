/** ***************************************************************************
 * @file  
 * 
 * @brief Declares the structs, and typedefs, functions for checking and 
 * outputting employee record validity. 
 *****************************************************************************/
#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <iomanip>
#include <fstream>
#include <cstring>
#include <cctype>
#include <sstream>
#include <vector>


using namespace std;

/******************************************************************************
 *             Constant Variables, defines and Enums
 *****************************************************************************/
#ifndef __PROG1__H__
#define __PROG1__H__


// put typedef statements here
/**
* @brief Holds a bitpacked date
*/
typedef unsigned int DATE;

/**
* @brief Holds a bitpacked zipcode
*/
typedef unsigned int ZIPCODE;


#pragma pack(push)
#pragma pack(1)
/**
* @brief Holds a given employee's records
*/
struct Record
{
    char name[30];          /**< Holds the employee's name */
    char address[30];       /**< The employee's street address */
    char city[28];          /**< The employee's city of residence */
    char state[2];          /**< The employee's state of residence */
    ZIPCODE zipCode;        /**< The employee's address zipcode */
    DATE birthDate;         /**< The employee's birth date */
    DATE licenseDate;       /**< The employee's first valid license date */
    DATE expirationDate;    /**< The employee's license expiration date */
    char radioClass;        /**< The employee's class */
    char callSign[5];       /**< The employee's call sign */
};
#pragma pack(pop)



/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
bool beforeDate ( DATE lhsDate, DATE rhsDate );
void checkValidity ( Record data, vector<string>& errors );
void closeFiles ( ifstream& fin, ofstream& fout1, ofstream& fout2 );
int get4DigitZip ( ZIPCODE zip );
int get5DigitZip ( ZIPCODE zip );
int getDay ( DATE date );
int getMonth ( DATE date );
int getYear ( DATE date );
void openFiles ( ifstream& fin, ofstream& fout1, ofstream& fout2,
    char**& argv );
void printFile ( Record data );
bool validAddress ( char* str );
bool validCallSign1 ( char ch );
bool validCallSign2 ( char* arr );
bool validCallSign3 ( char* arr );
bool validCallSign4 ( char* arr );
void validClassCallSign ( Record data, vector<string>& errors );
void validDates ( Record data, vector<string>& errors );
bool validDay ( DATE date );
void validFullAddress ( Record data, vector<string>& errors );
bool validMonth ( DATE date );
bool validName ( char* str );
bool validRadioClass ( char ch );
bool validState ( char* arr );
bool validStateCase ( char ch );
bool validYear ( DATE date );
bool validZip4 ( ZIPCODE zipcode );
bool validZip5 ( ZIPCODE zipcode );
void writeFiles ( ofstream& fout1, ofstream& fout2, vector<string> errors,
    Record data );

#endif
