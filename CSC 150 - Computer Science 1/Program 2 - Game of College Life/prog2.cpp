 /** **************************************************************************
  * @file
  * 
  * @brief Lets the player play "The Game of College Life"
  *
  * @mainpage program 2 - Game of College Life
  *
  * @section course_section Course Information
  *
  * @authors Zoe Millage
  *
  * @date October 2020
  *
  * @par Instructors:
  *          Christina Bergevin,
  *          Dr. Daniel de Castro,
  *          Dr. Mengyu Qiao
  *
  * @par Course:
  *         CSC 150 - Computer Science 1
  *
  * @section program_section Program Information
  *
  * @details This is a simplified parody of the Game of Life. Many of the rules
  * will be the same, but the individual squares will be different. The game
  * is also shorter and modified to be single player.
  *
  * @section compile_section Compiling and Usage
  *
  * @par Compiling Instructions:
  *      None
  *
  * @par Usage:
	 @verbatim
	c:\> prog2.exe
	d:\> c:\bin\prog2.exe
	@endverbatim
  *
  * @section todo_bugs_modification_section Todo, Bugs, and Modifications
  *
  * @bug In debug mode, using move values above the  may cause issues when moving extended amounts;
  * the payday functions may not function as intended 
  * if the player is moved values above 6, the maximum spin in normal gameplay.
  *
  *****************************************************************************/
#include <cstdlib>
#include <ctime>
#include <iomanip>
#include <iostream>

using namespace std;

//function prototypes
void avoid_event();
void bank_menu(int data[]);
int  bank_options(int player_choice, int data[]);
void bank_text(int data[]);
void bet_on_wheel(int data[]);
void borrow_from_bank(int data[]);
void end_game(int data[], char player_name[]);
int  exit_game();
char initialization(char player_name[], int data[]);
void interest_due(int data[]);
void main_menu(int data[], char player_name[], char game_board[], 
			   char tud, char default_board[]);
int  menu_options(int player_choice, int data[], char name[], 
				  char game_board[], char tud, char default_board[]);
void menu_text(int data[], char name[], char game_board[]);
void move(char tud, int data[], char game_board[], 
		  char default_board[], char player_name[]);
void new_cat(int data[]);
void new_dog(int data[]);
void pass_payday(int data[]);
void payday(int data[]);
int  spin(char tud);
void square0_11(int data[]);
void square12_23(int data[]);
void square24_34(int data[], char player_name[]);
void square_events(int data[], char player_name[]);
void two_paydays(int data[]);

 /*****************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calls initialization, which prints the starting lines for the game, 
 * then it calls main_menu, which can call the rest of the game functions.
 *
 * @returns 0 - program ran successfully
 *
 *****************************************************************************/
