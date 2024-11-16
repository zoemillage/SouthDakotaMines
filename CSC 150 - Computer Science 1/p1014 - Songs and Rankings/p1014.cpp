/** ***************************************************************************
 * @file
 *
 * @brief Sorts and compiles song and ranking data from 2 files
 *
 * @mainpage p1014 - Songs and Rankings
 *
 * @section course_section Course Information
 *
 * @authors Zoe Millage
 *
 * @date Fall 2020
 *
 * @par Course:
 *         CSC 150 - Computer Science 1
 *
 * @section program_section Program Information
 *
 * @details This program takes 3 files, 2 input and 1 output, tries to open 
 * them, and, if successful, reads file 1 as a list of ranks, file 2 as a list 
 * of song names, associates the entries together (entry 1 of ranks is tied to 
 * entry 1 of songs), sorts the data by rank, then outputs all the sorted 
 * entries into the output file in the format
 * rank name.
 *
 *****************************************************************************/

//This define is needed to avoid Visual Studio cstring function warnings
#define _CRT_SECURE_NO_WARNINGS

#include <fstream>
#include <iostream>
#include <string.h>

using namespace std;

int read_songs_and_ranks(ifstream & in_songs, ifstream & in_ranks,
						  int ranks[], char songs[][81], int max_elements);
void sort_songs_by_rank(int ranks[], char songs[][81], int elements_filled);
void swap_integers(int& n1, int& n2);
void swap_cstrings(char s1[], char s2[]);
void write_list(ofstream & out, int ranks[], char songs[][81], 
				int elements_filled);




/** ***************************************************************************
* @author K. Corwin
*
* @par Description:
*   Declares arrays
	Checks value of argc; if incorrect, terminates program
	Opens two input files and one output file
	Checks file success;  if incorrect, terminates program
	Reads from input files, obtaining number of data items read
	Sorts the song titles and ranking data based on rankings,
		in ascending order
	Writes the rankings and song titles, in ascending order,
		to the output file
	Closes all files
*
* @param[in] argc - the number of arguments from the command line.
* @param[in] argv - a 2d array of char containing the command line arguments.
*
* @returns 0 in all cases - a message to stdout describes any error states.
*
*****************************************************************************/
int main(int argc, char* argv[])
{
	//Declare two input file objects
	ifstream in_songs;
	ifstream in_ranks;

	//Declare one output file object
	ofstream out;

	//Holds the song titles
	char songs[100][81];

	//Holds the song rankings
	int ranks[100];

	//The actual number of songs read from the files.
	int num_songs;

	//Always error check argc before doing anything else!!
	if (argc != 4)
	{
		cout << "Usage: p1014.exe <song filename> "
			<< "<rank filename> <final list filename>" << endl;
		return 0;
	}

	//now try to open all 3 files
	in_songs.open(argv[1]);
	in_ranks.open(argv[2]);
	out.open(argv[3]);

	//if any file fails to open, stop the program
	if (!in_songs || !in_ranks || !out)
	{
		cout << "File error; program exiting." << endl;
		return 0;
	}

	//Read from both files, storing to arrays
	// and getting the actual value of num_songs
	num_songs = read_songs_and_ranks(in_songs, in_ranks, ranks, songs, 100);

	//Sort song and ranking data by rank
	sort_songs_by_rank(ranks, songs, num_songs);

	//Write the output to a new file
	write_list(out, ranks, songs, num_songs);

	//close all files
	in_songs.close();
	in_ranks.close();
	out.close();

	return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Gets songs and ranks from two separate files, reading them into arrays.
 * Also gets the number of songs/ranks read in, based on the songs file.
 *
 * @param[in] in_songs - the file stream with song names
 * @param[in] in_ranks - the file stream with rankings
 * @param[in, out] ranks - holds the read in ranks
 * @param[in, out] songs - holds the read in songs
 * @param[in]  max_elements - the maximum allowed number of songs/ranks
 *
 * @returns the number of song/rank pairs
 *
 *****************************************************************************/
int read_songs_and_ranks(ifstream & in_songs, ifstream & in_ranks,
						  int ranks[], char songs[][81], int max_elements)
{
	int i = 0;
	int elements_filled = 0;

	// get the ranks and the number of elements
	for (i = 0; i < max_elements; i++)
	{
		in_ranks >> ranks[i];

		if (in_ranks)
		++elements_filled;
	}

	// get the songs
	for (i = 0; i < max_elements; i++)
	{
		in_songs.getline(songs[i], 81);
	}

	return elements_filled;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Sorts the given arrays of ranks and songs based on rank, increasing.
 *
 * @param[in, out] ranks - holds the rankings
 * @param[in, out] songs - holds the song names
 * @param[in] elements_filled - the number of rank/song pairs
 *
 *****************************************************************************/
void sort_songs_by_rank(int ranks[], char songs[][81], int elements_filled)
{
	bool swapmade = true;

	int i, j;

	// go through the songs
	for (i = 0; i < elements_filled - 1 && swapmade; i++)
	{
		// sort the songs by ascending rank
		swapmade = false;
		for (j = 0; j < elements_filled - i - 1; j++)
		{
			if (ranks[j] > ranks[j + 1])
			{
				swapmade = true;
				swap_integers(ranks[j], ranks[j + 1]);
				swap_cstrings(songs[j], songs[j + 1]);
			}
		}
		if (!swapmade)
			return;
	}

	return;
}



/** ***************************************************************************
* @author K. Corwin
*
* @par Description:
* This function takes in two integers by reference
	and swaps them.  When the function is done,
	n1 contains the original value of n2, and
	n2 contains the original value of n1.

	Note: this is a great example of [in,out] params!
*
* @param[in,out] n1 - at the call, this contains the first integer value;
					  at return, this contains the second integer value.
* @param[in,out] n2 - at the call, this contains the second integer value;
					  at return, this contains the first integer value.
*
* @returns none
*****************************************************************************/
void swap_integers(int& n1, int& n2)
{
	//need a temporary integer for swapping,
	// then perform the 3 swap actions
	int swap = n2;
	n2 = n1;
	n1 = swap;

	return;
}



/** ***************************************************************************
* @author K. Corwin
*
* @par Description:
* This function takes in two C strings
	and swaps them.  When the function is done,
	s1 contains the original value of s2, and
	s2 contains the original value of s1.

	Note: this is a great example of [in,out] params!
*
* @param[in,out] s1 - at the call, this contains the first C string value;
						at return, this contains the second C string value.
* @param[in,out] s2 - at the call, this contains the second C string value;
						at return, this contains the first C string value.
*
* @returns none
*****************************************************************************/
void swap_cstrings(char s1[], char s2[])
{
	//need a temporary C string for swapping
	char temp[81] = "";

	//perform the 3 actions to swap 2 values
	strcpy(temp, s2);
	strcpy(s2, s1);
	strcpy(s1, temp);

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Writes a set of songs and rankings to a file.
 *
 * @param[in, out] out - the file stream to write to 
 * @param[in] ranks - holds the ranks
 * @param[in] songs - holds the song names
 * @param[in] elements_filled - the number of rank/song pairs
 *
 *****************************************************************************/
void write_list(ofstream & out, int ranks[], char songs[][81],
				int elements_filled)
{
	int i;
	int j = 0;

	// write to file in format rank song, with an endline after each record
	for (i = 0; i < elements_filled; i++)
	{
		out << ranks[i] << " ";

		while (songs[i][j] != '\0' && j < 81)
		{
			out << songs[i][j];

			j++;
		}

		out << endl;

		j = 0;
	}

	return;
}