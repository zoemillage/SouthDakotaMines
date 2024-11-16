/** ***************************************************************************
* @file
*
* @mainpage Program 3 - Knights Tour
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
* This program solves a Knight's tour: a sequence of moves where the knight
* from a game of chess moves to each space on a board exactly once.
* The program can be  run with either one or two arguments. If there is one
* argument, menu functions will be called that will allow the user to select
* the parameters for the permutation ( the size of the board and the starting
* coordinates). Once the user has confirmed the parameters, the permutation
* will be solved and a solution (if it exists) will be output to the screen.
* If there are two arguments, the second will be an input file containing
* parameters for the permutation(s). The program will read and solve each
* permutation and append the solution(s) to a separate output file named
* "Solutions.tours".
* The permutation function will only find the first solution for a given
* set of parameters. If no solution is found, the program will print "No
* solutions for this case" instead of the game board then exit. 
* The board for the tour will always have the same height and width, and the 
* board is represented in the program by a dynamically allocated 2D array with 
* a two element buffer on each side. The array must have a non-buffer value of
* 4 or more; a 3 x 3 or smaller is impossible to solve.
* The knight's possible movements in this program are in the following order:
* 1 right 2 up, 2 right 1 up, 2 right 1 down, 1 right 2 down, 1 left 2 down,
* 2 left 1 down, 2 left 1 up, 1 left 2 up.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     prog3.exe
*     prog3.exe tourfile
*
******************************************************************************/


#include <fstream>
#include <iostream>
#include <iomanip>

using namespace std;


/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
template <class TY>
void arrAllocate2D ( TY**& arrptr, int size );
template <class TY>
void arrFree2D ( TY** arrptr, int size );
void arrInitialize2D ( bool**& arrptr, int size, int x, int y );
void arrInitialize2D ( int**& arrptr, int size, int x, int y );
void close2Files ( ifstream& fin, ofstream& fout, int exitCode );
int  getMaxPosition ( int size );
void menuMain ( int& size, int& x, int& y );
void menuOptions ( int choice, int& size, int& x, int& y );
int  menuPrint ( int& size, int& x, int& y );
void open2Files ( ifstream& fin, ofstream& fout, char**& argv );
void permute ( int** arr2D, bool** used, int maxPos, int& res, int pos, 
     int size, int x, int xMoves[], int y, int yMoves[] );
void printResults ( int** arrptr, ostream& out, int result, int size,
     int tourNum, int x, int y );
bool readTour ( ifstream& fin, int& size, int& x, int& y );



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the starting point of the program. The command line arguments are
 * checked before anything else is called, and will end the program with an 
 * error message if the argument count is incorrect. If argc is 1, the menu
 * functions will be called and the permutation's outcome will be output to 
 * the screen once solved. If argc is 2, the parameters for the tour(s)
 * read from an input file and the solution(s) are appended to Solutions.tours.
 * x and y positions are 2 higher than the actual position to account
 * for a buffer used to check if the knight stepped off of the board 
 * with a move. Size is 4 higher as the board needs 2 buffer spaces on each
 * side of the board. All dynamic memory and opened files are freed/closed 
 * before the program ends.
 *
 * @param[in] argc - The number of arguments for the prog1; 
 * should equal 1 or 2.
 * @param[in] argv - Array that holds the names of the input/output file(s)
 *
 * @returns 0 - The program ran successfully.
 * @returns 1 - Incorrect command line arguments.
 * @returns 2 - The input file failed to open.
 * @returns 3 - The output file failed to open. 
 * @returns 4 - Dynamic array memory allocation error
 *
 *****************************************************************************/
