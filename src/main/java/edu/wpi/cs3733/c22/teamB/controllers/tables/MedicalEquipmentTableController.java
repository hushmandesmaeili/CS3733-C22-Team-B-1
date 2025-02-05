package edu.wpi.cs3733.c22.teamB.controllers.tables;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamB.Bapp;
import edu.wpi.cs3733.c22.teamB.controllers.AbsPage;
import edu.wpi.cs3733.c22.teamB.controllers.AnchorHomeController;
import edu.wpi.cs3733.c22.teamB.entity.DatabaseWrapper;
import edu.wpi.cs3733.c22.teamB.entity.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edu.wpi.cs3733.c22.teamB.entity.inheritance.IDatabase;
import edu.wpi.cs3733.c22.teamB.entity.objects.Employee;
import edu.wpi.cs3733.c22.teamB.entity.objects.Location;
import edu.wpi.cs3733.c22.teamB.entity.objects.MedicalEquipment;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MedicalEquipmentTableController extends AbsPage {

    @FXML private GridPane gridPane;
    @FXML private JFXButton confirmButton;
    @FXML private TextField equipmentTypeField;
    @FXML private TextField equipmentNameField;
    @FXML private TextField manufacturerField;
    @FXML private JFXComboBox statusField;
    @FXML private TextField colorField;
    @FXML private TextField sizeField;
    @FXML private TextField descriptionField;
    @FXML private JFXButton addButton;
    @FXML private JFXButton deleteButton;
    @FXML private TableView<MedicalEquipment> table;
    @FXML private JFXComboBox<String> LocationChoice;
    @FXML private JFXButton loadButton;
    @FXML private TextField amountField;
    private List<Location> locList;
    private Map<String, Location> locMap;
    @FXML private Pane popup;
    @FXML private Pane contentPane;
    @FXML private AnchorPane anchorPane;
    @FXML private JFXButton filterSubmitButton;
    @FXML private TextField textFilterField;
    @FXML private MenuButton visibilityMenu;

    private Set<String> filterFields = new HashSet<>();


    @Override
    public void namePage() {
        AnchorHomeController.curAnchorHomeController.setPageName("Medical Equipment Table");
    }

    private enum Function {ADD, MODIFY, DELETE, NOTHING};
    MedicalEquipmentTableController.Function func = MedicalEquipmentTableController.Function.NOTHING;

    private boolean initTable = false;
    private IDatabase medicalEquipmentDaoI = new MedicalEquipmentDaoI();
    List<MedicalEquipment> listOfMedicalEquipment;

    DatabaseWrapper db = DatabaseWrapper.getInstance();

    public MedicalEquipmentTableController() {}

    @FXML
    private void initialize() throws NullPointerException {
        deleteButton.setDisable(true);
        popup.setVisible(false);
        popup.setLayoutX(Bapp.getPrimaryStage().getWidth()/3.5);
        popup.setLayoutY(Bapp.getPrimaryStage().getHeight()/3.5);

        locList = db.getAllLocation();
        locMap =
                IntStream.range(0, locList.size())
                        .boxed()
                        .collect(
                                Collectors.toMap(
                                        i -> (locList.get(i).getNodeID() + ' ' + locList.get(i).getLongName()),
                                        i -> locList.get(i)));


        statusField.getItems().addAll(MedicalEquipment.getEquipmentStatus());
        statusField.setValue("WAITING");

        LocationChoice.getItems().addAll(locMap.keySet());
        gridPane.setVisible(false);
        gridPane.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
            }
        });

        loadTable();
        initResize();
        resize();
        namePage();

        filterFields.addAll(List.of(new String[]{"equipmentID", "equipmentName", "equipmentType", "manufacturer", "location", "status", "color", "size", "description", "amount"}));
        textFilterField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                filterSubmit(null);
            }
        });

        for (TableColumn<MedicalEquipment, ?> col : table.getColumns()) {
            CheckMenuItem item = new CheckMenuItem(col.getText());
            item.setSelected(true);
            item.setOnAction(event -> col.setVisible(item.isSelected()));
            visibilityMenu.getItems().add(item);
        }


