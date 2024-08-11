package za.ac.cput.demo.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Booking {
    public long bookingId;
    public LocalDate bookedForDate; //for the date the client wants to book
    public LocalTime bookedForTime; //for the time the user wants to book
    public LocalDate madeBookingDate;   //the day the booking was made
    public LocalTime madeBookingTime;   //the time the booking was made

    public Booking() {
    }

    public Booking(Builder builder){
        this.bookingId = builder.bookingId;
        this.bookedForDate = builder.bookedForDate;
        this.bookedForTime = builder.bookedForTime;
        this.madeBookingDate = builder.madeBookingDate;
        this.madeBookingTime = builder.madeBookingTime;
    }

    public long getBookingId() {
        return bookingId;
    }

    public LocalDate getBookedForDate() {
        return bookedForDate;
    }

    public LocalTime getBookedForTime() {
        return bookedForTime;
    }

    public LocalDate getMadeBookingDate() {
        return madeBookingDate;
    }

    public LocalTime getMadeBookingTime() {
        return madeBookingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingId == booking.bookingId && Objects.equals(bookedForDate, booking.bookedForDate) && Objects.equals(bookedForTime, booking.bookedForTime) && Objects.equals(madeBookingDate, booking.madeBookingDate) && Objects.equals(madeBookingTime, booking.madeBookingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, bookedForDate, bookedForTime, madeBookingDate, madeBookingTime);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookedForDate=" + bookedForDate +
                ", bookedForTime=" + bookedForTime +
                ", madeBookingDate=" + madeBookingDate +
                ", madeBookingTime=" + madeBookingTime +
                '}';
    }

    public static class Builder{
        public long bookingId;
        public LocalDate bookedForDate;
        public LocalTime bookedForTime;
        public LocalDate madeBookingDate;
        public LocalTime madeBookingTime;

        public Builder setBookingId(long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setBookedForDate(LocalDate bookedForDate) {
            this.bookedForDate = bookedForDate;
            return this;
        }

        public Builder setBookedForTime(LocalTime bookedForTime) {
            this.bookedForTime = bookedForTime;
            return this;
        }

        public Builder setMadeBookingDate(LocalDate madeBookingDate) {
            this.madeBookingDate = madeBookingDate;
            return this;
        }

        public Builder setMadeBookingTime(LocalTime madeBookingTime) {
            this.madeBookingTime = madeBookingTime;
            return this;
        }

        public Builder copy(Booking booking){
            this.bookingId = booking.bookingId;     //watch here
            this.bookedForDate = booking.bookedForDate;
            this.bookedForTime = booking.bookedForTime;
            this.madeBookingDate = booking.madeBookingDate;
            this.madeBookingTime = booking.madeBookingTime;
            return this;
        }
        public Booking build(){return new Booking(this);}
    }


}
