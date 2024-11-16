/** ***************************************************************************
 * @file  
 * 
 * @brief Contains test cases.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage m0080 - Sorted Singly Linked List
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
* This program holds a class for a singly linked list of integers which are
* automatically sorted in increasing order. The class has functions such as
* insert, remove, clear, find, print, and size.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0080.exe
*
******************************************************************************/
#define CATCH_CONFIG_MAIN
#include <iostream>
#include <sstream>
#include "catch.hpp"
#include "sortedSinglea.cpp"


using std::ostringstream;
using namespace std;


TEST_CASE("constructor")
 {
     sortedSingle list1;
     REQUIRE( list1.empty() == true);
     REQUIRE ( list1.size ( ) == 0 );
 }



TEST_CASE ( "clear" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;
     ostringstream sout;


     SECTION ( "clearing an empty list" )
     {
         list1.clear ( );
         REQUIRE ( list1.empty ( ) == true );
     }


     insertTF = list1.insert ( 7 );
     REQUIRE ( list1.empty ( ) == false );


     SECTION ( "1 element" )
     {
         list1.clear ( );
         REQUIRE ( list1.empty ( ) == true );
     }


     for ( i = 6; i > 1 && insertTF == true; i-- )
     {
         insertTF = list1.insert ( i );
         REQUIRE ( list1.empty ( ) == false );
     }


     SECTION ( "multiple elements" )
     {
         list1.clear ( );
         REQUIRE ( list1.empty ( ) == true );
     }

 }



TEST_CASE ( "empty" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;

     REQUIRE ( list1.empty ( ) == true );

     REQUIRE ( list1.size ( ) == 0 );

     for ( i = 1; i < 6 && insertTF == true; i++ )
     {
         insertTF = list1.insert ( i );
         REQUIRE ( list1.empty ( ) == false );
     }

 }



 TEST_CASE ( "find" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;


     SECTION ( "empty list" )
     {
         REQUIRE ( list1.find ( 1 ) == false );
     }


     for ( i = 1; i < 6 && insertTF == true; i++ )
         insertTF = list1.insert ( i );

     insertTF = list1.insert ( 444 );
     CHECK ( insertTF == true );

     REQUIRE ( list1.empty ( ) == false );
     CHECK ( list1.size ( ) == 6 );


     SECTION ( "inside the list" )
     {
         REQUIRE ( list1.find ( 444 ) == true );

         for ( i = 1; i < 6; i++ )
             REQUIRE ( list1.find ( i ) == true );
     }


     SECTION ( "outside of list" )
     {
         REQUIRE ( list1.find ( 0 ) == false );
         REQUIRE ( list1.find ( 7 ) == false );
     }

 }



