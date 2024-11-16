/** ***************************************************************************
 * @file  
 * 
 * @brief Runs catch, and contains test cases. Unless indicated otherwise, 
 * all test cases are written by Zoe Millage.
 *****************************************************************************/

#include "catch.hpp"
#include "prog1.h"

/** ***************************************************************************
 * @author Roger Schrader
 *
 * @par Description:
 * Provided by <a href=
 * "http://sdmines.sdsmt.edu/sdsmt/directory/personnel/rschrade" target=_blank>
 * Professor Roger Schrader</a>.
 * This runs the catch program, but putting it within a function allows this
 * program to utilize console output and other aspects that conflict with
 * the standalone catch. 
 *
 * @param[in] myargc - The argument count for the catch program
 * @param[in] myargv - The path to what program catch will test
 *
 *****************************************************************************/
void makeCATCH(int& myargc, char**& myargv)
{

    myargc = 1;
    myargv = new (nothrow) char* [myargc];

    myargv[0] = new (nothrow) char[strlen("x64\\debug\\prog1.exe") + 1];
    strcpy(myargv[0], "x64\\debug\\prog1.exe");
}




TEST_CASE ( "get5DigitZip" )
{
    ZIPCODE zip;                  //used for testing each case
    ZIPCODE zipA = 182043735;     /* binary 0000 1010 1101 1001 11
                                   * 00 0100 0101 0111*/
    ZIPCODE zipB = 945374295;     /* binary 0011 1000 0101 1001 01 
                                   * 00 0100 0101 0111*/
    ZIPCODE zipC = 910217564;     /* binary 0011 0110 0100 0000 11
                                   * 01 0001 0101 1100*/
    ZIPCODE zipD = 163941;        /* binary 0000 0000 0000 0010 10
                                   * 00 0000 0110 0101*/


    SECTION ( "full zip = 11111 1111" )
    {
        zip = get5DigitZip ( zipA );

        REQUIRE ( zip == 11111 );
    }



    SECTION ( "full zip = 57701 1111" )
    {
        zip = get5DigitZip ( zipB );

        REQUIRE ( zip == 57701 );
    }



    SECTION ( "full zip = 55555 4444" )
    {
        zip = get5DigitZip ( zipC );

        REQUIRE ( zip == 55555 );
    }



    SECTION ( "full zip = 00010 0101" )
    {
        zip = get5DigitZip ( zipD );

        REQUIRE ( zip == 10 );
    }
}



TEST_CASE ( "get4DigitZip" )
{
    ZIPCODE zip;                  //used for testing each case
    ZIPCODE zipA = 182043735;     /* binary 0000 1010 1101 1001 11
                                   * 00 0100 0101 0111*/
    ZIPCODE zipB = 945374295;     /* binary 0011 1000 0101 1001 01
                                   * 00 0100 0101 0111*/
    ZIPCODE zipC = 910217564;     /* binary 0011 0110 0100 0000 11
                                   * 01 0001 0101 1100*/
    ZIPCODE zipD = 163941;        /* binary 0000 0000 0000 0010 10
                                   * 00 0000 0110 0101*/

    SECTION ( "full zip = 11111 1111" )
    {
        ZIPCODE zip;

        zip = get4DigitZip ( zipA );

        REQUIRE ( zip == 1111 );
    }



    SECTION ( "full zip = 57701 1111" )
    {
        zip = get4DigitZip ( zipB );

        REQUIRE ( zip == 1111 );
    }



    SECTION ( "full zip = 55555 4444" )
    {
        zip = get4DigitZip ( zipC );

        REQUIRE ( zip == 4444 );
    }



    SECTION ( "full zip = 00010 0101" )
    {
        zip = get4DigitZip ( zipD );

        REQUIRE ( zip == 101 );
    }
}



