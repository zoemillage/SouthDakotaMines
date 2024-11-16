/** ***************************************************************************
 * @file
 *
 * @brief Creates and analyzes a 2D population grid
 *
 * @mainpage p1009 - 2D Population Grid Analysis
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
 * @details This program asks for the dimensions of the population grid along 
 * with values to propogate it with, prints this grid, and calculates center 
 * of the most populous area, along with the population of this area
 *
 *****************************************************************************/

#include <iomanip>
#include <iostream>

using namespace std;

int find_metro_pop(int populations[][20], int rows, int cols, int curr_row, int curr_col);
void get_max_pop_and_location(int populations[][20], int rows, int cols,
    int &max_row_location, int &max_col_location, int &total_pop);
void get_populations(int populations[][20], int rows, int cols);
void print_grid(int populations[][20], int rows, int cols);
void print_results(int row, int col, int total_pop);



int main()
{
	int populations[20][20];


    //Number of rows and columns to fill
    int rows, cols;

    //Maximum population value and its central location
    int max_r;
    int max_c;
    int max_pop;

    //ask user for size of grid
    cout << "How many rows in population grid? ";
    cin >> rows;
    cout << "How many columns in population grid? ";
    cin >> cols;


	//add function calls to perform each task.
    //fill grid
	get_populations(populations, rows, cols);

    //print grid to make sure it is read in properly
    print_grid(populations, rows, cols);

    //find highest metro area population and its center location
	get_max_pop_and_location(populations, rows, cols, max_r, max_c, max_pop);

    //print results
	print_results(max_r,max_c, max_pop);

    return 0;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Finds the total population for a population center centered around a given
 * point in the population grid.
 *
 * @param[in] populations - holds the population in each area 
 * @param[in] rows - the rows in the grid
 * @param[in] cols - the columns in the grid 
 * @param[in] curr_row - the row value of the population center
 * @param[in] curr_col - the column value of the population center
 *
 * @returns the population center's total population
 *
 *****************************************************************************/
int find_metro_pop(int populations[][20], int rows, int cols, int curr_row, int curr_col)
{
	int r; //loop variable
	int c; //loop variable
	int pop = 0; //current population

	int r_start = 0;
	int r_end = rows - 1;
	int c_start = 0;
	int c_end = cols - 1;

	//cases for if curr_row or curr_col is on the edge of an array
	if (curr_row != 0)
		r_start = curr_row - 1;

	if (curr_row != rows - 1)
		r_end = curr_row + 1;

	if (curr_col != 0)
		c_start = curr_col - 1;

	if (curr_col != cols - 1)
		c_end = curr_col + 1;

	//add population of population[r][c] and it's adjacent values
	for (r = r_start; r <= r_end; ++r)
	{
		for (c = c_start; c <= c_end; ++c)
		{
			pop += populations[r][c];
		}
	}

	return pop;
}



/** ***************************************************************************
 * @author K Corwin
 *
 * @par Description:
 * This function takes in the entire population array (up to 20x20),
 * 	and	the number of rows and columns that are filled in the array.
 *
 * This function calls the find_metro_pop function on each filled element,
 * to find the metro population total for that element plus each adjacent 
 * element. 
 * 
 * This function runs the find max algorithm on the metro population values. 
 * When the max is found, the central location of that maximum is also saved.
 * 
 * The function provides back the maximum metro population plus its row and 
 * column location.
 *
 * @param[in] populations - 2D array containing populations
 * @param[in] rows - the number of filled rows in the populations array.
 * @param[in] cols - the number of filled columns in the populations array.
 * @param[out] max_row_location - the row index of the center of the maximum
 * 								metro population
 * @param[out] max_col_location - the column index of the center of the maximum
 * 								metro population
 * @param[out] max_so_far - when the function is completed, this will contain
 * 							the maximum metro population value
 *
 *****************************************************************************/
void get_max_pop_and_location(int populations[][20], int rows, int cols,
    int &max_row_location, int &max_col_location, int &max_so_far)
{ 
    //metro_pop is a sum, so must initialize to zero.
    int metro_pop = 0;
    
    //Initialize max value to the first population value
    max_so_far = populations[0][0];
    
    //Initialize max location to the first element
    max_row_location = 0;
    max_col_location = 0;

    //Find the location of the largest metro area
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
			metro_pop = find_metro_pop(populations, rows, cols, i, j);
            
            //Max algorithm, but also
            // save the location it's found in
            if (metro_pop > max_so_far)
            {
                max_so_far = metro_pop;
                max_row_location = i;
                max_col_location = j;
            }
        }
    }
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes in population values from the user until the population grid is
 * fully filled.
 *
 * @param[in, out] populations - holds the population in each area 
 * @param[in] rows - the rows in the grid
 * @param[in] cols - the columns in the grid 
 *
 *****************************************************************************/
void get_populations(int populations[][20], int rows, int cols)
{
	int r;  //loop variable for rows
	int c;  //loop variable for columns

	//get the values for each element of the array
	cout << "Enter population values: ";

	for (r = 0; r < rows; ++r)
	{
		for (c = 0; c < cols; ++c)
		{
			cin >> populations[r][c];
		}
	}

	return;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Prints a population grid with its values.
 *
 * @param[in] populations - the population in each area
 * @param[in] rows - the rows in the grid
 * @param[in] cols - the columns in the grid
 *
 *****************************************************************************/
void print_grid(int populations[][20], int rows, int cols)
{
	int r;  //loop variable for rows
	int c;  //loop variable for columns

	//endl to match formatting
	cout << endl;

	for (r = 0; r < rows; ++r)
	{
		for (c = 0; c < cols; ++c)
		{
			//ouput values with up to 7 spaces between each number
			cout << setw(8) << setfill(' ') << populations[r][c];
		}

		cout << endl;
	}

	return;
}



/** ***************************************************************************
 * @author K Corwin
 *
 * @par Description:
 * This function prints the highest metro population and its location 
 * (row and column indexes for the center element of the highest metro
 * population) on the screen in an informative message.
 *
 * @param[in] row - the row index of the center of the maximum
 * 								metro population
 * @param[in] col - the column index of the center of the maximum
 * 								metro population
 * @param[in] total_pop - the maximum metro population value
 *
 *****************************************************************************/
void print_results(int row, int col, int total_pop)
{
    cout << "The most populous metro area is centered at ("
        << row << ", " << col << ")" << endl
        << "with a population of "
        << total_pop << endl;
}