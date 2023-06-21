module com.example.tabelview {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.tabelview to javafx.fxml;
    exports com.example.tabelview;
}