TEST_CASE ( "getDay" )
{
    DATE day;                   //used for testing each case
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        day = getDay ( dateA );

        REQUIRE ( day == 26 );

    }



    SECTION ( "1/31/1899" )
    {
        day = getDay ( dateB );

        REQUIRE ( day == 31 );
    }



    SECTION ( "13/32/2022" )
    {
        day = getDay ( dateC );

        REQUIRE ( day == 32 );

    }



    SECTION ( "0/0/1898" )
    {
        day = getDay ( dateD );

        REQUIRE ( day == 0 );
    }
}



TEST_CASE ( "getMonth" )
{
    DATE month;                 //used for testing each case
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        month = getMonth ( dateA );

        REQUIRE ( month == 2 );

    }



    SECTION ( "1/31/1899" )
    {
        month = getMonth ( dateB );

        REQUIRE ( month == 1 );
    }



    SECTION ( "13/32/2022" )
    {
        month = getMonth ( dateC );

        REQUIRE ( month == 13 );

    }



    SECTION ( "0/0/1898" )
    {
        month = getMonth ( dateD );

        REQUIRE ( month == 0 );
    }
}



TEST_CASE ( "getYear" )
{
    DATE year;                  //used for testing each case
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        year = getYear ( dateA );

        REQUIRE ( year == 2021 );

    }



    SECTION ( "1/31/1899" )
    {
        year = getYear ( dateB );

        REQUIRE ( year == 1899 );
    }



    SECTION ( "13/32/2022" )
    {
        year = getYear ( dateC );

        REQUIRE ( year == 2022 );

    }



    SECTION ( "0/0/1898" )
    {
        year = getYear ( dateD );

        REQUIRE ( year == 1898 );
    }
}



TEST_CASE ( "validAddress" )
{
    char str1[100] = "501 E St Joseph St";
    char str2[100] = "1234 Placeholder St. #999";
    char str3[100] = "Error: Address not found!!!";
    char str4[100] = "5678 Fakeaddress St, #900";

    SECTION ( "true - SDSMT address" )
    {
        REQUIRE ( validAddress ( str1 ) == true );
    }


    SECTION ( "true - Placeholder address" )
    {
        REQUIRE ( validAddress ( str2 ) == true );
    }


    SECTION ( "false - contains ! and :" )
    {
        REQUIRE ( validAddress ( str3 ) == false );
    }


    SECTION ( "false - , instead of ." )
    {
        REQUIRE ( validAddress ( str4 ) == false );
    }
}



TEST_CASE ( "validCallSign1" )
{
    char ch1 = 'a';
    char ch2 = 'A';
    char ch3 = 'K';
    char ch4 = 'w';
    char ch5 = 'N';

    SECTION ( "true - lowercase" )
    {
        REQUIRE ( validCallSign1 ( ch4 ) == true );
    }


    SECTION ( "true - uppercase" )
    {
        REQUIRE ( validCallSign1 ( ch3 ) == true );
        REQUIRE ( validCallSign1 ( ch5 ) == true );
    }


    SECTION ( "false" )
    {
        REQUIRE ( validCallSign1 ( ch1 ) == false );
        REQUIRE ( validCallSign1 ( ch2 ) == false );

    }
}



TEST_CASE ( "validCallSign2" )
{
    char original1[30] = "AA1AA";
    char original4[30] = "bbbbb";
    char str1[30] = "AA1AA";
    char str2[30] = "XY3WV";
    char str3[30] = "44444";
    char str4[30] = "bbbbb";

    SECTION ( "true" )
    {
        REQUIRE ( validCallSign2 ( str1 ) == true );
        REQUIRE ( validCallSign2 ( str2 ) == true );
        REQUIRE ( validCallSign2 ( str4 ) == true );
    }


    SECTION ( "false" )
    {
        REQUIRE ( validCallSign2 ( str3 ) == false );
    }


    SECTION ( "checking that arrays are not corrupted" )
    {
        int i;

        for ( i = 0; i < 5; i++ )
        {
            CHECK ( str1[i] == original1[i] );
            CHECK ( str4[i] == original4[i] );
        }
    }
}



