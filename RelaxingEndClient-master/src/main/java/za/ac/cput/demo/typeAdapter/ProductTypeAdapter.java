package za.ac.cput.demo.typeAdapter;

import com.google.gson.*;
import za.ac.cput.demo.domain.Product;

import java.lang.reflect.Type;

public class ProductTypeAdapter implements JsonSerializer<Product>, JsonDeserializer<Product> {

    @Override
    public Product deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        // Deserialize fields
        Long productId = jsonObject.get("productId").getAsLong(); // Or handle exclusion
        String productName = jsonObject.get("productName").getAsString();
        String productDescription = jsonObject.get("productDescription").getAsString();
        Float price = jsonObject.get("price").getAsFloat();
        Integer quantity = jsonObject.get("quantity").getAsInt();

        // Create Booking object
       Product product =new Product.Builder().setProductId(productId)
               .setProductName(productName)
               .setProductDescription(productDescription)
               .setPrice(price)
               .setQuantity(quantity)
               .build();
        // Set other fields
        //booking.setOtherField(jsonObject.get("otherField").getAsString());

        return product;
    }

    @Override
    public JsonElement serialize(Product product, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        // Serialize fields excluding bookingId if necessary
        jsonObject.addProperty("productId", product.getProductId()); // Or exclude if it's private

        // Add other fields as needed
        jsonObject.addProperty("price", String.valueOf(product.getPrice()));
        jsonObject.addProperty("productDescription", String.valueOf(product.getProductDescription()));
        jsonObject.addProperty("productName", String.valueOf(product.getProductName()));
        jsonObject.addProperty("quantity", String.valueOf(product.getQuantity()));

        return jsonObject;
    }
}
