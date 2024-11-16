/** ************************************************************************
 * @file
 * 
 * @brief Calculates the estimated worth of technology in a dorm room
 *
 * @mainpage Program 1 - Tech Values Calculator
 *
 * @section course_section Course Information
 *
 * @authors Zoe Millage
 *
 * @date September 2020
 *
 * @par Instructors:
 *         Christina Bergevin,
 *          Dr. Daniel de Castro,
 *          Dr. Mengyu Qiao
 *
 * @par Course:
 *         CSC 150 - Computer Science 1
 *
 * @section program_section Program Information
 *
 * @details This program will calculate the estimated worth of the technology
 * in a given dorm room for insurance purposes. The program will prompt the 
 * user for the worth and/or age of their computer, phone, printer, 
 * game consoles, disks, and games. The program will then output a summary 
 * of this information to the screen. The program assumes that an 
 * Xbox is worth $250, a PlayStation is worth $400, a Nintendo (Switch) 
 * is worth $300, a disk is worth $25, and a game is worth $35.
 *
 * @section compile_section Compiling and Usage
 *
 * @par Compiling Instructions:
 *      None
 *
 * @par Usage:
	@verbatim
   c:\> prog1.exe
   d:\> c:\bin\prog1.exe
   @endverbatim
 *
 *****************************************************************************/

#include <cmath>
#include <iomanip>
#include <iostream>
#include <string>
using namespace std;

/******************************************************************************
 *             Constant Variables, defines and Enums
 *****************************************************************************/
 /*!
  * @brief cost of a computer disk
  */
const  double DISK_COST = 25.00;

/*!
 * @brief cost of a console game
 */
const  double GAME_COST = 35.00;

/*!
 * @brief cost of a nintendo switch
 */
const  double NINTENDO_COST = 300.00;

/*!
 * @brief cost of a playstation console
 */
const  double PLAYSTATION_COST = 400.00;

/*!
 * @brief cost of an xbox console
 */
const  double XBOX_COST = 250.00;




/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * This is the starting point to the program.
 * It is also the entire program in this case. The program asks for the value
 * of the user's computer, phone, and printer. It also asks for the age of
 * the user's computer and whether or not the user has an xbox, playstation,
 * or nintendo console. It also asks how many computer disks and console 
 * games the user has. The program then calculates the estimated insurance
 * value of the technology in the room.
 * uses a %20 value loss per year formula, initial value * 0.8^years  
 *
 * @returns 0 Program ran successfully
 *
 *****************************************************************************/
int main() 
{

	char   nintendo_yn;            /*allows the user to indicate if a nintendo
								   switch is owned*/
	char   playstation_yn;         /*allows the user to indicate if a
							       playstation is owned*/
	char   xbox_yn;                //allows user to indicate an owned xbox

	double computer_subtotal;
	double consoles_subtotal = 0;
	double estimated_tech_worth;  /*sum of computer_subtotal,
								  consoles_subtotal, and tech_subtotal*/
	double games_disks_ratio;
	double games_disks_subtotal;
	double tech_subtotal;         //estimated value of a phone and printer
	
	int    computer_age;           //approximate age of a computer in years
	int    computer_disks;         /*allows the user to indicate the number of
							       disks or dvds owned*/
	int    computer_value;         //approximate value of a computer in dollars
	int    console_games;          /*allows the user to indicate the number of
								   console games owned*/
	int    num_consoles = 0;
	int    phone_value;            //approximate age of a computer in years
	int    printer_value;          //approximate age of a computer in years
	int    total_games_disks;      /*the sum of console games, disks, 
								   and dvds. set to 0 in case no games 
								   or disks are owned*/

	string user_name;
	


	//welcome the user
	cout << "Welcome to the Dorm Room Tech Calculator!" << endl << endl;


	//prompt for and get a value for user_name 
	cout << "Please enter your name: ";

	cin >> user_name;


	/*prompt for and get values for computer_value, computer_age, 
	phone_value, and printer_value*/ 
	cout << "Please enter the value of your computer when purchased: ";

	cin >> computer_value;

	cout << "Please enter the age of your computer: ";

	cin >> computer_age;

	cout << "Please enter the value of your phone: ";

	cin >> phone_value;

	cout << "Please enter the value of your printer: ";

	cin >> printer_value;


	//prompt for and get values for xbox_yn, playstation_yn, and nintendo_yn
	cout << "Do you have a XBox (y to add): ";

	cin >> xbox_yn;

	cout << "Do you have a PlayStation (y to add): ";

	cin >> playstation_yn;

	cout << "Do you have a Nintendo (y to add): ";

	cin >> nintendo_yn;


	//prompt for and get values for console_games, and computer_disks
	cout << "Please enter the total number of console games: ";

	cin >> console_games;

	cout << "Please enter the number of other computer or dvd disks: ";

	cin >> computer_disks;


	//set number formatting
	cout << fixed << showpoint << setprecision(2);


	//output user_name with formatting
	cout << endl << user_name << "..." << endl;


	//calculate game_disk_ratio and total_games_disks
	if ((computer_disks != 0) 
		&& (console_games / (double)computer_disks > 0.00))
	{
		games_disks_ratio = console_games / (double)computer_disks;
	}

	else if (computer_disks == console_games)
	{
		games_disks_ratio = 1;
	}

	else
	{
		games_disks_ratio = 0.00;
	}

	total_games_disks = console_games + computer_disks;


	//output game_disk_ratio and total_games_disks
	cout << "You have " << games_disks_ratio 
		 << " ratio of games to disks. " << endl;

	cout << "You have " << total_games_disks << " games and disks. " << endl;


	//calculate and output num_consoles
	if (xbox_yn == 'y')
	{
		++num_consoles;
	}

	if (playstation_yn == 'y')
	{
		++num_consoles;
	}

	if (nintendo_yn == 'y')
	{
		++num_consoles;
	}

	cout << "You have " << num_consoles << " consoles. " << endl;


	//calculate tech_subtotal, game_disks_subtotal, and computer_subtotal
	tech_subtotal = phone_value + printer_value; 

	games_disks_subtotal = (console_games * GAME_COST) 
		+ (computer_disks * DISK_COST);

	computer_subtotal = computer_value * pow(0.8, computer_age);


	//calculate consoles_subtotal
	if (nintendo_yn == 'y')
	{
		consoles_subtotal += NINTENDO_COST;
	}

	if (playstation_yn == 'y')
	{
		consoles_subtotal += PLAYSTATION_COST;
	}

	if (xbox_yn == 'y')
	{
		consoles_subtotal += XBOX_COST;
	}


	/*output tech_subtotal, game_disks_subtotal, computer_subtotal, 
	and consoles_subtotal with formatting*/
	cout << "Other tech subtotal:"
	     << setw(10) << setfill('.') << "$" << tech_subtotal << endl;

	cout << "Games\\disks subtotal:"
		 << setw(9) << setfill('.') << "$" << games_disks_subtotal << endl;

	cout << "Computer subtotal:" 
		 << setw(12) << setfill('.') << "$" << computer_subtotal << endl;

	cout << "Consoles subtotal:" 
		 << setw(12) << setfill('.') << "$" << consoles_subtotal << endl;


	//add the --- line 
	cout << "-" << setw(59) << setfill('-') << "-" << endl;


	//calculate and output estimated_tech_worth;
	estimated_tech_worth = tech_subtotal + computer_subtotal
		+ consoles_subtotal;

	cout << "The estimated tech worth in your room is $" 
		<< estimated_tech_worth << endl << endl;

	//thank the user
	cout << "Thank you for using the calculator!" << endl;

	return 0;
}