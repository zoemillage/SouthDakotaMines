/** ***************************************************************************
 * @file  
 * 
 * @brief Contains main along with functions related to 
 * opening, closing, and printing to files.
 *****************************************************************************/
/** ***************************************************************************
* @mainpage Program 2 - List Mania
*
* @section course_section Course Information
*
* @authors Anna Combalia Pardo, Zoe Millage
*
* @date March and April 2021
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
* This program uses three lists to store data: one ordered circularly linked 
* list ( list1 ) and two unordered STL lists ( list2 and list3 ). 
* Once the program has confirmed correct command line arguments
* and successful file opening, the program will begin reading integers from
* the input file into the lists. If the data does not exist in list1, it will 
* be added into it. If the integer is in list1 but is not in list2, 
* it will be removed from list1 and inserted into the front of list2. If the
* data is in both list1 and in list2, it will be removed from both and placed
* at the end of list3. If the data is in all three lists, it will be removed
* from all three and replaced into the front of list3. Once all data has been
* read, it will be formatted and output to a file. Each list will include
* the name of the list followed by its size on one line. Then, each data
* point will be printed with 5 per line, each data point using 10 characters
* at maximum.
* After outputting to the file, the program will close all files, free all
* dynamic memory, and close.
*
* @section compile_section Compiling and Usage
*
* @par Compiling Instructions:
*      None
*
* @par Usage
*     prog2.exe numbers.txt results.txt
*
******************************************************************************/

#include "AnnaOrderedList.cpp"
#include "ZoeOrderedList.cpp"
#include "orderedList.h"
#include <algorithm>
#include <list>


/******************************************************************************
 *                         Function Prototypes
 *****************************************************************************/
void closeFiles ( ifstream& fin, ofstream& fout, int exitCode );
int  countList ( list<int> list1, list<int>::iterator it );
void openFiles ( ifstream& fin, ofstream& fout, char**& argv );
void printList ( list<int> list1, list<int>::iterator it, ostream& out );



/** ***************************************************************************
 * @authors Anna Combalia Pardo, Zoe Millage
 *
 * @par Description:
 * This is the starting point of the program. This function will check for
 * correct command line arguments. Then, it will try to open the input and
 * output files. If both succeed, the program will read data from the
 * input file and store them into lists according to the rules stated on the
 * main page. Once all data has been read, the lists will output to a file.
 *
 * @param[in] argc - The number of arguments for the prog2; should equal 3
 * @param[in] argv - Array that holds the names of the input and output files
 *
 * @returns 0 - The program ran successfully.
 * @returns 1 - Incorrect command line arguments.
 * @returns 2 - The input file failed to open.
 * @returns 3 - The output file failed to open. 
 *
 *****************************************************************************/
int main(int argc, char** argv)
{
    ifstream fin;
    ofstream fout;
    orderedList list1;
    list<int> list2;
    list<int> list3;
    list<int>::iterator it2;
    list<int>::iterator it3;
    int num;
    int count2 = 0;
    int count3 = 0;

    // Check command line arugments
    if (argc != 3)
    {
        cout << "prog2.exe numbers.txt results.txt" << endl;
        return 1;
    }

    // Open files and error check
    openFiles ( fin, fout, argv );

    // Read in data and insert into the appropriate list
    while (fin >> num)
    {
        if (list1.find(num) == false)//not in list1
            list1.insert(num);

        else//in list1
        {
            it2 = find(list2.begin(), list2.end(), num);
            it3 = find(list3.begin(), list3.end(), num);

            list1.remove(num);

            if (it2 != list2.end())//in list2
            {
                list2.erase(it2);

                if (it3 != list3.end())//in list3
                {
                    list3.erase(it3);
                    list3.push_front(num);
                }

                else//not in list3
                {
                    list3.push_back(num);
                }
            }

            else//not in list2
            {
                list2.push_front(num);
            }
        }
    }

    // output list1
    fout << "List 1: size = " << list1.size() << endl;
    list1.print(fout);
    fout << endl << endl << endl;

    //output list2
    count2 = countList ( list2, it2 );
    fout << "List 2: size = " << count2 << endl;
    printList ( list2, it2, fout );

    //output list3
    count3 = countList ( list3, it3 );
    fout << "List 3: size = " << count3 << endl;;
    printList ( list3, it3, fout );

    // close the files
    closeFiles ( fin, fout, 0 );
    return 0;
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
void closeFiles ( ifstream& fin, ofstream& fout, int exitCode )
{
    fin.close ( );
    fout.close ( );
    exit ( exitCode );
}



/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description:
 * This function counts the number of elements in a list
 *
 * @param[in] list1 - the list to output
 * @param[in] it - an iterator for traversing through the list
 * 
 * @returns The number of elements
 *
 *****************************************************************************/
int countList ( list<int> list1, list<int>::iterator it )
{
    int count = 0;

    for ( it = list1.begin ( ); it != list1.end ( ); it++ )
    {
        count++;
    }

    return count;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This function opens files determined by argv. The
 * function also checks for successful file opening and will display an error
 * message, close the opened file(s), and exit the program if the check fails.
 *
 * @param[in] fin - an input file stream
 * @param[in] fout - an output file stream
 * @param[in] argv - dictates files to be opened/closed
 *
 *****************************************************************************/
void openFiles ( ifstream& fin, ofstream& fout, char**& argv )
{
    //open each file, check that it opened, call closeFiles if it didn't
    fin.open ( argv[1] );

    if ( !fin.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open input file: " << argv[1] << endl;

        closeFiles ( fin, fout, 2 );
    }


    fout.open ( argv[2] );

    if ( !fout.is_open ( ) )
    {
        //print an error message for which file didn't open
        cout << "Unable to open output file: " << argv[2] << endl;

        closeFiles ( fin, fout, 3 );
    }

    return;
}



/** ***************************************************************************
 * @author Anna Combalia Pardo
 *
 * @par Description:
 * This function prints a list such that 5 data points are printed per line
 * and each data point can take up 10 chracters. It then prints three blank
 * lines. 
 *
 * @param[in] list1 - the list to output
 * @param[in] it - an iterator for traversing through the list
 * @param[in, out] out - the stream to output to
 *
 *****************************************************************************/
void printList ( list<int> list1, list<int>::iterator it, ostream& out )
{
    int endlCheck = 1;      //determines whether or not an endl is output

    for ( it = list1.begin ( ); it != list1.end ( ); it++ )
    {
        out << setw ( 10 ) << setfill ( ' ' );
        out << *it << " ";
        if ( endlCheck != 0 && endlCheck % 5 == 0 )
            out << endl;
        endlCheck++;
    }

    // ensure uniform formatting between lists
    if ( ( endlCheck - 1 ) % 5 != 0 )
        out << endl;

    out << endl << endl << endl;
}