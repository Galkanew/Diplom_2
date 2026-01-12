package models;

import lombok.Data;
import java.util.List;

@Data
public class Order {
    private List<String> ingredients;

    @Data
    public static class OrderResponse {
        private String name;
        private OrderDetails order;
        private boolean success;
    }

    @Data
    public static class OrderDetails {
        private int number;
    }
}