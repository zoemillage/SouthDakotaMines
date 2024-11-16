/** ***************************************************************************
 * @file
 *
 * @brief Checks the strength of a password
 *
 * @mainpage p1028 - Password Checker
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
 * @details This program asks for a password, then calculates its size, 
 * character pool, entropy bits per character, entropy, and strength 
 *
 *****************************************************************************/

#include <cctype>
#include <cmath>
#include <iomanip>
#include <iostream>

using namespace std;

const int MAX_PASSWORD_LENGTH = 64;

double calculate_entropy(char password_array[], int password_length);
void display_complexity(double entropy);
int read_password(char password_array[], const int MAX_PASSWORD_LENGTH);

int main()
{
	char continue_yn;

	double entropy;
	int password_length = 0;

	//output the title
	cout << "Password checker with arrays" << endl << endl;

	//create a character array
	char password_array[MAX_PASSWORD_LENGTH];

	//call read_password
	password_length = read_password(password_array, MAX_PASSWORD_LENGTH);

	//call calculate_entropy
	entropy = calculate_entropy(password_array, password_length);

	//call display_complexity
	display_complexity(entropy);

	//repeat process with y/Y, say goodbye if not
	cout << "Continue (y/n)? ";
	cin >> continue_yn;
	while (continue_yn == 'y' || continue_yn == 'Y')
	{
		cout << endl;

		password_length = read_password(password_array, MAX_PASSWORD_LENGTH);
		entropy = calculate_entropy(password_array, password_length);

		display_complexity(entropy);

		cout << "Continue (y/n)? ";
		cin >> continue_yn;
	}

	cout << endl << "Good bye!" << endl;

	return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Calculates the entropy of the password.
 * length * (log2(character pool))
 *
 * @param[in] password_array - holds the password
 * @param[in] password_length - the length of the given password 
 *
 * @returns the password entropy
 *
 *****************************************************************************/
double calculate_entropy(char password_array[], int password_length)
{
	bool password_digit = false;
	bool password_punctuation = false;
	bool password_lowercase = false;
	bool password_uppercase = false;
	bool password_whitespace = false;
	 
	double entropy = 0;
	double entropy_bits = 0;
	int character_pool = 0;
	int i;

	// checking for different cases, digits, whitespace, and punctuation
	for (i = 0; i < password_length; ++i)
	{
		if (islower(password_array[i]))
			password_lowercase = true;

		if (isupper(password_array[i]))
			password_uppercase = true;
		
		if (isdigit(password_array[i]))
			password_digit = true;

		if (isblank(password_array[i]))
			password_whitespace = true;

		if (ispunct(password_array[i]))
			password_punctuation = true;

	}

	// add the number of possible unique characters in the password
	if (password_lowercase)
		character_pool += 26;

	if (password_uppercase)
		character_pool += 26;

	if (password_digit)
		character_pool += 10;

	if (password_whitespace)
		character_pool += 2;

	if (password_punctuation)
		character_pool += 32;

	// caclulate the entropy
	entropy_bits = log2(character_pool);
	entropy = entropy_bits * password_length;

	// print the results
	cout << "Character pool: " << character_pool << endl;

	cout << fixed << showpoint << setprecision(2);
	cout << "Entropy bits per character: " << entropy_bits << endl
		 << "Password entropy value: " << entropy << endl;

    return entropy;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints the strength of a password given its entropy
 *
 * @param[in] entropy - password strength
 *
 *****************************************************************************/
void display_complexity(double entropy)
{
	// print the password strength 
	if (entropy < 45)
		cout << "Password strength: Very weak" << endl << endl;

	else if (entropy < 53)
		cout << "Password strength: Weak" << endl << endl;

	else if (entropy < 73)
		cout << "Password strength: Reasonable" << endl << endl;

	else if (entropy <= 127)
		cout << "Password strength: Strong" << endl << endl;

	else if (entropy > 127)
		cout << "Password strength: Very strong" << endl << endl;

    return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes in a password from the user, prints it, and prints its length.
 *
 * @param[in] password_array - holds the password
 * @param[in] MAX_PASSWORD_LENGTH - the maximum allowed password
 *
 * @returns the length of the password
 *
 *****************************************************************************/
 int read_password(char password_array[], const int MAX_PASSWORD_LENGTH)
{
	 char ch;
	 int password_length = 0;
	 int i = 0;
	 int j = 0;

	 // get the password
	 cout << "Enter password/passphrase: ";
	 cin >> ch;

	 while (ch != '\n' && i < MAX_PASSWORD_LENGTH)
	 {

		 if (ch != '\n')
		 {
			 password_array[i] = ch;
			 i++;
		 }

		 cin.get(ch);
	 }

	 password_length = i;

	 // output the password and its size
	 cout << endl << "Password: ";
	 for (j = 0; j < password_length; j++)
		 cout << password_array[j];
	
	 cout << endl << "Password size: " << password_length << endl; 

	 return password_length;
}