/** ***************************************************************************
 * @file  
 * 
 * @brief Contains test cases.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage m0061 - Basic Recursion
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
* This program recursively checks 2 C-style strings to see if they are 
* identical (case-insensitive).
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0061.exe
*
******************************************************************************/

#define CATCH_CONFIG_MAIN
#include "catch.hpp"
#include "strlib.cpp"
#include "strlib.h"


// write your test cases here
TEST_CASE ( "recCStrCmp-v1 - index based" )
{
    char str1[100] = "defenestration is a good word.";
    char str2[100] = "dEFENEstration is a good word.";
    char str3[100] = "a1!@#$%^&*()[]";
    char str4[100] = "a1!@#$%^&*()[]";


    SECTION ( "equal - same case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2, 0 ) == 0 );
    }



    SECTION ( "equal - letters, numbers, and symbols; same case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4, 0 ) == 0 );
    }



    SECTION ( "unequal - same case" )
    {
        str1[1] = '3';
        REQUIRE ( recCStrCmp ( str1, str2, 0 ) < 0 );
    }



    SECTION ( "unequal - letters, numbers, and symbols; same case" )
    {
        str3[1] = '3';
        REQUIRE ( recCStrCmp ( str3, str4, 0 ) > 0 );
    }


    str1[4] = toupper ( str1[4] );
    str3[0] = toupper ( str3[0] );


    SECTION ( "equal - different case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2, 0 ) == 0 );
        REQUIRE ( recCStrCmp ( str2, str1, 0 ) == 0 );
    }



    SECTION ( "equal - letters, numbers, and symbols; different case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4, 0 ) == 0 );
        REQUIRE ( recCStrCmp ( str4, str3, 0 ) == 0 );
    }


    str1[1] = '3';
    str3[1] = '3';


    SECTION ( "unequal - different case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2, 0 ) < 0 );
        REQUIRE ( recCStrCmp ( str2, str1, 0 ) > 0 );
    }



    SECTION ( "unequal - letters, numbers, and symbols; different case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4, 0 ) > 0 );
        REQUIRE ( recCStrCmp ( str4, str3, 0 ) < 0 );
    }
}



TEST_CASE ( "recCStrCmp-v2 - pointer based" )
{
    char str1[100] = "defenestration is a good word.";
    char str2[100] = "dEFENEstration is a good word.";
    char str3[100] = "a1!@#$%^&*()[]";
    char str4[100] = "a1!@#$%^&*()[]";


    SECTION ( "equal - same case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2 ) == 0 );
    }



    SECTION ( "equal - letters, numbers, and symbols; same case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4 ) == 0 );
    }



    SECTION ( "unequal - same case" )
    {
        str1[1] = '3';
        REQUIRE ( recCStrCmp ( str1, str2 ) < 0 );
    }



    SECTION ( "unequal - letters, numbers, and symbols; same case" )
    {
        str3[1] = '3';
        REQUIRE ( recCStrCmp ( str3, str4 ) > 0 );
    }


    str1[4] = toupper ( str1[4] );
    str3[0] = toupper ( str3[0] );


    SECTION ( "equal - different case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2 ) == 0 );
        REQUIRE ( recCStrCmp ( str2, str1 ) == 0 );
    }



    SECTION ( "equal - letters, numbers, and symbols; different case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4 ) == 0 );
        REQUIRE ( recCStrCmp ( str4, str3 ) == 0 );
    }


    str1[1] = '3';
    str3[1] = '3';


    SECTION ( "unequal - different case" )
    {
        REQUIRE ( recCStrCmp ( str1, str2 ) < 0 );
        REQUIRE ( recCStrCmp ( str2, str1 ) > 0 );
    }



    SECTION ( "unequal - letters, numbers, and symbols; different case" )
    {
        REQUIRE ( recCStrCmp ( str3, str4 ) > 0 );
        REQUIRE ( recCStrCmp ( str4, str3 ) < 0 );
    }
}