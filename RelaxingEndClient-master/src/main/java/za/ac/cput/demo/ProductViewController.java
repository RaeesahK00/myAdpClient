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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.ac.cput.demo.domain.Product;
import za.ac.cput.demo.factory.ProductFactory;
import za.ac.cput.demo.typeAdapter.ProductTypeAdapter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class ProductViewController implements Initializable {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient product = new OkHttpClient();
    //public InputHandling inputHandler = new InputHandling();

    @FXML
    private TableColumn<Product, Long> columnPrdId;

    @FXML
    private TableColumn<Product, Float> columnPrdPrice;

    @FXML
    private TableColumn<Product, String> columnPrdDescription;

    @FXML
    private TableColumn<Product, String> columnPrdName;

    @FXML
    private TableColumn<Product, Integer> columnPrdQuantity;

    @FXML
    private TableView<Product> tblViewProduct;

    @FXML
    private TextField textPrName;

    @FXML
    private TextField textPrDescription;

    @FXML
    private TextField textPrPrice;

    @FXML
    private TextField textPrQuantity;

    @FXML
    private Button btnCreateProduct;

    @FXML
    private Button btnReset;

    @FXML
    private Label labelSuccess;

    @FXML
    private TextField searchIdField;

    @FXML
    private Button searchButton;

    @FXML
    private Button btnRefresh;

//    @FXML
//    private Button btnOpenProduct;
//
//    @FXML
//    private Button btnOpenBooking;

    private ObservableList<Product> observableProductList = FXCollections.observableArrayList();
    //ObservableList<Booking> getObservableProductListByClient = FXCollections.observableArrayList();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = product.newCall(request).execute()) {
            return response.body().string();
        }
    }

    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = product.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProductTableCells("Products");
    }
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




//-----------------------------------------------------------------------------------------



    public void viewAllProducts() {
        try {
            final String URL = "http://localhost:8080/relaxingend/product/getAll";
            String responseBody = run(URL);
            JSONArray products = new JSONArray(responseBody);
            System.out.println("Response Body: " + responseBody);

            //  observableProductList.clear();
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);


                Gson g = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter())
                        .create();
                Product b = g.fromJson(product.toString(), Product.class);
                observableProductList.add(b);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void setProductTableCells(String action) {
        columnPrdId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        columnPrdPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnPrdDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        columnPrdName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnPrdQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tblViewProduct.getItems().clear();
        if (action.equals("Products")) {
            viewAllProducts();
            tblViewProduct.setItems(observableProductList);
        }

        //handles the float price
        columnPrdPrice.setCellFactory(column -> new TableCell<Product, Float>() {
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

    protected void createProduct(String productName, String productDescription, Float price, Integer quantity) {
        try {
            final String URL = "http://localhost:8080/relaxingend/product/create";
            Product newProduct = ProductFactory.buildProduct(null, productName, productDescription, price, quantity);

            Gson g = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter()).create();
            String jsonString = g.toJson(newProduct);

            String productResponse = post(URL, jsonString);

            Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter()).create();
            Product createdProduct = gson.fromJson(productResponse, Product.class);

            if (createdProduct != null) {

                Platform.runLater(() -> observableProductList.add(createdProduct));

                labelSuccess.setText("Product successfully saved!");
                btnReset.setText("Add another product");
            } else {
                labelSuccess.setText("Failed to add product");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void newProduct(ActionEvent actionEvent) {
        String productName = textPrName.getText();
        String productDescription = textPrDescription.getText();
        Float price;
        Integer quantity;

        if (productName.isEmpty() || productDescription.isEmpty()) {
            showAlert("Validation Error", "Product name and description cannot be empty.");
            textPrName.setText("");
            textPrDescription.setText("");
            textPrPrice.setText("");
            textPrQuantity.setText("");
            labelSuccess.setText("");
            return;
        }

        try {
            quantity = Integer.parseInt(textPrQuantity.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "A valid quantity must be entered, numbers only.");
            textPrName.setText("");
            textPrDescription.setText("");
            textPrPrice.setText("");
            textPrQuantity.setText("");
            labelSuccess.setText("");
            return;
        }

        try {
            price = Float.parseFloat(textPrPrice.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "A valid price must be entered, numbers only.");
            textPrName.setText("");
            textPrDescription.setText("");
            textPrPrice.setText("");
            textPrQuantity.setText("");
            labelSuccess.setText("");
            return;
        }

        System.out.println("in newProduct the print");
        createProduct(productName, productDescription, price, quantity);
        setProductTableCells("Products");
        System.out.println("Success!");


    }

    public void searchProductById() {
        String idText = searchIdField.getText();
        if (idText.isEmpty()) {
            showAlert("Validation Error", "Product ID cannot be empty.");
            return;
        }

        try {
            long productId = Long.parseLong(idText);
            Product product = getProductById(productId);
            if (product != null) {
                observableProductList.clear();
                observableProductList.add(product);
                tblViewProduct.setItems(observableProductList);
                searchIdField.setText("");
            } else {
                showAlert("Search Error", "Product with ID " + productId + " not found.");
                searchIdField.setText("");
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Product ID must be a valid number.");
            searchIdField.setText("");
        }
    }

    private Product getProductById(long productId) {
        try {
            final String URL = "http://localhost:8080/relaxingend/product/read/" + productId;
            String responseBody = run(URL);
            Gson g = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter()).create();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return g.fromJson(jsonObject, Product.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

//must remove this when I join program to the main
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void clearProductFields(ActionEvent actionEvent) {
        textPrName.setText("");
        textPrDescription.setText("");
        textPrPrice.setText("");
        textPrQuantity.setText("");
        labelSuccess.setText("");

    }

    public void refreshProductTable(ActionEvent actionEvent) {
        setProductTableCells("Products");
    }


}






