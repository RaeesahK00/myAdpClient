package za.ac.cput.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class HelloController {
    @FXML
    private Label welcomeText;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}