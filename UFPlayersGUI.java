/*
    This creates a table for the UF Player's status using JavaFX. 
    It allows users to add, remove, and update player stats, as well as upload data from a text file.
    The table displays each player's data and stores it in an SQLite database.

    @author Courtney Ingersoll

*/
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.lang.classfile.Label;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

public class UFPlayersGUI extends Application {

    private players playerData = new players();
    private TableView<createPlayer> playerTable = new TableView<>();
    private ObservableList<createPlayer> tableData =
            FXCollections.observableArrayList();

    /*
        This method starts the javaFX app. It will create the columns for the SQLite database table.
        @param primaryStage.
    */
    @Override
    public void start(Stage primaryStage) {

        Label title = new Label("UF Players Status Tracker");

        // Create columns
        TableColumn<createPlayer, String> nameColumn =
                new TableColumn<>("Player Name");
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPlayerName()));
        TableColumn<createPlayer, Integer> threeColumn =
                new TableColumn<>("3 Pointers");
        threeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getThreePointers()));
        TableColumn<createPlayer, Integer> twoColumn =
                new TableColumn<>("2 Pointers");
        twoColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getTwoPointers()));
        TableColumn<createPlayer, Integer> layupsColumn =
                new TableColumn<>("Layups");
        layupsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getLayups()));
        TableColumn<createPlayer, Integer> assistsColumn =
                new TableColumn<>("Assists");
        assistsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getAssists()));
        TableColumn<createPlayer, Integer> pointsColumn =
                new TableColumn<>("Total Points");
        pointsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getTotalPoints()));

        playerTable.getColumns().setAll(nameColumn, threeColumn, twoColumn, layupsColumn, assistsColumn, pointsColumn);

        refreshTable();

        // Create buttons
        Button addButton = new Button("Add Player");
        Button removeButton = new Button("Remove Player");
        Button updateButton = new Button("Update Stats");
        Button uploadButton = new Button("Upload TXT");
        Button exitButton = new Button("Exit");

        addButton.setOnAction(e -> addPlayer());
        removeButton.setOnAction(e -> removePlayer());
        updateButton.setOnAction(e -> updatePlayer());
        uploadButton.setOnAction(e -> uploadFile());
        exitButton.setOnAction(e -> primaryStage.close());

        HBox buttons = new HBox(10);

        buttons.getChildren().addAll(addButton, removeButton, updateButton, uploadButton, exitButton);

        // Main 
        VBox layout = new VBox(15);
        layout.getChildren().addAll(title, playerTable, buttons);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 800, 450);
        primaryStage.setTitle("UF Players Status Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
        This will refresh the table with the updated data.
    */
    private void refreshTable() {
        tableData.setAll(playerData.getPlayerList());
        playerTable.setItems(tableData);
    }

    /*
        This will add a new player to the table.
        @param add the player.
        @throws a number format exception if the user enters invalid numbers.
        @throws a runtime exception if the user cancels. 
    */
    private void addPlayer() {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Add Player");
        dialog.setHeaderText("Enter Player Information");
        dialog.setContentText("Player Name:");

        dialog.showAndWait().ifPresent(name -> {

            if (name.trim().isEmpty()) {
                showMessage("Player name cannot be empty.");
                return;
            }

            try {
                int three = Integer.parseInt(askInput("3-Pointers:"));
                int two = Integer.parseInt(askInput("2-Pointers:"));
                int layups = Integer.parseInt(askInput("Layups:"));
                int assists = Integer.parseInt(askInput("Assists:"));

                if (three < 0 || two < 0 || layups < 0 || assists < 0) {
                    showMessage("Stats cannot be negative.");
                    return;
                }

                int points = (three * 3) + (two * 2) + layups;

                createPlayer newPlayer = new createPlayer(name.trim(), three, two, layups, assists, points);

                if (playerData.addPlayer(newPlayer)) {
                    refreshTable();
                    showMessage("Player added successfully!");
                } else {
                    showMessage("Player already exists.");
                }

            } catch (NumberFormatException ex) {
                showMessage("Please enter valid numbers.");
            } catch (RuntimeException ex) {
                showMessage("Player entry cancelled.");
            }
        });
    }

    /*
        This will remove a selected player from the table.
        @param remove the player.
        @throws a message if the player is unabled to be removed.
    */
    private void removePlayer() {
        createPlayer selected = playerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a player first.");
            return;
        }

        if (playerData.removePlayer(selected.getPlayerName())) {
            refreshTable();
            showMessage("Player removed successfully!");
        } else {
            showMessage("Unable to remove player.");
        }
    }

    /*
        This will update a selected player's stats.
        @param update the player.
        @throws a number format exception if the user enters invalid numbers.
        @throws an error is the player is unable to be updated.
    */
    private void updatePlayer() {
        createPlayer selected = playerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a player first.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("3-Pointers", "3-Pointers", "2-Pointers", "Layups", "Assists");

        dialog.setTitle("Update Player Stats");
        dialog.setHeaderText("Which stat would you like to edit?");
        dialog.setContentText("Choose a stat:");

        String status = dialog.showAndWait().orElse(null);

        if (status == null) {
            return;
        }

        try {
            int three = selected.getThreePointers();
            int two = selected.getTwoPointers();
            int layups = selected.getLayups();
            int assists = selected.getAssists();

            int newValue = Integer.parseInt(
                    askInput("Enter new value for " + status + ":")
            );

            if (newValue < 0) {
                showMessage("Stats cannot be negative.");
                return;
            }

            switch (status) {
                case "3-Pointers":
                    three = newValue;
                    break;
                case "2-Pointers":
                    two = newValue;
                    break;
                case "Layups":
                    layups = newValue;
                    break;
                case "Assists":
                    assists = newValue;
                    break;
            }

            int points = (three * 3) + (two * 2) + layups;

            createPlayer updatedPlayer = new createPlayer(selected.getPlayerName(), three, two, layups, assists, points);

            if (playerData.updatePlayer(updatedPlayer)) {
                refreshTable();
                showMessage("Player stats updated successfully!");
            } else {
                showMessage("Unable to update player.");
            }

        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid number.");
        } catch (RuntimeException ex) {
            showMessage("Update cancelled.");
        }
    }
    /*
        This will upload players from a text file.
        @param upload the players.
        @throws an error if the file cannot be found.
        @throws a number format exception if the user enters invalid numbers.
    */
    private void uploadFile() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select Player TXT File");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {

            boolean success = playerData.uploadPlayersFromFile(file.getAbsolutePath());

            if (success) {
                refreshTable();
                showMessage("Players uploaded successfully!");
            } else {
                showMessage("Unable to upload file.");
            }
        }
    }

    /*
        This will ask the user for input.
        @param ask the input.
    */
    private String askInput(String message) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Player Information");
        dialog.setHeaderText(message);
        dialog.setContentText(null);

        return dialog.showAndWait().orElseThrow(() -> new RuntimeException("Input cancelled."));
    }

    /*
        This will display a message to the user.
        @param message to display.
    */
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("UF Players Status Tracker");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }

    /*
        This will launch the javafx application.
        @param runs the application.
    */
    public static void main(String[] args) {
        launch(args);
    }
}
