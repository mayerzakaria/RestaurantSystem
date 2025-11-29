package restaurantsystem;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int itemId;      // Added for database ID
    private int menuId;      // Added to link to menu
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean isAvailable;

    public MenuItem(int itemId, int menuId, String name, String description, double price, String category, boolean isAvailable) {
        this.itemId = itemId;
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

  
    public int getItemId() {
        return itemId;
    }

    public int getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

  
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getInfo() {
        return String.format("%-20s | %-30s | EGP %.2f | %-15s | %s",
                name, description, price, category, isAvailable ? "Available" : "Not Available");
    }

    @Override
    public String toString() {
        return name + " - EGP " + price;
    }
}
