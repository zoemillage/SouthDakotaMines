/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the strLib functions.
 *****************************************************************************/

#include "strLib.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes in a string, a string segment to find, and replaces all instances of 
 * the segment with a different string segment.
 * In this version, all of the strings are stored as C-strings. 
 *
 * @param[in, out] str - the string to modify
 * @param[in] lookFor - the string to search for
 * @param[in] replaceWith - the string to replace all "LookFor"s with
 *
 *****************************************************************************/
void replaceAllCString(char* cstr, char* lookFor, char* replaceWith)
{
    char* found = nullptr;      //pointer to location of desired word/phrase
    char* modF = nullptr;       //"modified found"
    char* src = cstr;           //starting point of search
    char* tempstr = nullptr;    //pointer for temporary string

    size_t size;                //size of the temporary array


    //look for the desired word/phrase
    found = strstr ( cstr, lookFor );

    //only replace if the word/phrase is found
    while ( found != nullptr )
    {
        /*get the size of an array to hold everything after found's location
        * also, account for ReplaceWith's size and the null terminator*/
        if ( strlen ( lookFor ) > strlen ( replaceWith ) )
            size = strlen ( found ) + 1;

        else
            size = strlen ( found ) + strlen ( replaceWith ) + 1;

        //allocate a temporary dynamic array
        tempstr = new ( nothrow ) char[size];

        //error check for memory
        if ( tempstr == nullptr )
            exit ( 0 );

        //copy everything after lookFor into an array and modify it
        modF = found + strlen ( lookFor );
        strcpy ( tempstr, replaceWith );
        strcat ( tempstr, modF );

        //replace the undesired word/phrase
        strcpy ( found, tempstr );

        //free the dynamic array
        delete[] tempstr;
        tempstr = nullptr;

        //move the search pointer
        size = strlen ( replaceWith );
        src = found + size;

        //search again for the undesired word/phrase
        found = strstr ( src, lookFor );
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes in a string, a string segment to find, and replaces all instances of 
 * the segment with a different string segment.
 * In this version, all of the strings are stored as strings. 
 *
 * @param[in, out] str - the string to modify
 * @param[in] lookFor - the string to search for
 * @param[in] replaceWith - the string to replace all "LookFor"s with
 *
 *****************************************************************************/
void replaceAllString(string& str, string lookFor, string replaceWith)
{
    size_t found;

    string src = str;       //starting point of search


    //search for the undesired word/phrase
    found = src.find ( lookFor );

    //only replace if the word/phrase is found
    while ( found != string::npos )
    {
        //replace the undesired word/phrase
        str.replace ( found, lookFor.size ( ), replaceWith );

        //update the search range; search again for the undesired word/phrase
        src = str;
        found += replaceWith.size ( );
        found = src.find ( lookFor, found );
    }

    return;
}