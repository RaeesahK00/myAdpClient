package za.ac.cput.demo.domain;

import java.util.Objects;

public class Product {
        protected Long productId;
        protected String productName;
        protected String productDescription;
        protected Float price;
        protected Integer quantity;

        protected Product() {
        }

        public Product(Builder builder) {
            this.productId = builder.productId;
            this.productName = builder.productName;
            this.productDescription = builder.productDescription;
            this.price = builder.price;
            this.quantity = builder.quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public Float getPrice() {
            return price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Objects.equals(productId, product.productId) && Objects.equals(productName, product.productName) && Objects.equals(productDescription, product.productDescription) && Objects.equals(price, product.price) && Objects.equals(quantity, product.quantity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, productName, productDescription, price, quantity);
        }

        @Override
        public String toString() {
            return "Product{" +
                    "price=" + price +
                    ", productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", productDescription='" + productDescription + '\'' +
                    ", quantity=" + quantity +
                    '}' + "\n";
        }

        public static class Builder {
            protected Long productId;
            protected String productName;
            protected String productDescription;
            protected Float price;
            protected Integer quantity;

            public Builder setPrice(Float price) {
                this.price = price;
                return this;
            }

            public Builder setProductDescription(String productDescription) {
                this.productDescription = productDescription;
                return this;
            }

            public Builder setProductName(String productName) {
                this.productName = productName;
                return this;
            }

            public Builder setProductId(Long productId) {
                this.productId = productId;
                return this;
            }

            public Builder setQuantity(Integer quantity) {
                this.quantity = quantity;
                return this;
            }

            public Builder copy(Product product) {
                this.productId = product.productId;
                this.productName = product.productName;
                this.productDescription = product.productDescription;
                this.price = product.price;
                this.quantity = product.quantity;
                return this;
            }

            public Product build() {
                return new Product(this);
            }

        }
    }

