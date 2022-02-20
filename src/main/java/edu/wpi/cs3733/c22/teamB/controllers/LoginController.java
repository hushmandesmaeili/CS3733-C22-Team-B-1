package edu.wpi.cs3733.c22.teamB.controllers;

import edu.wpi.cs3733.c22.teamB.Bapp;
import edu.wpi.cs3733.c22.teamB.Main;
import edu.wpi.cs3733.c22.teamB.entity.DatabaseWrapper;
import edu.wpi.cs3733.c22.teamB.entity.PasswordHashing;
import edu.wpi.cs3733.c22.teamB.entity.objects.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class LoginController {
    String employeeUser;
    String employeePass;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField userField;

    @FXML
    private Label errorMessage;

    @FXML
    public void initialize(){
        Bapp.getPrimaryStage().setResizable(false);
        DatabaseWrapper db = new DatabaseWrapper();
        List<Employee> employeeList = db.getAllEmployee();
        for(Employee employee : employeeList){
            employeeUser = employee.getUsername();
            employeePass = employee.getPassword();
        }

    }

    @FXML
    void loginButton(ActionEvent event) {
        DatabaseWrapper db = new DatabaseWrapper();
        List<Employee> employeeList = db.getAllEmployee();
        String pass = PasswordHashing.hashPassword(passField.getText());
        for (Employee employee : employeeList) {
            employeeUser = employee.getUsername();
            employeePass = employee.getPassword();

            if (passField.getText().isEmpty() || userField.getText().isEmpty()) {
                errorMessage.setText("Enter a username and password"); //(!passField.getText().equals("admin") || !userField.getText().equals("admin")) && (!passField.getText().equals("staff") || !userField.getText().equals("staff")) &&
            } else if ((!pass.equals(employeePass) || !userField.getText().equals(employeeUser))) {
                errorMessage.setText("Incorrect username or password");
            } else {
                String[] args = new String[2];
                args[0] = userField.getText();
                args[1] = passField.getText();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/c22/teamB/views/borderHome.fxml"));
                    Bapp.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
