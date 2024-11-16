/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the class, struct, and function declarations, along with the
 * code for a queue template class.
 *****************************************************************************/
#include <fstream>
#include <iomanip>
#include <iostream>
#include <random>
#include <stack>

using namespace std;


/******************************************************************************
 *             Classes, Constant Variables,  Defines, and Structs
 *****************************************************************************/
#ifndef __FINAL__H__
#define __FINAL__H__

 /**
 * @brief Tells the program to run catch
 */
const bool RUNCATCH = true;


 /**
  * @brief Holds the data for a given card
  */
struct card
{
    int faceValue;  /**< The value of the card ( 2, 3, Ace, King, etc ). */
    int suit;       /**< The suit of the card ( club, spade, etc ). */

    bool operator== ( const card aCard );
    bool operator!= ( const card aCard );
    bool operator< ( const card aCard );
};


/**
* @brief a Queue; used as a player's hand for this program
*/
template <class TY>
class Queue
{
    public:
        Queue ( );
        Queue ( Queue<TY>& q );
        ~Queue ( );

        bool dequeue ( TY& element );
        bool empty ( );
        bool enqueue ( TY element );

        bool operator== ( const Queue<TY> q );

    private:

        /**
         * @brief Allows the queue's data to be stored like a linked list
         */
        struct node
        {
            TY data;        /**< The data the queue holds */
            node* next;     /**< A pointer to the next node in the queue */
        };

        node* headptr;      /**< A pointer to the first element in the queue */
        node* tailptr;      /**< A pointer to the last element in the queue */
};



/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
void checkCLineArgs ( int argc, char** argv );
void close2Files ( ifstream& fin, ifstream& fin2, int exitCode );
char convertFaceValue ( int& faceValue );
void convertSeeds ( char arr1[], int& seed1, char arr2[], int& seed2 );
char convertSuit ( int suit );
void discard1 ( Queue<card>& p1, stack<card>& s1, bool& check1, card& card1,
                Queue<card>& p2, stack<card>& s2, bool& check2, card& card2 );
void generateCards ( Queue<card>& player, int seed );
void getDiscard ( Queue<card>& hand, stack<card>& discard );
void makeCATCH ( int& myargc, char**& myargv );
void open2Files ( ifstream& fin, ifstream& fin2, char name[], char name2[] );
ostream& operator<< ( ostream& out, card aCard );

template <class TY> 
ostream& operator<< ( ostream& out, Queue<TY> q );

void playRound ( Queue<card>& player1, Queue<card>& player2 );

template <class TY>
void printResults ( ostream& out, Queue<TY> p1, Queue<TY> p2, int rounds );

void readCards ( ifstream& fin, Queue<card>& player, int suit );

#endif


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the constructor for the Queue. It sets headptr and tailptr (the
 * pointers to the first and last nodes, respectively) to nullptr.
 *
 *****************************************************************************/
