package com.example.lab4.Controller;

import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.HelloApplication;
import com.example.lab4.Service.Service;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

public class AccountViewController implements Observer<ChangeEvent> {

    @FXML
    public Text lastNameText;
    @FXML
    public Text firstNameText;
    @FXML
    public TableView<User> tableFriendsView;
    Service service;
    private ObservableList<User> modelFriends = FXCollections.observableArrayList();
    @FXML
    public Button deleteFriendButton;
    @FXML
    public Button logOutButton;
    @FXML
    public TableColumn<User, String> tableViewColumnFirstName;
    @FXML
    public TableColumn<User, String> tableViewColumnLastName;

    private User currentUser;

    private Node previousStage;

    private UserController userController;

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setPreviousStage(Node previousStage) {
        this.previousStage = previousStage;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        lastNameText.setText(currentUser.getLastName());
        firstNameText.setText(currentUser.getFirstName());
        initModel();
        initialize();

    }

    private void initModel() {
        modelFriends.setAll();
        modelFriends.addAll(service.getFriendsOfUserList(currentUser));


}

    public void handleDeleteFriend(ActionEvent actionEvent) {
        User user = tableFriendsView.getSelectionModel().getSelectedItem();
//        FriendshipForDB friendship = new FriendshipForDB(user.getId(), currentUser.getId(), LocalDateTime.now());
        FriendshipForDB friendship = service.findFriendshipByUsers(user.getId(), currentUser.getId());
        if(friendship == null)
            friendship = service.findFriendshipByUsers(currentUser.getId(), user.getId());
        service.deleteFriendship(friendship.getId());
        userController.update(null);
        update(null);
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        URL fxmlLocation = HelloApplication.class.getResource("views/login-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login Page");
        LoginController loginController = fxmlLoader.getController();
        loginController.setUserService(service);
        stage.show();
        previousStage.getScene().getWindow().hide();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        initModel();
        initialize();

    }

    private void initialize() {
        tableViewColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableViewColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableFriendsView.setItems(modelFriends);
    }
}
