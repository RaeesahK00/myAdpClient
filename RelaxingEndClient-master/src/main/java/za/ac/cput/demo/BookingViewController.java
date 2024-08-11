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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.ac.cput.demo.domain.Booking;
import za.ac.cput.demo.domain.Product;
import za.ac.cput.demo.factory.BookingFactory;
import za.ac.cput.demo.factory.ProductFactory;
import za.ac.cput.demo.typeAdapter.BookingTypeAdapter;
import za.ac.cput.demo.typeAdapter.ProductTypeAdapter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class BookingViewController implements Initializable {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

    @FXML
    private TextField textfldClientId;
    @FXML
    private Label lblClientName;
    @FXML
    private Label lblClientId;
    @FXML
    private DatePicker datePickerCreateBooking;
    @FXML
    private Label lblDateSelected;
    @FXML
    private ComboBox<String> cboSelectBookingTime;

    @FXML
    private Button btnClearBookings;

    @FXML
    private Button btnClearCreateBooking;

    @FXML
    private Button btnGo;

    @FXML
    private Button btnSaveBooking;

    @FXML
    private DatePicker datePickerViewBookingByDate;

    @FXML
    private Label lblCreateBookingHeader;
    @FXML
    private Label lblSelectBookingDate;

    @FXML
    private Label lblSelectBookingTime;

    @FXML
    private Button btnRefreshBookingTable;

    @FXML
    private TableColumn<Booking, LocalDate> tblColBookedForDate;

    @FXML
    private TableColumn<Booking, LocalTime> tblColBookedForTime;

    @FXML
    private TableColumn<Booking, Long> tblColBookingId;

    @FXML
    private TableColumn<Booking, LocalDate> tblColMadeBookingDate;

    @FXML
    private TableColumn<Booking, LocalTime> tblColMadeBookingTime;

    @FXML
    private TableView<Booking> tblViewBookings;
    //------------Product-----------------------------------------------------------------------------------------------------------
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


    private ObservableList<Product> observableProductList = FXCollections.observableArrayList();
    //ObservableList<Booking> getObservableProductListByClient = FXCollections.observableArrayList();

    //-----------------------------------------------------------------------------------------------------------------------
    ObservableList<Booking> observableBookingList = FXCollections.observableArrayList();
    ObservableList<Booking> observableBookingListFilter = FXCollections.observableArrayList();
    ObservableList<Booking> getObservableBookingListByClient = FXCollections.observableArrayList();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @FXML
    protected void displaySelectedDate() {
        //LocalDate pickedDate = datePickerCreateBooking.getValue();
        //lblDateSelected.setText(pickedDate.toString());
        LocalTime pickedTime = (LocalTime.parse(cboSelectBookingTime.getValue()));
        System.out.println(pickedTime);
        lblDateSelected.setText(pickedTime.toString());
    }

    protected void create(LocalDate bookedForDate, LocalTime bookedForTime) {
        try {
            final String URL = "http://localhost:8080/relaxingend/booking/create";
            Booking booking = BookingFactory.buildBooking(0, bookedForDate, bookedForTime);
            Gson g = new GsonBuilder().registerTypeAdapter(Booking.class, new BookingTypeAdapter())
                    .create();
            String jsonString = g.toJson(booking);
            String bookingToPost = post(URL, jsonString);
            if (bookingToPost != null)
                lblDateSelected.setText("Successfully saved!");
            else
                lblDateSelected.setText("Failed to create booking");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewAllBookings() {
        try {
            final String URL = "http://localhost:8080/relaxingend/booking/getall";
            String responseBody = run(URL);
            JSONArray bookings = new JSONArray(responseBody);

            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);
                //String id = booking.getString("id");
                //String bookedForDate = booking.getString("bookedForDate");
                //System.out.println(bookedForDate);

                Gson g = new GsonBuilder().registerTypeAdapter(Booking.class, new BookingTypeAdapter())
                        .create();
                Booking b = g.fromJson(booking.toString(), Booking.class);
                observableBookingList.add(b);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTableCells(String action, Long clientId, LocalDate filterDate) {
        tblColBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        tblColBookedForDate.setCellValueFactory(new PropertyValueFactory<>("bookedForDate"));
        tblColBookedForTime.setCellValueFactory(new PropertyValueFactory<>("bookedForTime"));
        tblColMadeBookingDate.setCellValueFactory(new PropertyValueFactory<>("madeBookingDate"));
        tblColMadeBookingTime.setCellValueFactory(new PropertyValueFactory<>("madeBookingTime"));

        tblViewBookings.getItems().clear();
        if (action.equals("view all bookings")) {
            viewAllBookings();
            tblViewBookings.setItems(observableBookingList);
        } else if (action.equals("view booking by clientId")) {
            viewBookingsByClientId(clientId);
            tblViewBookings.setItems(getObservableBookingListByClient);
        } else if (action.equals("Filter by date")) {
            filterBookingsByDate(filterDate);
            tblViewBookings.setItems(observableBookingListFilter);
        }
    }

    public void viewBookingsByClientId(Long clientId) {
        try {
            final String URL = "http://localhost:8080/relaxingend/clientbooking/readbyclientid/" + String.valueOf(clientId);
            JSONArray bookings = runResponseBody(URL);

            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);

                Gson g = new GsonBuilder().registerTypeAdapter(Booking.class, new BookingTypeAdapter())
                        .create();
                Booking b = g.fromJson(booking.toString(), Booking.class);
                getObservableBookingListByClient.add(b);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getClientName(Long capturedId) {
        try {
            final String URL = "http://localhost:8080/relaxingend/clientbooking/readclientname/" + String.valueOf(capturedId);
            String responseBody = run(URL);
            System.out.println("Run response body pass: " + responseBody);

            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void filterBookingsByDate(LocalDate bookedForDate) {
        viewAllBookings();      //populates observableBookingList
        List<Booking> bookingsToFilter = (List<Booking>) observableBookingList;

        if (bookingsToFilter.isEmpty()) {
            System.out.println("Observable booking list is empty");
        } else {
            System.out.println("Observable booking list is populated");
            System.out.println(bookingsToFilter);
        }

        for (int i = 0; i < bookingsToFilter.size(); i++) {
            System.out.println("Record: " + bookingsToFilter.get(i).getBookedForDate());
            if (bookingsToFilter.get(i).getBookedForDate().equals(bookedForDate)) {
                observableBookingListFilter.add(bookingsToFilter.get(i));
                System.out.println("match: " + bookingsToFilter.get(i));
            }
        }
        System.out.println("Filtered: " + observableBookingListFilter);

    }

    public JSONArray runResponseBody(String URL) throws IOException {
        String responseBody = run(URL);
        System.out.println("Response body: " + responseBody);
        JSONArray jsonArrayOfResponseBody = new JSONArray(responseBody);
        return jsonArrayOfResponseBody;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] timeFrames = {"09:00", "11:00"};
        cboSelectBookingTime.getItems().addAll(timeFrames);

        setTableCells("view all bookings", null, null);
        setProductTableCells("Products");
    }

    public void createBooking(ActionEvent actionEvent) {
        LocalDate bookedForDate = datePickerCreateBooking.getValue();
        LocalTime bookedForTime = (LocalTime.parse(cboSelectBookingTime.getValue()));

        create(bookedForDate, bookedForTime);
        setTableCells("view all bookings", null, null);
        System.out.println("Success!");
    }

    public void refreshBookingTable(ActionEvent actionEvent) {
        setTableCells("view all bookings", null, null);
    }

    public void searchBookingByClient(ActionEvent actionEvent) {
        String clientId = textfldClientId.getText();
        System.out.println(clientId);
        String clientName = getClientName(Long.parseLong(clientId));  //Debug from here
        System.out.println("Client name from search booking by client: " + clientName);
        lblClientName.setText(lblClientName.getText() + clientName);
        lblClientId.setText(lblClientId.getText() + textfldClientId.getText());
        setTableCells("view booking by clientId", Long.parseLong(clientId), null);
    }

    public void resetFields(ActionEvent actionEvent) {
        textfldClientId.setText("");
        lblClientName.setText("Name: ");
        lblClientId.setText("Client ID: ");
        setTableCells("view all bookings", null, null);
    }

    public void searchBookingByDate(ActionEvent actionEvent) {
        LocalDate dateToFilterBy = datePickerViewBookingByDate.getValue();
        System.out.println(dateToFilterBy);
        setTableCells("Filter by date", null, dateToFilterBy);
    }

    //---------------PRODUCT-------------------------------------------------------------------------------------------------
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
          //  System.out.println("Built Product: " + newProduct.getPrice());

            Gson g = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter()).create();
            String jsonString = g.toJson(newProduct);
          //  System.out.println("Serialized JSON: " + jsonString);

            String productResponse = post(URL, jsonString);
            // System.out.println("Server Response: " + productResponse);

            // Parse the response to get the newly created product with ID
            Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductTypeAdapter()).create();
            Product createdProduct = gson.fromJson(productResponse, Product.class);

            if (createdProduct != null) {
                // remember this part it shows the data in the table after create new product
                Platform.runLater(() ->observableProductList.add(createdProduct));

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
       // setTableCells("view all bookings", null, null);
    }

    public void refreshProductTable(ActionEvent actionEvent) {
        setProductTableCells("Products");
    }

    }

