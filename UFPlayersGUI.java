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

public class UFPlayersGUI extends Application {
    private players playerData = new players();
    private TableView<createPlayer> playerTable = new TableView<>();
    private ObservableList<createPlayer> tableData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("UF Players Status Tracker");

        // Create table
        TableColumn<createPlayer, String> nameColumn = new TableColumn<>("Player Name");

        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlayerName()));
        TableColumn<createPlayer, Integer> threeColumn = new TableColumn<>("3 Pointers");
        threeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getThreePointers()));
        TableColumn<createPlayer, Integer> twoColumn = new TableColumn<>("2 Pointers");
        twoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTwoPointers()));
        TableColumn<createPlayer, Integer> layupsColumn = new TableColumn<>("Layups");
        layupsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getLayups()));
        TableColumn<createPlayer, Integer> assistsColumn = new TableColumn<>("Assists");
        assistsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAssists()));
        TableColumn<createPlayer, Integer> pointsColumn = new TableColumn<>("Total Points");
        pointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalPoints()));

        playerTable.getColumns().addAll(nameColumn, threeColumn, twoColumn, layupsColumn, assistsColumn, pointsColumn);
        refreshTable();

        // Buttons
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

        // Separates buttons
        VBox layout = new VBox(15);

        layout.getChildren().addAll(
                title,
                playerTable,
                buttons
        );

        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 800, 450);

        primaryStage.setTitle("UF Players Status Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshTable() {
        tableData.setAll(playerData.getPlayerList());
        playerTable.setItems(tableData);
    }

    // Actions for the table
    private void addPlayer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Player");
        dialog.setHeaderText("Enter Player Information");
        dialog.setContentText("Player Name:");
        dialog.showAndWait().ifPresent(name -> {

            try {
                int three = Integer.parseInt(
                        askInput("3-Pointers:"));
                int two = Integer.parseInt(
                        askInput("2-Pointers:"));
                int layups = Integer.parseInt(
                        askInput("Layups:"));
                int assists = Integer.parseInt(
                        askInput("Assists:"));
                for (createPlayer p : playerData.getPlayerList()) {
                    if (p.getPlayerName().equalsIgnoreCase(name.trim())) {
                        showMessage("Player already exists.");
                        return;
                    }
                }
                int points = (three * 3) + (two * 2) + layups;

                createPlayer newPlayer = new createPlayer(
                        name.trim(), three, two, layups, assists, points);
                playerData.getPlayerList().add(newPlayer);
                refreshTable();
            } catch (Exception ex) {
                showMessage("Invalid player information.");
            }
        });
    }

    private void removePlayer() {
        createPlayer selected = playerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a player first.");
            return;
        }
        playerData.getPlayerList().remove(selected);
        refreshTable();
    }

    private void updatePlayer() {
    createPlayer selected = playerTable.getSelectionModel().getSelectedItem();

    if (selected == null) {
        showMessage("Please select a player first.");
        return;
    }

    // Dropdown
    ChoiceDialog<String> dialog = new ChoiceDialog<>("Click to select a stat", "3-Pointers", "2-Pointers", "Layups", "Assists");

    dialog.setTitle("Update Player Stats");
    dialog.setHeaderText("Which stat would you like to change?");
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

        int index = playerData.getPlayerList().indexOf(selected);
        playerData.getPlayerList().set(index, updatedPlayer);
        refreshTable();
        showMessage("Player stats updated successfully!");

    } catch (NumberFormatException ex) {
        showMessage("Please enter a valid number.");
    }
}

    private void uploadFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Player TXT File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            boolean success =
                    playerData.uploadPlayersFromFile(file.getAbsolutePath());
            if (success) {
                refreshTable();
                showMessage("Players uploaded successfully!");
            } else {
                showMessage("Unable to upload file.");
            }
        }
    }

    // Helpers
    private String askInput(String message) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Player Information");
        dialog.setHeaderText(message);
        dialog.setContentText(null);
        return dialog.showAndWait().orElseThrow(() -> new RuntimeException("Input cancelled."));
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("UF Players Status Tracker");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }
    
    // Main to launch
    public static void main(String[] args) {
        launch(args);
    }
}
