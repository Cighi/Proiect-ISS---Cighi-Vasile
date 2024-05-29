package org.iss.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.iss.Domain.Employee;
import org.iss.Domain.Role;
import org.iss.Repository.IBugRepository;
import org.iss.Services.BugService;
import org.iss.Services.EmployeeService;
import org.iss.Services.EmployeeWatcher;

import java.io.IOException;

public class LoginController {
    private BugService bugService;
    private EmployeeService employeeService;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private TextArea bugDescriptionField;
    @FXML
    private TextField bugNameField;

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });
    }

    public void setServices(BugService bugService, EmployeeService employeeService) {
        this.bugService = bugService;
        this.employeeService = employeeService;
    }

    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Employee employee = employeeService.findEmployeeByCredentials(username, password);
        if (employee == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Invalid Credentials");
            alert.showAndWait();
        } else {
            if(!EmployeeWatcher.loggedIn(employee))
            {
                EmployeeWatcher.login(employee);
                loadWindowForEmployee(employee);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Authentication failure");
                alert.setContentText("User logged in");
                alert.showAndWait();
            }
        }
    }

    private void loadWindowForEmployee(Employee employee) {
        String roleString = employee.getRole().toString().toLowerCase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + roleString + "-view.fxml"));
        try {
            Parent root = loader.load();
            if (employee.getRole() == Role.Admin) {
                AdminController adminController = loader.getController();
                adminController.setServices(bugService,employeeService);
                adminController.setLoggedEmployee(employee);
            }
            else if(employee.getRole() == Role.Fixer){
                FixerController fixerController = loader.getController();
                fixerController.setBugService(bugService);
                fixerController.setLoggedEmployee(employee);
            }
            else if(employee.getRole()==Role.Spotter){
                SpotterController spotterController = loader.getController();
                spotterController.setBugService(bugService);
                spotterController.setLoggedEmployee(employee);
            }
            Stage window = (Stage) usernameField.getScene().getWindow();
            window.setTitle(employee.getUsername() + " " + employee.getRole());
            window.setScene(new Scene(root));
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
