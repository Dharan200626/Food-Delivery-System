import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final int orderId;
    private final Customer customer;
    private final Restaurant restaurant;

    private final List<OrderItem> items;

    private OrderStatus status;

    public Order(int orderId, Customer customer, Restaurant restaurant) {
        if (customer == null || restaurant == null) {
            throw new IllegalArgumentException("Customer and restaurant are required");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
    }

    public void addItem(FoodItem foodItem, int quantity) {
        if (foodItem == null) {
            System.out.println("Invalid food item");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Invalid quantity");
            return;
        }

        // If the same food is ordered again, increase its quantity
        // instead of listing it on a second line.
        for (OrderItem item : items) {
            if (item.getFoodItem().getId() == foodItem.getId()) {
                item.addQuantity(quantity);
                return;
            }
        }

        items.add(new OrderItem(foodItem, quantity));
    }

    public double getTotalAmount() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }

        return total;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Moves the order to a new status.
     * Rules: a DELIVERED or CANCELLED order is final, the status can only
     * move forward, and an order can be cancelled only before it goes
     * out for delivery.
     *
     * @return true if the status changed, false if the change was not allowed
     */
    public boolean updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            return false;
        }
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            return false;
        }
        if (newStatus == OrderStatus.CANCELLED) {
            if (status.ordinal() >= OrderStatus.OUT_FOR_DELIVERY.ordinal()) {
                return false;
            }
        } else if (newStatus.ordinal() <= status.ordinal()) {
            return false;
        }

        this.status = newStatus;
        return true;
    }

    public void displayOrder() {
        System.out.println("\n================================");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Status: " + status);

        System.out.println("--------------------------------");

        for (OrderItem item : items) {
            System.out.println(item);
        }

        System.out.println("--------------------------------");
        System.out.println("Total: " + FoodItem.formatPrice(getTotalAmount()));
        System.out.println("================================");
    }
}
