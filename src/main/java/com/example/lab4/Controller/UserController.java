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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserController implements Observer<ChangeEvent> {
    @FXML
    public Button sendRequestButton;
    @FXML
    public TextField searchBar;
    @FXML
    public Button seeRequestsButton;
    @FXML
    public Text myAccountText;
    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, String> tableColumnLastName;
    @FXML
    public TableColumn<User, String> tableColumnFirstName;

    private User currentUser;

    Service service;

    private ObservableList<User> modelUser = FXCollections.observableArrayList();

    private ObservableList<User> modelFriends = FXCollections.observableArrayList();


    @FXML
    public void setCurrentUser(User u) {
        currentUser = u;
    }

    public void setUserService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableView.setItems(modelUser);
    }

    private void initModel() {
        modelFriends.setAll();

        modelFriends.addAll(service.getFriendsOfUserList(currentUser));

        modelUser.setAll();
        modelUser.addAll(service.getUnacceptedRequestsOfUser(currentUser));
    }
    @Override
    public void update(ChangeEvent friendshipForDBChangeEvent) {
        tableView.setItems(null);
        initModel();
        initialize();

    }

    public void handleSendRequest(ActionEvent actionEvent){
        User user = tableView.getSelectionModel().getSelectedItem();
        if(Objects.equals(currentUser.getId(), user.getId())){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "You can't send a friend request to yourself!");
        }
        else{
            boolean status = service.sendFriendRequest(currentUser.getId(), user.getId());
            if(status)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Requst sent successfully!");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "Already friend!");
        }
    }

    public void handleSearchUsers(KeyEvent keyEvent){
        modelUser.setAll(service.getUnacceptedRequestsOfUser(currentUser));
        tableView.setItems(modelUser);
        String nameVal = String.valueOf(searchBar.getCharacters());
        if(!nameVal.equals("")){
            List<User> unaccepted = service.getUnacceptedRequestsOfUser(currentUser).stream().filter(x -> (x.getLastName()+x.getFirstName()).contains(nameVal)).toList();
            modelUser.setAll(unaccepted);
            tableView.setItems(modelUser);
        }
        sendRequestButton.setDisable(true);
    }


    public void handleOpenSeeRequests(ActionEvent actionEvent){
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/requests-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Received Requests Page");
            RequestsController requestsController= fxmlLoader.getController();
            requestsController.setCurrentUser(currentUser);
            requestsController.setUserService(service);
            requestsController.setController(this);
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleOpenAccount(javafx.scene.input.MouseEvent mouseEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/account-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("My Account");
            AccountViewController accountViewController = fxmlLoader.getController();
            accountViewController.setCurrentUser(currentUser);
            accountViewController.setService(service);
            accountViewController.setUserController(this);
            accountViewController.setPreviousStage((Node)mouseEvent.getSource());
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void handleActivateButtonAdd(javafx.scene.input.MouseEvent mouseEvent) {
        sendRequestButton.setDisable(false);
    }

    public void handleClickSearch(javafx.scene.input.MouseEvent mouseEvent) {
        searchBar.setText("");
    }
}


