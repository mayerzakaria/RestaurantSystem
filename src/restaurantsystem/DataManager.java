package restaurantsystem;

import java.io.*;
import java.util.*;

public class DataManager {
    private static final String DATA_DIR = "data/";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.dat";
    private static final String CASHIERS_FILE = DATA_DIR + "cashiers.dat";
    private static final String DELIVERY_FILE = DATA_DIR + "delivery.dat";
    private static final String ORDERS_FILE = DATA_DIR + "orders.dat";
    private static final String MENU_FILE = DATA_DIR + "menu.dat";
    private static final String TABLES_FILE = DATA_DIR + "tables.dat";
    private static final String COUNTERS_FILE = DATA_DIR + "counters.dat";
    
    public static void initializeDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Data directory created: " + DATA_DIR);
        }
    }
    
    public static void saveAllData(ArrayList<Customer> customers,
                                   ArrayList<Cashier> cashiers,
                                   ArrayList<Delivery> deliveryPersons,
                                   ArrayList<Order> orders,
                                   Menu menu,
                                   ArrayList<Table> tables) {
        initializeDataDirectory();
        
        System.out.println("\n=== SAVING DATA ===");
        
        saveCustomers(customers);
        saveCashiers(cashiers);
        saveDeliveryPersons(deliveryPersons);
        saveOrders(orders);
        saveMenu(menu);
        saveTables(tables);
        saveCounters();
        
        System.out.println("All data saved successfully!");
        System.out.println("===================\n");
    }
    
    public static void saveAllDataSilent(ArrayList<Customer> customers,
                                         ArrayList<Cashier> cashiers,
                                         ArrayList<Delivery> deliveryPersons,
                                         ArrayList<Order> orders,
                                         Menu menu,
                                         ArrayList<Table> tables) {
        initializeDataDirectory();
        
        try {
            saveCustomers(customers);
            saveCashiers(cashiers);
            saveDeliveryPersons(deliveryPersons);
            saveOrders(orders);
            saveMenu(menu);
            saveTables(tables);
            saveCounters();
        } catch (Exception e) {
            // Silent - no error message
        }
    }
    
    public static void loadAllData(ArrayList<Customer> customers,
                                   ArrayList<Cashier> cashiers,
                                   ArrayList<Delivery> deliveryPersons,
                                   ArrayList<Order> orders,
                                   Menu menu,
                                   ArrayList<Table> tables) {
        initializeDataDirectory();
        
        System.out.println("\n=== LOADING DATA ===");
        
        loadCustomers(customers);
        loadCashiers(cashiers);
        loadDeliveryPersons(deliveryPersons);
        loadOrders(orders);
        loadMenu(menu);
        loadTables(tables);
        loadCounters();
        
        System.out.println("All data loaded successfully!");
        System.out.println("====================\n");
    }
    
    private static void saveCustomers(ArrayList<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
            System.out.println("Customers saved: " + customers.size());
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadCustomers(ArrayList<Customer> customers) {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            System.out.println("No customers data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMERS_FILE))) {
            ArrayList<Customer> loaded = (ArrayList<Customer>) ois.readObject();
            customers.clear();
            customers.addAll(loaded);
            System.out.println("Customers loaded: " + customers.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }
    
    private static void saveCashiers(ArrayList<Cashier> cashiers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CASHIERS_FILE))) {
            oos.writeObject(cashiers);
            System.out.println("Cashiers saved: " + cashiers.size());
        } catch (IOException e) {
            System.out.println("Error saving cashiers: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadCashiers(ArrayList<Cashier> cashiers) {
        File file = new File(CASHIERS_FILE);
        if (!file.exists()) {
            System.out.println("No cashiers data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CASHIERS_FILE))) {
            ArrayList<Cashier> loaded = (ArrayList<Cashier>) ois.readObject();
            cashiers.clear();
            cashiers.addAll(loaded);
            System.out.println("Cashiers loaded: " + cashiers.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading cashiers: " + e.getMessage());
        }
    }
    
    private static void saveDeliveryPersons(ArrayList<Delivery> deliveryPersons) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DELIVERY_FILE))) {
            oos.writeObject(deliveryPersons);
            System.out.println("Delivery persons saved: " + deliveryPersons.size());
        } catch (IOException e) {
            System.out.println("Error saving delivery persons: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadDeliveryPersons(ArrayList<Delivery> deliveryPersons) {
        File file = new File(DELIVERY_FILE);
        if (!file.exists()) {
            System.out.println("No delivery persons data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DELIVERY_FILE))) {
            ArrayList<Delivery> loaded = (ArrayList<Delivery>) ois.readObject();
            deliveryPersons.clear();
            deliveryPersons.addAll(loaded);
            System.out.println("Delivery persons loaded: " + deliveryPersons.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading delivery persons: " + e.getMessage());
        }
    }
    
    private static void saveOrders(ArrayList<Order> orders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(orders);
            System.out.println("Orders saved: " + orders.size());
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadOrders(ArrayList<Order> orders) {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            System.out.println("No orders data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ORDERS_FILE))) {
            ArrayList<Order> loaded = (ArrayList<Order>) ois.readObject();
            orders.clear();
            orders.addAll(loaded);
            System.out.println("Orders loaded: " + orders.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }
    
    private static void saveMenu(Menu menu) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(MENU_FILE))) {
            oos.writeObject(menu.getMenuItems());
            System.out.println("Menu items saved: " + menu.getMenuItems().size());
        } catch (IOException e) {
            System.out.println("Error saving menu: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadMenu(Menu menu) {
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            System.out.println("No menu data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(MENU_FILE))) {
            ArrayList<MenuItem> loaded = (ArrayList<MenuItem>) ois.readObject();
            
            for (MenuItem item : loaded) {
                menu.addItem(item);
            }
            System.out.println("Menu items loaded: " + loaded.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }
    
    private static void saveTables(ArrayList<Table> tables) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TABLES_FILE))) {
            oos.writeObject(tables);
            System.out.println("Tables saved: " + tables.size());
        } catch (IOException e) {
            System.out.println("Error saving tables: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadTables(ArrayList<Table> tables) {
        File file = new File(TABLES_FILE);
        if (!file.exists()) {
            System.out.println("No tables data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TABLES_FILE))) {
            ArrayList<Table> loaded = (ArrayList<Table>) ois.readObject();
            tables.clear();
            tables.addAll(loaded);
            System.out.println("Tables loaded: " + tables.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tables: " + e.getMessage());
        }
    }
    
    private static void saveCounters() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUNTERS_FILE))) {
            writer.println(Order.getOrderCounter());
            System.out.println("Counters saved");
        } catch (IOException e) {
            System.out.println("Error saving counters: " + e.getMessage());
        }
    }
    
    private static void loadCounters() {
        File file = new File(COUNTERS_FILE);
        if (!file.exists()) {
            System.out.println("No counters data found. Starting fresh.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTERS_FILE))) {
            int orderCounter = Integer.parseInt(reader.readLine());
            Order.setOrderCounter(orderCounter);
            System.out.println("Counters loaded");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading counters: " + e.getMessage());
        }
    }
    
    public static boolean dataExists() {
        return new File(CUSTOMERS_FILE).exists() ||
               new File(CASHIERS_FILE).exists() ||
               new File(DELIVERY_FILE).exists() ||
               new File(ORDERS_FILE).exists() ||
               new File(MENU_FILE).exists() ||
               new File(TABLES_FILE).exists();
    }
    
    public static void clearAllData() {
        initializeDataDirectory();
        
        System.out.println("\n=== CLEARING ALL DATA ===");
        
        deleteFile(CUSTOMERS_FILE);
        deleteFile(CASHIERS_FILE);
        deleteFile(DELIVERY_FILE);
        deleteFile(ORDERS_FILE);
        deleteFile(MENU_FILE);
        deleteFile(TABLES_FILE);
        deleteFile(COUNTERS_FILE);
        
        System.out.println("All data cleared!");
        System.out.println("=========================\n");
    }
    
    private static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted: " + filename);
            } else {
                System.out.println("Failed to delete: " + filename);
            }
        }
    }
}