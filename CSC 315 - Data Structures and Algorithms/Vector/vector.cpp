/** ***************************************************************************
 * @file  
 * 
 * @brief Contains main and vector printing functions.
 *****************************************************************************/
/** ***************************************************************************
* @file  
*
* @mainpage Vector
* 
* @section course_section Course Information
*
* @author Zoe Millage
*
* @date Fall 2021
*
* @par Instructor
*     Rohan Loveland
*
* @par Course
*     CSC 315 - Data Structures & Algorithms
*
* @section program_section Program Information
*
* @details
* This program contains a vector class, which contains template objects.
* Essentially a smaller implimentation of the vector class.
* Each method of the class has a print function to demonstrate that
* each method is used and when it is called.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     vector.exe
*
******************************************************************************/

#include "vector.h"


int main ( )
{
    int number = 4444;
    
    // calls method 1 - default constructor
    Vector<int>  vector1;
    Vector<int>* vector2;
    Vector<int>  vector3;
    Vector<char> vector4;
    cout << endl;

    // calls method 14 - push_back ( lvalue )
    vector1.push_back ( number );
    cout << endl;

    // calls method 2 - copy constructor
    vector2 = new (nothrow) Vector<int> ( vector1 );
    cout << endl;

    // calls method 15 - push_back ( rvalue )
    vector1.push_back ( 2 );
    vector1.push_back ( 4 );
    vector4.push_back ( 'A' );
    cout << endl;

    /* calls method 3 - copy assignment
    *  also calls methods 2(copy constructor), 5 (move constructor),
    *  6 (move assignment), and 4 (destructor) to complete the intended task */
    vector3 = vector1;
    cout << endl;

    // calls method 16 - pop_back
    vector3.pop_back ( );
    cout << endl;

    // calls Methods 18 (interator begin) and 20 (iterator end)
    cout << *vector3.begin ( ) << ", " << *vector3.end ( ) << endl << endl;

    // calls Methods 19 (const_iterator begin) and 21 (const_iterator end)
    printBeginEnd ( vector3 );

    // calls method 17 - back
    cout << vector4.back ( ) << endl << endl;

    // calls methods 7 ( resize ) and 8 (reserve)
    vector1.resize ( 20 );

    // calls method 13 - capacity
    cout << vector1.capacity ( ) << endl << endl;

    // calls methods 9 ( [] operator ), 11 ( empty ), and 12 ( size )
    cout << "Print vectors" << endl << "Vector 1:" << endl;
    printVector ( vector1 );

    cout << "Vector 2:" << endl;
    printVector ( *vector2 );

    cout << "Vector 3:" << endl;
    printVector ( vector3 );

    cout << "Vector 4:" << endl;
    printVector ( vector4 );
    
    // calls methods 10 ( const [] operator ), 11 ( empty ), and 12 ( size )
    cout << "Vector 3 (const):" << endl;
    constPrintVector ( vector3 );

    // calls method 4 - destructor
    delete vector2;

    return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @brief Prints a vector.
 *
 * @param[in] v1 - the vector to print
 *
 *****************************************************************************/
template <typename Object>
void constPrintVector ( const Vector<Object> v1 )
{
    int i;

    if ( v1.empty ( ) )
        return;

    for ( i = 0; i < v1.size ( ); i++ )
        cout << v1[ i ] << endl;

    cout << endl;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @brief Prints the first and last elements of a vector.
 *
 * @param[in] v1 - the vector to partially print
 *
 *****************************************************************************/
template <typename Object>
void printBeginEnd ( const Vector<Object> & v1 )
{
    // print begin() and end()
    cout << *v1.begin ( ) << ", " << *v1.end ( ) << endl << endl;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @brief Prints a vector.
 *
 * @param[in] v1 - the vector to print
 *
 *****************************************************************************/
template <typename Object>
void printVector ( Vector<Object> & v1 )
{
    int i;

    // do nothing if empty
    if ( v1.empty ( ) )
        return;

    // print the contents
    for ( i = 0; i < v1.size ( ); i++ )
        cout << v1[ i ] << endl;

    cout << endl;
}