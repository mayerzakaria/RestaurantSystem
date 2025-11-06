/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.util.*;
import java.text.SimpleDateFormat;

public class Menu {
    
    private Date lastUpdate;
    private ArrayList<MenuItem> items;

    private void updateLastUpdate() {
        this.lastUpdate = new Date();
    }

    public Menu() {
        this.lastUpdate = new Date();
        this.items = new ArrayList<>();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return items;
    }

    public boolean addItem(MenuItem item) {
        if (item == null) {
            System.out.println(" Error: Cannot add null item");
            return false;
        }

        for (MenuItem existing : items) {
            if (existing.getName().equalsIgnoreCase(item.getName())) {
                System.out.println(" Error: '" + item.getName() + 
                                   "' already exists in menu!");
                return false;
            }
        }

        items.add(item);
        updateLastUpdate();
        System.out.println(item.getName() + " added to menu successfully!");
        return true;
    }

    public boolean removeItem(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            System.out.println(" Error: Item name cannot be empty");
            return false;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                items.remove(i);
                updateLastUpdate();
                System.out.println(itemName + " removed from menu.");
                return true;
            }
        }

        System.out.println(" Error: '" + itemName + "' not found in menu.");
        return false;
    }

    public boolean updateItem(String itemName, MenuItem newItem) {
        if (itemName == null || newItem == null) {
            System.out.println(" Error: Invalid parameters");
            return false;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                items.set(i, newItem);
                updateLastUpdate();
                System.out.println("'" + itemName + "' updated successfully!");
                return true;
            }
        }

        System.out.println(" Error: '" + itemName + "' not found in menu.");
        return false;
    }

    public ArrayList<MenuItem> searchItems(String keyword) {
        ArrayList<MenuItem> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (MenuItem item : items) {
            if (item.getName().toLowerCase().contains(lowerKeyword)
                || item.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(item);
            }
        }

        return results;
    }

    public ArrayList<MenuItem> getItemByCategory(String category) {
        ArrayList<MenuItem> categoryItems = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                categoryItems.add(item);
            }
        }
        return categoryItems;
    }

    public ArrayList<MenuItem> getAllAvialableItems() {
        ArrayList<MenuItem> avialableItems = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.isAvailable()) {
                avialableItems.add(item);
            }
        }
        return avialableItems;
    }

    public void showLastUpdate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Last updated on: " + formatter.format(lastUpdate));
    }

    // ✅ FIXED VERSION of selectMenuItems()
    // 1. Removed reference to undefined 'menu'
    // 2. Added Scanner as a parameter (no need for static)
    // 3. Uses this.items instead of external menu variable

    public Map<MenuItem, Integer> selectMenuItems(Scanner scanner) {
        Map<MenuItem, Integer> selectedItems = new HashMap<>();

        System.out.println("\n========== MENU ==========");
        ArrayList<MenuItem> availableItems = getAllAvialableItems();

        if (availableItems.isEmpty()) {
            System.out.println("No items available at the moment!");
            return selectedItems;
        }

        for (int i = 0; i < availableItems.size(); i++) {
            MenuItem item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getInfo());
        }

        System.out.println("\nSelect items (enter 0 to finish):");

        while (true) {
            System.out.print("Item number: ");
            int itemNum = scanner.nextInt();

            if (itemNum == 0) break;

            if (itemNum < 1 || itemNum > availableItems.size()) {
                System.out.println("Invalid item number!");
                continue;
            }

            System.out.print("Quantity: ");
            int qty = scanner.nextInt();

            MenuItem selectedItem = availableItems.get(itemNum - 1);
            selectedItems.put(selectedItem, qty);

            System.out.println(" Added: " + selectedItem.getName() + " x" + qty);
        }

        scanner.nextLine(); // consume newline
        return selectedItems;
    }
}
