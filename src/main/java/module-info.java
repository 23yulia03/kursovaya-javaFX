module com.example.gamefx2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamefx2 to javafx.fxml;
    exports com.example.gamefx2;
}