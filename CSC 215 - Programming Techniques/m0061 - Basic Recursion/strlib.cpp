/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the strlib function code.
 *****************************************************************************/

#include "strlib.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Recursively runs through 2 C-style strings to check if they are the same 
 * (case-insensitive). This function compares by checking a given index in 
 * the string.
 *
 * @param[in] cstr1 - the first string for comparison 
 * @param[in] cstr2 - the second string for comparison
 * @param[in] index - the index of the character to check in the string
 *
 * @returns 0 if the characters are the same or the difference in the
 * characters (char1 - char2) otherwise.
 *
 *****************************************************************************/
int recCStrCmp ( char* cstr1, char* cstr2, int index )
{
    static int cmp = 0;

    //test for the base case
    if ( cstr1[index] == '\0' && cstr2[index] == '\0' )
        return cmp;

    /*check if both characters are letters; 
     *if yes, set both to the same case before comparing*/
    if ( isalpha ( cstr1[index] ) && isalpha ( cstr2[index] ) )
        cmp = tolower ( cstr1[index] ) - tolower ( cstr2[index] );

    //compare non-alphabetic characters
    else
        cmp = cstr1[index] - cstr2[index];

    //recall this function with the next character in the string
    if ( cmp == 0 )
    {
        ++index;
        recCStrCmp ( cstr1, cstr2, index );
    }

    return cmp;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Recursively runs through 2 C-style strings to check if they are the same
 * (case-insensitive). This function compares using pointer dereferencing.
 *
 * @param[in] cstr1 - the first string for comparison 
 * @param[in] cstr2 - the second string for comparison
 *
 * @returns 0 if the characters are the same or the difference in the
 * first non-identical characters (char1 - char2) otherwise.
 *
 *****************************************************************************/
int recCStrCmp ( char* str1, char* str2 )
{
    static int cmp = 0;

    //test for the base case
    if ( *str1 == '\0' && *str2 == '\0' )
        return cmp;

    /*check if both characters are letters;
     *if yes, set both to the same case before comparing*/
    if ( isalpha ( *str1 ) && isalpha ( *str2 ) )
        cmp = tolower ( *str1 ) - tolower ( *str2 );
    
    //compare non-alphabetic characters
    else
        cmp = *str1 - *str2;

    //recall this function with the next character in the string
    if ( cmp == 0 )
    {
        str1++;
        str2++;
        recCStrCmp ( str1, str2 );
    }

    return cmp;
}