TEST_CASE ( "validCallSign3" )
{
    char original1[30] = "AA1AA";
    char original4[30] = "bbbbb";
    char str1[30] = "AA1AA";
    char str2[30] = "XY3WV";
    char str3[30] = "44444";
    char str4[30] = "bbbbb";

    SECTION ( "true" )
    {
        REQUIRE ( validCallSign3 ( str1 ) == true );
        REQUIRE ( validCallSign3 ( str2 ) == true );
        REQUIRE ( validCallSign3 ( str3 ) == true );
    }


    SECTION ( "false" )
    {
        REQUIRE ( validCallSign3 ( str4 ) == false );
    }


    SECTION ( "checking that arrays are not corrupted" )
    {
        int i;

        for ( i = 0; i < 5; i++ )
        {
            CHECK ( str1[i] == original1[i] );
            CHECK ( str4[i] == original4[i] );
        }
    }
}



TEST_CASE ( "validCallSign4" )
{
    char original1[30] = "AA1AA";
    char original4[30] = "bbbbb";
    char str1[30] = "AA1AA";
    char str2[30] = "XY3WV";
    char str3[30] = "44444";
    char str4[30] = "bbbbb";

    SECTION ( "true" )
    {
        REQUIRE ( validCallSign4 ( str1 ) == true );
        REQUIRE ( validCallSign4 ( str2 ) == true );
    }


    SECTION ( "false" )
    {
        REQUIRE ( validCallSign4 ( str3 ) == false );
        REQUIRE ( validCallSign4 ( str4 ) == false );
    }


    SECTION ( "checking that arrays are not corrupted" )
    {
        int i;

        for ( i = 0; i < 5; i++ )
        {
            CHECK ( str1[i] == original1[i] );
            CHECK ( str4[i] == original4[i] );
        }
    }
}



TEST_CASE ( "validDay" )
{
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        REQUIRE ( validDay (dateA) == true );
    }



    SECTION ( "1/31/1899" )
    {
        REQUIRE ( validDay ( dateB ) == true );
    }



    SECTION ( "13/32/2022" )
    {
        REQUIRE ( validDay ( dateC ) == false );
    }



    SECTION ( "0/0/1898" )
    {
        REQUIRE ( validDay ( dateD ) == false );
    }
}



TEST_CASE ( "validMonth" )
{
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        REQUIRE ( validMonth ( dateA ) == true );
    }



    SECTION ( "1/31/1899" )
    {
        REQUIRE ( validMonth ( dateB ) == true );
    }



    SECTION ( "13/32/2022" )
    {
        REQUIRE ( validMonth ( dateC ) == false );
    }



    SECTION ( "0/0/1898" )
    {
        REQUIRE ( validMonth ( dateD ) == false );
    }
}



TEST_CASE ( "validYear" )
{
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/

    SECTION ( "2/26/2021" )
    {
        REQUIRE ( validYear ( dateA ) == true );
    }



    SECTION ( "1/31/1899" )
    {
        REQUIRE ( validYear ( dateB ) == false );
    }



    SECTION ( "13/32/2022" )
    {
        REQUIRE ( validYear ( dateC ) == false );
    }



    SECTION ( "0/0/1898" )
    {
        REQUIRE ( validYear ( dateD ) == false );
    }
}



