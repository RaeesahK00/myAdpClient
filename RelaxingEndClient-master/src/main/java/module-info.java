module za.ac.cput.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.validator;
    requires okhttp3;
    requires json;
    requires com.google.gson;


    opens za.ac.cput.demo to javafx.fxml;
    opens za.ac.cput.demo.domain;
    exports za.ac.cput.demo;
}