int main()
{
	char default_board[40] = "......PS.S...P..S..P..SP......P....";
		                   /*will be used to revert a given space back 
						   to its original after a player leaves the space*/
	char game_board[40] = "......PS.S...P..S..P..SP......P....";
	char player_name[50];
	char t_u_d;           /*used to dertermine the user's game mode:
						  t: time mode
						  u: user defined
						  d: debug */

	int data[30] = { 0, 0, 1000, 0, 0, 0, 0, 0, 0 };
					  /* [0] holds the length of the player's name,
					  [1] paycheck value, [2] funds, [3] debts, 
					  [4] is the player's position on the board,
					  [5] determines whether or not the player can bet
					  [6] is the number of dogs the player has
					  [7] is the number of cats the player has
					  [8] determines whether or not the 
					  player has had/is eligible for a payday*/

	//calls initialization and main_menu
	t_u_d = initialization(player_name, data);
	main_menu(data, player_name, game_board, t_u_d, default_board);

	return 0;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Outputs "Avoided Event."
 *
 * @returns none
 *****************************************************************************/
void avoid_event()
{
	cout << "Avoided Event" << endl;

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calls the other bank functions.
 *
 * @param[in] data[] - holds the player's game data
 *
 * @returns none
 *****************************************************************************/
void bank_menu(int data[])
{
	int player_choice = -1;

	//call menu_text and menu_options while user does not type 0 to quit
	while (player_choice != 1 && player_choice != 2 && player_choice != 0)
	{
		bank_text(data);

		cin >> player_choice;

		player_choice = bank_options(player_choice, data);
	}

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Lets the player borrow from or pay the bank and lets them exit the bank 
 * menu.
 *
 * @param[in] player_choice - determines what action the function takes
 * @param[in] data[] - holds the player's game data
 *
 * @returns the player choice, forwarded to other menu functions
 * 
 *****************************************************************************/
int bank_options(int player_choice, int data[])
{
	if (player_choice == 1)
	{
		//prompts for loan
		cout << "Enter value to borrow or 0 to exit (multiple of $2,000): ";
		cin >> player_choice;

		//prevents non $2,000 increments
		while (player_choice % 2000 != 0)
		{
			cout << "Invalid amount. Need multiple of $2,000." << endl
				<< "Enter value to borrow or "
				<< "0 to exit (multiple of $2,000): ";
			cin >> player_choice;
		}

		//add to funds and debts
		data[2] += player_choice;
		data[3] += player_choice;
	}

	else if (player_choice == 2)
	{
		//prompt for payment
		cout << "Enter value to pay or 0 to exit (multiple of $2,000): ";
		cin >> player_choice;

		//prevents non $2,000 increments
		while (player_choice % 2000 != 0)
		{
			cout << "Invalid amount. Need multiple of $2,000." << endl
				<< "Enter value to pay or 0 to exit (multiple of $2,000): ";
			cin >> player_choice;
		}

		//pays off debt
		data[2] -= player_choice;
		data[3] -= player_choice;
	}

	return player_choice;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints bank_menu text.
 *
 * @param[in] data[] - lets the function print funds and debts
 *
 * @returns none
 *****************************************************************************/
void bank_text(int data[])
{
	//print the bank text
	cout << endl << "Welcome to your bank" << endl
		<< "\tYour current funds are: $" << data[2] << ".00" << endl
		<< " \tYour current debt is: $" << data[3] << ".00" << endl << endl
		<< "1) Borrow" << endl << "2) Pay" << endl << "0) Exit" << endl
		<< endl << "Enter your choice: ";

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Lets the player bet on the wheel.
 *
 * @param[in, out] data[] - allows funds to be updated and reads 
 * if the player has bet on this turn
 *
 * @returns none
 *****************************************************************************/
void bet_on_wheel(int data[])
{
	int number_one = 0;
	int number_two = 0;
	int random_number;

	/*If bet_on_wheel has been called this turn, stop the player
	data[5] is used to determine if the user has bet this turn*/
	if (data[5] > 0)
	{
		cout << "Extra bet not allowed in this turn." << endl << endl;

		return;
	}

	//prompt for betting numbers
	cout << "You are betting against the wheel" << endl
		<< "Your bet is $1,000, you need to choose 2 numbers from 1 to 6."
		<< endl << "Choose number 1: ";
	cin >> number_one;
	cout << "Choose number 2: ";
	cin >> number_two;

	random_number = (rand() % 6) + 1;

	//"spin the wheel" and calculate results
	cout << "You spin the wheel and get a " << random_number << endl;

	if (number_one == random_number || number_two == random_number)
	{
		cout << "YOU WIN! You receive $10,000" << endl;

		data[2] += 10000;
	}

	else
	{
		cout << "You lost your bet." << endl;

		data[2] -= 1000;
	}

	//sets data[5] to 1; stops the player from betting multiple times per turn
	data[5] = 1;

	cout << endl;

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Borrows $2,000 until the player has positive funds.
 *
 * @param[in, out] data[] - allows funds and debts to be updated
 *
 * @returns none
 *****************************************************************************/
void borrow_from_bank(int data[])
{
	int amt_borrowed = 0;  //amount borrowed

	//borrows in $2,000 increments 
	while (data[2] < 0)
	{
		data[2] += 2000;
		data[3] += 2000;
		amt_borrowed += 2000;
	}

	//outputs how much was borrowed
	cout << "You borrowed $" << amt_borrowed << ".00 from the bank" << endl;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the final scores of the game and output if the player won.
 * If a player has debt, they pay it back with 25% interest.
 * If a player has a pet, they gain $2000 per pet.
 * If the player wins if their remaining money isn't negative.
 *
 * @param[in, out] data[] - allows function to update the player's game data
 * @param[in] player_name[] - the player's name
 *
 * @returns none
 *****************************************************************************/
void end_game(int data[], char player_name[])
{
	int i;  //loop variable

	//subtracts debt with interest from funds
	while (data[3] > 0)
	{
		data[3] -= 2000;
		data[2] -= 2500;
	}

	//awards $2,000 per pet owned 
	if (data[6] > 0 || data[7] > 0)
	{
		data[2] += (2000 * data[6]) + (2000 * data[7]);
	}

	//congradulates the player by name for graduating
	cout << "Congratulations! You've graduated" << endl << endl
		<< "Calculating results for ";

	for (i = 0; i < data[0]; ++i)
	{
		cout << player_name[i];
	}

	//the player loses if they are in debt
	cout << endl << "\tTotal = $" << data[2] << ".00" << endl;

	if (data[2] < 0)
	{
		cout << "You lost!";
	}

	//congradulates the player if they have positive funds at game's end
	else
	{
		cout << "Congratulations, you win!";
	}

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * ends the program
 *
 * @returns 0 - the game ended
 *****************************************************************************/
int exit_game()
{
	cout << endl << "Goodbye" << endl;

	return 0;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Initializes the game, using the seed and game mode of choice.
 * Sets the seed, player's name, payday value. 
 *
 * @param[in, out] player_name[] - the player's name
 * @param[in, out] data[] - allows the function to update the player's funds
 *
 * @returns the seed for rand calculations
 *****************************************************************************/
char initialization(char player_name[], int data[])
{
	char name;           //the player's name
	char seed_choice;    /*lets the user choose their game mode, 
					  	 time, user-defined, or debug */
	int i = 0;           //loop variable
	int payday_value = 0;
	int seed_value = -1; //if -1, the player is considered to be in time mode

	//print introduction lines, prompt for and get seed_chioce
	cout << "Welcome to the Game of College Life" << endl << endl
		 << "Seed for randomization (T)ime, (U)ser defined, (D)ebug mode: ";
	cin  >> seed_choice;

	//convert seed_choice to lowercase
	seed_choice = tolower(seed_choice);

	//print "Invalid Option" and the first question if seed_choice is invalid
	while (seed_choice != 't' && seed_choice != 'u' && seed_choice != 'd')
	{
		cout << "Invalid Option" << endl
			 << "Seed for randomization (T)ime, (U)ser defined, (D)ebug mode: ";
		cin  >> seed_choice;

		seed_choice = tolower(seed_choice);
	}

	//ask for seed value if user typed u or d; set srand
	if (seed_choice == 'u' || seed_choice == 'd')
	{
		cout << "Write a number for the seed: ";
		cin >> seed_value;

		srand ( int ( seed_value ) );
	}

	else
	{
		srand ( int ( time( 0 ) ) );
	}

	//prompt for and get player_name, calculate name_length
	cout << "Name of player 1? ";
	cin >> name;

	//transfer player_name into an array
	while (name != '\n' && i < 50)
	    {
		    player_name[i] = name;
			i++;

			cin.get(name);
		}

	data[0] = i;

	//spin the wheel and print result
	payday_value = (rand() % 6) + 1;

	cout << "Spinning wheel for payment..." << payday_value << "..." << endl;

	//"calculate" and output payday_value
	if (payday_value < 3)
	{
		data[1] = 500;
	}

	else if (payday_value < 6)
	{
		data[1] = 1000;
	}

	else
	{
		data[1] = 1500;
	}

	cout << "Your payment will be $" << data[1] << " on each payday." << endl << endl;

	return seed_choice;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Makes the player pay $100 per $2,000 of debt they have.
 *
 * @param[in, out] data[] - allows the function to update the player's funds
 *
 * @returns none
 *****************************************************************************/
void interest_due(int data[])
{
	int amount_owed;

	//avoids the event if the player has no debt
	if (data[3] == 0)
	{
		avoid_event();
	}

	//makes the player pay $100 per $2,000 of debt
	else
	{
		amount_owed = (data[3] / 2000) * 100;

		data[2] -= amount_owed;

		cout << "Penalty: Interest Due. Pay $100 for "
			<< "every $2,000 owed in debt -- pay " << amount_owed << endl;
	}
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calls menu_text and menu_options and calls exit_game if the player enters 0.
 *
 * @param[in] data[] - holds the player's game data
 * @param[in] player_name[] - the player's name
 * @param[in] game_board[] - the layout of the game board
 * @param[in] tud - determines the player's game mode
 * @param[in] default_board[] - the default layout of the game board
 *
 * @returns none
 *****************************************************************************/
void main_menu(int data[], char player_name[], char game_board[],
			   char tud, char default_board[])
{
	int player_choice = -1;

	//call menu_text and meny_options while user does not type 0 to quit
	while (player_choice != 0 && data[4] < 36)
	{
		menu_text(data, player_name, game_board);

		cin >> player_choice;

		player_choice = menu_options
		(player_choice, data, player_name, game_board, tud, default_board);
	}

	//lets the player exit the game
	exit_game();

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calls functions based on the players choice in menu_text.
 *
 * @param[in] player_choice - determines what function is called
 * @param[in] data[] - holds the player's game data
 * @param[in] player_name[] - the player's name 
 * @param[in] game_board[] - the layout of the game board
 * @param[in] tud - determines the player's game mode
 * @param[in] default_board[] - the default layout of the game board
 *
 * @returns the menu choice, forwarded to other menu options
 *****************************************************************************/
int menu_options(int player_choice, int data[], char player_name[],
				 char game_board[], char tud, char default_board[])
{
	//print "Invalid Option" and the first question if player_choice is invalid
	while (player_choice != 0 && player_choice != 1 && player_choice != 2
		   && player_choice != 3)
	{
		cout << "Invalid option";

		menu_text(data, player_name, game_board);

		cin >> player_choice;
	}

	//calls spin, bet_on_wheel, bank_menu, or exit_game depending on input
	if (player_choice == 1)
	{
		move(tud, data, game_board, default_board, player_name);
	}

	else if (player_choice == 2)
	{
		bet_on_wheel(data);
	}

	else if (player_choice == 3)
	{
		bank_menu(data);
	}

	return player_choice;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints the main menu.
 *
 * @param[in] data[] - holds the player's game data
 * @param[in] name[] - the player's name
 * @param[in] game_board[] - the layout of the game board including 
 *    player position
 *
 * @returns none
 *****************************************************************************/
void menu_text(int data[], char name[], char game_board[])
{
	int i;     //loop variable

	//output Player 1: [player name] and the game board
	cout << "Player 1: ";

	for (i = 0; i < data[0]; ++i)
	{
		cout << name[i];
	}

	cout << "'s turn" << endl;

	cout << "=" << setw(47) << setfill('=');
	cout << "=" << endl << "Game board: ";

	for (i = 0; i < 35; ++i)
	{
		cout << game_board[i];
	}
	
	cout << endl << "=" << setw(47) << setfill('=');
cout << "=" << endl;

//output funds and debt with 2 significnat figures
cout << fixed << showpoint << setprecision(2);

cout << "Current funds : $" << data[2] << ".00" << endl
<< "Current Debt : $" << data[3] << ".00" << endl << endl;

//print options, prompt for and get player_chioce
cout << "1) Spin" << endl << "2) Bet on Wheel" << endl
<< "3) Go to the Bank" << endl << "0) Quit" << endl << endl
<< "Enter your choice: ";

return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Moves the player.
 *
 * @param[in] tud - determines if the player is in dubug mode or not
 * @param[in, out] data[] - allows the function to update the player's position
 * @param[in, out] game_board[] - allows the function to update player location
 * @param[in] default_board[] - the default position of the game board
 * @param[in] player_name[] - the player's name
 *
 * @returns none
 *****************************************************************************/
void move(char tud, int data[], char game_board[],
		  char default_board[], char player_name[])
{
	int spin_value;

	//updates the board to its original state
	game_board[data[4] - 1] = default_board[data[4] - 1];

	//call spin and output spin_value
	spin_value = spin(tud);

	cout << "You spin the wheel and get..." << spin_value << endl;

	//output special payday functions if needed
	if (data[4] == 20 && spin_value >= 5
		|| data[4] == 19 && spin_value == 6)
	{
		two_paydays(data);
	}

	if ((data[4] == 10 && spin_value > 6) ||
		(data[4] == 17 && spin_value == 6) ||
		data[4] == 29 && spin_value > 6)
	{
		pass_payday(data);
	}

	if (data[4] == 23 && spin_value > 1)
	{
		pass_payday(data);
	}

	//updates the player's position
	data[4] += spin_value;

	game_board[data[4] - 1] = '1';

	//calls square_events
	square_events(data, player_name);
	 
	//allows the player to bet next turn
	data[5] = 0;

	cout << endl;

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Gives the player a cat.
 *
 * @param[in, out] data[] - allows the function to update the player's number 
 * of cats
 *
 * @returns none
 *****************************************************************************/
void new_cat(int data[])
{
	//gives the player a cat
	cout << "Status: You got a cat" << endl;

	++data[7];

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Gives the player a dog.
 *
 * @param[in, out] data[] - allows the function to update the player's number 
 * of dogs
 *
 * @returns none
 *****************************************************************************/
void new_dog(int data[])
{
	//gives the player a dog
	cout << "Status: You got a dog" << endl;

	++data[6];

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Outputs that the player passed a payday and their payday value;
 * gives the player funds equal to their payday value.
 *
 * @param[in, out] data[] - allows the function to update the player's funds 
 * and the number of paydays they've passed in this block of squares
 *
 * @returns none
 *****************************************************************************/
void pass_payday(int data[])
{
	//output passing payday message
	cout << "You passed over 1 payday so you receive $"
		<< data[1] << endl;

	data[2] += data[1];

	//update number of paydays passed in block
	++data[8];

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Outputs payday and gives the player funds equal to their payday value.
 *
 * @param[in, out] data[] - allows the function to update the player's funds 
 * and the number of paydays they've passed in this block of squares
 *
 * @returns none
 *****************************************************************************/
void payday(int data[])
{
	//output payday
	cout << "Payday" << endl;

	data[2] += data[1];

	//update number of paydays passed in block
	++data[8];

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Determines how much the player will move with the move() function.
 *
 * @param[in] tud - determines if the player is in time, user deifned, or 
 * debug mode
 *
 * @returns How many spaces the plaer moves this turn
 *****************************************************************************/
int spin(char tud)
{
	int spin_value = 0;

	//calculates the spin value if the user is not in debug mode
	if (tud != 'd')
	{
		spin_value = (rand() % 6) + 1;
	}

	//if the user is in debug mode, allow them to determine their spin value
	else
	{
		cout << "Spin value: ";
		cin >> spin_value;
	}

	return spin_value;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * contains the conditions and events that will occur while the player is
 * on squares between square 0 and 11.
 *
 * @param[in, out] data[] - determines where the player is on the game board 
 * and lets the function update funds and debts 
 *
 * @returns none
 *****************************************************************************/
void square0_11(int data[]) 
{
	//conditions for landing on or passing payday spaces
	if (data[4] < 7)
	{
		data[8] = 0;
	}

	else if (data[4] > 7 && data[8] < 1)
	{
		pass_payday(data);
	}

	//conditions/events for non payday squares 0-11
	if (data[4] == 1)
	{
		cout << "Reward: Accepted Party -- gain $500" << endl;

		data[2] += 500;
	}

	if (data[4] == 2)
	{
		cout << "Reward: Grandpa gave you money for a car --"
			 << " gain $1,500" << endl;

		data[2] += 1500;
	}

	if (data[4] == 3)
	{
		cout << "Penalty: Buy Books -- pay $500" << endl;

		data[2] -= 500;
	}

	if (data[4] == 4)
	{
		cout << "Reward: Lucky day -- gain $2,000" << endl;

		data[2] += 2000;
	}

	if (data[4] == 5)
	{
		cout << "Penalty: Course Fees -- pay $700" << endl;

		data[2] -= 700;
	}

	if (data[4] == 6)
	{
		cout << "Penalty: Buy furniture -- pay $2,000" << endl;

		data[2] -= 2000;
	}

	if (data[4] == 7)
	{
		payday(data);
	}

	if (data[4] == 8)
	{
		new_dog(data);
	}

	if (data[4] == 9)
	{
		cout << "Reward: Sold a project to a company -- gain $10,000" << endl;

		data[2] += 10000;
	}

	if (data[4] == 10)
	{
		new_cat(data);
	}

	if (data[4] == 11)
	{
		interest_due(data);
	}

	if (data[4] == 12)
	{
		cout << "Reward: Grandma came to visit -- gain $500" << endl;

		data[2] += 500;
	}

	//borrows in increments of 2000 until the player has positive funds
	if (data[2] < 0)
	{
		borrow_from_bank(data);
	}

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Contains the conditions and events that will occur while the player is
 * on squares between square 12 and 23.
 *
 * @param[in, out] data[] - determines where the player is on the game board
 * and lets the function update funds and debts
 *
 * @returns none
 *****************************************************************************/
void square12_23(int data[])
{
	//conditions for landing on or passing payday spaces
	if (data[4] < 14)
	{
		data[8] = 0;
	}

	else if (data[4] > 14 && data[8] < 1)
	{
		pass_payday(data);
	}

	else if (data[4] > 20 && data[8] < 2)
	{
		pass_payday(data);
	}

	//conditions/events for non payday squares 12-23
	if (data[4] == 13)
	{
		if (data[6] > 0)
		{
			cout << "Penalty: If you own a dog, your dog chewed through" 
				 << " the door -- pay $700" << endl;

			data[2] -= 700;
		}

		else
		{
			avoid_event();
		}
	}

	if (data[4] == 14 || data[4] == 20 || data[4] == 24)
	{
		payday(data);
	}

	if (data[4] == 15)
	{
		cout << "Penalty: Mice found!"
		 	 << " Call an exterminator -- pay $2,000" << endl;

		data[2] -= 2000;
	}

	if (data[4] == 16)
	{
		cout << "Reward: Help friend move -- gain $1,000" << endl;

		data[2] += 1000;
	}

	if (data[4] == 17)
	{
		new_cat(data);
	}

	if (data[4] == 18)
	{
		cout << "Reward: Won a new computer -- gain $1,500" << endl;

		data[2] += 1500;
	}

	if (data[4] == 19)
	{
		cout << "Reward: Got a scholarship -- gain $5,000" << endl;

		data[2] += 5000;
	}

	if (data[4] == 21)
	{
		cout << "Reward: Aced your Midterm -- gain $1,000" << endl;

		data[2] += 1000;
	}

	if (data[4] == 22)
	{
		interest_due(data);
	}

	if (data[4] == 23)
	{
		new_dog(data);
	}

	//borrows in increments of 2000 until the player has positive funds
	if (data[2] < 0)
	{
		borrow_from_bank(data);
	}

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Contains the conditions and events that will occur while the player is 
 * on squares beyond square 23.
 *
 * @param[in, out] data[] - determines where the player is on the game board
 *    and lets the function update funds and debts
 * @param[in] player_name[] - the name of the player
 *
 * @returns none
 *****************************************************************************/
void square24_34(int data[], char player_name[])
{
	int vet_fee = 0;
	int calming_value = 0; /*how much the player gains if 
						   a pet calms them during finals*/

	//conditions for landing on or passing payday spaces
	if (data[4] < 31 && data[8] == 2)
	{
		data[8] = 0;
	}

	if (data[4] > 24 && data[8] < 1)
	{
		pass_payday(data);
	}

	else if (data[4] > 31 && data[8] < 2)
	{
		pass_payday(data);
	}
	
	else if (data[4] > 31 && data[8] < 1)
	{
		two_paydays(data);
	}

	if (data[4] == 31)
	{
		payday(data);
	}

	//conditions/events for non payday squares 24-34
	if (data[4] == 25)
	{
		cout << "Penalty: Caught the flu -- pay $1,000" << endl;

		data[2] -= 1000;
	}

	if (data[4] == 26)
	{
		if (data[6] > 0)
		{
			vet_fee = 300 * data[6];

			cout << "Penalty: If you have a dog, take it to the vet -- pay $"
				 << vet_fee << endl;

			data[2] -= vet_fee;
		}

		else
		{
			avoid_event();
		}

	}
	
	if (data[4] == 27)
	{
		if (data[7] > 0)
		{
			vet_fee = 300 * data[7];

			cout << "Penalty: If you have a cat, take it to the vet -- pay $"
			 	 << vet_fee << endl;

			data[2] -= vet_fee;
		}

		else
		{
			avoid_event();
		}
	}

	if (data[4] == 28)
	{
		if (data[6] > 0)
		{
			calming_value = 300 * data[6];

			cout << "Reward: If you have a dog, it keeps you calm" 
				 << " through finals -- gain $" << calming_value << endl;

			data[2] += calming_value;
		}

		else
		{
			avoid_event();
		}
	}

	if (data[4] == 29)
	{
		if (data[7] > 0)
		{
			calming_value = 300 * data[7];

			cout << "Reward: If you have a cat, it keeps you calm"
			 	 << " through finals -- gain $" << calming_value << endl;

			data[2] += calming_value;
		}

		else
		{
			avoid_event();
		}
	}

	if (data[4] == 30)
	{
		cout << "Penalty: Your computer crashed when "
			 << "you worked on your final paper -- pay $3,500" << endl;

		data[2] -= 3500;
	}

	if (data[4] == 32)
	{
		cout << "Reward: Job offer -- gain $5,000" << endl;

		data[2] += 5000;
	}

	if (data[4] == 33)
	{
		interest_due(data);
	}

	if (data[4] == 34)
	{
		cout << "Reward: Sold a project to a company -- gain $10,000" << endl;

		data[2] += 10000;
	}

	if (data[4] == 35)
	{
		cout << "Penalty: Sickness due to final exam stress -- "
			 << "pay $1,000" << endl;

		data[2] -= 1000;
	}

	//borrows in increments of 2000 until the player has positive funds
	if (data[2] < 0)
	{
		borrow_from_bank(data);
	}

	//calls end_game if the player passes or lands on square 35
	if (data[4] > 35)
	{
		end_game(data, player_name);
	}
	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calls the other square event functions.
 *
 * @param[in] data[] - determines where the player is on the game board
 * @param[in] player_name[] - the name of the player
 *
 * @returns none
 *****************************************************************************/
void square_events(int data[], char player_name[])
{
	/*square 0 is functionally the 1st square after the starting space,
	this will appear in this code as data[4] = 1 */

	if (data[4] < 13)
	{
		square0_11(data);
	}

	else if (data[4] < 25)
	{
		square12_23(data);
	}

	/*end_game is called in this function to account for
	passing sqare 30's payday while also
	winning the game by passing the 34th square*/
	else
	{
		square24_34(data, player_name);
	}

	return;
}



/******************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * In specific cases, the player can pass over two payday spaces. This will
 * give 2 paychecks worth of funds to the player.
 *
 * @param[in, out] data[] - allows the function to call the player's 
 * paycheck and update their current funds
 *
 * @returns none
 *****************************************************************************/
void two_paydays(int data[])
{
	int double_payday;

	//calculates and outputs 2 paydays worth of funds
	double_payday = 2 * data[1];

	cout << "You passed over 2 paydays so you receive $"
		 << data[1] << endl;

	/*updates player's funds and the number of paydays they have had in
	a given block of square events*/
	data[2] += double_payday;

	data[8] = 1;

	return;
}