/** ***************************************************************************
 * @file  
 * 
 * @brief Contains function definitions for checking and outputting
 * employee record validity.
 *****************************************************************************/
#include "prog1.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes two dates and compares them to 
 * determine whether or not the first date is before the second date. Both
 * incoming dates are bitpacked, so this function calls the getDay, getMonth, 
 * and getYear functions prior to performing comparisons. 
 *
 * @param[in] lhsDate - bitpacked; contains day, month, and year values
 * @param[in] rhsDate - another bitpacked day, month, and year 
 *
 * @returns True - lhsDate is before rshDate
 * @returns False - lhsDate is after rshDate
 *
 *****************************************************************************/
bool beforeDate ( DATE lhsDate, DATE rhsDate )
{
    int lhsDay;
    int lhsMonth;
    int lhsYear;
    int rhsDay;
    int rhsMonth;
    int rhsYear;

    // pull out the date parts
    lhsDay = getDay ( lhsDate );
    lhsMonth = getMonth ( lhsDate );
    lhsYear = getYear ( lhsDate );

    rhsDay = getDay ( rhsDate );
    rhsMonth = getMonth ( rhsDate );
    rhsYear = getYear ( rhsDate );

    //earlier year
    if ( lhsYear < rhsYear )
        return true;

    // later year
    if ( lhsYear > rhsYear )
        return false;

    // same year
    if ( lhsMonth > rhsMonth )
        return false;

    if ( lhsDay < rhsDay )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes a struct and calls the valid functions
 * to determine if each member is valid. The validity of each member is 
 * noted in their respective valid functions. If a given member's value is
 * considered invalid, this function will add an appropriate error message 
 * into a vector of strings that will be printed by a different function.
 *
 * @param[in] data - the struct to check
 * @param[in, out] errors - a vector that holds all errors in the given struct
 *
 *****************************************************************************/
void checkValidity ( Record data, vector<string>& errors )
{
    bool valid;

    //make sure the vector is empty
    errors.clear ( );

    //test the name, add an error if invalid
    valid = validName ( data.name );
    if ( valid == false )
    {
        errors.push_back ( "Invalid character in name field" );
    }

    //test the address values, adding errors if invalid
    validFullAddress ( data, errors );

    //test the dates, adding errors if invalid
    validDates ( data, errors );

    //test the radio class and call sign, adding errors if invalid
    validClassCallSign ( data, errors );

    //make sure the dates are in valid orders, add an error if not
    valid = beforeDate ( data.birthDate, data.licenseDate );
    if ( valid == false )
    {
        errors.push_back ( "Birth date not before license date" );
    }

    valid = beforeDate ( data.licenseDate, data.expirationDate );
    if ( valid == false )
    {
        errors.push_back ( "License date not before expiration date" );
    }


    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function assumes an isopen() check has failed or the
 * files are being closed to exit the program. This function closes all
 * files and exits the program.
 *
 * @param[in] fin - an input file stream
 * @param[in] fout1 - an output file stream
 * @param[in] fout2 - another output file stream
 *
 *****************************************************************************/
void closeFiles ( ifstream& fin, ofstream& fout1, ofstream& fout2 )
{
    //close the files and exit
    fin.close ( );
    fout1.close ( );
    fout2.close ( );

    exit ( 0 );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a bitpacked unsigned integer and 
 * extracts a 4-digit zipcode. The bitpacked zipcode is made of 32 bits where
 * the lower 14 bits contain the 4-digit zipcode and the upper 18 contain the
 * 5-digit zipcode.
 *
 * @param[in] zip - bitpacked; contains both a four and five digit zipcode
 *
 * @returns The unpacked 4 digit zipcode
 *
 *****************************************************************************/
int get4DigitZip ( ZIPCODE zip )
{
    ZIPCODE mask = 16383;   // binary 0011 1111 1111 1111
    ZIPCODE zip4 = 0;       // zip code with 4 digits

    // extract the 4 digit zip
    zip4 = zip & mask;

    return zip4;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a bitpacked unsigned integer and 
 * extracts a 5-digit zipcode. The bitpacked zipcode is made of 32 bits where
 * the lower 14 bits contain the 4-digit zipcode and the upper 18 contain the
 * 5-digit zipcode. 
 *
 * @param[in] zip - bitpacked; contains both a four and five digit zipcode  
 *
 * @returns The unpacked 5 digit zipcode 
 * 
 *****************************************************************************/
int get5DigitZip ( ZIPCODE zip )
{
    ZIPCODE mask = 0;
    ZIPCODE zip5 = 0;       // zip code with 5 digits

    // shift the bits so the zip code can be extracted
    zip = zip >> 14;
    zip5 = zip | mask;

    return zip5;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a bitpacked unsigned integer and
 * extracts the day value. The bitpacked date is made of 32 bits where the 
 * lowest 6 bits contain the day, bits 6-9 contain the month, and bits 12-23 
 * contain the year. bits 10-11 and 24-31 are irrelevant and inconsistent, so
 * extracting the date values will account for these bits. 
 *
 * @param[in] date - Bitpacked; contains day, month, and year values
 *
 * @returns The unpacked day value
 *
 *****************************************************************************/
int getDay ( DATE date )
{
    DATE day = 0;
    DATE mask = 63;      // binary 0011 1111
   

    // extract the day
    day = date & mask;

    return day;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a bitpacked unsigned integer and
 * extracts the month value. The bitpacked date is made of 32 bits where the 
 * lowest 6 bits contain the day, bits 6-9 contain the month, and bits 12-23 
 * contain the year. bits 10-11 and 24-31 are irrelevant and inconsistent, so
 * extracting the date values will account for these bits.
 *
 * @param[in] date - bitpacked; contains day, month, and year values
 *  
 * @returns The unpacked month value
 *
 *****************************************************************************/
int getMonth ( DATE date )
{
    DATE mask = 15;      // binary 1111
    DATE month = 0;      // zip code with 5 digits

    // shift the bits so date can be extracted
    date = date >> 6;
    month = date & mask;

    return month;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a bitpacked unsigned integer and
 * extracts the year value. The bitpacked date is made of 32 bits where the 
 * lowest 6 bits contain the day, bits 6-9 contain the month, and bits 12-23 
 * contain the year. bits 10-11 and 24-31 are irrelevant and inconsistent, so
 * extracting the date values will account for these bits. 
 *
 *
 * @param[in] date - bitpacked; contains day, month, and year values
 *
 * @returns The unpacked year value
  *
 *****************************************************************************/
int getYear ( DATE date )
{
    DATE mask = 4095;   // binary 1111 1111 1111
    DATE year = 0;      // zip code with 5 digits

    // shift the bits so the date can be extracted
    date = date >> 12;
    year = date & mask;

    return year;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function opens files determined by argv. The
 * function also checks for successful file opening and will display an error
 * message, close the opened file(s), and exit the program if the check fails.
 *
 * @param[in] fin - an input file stream
 * @param[in] fout1 - an output file stream
 * @param[in] fout2 - another output file stream
 * @param[in] argv - dictates files to be opened/closed
 *
 *****************************************************************************/
void openFiles ( ifstream& fin, ofstream& fout1, ofstream& fout2,
    char**& argv )
{
    //open each file, check that it opened, call closeFiles if it didn't
    fin.open ( argv[1], ios::in | ios::binary );

    if ( !fin.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open input file: " << argv[1] << endl;

        closeFiles ( fin, fout1, fout2 );
    }


    fout1.open ( argv[2], ios::out | ios::trunc | ios::binary );

    if ( !fout1.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open output file: " << argv[2] << endl;

        closeFiles ( fin, fout1, fout2 );
    }


    fout2.open ( argv[3], ios::out | ios::trunc | ios::binary );

    if ( !fout2.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open output file: " << argv[3] << endl;

        closeFiles ( fin, fout1, fout2 );
    }


    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function prints all of the data from a employee data record struct, 
 * along with a line of dashes.
 *
 * @param[in] data - the struct to print
 *
 *****************************************************************************/
void printFile ( Record data )
{
    int bDay;       //birth day
    int bMonth;
    int bYear;
    int eDay;       //expiration day
    int eMonth;
    int eYear;
    int i;
    int lDay;       //license day
    int lMonth;
    int lYear;
    int zip4;       //4-digit zip code
    int zip5;       //5-digit zip code


    bDay = getDay ( data.birthDate );
    bMonth = getMonth ( data.birthDate );
    bYear = getYear ( data.birthDate );
    eDay = getDay ( data.expirationDate );
    eMonth = getMonth ( data.expirationDate );
    eYear = getYear ( data.expirationDate );
    lDay = getDay ( data.licenseDate );
    lMonth = getMonth ( data.licenseDate );
    lYear = getYear ( data.licenseDate );
    zip4 = get4DigitZip ( data.zipCode );
    zip5 = get5DigitZip ( data.zipCode );

    cout << setw ( 80 ) << setfill ( '-' ) << "-";
    cout << endl << "Name: \t \t";
    for ( i = 0; data.name[i] != '\0'; i++ )
        cout << data.name[i];

    cout << endl << "Address: \t \t";
    for ( i = 0; data.address[i] != '\0'; i++ )
        cout << data.address[i];

    cout << endl << "City, State, Zip: ";
    for ( i = 0; data.city[i] != '\0'; i++ )
        cout << data.city[i];

    cout << ", " << data.state[0] << data.state[1] << " ";

    if ( zip5 < 10000 )
        cout << "0";
    cout << zip5 << "-";

    if ( zip4 < 1000 )
        cout << "0";

    cout << zip4 << endl << "BirthDay: " << bMonth << "/" << bDay << "/" <<
        bYear << "\t" << "Licensing: " << lMonth << "/" << lDay << "/" <<
        lYear << " - " << eMonth << "/" << eDay << "/" << eYear << endl <<
        "Radio Class: " << data.radioClass << "\t Call Sign: ";

    for ( i = 0; i < 5; i++ )
        cout << data.callSign[i];

    cout << endl;
    cout << setw ( 80 ) << setfill ( '-' ) << "-" << endl;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a string holding an address and 
 * determines whether or not the address is valid. The address is considered 
 * valid if it only contains letters, numbers, periods, spaces, and pound 
 * signs
 *
 * @param[in] str - a c-style string that contains an address 
 *
 * @returns True - the address is valid
 * @returns False - the address is invalid
 *
 *****************************************************************************/
bool validAddress ( char* str )
{
    int i = 0;

    // run through the entire array
    while ( str[i] != '\0' )
    {
        // return false if an invalid character is found
        if ( ( isalpha ( str[i] ) == false ) && ( isdigit ( str[i] ) == false )
            && ( str[i] != '.' ) && ( str[i] != ' ' ) && ( str[i] != '#' ) )
            return false;

        i++;
    }

    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a one-character segment of a call sign (which 
 * should be the 1st character) and  determines whether or not it is 
 * valid. This segment is considered valid if the given character is 
 * 'k,' 'w,' or 'n' (case insensitive).
 *
 * @param[in] ch - one character that is part of a 5 character call sign
 *
 * @returns True - the character is valid
 * @returns False - the character is invalid
 *
 *****************************************************************************/
bool validCallSign1 ( char ch )
{
    ch = tolower ( ch );

    // check if the given character is a k, n, or w; return false if not
    if ( ch != 'k' && ch != 'n' && ch != 'w' )
        return false;

    else
        return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a call sign and determines
 * whether or not a segment of it is valid. This segment is considered
 * valid if the 2nd, 4th, and 5th characters of the call sign are letters.
 *
 * @param[in] arr - a character array that holds a 5 character call sign
 *
 * @returns True - the tested portion of the call sign is valid
 * @returns False - the tested portion of the call sign is invalid
 *
 *****************************************************************************/
bool validCallSign2 ( char* arr )
{
    /* check if the array contains letters at indexes 1, 3, and 4
     * return false if they do not*/
    if ( !isalpha ( arr[1] ) )
        return false;

    if ( !isalpha ( arr[3] ) )
        return false;

    if ( !isalpha ( arr[4] ) )
        return false;


    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a call sign and determines
 * whether or not a segment of it is valid. This segment is considered
 * valid if the 3rd character is a number.
 *
 * @param[in] arr - a character array that holds a 5 character call sign
 *
 * @returns True - the tested portion of the call sign is valid
 * @returns False - the tested portion of the call sign is invalid
 *
 *****************************************************************************/
bool validCallSign3 ( char* arr )
{
    // return false if the character at index 2 is not a number
    if ( !isdigit ( arr[2] ) )
        return false;


    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a call sign and determines
 * whether or not a segment of it is valid. This segment is considered
 * valid if the 1st, 2nd, 4th, and 5th characters of the call sign 
 * are uppercase letters.
 *
 * @param[in] arr - a character array that holds a 5 character call sign
 *
 * @returns True - the tested portion of the call sign is valid
 * @returns False - the tested portion of the call sign is invalid
 *
 *****************************************************************************/
bool validCallSign4 ( char* arr )
{
    // return false if the character at index 0, 1, 3, or 4 are not upper case
    if ( !isupper ( arr[0] ) )
        return false;

    if ( !isupper ( arr[1] ) )
        return false;

    if ( !isupper ( arr[3] ) )
        return false;

    if ( !isupper ( arr[4] ) )
        return false;

    return true;
}


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * part of checkValidity;
 * This function takes a struct and calls the valid functions
 * for the radio class and call sign. The validity of each member is
 * noted in their respective valid functions. If a given member's value is
 * considered invalid, this function will add an appropriate error message
 * into a vector of strings that will be printed by a different function.
 *
 * @param[in] data - the struct to check
 * @param[in, out] errors - a vector that holds all errors in the given struct
 *
 *****************************************************************************/
void validClassCallSign ( Record data, vector<string>& errors )
{
    bool valid;


    //test the radio class, add an error if invalid
    valid = validRadioClass ( data.radioClass );
    if ( valid == false )
    {
        errors.push_back ( "Invalid radio class" );
    }

    //test the call sign, add an error if invalid
    valid = validCallSign1 ( data.callSign[0] );
    if ( valid == false )
    {
        errors.push_back ( "Invalid 1st character in call sign" );
    }


    valid = validCallSign2 ( data.callSign );
    if ( valid == false )
    {
        errors.push_back ( "Invalid 2nd, 4th, or 5th character in call sign" );
    }

    valid = validCallSign3 ( data.callSign );
    if ( valid == false )
    {
        errors.push_back ( "Invalid digit in 3rd character of call sign" );
    }

    valid = validCallSign4 ( data.callSign );
    if ( valid == false )
    {
        errors.push_back
        ( "Alphabetic characters in call sign not capitalized" );
    }


    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * part of checkValidity;
 * This function takes a struct and calls the valid functions
 * for the day, month, and year for a birthdate, license date, and expiration
 * date. The validity of each member is noted in their respective
 * valid functions. If a given member's value is
 * considered invalid, this function will add an appropriate error message
 * into a vector of strings that will be printed by a different function.
 *
 * @param[in] data - the struct to check
 * @param[in, out] errors - a vector that holds all errors in the given struct
 *
 *****************************************************************************/
void validDates ( Record data, vector<string>& errors )
{
    bool valid;


    //test the birthday values, adding errors if invalid
    valid = validMonth ( data.birthDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid month in birth date" );
    }

    valid = validDay ( data.birthDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid day in birth date" );
    }

    valid = validYear ( data.birthDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid year in birth date" );
    }

    //test the license date values, adding errors if invalid
    valid = validMonth ( data.licenseDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid month in license date" );
    }

    valid = validDay ( data.licenseDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid day in license date" );
    }

    valid = validYear ( data.licenseDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid year in license date" );
    }

    //test the expiration date values, adding errors if invalid
    valid = validMonth ( data.expirationDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid month in expiration date" );
    }
    valid = validDay ( data.expirationDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid day in expiration date" );
    }

    valid = validYear ( data.expirationDate );
    if ( valid == false )
    {
        errors.push_back ( "Invalid year in expiration date" );
    }


    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes a date and determines whether or 
 * not the given day is valid (between 1 and 30 or 31 depending on the month).
 * The incoming date is bitpacked, so this function calls the getDay function.
 * Because the validity of the day depends on the month, this function calls 
 * the getMonth and validMonth functions. This program ignores leap years, 
 * so a 29th day is never considered valid in February. If the month is 
 * invalid, the day is valid as long as it is between 1 and 31 inclusive. 
 *
 * @param[in] date - Bitpacked; contains day, month, and year values
 *
 * @returns True - the day is valid
 * @returns False - the day is invalid
 *
 *****************************************************************************/
bool validDay ( DATE date )
{
    DATE day;
    DATE month;

    // extract the month and day
    month = getMonth ( date );
    day = getDay ( date );

    // return false if the day is above 31
    if ( day > 31 )
        return false;

    // return false if the day is below 1
    if ( day < 1 )
        return false;

    // if the month is invalid, the day is valid if it's below 32 and above 0
    if ( !validMonth ( month ) )
        return true;

    // check the validity of the day based on the valid months
    // february 1-28, never counting leap days
    if ( month == 2 )
    {
        if ( day < 29 )
            return true;
    }

    // April, June, September, and November 1-30
    if ( month == 4 || month == 6 || month == 9 || month == 11 )
    {
        if ( day < 31 )
            return true;
    }

    // other valid months, already tested for above 0 and below 32
    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * part of checkValidity;
 * This function takes a struct and calls the valid functions for the city 
 * address, the city, the state, and the zipcodes. The validity of each member 
 * is noted in their respective valid functions. If a given member's value is
 * considered invalid, this function will add an appropriate error message
 * into a vector of strings that will be printed by a different function.
 *
 * @param[in] data - the struct to check
 * @param[in, out] errors - a vector that holds all errors in the given struct
 *
 *****************************************************************************/
void validFullAddress ( Record data, vector<string>& errors )
{
    bool valid;

    valid = validAddress ( data.address );
    if ( valid == false )
    {
        errors.push_back ( "Invalid character in address field" );
    }

    valid = validName ( data.city );
    if ( valid == false )
    {
        errors.push_back ( "Invalid character in city field" );
    }

    valid = validState ( data.state );
    if ( valid == false )
    {
        errors.push_back ( "Invalid state code" );
    }

    valid = validStateCase ( data.state[0] );
    if ( valid == false )
    {
        errors.push_back ( "State code's 1st character not capitalized" );
    }

    //test the zipcodes, add an error if invalid
    valid = validZip5 ( data.zipCode );
    if ( valid == false )
    {
        errors.push_back ( "Invalid 5-digit zip code" );
    }

    valid = validZip4 ( data.zipCode );
    if ( valid == false )
    {
        errors.push_back ( "Invalid 4-digit zip code" );
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a date and determines whether or 
 * not the given month is valid (between 1 and 12 inclusive). The incoming date
 * is bitpacked, so this function calls the getMonth function.
 * 
 * @param[in] date - Bitpacked; contains day, month, and year values
 *
 * @returns True - the month is valid
 * @returns False - the month is invalid
 *
 *****************************************************************************/
bool validMonth ( DATE date )
{
    DATE month;

    // extract the month
    month = getMonth ( date );

    // check if the month is valid
    if ( month > 0 && month < 13 )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a string containing a name and 
 * determines whether or not the name is valid. A name is considered valid if
 * it only contains letters, periods, and spaces.
 *
 * @param[in] str - A c-style string that contains a name
 *
 * @returns True - the name is valid
 * @returns False - the name is invalid
 *
 *****************************************************************************/
bool validName ( char* str )
{
    int i = 0;

    // run through the string and check if each character is valid for a name
    while ( str[i] != '\0' )
    {
        if ( ( isalpha ( str[i] ) == false) && ( str[i] != '.' ) 
            && ( str[i] != ' ' ) )
            return false;

        i++;
    }

    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a radio class, represented by a single
 * character, and determines whether or not it is valid. The class is valid if
 * the character is 'a,' 'g,' 'n,' 'p,' 't,' or 'x' (case insensitive).
 *
 * @param[in] ch - a character that indicates an operator's class
 *
 * @returns True - the operator class is valid
 * @returns False - the operator class is invalid
 *
 *****************************************************************************/
bool validRadioClass ( char ch )
{
    char temp;
    
    // return false if the character is not a letter
    if ( !isalpha ( ch ) )
        return false;

    // check if the character is valid for the given parameter
    temp = tolower ( ch );

    if (   ( temp == 'a' ) || ( temp == 'g' ) || ( temp == 'n' )
        || ( temp == 'p' ) || ( temp == 't' ) || ( temp == 'x' ) )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a two character character array and 
 * determines whether or not the two characters form a valid US state postal
 * code. This function only accounts for the codes of the 50 states, no
 * territories or anything similar. 
 *
 * @param[in] arr - a character array that contains 2 character state code
 *
 * @returns True - the state code is valid
 * @returns False - the state code is invalid
 *
 *****************************************************************************/
bool validState ( char* arr )
{
    char temp1;
    char temp2;

    // copy array values so the original array isn't corrupted
    temp1 = tolower ( arr[0] );
    temp2 = tolower ( arr[1] );

    // check states starting with A
    if ( temp1 == 'a' )
    {
        if (   ( temp2 == 'k' ) || ( temp2 == 'l' )
            || ( temp2 == 'r' ) || ( temp2 == 'z' ) )
            return true;
    }

    // check states starting with C
    if ( temp1 == 'c' )
    {
        if (   ( temp2 == 'a' ) || ( temp2 == 'o' )
            || ( temp2 == 't' ) )
            return true;
    }

    // check states starting with D
    if ( ( temp1 == 'd' ) && ( temp2 == 'e' ) )
        return true;

    // check states starting with F
    if ( ( temp1 == 'f' ) && ( temp2 == 'l' ) )
        return true;

    // check states starting with G
    if ( ( temp1 == 'g' ) && ( temp2 == 'a' ) )
        return true;

    // check states starting with H
    if ( ( temp1 == 'h' ) && ( temp2 == 'i' ) )
        return true;

    // check states starting with I
    if ( temp1 == 'i' )
    {
        if (   ( temp2 == 'a' ) || ( temp2 == 'd' )
            || ( temp2 == 'l' ) || ( temp2 == 'n' ) )
            return true;
    }

    // check states starting with K
    if (   ( temp1 == 'k' ) && ( temp2 == 's' ) 
        || ( temp1 == 'k' ) && ( temp2 == 'y' ) )
        return true;

    // check states starting with L
    if ( ( temp1 == 'l' ) && ( temp2 == 'a' ) )
        return true;

    // check states starting with M
    if ( temp1 == 'm' )
    {
        if (   ( temp2 == 'a' ) || ( temp2 == 'd' )
            || ( temp2 == 'e' ) || ( temp2 == 'i' )
            || ( temp2 == 'n' ) || ( temp2 == 'o' )
            || ( temp2 == 's' ) || ( temp2 == 't' ) )
            return true;
    }

    // check states starting with N
    if ( temp1 == 'n' )
    {
        if (   ( temp2 == 'c' ) || ( temp2 == 'd' )
            || ( temp2 == 'e' ) || ( temp2 == 'h' )
            || ( temp2 == 'j' ) || ( temp2 == 'm' )
            || ( temp2 == 'v' ) || ( temp2 == 'y' ) )
            return true;
    }

    // check states starting with O
    if ( temp1 == 'o' )
    {
        if (   ( temp2 == 'h' ) || ( temp2 == 'k' )
            || ( temp2 == 'r' ) )
            return true;
    }

    // check states starting with P
    if ( ( temp1 == 'p' ) && ( temp2 == 'a' ) )
        return true;

    // check states starting with R
    if ( ( temp1 == 'r' ) && ( temp2 == 'i' ) )
        return true;

    // check states starting with S
    if (   ( temp1 == 's' ) && ( temp2 == 'c' )
        || ( temp1 == 's' ) && ( temp2 == 'd' ) )
        return true;

    // check states starting with T
    if (   ( temp1 == 't' ) && ( temp2 == 'n' )
        || ( temp1 == 't' ) && ( temp2 == 'x' ) )
        return true;

    // check states starting with U
    if ( ( temp1 == 'u' ) && ( temp2 == 't' ) )
        return true;

    // check states starting with V
    if (   ( temp1 == 'v' ) && ( temp2 == 'a' )
        || ( temp1 == 'v' ) && ( temp2 == 't' ) )
        return true;

    // check states starting with W
    if ( temp1 == 'w' )
    {
        if (   ( temp2 == 'a' ) || ( temp2 == 'i' )
            || ( temp2 == 'v' ) || ( temp2 == 'y' ) )
            return true;
    }

    return false;
}




/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes a character that represents the 1st
 * character of a state code and determines whether or not it is valid. The
 * character is considered valid if it is is uppercase
 *
 * @param[in] ch - the 1st character of an array that holds a state code
 *
 * @returns True - the character is valid
 * @returns False - the character is invalid
 *
 *****************************************************************************/
bool validStateCase ( char ch )
{
    if ( !isalpha ( ch ) )
        return false;

    if ( !isupper ( ch ) )
        return false;

    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a date and determines whether or 
 * not the given year is valid. For this program, a valid year is between
 * 1900 and 2021 inclusive. The incoming date is bitpacked, so this function
 * calls the getYear function.
 *
 * @param[in] date - Bitpacked; contains day, month, and year values
 *
 * @returns True - the year is valid
 * @returns False - the year is invalid
 *
 *****************************************************************************/
bool validYear ( DATE date )
{
    DATE year;

    // extract the year
    year = getYear ( date );

    // check if the year is valid
    if ( year > 1899 && year < 2022 )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a zipcode and determines whether or
 * not the given 4-digit zipcode is valid. For this program, a valid zipcode
 * is between 1000 and 9999 inclusive. The incoming zipcode is bitpacked, so
 * this functions calls the get4DigitZip function. 
 *
 * @param[in] zipcode - Bitpacked; contains both a four and five digit zipcode
 *
 * @returns True - the 4 digit zipcode is valid
 * @returns False - the 4 digit zipcode is invalid
 *
 *****************************************************************************/
bool validZip4 ( ZIPCODE zipcode )
{
    int testZip;

    // check if the zipcode is in the valid range
    testZip = get4DigitZip ( zipcode );
    if ( testZip > 999 && testZip < 10000 )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * This function takes a zipcode and determines whether or
 * not the given 5-digit zipcode is valid. For this program, a valid zipcode
 * is between 10000 and 99999 inclusive. The incoming zipcode is bitpacked, so
 * this functions calls the get5DigitZip function. 
 *
 * @param[in] zipcode - Bitpacked; contains both a four and five digit zipcode
 *
 * @returns True - the 5 digit zipcode is valid
 * @returns False - the 5 digit zipcode is invalid
 *
 *****************************************************************************/
bool validZip5 ( ZIPCODE zipcode )
{
    int testZip;

    // check if the zipcode is in the valid range
    testZip = get5DigitZip ( zipcode );
    if ( testZip > 9999 && testZip < 100000 )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes a vector of strings and a structure of data. If the
 * vector is empty, this function will write the struct's data to a binary
 * file for data without errors. If the vector is not empty, the struct's
 * contents will be written to a binary file for invalid data and print the
 * specific errors in the struct's data to the screen.
 *
 * @param[in] fout1 - an ofstream opened to a binary file for structs that do
 * not contain any errors
 * @param[in] fout2 - an ofstream opened to a binary file for structs that
 * have errors
 * @param[in] errors - a vector of strings that holds a struct's errors
 * @param[in] data - a struct holding data to be written to a binary file
 *
 *****************************************************************************/
void writeFiles ( ofstream& fout1, ofstream& fout2, vector<string> errors,
    Record data )
{
    int i;

    if ( errors.size ( ) == 0 )
        fout1.write ( ( char* ) &data, int ( sizeof ( Record ) ) );

    else
    {
        //write the struct into the error binary file
        fout2.write ( ( char* ) &data, int ( sizeof ( Record ) ) );

        //print the error struct to the screen
        printFile ( data );

        //print the errors to the screen
        for ( i = 0; i < errors.size ( ); i++ )
        {
            cout << "\t \t" << errors[i] << endl;
        }
    }

    return;
}