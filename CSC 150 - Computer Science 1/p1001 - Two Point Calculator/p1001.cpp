/** ***************************************************************************
 * @file
 * 
 * @brief Calculates values based on two point coordinates
 *
 * @mainpage p1001 - Two Point Calculator
 *
 * @section course_section Course Information
 *
 * @authors Zoe Millage
 *
 * @date Fall 2020
 *
 * @par Course:
 *         CSC 150
 *
 * @section program_section Program Information
 *
 * @details Enter the coordinates for two points, and this program will 
 * calculate and output
 *  - the length of the line
 *  - the coordinates of the line's midpoint
 *  - the area of a square with the line as its diagonal
 *  - the area of a circle with the line as its diameter
 *
 * the program will then ask for the length and width of an average sheep, and 
 * calculate and output the area of those sheep and how many of them will fit 
 * in the square and circle mentioned above.
 *
 *****************************************************************************/

#define _USE_MATH_DEFINES
#include <cmath>
#include <iomanip>
#include <iostream>

using namespace std;

int main()
{
	double xa;					//x coordinate for point A
	double ya;					//y coordinate for point A
	double xb;					//x coordinate for point B
	double yb;					//y coordinate for point B
	double ab;					//contains the length of the line AB
	double ab_midpt_x;			//contains the x value of AB's midpoint
	double ab_midpt_y;			//contains the y value of AB's midpoint
	double ab_square_area;		/*contains the area of a square
								with a diagonal of line AB*/
	double ab_circle_area;		/*contains the area of a circle
								with a diameter of line AB*/
	double avg_sheep_height;	//contains the average height of a sheep
	double avg_sheep_width;		//contains the average width of a sheep
	double avg_sheep_area;		//contains the area of an average sheep
	int	   sheep_in_square;		/*contains the number of sheep
								that fit into the AB square*/
	int    sheep_in_circle;		/*contains the number of sheep
								that fit into the AB circle*/

	cout << fixed << showpoint << left << setprecision(2);

	//prompt and get inputs for xa, ya, xb, and yb values (real numbers)
	cout << "Enter point A x value: ";
	cin >> xa;
	cout << "Enter point A y value: ";
	cin >> ya;
	cout << "Enter point B x value: ";
	cin >> xb;
	cout << "Enter point B y value: ";
	cin >> yb;

	//calculates the distance between points A and B
	ab = sqrt((pow((xa - xb), 2)) + (pow((ya - yb), 2)));
	cout << "The distance between A and B is " << ab << "." << endl;

	//calculates the midpoint of line AB
	ab_midpt_x = (xa + xb) / 2;
	ab_midpt_y = (ya + yb) / 2;
	cout << "The midpoint between A and B is ";
	cout << "(" << ab_midpt_x << ", ";
	cout << ab_midpt_y << ")." << endl;

	//calculates the area of a square with a diagonal of AB
	ab_square_area = (pow(ab, 2)) / 2;
	cout << "The area of the square with AB as diagonal is ";
	cout << ab_square_area << "." << endl;

	//calculates the area of a circle with a diameter of AB
	ab_circle_area = M_PI * pow((ab / 2), 2);
	cout << "The area of the circle with AB as diameter is ";
	cout << ab_circle_area << "." << endl;

	//prompt and get inputs for avg_sheep_length and avg_sheep_width
	cout << "Enter length of average sheep: ";
	cin >> avg_sheep_height;
	cout << "Enter width of average sheep: ";
	cin >> avg_sheep_width;

	//calculate the area of a average sheep
	avg_sheep_area = M_PI * (avg_sheep_width / 2) * (avg_sheep_height / 2);
	cout << "Each sheep is approximately ";
	cout << avg_sheep_area << " units in area." << endl;

	/*calculate the number of sheep that will fit into the AB square
	(calculates using doubles, then returns an int output)*/
	sheep_in_square = static_cast<int> (ab_square_area / avg_sheep_area);
	cout << "The square can fit approximately ";
	cout << sheep_in_square << " sheep " << endl;

	/*calculate the number of sheep that will fit into the AB circle
	(calculates using doubles, then returns an int output)*/
	sheep_in_circle = static_cast<int> (ab_circle_area / avg_sheep_area);
	cout << "and the circle can fit approximately ";
	cout << sheep_in_circle << " sheep." << endl;

	return 0;
}