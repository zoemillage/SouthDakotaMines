/** ***************************************************************************
 * @file
 *
 * @brief Checks the contents of a C-string and if it is a palindrome 
 *
 * @mainpage p1012 - C-string Analysis
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
 * @details This program asks for a message, determines if the given message 
 * is a palindrome, and prints how many of each letter appears in said message.
 *
 *****************************************************************************/

//This define is needed to avoid Visual Studio cstring function warnings
#define _CRT_SECURE_NO_WARNINGS
#include <cstring>
#include <iostream>

using namespace std;

void count_letters(char user_message[], int letter_counts[], 
				   int message_length);
void is_palindrome(char user_message[], int message_length);				   
void print_counts(int letter_counts[]);


int main()
{
	int letter_counts[26] = { 0 };
	int message_length;
	char user_message[81];

	//Ask for user input string
	cout << "Enter your message, up to 80 characters: " << endl;
	cin.get(user_message, 81);

	message_length = (int)strlen(user_message);

	//This endl is in the starter file for formatting purposes
	cout << endl;

	// check the character array
	is_palindrome(user_message, message_length);

	count_letters(user_message, letter_counts, message_length);

	print_counts(letter_counts);

	return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Counts how many of each letter is in the given character array.
 *
 * @param[in] user_message - the string to check
 * @param[in, out] letter_counts - holds the amount of each letter
 * @param[in] message_length - the length of the string 
 *
 *****************************************************************************/
void count_letters(char user_message[], int letter_counts[], 
				   int message_length)
{
	int i;
	int letter;

	// go through each character, adding the correct character to the counts
	for (i = 0; i < message_length + 1; i++)
	{
		if (isalpha(user_message[i]))
		{
			user_message[i] = toupper(user_message[i]);

			letter = (int)user_message[i] - 'A';

			++letter_counts[letter];
		}
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Checks if a given character array is a palindrome.
 *
 * @param[in] user_message - the character array to check
 * @param[in] message_length - the length of the character array
 *
 *****************************************************************************/
void is_palindrome(char user_message[], int message_length)
{
	char reverse_message[81];

	// copy and reverse the string
	strncpy(reverse_message, user_message, message_length + 1);

	_strrev(reverse_message);

	// check if the strings are the same
	if (strncmp(user_message, reverse_message,
		message_length) == 0)
	{
		cout << "Your message is a palindrome." << endl;
	}

	else
		cout << "Not a palindrome." << endl;

	return;
}



/** ***************************************************************************
* @author K Corwin
*
* @par Description:
* This function takes in an array of counts of letters.  The function
	prints out the contents of each array element, labeled with the
	appropriate letter of the alphabet (i.e. the value in the first
	element is labeled A: , the second element is labeled B: , etc.).
*
* @param[in] letter_counts - the array of letter counts
*
* @returns none
*****************************************************************************/
void print_counts(int letter_counts[])
{
	char letter = 'A';
	for (int index = 0; index < 26; index++)
	{
		cout << letter << ": " << letter_counts[index] << endl;
		letter++;
	}
}