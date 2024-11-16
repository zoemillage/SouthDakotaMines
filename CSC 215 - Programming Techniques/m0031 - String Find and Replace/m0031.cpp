/** ***************************************************************************
 * @file  
 * 
 * @brief Contains test cases.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage m0031 - String Find and Replace
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
* This program contains find-and-replace-all functions, like in a word 
* processing program. It can find and replace using string-library
* strings, or with C-style strings.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0031.exe
*
******************************************************************************/

#define CATCH_CONFIG_MAIN
#include "catch.hpp"
#include "strLib.cpp"
#include "strLib.h"


//test case provided by Roger Schrader
TEST_CASE ( "replaceAllCString - single substitution, same size" )
{
    char array[100] = "This is a simple sentence.";
    char answer[100] = "This is a sample sentence.";
    char searchStr[20] = "simple";
    char replaceStr[20] = "sample";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}



//test case provided by Roger Schrader
TEST_CASE ( "replaceAllCString - single substitution, smaller replacement" )
{
    char array[100] = "This is a simple sentence.";
    char answer[100] = "This is a silly sentence.";
    char searchStr[20] = "simple";
    char replaceStr[20] = "silly";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}



//test case provided by Roger Schrader
TEST_CASE ( "replaceAllCString -  multiple substitution, larger replacement" )
{
    char array[100] = "This is a silly sentence.";
    char answer[100] = "This is a really simple sentence.";
    char searchStr[20] = "silly";
    char replaceStr[20] = "really simple";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}



TEST_CASE ( "replaceAllCString - multiple substitution, same size" )
{
    char array[100] = "testing testing testing";
    char answer[100] = "success success success";
    char searchStr[20] = "testing";
    char replaceStr[20] = "success";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}


TEST_CASE ( "replaceAllCString - multiple substitution, smaller replacement" )
{
    char array[100] = "This is a silly silly example.";
    char answer[100] = "This is a lazy lazy example.";
    char searchStr[20] = "silly";
    char replaceStr[20] = "lazy";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}



TEST_CASE ( "replaceAllCString - multiple substitution, larger replacement" )
{
    char array[100] = "int int int";
    char answer[100] = "long int long int long int";
    char searchStr[20] = "int";
    char replaceStr[20] = "long int";

    replaceAllCString ( array, searchStr, replaceStr );
    REQUIRE ( strcmp ( answer, array ) == 0 );
}



//test case provided by Roger Schrader
TEST_CASE ( "replaceAllString - single substitution, same size" )
{
    string array = "This is a simple sentence.";
    string answer = "This is a sample sentence.";
    string searchStr = "simple";
    string replaceStr = "sample";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}



//test case provided by Roger Schrader
TEST_CASE ( "replaceAllString - single substitution, smaller replacement" )
{
    string array = "This is a simple sentence.";
    string answer = "This is a silly sentence.";
    string searchStr = "simple";
    string replaceStr = "silly";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}



//test case provided by Roger Schrader
TEST_CASE ( "replaceAllString - single substitution, larger replacement" )
{
    string array = "This is a silly sentence.";
    string answer = "This is a really simple sentence.";
    string searchStr = "silly";
    string replaceStr = "really simple";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}



TEST_CASE ( "replaceAllString - multiple substitution, same size" )
{
    string array = "testing testing testing";
    string answer = "success success success";
    string searchStr = "testing";
    string replaceStr = "success";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}



TEST_CASE ( "replaceAllString - multiple substitution, smaller replacement" )
{
    string array = "This is a silly silly example.";
    string answer = "This is a lazy lazy example.";
    string searchStr = "silly";
    string replaceStr = "lazy";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}



TEST_CASE ( "replaceAllString - multiple substitution, larger replacement" )
{
    string array = "int int int";
    string answer = "long int long int long int";
    string searchStr = "int";
    string replaceStr = "long int";

    replaceAllString ( array, searchStr, replaceStr );
    REQUIRE ( answer == array );
}