package com.example.lab4.Controller;

import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.HelloApplication;
import com.example.lab4.Service.Service;
import com.example.lab4.Service.UserService;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class LoginController implements Observer<ChangeEvent> {

    Service service;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signUpButton;
    @FXML
    private Text handleTestResponse;
    @FXML
    private TextField enterUserName;
    @FXML
    private PasswordField enterPassword;


    @FXML
    public void handleLoginButton(ActionEvent actionEvent){

        User u = service.findUserByUserName(enterUserName.getText(), enterPassword.getText());
        if(u!=null){
            handleTestResponse.setText("You have been logged in!");
            try{
                URL fxmlLocation = HelloApplication.class.getResource("views/users-view.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Menu");
                UserController userController = fxmlLoader.getController();
                userController.setCurrentUser(u);
                userController.setUserService(service);
                stage.show();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();



            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            handleTestResponse.setText("Login failed!");
    }



    public void setUserService(Service service){
        this.service = service;
        service.addObserver(this);
    }
    @Override
    public void update(ChangeEvent friendshipForDBChangeEvent) {

    }
    @FXML
    public void handleSignUp(MouseEvent mouseEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/signup-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader((fxmlLocation));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            SignUpController signUpController = fxmlLoader.getController();
            signUpController.setUserService(service);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
