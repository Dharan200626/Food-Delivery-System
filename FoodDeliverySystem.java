import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FoodDeliverySystem {

    private final List<Restaurant> restaurants;
    private final List<Customer> customers;
    private final List<Order> orders;

    private int nextOrderId = 1001;

    public FoodDeliverySystem() {
        restaurants = new ArrayList<>();
        customers = new ArrayList<>();
        orders = new ArrayList<>();
    }

    // =========================
    // RESTAURANT MANAGEMENT
    // =========================

    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            System.out.println("Cannot add an empty restaurant.");
            return;
        }
        if (findRestaurant(restaurant.getId()) != null) {
            System.out.println("Restaurant ID " + restaurant.getId() + " already exists.");
            return;
        }
        restaurants.add(restaurant);
    }

    public Restaurant findRestaurant(int id) {
        return restaurants
                .stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void displayRestaurants() {
        System.out.println("\nAvailable Restaurants");
        restaurants.forEach(System.out::println);
    }

    // =========================
    // CUSTOMER MANAGEMENT
    // =========================

    public void addCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("Cannot add an empty customer.");
            return;
        }
        if (findCustomer(customer.getId()) != null) {
            System.out.println("Customer ID " + customer.getId() + " already exists.");
            return;
        }
        customers.add(customer);
    }

    public Customer findCustomer(int id) {
        return customers
                .stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // =========================
    // ORDER PROCESSING
    // =========================

    /** Returns a fresh, unique order ID (1001, 1002, ...). */
    public int nextOrderId() {
        return nextOrderId++;
    }

    public void addOrder(Order order) {
        if (order == null) {
            System.out.println("Cannot add an empty order.");
            return;
        }
        if (findOrder(order.getOrderId()) != null) {
            System.out.println("Order ID " + order.getOrderId() + " already exists.");
            return;
        }

        orders.add(order);

        System.out.println("Order " + order.getOrderId() + " placed successfully.");
    }

    public Order findOrder(int orderId) {
        return orders
                .stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        if (order.updateStatus(status)) {
            System.out.println("Order status updated to " + status);
        } else {
            System.out.println(
                    "Cannot change order status from "
                            + order.getStatus() + " to " + status + "."
            );
        }
    }

    public void displayAllOrders() {
        for (Order order : orders) {
            order.displayOrder();
        }
    }

    // =========================
    // ANALYTICS
    // =========================

    private List<Order> ordersWithStatus(OrderStatus status) {
        return orders
                .stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void totalRevenue() {
        double revenue = ordersWithStatus(OrderStatus.DELIVERED)
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        System.out.println("Total Revenue: " + FoodItem.formatPrice(revenue));
    }

    public void totalOrders() {
        System.out.println("Total Orders: " + orders.size());
    }

    public void deliveredOrders() {
        System.out.println(
                "Delivered Orders: " + ordersWithStatus(OrderStatus.DELIVERED).size()
        );
    }

    public void cancelledOrders() {
        System.out.println(
                "Cancelled Orders: " + ordersWithStatus(OrderStatus.CANCELLED).size()
        );
    }

    // =========================
    // CUSTOMER ANALYTICS
    // =========================

    public void customerAnalytics() {
        Map<String, Double> customerRevenue =
                ordersWithStatus(OrderStatus.DELIVERED)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        o -> o.getCustomer().getName(),
                                        TreeMap::new,
                                        Collectors.summingDouble(Order::getTotalAmount)
                                )
                        );

        System.out.println("\nCustomer Revenue");

        customerRevenue.forEach(
                (customer, revenue) ->
                        System.out.println(customer + " -> " + FoodItem.formatPrice(revenue))
        );
    }

    // =========================
    // RESTAURANT ANALYTICS
    // =========================

    public void restaurantAnalytics() {
        Map<String, Double> restaurantRevenue =
                ordersWithStatus(OrderStatus.DELIVERED)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        o -> o.getRestaurant().getName(),
                                        TreeMap::new,
                                        Collectors.summingDouble(Order::getTotalAmount)
                                )
                        );

        System.out.println("\nRestaurant Revenue");

        restaurantRevenue.forEach(
                (restaurant, revenue) ->
                        System.out.println(restaurant + " -> " + FoodItem.formatPrice(revenue))
        );
    }

    // =========================
    // FOOD ANALYTICS
    // =========================

    /** Quantity sold per food name, counting delivered orders only. */
    private Map<String, Integer> deliveredFoodQuantities() {
        Map<String, Integer> foodQuantity = new TreeMap<>();

        for (Order order : ordersWithStatus(OrderStatus.DELIVERED)) {
            for (OrderItem item : order.getItems()) {
                foodQuantity.merge(
                        item.getFoodItem().getName(),
                        item.getQuantity(),
                        Integer::sum
                );
            }
        }

        return foodQuantity;
    }

    public void foodAnalytics() {
        System.out.println("\nFood Sales");

        deliveredFoodQuantities().forEach(
                (food, quantity) ->
                        System.out.println(food + " -> " + quantity + " sold")
        );
    }

    public void topSellingFood() {
        deliveredFoodQuantities()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(
                        entry ->
                                System.out.println(
                                        "\nTop Selling Food: "
                                                + entry.getKey()
                                                + " ("
                                                + entry.getValue()
                                                + " sold)"
                                )
                );
    }

    // =========================
    // ORDER STATUS ANALYTICS
    // =========================

    public void orderStatusAnalytics() {
        Map<OrderStatus, Long> statusCount =
                orders
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Order::getStatus,
                                        () -> new java.util.EnumMap<>(OrderStatus.class),
                                        Collectors.counting()
                                )
                        );

        System.out.println("\nOrder Status Analytics");

        statusCount.forEach(
                (status, count) -> System.out.println(status + " -> " + count)
        );
    }

    // =========================
    // HIGH VALUE ORDERS
    // =========================

    public void highValueOrders(double amount) {
        Predicate<Order> highValue = order -> order.getTotalAmount() >= amount;

        System.out.println("\nOrders >= " + FoodItem.formatPrice(amount));

        orders
                .stream()
                .filter(highValue)
                .forEach(
                        order ->
                                System.out.println(
                                        "Order "
                                                + order.getOrderId()
                                                + " -> "
                                                + FoodItem.formatPrice(order.getTotalAmount())
                                )
                );
    }

    // =========================
    // SORT ORDERS BY PRICE
    // =========================

    public void sortOrdersByAmount() {
        System.out.println("\nOrders Sorted By Amount");

        orders
                .stream()
                .sorted(Comparator.comparingDouble(Order::getTotalAmount).reversed())
                .forEach(
                        order ->
                                System.out.println(
                                        "Order "
                                                + order.getOrderId()
                                                + " -> "
                                                + FoodItem.formatPrice(order.getTotalAmount())
                                )
                );
    }

    // =========================
    // FUNCTION EXAMPLE
    // =========================

    public void displayOrderTotal(int orderId) {
        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found");
            return;
        }

        Function<Order, Double> calculateTotal = Order::getTotalAmount;

        System.out.println(
                "Order Total: " + FoodItem.formatPrice(calculateTotal.apply(order))
        );
    }

    // =========================
    // COMPLETE ANALYTICS
    // =========================

    public void showAnalytics() {
        System.out.println("\n====================================");
        System.out.println("       FOOD DELIVERY ANALYTICS");
        System.out.println("====================================");

        totalOrders();
        deliveredOrders();
        cancelledOrders();
        totalRevenue();
        restaurantAnalytics();
        customerAnalytics();
        foodAnalytics();
        topSellingFood();
        orderStatusAnalytics();
        sortOrdersByAmount();

        System.out.println("====================================");
    }
}