TEST_CASE ( "beforeDate" )
{
    DATE dateA = 8278170;       /*binary 0111 1110 0101|
                                 * 00 0010 01 1010*/
    DATE dateB = 7778399;       /*binary 0111 0110 1011
                                 * 00 0001 01 1111*/
    DATE dateC = 8282976;       /*binary 0111 1110 0110
                                 * 00 1101 10 0000*/
    DATE dateD = 7774208;       /*binary 0111 0110 1010
                                 * 00 0000 00 0000*/
    DATE dateE= 7774209;       /*binary 0111 0110 1010
                             * 00 0000 00 0001*/


    SECTION ( "same date" )
    {
        REQUIRE ( beforeDate ( dateA, dateA ) == false );
    }


    SECTION ( "2/26/2021, 1/31/1899" )
    {
        REQUIRE ( beforeDate ( dateA, dateB ) == false );
    }


    SECTION ( "1/31/1899, 2/26/2021" )
    {
        REQUIRE ( beforeDate ( dateB, dateA ) == true );
    }


    SECTION ( "13/32/2022, 2/26/2021" )
    {
        REQUIRE ( beforeDate ( dateC, dateA ) == false );
    }


    SECTION ( "0/0/1898, 1/31/1899" )
    {
        REQUIRE ( beforeDate(dateD, dateB) == true );
    }


    SECTION ( "1/31/1899, 0/0/1898" )
    {
        REQUIRE ( beforeDate ( dateB, dateD ) == false );
    }


    SECTION ( "0/0/1898, 0/1/1898" )
    {
        REQUIRE ( beforeDate ( dateD, dateE ) == true );
    }

    
    SECTION ( "0/0/1898, 0/1/1898" )
    {
        REQUIRE ( beforeDate ( dateD, dateE ) == true );
    }
}



TEST_CASE ( "validName" )
{
    char name1[100] = "Roger Schrader";
    char name2[100] = "Dr. Mr. Ms.";
    char name3[100] = "Dr-_-404";
    char name4[100] = "K8lyn";

    SECTION ( "valid - Roger Schrader" )
    {
        REQUIRE ( validName ( name1 ) == true );
    }


    SECTION ( "valid - testing titles" )
    {
        REQUIRE ( validName ( name2 ) == true );
    }


    SECTION ( "invalid - numbers and symbols" )
    {
        REQUIRE ( validName ( name3 ) == false );
        REQUIRE ( validName ( name4 ) == false );
    }
}



TEST_CASE ( "validRadioClass" )
{
    char ch1 = 'a';
    char ch2 = 'B';
    char ch3 = 'g';
    char ch4 = 'n';
    char ch5 = 'P';
    char ch6 = 'T';

    SECTION ( "uppercase" )
    {
        REQUIRE ( validRadioClass ( ch2 ) == false );
        REQUIRE ( validRadioClass ( ch5 ) == true );
        REQUIRE ( validRadioClass ( ch6 ) == true );
    }


    SECTION ( "lowercase" )
    {
        REQUIRE ( validRadioClass ( ch1 ) == true );
        REQUIRE ( validRadioClass ( ch3 ) == true );
        REQUIRE ( validRadioClass ( ch4 ) == true );
    }
}



TEST_CASE ( "validState" )
{
    SECTION ( "South Dakota" )
    {
        //testcase provided by Roger Schrader
        int i;

        char s[4][2] = { {'S', 'D' }, {'s', 'D'}, {'s', 'd'},{'S','d'} };
        char orig[4][2] = { {'S', 'D' }, {'s', 'D'}, {'s', 'd'},{'S','d'} };

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( validState ( s[i] ) == true );
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }

    }


    SECTION ( "North Dakota" )
    {
        int i;

        char s[4][2] = { {'N', 'D' }, {'n', 'D'}, {'n', 'd'},{'N','d'} };
        char orig[4][2] = { {'N', 'D' }, {'n', 'D'}, {'n', 'd'},{'N','d'} };

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( validState ( s[i] ) == true );
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }

    }


    SECTION ( "fake state - AA" )
    {
        int i;

        char s[4][2] = { {'A', 'A' }, {'A', 'a'}, {'a', 'A'},{'a','a'} };
        char orig[4][2] = { {'A', 'A' }, {'A', 'a'}, {'a', 'A'},{'a','a'} };

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( validState ( s[i] ) == false );
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }
    }
}



