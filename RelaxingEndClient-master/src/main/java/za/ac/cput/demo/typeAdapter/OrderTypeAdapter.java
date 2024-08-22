package za.ac.cput.demo.typeAdapter;

import com.google.gson.*;

import za.ac.cput.demo.domain.Purchase;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderTypeAdapter implements JsonSerializer<Purchase>, JsonDeserializer<Purchase> {


    @Override
    public Purchase deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Long purchaseId = jsonObject.get("purchaseId").getAsLong();
        Float purchaseAmount = jsonObject.get("purchaseAmount").getAsFloat();
        LocalDate purchaseDate = LocalDate.parse(jsonObject.get("purchaseDate").getAsString());
        LocalTime purchaseTime = LocalTime.parse(jsonObject.get("purchaseTime").getAsString());

        Purchase purchase = new Purchase.Builder()
                .setPurchaseId(purchaseId)
                .setPurchaseAmount(purchaseAmount)
                .setPurchaseDate(purchaseDate)
                .setPurchaseTime(purchaseTime)
                .build();
        return purchase;

    }

    @Override
    public JsonElement serialize(Purchase purchase, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("purchaseId", purchase.getPurchaseId());
        jsonObject.addProperty("purchaseAmount", String.valueOf(purchase.getPurchaseAmount()));
        jsonObject.addProperty("purchaseDate", String.valueOf(purchase.getPurchaseDate()));
        jsonObject.addProperty("purchaseTime", String.valueOf(purchase.getPurchaseTime()));

        return jsonObject;
    }


}
