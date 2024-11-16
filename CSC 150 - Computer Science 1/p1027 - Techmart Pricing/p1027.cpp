/** ***************************************************************************
 * @file
 * 
 * @brief Simulated program used for a retail store
 *
 * @mainpage Lab 5 - TechMart
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
 * @details You are working on a new program at your personal business.The old
 * system was done by hand and you wish to remove human error from the 
 * calculations of the discounts and price. When your program first starts up 
 * you will display the title SDSM&T TechMart to the screen. You will then 
 * ask if the customer is a club member. Then you will ask if the order is 
 * tax exempt. After retrieving the basic information about the customer, you 
 * will input the prices of all the items in the cart summing each item until 
 * the cashier (you) presses ctrl-z. After inputting all the items, you will
 * calculate the tax that will be added to the final bill based on the full 
 * amount of the subtotal. You will also calculate the amount of discount 
 * the customer receives based on the full amount of the subtotal.
 * Tax Rate is 6%. Club discount is 10%.
 *
 *****************************************************************************/

#include <iomanip>
#include <iostream>

using namespace std;


 /** **************************************************************************
  * @author Zoe Millage
  *
  * @par Description:
  * This is the starting point to the program. 
  * It is also the entire program in this case. The program asks if the user 
  * is a club member or tax exempt. It then finds the sum of a shopping list, 
  * applies deductions if applicable, and outputs the total
  *
  * @returns 0 - Program ran successfully
  *
  ******************************************************************************/
int main()
{
	char   club_member_yn;        /*allows user to state if they 
								  are a club member*/
	char   tax_exempt_yn;         /*allows user to state if they 
								  are tax exempt*/

	double member_discount;       //10%
	double price;                 //indicates the price of an item
	double sub_total = 0;
	double tax;                   //6%
	double total_bill;            //subtotal with applicable deductions

	int    items_purchased = 0;

	cout << endl << "SDSM&T TechMart";

	//prompt and get inputs for club_member_yn and tax_exampt_yn;
	cout << endl << "Are you a club member (y/n): ";

	cin >> club_member_yn;

	cout << endl << "Are you taxed exempt (y/n): ";

	cin  >> tax_exempt_yn;

	//prompts for and get input(s) for price using a while loop
	cout << endl << "Enter price: ";

	while (cin >> price)
	{
		sub_total += price;

		++items_purchased;

		cout << endl << "Enter price: ";
	}

	//formats numbers into a currency format
	cout << fixed << showpoint << setprecision(2);

	//calculates and /outputs items_purchased and sub_total
	cout << endl << "Items Purchased : " << setw(5) << setfill(' ') 
		<< items_purchased;

	cout << endl << "Sub Total       $ " << setw(8) << setfill(' ') 
		<< sub_total;

	//calculates and outputs member_discount and tax_applied
	if (club_member_yn == 'y')
	{
		member_discount = -sub_total * 0.1;

	}

	 else if (club_member_yn == 'n')
	{
		member_discount = 0;
	}


	if (tax_exempt_yn == 'n')
	{
		tax = sub_total * 0.06;
	}

	 else if (tax_exempt_yn == 'y')
	{
		tax = 0;
	}


	cout << endl << "Member Discount $ " << setw(8) << setfill(' ') 
		<< member_discount;

	cout << endl << "Tax             $ " << setw(8) << setfill(' ') << tax;

	//calculates and outputs total_bill
	total_bill = sub_total + member_discount + tax;

	cout << endl << "Total Bill      $ " << setw(8) << setfill(' ') 
		<< total_bill;


	return 0;
}