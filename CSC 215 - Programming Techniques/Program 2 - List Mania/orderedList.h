/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the class declaration for the orderedList.
 *****************************************************************************/

#include <iostream>
#include <iomanip>
#include <fstream>
#include <list>

using namespace std;

#ifndef __ORDEREDLIST__H__
#define __ORDEREDLIST__H__


// class declaration
/**
* @brief Holds the data for a circularly linked list. Provided by <a href=
* "http://sdmines.sdsmt.edu/sdsmt/directory/personnel/rschrade" target=_blank>
* Professor Roger Schrader</a>.
*/
class orderedList
{
public:
    orderedList ( );
    ~orderedList ( );

    bool find ( int num );
    bool insert ( int num );
    bool isEmpty ( );
    void print ( ostream& out );
    bool remove ( int num );
    int size ( );

private:
    /**
    * @brief Holds each individual data point in the linked list. 
    * Provided by <a href=
    * "http://sdmines.sdsmt.edu/sdsmt/directory/personnel/rschrade" 
    * target=_blank> Professor Roger Schrader</a>.
    */
    struct node
    {
        int item;                   /**< The value of each data point */
        node* next;                 /**< The memory address for the next
                                    data point */
    };
    node* tailptr;                  /**<  The memory address of the last data
                                    point */
};

#endif