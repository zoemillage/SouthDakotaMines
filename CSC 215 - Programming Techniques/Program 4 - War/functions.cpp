/** ***************************************************************************
 * @file  
 * 
 * @brief Contains functions related to setting up, playing, and printing the
 * results of the game.
 *****************************************************************************/
#include <cstring>
#include <sstream>
#include "catch.hpp"
#include "final.h"

#define _CRT_SECURE_NO_WARNINGS

using namespace std;



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function checks command line arguments. 
 * It checks that the argument count is the correct value ( 4 in 
 * this case ). Then, it checks that argv[1] is the correct value for running
 * the program. A valid argv[1] is either "-s" or "-f" 
 *
 * @param[in] argc - The number of arguments for the program
 * @param[in] argv - An array that holds the argument names
 *
 *****************************************************************************/
void checkCLineArgs ( int argc, char** argv )
{
    // 4 is the only valid argument count
    if ( argc != 4 )
    {
        cout << "Usage: final.exe -s seed1 seed2" << endl
            << "       final.exe -f file1 file2" << endl;

        exit ( 1 );
    }

    // -s and -f are the only valid argv[1]s
    if ( argv[1][0] != '-' && argv[1][1] != 's' && argv[1][1] != 'f' )
    {
        cout << "Usage: final.exe -s seed1 seed2" << endl
            << "       final.exe -f file1 file2" << endl;

        exit ( 1 );
    }
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function assumes an isopen() check has failed or files are being
 * closed to exit the program, so this function closes all files.
 *
 * @param[in] fin - an input file stream, used to get the 1st deck of cards.
 * @param[in] fin2 - an input file stream, used to get the 2nd deck of cards.
 * @param[in] exitCode - the code to output when exiting the program
 *
 *****************************************************************************/
void close2Files ( ifstream& fin, ifstream& fin2, int exitCode )
{
    fin.close ( );
    fin2.close ( );

    exit ( exitCode );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes the face value from a card and returns its corresponding
 * character ( A = Ace, J = Jack, Q = Queen, K = King, and all other values
 * are valid as numbers).
 *
 * @param[in] faceValue - the number value for the card
 *
 * @returns A - Ace
 * @returns J - Jack
 * @returns Q - Queen
 * @returns K - King
 * @returns 2 - 10 if the card isn't a face card
 *
 *****************************************************************************/
char convertFaceValue ( int& faceValue )
{
    char checking = 0;      // used to check if a char or an int was returned

    int temp = 0;

    // return the correct character if applicable
    if ( faceValue == 0 )
        return 'A';

    else if ( faceValue == 10 )
        return 'J';

    else if ( faceValue == 11 )
        return 'Q';

    else if ( faceValue == 12 )
        return 'K';

    // increment the face value and return if not a letter value
    else
        faceValue++;

    return checking;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function converts the seeds given in the command line arguments into
 * integers.
 *
 * @param[in] arr1 - the char array holding the first seed
 * @param[in, out] seed1 - the 1st converted seed
 * @param[in] arr2 - the char array holding the second seed
 * @param[in, out] seed2 - the 2nd converted seed
 *
 *****************************************************************************/
void convertSeeds ( char arr1[], int& seed1, char arr2[], int& seed2 )
{
    seed1 = atoi ( arr1 );
    seed2 = atoi ( arr2 );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes the suit value from a card and returns its corresponding
 * character ( 0 = hearts, 1 = diamonds, 2 = clubs, 3 = spades ).
 *
 * @param[in] suit - the suit value for the card
 *
 * @returns H - Hearts
 * @returns D - Diamonds
 * @returns C - Clubs
 * @returns S - Spades
 *
 *****************************************************************************/
char convertSuit ( int suit )
{
    // return the correct suit abbreviation
    if ( suit == 0 )
        return 'H';

    if ( suit == 1 )
        return 'D';

    if ( suit == 2 )
        return 'C';

    return 'S';
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function discards one card from each hand and transfers them to their
 * respective discard stack. Previous error checking done before calling the
 * function allows this function to assume a dequeue will be successful.
 *
 * @param[in, out] p1 - player 1's hand
 * @param[in, out] s1 - player 1's discard pile
 * @param[in, out] check1 - checks if player 1's hand dequeued properly
 * @param[in, out] card1 - the most recently discarded card from p1
 * @param[in, out] p2 - player 2's hand
 * @param[in, out] s2 - player 2's discard pile
 * @param[in, out] check2 - checks if player 2's hand dequeued properly
 * @param[in, out] card2 - the most recently discarded card from p2
 *
 *****************************************************************************/
void discard1 ( Queue<card>& p1, stack<card>& s1, bool& check1, card& card1,
                Queue<card>& p2, stack<card>& s2, bool& check2, card& card2 )
{
    check1 = p1.dequeue ( card1 );
    check2 = p2.dequeue ( card2 );
    
    if ( check1 && check2 )
    {
        s1.push ( card1 );
        s2.push ( card2 );
    }
    
    return;
}



/** ***************************************************************************
 * @authors Zoe Millage and <a href = 
 * "https://www.sdsmt.edu/Directories/Personnel/Schrader,-Roger/" 
 * target=_blank> Roger Schrader</a>
 *
 * @par Description:
 * Generates a 52 card deck for a given player with the given seed value.
 *
 * @param[in] player - a player's deck of cards
 * @param[in] seed - the seed used to generate the cards
 *
 *****************************************************************************/
void generateCards ( Queue<card>& player, int seed )
{
    bool enqueueTF = false;      // whether or not an enqueue succeeded
    bool used[13][4] = { 0 };

    card aCard;

    default_random_engine generator ( seed );

    int cardValue;
    int totalCards = 0;

    uniform_int_distribution<int> distribution ( 0, 51 );

    // generate until all 52 are generated
    while ( totalCards < 52 )
    {
        // generate a card
        cardValue = distribution ( generator ); // gives number between 0 - 51
        aCard.faceValue = cardValue % 13;       // 0 = ace, 9 = ten,...
        aCard.suit = cardValue / 13;            // 0 = H, 1 = D, 2 = C, 3 = S

        // enqueue if card isn't already used
        if ( used[aCard.faceValue][aCard.suit] == 0 )
        {
            enqueueTF = player.enqueue ( aCard );
            used[aCard.faceValue][aCard.suit] = 1;
        }

        // increment the total number of cards
        totalCards++;
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes the top card in the discard stack and adds it to the 
 * hand queue until the discard stack is empty.
 *
 * @param[in, out] hand - a player's hand
 * @param[in, out] discard - a player's discard pile
 *
 *****************************************************************************/
void getDiscard ( Queue<card>& hand, stack<card>& discard )
{
    bool check;    //checks if the enqueue succeeded or failed

    card temp;

    while ( discard.empty ( ) == false )
    {
        temp = discard.top ( );
        check = hand.enqueue ( temp );
        discard.pop ( );
    }
}



#pragma warning(disable : 4996)
/** ***************************************************************************
 * @author Roger Schrader
 *
 * @par Description:
 * Provided by <a href=
 * "http://sdmines.sdsmt.edu/sdsmt/directory/personnel/rschrade" target=_blank>
 * Professor Roger Schrader</a>.
 * This runs the catch program.
 *
 * @param[in] myargc - The argument count for the catch program
 * @param[in] myargv - The path to what program catch will test
 *
 *****************************************************************************/
void makeCATCH ( int& myargc, char**& myargv )
{

    myargc = 1;
    myargv = new ( nothrow ) char* [myargc];

    myargv[0] = new ( nothrow ) char[strlen ( "x64\\debug\\final.exe" ) + 1];
    strcpy ( myargv[0], "x64\\debug\\final.exe" );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function opens a file determined by argv. The
 * function also checks for successful file opening and will display an error
 * message, close the file as a failsafe, and exit the program if the
 * check fails.
 *
 * @param[in] fin - an input file stream, used to get the 1st deck of cards
 * @param[in] name - the name of the file to open, holds the 1st deck
 * @param[in] fin2 - another input file stream, used to ge tthe 2nd deck 
 * @param[in] name2 - the name of the file to open, holds the 2nd deck
 *
 *****************************************************************************/
void open2Files ( ifstream& fin, ifstream& fin2, char name[], char name2[] )
{
    int i = 0;

    // Open the file, check that it opened, call close1File if it didn't
    fin.open ( name );

    if ( !fin.is_open ( ) )
    {
        //print an error message
        cout << "Unable to open output file: ";

        // print the unopened file
        while ( name[i] != '\0' )
        {
            cout << name[i];
            i++;
        }

        cout << endl;

        close2Files ( fin, fin2, 2 );
    }


    fin2.open ( name2 );
    if ( !fin2.is_open ( ) )
    {
        //print an error message
        cout << "Unable to open output file: ";

        // print the unopened file
        while ( name2[i] != '\0' )
        {
            cout << name2[i];
            i++;
        }

        cout << endl;

        close2Files ( fin, fin2, 2 );
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is an overloaded equality operator for the card struct. It checks if
 * the face value of a card is equal to the face value of the given card.
 *
 * @param[in] aCard - the card that is being compared to
 *
 * @returns true - the card values are equal
 * @returns false - the card values are not equal
 *
 *****************************************************************************/
bool card::operator== ( card aCard )
{
    // check is the cards have the same number
    return this->faceValue == aCard.faceValue;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is an overloaded equality operator for the card struct. It checks if
 * the face value of a card is equal to the face value of the given card.
 *
 * @param[in] aCard - the card that is being compared to
 *
 * @returns true - the card values are equal
 * @returns false - the card values are not equal
 *
 *****************************************************************************/
bool card::operator!= ( card aCard )
{
    return this->faceValue != aCard.faceValue;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is an overloaded less than operator for the card struct.
 * It checks if the face value of a card is less than the face value of the
 * given card.
 *
 * @param[in] aCard - the card that is being compared to
 *
 * @returns true - aCard's value is greater
 * @returns false - aCard's value is lesser
 *
 *****************************************************************************/
bool card::operator< ( card aCard )
{
    return this->faceValue < aCard.faceValue;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is an overloaded bit shift operator being used as an output operator.
 * This outputs the face value and suit of a card. The card values are
 * stored as integers, so aspects (such as the suit) are converted to
 * characters as needed.
 *
 * @param[in, out] out - the stream to be output to
 * @param[in] aCard - the card to be output
 *
 * @returns out - the stream that is being output to
 *
 *****************************************************************************/
ostream& operator<< ( ostream& out, card aCard )
{
    char face;
    char suit;

    // convert the face and suit into chars if applicable
    face = convertFaceValue ( aCard.faceValue );
    suit = convertSuit ( aCard.suit );

    // if face = 0, the face value is valid as an int
    if ( face == 0 )
        out << aCard.faceValue;

    // print the face and suit
    else
        out << face;

    out << suit;

    return out;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function will play a round of war. Each player will discard one card 
 * and whoever discarded the higher card will add the losers discard into 
 * their deck then add their own discard to their deck. If the two cards tied,
 * three ( or however many are left if a player has less than three cards 
 * left ) cards will be discarded where the third will be the deciding card.
 * If the third cards tie, the process will repeat until the tie is broken or
 * one deck's final card is hit. If the final card is hit and both are tied,
 * both players will add their own discard piles into their own hands. 
 *
 * @param[in] player1 - player 1's hand 
 * @param[in] player2 - player 2's hand
 *
 *****************************************************************************/
void playRound ( Queue<card>& player1, Queue<card>& player2 )
{
    bool denqueue1;  // stores if player 1's de/enqueue succeeded or failed
    bool denqueue2;  // stores if player 2's de/enqueue succeeded or failed

    card p1;        // player 1's most recent discard
    card p2;        // player 2's most recent discard

    int i = 0;

    stack<card> s1; // player 1's discard stack
    stack<card> s2; // player 2's discard stack


    // dequeue from queue, add to stack
    discard1 ( player1, s1, denqueue1, p1, player2, s2, denqueue2, p2 );

    // if initial dequeue failed, one or more queue is already empty
    if ( denqueue1 == false || denqueue2 == false )
    {
        // get back any discarded cards if they exist
        getDiscard ( player1, s1 );
        getDiscard ( player2, s2 );

        return;
    }

    // if the discard is tied, triple discard, compare the 3rd until not tied
    while ( p1 == p2 && denqueue1 && denqueue2 )
    {
        // if either queue runs out, match dequeues on both sides
        while ( denqueue1 && denqueue2 && i < 3 )
        {
            discard1 ( player1, s1, denqueue1, p1,
                       player2, s2, denqueue2, p2 );
            i++;
        }

        i = 0;
    }


    // if final cards tie, each take own discard
    if ( p1 == p2 )
    {
        getDiscard ( player1, s1 );
        getDiscard ( player2, s2 );
        return;
    }


    // winner gets opponent's discard, then own discard
    if ( p1 < p2 )
    {
        // player 2 won
            getDiscard ( player2, s1 );
            getDiscard ( player2, s2 );

        return;
    }

    // player 1 won
        getDiscard ( player1, s2 );
        getDiscard ( player1, s1 );

    return;
}



/** ***************************************************************************
 * @author Zoe Millage and <a href = 
 * "https://www.sdsmt.edu/Directories/Personnel/Schrader,-Roger/" 
 * target=_blank> Roger Schrader</a>
 *
 * @par Description:
 * Reads in card values from a file and converts them into cards
 * for the given player's card queue.
 *
 * @param[in] fin - the file to read from
 * @param[in] player - the player's hand
 * @param[in] suit - the suit of the cards
 *
 *****************************************************************************/
void readCards ( ifstream& fin, Queue<card>& player, int suit )
{
    card aCard;
    
    int cardValue;

    //get the card
    while ( fin >> cardValue )
    {
        // convert into usable information for the card struct
        aCard.faceValue = cardValue % 13;       // 0 = ace, 9 = ten...
        aCard.suit = cardValue / 13;            // 0 = H, 1 = D, 2 = C, 3 = S

        player.enqueue ( aCard );
    }


    return;
}





TEST_CASE ( "Queue::enque - Inserting into a queue" )
{
    // by extension of the way this is tested, this test case also
    // tangentially tests .empty and the << operator overload for queues

    ostringstream sout;

    Queue<int> queue1;

    SECTION ( "Empty queue" )
    {
        // because we're using a template queue, all datatypes should work
        // other testcases will use different data types

        REQUIRE ( queue1.empty ( ) == true );

        sout << queue1;
        REQUIRE ( sout.str ( ) == "" );
    }

    REQUIRE ( queue1.enqueue ( 1 ) == true );
    REQUIRE ( queue1.empty ( ) == false );


    SECTION ( "1 item" )
    {
        sout << queue1;
        REQUIRE ( sout.str ( ) == "1" );
    }


    SECTION ( "Multiple items" )
    {
        REQUIRE ( queue1.enqueue ( 2 ) == true );
        REQUIRE ( queue1.enqueue ( 3 ) == true );
        REQUIRE ( queue1.enqueue ( 4 ) == true );
        REQUIRE ( queue1.enqueue ( 5 ) == true );

        sout << queue1;
        REQUIRE ( sout.str ( ) == "1 2 3 4 5" );
    }
}



TEST_CASE ( "Queue::dequeue - removing from a queue" )
{
    ostringstream sout;

    Queue<string> queue1;

    string word = "";


    SECTION ( "Empty queue" )
    {
        REQUIRE ( queue1.empty ( ) == true );

        REQUIRE ( queue1.dequeue ( word ) == false );

        sout << queue1;
        REQUIRE ( sout.str ( ) == "" );
    }


    SECTION ( "1 deletion, final node" )
    {
        CHECK ( queue1.enqueue ( "one" ) == true );
        REQUIRE ( queue1.dequeue ( word ) == true );
        REQUIRE ( word == "one" );

        sout << queue1;
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

        sout << queue1;
        REQUIRE ( sout.str ( ) == "one three five" );
    }
}



TEST_CASE ( "convertFaceValue - convert from int to a card value" )
{
    char faceValue;

    int value1, value2, value3, value4;

    SECTION ( "valid as characters" )
    {
        value1 = 0;
        value2 = 10;
        value3 = 11;
        value4 = 12;

        faceValue = convertFaceValue ( value1 );
        REQUIRE ( faceValue == 'A' );

        faceValue = convertFaceValue ( value2 );
        REQUIRE ( faceValue == 'J' );

        faceValue = convertFaceValue ( value3 );
        REQUIRE ( faceValue == 'Q' );

        faceValue = convertFaceValue ( value4 );
        REQUIRE ( faceValue == 'K' );
    }


    SECTION ( "valid as integers" )
    {
        value1 = 1;
        value2 = 3;
        value3 = 8;
        value4 = 9;

        faceValue = convertFaceValue ( value1 );
        REQUIRE ( faceValue == 0 );
        REQUIRE ( value1 == 2 );

        faceValue = convertFaceValue ( value2 );
        REQUIRE ( faceValue == 0 );
        CHECK ( value2 == 4 );

        faceValue = convertFaceValue ( value3 );
        REQUIRE ( faceValue == 0 );
        CHECK ( value3 == 9 );

        faceValue = convertFaceValue ( value4 );
        REQUIRE ( faceValue == 0 );
        REQUIRE ( value4 == 10 );
    }
}



TEST_CASE ( "convertSuit - convert from int to the proper char" )
{
    char suit;

    int suitValue1, suitValue2, suitValue3, suitValue4;

    suitValue1 = 0;
    suitValue2 = 1;
    suitValue3 = 2;
    suitValue4 = 3;


    suit = convertSuit ( suitValue1 );
    REQUIRE ( suit == 'H' );

    suit = convertSuit ( suitValue2 );
    REQUIRE ( suit == 'D' );

    suit = convertSuit ( suitValue3 );
    REQUIRE ( suit == 'C' );

    suit = convertSuit ( suitValue4 );
    REQUIRE ( suit == 'S' );
}



TEST_CASE ( "playRound - play a round of war " )
{
    // everything until the end of SECTION ( "single Card win" ) was provided
    // by Roger Schrader

    // by testing playRound, we are also testing the smaller functions that 
    // make up playRound, including overloading operators for the card struct
    card aCard;
    int i;
    ostringstream sout1, sout2;
    Queue<card> p1, p2;
    int p1Cards[2] = { 4, 8 };
    int p2Cards[2] = { 2, 7 };

    for ( i = 0; i < 2; i++ )
    {
        aCard.faceValue = p1Cards[i];
        aCard.suit = 0;                 // gave it a suit of hearts
        p1.enqueue ( aCard );
    }

    for ( i = 0; i < 2; i++ )
    {
        aCard.faceValue = p2Cards[i];
        aCard.suit = 0;                 // gave it a suit of hearts
        p2.enqueue ( aCard );
    }


    SECTION ( "Single Card win" )
    {
        playRound ( p1, p2 );
        sout1 << p1;
        sout2 << p2;
        REQUIRE ( sout1.str ( ) == "9H 3H 5H" );
        REQUIRE ( sout2.str ( ) == "8H" );
    }


    SECTION ( "multiple rounds" )
    {
        playRound ( p1, p2 );
        playRound ( p1, p2 );
        sout1 << p1;
        sout2 << p2;
        REQUIRE ( sout1.str ( ) == "3H 5H 8H 9H" );
        REQUIRE ( sout2.str ( ) == "" );
    }


    SECTION ( "tie" )
    {
        int p3Cards[4] = { 6, 2, 7, 5 };
        int p4Cards[4] = { 6, 2, 7, 4 };
        Queue<card> p3, p4;

        for ( i = 0; i < 4; i++ )
        {
            aCard.faceValue = p3Cards[i];
            aCard.suit = 1;                 // gave it a suit of diamonds
            p3.enqueue ( aCard );
        }

        for ( i = 0; i < 4; i++ )
        {
            aCard.faceValue = p4Cards[i];
            aCard.suit = 2;                 // gave it a suit of clubs
            p4.enqueue ( aCard );
        }

        playRound ( p3, p4 );
        sout1 << p3;
        sout2 << p4;
        REQUIRE ( sout1.str ( ) == "5C 8C 3C 7C 6D 8D 3D 7D" );
        REQUIRE ( sout2.str ( ) == "" );
    }


    SECTION ( "multiple tie, less than 7 cards" )
    {
        int p3Cards[6] = { 6, 2, 7, 5, 4, 4 };
        int p4Cards[6] = { 6, 2, 7, 5, 4, 3 };
        Queue<card> p3, p4;

        for ( i = 0; i < 6; i++ )
        {
            aCard.faceValue = p3Cards[i];
            aCard.suit = 1;                 // gave it a suit of diamonds
            p3.enqueue ( aCard );
        }

        for ( i = 0; i < 6; i++ )
        {
            aCard.faceValue = p4Cards[i];
            aCard.suit = 3;                 // gave it a suit of spades
            p4.enqueue ( aCard );
        }

        playRound ( p3, p4 );
        sout1 << p3;
        sout2 << p4;
        CHECK ( sout1.str ( ) == "4S 5S 6S 8S 3S 7S 5D 5D 6D 8D 3D 7D" );
        REQUIRE ( sout2.str ( ) == "" );
    }


    SECTION ( "final card ties" )
    {
        int p3Cards[4] = { 6, 2, 7, 4 };
        int p4Cards[4] = { 6, 2, 7, 4 };
        Queue<card> p3, p4;

        for ( i = 0; i < 4; i++ )
        {
            aCard.faceValue = p3Cards[i];
            aCard.suit = 2;                 // gave it a suit of clubs
            p3.enqueue ( aCard );
        }

        for ( i = 0; i < 4; i++ )
        {
            aCard.faceValue = p4Cards[i];
            aCard.suit = 3;                 // gave it a suit of spades
            p4.enqueue ( aCard );
        }

        playRound ( p3, p4 );
        sout1 << p3;
        sout2 << p4;
        REQUIRE ( sout1.str ( ) == "5C 8C 3C 7C" );
        REQUIRE ( sout2.str ( ) == "5S 8S 3S 7S" );
    }
}