template <class TY>
Queue<TY>::Queue ( )
{
    // set headptr and tailptr to nullptr
    headptr = nullptr;
    tailptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the copy constructor for the queue. It will walk through the given
 * Queue, duplicating every element into a new queue.
 *
 * @param[in] q - the queue to copy
 *
 *****************************************************************************/
template <class TY>
Queue<TY>::Queue ( Queue& q )
{
    node* dest; // the new node for the duplicate queue
    node* src;  // the node from q to copy

    headptr = nullptr;
    tailptr = nullptr;

    // a null headptr prevents traversal even if the list isn't empty
    if ( q.headptr == nullptr )
        return;

    // dynamically allocate headptr and error check
    headptr = new ( nothrow ) node;
    if ( headptr == nullptr )
        exit ( 0 );

    // duplicate headptr
    headptr->data = q.headptr->data;
    headptr->next = nullptr;

    src = q.headptr->next;
    dest = headptr;

    // duplicate the queue
    while ( src != nullptr )
    {
        // dynamically allocate a new node and error check
        dest->next = new( nothrow ) node;
        dest = dest->next;
        if ( dest == nullptr )
            exit ( 0 );

        // copy the data from the original queue to the duplicate
        dest->data = src->data;
        dest->next = nullptr;

        // move onto the next node
        src = src->next;
    }

    // initialize tailptr
    tailptr = dest;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the deconstructor for the Queue. It will check if the first node
 * of the queue is null, delete any nodes if they exist, then reset tailptr to
 * null once no nodes exist.
 *
 *****************************************************************************/
template <class TY>
Queue<TY>::~Queue ( )
{
    node* temp = nullptr;


    temp = headptr;

    // walk through the list, removing any node that exists
    while ( headptr != nullptr )
    {
        headptr = headptr->next;
        delete temp;
        temp = headptr;
    }

    // reset tailptr
    tailptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function deletes the first element in a queue and sets headptr
 * appropriately to the modified queue. The deleted element is stored into
 * a separate variable.
 *
 * @param[in] element - the element that is deleted
 *
 * @returns true - the dequeue succeeded
 * @returns false - the dequeue failed
 *
 *****************************************************************************/
template <class TY>
bool Queue<TY>::dequeue ( TY& element )
{
    node* temp = nullptr;

    // empty lists have nothing to dequeue
    if ( empty ( ) == true )
        return false;

    // set element to what's about to be deleted from the queue
    temp = headptr;
    element = temp->data;

    // change headptr and delete the node
    headptr = headptr->next;
    delete temp;

    // account for the final node being deleted
    if ( headptr == nullptr )
        tailptr = nullptr;

    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function checks if a queue is empty or not.
 *
 * @returns true - the queue is empty
 * @returns false - the queue is not empty
 *
 *****************************************************************************/
template <class TY>
bool Queue<TY>::empty ( )
{
    // if the list's empty, return true, return false if not
    if ( headptr == nullptr && tailptr == nullptr )
        return true;

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function adds a new node to the end of the queue.
 *
 * @param[in] element - the item to enqueue
 *
 * @returns true - the enqueue succeeded
 * @returns false - the enqueue failed
 *
 *****************************************************************************/
template <class TY>
bool Queue<TY>::enqueue ( TY element )
{
    node* temp = nullptr;

    // dynamically allocate a node and error check
    temp = new ( nothrow ) node;

    if ( temp == nullptr )
        return false;

    // initialize the new node
    temp->data = element;
    temp->next = nullptr;

    // adding the first node
    if ( empty ( ) == true )
    {
        headptr = temp;
        tailptr = temp;
        return true;
    }

    // update the tailptr
    tailptr->next = temp;
    tailptr = temp;
    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the overloaded equality operator for the queue class. It will check
 * if the value of each node is equal. Queues of different lengths are
 * never equal.
 *
 * @param[in] q - the queue that is being compared to
 *
 * @returns true - the queues are equal
 * @returns false - the queues are not equal
 *
 *****************************************************************************/
template <class TY>
bool Queue<TY>::operator== ( const Queue<TY> q )
{
    node* temp = q.headptr;
    node* temp2 = q.tailptr;

    // two empty queues are equal
    if ( empty ( ) )
    {
        if ( temp == nullptr && temp2 == nullptr )
            return true;

        else
            return false;
    }

    temp = this->headptr;
    temp2 = q.headptr;

    // walk through the queue and check each value
    while ( temp != nullptr )
    {
        // different sized queues aren't equal
        if ( temp2 == nullptr )
            return false;

        // check for differences
        if ( temp->data != temp2->data )
            return false;

        // get the next elements
        temp = temp->next;
        temp2 = temp2->next;
    }

    // If no differences were found, the queues are equal
    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is an overloaded bit shift operator being used as an output operator.
 * This outputs the data of all nodes of a queue separated by a space.
 *
 * @param[in, out] out - the stream to be output to
 * @param[in] q - the queue to print
 *
 * @returns out - the stream that is being output to
 *
 *****************************************************************************/
template <class TY>
ostream& operator<< ( ostream& out, Queue<TY> q )
{
    bool dequeueTF; // holds whether a dequeue call returned true or false

    TY element;

    // get the first value of the queue
    dequeueTF = q.dequeue ( element );

    // output each element. Output a space if the full queue hasn't printed
    while ( dequeueTF == true )
    {
        out << element;

        if ( q.empty ( ) == false )
            out << " ";

        dequeueTF = q.dequeue ( element );
    }

    return out;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This prints the results of the game. If the queues started equal, an
 * appropriate error message will be printed. This will print the
 * player's hands and who won in how many rounds.
 *
 * @param[in, out] out - the stream to print to
 * @param[in, out] p1 - player 1's hand
 * @param[in, out] p2 - player 2's hand
 * @param[in] rounds - the number of rounds it took for the game to finish
 *
 *****************************************************************************/
template <class TY>
void printResults ( ostream& out, Queue<TY> p1, Queue<TY> p2, int rounds )
{
    if ( rounds == -1 )
    {
        out << "The queues are equal. No winner can be found." << endl
            << "Player 1 hand: " << p1 << endl
            << "Player 2 hand: " << p2 << endl;
        exit ( 0 );
    }

    if ( p1.empty ( ) )
        out << "Player 2 wins ";

    else
        out << "Player 1 wins ";

    out << "after " << rounds << " rounds." << endl
        << "Player 1 hand: " << p1 << endl
        << "Player 2 hand: " << p2 << endl;

    return;
}