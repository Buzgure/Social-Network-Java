package com.example.lab4;

import com.example.lab4.Controller.LoginController;
import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.Domain.Validators.FriendshipValidator;
import com.example.lab4.Domain.Validators.UserValidator;
import com.example.lab4.Repository.DataBase.FriendshipDataBaseRepository;
import com.example.lab4.Repository.DataBase.UserDataBaseRepository;
import com.example.lab4.Repository.Repository;
import com.example.lab4.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    Repository<Long, User> userRepo;
    Repository<Long, FriendshipForDB> friendRepo;
    Service service;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Running file");
        String username = "postgres";
        String password = "Tudor20011969";
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";
        userRepo = new UserDataBaseRepository(url, username, password, new UserValidator());
        friendRepo = new FriendshipDataBaseRepository(url, username, password, new FriendshipValidator());

        service = new Service(userRepo, friendRepo);
        loginView(stage);
        stage.show();

    }

    private void loginView(Stage stage) throws IOException {

        URL fxmlLocation = HelloApplication.class.getResource("views/login-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        AnchorPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));
        stage.setTitle("Login Page");

        LoginController loginController = fxmlLoader.getController();
        loginController.setUserService(service);
    }

    public static void main(String[] args) {
        launch();
    }
}