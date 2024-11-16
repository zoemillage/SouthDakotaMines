/** **************************************************************************
 * @file    
 *
 * @brief The declaration of the sorted singly linked list for a list.
 ****************************************************************************/
#include <iostream>
#include <string>
using std::ostream;
using std::string;
using std::nothrow;


/** **************************************************************************
 * @class sortedSingle
 *
 * @brief An ascending sorted singly linked list of integers.
 ****************************************************************************/
class sortedSingle
{
    public:

    sortedSingle( );
    ~sortedSingle( );

    void clear( );
    bool empty( );
    bool find( int item );
    bool insert( int item );
    void print( ostream &out, string seperator = ", " );
    bool remove( int item );
    int retrievePosition( int item );
    int size( );
    

    private:

    /** 
     * @brief the storage container for each item in the list 
     */
    struct node
    {

        int theItem;    /*!< the item */
        node *next;     /*!< pointer to the remaining nodes in list */
    };
    node *headptr;      /*!< The location of the first node in the list */
};

