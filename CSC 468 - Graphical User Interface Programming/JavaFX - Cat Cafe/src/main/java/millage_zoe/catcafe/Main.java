/*
Program: JavaFX - Cat Cafe
Author: Zoe Millage
Class: CSC-468-M01, 2024s
Date Due: 15 February 2024
Description: This file contains the program initializer.
    It initializes the Layout class and creates the stage.

Last tier completed: __2b___


Complete the following checklist. If you partially completed an item, put a note how it can be checked for
what is working for partial credit.


__Hopefully__ Followed the class OOP diagram
__Hopefully__ Observer pattern (ignores tiers)


__Hopefully__ 1.	Tier: Views and tile area
__Hopefully__ a. All objects (ignoring the sim area) (-1 for each missing)
__Hopefully__ b. Have a starting number of tiles in sim area
__Hopefully__ c. Able to add/remove a tile area properly (-33% for each error)
__Hopefully__ d. Info bar listed correctly with all the required elements (-25% for each error)
__Hopefully__ f. Tile Text correct in tile area (-25% per error)
__Hopefully__ g. Radio buttons update properly
__Hopefully__ h. Selecting a rectangle with “view” updates the tile area info (-50% per error)


__Hopefully__ 2a Tier: Advanced functionality
__Hopefully__ a. Next week button has some noticeable effect*
__Hopefully__ b. Tile areas updated properly on “next” (-33% per error)*
__Hopefully__ c. Sim info bar updated properly (-25% per error)
__Hopefully__ d. Selecting a tile after an update shows the new information


__Hopefully__ 2b: Layout
__Hopefully__ a. Location of all items in correct spot (-20% per error)
__Hopefully__ b. Layout still correct on window resize (-20%  for minor error)
__Hopefully__ c. Resize grid at minimum resets the grid and info (-50% if minor error)
__Hopefully__ d. Everything still working that is listed above with resize (-50% if minor error)


The grade you compute is the starting point for course staff, who reserve the right to change the grade
if they disagree with your assessment and to deduct points for other issues they may encounter,
such as errors in the submission process, naming issues, etc.
 */

package millage_zoe.catcafe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 600;

    private Layout layout;


    public static void main(String[] args) {
        launch();
    }



    /**
     * Initializes the stage and layout, and shows the stage
     * @param stage the stage on which the GUI is displayed
     * @throws IOException presumably something that ends up
     *      calling this function can get an I/O fail
     */
    @Override
    public void start(Stage stage) throws IOException {
        layout = new Layout();
        BorderPane root = layout.getRoot();

        stage.setTitle("Cat Cafe Sim Simulator");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

}