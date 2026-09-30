import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurant {

    private final int id;
    private final String name;
    private final String location;

    private final List<FoodItem> menu;

    public Restaurant(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.menu = new ArrayList<>();
    }

    public void addFoodItem(FoodItem foodItem) {
        if (foodItem == null) {
            System.out.println("Cannot add an empty food item.");
            return;
        }
        if (findFoodItem(foodItem.getId()) != null) {
            System.out.println("Food ID " + foodItem.getId() + " already exists.");
            return;
        }
        menu.add(foodItem);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<FoodItem> getMenu() {
        return Collections.unmodifiableList(menu);
    }

    public FoodItem findFoodItem(int foodId) {
        for (FoodItem food : menu) {
            if (food.getId() == foodId) {
                return food;
            }
        }
        return null;
    }

    public void displayMenu() {
        System.out.println("\nRestaurant: " + name);
        System.out.println("Location: " + location);
        System.out.println("--------------------------------");

        for (FoodItem food : menu) {
            System.out.println(food);
        }
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + location;
    }
}
