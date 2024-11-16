/** ***************************************************************************
 * @file
 *
 * @brief Outputs a Fibonacci sequence
 *
 * @mainpage p1004 - Fibonacci Sequence
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
 * @details This program will ask for two starting values and the number of 
 * terms after those starting values and output the appropriate fibonacci 
 * sequence
 *
 *****************************************************************************/

#include <iostream>

using namespace std;

int main()
{
	int i;
	int num_one;
	int num_terms;
	int num_three;
	int num_two;
	

	//prompt for num_one, num_two, and num_terms
	cout << "Enter first term: ";

	cin  >> num_one;

	cout << "Enter second term: ";

	cin  >> num_two;
	
	cout << "Enter number of terms to follow: ";
	
	cin  >> num_terms;

	//calculate and output the fibonacci sequence
	cout << "Sequence:" << endl << num_one << endl << num_two<< endl;

	for (i = 0; i < num_terms; ++i)
	{
		num_three = num_one + num_two;

		cout << num_three << endl;

		num_one = num_two;
		
		num_two = num_three;
	}


	return 0;
}