int main ( int argc, char** argv )
{
    bool** used = nullptr;      // stores which locations have been visited

    ifstream fin;

    int** arr2D = nullptr;      // a 2D array
    int i = 1;
    int maxPos = 0;             // number of positions to fill
    int pos = 1;
    int size = 12;              // size of the board
    int result = -1;
    int tourNum = 1;
    int x = 9;                  // 2+ the x position on the board
    int xMoves[8] = { 1, 2, 2, 1, -1, -2, -2, -1 };
    int y = 9;                  // 2+ the y position on the board
    int yMoves[8] = { 2, 1, -1, -2, -2, -1, 1, 2 };

    ofstream fout;

    // check command line arguments and error check
    if ( argc != 1 && argc != 2 )
    {
        cout << "Usage: prog3.exe" << endl 
             << "       prog3.exe tourfile" << endl;

        return 1;
    }

    // print the menu if argc = 1
    if ( argc == 1 )
    {
        menuMain ( size, x, y );

        // allocte the solution and used arrays
        arrAllocate2D ( arr2D, size );
        arrInitialize2D ( arr2D, size, x, y );
        arrAllocate2D ( used, size );
        arrInitialize2D ( used, size, x, y );
        maxPos = getMaxPosition ( size );

        // call the permutation
        permute ( arr2D, used, maxPos, result, pos, size, x, xMoves, y, 
                  yMoves );

        // print the results and free the dynamic arrays
        printResults ( arr2D, cout, result, size, 1, x, y );

        arrFree2D ( arr2D, size );

        return 0;
    }

    // open files and error check if argc = 2
    open2Files ( fin, fout, argv );

    // read the file to get parameters and allocate arrays
    while ( readTour( fin, size, x, y ) ) {
        arrAllocate2D ( arr2D, size );
        arrInitialize2D ( arr2D, size, x, y );
        arrAllocate2D ( used, size );
        arrInitialize2D ( used, size, x, y );
        maxPos = getMaxPosition ( size );

        // call the permutation
        permute ( arr2D, used, maxPos, result, pos, size, x, xMoves, y, yMoves );

        // print results and free dynamic memory
        printResults ( arr2D, fout, result, size, i, x, y );

        arrFree2D ( arr2D, size );
        arrFree2D ( used, size);
        result = -1;

        i++;
    }

    // close files
    close2Files ( fin, fout, 0 );

    return 0;
}






/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function dynamically allocates a 2d array. If an allocation fails, 
 * this function will call ArrFree2D.
 *
 * @param[in] arrptr - The pointer to the dynamic array
 * @param[in] size - The number of rows and columns in the array
 *
 *****************************************************************************/
