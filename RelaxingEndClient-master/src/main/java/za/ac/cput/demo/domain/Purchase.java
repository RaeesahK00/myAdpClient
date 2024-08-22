package za.ac.cput.demo.domain;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


public class Purchase {

    private Long purchaseId;
    private Float purchaseAmount;
    private String purchaseItems;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;

    private Payment payment;

    public Purchase() {
    }


    public Purchase(Builder builder) {
        this.purchaseId = builder.purchaseId;
        this.purchaseAmount = builder.purchaseAmount;
        this.purchaseDate = builder.purchaseDate;
        this.purchaseTime = builder.purchaseTime;
        this.payment = builder.payment;
        this.purchaseItems = builder.purchaseItems;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public Float getPurchaseAmount() {
        return purchaseAmount;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public LocalTime getPurchaseTime() {
        return purchaseTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getPurchaseItems() {
        return purchaseItems;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(purchaseId, purchase.purchaseId) && Objects.equals(purchaseAmount, purchase.purchaseAmount) && Objects.equals(purchaseItems, purchase.purchaseItems) && Objects.equals(purchaseDate, purchase.purchaseDate) && Objects.equals(purchaseTime, purchase.purchaseTime) && Objects.equals(payment, purchase.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, purchaseAmount, purchaseItems, purchaseDate, purchaseTime, payment);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", purchaseAmount=" + purchaseAmount +
                ", purchaseItems=" + purchaseItems + " " +
                ", purchaseDate=" + purchaseDate +
                ", purchaseTime=" + purchaseTime +
                ", payment=" + payment +
                '}'+ "\n";
    }

    public static class Builder{
        private Long purchaseId;
        private Float purchaseAmount;
        private LocalDate purchaseDate;
        private LocalTime purchaseTime;
        private Payment payment;
        private String purchaseItems;


        public Builder setPurchaseId(Long purchaseId) {
            this.purchaseId = purchaseId;
            return this;
        }

        public Builder setPurchaseAmount(Float purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public Builder setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public Builder setPurchaseTime(LocalTime purchaseTime) {
            this.purchaseTime = purchaseTime;
            return this;
        }

        public Builder setPayment(Payment payment) {
            this.payment = payment;
            return this;
        }

        public Builder setPurchaseItems(String purchaseItems) {
            this.purchaseItems = purchaseItems;
            return this;
        }

        public Builder copy(Purchase purchase) {
            this.purchaseId = purchase.purchaseId;
            this.purchaseAmount = purchase.purchaseAmount;
            this.purchaseDate = purchase.purchaseDate;
            this.purchaseTime = purchase.purchaseTime;
            this.payment = purchase.payment;
            this.purchaseItems = purchase.purchaseItems;
            return  this;
        }

        public Purchase build() {
            return new Purchase(this);
        }


    }

}