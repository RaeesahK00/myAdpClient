package za.ac.cput.demo.typeAdapter;

import com.google.gson.*;
import za.ac.cput.demo.domain.Booking;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingTypeAdapter implements JsonSerializer<Booking>, JsonDeserializer<Booking> {

    @Override
    public JsonElement serialize(Booking src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Serialize fields excluding bookingId if necessary
        jsonObject.addProperty("bookingId", src.getBookingId()); // Or exclude if it's private

        // Add other fields as needed
        jsonObject.addProperty("bookedForDate", String.valueOf(src.getBookedForDate()));
        jsonObject.addProperty("bookedForTime", String.valueOf(src.getBookedForTime()));
        jsonObject.addProperty("madeBookingDate", String.valueOf(src.getMadeBookingDate()));
        jsonObject.addProperty("madeBookingTime", String.valueOf(src.getMadeBookingTime()));

        return jsonObject;
    }

    @Override
    public Booking deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Deserialize fields
        Long bookingId = jsonObject.get("bookingId").getAsLong(); // Or handle exclusion
        LocalDate bookedForDate = LocalDate.parse(jsonObject.get("bookedForDate").getAsString());
        LocalTime bookedForTime = LocalTime.parse(jsonObject.get("bookedForTime").getAsString());
        LocalDate madeBookingDate = LocalDate.parse(jsonObject.get("madeBookingDate").getAsString());
        LocalTime madeBookingTime = LocalTime.parse(jsonObject.get("madeBookingTime").getAsString());

        // Create Booking object
        Booking booking = new Booking.Builder().setBookingId(bookingId)
                .setBookedForDate(bookedForDate)
                .setBookedForTime(bookedForTime)
                        .setMadeBookingDate(madeBookingDate)
                                .setMadeBookingTime(madeBookingTime).build();

        // Set other fields
        //booking.setOtherField(jsonObject.get("otherField").getAsString());

        return booking;
    }
}
