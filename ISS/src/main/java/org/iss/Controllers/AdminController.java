package org.iss.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.iss.Domain.Bug;
import org.iss.Domain.Employee;
import org.iss.Domain.DomainException;
import org.iss.Domain.Role;
import org.iss.Services.BugService;
import org.iss.Services.EmployeeService;
import org.iss.Services.EmployeeWatcher;
import org.iss.Services.Utils.Observer;

import java.io.IOException;

public class AdminController implements Observer {
    ObservableList<Employee> modelEmployees = FXCollections.observableArrayList();
    ObservableList<Bug> modelBugs = FXCollections.observableArrayList();
    private EmployeeService employeeService;
    private BugService bugService;
    @FXML
    private ListView<Bug> bugList;
    @FXML
    private TextArea bugDescriptionField;
    private Employee loggedEmployee;
    @FXML
    private Button logoutButton;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> tableColumnEmployeeName, tableColumnEmployeeUsername;
    @FXML
    private TableColumn<Employee, Role> tableColumnEmployeeRole;
    @FXML
    private TextField nameField, usernameField, nameFieldAdd, usernameFieldAdd, passwordFieldAdd, bugNameField;

    @FXML
    private ComboBox<Role> roleBox, roleBoxAdd;

    public void initialize() {
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bugList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bugList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                bugNameField.setText(obs.getValue().getName());
                bugDescriptionField.setText(obs.getValue().getDescription());
            } else {
                bugNameField.clear();
                bugDescriptionField.clear();
            }
        });
        bugDescriptionField.setWrapText(true);
        tableColumnEmployeeUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnEmployeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmployeeRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        employeeTable.setItems(modelEmployees);
        bugList.setItems(modelBugs);
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(obs.getValue().getName());
                usernameField.setText(obs.getValue().getUsername());
                roleBox.setValue(obs.getValue().getRole());
            } else {
                nameField.clear();
                usernameField.clear();
                roleBox.setValue(null);
            }
        });
        bugList.setCellFactory(lv -> {
            TextFieldListCell<Bug> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<Bug>() {
                @Override
                public String toString(Bug object) {
                    return object.getName();
                }

                @Override
                public Bug fromString(String string) {
                    return null;
                }
            });
            return cell;
        });
    }

    public void setLoggedEmployee(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }

    public void setServices(BugService bugService, EmployeeService employeeService) {
        this.bugService = bugService;
        this.employeeService = employeeService;
        bugService.addObserver(this);
        employeeService.addObserver(this);
        loadData();
        loadRoles();
        loadBugs();
    }

    private void loadBugs() {
        modelBugs.setAll(bugService.getAllBugs());
    }

    private void loadRoles() {
        for (Role role : Role.values()) {
            roleBox.getItems().add(role);
            roleBoxAdd.getItems().add(role);
        }

    }

    private void loadData() {
        modelEmployees.setAll(employeeService.getAllEmployees());
    }

    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Parent root = loader.load();
            LoginController loginViewController = loader.getController();
            loginViewController.setServices(bugService, employeeService);
            Stage window = (Stage) logoutButton.getScene().getWindow();
            EmployeeWatcher.logout(loggedEmployee);
            window.setTitle("Bug Spotter");
            window.setScene(new Scene(root));
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void deleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Selection problem");
            alert.setContentText("Please select an employee");
            alert.showAndWait();
            return;
        }
        Boolean deleted = employeeService.deleteEmployee(selectedEmployee);
    }

    public void updateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Selection problem");
            alert.setContentText("Please select an employee");
            alert.showAndWait();
            return;
        }
        String newUsername = usernameField.getText();
        String newName = nameField.getText();
        Role newRole = roleBox.getValue();
        try {
            employeeService.updateEmployee(selectedEmployee, newName, newUsername, newRole);
        }
        catch (DomainException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Update problem");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }
    }

    public void addEmployee() {
        String newUsername = usernameFieldAdd.getText();
        String newName = nameFieldAdd.getText();
        String newPassword = passwordFieldAdd.getText();
        Role newRole = roleBoxAdd.getValue();

        try {
            employeeService.addEmployee(newName, newUsername, newPassword, newRole);
        } catch (DomainException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Insertion error");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }

    }

    @Override
    public void update() {
        loadData();
        loadBugs();
    }
}
