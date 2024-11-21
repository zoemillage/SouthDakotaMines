/** ***************************************************************************
 * @file    
 *
 * @brief   Declaration of my queue class
 *****************************************************************************/
#include <iostream>
#include <new>
#include <string>
using std::string;
using std::nothrow;
using std::ostream;

#ifndef __QUEUE__H__
#define __QUEUE__H__



/** ***************************************************************************
 * @brief   A Queue.
 *
 * @author  Roger L. Schrader
 * @date    10/28/2020
 *****************************************************************************/
class Queue
{
    public:
    Queue( );
    Queue( Queue &q );
    ~Queue( );

    bool enqueue( string word );
    bool dequeue( string &word );
    bool front( string &word );

    int size( );
    bool empty( );
    void clear( );
    void swap( Queue &anotherQ );


    void print( ostream &out );


    bool operator==( const Queue q );
    bool operator!=( Queue q );
    Queue &operator=( Queue q );

    private:

    /** ***********************************************************************
     * @brief   A node for use in linked list that implements a Queue
     *
     * @author  Roger L. Schrader
     * @date    10/28/2020
     *************************************************************************/
    struct node
    {
        string item;    /*!< The value at each node */
        node *next;     /*!< The pointer to the following node in the Queue */
    };
    node *headptr;  /*!< A pointer to the first item in the Queue */
    node *tailptr;  /*!< A pointer to the last item in the Queue */
};



#endif
