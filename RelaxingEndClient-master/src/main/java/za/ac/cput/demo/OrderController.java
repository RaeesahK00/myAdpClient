package za.ac.cput.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.ac.cput.demo.domain.Product;
import za.ac.cput.demo.domain.Purchase;
import za.ac.cput.demo.factory.PurchaseFactory;
import za.ac.cput.demo.typeAdapter.OrderTypeAdapter;
import za.ac.cput.demo.typeAdapter.ProductTypeAdapter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TableColumn<Purchase, Long> columnPurchaseId;

    @FXML
    private TableColumn<Purchase, Float> columnPurchaseAmount;

    @FXML
    private TableColumn<Purchase, LocalDate> columnPurchaseDate;

    @FXML
    private TableColumn<Purchase, LocalTime> columnPurchaseTime;

    @FXML
    private TableView<Purchase> tblViewPurchase;

    @FXML
    private TextField searchIdField;

    @FXML
    private Button searchButton;

    @FXML
    private Button btnRefresh;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblOrderIdName;

    @FXML
    private Label labelSuccess;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnAddItem;


    @FXML
    private ComboBox<String> comboBoxPurchaseItems;

    @FXML
    private TextField textPurchaseAmount;

    @FXML
    private TextArea textPurchaseItems;


    //-----------------------------------------------------------------------------------------

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient purchase = new OkHttpClient();
   // public InputHandling inputHandler = new InputHandling();

    private ObservableList<Purchase> observablePurchaseList = FXCollections.observableArrayList();
    //ObservableList<Booking> getObservableProductListByClient = FXCollections.observableArrayList();



    //-----------------------------------------------------------------------------------------


    FXMLLoader fxmlLoader = new FXMLLoader();
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToBooking(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/BookingView.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProduct(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/ProductView.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOrder(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/OrderView.fxml"));
        Parent root = fxmlLoader.load();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




//-------------- POST - RUN - INITIALIZE ---------------------------------------------------------------------------


    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = purchase.newCall(request).execute()) {
            return response.body().string();
        }
    }

    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = purchase.newCall(request).execute()) {
            return response.body().string();
        }
    }
    private List<String> productNames;

    public void initialize(URL url, ResourceBundle resourceBundle) {
//        List<String> productNames = productService.getProductNames();
//
//        // Populate the ComboBox
//        comboBoxPurchaseItems.getItems().addAll(productNames);
        String [] productNames  = {"Feet cream", "Face Mask"};
        comboBoxPurchaseItems.getItems().addAll(productNames);

//        getProductNames(); // Replace with your method that retrieves names
//        comboBoxPurchaseItems.getItems().addAll(productNames);


        setPurchaseTableCells("Purchase");
    }



