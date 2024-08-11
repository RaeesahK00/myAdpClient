package za.ac.cput.demo.factory;

import za.ac.cput.demo.domain.Product;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProductFactory {
    public static Product buildProduct(Long productId, String productName, String productDescription, Float price, Integer quantity){


        return new Product.Builder().setProductId(productId)
                .setProductName(productName)
                .setProductDescription(productDescription)
                .setPrice(price)
                .setQuantity(quantity)
                .build();
    }

    public static Product buildProduct(long productId, String productName, String productDescription, Float price, Integer quantity){


        return new Product.Builder()
                .setProductName(productName)
                .setProductDescription(productDescription)
                .setPrice(price)
                .setQuantity(quantity)
                .build();
    }
}
