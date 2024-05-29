package org.iss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.iss.Controllers.LoginController;
import org.iss.Domain.*;
import org.iss.Repository.*;
import org.iss.Services.BugService;
import org.iss.Services.EmployeeService;
import org.iss.Services.ServiceContainer;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();

        FXMLLoader loaderSecondWindow = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        Parent rootSecondWindow = loaderSecondWindow.load();
        LoginController loginControllerSecondWindow = loaderSecondWindow.getController();

        IBugRepository bugRepository = new BugRepository();
        IEmployeeRepository employeeRepository = new EmployeeRepository();
        Validator<Employee> employeeValidator = new EmployeeValidator();
        Validator<Bug> bugValidator = new BugValidator();
        EmployeeService employeeService = new EmployeeService(employeeRepository,employeeValidator);
        BugService bugService = new BugService(bugRepository,bugValidator);
        Thread configureHibernateThread = new Thread(HibernateUtils::getSessionFactory);
        configureHibernateThread.start();
        ServiceContainer.setBugService(bugService);
        ServiceContainer.setEmployeeService(employeeService);

        loginController.setServices(bugService,employeeService);
        loginControllerSecondWindow.setServices(bugService, employeeService);

        Scene myScene = new Scene(root);
        primaryStage.setScene(myScene);
        primaryStage.setTitle("Bug Spotter");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> HibernateUtils.closeSessionFactory());

        Stage secondStage = new Stage();
        Scene secondScene = new Scene(rootSecondWindow);
        secondStage.setScene(secondScene);
        secondStage.setTitle("Bug Spotter");
        secondStage.setResizable(false);
        secondStage.show();

    }

}