//        popup.setLayoutX(Bapp.getPrimaryStage().getWidth()/2.5);
//        popup.setLayoutY(Bapp.getPrimaryStage().getHeight()/2.5);
    }

    public void loadTable() throws NullPointerException {
        if (!initTable) {
            initTable = true;


            TableColumn<MedicalEquipment, String> col1 = new TableColumn<>("equipmentID"); // column names
            TableColumn<MedicalEquipment, String> col2 = new TableColumn<>("equipmentName");
            TableColumn<MedicalEquipment, String> col3 = new TableColumn<>("equipmentType");
            TableColumn<MedicalEquipment, String> col4 = new TableColumn<>("manufacturer");
            TableColumn<MedicalEquipment, String> col5 = new TableColumn<>("location");
            TableColumn<MedicalEquipment, String> col6 = new TableColumn<>("status"); // column names
            TableColumn<MedicalEquipment, String> col7 = new TableColumn<>("color");
            TableColumn<MedicalEquipment, String> col8 = new TableColumn<>("size");
            TableColumn<MedicalEquipment, String> col9 = new TableColumn<>("description");
            TableColumn<MedicalEquipment, String> col10 = new TableColumn<>("amount");

            col1.setCellValueFactory(
                    new PropertyValueFactory<>("equipmentID")); // MedicalEquipmentSR fields
            col2.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
            col3.setCellValueFactory(new PropertyValueFactory<>("equipmentType"));
            col4.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
//            col5.setCellValueFactory(new PropertyValueFactory<>("location"));
            col5.setCellValueFactory(cd -> {
                Location loc = cd.getValue().getLocation();
                return Bindings.createStringBinding(() -> loc.getNodeID() + ' ' + loc.getLongName());
            });
            col6.setCellValueFactory(new PropertyValueFactory<>("status"));
            col7.setCellValueFactory(new PropertyValueFactory<>("color"));
            col8.setCellValueFactory(new PropertyValueFactory<>("size"));
            col9.setCellValueFactory(new PropertyValueFactory<>("description"));
            col10.setCellValueFactory(new PropertyValueFactory<>("amount"));

            table.getColumns().add(col1); // adding columns to setup table
            table.getColumns().add(col2);
            table.getColumns().add(col3);
            table.getColumns().add(col4);
            table.getColumns().add(col5);
            table.getColumns().add(col6);
            table.getColumns().add(col7);
            table.getColumns().add(col8);
            table.getColumns().add(col9);
            table.getColumns().add(col10);

            table.setEditable(true);
        }
        table.getItems().clear();
        listOfMedicalEquipment = db.getAllMedicalEquipment();
        table.getItems().addAll(listOfMedicalEquipment); // create and add object
        //table.getItems().addAll(listMedicalEquipment);
        // table.getItems().addAll(new MedicalEquipment("15", "jeff", "jeffery", "jeff himself", new
        // Location("3", 3 , 4, "floor 3", "This building", " idkman", "longname", "shortname"),
        // "Done","blue","large", "funn"));

        // table.getItems().addAll(--list of objects here--); //

    }
    // Go to the home fxml when the home button is pressed
    @FXML
    void goToHome(ActionEvent event) {
        // Try to go home
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/c22/teamB/views/Home.fxml"));
            Bapp.getPrimaryStage().getScene().setRoot(root);
            // Print stack trace if unable to go home
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void addLocation(ActionEvent actionEvent) {
        gridPane.setVisible(true);
        gridPane.setDisable(false);
        equipmentNameField.setVisible(true);
        equipmentTypeField.setVisible(true);
        manufacturerField.setVisible(true);
        statusField.setVisible(true);
        LocationChoice.setVisible(true);
        colorField.setVisible(true);
        sizeField.setVisible(true);
        descriptionField.setVisible(true);
        amountField.setVisible(true);
        equipmentNameField.setDisable(false);
        equipmentTypeField.setDisable(false);
        manufacturerField.setDisable(false);
        statusField.setDisable(false);
        LocationChoice.setDisable(false);
        colorField.setDisable(false);
        sizeField.setDisable(false);
        descriptionField.setDisable(false);
        amountField.setDisable(false);

        func = Function.ADD;
        clearForm(null);
    }

    @FXML
    private void modifyLocation(ActionEvent actionEvent) {
        gridPane.setVisible(true);
        gridPane.setDisable(false);
        equipmentNameField.setVisible(true);
        equipmentTypeField.setVisible(true);
        manufacturerField.setVisible(true);
        statusField.setVisible(true);
        LocationChoice.setVisible(true);
        colorField.setVisible(true);
        sizeField.setVisible(true);
        descriptionField.setVisible(true);
        amountField.setVisible(true);
        equipmentNameField.setDisable(false);
        equipmentTypeField.setDisable(false);
        manufacturerField.setDisable(false);
        statusField.setDisable(false);
        LocationChoice.setDisable(false);
        colorField.setDisable(false);
        sizeField.setDisable(false);
        descriptionField.setDisable(false);
        amountField.setDisable(false);

        MedicalEquipment loc = table.getSelectionModel().getSelectedItem();
        equipmentNameField.setText(loc.getEquipmentName());
        equipmentTypeField.setText(loc.getEquipmentType());
        manufacturerField.setText(loc.getManufacturer());
        statusField.setValue(loc.getStatus());
        LocationChoice.setValue(loc.getLocation().getNodeID() + ' ' + loc.getLocation().getLongName());
        colorField.setText(loc.getColor());
        sizeField.setText(loc.getSize());
        descriptionField.setText(loc.getDescription());
        amountField.setText(String.valueOf(loc.getAmount()));

        func = Function.MODIFY;
        //func = MedicalEquipmentTableController.Function.MODIFY;
    }

    @FXML
    private void deleteLocation(ActionEvent actionEvent) {
        String value = table.getSelectionModel().getSelectedItem().getEquipmentID();
        db.deleteMedicalEquipment(value);
        loadTable();
        cancelForm(null);

        // submitted confirmation popup
        popup.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(1)
        );
        visiblePause.setOnFinished(
                event -> popup.setVisible(false)
        );
        visiblePause.play();
    }

    @FXML private void locationTableClick(MouseEvent mouseEvent) {
        deleteButton.setVisible(true);

        if (table.getSelectionModel().getSelectedItem().getEquipmentID() != null){
            modifyLocation(null);
        }
    }

    @FXML private void confirm(ActionEvent actionEvent) {
        if(func == MedicalEquipmentTableController.Function.ADD) {
            MedicalEquipment m = new MedicalEquipment(
                    equipmentNameField.getText(),
                    equipmentTypeField.getText(),
                    manufacturerField.getText(),
                    locMap.get(LocationChoice.getValue()),
                    statusField.getValue().toString(),
                    colorField.getText(),
                    sizeField.getText(),
                    descriptionField.getText(),
                    Integer.parseInt(amountField.getText()));
            System.out.println(m);
            db.addMedicalEquipment(m);
            loadTable();

            // submitted confirmation popup
            popup.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1)
            );
            visiblePause.setOnFinished(
                    event -> popup.setVisible(false)
            );
            visiblePause.play();

            clearForm(actionEvent);
        } else if (func == MedicalEquipmentTableController.Function.MODIFY) {
            MedicalEquipment newEquip = new MedicalEquipment(
                    table.getSelectionModel().getSelectedItem().getEquipmentID(),
                    equipmentNameField.getText(),
                    equipmentTypeField.getText(),
                    manufacturerField.getText(),
                    locMap.get(LocationChoice.getValue()),
                    statusField.getValue().toString(),
                    colorField.getText(),
                    sizeField.getText(),
                    descriptionField.getText(),
                    Integer.parseInt(amountField.getText()));
            db.updateMedicalEquipment(newEquip);
            loadTable();

            // submitted confirmation popup
            popup.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1)
            );
            visiblePause.setOnFinished(
                    event -> popup.setVisible(false)
            );
            visiblePause.play();

            cancelForm(actionEvent);
        }
    }

    @FXML private void clearForm(ActionEvent actionEvent) {
        equipmentNameField.clear();
        equipmentTypeField.clear();
        manufacturerField.clear();
        statusField.setValue("");
        LocationChoice.setValue("");
        colorField.clear();
        sizeField.clear();
        descriptionField.clear();
        amountField.clear();
    }

    @FXML private void cancelForm(ActionEvent actionEvent) {
        gridPane.setDisable(true);
        gridPane.setVisible(false);
        clearForm(actionEvent);

        addButton.setVisible(true);
        addButton.setDisable(false);

        deleteButton.setVisible(true);
        deleteButton.setDisable(false);

        func = MedicalEquipmentTableController.Function.NOTHING;
    }
    @Override
    public void initResize() {
        contentPane.setLayoutX(Bapp.getPrimaryStage().getWidth()/8);
        contentPane.setLayoutY(Bapp.getPrimaryStage().getHeight()/12);
        anchorPane.setPrefWidth(Bapp.getPrimaryStage().getWidth() - AnchorHomeController.curAnchorHomeController.sidebar.getWidth());
        anchorPane.setPrefHeight(Bapp.getPrimaryStage().getHeight() - AnchorHomeController.curAnchorHomeController.sidebar.getHeight());
    }

    public void filterSubmit(ActionEvent actionEvent) {
        table.getItems().clear();
        table.getItems().removeAll();
        table.getItems().addAll(db.getAllMedicalEquipment().stream().filter(sr -> {
            String input = textFilterField.getText().toLowerCase(Locale.ROOT);
            return  (filterFields.contains("equipmentID") && sr.getEquipmentID().toLowerCase(Locale.ROOT).contains(input)) || //||
                    (filterFields.contains("equipmentName") && sr.getEquipmentName().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("equipmentType") && sr.getEquipmentType().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("manufacturer") && sr.getManufacturer().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("location") && sr.getLocation().getNodeID().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("status") && sr.getStatus().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("color") && sr.getColor().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("size") && sr.getSize().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("description") && sr.getDescription().toLowerCase(Locale.ROOT).contains(input)) ||
                    (filterFields.contains("amount") && String.valueOf(sr.getAmount()).contains(input));
        }).collect(Collectors.toList()));
    }
}