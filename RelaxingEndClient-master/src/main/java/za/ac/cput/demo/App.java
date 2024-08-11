package za.ac.cput.demo;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import za.ac.cput.demo.domain.Booking;
import com.google.gson.Gson;
import za.ac.cput.demo.typeAdapter.BookingTypeAdapter;

import java.io.IOException;

public class App {

    private static OkHttpClient client = new OkHttpClient();

    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void getAll(){
        try{
            final String URL = "http://localhost:8080/relaxingend/booking/getall";
            String responseBody = run(URL);
            System.out.println(responseBody);
            JSONArray bookings = new JSONArray(responseBody);

            for(int i = 0; i < bookings.length(); i++){
                JSONObject booking = bookings.getJSONObject(i);
                //String id = booking.getString("id");
                //String bookedForDate = booking.getString("bookedForDate");
                //System.out.println(bookedForDate);

                Gson g = new GsonBuilder().registerTypeAdapter(Booking.class, new BookingTypeAdapter())
                        .create();
                System.out.println("Gson");
                Booking b = g.fromJson(booking.toString(), Booking.class);
                System.out.println("Converted to type Booking");
                System.out.println(b.toString());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        getAll();
    }
}
