package org.iss.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.iss.Domain.Bug;
import org.iss.Domain.DomainException;
import org.iss.Domain.Employee;
import org.iss.Services.BugService;
import org.iss.Services.EmployeeWatcher;
import org.iss.Services.ServiceContainer;
import org.iss.Services.Utils.Observer;

import java.io.IOException;

public class SpotterController implements Observer {
    private BugService bugService;
    private Employee loggedEmployee;
    @FXML
    private Button logoutButton;
    @FXML
    private ListView<Bug> bugList;
    @FXML
    private TextArea bugDescriptionField, bugDescriptionFieldAdd;
    @FXML
    private TextField bugNameField, bugNameAddField;

    ObservableList<Bug> modelBugs = FXCollections.observableArrayList();


    public void setLoggedEmployee(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }
    private void loadBugs() {
        modelBugs.setAll(bugService.getAllBugs());
    }

    public void setBugService(BugService bugService) {
        this.bugService = bugService;
        bugService.addObserver(this);
        loadBugs();
    }

    public void initialize() {
        bugList.setItems(modelBugs);
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

    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Parent root = loader.load();
            LoginController loginViewController = loader.getController();
            loginViewController.setServices(bugService, ServiceContainer.getEmployeeService());
            Stage window = (Stage) logoutButton.getScene().getWindow();
            EmployeeWatcher.logout(loggedEmployee);
            window.setTitle("Bug Spotter");
            window.setScene(new Scene(root));
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void addBug() {
        String name = bugNameAddField.getText();
        String description = bugDescriptionFieldAdd.getText();
        try {
            bugService.addBug(name, description);
        }
        catch (DomainException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Insertion error");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void updateBug() {
        Bug bug = bugList.getSelectionModel().getSelectedItem();
        if(bug==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Selection problem");
            alert.setContentText("Please select a bug");
            alert.showAndWait();
            return;
        }
        String name = bugNameField.getText();
        String description = bugDescriptionField.getText();
        try {
            bugService.updateBug(bug, name, description);
        }
        catch (DomainException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Update problem");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
            return;
        }
        bugList.getSelectionModel().clearSelection();
    }

    @Override
    public void update() {
        loadBugs();
    }
}
