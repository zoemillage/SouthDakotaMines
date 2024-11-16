/** ***************************************************************************
 * @file    
 *
 * @brief   Implements the myQueue class functions and operators.
 *****************************************************************************/
  
#include "myQueue.h"


/** ***************************************************************************
 * @brief   Default constructor
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *****************************************************************************/
Queue::Queue( )
{
    headptr = nullptr;
    tailptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Copies one queue to another at instanitation.
 *
 * @param[in, out] q - the queue to duplicate 
 *
 *****************************************************************************/
Queue::Queue( Queue &q )
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
    headptr->item = q.headptr->item;
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

        dest->item = src->item;
        dest->next = nullptr;

        src = src->next;
    }

    tailptr = dest;
}



/** ***************************************************************************
 * @brief   Destructor
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *****************************************************************************/
Queue::~Queue( )
{
    clear( );
}



/** ***************************************************************************
 * @brief   Clears this object to an empty state.
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *****************************************************************************/
void Queue::clear( )
{
    node *temp = headptr;
    while( headptr != nullptr )
    {
        temp = headptr;
        headptr = headptr->next;
        delete temp;
    }
    tailptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Removes the head object from this Queue.
 *
 * @param[in, out] word - the word that was removed if it exists 
 *
 * @returns True if it succeeds in removing a word, false if it fails.
 *
 *****************************************************************************/
bool Queue::dequeue( string &word )
{
    node* temp = nullptr;

    // empty list
    if ( empty ( ) == true )
        return false;

    temp = headptr;

    word = temp->item;

    // change headptr and delete the node
    headptr = headptr->next;
    delete temp;

    // account for the final node being deleted
    if ( headptr == nullptr )
        tailptr = nullptr;

    return true;
}



/** ***************************************************************************
 * @brief   Checks if the queue contain any items
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *
 * @returns True if it emtpy, false if it contains some items.
 *****************************************************************************/
bool Queue::empty( )
{
    return headptr == nullptr && tailptr == nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Adds an object onto the end of this Queue.
 *
 * @param[in] word - the word to place at the end of the queue
 *
 * @returns True if it succeeds, false if it fails
 *
 *****************************************************************************/
bool Queue::enqueue( string word )
{
    node* temp = nullptr;

    // dynamically allocate a node and error check
    temp = new ( nothrow ) node;

    if ( temp == nullptr )
        return false;

    temp->item = word;
    temp->next = nullptr;

    if ( empty ( ) == true )
    {
        headptr = temp;
        tailptr = temp;
        return true;
    }

    tailptr->next = temp;
    tailptr = temp;
    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Get the first word in the Queue.
 *
 * @param [in,out]  word    The word at the beginning of the Queue.
 *
 * @returns True if it succeeds and a word is at the front, false if it fails.
 * 
 *****************************************************************************/
bool Queue::front( string &word )
{
    node* temp = nullptr;

    if ( empty ( ) == true )
        return false;

    temp = headptr;
    word = temp->item;

    return true;
}



/** ***************************************************************************
 * @brief   Prints the Queue class item out to an ostream.
 *
 * @author  Roger L. Schrader
 * @date    10/30/2020
 *
 * @param [in,out]  out the output stream to print the data at.
 *****************************************************************************/
void Queue::print( ostream &out )
{
    node *temp = headptr;

    while( temp != nullptr )
    {
        out << temp->item;
        if( temp->next )
            out << ", ";
        temp = temp->next;
    }
}



/** ***************************************************************************
 * @brief   Gets the number of items in the queue
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *
 * @returns The number of items in the queue 
 *****************************************************************************/
int Queue::size( )
{
    int count = 0;
    node *temp = headptr;
    while( temp != nullptr )
    {
        count += 1;
        temp = temp->next;
    }
    return count;
}



/** ***************************************************************************
 * @brief   Swaps the given Queue with another Queue
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *
 * @param [in,out]  anotherQ    another Queue.
 *****************************************************************************/
void Queue::swap( Queue &anotherQ )
{
    std::swap( headptr, anotherQ.headptr );
    std::swap( tailptr, anotherQ.tailptr );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Checks if this queue doesn't contain the same elements in the same order as 
 * the given queue.
 *
 * @param[in] q - the other queue to test against
 *
 * @returns True if the queues are different, false otherwise.
 *
 *****************************************************************************/
bool Queue::operator!=( Queue q )
{
    if ( *this == q )
        return false;

    return true;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Makes this queue contain the same elements in the same order as the given
 * queue.
 *
 * @param[in] q - the queue to copy
 *
 *****************************************************************************/
Queue& Queue::operator=( Queue q )
{
    Queue temp ( q );

    // do not do queue1 = queue1
    if ( *this == q )
        return *this;

    // account for the left hand list not being empty
    if ( this->headptr != nullptr )
        clear ( );

    headptr = nullptr;
    tailptr = nullptr;

    swap ( temp );
    return *this;

}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Checks if this queue and the given queue have the same elements in the same 
 * order.
 *
 * @param[in] q - the queue to check against
 *
 * @returns True if the queues are the same, false if not.
 *
 *****************************************************************************/
bool Queue::operator==( const Queue q )
{
    node* temp = q.headptr;
    node* temp2 = q.tailptr;

    // two empty lists are equal
    if ( empty ( ) )
    {
        if ( temp == nullptr && temp2 == nullptr)
            return true;

        else
            return false;
    }

    temp = this->headptr;
    temp2 = q.headptr;

    // walk through the list and check each value
    while ( temp != nullptr )
    {
        if ( temp2 == nullptr )
            return false;

        if ( temp->item != temp2->item )
            return false;

        temp = temp->next;
        temp2 = temp2->next;
    }

    return true;
}