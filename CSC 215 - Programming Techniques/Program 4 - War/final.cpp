/** ***************************************************************************
 * @file  
 * 
 * @brief Starts the program, which does the appropriate action for 
 * the given command line arguments, starts the game, and prints the results.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage Final Project - War
*
* @section course_section Course Information
*
* @author Zoe Millage
*
* @date Spring 2021
*
* @par Instructor
*     <a href = "https://www.sdsmt.edu/Directories/Personnel/Schrader,-Roger/" 
*     target=_blank> Roger Schrader</a>
*
* @par Course
*     CSC 215 - Programming Techniques
*
* @section program_section Program Information
*
* @details
* This program simulates the game of war. Each player is given a hand
* (either generated "randomly" or determined by a file) that is represented
* by a Queue and a discard pile represented by a stack. Each player will 
* discard a card and whoever had the discard of higher value takes the
* opponenets discard, then their own, and adds them to their own hand. This
* repeats until one player has all of the cards. If at any time there is 
* a tie, each player will discard 3 cards, and the 3rd determines who gets
* all of the cards. Repeat until the tie is broken. If a war occurs but one
* player has less than 3 cards left, the second player will discard an equal
* amount instead of 3. If the final card is a tie, both players will add their
* own discard into their hand. Once the game has ended, the winner and 
* each hand will be printed. 
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      Start compilation with "final.cpp"
*
* @par Usage
* @verbatim
  final.exe -s seed1     seed2
  final.exe -f file1.txt file2.txt
  @endverbatim
*
******************************************************************************/
#define CATCH_CONFIG_RUNNER

#include "catch.hpp"
#include "functions.cpp"

using namespace std;


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the starting point of the program. It will check command line 
 * arguemnts to determine whether to start from seeds or to get cards from
 * files. Then, it will call the functions to play rounds until the game has
 * been won, then print the results. 
 *
 * @param[in] argc - The number of arguments for the program; should equal 4.
 * @param[in] argv - An array that holds the argument names
 *
 * @returns 0 - The program ran successfully.
 * @returns 1 - Incorrect command line arguments.
 * @returns 2 - An input file failed to open.
 *
 *****************************************************************************/
int main ( int argc, char** argv )
{
    ifstream fin1;
    ifstream fin2;

    int exit = 0;
    int rounds = 0;
    int seed1 = 0;
    int seed2 = 0;

    Queue<card> player1;
    Queue<card> player2;

    int result = 1;
    int myargc;
    char** myargv;
    if ( RUNCATCH )
    {
        makeCATCH ( myargc, myargv );
        result = Catch::Session ( ).run ( myargc, myargv );
        if ( result != 0 )
        {
            cout << "You have errors in your functions" << endl;
            return 0;
        }
    }


    // get the cards for the game
    checkCLineArgs ( argc, argv );

    // read from files if -f is an argument
    if ( argv[1][1] == 'f' )
    {
        open2Files ( fin1, fin2, argv[2], argv[3] );

        readCards ( fin1, player1, 0 );
        readCards ( fin2, player2, 13 );
    }
    
    // generate cards from seeds if -s is an argument
    else
    {
        convertSeeds ( argv[2], seed1, argv[3], seed2 );
        generateCards ( player1, seed1 );
        generateCards ( player2, seed2 );
    }

    // make sure the queues are not equal
    if ( player1 == player2 )
        printResults ( cout, player1, player2, -1 );

    // play rounds until one of the queues are empty
    while ( player1.empty ( ) == false && player2.empty ( ) == false )
    {
        playRound ( player1, player2 );
        rounds++;
    }

    printResults ( cout, player1, player2, rounds );
    
    if ( argv[1][1] == 'f' )
    {
        close2Files ( fin1, fin2, 0 );
    }

    return 0;
}