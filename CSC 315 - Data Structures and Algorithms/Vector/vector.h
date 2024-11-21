/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the vector class.
 *****************************************************************************/

#include <algorithm>
#include <iostream>

using namespace std;


#ifndef __VECTOR__H__
#define __VECTOR__H__

/** ***************************************************************************
 * @author Zoe Millage
 *
 * @brief An alternate version of <vector>.
 *****************************************************************************/
template <typename Object>
class Vector
{
  public:
    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief The default constructor.
     *
     * @param[in] initSize - the initial size of the vector
     *
     *************************************************************************/
    explicit Vector( int initSize = 0 ) : theSize { initSize },
            theCapacity{ initSize + SPARE_CAPACITY }
    { 
        // initialize objects
        cout << "Method 1 - default constructor" << endl;
        objects = new Object[ theCapacity ]; 
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Copy contructor. Copy the contents of the given vector.
     *
     * @param[in] rhs - the vector to copy
     *
     *************************************************************************/
    Vector ( const Vector & rhs ) : theSize { rhs.theSize },
        theCapacity { rhs.theCapacity }, objects { nullptr }
    {
        // copy rhs's contents
        cout << "Method 2 - copy constructor" << endl;
        objects = new Object[ theCapacity ];
        for ( int k = 0; k < theSize; ++k )
            objects[ k ] = rhs.objects[ k ];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Move constructor. Become the parameter then delete it.
     *
     * @param[in] rhs - the vector to become
     *
     *************************************************************************/
    Vector(Vector && rhs) : theSize { rhs.theSize },
        theCapacity { rhs.theCapacity }, objects { rhs.objects }
    {
        // erase rhs
        cout << "Method 5 - move constructor" << endl;
        rhs.objects = nullptr;
        rhs.theSize = 0;
        rhs.theCapacity = 0;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief The destructor.
     *
     *************************************************************************/
    ~Vector()
    {
        // clear objects
        cout << "Method 4 - destructor" << endl;
        delete[] objects;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the final element of the vector.
     *
     * @returns the last element in the vector
     *
     *************************************************************************/
    const Object & back ( ) const
    {
        // get the last element
        cout << "Method 17 - back" << endl;
        return objects[ theSize - 1 ];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the capacity of the vector.
     *
     * @returns this vector's capacity
     *
     *************************************************************************/
    int capacity ( ) const
    {
        // get the capacity
        cout << "Method 13 - capacity" << endl;
        return theCapacity;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Returns whether or not the vector is empty.
     *
     * @returns true if empty, false if not
     *
     *************************************************************************/
    bool empty ( ) const
    {
        // return true if empty
        cout << "Method 11 - empty" << endl;
        return size ( ) == 0;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Decreases size by one.
     *
     *************************************************************************/
    void pop_back ( )
    {
        // "remove" an element
        cout << "Method 16 - pop_back" << endl;
        --theSize;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Adds the given element to the vector, checking for capacity.
     *
     * @param[in] x - the object to add
     *
     *************************************************************************/
    void push_back ( const Object & x )
    {
        cout << "Method 14 - push_back (lvalue)" << endl;

        // reserve more capacity if needed
        if ( theSize == theCapacity )
            reserve ( 2 * theCapacity + 1 );

        // add to the size after adding the new value
        objects[ theSize++ ] = x;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Adds the given element to the vector, checking for capacity.
     *
     * @param[in] x - the object to add
     *
     *************************************************************************/
    void push_back ( Object && x )
    {
        cout << "Method 15 - push_back (rvalue)" << endl;

        // reserve more capacity if needed
        if ( theSize == theCapacity )
            reserve ( 2 * theCapacity + 1 );
        
        // add to the size after adding the new value
        objects[ theSize++ ] = std::move ( x );
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Changes the capacity of this vector.
     *
     * @param[in] newCapacity - the new capacity
     *
     *************************************************************************/
    void reserve ( int newCapacity )
    {
        cout << "Method 8 - reserve" << endl;

        // don't go below the size
        if ( newCapacity < theSize )
            return;

        // copy the old objects into a larger array
        Object *newArray = new Object[ newCapacity ];
        for ( int k = 0; k < theSize; ++k )
            newArray[ k ] = std::move ( objects[ k ] );

        // copy/move the appropriate values
        theCapacity = newCapacity;
        std::swap ( objects, newArray );

        // try not to memory leak
        delete[] newArray;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Resizes the vector, copying the last element if becoming larger.
     *
     * @param[in] newSize - the new size of the vector
     *
     *************************************************************************/
    void resize ( int newSize )
    {
        cout << "Method 7 - resize" << endl;
        
        // check if we're above capacity
        if ( newSize > theCapacity )
            reserve ( newSize * 2 );

        // copy the last element if we're going larger. To avoid garbage data.
        if ( newSize > theSize ) {

            Object temp = objects[theSize - 1];
            
            for ( int i = theSize; i < newSize; i++) {
                objects[i] = temp;
            }
        }
        
        // change the size
        theSize = newSize;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Returns the size of the vector.
     *
     * @returns the size of this vector
     *
     *************************************************************************/
    int size ( ) const
    {
        // get the size
        cout << "Method 12 - size" << endl;
        return theSize;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief The copy assignment operator. Become a copy of the given vector.
     *
     * @param[in] rhs - the vector to copy
     *
     * @returns a copy of the given vector
     *
     *************************************************************************/
        Vector & operator= ( const Vector& rhs )
    {
        // become the copy
        cout << "Method 3 - copy assignment" << endl;
        Vector copy = rhs;
        std::swap ( *this, copy );

        return *this;
    }
    


    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief The move assignment operator. Become a copy of the given vector.
     *
     * @param[in] rhs - the vector to copy
     *
     * @returns a copy of the given vector
     *
     *************************************************************************/
    Vector & operator= ( Vector && rhs )
    {
        // copy that vector into this one
        cout << "Method 6 - move assignment" << endl;
        std::swap ( theSize, rhs.theSize );
        std::swap ( theCapacity, rhs.theCapacity );
        std::swap ( objects, rhs.objects );

        return *this;
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the element at the given vector.
     *
     * @param[in] index - the index to retrieve from
     *
     * @returns the element at the specified index
     *
     *************************************************************************/
    Object & operator[] ( int index )
    {
        // get the object
        cout << "Method 9 - [] operator" << endl;
        return objects[ index ];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the element at the given vector.
     *
     * @param[in] index - the index to retrieve from
     *
     * @returns the element at the specified index
     *
     *************************************************************************/
    const Object & operator[] ( int index ) const
    {
        // get the object
        cout << "Method 10 - const [] operator" << endl;
        return objects[ index ];
    }




    typedef Object* iterator;    
    typedef const Object* const_iterator;


    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the first element of the vector.
     *
     * @returns the first element in the vector
     *
     *************************************************************************/
    iterator begin ( )
    {
        // get the first element
        cout << "Method 18 - iterator begin" << endl;
        return &objects[ 0 ];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the first element of the vector.
     *
     * @returns the first element in the vector
     *
     *************************************************************************/
    const_iterator begin ( ) const
    {
        // get the first element
        cout << "Method 19 - const_iterator begin" << endl;
        return &objects[ 0 ];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the final element of the vector.
     *
     * @returns the last element in the vector
     *
     *************************************************************************/
    iterator end ( )
    {
        // get the last element
        cout << "Method 20 - iterator end" << endl;
        return &objects[ size ( ) - 1];
    }



    /** ***********************************************************************
     * @author Zoe Millage
     *
     * @brief Gets the final element of the vector.
     *
     * @returns the last element in the vector
     *
     *************************************************************************/
    const_iterator end ( ) const
    {
        // get the last element
        cout << "Method 21 - const_iterator end" << endl;
        return &objects[ size ( ) - 1];
    }



    static const int SPARE_CAPACITY = 16;  /*!< Used for initial capacity */



  private:
    int theSize; /*!< The current number of used elements */
    int theCapacity; /*!< How much more data can be added without resizing */
    Object* objects; /*!< The actual data */

};


// function prototype
template <typename Object>
void constPrintVector ( const Vector<Object> v1 );
template <typename Object>
void printBeginEnd ( const Vector<Object> & v1 );
template <typename Object>
void printVector ( Vector<Object> & v1 );


#endif