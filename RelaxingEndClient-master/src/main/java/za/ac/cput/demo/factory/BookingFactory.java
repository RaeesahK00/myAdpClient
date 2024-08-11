package za.ac.cput.demo.factory;



import za.ac.cput.demo.domain.Booking;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingFactory {
    public static Booking buildBooking(long bookingId, LocalDate bookedForDate, LocalTime bookedForTime){
        //No validation needed here

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        return new Booking.Builder().setBookedForDate(bookedForDate)
                .setBookedForTime(bookedForTime)
                .setMadeBookingDate(date)
                .setMadeBookingTime(time)
                .build();
    }
}
