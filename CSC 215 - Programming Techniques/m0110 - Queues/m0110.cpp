
/** ***************************************************************************
 * @file  
 * 
 * @brief Contains test cases and the print queue function (separate from a 
 * queue's dedicated print function).
 *****************************************************************************/
/** ***************************************************************************
* @mainpage m0110 - Queues
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
* This program contains the implementation for a queue and tests for 
* its functionality.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0110.exe
*
******************************************************************************/
#define CATCH_CONFIG_MAIN
#include <sstream>
#include "catch.hpp"
#include "myQueue.cpp"
#include "myQueue.h"


using std::ostringstream;

void printQueue( Queue q, ostream &out );



//write your test cases here
TEST_CASE ( "Queue::enque - Inserting into a queue" )
{
    ostringstream sout;

    Queue queue1;

    SECTION ( "Empty queue"  )
    {
        REQUIRE ( queue1.empty ( ) == true );
        REQUIRE ( queue1.size ( ) == 0 );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "" );
    }


    REQUIRE ( queue1.enqueue ( "one" ) == true );
    REQUIRE ( queue1.empty ( ) == false );
    

    SECTION ( "1 item" )
    {
        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one" );
    }


    SECTION ( "Multiple items" )
    {
        REQUIRE ( queue1.enqueue ( "two" ) == true );
        REQUIRE ( queue1.enqueue ( "three" ) == true );
        REQUIRE ( queue1.enqueue ( "four" ) == true );
        REQUIRE ( queue1.enqueue ( "five" ) == true );

        REQUIRE ( queue1.size ( ) == 5 );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one, two, three, four, five" );
    }
}



TEST_CASE ( "Queue::dequeue - removing from a queue" )
{
    ostringstream sout;

    Queue queue1;

    string word = "";


    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );
        REQUIRE ( queue1.size ( ) == 0 );

        REQUIRE ( queue1.dequeue ( word ) == false );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "" );
    }


    SECTION ( "1 deletion, final node" )
    {
        CHECK (queue1.enqueue ( "one" ) == true);
        REQUIRE ( queue1.dequeue ( word ) == true );
        REQUIRE ( word == "one" );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "" );
    }


    SECTION ( "Multiple deletions" )
    {
        CHECK ( queue1.enqueue ( "four" ) == true );
        CHECK ( queue1.enqueue ( "four" ) == true );
        CHECK ( queue1.enqueue ( "negative two" ) == true );
        CHECK ( queue1.enqueue ( "one" ) == true );
        CHECK ( queue1.enqueue ( "three" ) == true );
        CHECK ( queue1.enqueue ( "five" ) == true );

        REQUIRE ( queue1.dequeue ( word ) == true );
        REQUIRE ( word == "four" );

        REQUIRE ( queue1.dequeue ( word ) == true );
        REQUIRE ( word == "four" );

        REQUIRE ( queue1.dequeue ( word ) == true );
        REQUIRE ( word == "negative two" );

        REQUIRE ( queue1.size ( ) == 3 );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one, three, five" );
    }
}



TEST_CASE ( "Queue::front - peek at the front value of a queue" )
{
    ostringstream sout;

    Queue queue1;

    string word = "";


    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );
        REQUIRE ( queue1.size ( ) == 0 );
        REQUIRE ( queue1.front ( word ) == false );

        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "" );
    }



    SECTION ( "Changing queue" )
    {
        CHECK ( queue1.enqueue ( "four" ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "four" );

        CHECK ( queue1.enqueue ( "two" ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "four" );

        CHECK ( queue1.enqueue ( "negative two" ) == true );
        CHECK ( queue1.enqueue ( "one" ) == true );
        CHECK ( queue1.enqueue ( "three" ) == true );
        CHECK ( queue1.enqueue ( "five" ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "four" );

        CHECK ( queue1.dequeue ( word ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "two" );

        CHECK ( queue1.dequeue ( word ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "negative two" );


        CHECK ( queue1.dequeue ( word ) == true );
        REQUIRE ( queue1.front ( word ) == true );
        REQUIRE ( word == "one" );
        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one, three, five" );


        CHECK ( queue1.dequeue ( word ) == true );
        CHECK ( queue1.dequeue ( word ) == true );
        CHECK ( queue1.dequeue ( word ) == true );
        REQUIRE ( queue1.front ( word ) == false );

        // the queue is now empty, so sout will be the previous output with no 
        // additions added
        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one, three, five" );
    }
}



TEST_CASE ( "Queue::Queue - copy constructor" )
{
    ostringstream sout;

    Queue queue1;

    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );
        REQUIRE ( queue1.size ( ) == 0 );

        printQueue ( queue1, sout );
        REQUIRE ( sout.str ( ) == "" );
    }


    CHECK ( queue1.enqueue ( "one" ) == true );


    SECTION ( "One element" )
    {
        printQueue ( queue1, sout );
        REQUIRE ( sout.str ( ) == "one" );
    }


    CHECK ( queue1.enqueue ( "two" ) == true );
    CHECK ( queue1.enqueue ( "three" ) == true );
    CHECK ( queue1.enqueue ( "four" ) == true );
    CHECK ( queue1.enqueue ( "five" ) == true );


    SECTION ( "Multiple elements" )
    {
        printQueue ( queue1, sout );
        REQUIRE ( sout.str ( ) == "one, two, three, four, five" );
    }



    SECTION ( "Ensure passing by value did not destory the original" )
    {
        queue1.print ( sout );
        REQUIRE ( sout.str ( ) == "one, two, three, four, five" );
    }
}



TEST_CASE ( "Queue::operator== - equality operator" )
{
    ostringstream sout;

    Queue queue1;
    Queue queue2;


    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );
        REQUIRE ( queue2.empty ( ) == true );
        REQUIRE ( ( queue1 == queue2 ) == true );
    }

}

TEST_CASE ( "Queue::operator= - assignment operator" )
{
    ostringstream sout;

    Queue queue1;
    Queue queue2;

    string word = "";


    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );
        queue2 = queue1;
        REQUIRE ( queue2.empty ( ) == true );
    }

    CHECK ( queue1.enqueue ( "one" ) == true );

    SECTION ( "One element" )
    {
        queue2 = queue1;
        REQUIRE ( queue2.empty ( ) == false );
        queue2.print ( sout );
        REQUIRE ( sout.str ( ) == "one" );
    }

    CHECK ( queue1.enqueue ( "two" ) == true );
    CHECK ( queue1.enqueue ( "three" ) == true );
    CHECK ( queue1.enqueue ( "four" ) == true );
    CHECK ( queue1.enqueue ( "five" ) == true );

    SECTION ( "Multiple elements, multiple assignments" )
    {
        queue2 = queue1;
        /* queue2 should be "one, two, three, four, five" but multiple .print
         * calls will add the two .prints*/

        CHECK ( queue1.dequeue ( word ) == true );
        CHECK ( queue1.dequeue ( word ) == true );
        CHECK ( queue1.dequeue ( word ) == true );

        queue2 = queue1;
        
        //make sure = doesn't just add or something else strange
        queue2.print ( sout );
        REQUIRE ( sout.str ( ) == "four, five" );
    }

}



void printQueue( Queue q, ostream &out )
{
    // write the code to print a queue to the screen destroying the
    // contents of the queue when done. It is pass by value.
    
    bool dequeueTF;    // holds whether a dequeue call returned true or false

    string word = "";

    dequeueTF = q.dequeue ( word );

    while ( dequeueTF == true )
    {
        out << word;

        if ( q.empty ( ) == false )
            out << ", ";

        dequeueTF = q.dequeue ( word );
    }


   return;
}