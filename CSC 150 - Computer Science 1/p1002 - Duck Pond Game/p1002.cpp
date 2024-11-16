/** ***************************************************************************
 * @file
 * 
 * @brief Creates a duck game analysis 
 *
 * @mainpage Lab 4 - Duck Pond Game
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
 * @details In this program, we will analyze a duck pond game. In this game, 
 *	there are a number of rubber ducks floating in a pond. You can buy a 
 *	chance to choose oneduck for a set price, and when you turn it over, 
 *	the duck may or may not have a number on the bottom 
 *	(the amount of money that you win). In our program, we will assume that 
 *	there are exactly 3 winning ducks and that there are at least 4 ducks
 *	in the pond. We can use the expected value formula from statistics to
 *	calculate, on average, whether a player can expect to gain or lose money
 *	by playing the duck pond game, given the following
 *	information: total number of ducks in the pond, cost to buy a duck, and
 *	the winning amount values for each of the 3 winning ducks.
 *	The expected value is a kind of probability-based average.
 *	In this program, our only concern will be whether the expected value is
 *	positive (meaning that if many players played the game many times,
 *	on average they will gain money), or not positive
 *	(the players would either lose money or come out even).
 *
 * After the arrays are filled, we will pass one array to a function that will
 * sort the array using the bubble sort method.  Upon return from this a
 * function will verify that the array was sorted.  If it was not sorted, the
 * program will exit.
 *
 * Because of space, the rest of the details have been omitted.
 *
 ******************************************************************************/

#include <cctype>
#include <cstdlib>
#include <ctime>
#include <iomanip>
#include <iostream>

using namespace std;

int main()
{
	char   enter_random;     /*allows user to choose whether to generate random
							 values or enter their own*/
	char   fixed_time;       //allows user to choose if a seed will be random
	int    seed_value = 1;   /*allows the user to determine the starting seed
							 and assigns the seed to 1 before being changed*/
	int    low_bound;        //lowest random value of a duck
	int    up_bound;         //highest random value of a duck
	double duck_1_win_amt;   //amount won if duck 1 is collected
	double duck_2_win_amt;   //amount won if duck 2 is collected
	double duck_3_win_amt;   //amount won if duck 3 is collected
	double duck_cost;        //cost to buy a duck
	int    total_ducks;
	double expected_value; //expected amount of money to win or lose by playing

	//prints the title of the program
	cout << "Duck Pond Game Analysis" << endl << endl;

	/*prompt to get or generate inputs for duck_1_win_amt, duck_2_win_amt, 
	 duck_3_win_amt	      change format of this comment later*/
	cout << "Do you want to (e)nter the winning duck amounts" << endl
	     << "or (r)andomly generate them? ";

	cin >> enter_random;

	enter_random = tolower(enter_random);

	/*setting precision here due to large number of nested if and else
	statements that use numbers*/
	cout << fixed << showpoint << setprecision(2);

	//generate duck_1_win_amt, duck_2_win_amt, and duck_3_win_amt if necessary
	if (enter_random == 'r')
	{ 
		//prompt for if the seed shall be fixed or based on time
		cout << "(F)ixed seed or (t)ime seed? ";

		cin >> fixed_time;
		
		fixed_time = tolower(fixed_time);

		if (fixed_time == 'f')
		{
			cout << "Enter fixed seed value: ";

			cin >> seed_value;
		}

		//end program if user enters invalid selection
		else if (fixed_time != 't')
		{
			cout << "Invalid selection; please run program again" << endl;

			return 0;
		}
		//prompt for and get lower and upper bounds
		cout << "Lower bound: ";

		cin >> low_bound;

		cout << "Upper bound: ";

		cin >> up_bound;

		if ((up_bound > low_bound) && (low_bound > 0))
		{
			//generates values for winning duck values
			// user given seed
			if (fixed_time == 'f')
			{
				srand(int (seed_value));
				duck_1_win_amt = (rand() % (up_bound - low_bound + 1)) 
					+ low_bound;
				duck_2_win_amt = (rand() % (up_bound - low_bound + 1)) 
					+ low_bound;
				duck_3_win_amt = (rand() % (up_bound - low_bound + 1)) 
					+ low_bound;
				
			}

			// time seed
			else
			{
				srand((int)time(0));
				duck_1_win_amt = (rand() % (up_bound - low_bound + 1))
					+ low_bound;
				duck_2_win_amt = (rand() % (up_bound - low_bound + 1))
					+ low_bound;
				duck_3_win_amt = (rand() % (up_bound - low_bound + 1))
					+ low_bound;
			}
		}
		
		else if (up_bound == low_bound)
		{
			duck_1_win_amt = up_bound;
			duck_2_win_amt = up_bound;
			duck_3_win_amt = up_bound;
		}

		else
		{
			cout << "Invalid bounds; please run program again" << endl;

			return 0;
		}

		cout << "Duck 1 winning amount: $" << duck_1_win_amt << endl;
		cout << "Duck 2 winning amount: $" << duck_2_win_amt << endl;
		cout << "Duck 3 winning amount: $" << duck_3_win_amt << endl;
	} 
	
	// user entered
	else if (enter_random == 'e')
	{
		cout << "Duck 1 winning amount: $";

		cin >> duck_1_win_amt;

		cout << "Duck 2 winning amount: $";

		cin >> duck_2_win_amt;

		cout << "Duck 3 winning amount: $";

		cin >> duck_3_win_amt;
	}

	// invalid initial choice
	else
	{
		cout << "Invalid selection; please run program again" << endl;

		return 0;
	}

	//prompt and get input for duck_cost and total_ducks
	cout << "Enter duck cost: $";

	cin >> duck_cost;

	if (duck_cost <= 0)
	{
		cout << "Invalid selection; please run program again" << endl;

		return 0;
	}

	//calculate expected_value
	cout << "Enter total number of ducks in pond: ";

	cin >> total_ducks;

	if (total_ducks <= 3)
	{
		cout << "Invalid selection; please run program again" << endl;

		return 0;
	}

	//calculate expected_value
	expected_value = (duck_1_win_amt * (1.0 / (total_ducks - 3.0)))
		+ (duck_2_win_amt * (1.0 / (total_ducks - 3.0)))
		+ (duck_3_win_amt * (1.0 / (total_ducks - 3.0)))
		- (duck_cost * ((total_ducks - 3.0) / total_ducks));

	cout << "Expected value: $" << expected_value << endl;

	//determine whether or not to play the duck game
	if (expected_value > 0)
	{
		cout << "You should play the duck game" << endl;
	}

	else
	{
		cout << "You should not play the duck game" << endl;
	}
	return 0;
}