/*
Program: Java OOP
Author: Zoe Millage; some functions, partial functions, and classes by Dr. Lisa Rebenitsch
Class: CSC-461-M01, 2023f
Date Due: 28 September 2023
Description: Lets the user customize a small city by placing tiles,
             changing tile color, rezoning, and updating road layouts.
             Utilizes the visitor pattern for many city customizations
             and information gathering.
             This file contains the CityStart class, which initializes the
             city, visitors for the city, and a main menu for the user to
             alter the city

Grading tags in for all lines marked with *		_Hopefully_

        The visitor pattern is used 				_Visitors are used in the applicable completed tiers_
        Handles bad input with 1 try-catch			_Hopefully completed_
        Threw the exception in tier 8 (rezone)		_Hopefully completed_

        Tier 1: running and menu working 			_Passed_
        Tier 2: set any object at 0, 0 				_Passed_
        Tier 3: set any object a anywhere			_Passed_
        Tier 4: handles bad input at this point		_Passed_
        Tier 5: default grid displays properly 		_Passed_
        Tier 6: count types * 					    _Passed_
        Tier 7: coloring and menus completed*		_Passed_
        Tier 8: Rezone *					        _Passed_
        Tier 9: Fix roads*			  		        _Passed_
        All adjacent pullable objects removed		_Hopefully completed_
        At least one pullable objects are pulled inwards  _Hopefully completed_


Last tier completed: ___Tier 9___

*/


package millage_zoe;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CityStart {
    public static Scanner cin;
    public static void main(String[] args) {
        ArrayList<Integer> rezoneRows = new ArrayList<Integer>();
        ArrayList<Integer> rezoneCols = new ArrayList<Integer>();

        City c = new City();

        cin = new Scanner(System.in);

        ColorText.Color color;

        int col;
        int input = -1;
        int numEmpty;
        int row;

        // almost all the menu strings
        String menu =
                "1) Set Tile\n"+
                "2) Make Default City\n"+
                "3) Count Zones\n"+
                "4) Set Tile Color\n"+
                "5) Rezone\n"+
                "6) Fix Roads\n"+
                "0) Quit\n";
        String menu1p1 = "\nInput tile type 1) greenspace 2) water 3) road 4) building 5) empty:> ";
        String menu1p2 = "Input location (x y): ";
        String menu4p1 = "Input tile type 1) building 2) road 3) non-structure:> ";
        String menu4p2 = "Input color 1) red 2) orange 3) blue 4) green 5) black:> ";
        String menuErr1 = "Please input an integer";
        String menuErr2 = "Number is out of range";
        String menuErr3 = "Insufficient open areas";

        // make the visitors
        ChangeColor colorVisitor = new ChangeColor();
        Rezone rezoneVisitor = new Rezone();
        FixRoads fixVisitor = new FixRoads();
        GetCounts countVisitor = new GetCounts();


        while(input != 0) {
            // print the main menu
            System.out.println(c);
            System.out.println(menu);
            System.out.print("Choice:> ");


            try{
                // get the menu choice
                input = cin.nextInt();

                // test choice validity
                MenuExceptions.mainOOB(input);


                // menu option 1: change a tile
                if ( input == 1){
                    // get the new tile type and check
                    System.out.print(menu1p1);
                    input = cin.nextInt();
                    MenuExceptions.subOOB1to5(input);

                    // get the tile location and check
                    System.out.print(menu1p2);
                    row = cin.nextInt();
                    MenuExceptions.subOOBBounded(row, c.rows);
                    col = cin.nextInt();
                    MenuExceptions.subOOBBounded(col, c.cols);

                    // change the tile
                    c.changeTile(input, row, col);
                }


                // menu option 2: set a default grid
                else if ( input == 2){
                    c.defaultCity();
                }


                // menu option 3; count tiles
                else if ( input == 3){
                    // make sure the visitor starts at 0 for each count
                    countVisitor.reset();

                    // visit all the tiles
                    // GRADING: COUNT.
                    c.accept(countVisitor);

                    // print the results
                    numEmpty = countVisitor.getNumEmpty();

                    System.out.println("Empty: " + numEmpty + "\nBuildings: "
                            + countVisitor.getNumBuildings() + "\nGreenspaces: "
                            + countVisitor.getNumGreenspace() + "\nRoads: "
                            + countVisitor.getNumStreet() + "\nWater: "
                            + countVisitor.getNumWater());
                }


                // menu option 4: change color
                else if ( input == 4){
                    // pick which tile type
                    System.out.print(menu4p1);
                    input = cin.nextInt();
                    MenuExceptions.subOOB1to3(input);
                    colorVisitor.setGroup(input);

                    // pick the new color
                    System.out.print(menu4p2);
                    input = cin.nextInt();
                    MenuExceptions.subOOB1to5(input);
                    color = setColor( input );
                    colorVisitor.setColor(color);

                    // change the color of the tiles
                    // GRADING: COLOR.
                    c.accept(colorVisitor);
                }

                // menu option 5: rezone
                else if ( input == 5){
                    // check the number of each tile
                    countVisitor.reset();
                    c.accept(countVisitor);
                    numEmpty = countVisitor.getNumEmpty();

                    // error >= 5 empty tiles
                    MenuExceptions.testRezone( numEmpty );

                    // get the locations of each empty tile
                    // GRADING: REZONE.
                    c.accept(rezoneVisitor);
                    rezoneRows = rezoneVisitor.getRList();
                    rezoneCols = rezoneVisitor.getCList();

                    // convert empty tiles to greenspace
                    for ( int i = 0; i < rezoneRows.size(); i++){
                        c.changeTile(1, rezoneRows.get(i), rezoneCols.get(i));
                    }

                    // clear the rezone list
                    rezoneVisitor.clearLists();
                }

                // menu option 6: fix roads
                else{
                    c.accept(fixVisitor);
                }
            }
            catch ( InputMismatchException e ){
                System.out.println(menuErr1);

                // Based on the documentation in IntelliJ,
                // InputMismatchException seems to save an error string or a
                // reference to it, which keeps causing the exception
                // if it isn't cleared.
                cin.next();
            }
            catch (MenuExceptions.OutOfBounds e){
                System.out.println(menuErr2);
            }
            catch (MenuExceptions.CannotRezone e){
                System.out.println(menuErr3);
            }
        }
    }



    private static ColorText.Color setColor(int choice)
    {
        if ( choice == 1 )
            return ColorText.Color.RED;

        else if ( choice == 2 )
            return ColorText.Color.ORANGE;

        else if ( choice == 3 )
            return ColorText.Color.BLUE;

        else if ( choice == 4 )
            return ColorText.Color.GREEN;

        else
            return ColorText.Color.BLACK;
    }


}