//-------METHODS--------------------------------------------------------------------------------

    public void viewAllPurchases() {
        try {
            final String URL = "http://localhost:8080/relaxingend/purchase/getAll";
            String responseBody = run(URL);
            JSONArray purchase = new JSONArray(responseBody);
            System.out.println("Response Body: " + responseBody);


            //  observableProductList.clear();
            for (int i = 0; i < purchase.length(); i++) {
                JSONObject purchases = purchase.getJSONObject(i);


                Gson g = new GsonBuilder().registerTypeAdapter(Purchase.class, new OrderTypeAdapter())
                        .create();
                Purchase b = g.fromJson(purchases.toString(), Purchase.class);
                observablePurchaseList.add(b);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setPurchaseTableCells(String action) {
        columnPurchaseId.setCellValueFactory(new PropertyValueFactory<>("purchaseId"));
        columnPurchaseAmount.setCellValueFactory(new PropertyValueFactory<>("purchaseAmount"));
        columnPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        columnPurchaseTime.setCellValueFactory(new PropertyValueFactory<>("purchaseTime"));


        tblViewPurchase.getItems().clear();
        if (action.equals("Purchase")) {
            viewAllPurchases();
            tblViewPurchase.setItems(observablePurchaseList);
        }

        //handles the float price
        columnPurchaseAmount.setCellFactory(column -> new TableCell<Purchase, Float>() {
            private final NumberFormat format = NumberFormat.getCurrencyInstance();

            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });


    }

//----------------------SEARCH-------------------------------------------------------------------

    private Purchase getPurchaseById(long purchaseId) {
        try {
            final String URL = "http://localhost:8080/relaxingend/purchase/read/" + purchaseId;
            String responseBody = run(URL);
            Gson g = new GsonBuilder().registerTypeAdapter(Purchase.class, new OrderTypeAdapter()).create();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return g.fromJson(jsonObject, Purchase.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void searchPurchaseById() {
        String idText = searchIdField.getText();
        lblOrderId.setText(lblOrderId.getText() + searchIdField.getText());
        if (idText.isEmpty()) {
            showAlert("Validation Error", "Product ID cannot be empty.");
            return;
        }

        try {
            long purchaseId = Long.parseLong(idText);
            Purchase purchase = getPurchaseById(purchaseId);
            if (purchase != null) {
                observablePurchaseList.clear();
                observablePurchaseList.add(purchase);
                tblViewPurchase.setItems(observablePurchaseList);
                searchIdField.setText("");
            } else {
                showAlert("Search Error", "Order with ID " + purchaseId + " not found.");
                searchIdField.setText("");
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Order ID must be a valid number.");
            searchIdField.setText("");
        }
    }




 //---------------CREATE ORDER,PURCHASE-----------------------------------------------------------------------------


    public void fillComboBox(ActionEvent actionEvent){
        String comboBox = (String) comboBoxPurchaseItems.getValue();
        textPurchaseItems.setText(comboBox + "\n");
    }

    public void productComboBox(ActionEvent actionEvent){
        String comboBox = (String) comboBoxPurchaseItems.getValue();
        textPurchaseItems.setText(comboBox + "\n");
    }

    public void getProductNames() {
        try {
            final String URL = "http://localhost:8080/relaxingend/purchase/getProductNames/" ;
            String responseBody = run(URL);
            Gson g = new GsonBuilder().registerTypeAdapter(Purchase.class, new OrderTypeAdapter()).create();
            Purchase p = g.fromJson(responseBody, Purchase.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }


//    private List<String> getProductNames() {
//        // Implement your logic to fetch product names from your data source
//        // (e.g., database, service call)
//        List<String> names = new ArrayList<>(); // Replace with your actual data retrieval
//        names.add("Product 1");
//        names.add("Product 2");
//        return names;
//    }


    public void newPurchase(ActionEvent actionEvent) {
        String comboBox = (String) comboBoxPurchaseItems.getValue(); // Gets the selected item from the ComboBox
        Float purchaseAmount;


        String purchaseItems = textPurchaseItems.getText();

        if (purchaseItems == null || purchaseItems.isEmpty()) {
            showAlert("Validation Error", "Purchase items must be selected from the dropdown.");
            textPurchaseAmount.setText("");
            labelSuccess.setText("");
            return;
        }


        try {
            purchaseAmount = Float.parseFloat(textPurchaseAmount.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "A valid amount must be entered, numbers only.");
            textPurchaseAmount.setText("");
            labelSuccess.setText("");
            return;
        }

        // Use the current date and time
        LocalDate purchaseDate = LocalDate.now();
        LocalTime purchaseTime = LocalTime.now();

        // Proceed with creating the purchase
        System.out.println("in newPurchase the print");
        createPurchase(purchaseItems, purchaseAmount, purchaseDate, purchaseTime);
        setPurchaseTableCells("Purchase"); // or however you need to refresh the display
        System.out.println("Success!");
    }









    protected void createPurchase( String purchaseItems, Float purchaseAmount,LocalDate purchaseDate, LocalTime purchaseTime){
        try {
            final String URL = "http://localhost:8080/relaxingend/purchase/create";

            Purchase newPurchase = PurchaseFactory.buildPurchase(null, purchaseItems, purchaseAmount,  LocalDate.now(), LocalTime.now());


            Gson g = new GsonBuilder().registerTypeAdapter(Purchase.class, new OrderTypeAdapter()).create();
            String jsonString = g.toJson(newPurchase);


            String purchaseResponse = post(URL, jsonString);


            Gson gson = new GsonBuilder().registerTypeAdapter(Purchase.class, new OrderTypeAdapter()).create();
            Purchase createdPurchase = gson.fromJson(purchaseResponse, Purchase.class);

            if (createdPurchase != null) {

                Platform.runLater(() -> observablePurchaseList.add(createdPurchase));

                labelSuccess.setText("Order successfully saved!");
                btnReset.setText("Place another order");
            } else {
                labelSuccess.setText("Failed to add purchase");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }










    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void clearPurchaseFields(ActionEvent actionEvent) {
        textPurchaseAmount.setText("");
        labelSuccess.setText("");

    }

    public void refreshPurchaseTable(ActionEvent actionEvent) {
        lblOrderIdName.setVisible(false);
        lblOrderId.setVisible(false);
        setPurchaseTableCells("Purchase");
    }




}
