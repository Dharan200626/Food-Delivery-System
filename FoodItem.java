public class FoodItem {

    private final int id;
    private final String name;
    private final double price;

    public FoodItem(int id, String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /** Formats an amount as "Rs.180.00" so every price prints the same way. */
    public static String formatPrice(double amount) {
        return String.format("Rs.%.2f", amount);
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + formatPrice(price);
    }
}
