package com.example.tabelview;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.*;
import java.time.LocalDateTime;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class HelloController {

    static final String DB_URL = "jdbc:mysql://localhost:3306/sakila";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "admin";
    static final String PASS = "admin";
    static Connection conn = null;
    static ResultSet rs;
    @FXML TableView tableview;
    @FXML Button btnActor;
    @FXML Button btnFilm;
    @FXML
    private Label welcomeText;

    private static ObservableList<ObservableList<String>> data;

    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList();
        // Open a connection
        try {
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM actor");
            rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML void onActorButtonClick(ActionEvent actionEvent) {
        String ActorQuery = "SELECT * FROM actor";
        creatTableViewStructure(ActorQuery);
        buildData(ActorQuery);
    }

    @FXML public void onFilmButtonClick(ActionEvent actionEvent) {
        String FilmQuery = "SELECT * FROM film";
        creatTableViewStructure(FilmQuery);
        buildData(FilmQuery);
    }
    @FXML public void creatTableViewStructure(String SQL) {
        tableview.getColumns().clear();
        try {
            //ResultSet
            rs = conn.createStatement().executeQuery(SQL);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        if (param.getValue().get(j)==null){ return new SimpleStringProperty("");}
                        else { return new SimpleStringProperty(param.getValue().get(j).toString());}
                    }
                });
                tableview.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }// end creatTableViewStructure

    @FXML public void buildData(String SQL) {
        data.clear();
        try {
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
            }
            tableview.setItems(data);
            tableview.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }// end buildData
}