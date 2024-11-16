/** ***************************************************************************
 * @file  
 * 
 * @brief Contains test cases.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage m0011 - Bitpacking
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
* This program will bitpack and bit unpack sets of 3 characters, either all 
* uppercase or all lowercase. The characters are packed into 1 short integer.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0011.exe
*
******************************************************************************/

#define CATCH_CONFIG_MAIN
#include "catch.hpp"
#include "bitpacking.cpp"
#include "bitpacking.h"

using namespace std;


TEST_CASE("packThree - lowercase b c d")
{
    char high = 'b', middle = 'c', lower = 'd';
    short int result;
    short int serverAnswer = 2148;

    result = packThree(high, middle, lower);

    REQUIRE(serverAnswer == result);
}



TEST_CASE("packThree - lowercase e f g")
{
    char high = 'e', middle = 'f', lower = 'g';
    short int result;
    short int serverAnswer = 5319;

    result = packThree(high, middle, lower);

    REQUIRE(serverAnswer == result);
}



//test case provided by Roger Schrader
TEST_CASE ( "packThree - lowercase t m a" )
{
    char high = 't', middle = 'm', lower = 'a';
    short int result;
    short int serverAnswer = 20897;

    result = packThree ( high, middle, lower );

    REQUIRE (serverAnswer == result );
}



TEST_CASE( "packThree - uppercase H I J" )
{
    char high = 'H', middle = 'I', lower = 'J';
    short int result;
    short int serverAnswer = 8490;

    result = packThree(high, middle, lower);

    REQUIRE(serverAnswer == result);
}



TEST_CASE( "packThree - uppercase K L N" )
{
    char high = 'K', middle = 'L', lower = 'N';
    short int result;
    short int serverAnswer = 11662;

    result = packThree(high, middle, lower);

    REQUIRE(serverAnswer == result);
}



TEST_CASE( "packThree - uppercase O P Q" )
{
    char high = 'O', middle = 'P', lower = 'Q';
    short int result;
    short int serverAnswer = 15889;

    result = packThree(high, middle, lower);

    REQUIRE(serverAnswer == result);
}



TEST_CASE("unpackThree - lowercase 2148")
{
    short int packedValues = 2148;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, LOWERCASE);

    CHECK(upper == 'b');
    CHECK(middle == 'c');
    CHECK(lower == 'd');
}



TEST_CASE("unpackThree - lowercase 5319")
{
    short int packedValues = 5319;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, LOWERCASE);

    CHECK(upper == 'e');
    CHECK(middle == 'f');
    CHECK(lower == 'g');
}



//test case provided by Roger Schrader
TEST_CASE ( "unpackThree - lowercase 20897" )
{
    short int packedValues = 20897;
    char upper, middle, lower;

    unpackThree (packedValues, upper, middle, lower, LOWERCASE );

    CHECK (upper == 't');
    CHECK (middle == 'm');
    CHECK ( lower == 'a');
}



TEST_CASE("unpackThree - uppercase 8490")
{
    short int packedValues = 8490;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, UPPERCASE);

    CHECK(upper == 'H');
    CHECK(middle == 'I');
    CHECK(lower == 'J');
}



TEST_CASE("unpackThree - uppercase 11662")
{
    short int packedValues = 11662;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, UPPERCASE);

    CHECK(upper == 'K');
    CHECK(middle == 'L');
    CHECK(lower == 'N');
}



TEST_CASE("unpackThree - uppercase 15889")
{
    short int packedValues = 15889;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, UPPERCASE);

    CHECK(upper == 'O');
    CHECK(middle == 'P');
    CHECK(lower == 'Q');
}



TEST_CASE( "unpackThree - uppercase 20897" )
{
    short int packedValues = 20897;
    char upper, middle, lower;

    unpackThree(packedValues, upper, middle, lower, UPPERCASE);

    CHECK(upper == 'T');
    CHECK(middle == 'M');
    CHECK(lower == 'A');
}