TEST_CASE ( "validZip4" )
{
    ZIPCODE zipA = 182043735;     /* binary 0000 1010 1101 1001 11
                                   * 00 0100 0101 0111*/
    ZIPCODE zipB = 945374295;     /* binary 0011 1000 0101 1001 01
                                   * 00 0100 0101 0111*/
    ZIPCODE zipC = 910217564;     /* binary 0011 0110 0100 0000 11
                                   * 01 0001 0101 1100*/
    ZIPCODE zipD = 163941;        /* binary 0000 0000 0000 0010 10
                                   * 00 0000 0110 0101*/

    SECTION ( "full zip = 11111 1111" )
    {
        REQUIRE ( validZip4 ( zipA ) == true );
    }



    SECTION ( "full zip = 57701 1111" )
    {
        REQUIRE ( validZip4 ( zipB ) == true );
    }



    SECTION ( "full zip = 55555 4444" )
    {
        REQUIRE ( validZip4 ( zipC ) == true );
    }



    SECTION ( "full zip = 00010 0101" )
    {
        REQUIRE ( validZip4 ( zipD ) == false );
    }
}



TEST_CASE ( "validZip5" )
{
    ZIPCODE zipA = 182043735;     /* binary 0000 1010 1101 1001 11
                                   * 00 0100 0101 0111*/
    ZIPCODE zipB = 945374295;     /* binary 0011 1000 0101 1001 01
                                   * 00 0100 0101 0111*/
    ZIPCODE zipC = 910217564;     /* binary 0011 0110 0100 0000 11
                                   * 01 0001 0101 1100*/
    ZIPCODE zipD = 163941;        /* binary 0000 0000 0000 0010 10
                                   * 00 0000 0110 0101*/

    SECTION ( "full zip = 11111 1111" )
    {
        REQUIRE ( validZip4 ( zipA ) == true );
    }



    SECTION ( "full zip = 57701 1111" )
    {
        REQUIRE ( validZip4 ( zipB ) == true );
    }



    SECTION ( "full zip = 55555 4444" )
    {
        REQUIRE ( validZip4 ( zipC ) == true );
    }



    SECTION ( "full zip = 00010 0101" )
    {
        REQUIRE ( validZip4 ( zipD ) == false );
    }
}



TEST_CASE ( "validStateCase" )
{
    SECTION ( "South Dakota" )
    {
        int i;

        char s[4][2] = { {'S', 'D' }, {'s', 'D'}, {'s', 'd'},{'S','d'} };
        char orig[4][2] = { {'S', 'D' }, {'s', 'D'}, {'s', 'd'},{'S','d'} };


        REQUIRE ( validStateCase ( s[0][0] ) == true );
        REQUIRE ( validStateCase ( s[1][0] ) == false );
        REQUIRE ( validStateCase ( s[2][0] ) == false );
        REQUIRE ( validStateCase ( s[3][0] ) == true );

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }

    }


    SECTION ( "North Dakota" )
    {
        int i;

        char s[4][2] = { {'N', 'D' }, {'n', 'D'}, {'n', 'd'},{'N','d'} };
        char orig[4][2] = { {'N', 'D' }, {'n', 'D'}, {'n', 'd'},{'N','d'} };


        REQUIRE ( validStateCase ( s[0][0] ) == true );
        REQUIRE ( validStateCase ( s[1][0] ) == false );
        REQUIRE ( validStateCase ( s[2][0] ) == false );
        REQUIRE ( validStateCase ( s[3][0] ) == true );

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }

    }


    SECTION ( "fake state - AA" )
    {
        int i;

        char s[4][2] = { {'A', 'A' }, {'A', 'a'}, {'a', 'A'},{'a','a'} };
        char orig[4][2] = { {'A', 'A' }, {'A', 'a'}, {'a', 'A'},{'a','a'} };


        REQUIRE ( validStateCase ( s[0][0] ) == true );
        REQUIRE ( validStateCase ( s[1][0] ) == true );
        REQUIRE ( validStateCase ( s[2][0] ) == false );
        REQUIRE ( validStateCase ( s[3][0] ) == false );

        for ( i = 0; i < 4; i++ )
        {
            CHECK ( ( s[i][0] == orig[i][0] && s[i][1] == orig[i][1] ) );
        }
    }

}