template <class TY>
void arrAllocate2D ( TY**& arrptr, int size )
{
    int i = 0;

    // allocate the initial array, error check
    arrptr = new ( nothrow ) TY* [size];
    if ( arrptr == nullptr )
    {
        cout << "Memory Allocation Error" << endl;
        exit ( 4 );
    }

    // allocate the subsequent arrays, error checking each allocation
    for ( i = 0; i < size; i++ )
    {
        arrptr[i] = new ( nothrow ) TY [size];
        if ( arrptr[i] == nullptr )
        {
            // free all arrays if an allocation fails
            arrFree2D ( arrptr, i );

            cout << "Memory Allocation Error" << endl;
            exit ( 4 );
        }
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function frees a dynamically allocated 2d array.
 *
 * @param[in] arrptr - The pointer to the dynamic array
 * @param[in] size - The number of rows and columns in the array
 *
 *****************************************************************************/
template <class TY>
void arrFree2D ( TY** arrptr, int size )
{
    int i = 0;

    // an empty array has nothing to delete
    if ( arrptr == nullptr )
        return;

    // walk through the arrays, deleting each found 
    for ( i = 0; i < size; i++ )
        delete[] arrptr[i];
    
    // delete the initial array
    delete[] arrptr;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function initializes a dynamic 2D bool array. There is a 2 element
 * buffer on each side of the board the array represents, so all buffer spaces
 * are set to 1 and all valid board spaces are set to 0.
 *
 * @param[in] arrptr - The pointer to the dynamic array
 * @param[in] size - The number of rows and columns in the array
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 *
 *****************************************************************************/
void arrInitialize2D ( bool**& arrptr, int size, int x, int y )
{
    int i;
    int j;

    // nonexistant arrays have  nothing to initialize
    if ( arrptr == nullptr )
        return;

    // an array that is <= 4 in size is just buffer spaces; don't initialize
    if ( size < 5 )
        return;

    // fill the array with -1
    for ( i = 0; i < size; i++ )
    {
        for ( j = 0; j < size; j++ )
            arrptr[i][j] = 1;
    }

    // fill the valid spaces with 0
    for ( i = 2; i < size - 2; i++ )
    {
        for ( j = 2; j < size - 2; j++ )
            arrptr[i][j] = 0;
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function initializes a dynamic 2D integer array. There is a 2 element
 * buffer on each side of the board the array represents, so all buffer spaces
 * are set to -1 and all valid board spaces are set to 0.
 *
 * @param[in] arrptr - The pointer to the dynamic array
 * @param[in] size - The number of rows and columns in the array
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 *
 *****************************************************************************/
void arrInitialize2D ( int**& arrptr, int size, int x, int y )
{
    int i;
    int j;

    // nonexistant arrays have  nothing to initialize
    if ( arrptr == nullptr )
        return;

    // an array that is <= 4 in size is just buffer spaces; don't initialize
    if ( size < 5 )
        return;

    // fill the array with -1
    for ( i = 0; i < size; i++ )
    {
        for ( j = 0; j < size; j++ )
            arrptr[i][j] = -1;
    }

    // fill the valid spaces with 0
    for ( i = 2; i < size - 2; i++ )
    {
        for ( j = 2; j < size - 2; j++ )
            arrptr[i][j] = 0;
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function assumes an isopen() check has failed or files are being
 * closed to exit the program, so this function closes all files.
 *
 * @param[in] fin - an input file stream
 * @param[in] fout - an output file stream
 * @param[in] exitCode - the code to output when exiting the program
 *
 *****************************************************************************/
void close2Files ( ifstream& fin, ofstream& fout, int exitCode )
{
    // close the files; exit with the given exit code
    fin.close ( );
    fout.close ( );
    exit ( exitCode );
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function gets the number of spaces that need to be filled to solve a
 * permutation. The number of spaces if equal to the number of rows 
 * ( or columns, they are the same) of the board squared.
 *
 * @param[in] size - the size of the board
 *
 * @returns  The number of spaces to fill
 *
 *****************************************************************************/
int getMaxPosition ( int size )
{
    int maxPos = size - 4;

    maxPos = maxPos * maxPos;

    return maxPos;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function acts as the menu for the program (only appears when no input
 * file is given). This function prints the main menu and prompts for 
 * the user's choice. If an invalid choice is given ( not 1, 2, or 3 ), the
 * user will be prompted for a valid option until one is given. If the user
 * choses 3, the menu loop is broken, allowing for the board to be solved.
 * This function is not standalone, so it calls
 * the menuPrint, menuOptions, and permutation functions. 
 *
 * @param[in, out] size - the size of the board
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 *
 *****************************************************************************/
void menuMain ( int& size, int& x, int& y )
{
    int choice;    // the option the user chooses

    // print the main menu options and prompt for user's choice
    choice =  menuPrint ( size, x, y );

    // allow menu traversal while not trying to solve the board
    while ( choice != 3 )
    {
        // anything other than 1, 2, and 3 are invalid inputs
        while ( choice != 1 && choice != 2 && choice != 3 )
        {
            // prompt for a valid value until one is given
            cout << endl << "Invalid option." << endl << endl;
            choice = menuPrint ( size, x, y );
        }

        // allow menu traversal for multiple changes
        menuOptions ( choice, size, x, y );
        cout << endl;
        choice = menuPrint ( size, x, y );
    }

    // solve the board

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description: 
 * Once the user enters a valid option other than 3, this
 * function will process all other possible actions. If the player chose 1, 
 * the user will be prompted to enter a new size for the board until they
 * give a valid size. If they chose 2, they will be prompted for a starting
 * position until a valid position is given. Once a valid value is given,
 * the board information will be updated and the user will be "returned" 
 * to the main menu.
 * A board size is only valid if it is above 3 or the values of the starting
 * position, whichever is larger. A starting position is valid if both values
 * are greater than or equal to 0 and less than or equal to the board size.
 *
 *
 * @param[in] choice - the menu option the user chose
 * @param[in, out] size - the size of the board
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 *
 *****************************************************************************/
void menuOptions ( int choice, int& size, int& x, int& y )
{
    int newSize;
    int newX;
    int newY;

    // change the size if choice = 1
    if ( choice == 1 )
    {
        cout << endl << "Enter the size of the NxN board (>3): ";
        cin >> newSize;

        // error check for newsize < x,y, or < 3
        while ( newSize < x - 1 || newSize < y - 1 || newSize <= 3 )
        {
            // check if size < starting position
            if ( newSize < x - 1 || newSize < y - 1 )
            {
                cout << endl 
                     << "Size cannot be smaller than the starting position."
                     << endl;
            }

            else
                cout << endl << "Size must be greater than 3." << endl;

            // prompt for size until a valid value is given
            cout << endl << "Enter the size of the NxN board (>3): ";
            cin >> newSize;
        }

        // set size to be useable for the array with a buffer
        size = newSize + 4;
        return;
    }

    // change the starting position if choice = 2
    else if ( choice == 2 )
    {
        cout << endl << "Enter starting coordinates [ row, col ]: ";
        cin >> newX >> newY;

        // error check for either coordinate being too small or too large
        while ( newX + 5 > size || newY + 5 > size || 
                newX < 0 || newY < 0 )
        {
            // check if either coordinate is < 0
            if ( newX < 0 || newY < 0 )
            {
                cout << endl << "Minimum starting position is [ 0, 0 ]." 
                     << endl;
            }

            // the starting coordinates are too small
            else
            {
                cout << endl << "Starting coodinates cannot be " 
                     << "larger than the board size." << endl;
            }

            // prompt for a starting position until a valid value is given
            cout << endl << "Enter starting coordinates [ row, col ]: ";
            cin >> newX >> newY;
        }

        // make x and y usable for the array with a buffer
        x = newX + 2;
        y = newY + 2;
        return;
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function prints the options for the main menu along with the data
 * for the board. This function also prompts for the user's option choice
 * and returns it. The 3 options are 1: change the size of the board, 2: 
 * change the starting position of the board, and 3: solve the tour. 
 *
 * @param[in, out] size - the size of the board
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 * 
 * @returns The menu option the user chose
 *
 *****************************************************************************/
int menuPrint ( int& size, int& x, int& y )
{   
    int choice;
    int tempSize;
    int tempX;
    int tempY;

    // set temporary values to display as their functional board values
    tempSize = size - 4;
    tempX = x - 2;
    tempY = y - 2;

    // print the menu options
    cout << "  1) Change board size from " << tempSize << "x" << tempSize 
         << endl;
    cout << "  2) Change starting location from [" << tempX << "," << tempY 
         << "]" << endl;
    cout << "  3) Exit and solve tour" << endl;

    // prompt for the user's choice
    cout << "Enter choice: ";
    cin >> choice;

    return choice;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function opens an input file determined by argv and an output file
 * named "Solutions.tours." The function also checks for successful file
 * opening and will display an error message, close the opened file(s),
 * and exit the program if the check fails.
 *
 * @param[in] fin - an input file stream
 * @param[in] fout - an output file stream
 * @param[in] argv - dictates the input file to open
 *
 *****************************************************************************/
void open2Files ( ifstream& fin, ofstream& fout, char**& argv )
{
    //open each file, check that it opened, call closeFiles if it didn't
    fin.open ( argv[1] );

    if ( !fin.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open input file: " << argv[1] << endl;

        // close the files
        close2Files ( fin, fout, 2 );
    }

    fout.open ( "Solutions.tours", ios::app );

    if ( !fout.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open output file: Solutions.tours " << endl;

        // close the files
        close2Files ( fin, fout, 3 );
    }

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function solves a knight's tour; it is a brute force backtracking 
 * function that recursively calls itself until the final position is hit or 
 * all possibilities have been exhaused. 
 *
 * @param[in] arr2D - A 2D array holding the solution to the tour
 * @param[in] used - A 2D array holding the currently used positions
 * @param[in] maxPos - the maximum position (filled spaces) on the board
 * @param[in, out] result - the result of the permutation; 0 indicates failure
 * @param[in] pos - the current posiiton in the permutation
 * @param[in] size - the size of the board
 * @param[in] x - the current x position
 * @param[in] xMoves - the possible next movements for x
 * @param[in] y - the current y position
 * @param[in] yMoves - the possible next movements for y
 *
 *****************************************************************************/
void permute ( int** arr2D, bool** used, int maxPos, int& result, int pos, 
    int size, int x, int xMoves[], int y, int yMoves[] )
{
    int i = 0;
    int tempX = 0;
    int tempY = 0;

    // base case: all slots filled
    if ( pos > maxPos || result == 1 )
    {
        result = 1;
        return;
    }

    // permute
    for ( i = 0; i < 8; i++ )
    {
        if ( used[x][y] == false )
        {
            arr2D[x][y] = pos;
            used[x][y] = true;

            tempX = x + xMoves[i];
            tempY = y + yMoves[i];

            permute ( arr2D, used, maxPos, result, pos + 1, size, tempX,
                      xMoves, tempY, yMoves );

            //arr2D[x][y] = 0;
            used[x][y] = 0;
        }
        
    }

    return;
}


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function will print the results of a permutation. This will print
 * the parameters for the permutation, then print the results of the 
 * permutation. If an answer exists for the given board, it will be printed
 * to an output stream. If the permutation failed, "No solutions for 
 * this case" will be printed instead. 
 *
 * @param[in] arrptr - a 2D array pointer that holds the permutation results
 * @param[in, out] out - the stream to output to
 * @param[in] tourNum - the number for the tour being printed this session
 * @param[in] size - the size of the game board, plus a buffer
 * @param[in] x - the initial x value, accounting for the size buffer
 * @param[in] y - the initial y value, accounting for the size buffer
 * @param[in] result - the result of the permutation algorithm
 *
 *****************************************************************************/
void printResults ( int** arrptr, ostream& out, int result, int size, 
     int tourNum, int x, int y )
{
    int endlCheck = -1;
    int i;
    int j;
    int tempSize = size - 4;
    int tempX = x - 2;
    int tempY = y - 2;

    // output the permutation parameters
    out << endl << "Tour # " << tourNum << endl << "\t"
        << tempSize << "x" << tempSize << " starting at (" << tempX << ", "
        << tempY << ")" << endl << endl;

    // don't print the array if the permutation failed
    if ( result != 1 )
    {
        out << "\tNo solutions for this case." << endl;
        return;
    }

    // print the array with the solution
    for ( i = 2; i < size - 2; i++ )
    {
        out << left << "\t";

        for ( j = 2; j < size - 2; j++ )
        {
            out << right << setw ( 2 ) << setfill ( ' ' );
            out << arrptr[i][j] << " ";
        }

        out << endl;
    }


    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function reads in tour parameters ( size and starting coordinates) and
 * converts them to be usable by an array that has two elements of buffer on
 * each size.
 *
 * @param[in] fin - a file input stream
 * @param[in, out] size - the size of the board
 * @param[in, out] x - the x value of the starting position
 * @param[in, out] y - the y value of the starting position
 *
 * @returns True if reading was successful, false otherwise
 * 
 *****************************************************************************/
bool readTour ( ifstream& fin, int& size, int& x, int& y )
{
    // read the size and starting coordinates
    if ( fin >> size >> x >> y )
    {
        // convert the parameters to be used in the buffer array
        size = size + 4;
        x = x + 2;
        y = y + 2;

        return true;
    }

    return false;
}