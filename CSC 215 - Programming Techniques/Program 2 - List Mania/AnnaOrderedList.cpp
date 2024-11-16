/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the orderdList class's destructor, isEmpty, 
 * remove, and size functions.
 *****************************************************************************/
#include "orderedList.h"


/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description:
 * This is the destructor for the list. If the list is not already empty, this
 * function will walk through it, removing all nodes encountered. 
 *
 *****************************************************************************/
orderedList::~orderedList()
{
    node* temp = nullptr;
    node* c = nullptr;

    // Check that the list is not empty
    if (tailptr == nullptr)
        return;

    temp = tailptr->next;

    // Walk through the list, deleting all nodes
    while (temp->next != tailptr)
    {
        c = temp->next;
        temp->next = c->next;
        delete c;
    }
}



/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description:
 * This function checks the value of tailptr to determine whether the list 
 * is empty or not.
 *
 * @returns True - the list is empty.
 * @returns False -the list is not empty.
 *
 *****************************************************************************/
bool orderedList::isEmpty ( )
{    
    // Check if the list is empty
    if (tailptr == nullptr)
        return true;

    return false;
}



/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description: 
 * This function will receive a number corresponding to the value of a node in
 * a list. If a node of matching value is found, it will be removed from the
 * list. If the list is empty or a node of the desired value is not found 
 * within the list, it cannot be removed. 
 *
 * @param[in] num - The value of the node to be removed.
 *
 * @returns True - the element was removed. 
 * @returns False - the element could not be removed. 
 *
 *****************************************************************************/
bool orderedList::remove ( int num )
{
    node* temp = nullptr;
    node* c = nullptr;
    node* p = nullptr;

    temp = tailptr;

    //empty list
    if (tailptr == nullptr)
        return false;

    //number not in list
    if (find(num) == false)
        return false;

    //last item in list
    if (tailptr->next == tailptr)
    {
        tailptr = nullptr;
        delete temp;
        return true;
    }

    //front of list
    temp = tailptr->next;
    if (temp->item == num)
    {
        tailptr->next = temp->next;
        delete temp;
        return true;
    }

    //middle and end
    else
    {
        c = tailptr->next;
        p = tailptr->next;

        while (c != tailptr && c->item != num)
        {
            p = c;
            c = c->next;
        }
        if (c == tailptr)
            tailptr = p;
        p->next = c->next;
        delete c;
        return true;
    }

    return false;
}



/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description: 
 * This function traverses through a list to determine the total size of the
 * list.
 *
 * @returns The size of the list.
 *
 *****************************************************************************/
int orderedList::size ( )
{
    int count = 1;
    node* temp = nullptr;

    // Return 0 if list is empty
    if (tailptr == nullptr)
        return 0;

    // Walk through list, incrementing count
    temp = tailptr->next;
    while (temp != tailptr)
    {
        temp = temp->next;
        count++;
    }

    return count;
}