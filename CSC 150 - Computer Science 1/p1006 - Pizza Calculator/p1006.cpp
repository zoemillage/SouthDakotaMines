/** ***************************************************************************
 * @file
 *
 * @brief Calculates best deal on pizza given size and cost
 *
 * @mainpage p1006 - Pizza Calculator
 *
 * @section course_section Course Information
 *
 * @authors Zoe Millage
 *
 * @date October 2020
 *
 * @par Course:
 *         CSC 150 - Computer Science 1
 *
 * @section program_section Program Information
 *
 * @details This program asks the user to enter the size (diameter, in
 * inches) of three sizes of pizza, using a function to get each value.
 * The size names are passed from main
 * into the get_diameter and get_cost functions as parameters.
 *
 * Then, the program uses another function to calculate the area of each
 * pizza and return the cost per square inch of each pizza. In the main function,
 * the cost per square inch are compared to find out which pizza size is the
 * best deal.
 *
 * Finally, another function is called to print the results of the best
 * pizza deal, including the pizza diameter, total cost, and cost per
 * square inch.
 *
 *****************************************************************************/

 /*This statement allows use of M_PI in Visual Studio*/
#define _USE_MATH_DEFINES

#include <cmath>
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;

/*Function Prototypes*/
double calc_unit_cost(int diameter, double cost);
double get_cost(string category);
int get_diameter(string pizza_size);
void print_header();
void print_results(int best_d, double best_cost, double best_unit_cost);

/** ***************************************************************************
 * @author M. Qiao and Zoe Millage
 *
 * @par Description:
 * First, set the output iomanip options to print doubles as money values. 
 * Then,call the function to print the welcome message for the program.  
 * Next, call get_diameter and get_cost for each of the three size categories 
 * of pizza. Call calc_unit_cost for each size of pizza to find the unit cost 
 * for each size. Compare the unit costs for each size of pizza to find which 
 * is the best deal (lowest cost per unit) and save that pizza's info as 
 * "best" cost, size, and unit cost. Finally, call print_results to print the 
 * cost, size, and unit cost for the best pizza deal.
 *
 * @returns 0 - Program ran successfully
 *
 *****************************************************************************/
int main()
{
	//Pizza Diameters, in whole inches
	int small_d;
	int med_d;
	int large_d;

	//Total cost of each pizza
	double small_cost;
	double med_cost;
	double large_cost;

	//Cost per square inch of each pizza
	double small_unit_cost;
	double med_unit_cost;
	double large_unit_cost;

	//Store the diameter, total cost, and the cost per square inch
	// for the pizza that is the best deal.
	int best_d;
	double best_cost;
	double best_unit_cost;

	//Set output to 2 decimal places (money)
	cout << fixed << showpoint << setprecision(2);

	//Print header
	print_header();

	//Call the get cost and get diameter functions for each pizza size
	small_d = get_diameter("Small");
	small_cost = get_cost("Small");

	med_d = get_diameter("Medium");
	med_cost = get_cost("Medium");

	large_d = get_diameter("Large");
	large_cost = get_cost("Large");

	//Calculate unit cost for each size
	small_unit_cost = calc_unit_cost(small_d, small_cost);
	med_unit_cost = calc_unit_cost(med_d, med_cost);
	large_unit_cost = calc_unit_cost(large_d, large_cost);

	//Find the best deal - cheapest per square inch - and save the data
	// for that pizza.
	if (small_unit_cost <= med_unit_cost && small_unit_cost <= large_unit_cost)
	{
		best_d = small_d;
		best_cost = small_cost;
		best_unit_cost = small_unit_cost;
	}
	else if (med_unit_cost <= small_unit_cost && med_unit_cost <= large_unit_cost)
	{
		best_d = med_d;
		best_cost = med_cost;
		best_unit_cost = med_unit_cost;
	}
	else
	{
		best_d = large_d;
		best_cost = large_cost;
		best_unit_cost = large_unit_cost;
	}

	cout << endl;

	//print the best deal
	print_results(best_d, best_cost, best_unit_cost);

	//Main function always returns 0; means program was successful.
	return 0;
}



/** ***************************************************************************
* @author Zoe Millage
*   
* @par Description:
* Calculates the area of a pizza then divides the cost by the area. 
*
* @param[in] diameter - the diameter of the pizza
* @param[in] cost - the cost of the pizza
*
* @returns unit_cost - Cost per square inch of a given pizza
*
******************************************************************************/
double calc_unit_cost(int diameter, double cost)
{
	double radius = diameter / 2.0;
	double area;
	double unit_cost;

	// calculate the area and unit cost
	area = M_PI * pow(radius, 2.0);
	unit_cost = cost / area;

	return unit_cost;
}



/** ***************************************************************************
 * @author M. Qiao
 *
 * @par Description:
 * Prints a prompt for the user to enter the total cost for the pizza in the
 * desired size category, given by the category parameter.  Reads in a double
 * input from the user and returns that value as the total cost for the pizza.
 * No error checking is performed on the user input.
 *
 * @param[in] category - The size category for the pizza (e.g. "small")
 *
 * @returns cost - The total cost of that pizza
 * 
 *****************************************************************************/
double get_cost(string category)
{
	double cost;
	cout << "Enter cost for " << category << " pizza: $";
	cin >> cost;
	return cost;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prompts for and gets an integer value of diameter from the user.
 *
 * @param[in] pizza_size - The size of the pizza (e.g. "large")
 *
 * @returns diameter - Diameter for a given size of pizza
 * 
 *****************************************************************************/
int get_diameter(string pizza_size)
{
	// ask for the diameter
	int diameter;
	cout << "Enter diameter for " << (pizza_size) << " pizza: ";
	cin >> diameter;
	return diameter;
}



/** ***************************************************************************
 * @author M. Qiao
 *
 * @par Description:
 * Prints the welcome message for the program followed by a line of dashes.
 *
 * @returns None
 * 
 *****************************************************************************/
void print_header()
{
	cout << "Hardrocker Pizza Comparison Tool" << endl;
	cout << "--------------------------------" << endl << endl;
}



/** ***************************************************************************
* @author Zoe Millage
*
* @par Description:
* Prints the best pizza, cost-per-square-inch wise.
*
* @param[in] best_d - the diameter of the most cost-effective pizza
* @param[in] best_cost - the cost of the most cost-effective pizza
* @param[in] best_unit_cost - the unit cost of the most cost-effective pizza
*
* @returns None
*
******************************************************************************/
void print_results(int best_d, double best_cost, double best_unit_cost)
{
	// print the results
	cout << "Best deal is the " << best_d << "\" pizza." << endl
		 << "Total cost: $" << best_cost << endl
		 << "Cost per square inch: $" << best_unit_cost << endl;

	return;
}
