/** ***************************************************************************
 * @file
 *
 * @brief Calculates grades based on scores out of 100
 *
 * @mainpage p1008 - Grade Calculator
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
 * @details This program asks for the number (under some threshold) of grades 
 * to analyze, calculating and outputting the minimum, maximum, and average 
 * grades. Grades should be entered as assignment scores 
 * (e.g. scoring 70/100 on a test means entering 70) 
 *
 *****************************************************************************/

#include <iomanip>
#include <iostream>

using namespace std;

//constant(s)
const int MAX_STUDENT_GRADES = 25;

//function prototypes
double calc_avg(int array[], int number_grades);
void find_min_max(int array[], int array2[], int number_grades);
void get_grades(int array[], int number_grades);


int main()
{
	//declare variables
	double student_average;
	int max_min[2];
	int number_grades;
	int student_grades[MAX_STUDENT_GRADES];

	cout << "There is space for " << MAX_STUDENT_GRADES
		<< " grades available." << endl;
	cout << "How many grades to analyze? ";
	cin >> number_grades;

	//call get_grades;
	get_grades(student_grades, number_grades);

	//call calc_avg and find_mind_max
	student_average = calc_avg(student_grades, number_grades);

	find_min_max(student_grades, max_min, number_grades);

	cout << fixed << showpoint << setprecision(2);

	cout << endl << "Average: " << student_average << endl
		<< "Min: " << max_min[1] << endl
		<< "Max: " << max_min[0] << endl;

	return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the averge value from an array of integers.
 *
 * @param[in] array - holds all the data
 * @param[in] number_grades - the number of values in array
 *
 * @returns the average
 *
 *****************************************************************************/
double calc_avg(int array[], int number_grades)
{
	double avg;
	int i;
	int total = 0;

	// get the sum
	for (i = 0; i < number_grades; i++)
		total += array[i];

	// divide to get the average
	avg = (double)total / number_grades;

	return avg;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Finds the minimum and maximum values in an integer array and stores
 * them in a separate integer array.
 *
 * @param[in, out] array - holds the data
 * @param[in, out] array2 - holds the maximum and minimum values
 * @param[in] number_grades - the number of values in array
 *
 *****************************************************************************/
void find_min_max(int array[], int array2[], int number_grades)
{
	int i;

	/*array2 has only 2 elements, array2[0] will be maximum value,
	and array2[1] will be the minimum value*/
	array2[0] = array[0];
	array2[1] = array[0];

	// get the maximum
	for (i = 0; i < number_grades; ++i)
	{
		if (array2[0] < array[i])
		{
			array2[0] = array[i];
		}
	}

	// get the minimum
	for (i = 0; i < number_grades; ++i)
	{
		if (array2[1] > array[i])
		{
			array2[1] = array[i];
		}
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes in the given number of grades and puts them into an array.
 *
 * @param[in, out] array - holds the grades
 * @param[in] number_grades - the number of grades to take in
 *
 *****************************************************************************/
void get_grades(int array[], int number_grades)
{
	int i;

	// take in the grades
	cout << "Enter grades: ";

	for (i = 0; i < number_grades; ++i)
	{
		cin >> array[i];
	}

	return;
}