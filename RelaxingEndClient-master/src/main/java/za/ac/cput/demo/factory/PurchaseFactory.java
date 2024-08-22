package za.ac.cput.demo.factory;


import za.ac.cput.demo.domain.Payment;
import za.ac.cput.demo.domain.Purchase;

import java.time.LocalDate;
import java.time.LocalTime;

public class PurchaseFactory {
    public static Purchase buildPurchase(Long purchaseId, Float purchaseAmount, LocalDate purchaseDate, LocalTime purchaseTime, Payment payment )
    {

        return new Purchase.Builder()
                .setPurchaseId(purchaseId)
                .setPurchaseAmount(purchaseAmount)
                .setPurchaseDate(purchaseDate)
                .setPurchaseTime(purchaseTime)
                .setPayment(payment).build();
    }

    public static Purchase buildOrder(Long purchaseId, Float purchaseAmount, LocalDate purchaseDate, LocalTime purchaseTime) {

        return new Purchase.Builder()
                .setPurchaseId(purchaseId)
                .setPurchaseAmount(purchaseAmount)
                .setPurchaseDate(purchaseDate)
                .setPurchaseTime(purchaseTime)
                .build();
    }

    public static Purchase buildPurchase(Long purchaseId,String purchaseItems, Float purchaseAmount,LocalDate purchaseDate, LocalTime purchaseTime )
    {

        return new Purchase.Builder()
                .setPurchaseId(purchaseId)
                .setPurchaseItems(purchaseItems)
                .setPurchaseAmount(purchaseAmount)
                .setPurchaseDate(purchaseDate)
                .setPurchaseTime(purchaseTime)
                .build();
    }


}
