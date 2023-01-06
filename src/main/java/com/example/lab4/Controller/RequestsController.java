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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RequestsController implements Observer<ChangeEvent> {
    @FXML
    public Button cancelButton;
    Service service;

    private ObservableList<User> modelRequest = FXCollections.observableArrayList();
    private ObservableList<FriendshipForDB> modelRequestFriend = FXCollections.observableArrayList();

    private User currentUser;

    private UserController controller;
    @FXML
    public Button buttonAccept;
    @FXML
    public TableView<User> tableRequestsView;
    @FXML
    public TableColumn<User, String> tableColumnLastName;
    @FXML
    public TableColumn<User, String> tableColumnFirstName;
    @FXML
    public TableColumn<User, LocalDateTime> tableColumnDate;

    public void handleAcceptFriend(ActionEvent actionEvent) {
        User user = tableRequestsView.getSelectionModel().getSelectedItem();
        service.updateFriendship(user.getId(), currentUser.getId(), true);

        controller.update(null);
        update(null);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Friend added successfully!");
    }

    @Override
    public void update(ChangeEvent friendshipForDBChangeEvent) {

    }

    public void setController(UserController controller){
        this.controller = controller;
    }

    @FXML
    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    public void setUserService(Service service){
        this.service = service;
        service.addObserver(this);
        initModel();
        initialize();
    }

    public void initialize(){
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tableRequestsView.setItems(modelRequest);
    }

    private void initModel() {
        modelRequest.setAll();
        modelRequest.addAll(service.getRequestsOfUserList(currentUser));

    }

    public void handleBackToMain(ActionEvent actionEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/users-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            UserController userController = fxmlLoader.getController();
            userController.setCurrentUser(currentUser);
            userController.setUserService(service);
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
