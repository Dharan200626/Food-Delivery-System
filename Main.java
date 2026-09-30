import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        FoodDeliverySystem system = new FoodDeliverySystem();

        loadRestaurants(system);

        try {
            runOrderFlow(sc, system);
        } catch (NoSuchElementException e) {
            // Happens when input ends before the order is finished (Ctrl+D / Ctrl+Z)
            System.out.println("\nInput ended unexpectedly. Exiting.");
        } finally {
            sc.close();
        }
    }

    // =========================
    // SAMPLE DATA
    // =========================

    private static void loadRestaurants(FoodDeliverySystem system) {

        Restaurant anjappar =
                new Restaurant(1, "Anjappar", "Chennai");

        anjappar.addFoodItem(new FoodItem(101, "Chicken Biryani", 180));
        anjappar.addFoodItem(new FoodItem(102, "Mutton Biryani", 250));
        anjappar.addFoodItem(new FoodItem(103, "Chicken 65", 150));
        anjappar.addFoodItem(new FoodItem(104, "Mutton Chukka", 220));
        anjappar.addFoodItem(new FoodItem(105, "Fish Fry", 180));
        anjappar.addFoodItem(new FoodItem(106, "Parotta", 50));
        anjappar.addFoodItem(new FoodItem(107, "Kothu Parotta", 130));
        anjappar.addFoodItem(new FoodItem(108, "Chicken Noodles", 160));
        anjappar.addFoodItem(new FoodItem(109, "Chicken Fried Rice", 180));
        anjappar.addFoodItem(new FoodItem(110, "Egg Dosa", 100));

        system.addRestaurant(anjappar);

        Restaurant a2b =
                new Restaurant(2, "A2B", "Salem");

        a2b.addFoodItem(new FoodItem(201, "Idli", 50));
        a2b.addFoodItem(new FoodItem(202, "Masala Dosa", 80));
        a2b.addFoodItem(new FoodItem(203, "Plain Dosa", 60));
        a2b.addFoodItem(new FoodItem(204, "Vada", 40));
        a2b.addFoodItem(new FoodItem(205, "Pongal", 70));
        a2b.addFoodItem(new FoodItem(206, "Poori Masala", 90));
        a2b.addFoodItem(new FoodItem(207, "Rava Dosa", 100));
        a2b.addFoodItem(new FoodItem(208, "Vegetable Biryani", 140));
        a2b.addFoodItem(new FoodItem(209, "Curd Rice", 70));
        a2b.addFoodItem(new FoodItem(210, "Ghee Roast", 120));

        system.addRestaurant(a2b);

        Restaurant saravana =
                new Restaurant(3, "Saravana Bhavan", "Coimbatore");

        saravana.addFoodItem(new FoodItem(301, "Idli", 50));
        saravana.addFoodItem(new FoodItem(302, "Masala Dosa", 100));
        saravana.addFoodItem(new FoodItem(303, "Ghee Roast", 120));
        saravana.addFoodItem(new FoodItem(304, "Medhu Vada", 60));
        saravana.addFoodItem(new FoodItem(305, "Pongal", 80));
        saravana.addFoodItem(new FoodItem(306, "South Indian Meals", 180));
        saravana.addFoodItem(new FoodItem(307, "Lemon Rice", 80));
        saravana.addFoodItem(new FoodItem(308, "Sambar Rice", 90));
        saravana.addFoodItem(new FoodItem(309, "Chapathi", 70));
        saravana.addFoodItem(new FoodItem(310, "Vegetable Biryani", 150));

        system.addRestaurant(saravana);

        Restaurant biryaniHouse =
                new Restaurant(4, "Biryani House", "Chennai");

        biryaniHouse.addFoodItem(new FoodItem(401, "Chicken Biryani", 190));
        biryaniHouse.addFoodItem(new FoodItem(402, "Mutton Biryani", 260));
        biryaniHouse.addFoodItem(new FoodItem(403, "Egg Biryani", 150));
        biryaniHouse.addFoodItem(new FoodItem(404, "Chicken 65", 160));
        biryaniHouse.addFoodItem(new FoodItem(405, "Pepper Chicken", 190));
        biryaniHouse.addFoodItem(new FoodItem(406, "Chicken Fried Rice", 170));
        biryaniHouse.addFoodItem(new FoodItem(407, "Chicken Noodles", 170));
        biryaniHouse.addFoodItem(new FoodItem(408, "Parotta", 50));
        biryaniHouse.addFoodItem(new FoodItem(409, "Chicken Kothu Parotta", 150));
        biryaniHouse.addFoodItem(new FoodItem(410, "Plain Dosa", 70));

        system.addRestaurant(biryaniHouse);

        Restaurant southIndian =
                new Restaurant(5, "South Indian Taste", "Madurai");

        southIndian.addFoodItem(new FoodItem(501, "Idli", 45));
        southIndian.addFoodItem(new FoodItem(502, "Plain Dosa", 60));
        southIndian.addFoodItem(new FoodItem(503, "Onion Dosa", 90));
        southIndian.addFoodItem(new FoodItem(504, "Chicken Biryani", 180));
        southIndian.addFoodItem(new FoodItem(505, "Parotta", 45));
        southIndian.addFoodItem(new FoodItem(506, "Kothu Parotta", 120));
        southIndian.addFoodItem(new FoodItem(507, "Fish Curry", 180));
        southIndian.addFoodItem(new FoodItem(508, "Chicken Curry", 190));
        southIndian.addFoodItem(new FoodItem(509, "Curd Rice", 60));
        southIndian.addFoodItem(new FoodItem(510, "Filter Coffee", 40));

        system.addRestaurant(southIndian);
    }

    // =========================
    // ORDER FLOW
    // =========================

    private static void runOrderFlow(Scanner sc, FoodDeliverySystem system) {

        System.out.println("================================");
        System.out.println("      FOOD DELIVERY SYSTEM");
        System.out.println("================================");

        int customerId = readInt(sc, "Enter Customer ID: ", 1);

        String customerName = readName(sc);

        String phone = readPhone(sc);

        Customer customer = new Customer(customerId, customerName, phone);

        system.addCustomer(customer);

        system.displayRestaurants();

        Restaurant selectedRestaurant = null;

        while (selectedRestaurant == null) {

            int restaurantId = readInt(sc, "Enter Restaurant ID: ", 1);

            selectedRestaurant = system.findRestaurant(restaurantId);

            if (selectedRestaurant == null) {
                System.out.println("Invalid Restaurant ID. Try again.");
            }
        }

        selectedRestaurant.displayMenu();

        Order order = new Order(system.nextOrderId(), customer, selectedRestaurant);

        while (true) {

            int foodId = readInt(sc, "\nEnter Food ID: ", 1);

            FoodItem food = selectedRestaurant.findFoodItem(foodId);

            if (food == null) {
                System.out.println("Invalid Food ID. Try again.");
                continue;
            }

            int quantity = readInt(sc, "Enter Quantity: ", 1);

            order.addItem(food, quantity);

            System.out.println(food.getName() + " added to order.");

            if (!readYesNo(sc, "Do you want to add another food? (yes/no): ")) {
                break;
            }
        }

        system.addOrder(order);

        System.out.println("\nYour Order");
        order.displayOrder();

        if (readYesNo(sc, "\nDo you want to confirm the order? (yes/no): ")) {

            system.updateOrderStatus(order.getOrderId(), OrderStatus.CONFIRMED);

            System.out.println("Order confirmed successfully.");

        } else {

            system.updateOrderStatus(order.getOrderId(), OrderStatus.CANCELLED);

            System.out.println("Order cancelled.");
        }
    }

    // =========================
    // SAFE INPUT HELPERS
    // =========================

    /** Keeps asking until the user types a whole number >= min. */
    private static int readInt(Scanner sc, String prompt, int min) {

        while (true) {

            System.out.print(prompt);
            String line = sc.nextLine().trim();

            try {
                int value = Integer.parseInt(line);

                if (value >= min) {
                    return value;
                }

                System.out.println("Value must be " + min + " or greater.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /** Names may contain only letters and single spaces between words. */
    private static String readName(Scanner sc) {

        while (true) {

            System.out.print("Enter Customer Name: ");
            String name = sc.nextLine().trim();

            if (name.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
                return name;
            }

            System.out.println("Invalid name. Name should contain only letters.");
        }
    }

    private static String readPhone(Scanner sc) {

        while (true) {

            System.out.print("Enter Phone Number: ");
            String phone = sc.nextLine().trim();

            if (phone.matches("[0-9]{10}")) {
                return phone;
            }

            System.out.println("Invalid phone number. Enter exactly 10 digits.");
        }
    }

    /** Accepts yes/y/no/n (any case) and asks again for anything else. */
    private static boolean readYesNo(Scanner sc, String prompt) {

        while (true) {

            System.out.print(prompt);
            String answer = sc.nextLine().trim();

            if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                return true;
            }

            if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Please type yes or no.");
        }
    }
}
