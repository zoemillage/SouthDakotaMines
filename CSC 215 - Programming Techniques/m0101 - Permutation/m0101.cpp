/** ***************************************************************************
* @file  
*
* @mainpage m0101 - Permutation
* 
* @section course_section Course Information
*
* @author Zoe Millage
*
* @date Spring 2021
*
* @par Instructor
*     Roger Schrader
*
* @par Course
*     CSC 215 - Programming Techniques
*
* @section program_section Program Information
*
* @details
* This program takes in a file containing the number of items, the number to 
* permute, and string. It then prints all the applicable permutations 
* (order matters).
* An input file should be in the format
* number of items, number of items to permute, items
* separated by whitespace. 
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     m0101.exe itemfile.txt
*
******************************************************************************/

#include <fstream>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

// function prototype
void permute ( int n, int k, int pos, bool used[], string items[], 
    int solution[] );



int main ( int argc, char* argv[] )
{
    bool used[16] = { false };

    char str[256];

    ifstream fin;

    int i = 0;
    int k;                         // number of elements per permutation group
    int n;                         // total num  ber of elements
    int pos = 0;                   // position
    int solution[16] = { 0 };

    string items[16];

    // error check command line arguments
    if ( argc != 2 )
    {
        cout << "Usage: m0101.exe itemfile.txt" << endl;

        return 0;
    }

    // open file and error check
    fin.open ( argv[1] );

    if ( !fin.is_open ( ) )
    {
        cout << "Unable to open file: " << argv[1] << endl;

        fin.close ( );

        return 0;
    }


    // get n and k
    fin >> n;
    if ( n > 16 )
        n = 16;


    fin >> k;
    if ( k > n )
        k = n;

    // remove the \n from the fin buffer
    fin.ignore ( );

    // get the strings
    while ( i < n && fin.getline ( str, 255, '\n' ) )
    {
        items[i].assign ( str );
        i++;
    }

    // close the file
    fin.close ( );


    vector <string> ::iterator it;
    // call the permutation function
    permute ( n, k, pos, used, items, solution );

    return 0;
}
 


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Recursively runs through permutations on a set of strings.
 *
 * @param[in] n - the total number of elements
 * @param[in] k - the max position/number of elements per permutation
 * @param[in] pos - the current position
 * @param[in] used - holds which positions are used in the current permutation
 * @param[in] items - holds the items as strings
 * @param[in] solution - holds the indeces of each item in the permutation
 *
 *****************************************************************************/
void permute ( int n, int k, int pos, bool used[], string items[],
    int solution[] )
{
    int i;
    string temp;

    // base case
    if ( pos == k )
    {

        // output the permutations
        for ( i = 0; i < k; i++ )
        {
             cout << items[solution[i]] << " ";
        }

        cout << endl;
        return;

    }

    // fill an array with all permutation possiblities
    for ( i = 0; i < n; i++ )
    {
        // fill a position if it is unsued
        if ( used[i] == false )
        {
            solution[pos] = i;
            used[i] = true;

            permute ( n, k, pos+1, used, items, solution );

            used[i] = false;
        }
            
    }

    return;
}