TEST_CASE ( "insert" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;
     ostringstream sout;


     for ( i = 6; i > 1 && insertTF == true; i-- )
     {
         insertTF = list1.insert ( i );
         REQUIRE ( list1.empty ( ) == false );
     }

     SECTION ( "1 - 5" )
     {
         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "2, 3, 4, 5, 6" );
     }
     
     SECTION ( "longer list with a duplicate" )
     {
         REQUIRE ( list1.insert ( 1 ) == true );
         REQUIRE ( list1.insert ( 1 ) == true );
         REQUIRE ( list1.insert ( 20 ) == true );
         REQUIRE ( list1.insert ( 10 ) == true );
         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "1, 1, 2, 3, 4, 5, 6, 10, 20" );
     }

 }


 
 TEST_CASE ( "print" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;
     ostringstream sout;


     SECTION ( "empty list" )
     {
         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "" );
     }

     for ( i = 6; i > 1 && insertTF == true; i-- )
     {
         insertTF = list1.insert ( i );
         REQUIRE ( list1.empty ( ) == false );
     }


     SECTION ( "1 - 5" )
     {
         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "2, 3, 4, 5, 6" );
     }


     SECTION ( "longer list with a duplicate" )
     {
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 20 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "1, 1, 2, 3, 4, 5, 6, 10, 20" );
     }


     SECTION ( "1 - 5, different seperator" )
     {
         list1.print ( sout, ". " );
         REQUIRE ( sout.str ( ) == "2. 3. 4. 5. 6" );
     }


     SECTION ( "longer list with a duplicate, different seperator" )
     {
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 20 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         list1.print ( sout, " | " );
         REQUIRE ( sout.str ( ) == "1 | 1 | 2 | 3 | 4 | 5 | 6 | 10 | 20" );
     }


     SECTION ( "longer list with a duplicate, different seperator 2" )
     {
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 1 ) == true );
         CHECK ( list1.insert ( 20 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         CHECK ( list1.insert ( 4444 ) == true );
         list1.print ( sout, " A " );
         REQUIRE ( sout.str ( ) == 
             "1 A 1 A 2 A 3 A 4 A 5 A 6 A 10 A 10 A 20 A 4444" );
     }

 }



 TEST_CASE ( "remove" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;
     ostringstream sout;


     SECTION ( "empty list" )
     {
         REQUIRE ( list1.remove ( 1 ) == false );
         REQUIRE ( list1.remove ( 2 ) == false );
         REQUIRE ( list1.remove ( 3 ) == false );
     }


     for ( i = 6; i > 1 && insertTF == true; i-- )
     {
         insertTF = list1.insert ( i );
         REQUIRE ( list1.empty ( ) == false );
     }

     CHECK ( list1.insert ( 1 ) == true );


     SECTION ( "remove all" )
     {
         for ( i = 6; i > 1 && insertTF == true; i-- )
         {
             REQUIRE ( list1.remove ( i ) == true );
             REQUIRE ( list1.empty ( ) == false );
         }

         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "1" );

         insertTF = list1.remove ( 1 );
         REQUIRE ( list1.empty ( ) == true );
     }


     SECTION ( "longer list, remove first and final elements" )
     {
         CHECK ( list1.insert ( 20 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         CHECK ( list1.insert ( 10 ) == true );
         CHECK ( list1.insert ( 4444 ) == true );

         REQUIRE ( list1.remove ( 1 ) == true );
         REQUIRE ( list1.remove ( 4444 ) == true );


         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "2, 3, 4, 5, 6, 10, 10, 20" );

     }


     SECTION ( "longer list, remove all but one" )
     {

         CHECK ( list1.insert ( 4 ) == true );

         for ( i = 1; i < 7; i++ )
         {
             REQUIRE ( list1.remove ( i ) == true );
         }

         list1.print ( sout, ", " );
         REQUIRE ( sout.str ( ) == "4" );

     }
 }



 TEST_CASE ( "retrieve position" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;


     SECTION ( "empty list" )
     {
         REQUIRE ( list1.retrievePosition ( 1 ) == 0 );
     }


     for ( i = 1; i < 6 && insertTF == true; i++ )
         insertTF = list1.insert ( i );

     insertTF = list1.insert ( 444 );

     CHECK ( insertTF == true );
     REQUIRE ( list1.empty ( ) == false );
     CHECK ( list1.size ( ) == 6 );

     SECTION ( "inside the list" )
     {
         REQUIRE ( list1.retrievePosition ( 1 ) == 1 );
         REQUIRE ( list1.retrievePosition ( 444 ) == 6 );
         REQUIRE ( list1.retrievePosition ( 7 ) == 0 );

         for ( i = 2; i < 6; i++ )
             CHECK ( list1.retrievePosition ( i ) == i );
     }


     SECTION ( "outside of list" )
     {
         REQUIRE ( list1.retrievePosition ( 0 ) == 0 );
         REQUIRE ( list1.retrievePosition ( 7 ) == 0 );
     }

 }



 TEST_CASE ( "size" )
 {
     bool insertTF = true;         //indication if an element was inserted
     int i;

     sortedSingle list1;

     SECTION ( "empty list" )
     {
         REQUIRE ( list1.size ( ) == 0 );
     }
     


     for ( i = 1; i < 6 && insertTF == true; i++ )
     {
         insertTF = list1.insert ( i );
         CHECK ( list1.size ( ) == i );
     }


     SECTION ( "not empty list" )
     {
         CHECK ( insertTF == true );
         REQUIRE ( list1.empty ( ) == false );
         REQUIRE ( list1.size ( ) == 5 );

         insertTF = list1.insert ( 444 );

         REQUIRE ( list1.size ( ) == 6 );
     }
 }