/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the implimentation of the sortedSingle class's functions.
 *****************************************************************************/

#include "sortedSingle.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Default constructor, creates an empty list by initializing headptr.
 *
 *****************************************************************************/
sortedSingle::sortedSingle()
{
    // initialize headptr
    headptr = nullptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Destructor, frees all nodes within the linked list.
 *
 *****************************************************************************/
sortedSingle::~sortedSingle ( )
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
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Frees up all the node in the list.
 *
 *****************************************************************************/
void sortedSingle::clear ( )
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

    headptr = nullptr;

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Determines if the list contains any items.
 *
 * @returns true if the list is empty, false if not.
 * 
 *****************************************************************************/
bool sortedSingle::empty ( )
{
    // if the list's empty, return true, return false if not
    if ( headptr == nullptr )
        return true;

    else
        return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Searches for the first match of a given integer.
 *
 * @param[in] item - The item to locate within the list.
 *
 * @returns true if the item is in the list, false if not.
 *
 *****************************************************************************/
bool sortedSingle::find ( int item )
{
    node* curr = nullptr;   // current location

    // return false if the list is empty
    if ( headptr == nullptr )
        return false;

    curr = headptr;

    // walk though the list and return true if the wanted item is found
    while ( curr != nullptr && curr->theItem <= item )
    {
        if ( curr->theItem == item )
            return true;

        curr = curr->next;
    }

    return false;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Inserts the given item into the linked list. The item is placed in the 
 * correct position to keep the list sorted.
 *
 * @param[in] item - The integer to be inserted
 *
 * @returns true if successful, false if not.
 *
 *****************************************************************************/
bool sortedSingle::insert ( int item )
{
    node* temp = nullptr;
    node* prev = nullptr;   // previous location
    node* curr = nullptr;   // current location


    // create a new node
    temp = new ( nothrow ) node;

    if ( temp == nullptr )
        return false;

    // insert the node to the front if the list is empty
    if ( headptr == nullptr )
    {
        temp->theItem = item;
        temp->next = nullptr;
        headptr = temp;
        return true;
    }

    temp->theItem = item;
    prev = headptr;
    curr = headptr;

    /* walk through the list until a spot is found to place the new node
       such that all elements will be in increasing order */
    while ( curr != nullptr && curr->theItem < item )
    {
        prev = curr;
        curr = curr->next;
    }

    
    /* insert the node to the beginning of the list if its contents are 
       less than all existing nodes in the list */
    if ( curr == headptr )
    {
        temp->next = curr;
        headptr = temp;
        return true;
    }

    //insert the node in the appropirate space
    temp->next = curr;
    prev->next = temp;
    return true;

}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Displays the data to a stream with a given seperator used between two pieces of 
 * data. The last item will not be followed by the seperator.
 *
 * @param [in,out]  out - The stream to print the data to
 * @param[in] seperator - The string to be output between data
 *
 *****************************************************************************/
void sortedSingle::print ( ostream& out, string seperator )
{
    node* temp = nullptr;

    // do not print an empty list
    if ( headptr == nullptr )
        return;

    temp = headptr;

    // walk through the list, inserting the seperator between valid nodes
    while ( temp != nullptr )
    {
        out << temp->theItem;

        if ( temp->next != nullptr )
            out << seperator;

        temp = temp->next;
    }

}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Removes the first instance of the given item from the list.
 *
 * @param[in] item - The item to be removed from the list.
 *
 * @returns true if successful, false if not.
 *
 *****************************************************************************/
bool sortedSingle::remove ( int item )
{
    node* prev = nullptr;   // previous location
    node* curr = nullptr;   // current location

    // return false if the list is empty
    if ( headptr == nullptr )
        return false;

    prev = headptr;
    curr = headptr;

    // walk through list until the item is found or the list ends
    while ( curr != nullptr &&  curr->theItem != item )
    {
        prev = curr;
        curr = curr->next;
    }

    
    if ( curr == nullptr )
        return false;


    // account for removal of the 1st element
    if ( curr == headptr )
    {
        headptr = curr->next;
        delete curr;
        return true;
    }

    // remove the element
    prev->next = curr->next;
    delete curr;
    return true;

}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Retrieves a position of the given item, with counting starting at 1.
 * 0 is returned for an item not found, so this function can be tested
 * for true/false.
 * 
 * @param[in] item - the item to locate within the list
 *
 * @returns the position of the item, 0 if it is not found.
 *
 *****************************************************************************/
int sortedSingle::retrievePosition ( int item )
{
    int location = 1;
    node* curr = nullptr;   // current location

    // return false if the list is empty
    if ( headptr == nullptr )
        return 0;

    curr = headptr;

    // walk through the list and return the wanted item's location if found
    while ( curr != nullptr && curr->theItem <= item )
    {
        if ( curr->theItem == item )
            return location;

        curr = curr->next;
        ++location;
    }
    
    return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Returns the number of nodes in the linked list.
 *
 * @returns the number of items in the list.
 *
 *****************************************************************************/
int sortedSingle::size (  )
{
    int size = 0;
    node* curr = nullptr;   // current location

    // return 0 if the list is empty
    if ( headptr == nullptr )
        return 0;

    curr = headptr;

    // walk though the list, incrementing size for each valid node
    while ( curr != nullptr )
    {
        curr = curr->next;
        ++size;
    }

    return size;
}