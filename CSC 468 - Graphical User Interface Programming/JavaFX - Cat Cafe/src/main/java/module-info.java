module com.example.catcafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens millage_zoe.catcafe to javafx.fxml;
    exports millage_zoe.catcafe;
}