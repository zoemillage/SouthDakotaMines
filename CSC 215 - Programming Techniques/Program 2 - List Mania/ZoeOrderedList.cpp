/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the orderedList class's constructor, find, insert, 
 * and print functions
 *****************************************************************************/


#include "orderedList.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the constructor for an ordered list. Nothing is
 * dynamically allocated by this constructor, so this function only sets
 * tailptr, a pointer to the final node of the list, to nullptr.
 *
 *****************************************************************************/
orderedList::orderedList ( )
{
    // set tailptr to nullptr
    tailptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function receives a value and tries to find it
 * within an ordered list. All values within the list are integers.
 *
 * @param[in] num - The value to be searched for
 *
 * @returns true - The value was found.
 * @returns false - The value was not found.
 *
 *****************************************************************************/
bool orderedList::find ( int num )
{
    node* c = nullptr;   // current location

    // return false if the list is empty
    if ( tailptr == nullptr )
        return false;

    c = tailptr;

    // walk though the list and return true if the wanted item is found
    do
    {
        c = c->next;
        if ( c->item == num )
            return true;

    }     while ( c != tailptr );

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function dynamically allocates a new node for
 * an ordered list and inserts it into the list. The list is made of intergers
 * and the nodes are sorted into increasing order.
 *
 * @param[in] num - The value of the node to be inserted
 *
 * @returns true - The node was inserted successfully.
 * @returns false - The node was not inserted.
 *
 *****************************************************************************/
bool orderedList::insert ( int num )
{
    node* temp = nullptr;
    node* p = nullptr;   // previous location
    node* c = nullptr;   // current location

    // create a new node and error check
    temp = new ( nothrow ) node;

    if ( temp == nullptr )
        return false;

    temp->item = num;

    // insert node to the front of the list if it's empty
    if ( tailptr == nullptr )
    {
        temp->next = temp;
        tailptr = temp;
        return true;
    }

    p = tailptr;
    c = tailptr;

    // walk through list until temp would be in place for an ascending list
    do
    {
        p = c;
        c = c->next;
    }     while ( c != tailptr && c->item < num );


    // front of list insert
    if ( p == tailptr )
    {
        temp->next = c;
        tailptr->next = temp;
        return true;
    }

    // middle of list insert
    if ( c->item >= num )
    {
        temp->next = c;
        p->next = temp;
        return true;
    }

    // tailptr was hit, end of list insert
    if ( c == tailptr )
    {
        temp->next = tailptr->next;
        c->next = temp;
        tailptr = temp;
        return true;
    }


    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function takes an ordered list and outputs it to
 * an ostream. This function will print the values in the list 5 per line
 * where each data point can take up to 10 characters.
 *
 * @param[in, out] out - The stream the list will be output to
 *
 *****************************************************************************/
void orderedList::print ( ostream &out )
{
    int endlCheck = 1;

    node* temp = nullptr;

    // empty lists have nothing to print
    if ( tailptr == nullptr )
        return;

    temp = tailptr;

    // walk through the list and print everything in it
    do
    {
        temp = temp->next;
        out << setw ( 10 ) << setfill ( ' ' );
        out << temp->item << " ";

        // output an endl every 5 elements
        if ( endlCheck != 0 && endlCheck % 5 == 0 )
            out << endl;

        endlCheck++;

    }     
    while ( temp != tailptr );

    // ensure uniform formatting between lists
    if ( ( endlCheck - 1 ) % 5 != 0 )
        out << endl;

    return;
}