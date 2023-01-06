package com.example.lab4.Controller;

import com.example.lab4.Domain.User;
import com.example.lab4.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignUpController {
    Service service;
    @FXML
    private Text textResponse;
    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passField;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    @FXML
    public void handleRegisterButton(ActionEvent actionEvent) {
        String pass = passField.getText();
        if(String.valueOf(passField.getText()) == null){
            textResponse.setText("Invalid Password!");
        }
        else{
            User u = new User(lastNameField.getText(), firstNameField.getText(), userNameField.getText(), passField.getText());

            if(service.addUser(u).isPresent()){
                service.addUser(u);
                textResponse.setText("Registered successfully!");
            }
        }
    }

    public void setUserService(Service service){
        this.service = service;
    }
}
