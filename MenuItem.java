/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

/**
 *
 * @author Mayer
 */
import java.util.*;
import java.io.Serializable;

public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
     private String name;
    private String description;
    private double price;
    private String category;
    private boolean isAvailable;

    public MenuItem(String name, String description, double price, String category, boolean isAvailable) {
        setName(name);
        setDescription(description);
        setPrice(price);
        setCategory(category);
        setIsAvailable(isAvailable);
    }
    
      public void setName(String name){
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
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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
    
    public String getInfo(){
        return String.format(
            "Name: %s | Description: %s | Price: %.2f | Category: %s | Available: %s",
            name, description, price, category, isAvailable ? "Yes" : "No"
        );
    }
}
