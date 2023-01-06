module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.lab4 to javafx.fxml;
    exports com.example.lab4;
    exports com.example.lab4.Controller;
    exports com.example.lab4.Domain;
    exports com.example.lab4.Repository;
    exports com.example.lab4.Service;
    exports com.example.lab4.utils.Events;
    exports com.example.lab4.utils.Observer;
    opens  com.example.lab4.Controller to javafx.fxml;
}