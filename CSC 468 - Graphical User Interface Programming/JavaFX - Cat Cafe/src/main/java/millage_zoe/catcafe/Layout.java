/**
 * Author: Zoe Millage
 * Description: This file contains the main view for the catfe.
 *      Holds the catfe tiles and the edit/details panes
 */

package millage_zoe.catcafe;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Layout implements PropertyChangeListener{
    private CatSimView simView;
    private Button sizeButton3;
    private Button sizeButton5;
    private Button sizeButton9;
    private Controller theController;
    private BorderPane thePane;
    private ToggleGroup tileOptions;
    private Button updateButton;


    public Layout() {
        BorderPane actionCommands = new BorderPane();
        Label cafeInfo = new Label("Resize: ");
        ColumnConstraints cc = new ColumnConstraints();
        HBox optionsBox = new HBox();
        RadioButton rButton = new RadioButton("Table");
        RowConstraints rc = new RowConstraints();
        HBox sizes = new HBox();
        Label tileDetails;
        tileOptions = new ToggleGroup();

        simView = new CatSimView();
        sizeButton3 = new Button("3X3");
        sizeButton5 = new Button("5X5");
        sizeButton9 = new Button("9X9");
        thePane = new BorderPane();
        updateButton = new Button("Next Week");
        theController = new Controller(this);

        // allocate the size buttons
        ObservableList<Node> children = sizes.getChildren();

        children.add(cafeInfo);

        children.add(sizeButton3);
        children.add(sizeButton5);
        children.add(sizeButton9);

        children = optionsBox.getChildren();

        // initialize radio buttons
        rButton.setToggleGroup(tileOptions);
        children.add(rButton);

        rButton = new RadioButton("Cat");
        rButton.setToggleGroup(tileOptions);
        children.add(rButton);

        rButton = new RadioButton("Kitten");
        rButton.setToggleGroup(tileOptions);
        children.add(rButton);

        rButton = new RadioButton("Empty");
        rButton.setToggleGroup(tileOptions);
        children.add(rButton);

        rButton = new RadioButton("View");
        rButton.setToggleGroup(tileOptions);
        rButton.setSelected(true);
        children.add(rButton);


        // radio button layout constraints
        rc.setVgrow(Priority.ALWAYS);
        rc.setFillHeight(true);

        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);

        theController.addObserver(this);

        // layout constraints for the resize area
        optionsBox.setAlignment( Pos.CENTER );
        optionsBox.setMinWidth(300);
        optionsBox.setSpacing(10);

        actionCommands.setCenter(optionsBox);
        actionCommands.setLeft(updateButton);
        sizes.setAlignment(Pos.CENTER);
        actionCommands.setRight(sizes);

        //catfe info
        cafeInfo = new Label(simView.toString());
        cafeInfo.setPadding(new Insets(0, 0, 10, 0));
        BorderPane.setAlignment(cafeInfo, Pos.CENTER);

        // get tile details
        tileDetails = new Label(theController.getTileDetails(0));

        tileDetails.setPadding(new Insets(0, 10, 0, 0));
        BorderPane.setAlignment(tileDetails, Pos.CENTER);

        actionCommands.setPadding(new Insets(10, 0, 0, 0));
        thePane.setPadding(new Insets(10));

        // put everything into the border pane
        thePane.setBottom(actionCommands);
        thePane.setLeft(tileDetails);
        thePane.setTop(cafeInfo);
        thePane.setCenter(simView);
    }



    public Button getNextWeek() {
        return updateButton;
    }



    /**
     * gets the selected radio button option as a string
     * @return the current radio button option as a string
     */
    public String getRadioOption() {
        return ((RadioButton) (tileOptions.getSelectedToggle())).getText();
    }



    public BorderPane getRoot(){
        return this.thePane;
    }



    public Button getSize3() {
        return sizeButton3;
    }



    public Button getSize5() {
        return sizeButton5;
    }



    public Button getSize9() {
        return sizeButton9;
    }



    public void propertyChange(PropertyChangeEvent evt) {
        updateDetails();
        updateTileDetails(theController.getSelectedTile());
    }



    /**
     * forwards the resize call to the sim, and updates the gui
     * @param col the new number of columns
     * @param row the new number of rows
     */
    public void resize( int col, int row) {
        simView.resize(col, row);
        updateDetails();
        updateTileDetails(theController.getSelectedTile());
    }



    public void setViewModel(CafeSim model) {
        simView.setModel(model);
    }



    /**
     * updates the top bar, which holds which week it is, how many
     * filled (non empty) tiles there are, the cafe's current funds, and
     * the number of cats/kittens that have been adopted from the cafe
     */
    public void updateDetails() {
        String data = theController.getCafeInfo();
        Label details = new Label(data);
        details.setPadding(new Insets(0, 0, 10, 0));
        BorderPane.setAlignment(details, Pos.CENTER);

        thePane.setTop(details);
    }



    /**
     * updates the tile detail sidebar, which shows the given tile's name,
     * week when it was changed, age (time since being changed),
     * the total amount the tile has cost over time,
     * the total revenue gained if the tile is a table,
     * and the cat's age and time until adoption if the tile is a cat or kitten
     * @param index the index of the tile whose details should be shown
     */
    public void updateTileDetails(int index) {
        Label tileDetails = new Label(theController.getTileDetails(index));

        tileDetails.setPadding(new Insets(0, 10, 0, 0));
        BorderPane.setAlignment(tileDetails, Pos.CENTER);

        thePane.setLeft(tileDetails);
    }

}
