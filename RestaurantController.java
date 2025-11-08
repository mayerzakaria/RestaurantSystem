package restaurantsystem1;

import java.util.*;


public class RestaurantController {
    private ArrayList<Customer> customers;
    private ArrayList<Cashier> cashiers;
    private Menu menu;
    private ArrayList<Table> tables;
    private ArrayList<Order> orders;
    private Systemmanager systemManager;

    public RestaurantController() {
        this.customers = new ArrayList<>();
        this.cashiers = new ArrayList<>();
        this.menu = new Menu();
        this.tables = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.systemManager = new Systemmanager(Systemmode.ONLINE_DELIVERY);
    }

    public void initializeSystem() {
        System.out.println("\n Initializing Restaurant System...");

        
        DataManager.initializeDataDirectory();
        
        if (DataManager.dataExists()) {
            loadAllData();
        } else {
            
            createDefaultData();
        }

        System.out.println(" System initialized successfully!\n");
    }

    private void createDefaultData() {
        menu.addItem(new MenuItem("Burger", "Beef burger with cheese", 80.0, "Main", true));
        menu.addItem(new MenuItem("Pizza", "Margherita pizza", 120.0, "Main", true));
        menu.addItem(new MenuItem("Pasta", "Creamy pasta with chicken", 95.0, "Main", true));
        menu.addItem(new MenuItem("Salad", "Fresh green salad", 45.0, "Appetizer", true));
        menu.addItem(new MenuItem("Cola", "Soft drink", 20.0, "Beverage", true));
        menu.addItem(new MenuItem("Juice", "Fresh orange juice", 30.0, "Beverage", true));
        menu.addItem(new MenuItem("French Fries", "Crispy golden fries", 35.0, "Side", true));
        menu.addItem(new MenuItem("Ice Cream", "Vanilla ice cream", 40.0, "Dessert", true));

        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i, 4, Table.TableStatus.AVAILABLE));
        }

        cashiers.add(new Cashier("Ahmed Ali", "ahmed@restaurant.com", 
                                "0123456789", "cash123", 5000, "Morning"));
        cashiers.add(new Cashier("Sara Mohamed", "sara@restaurant.com", 
                                "0111222333", "cash456", 5000, "Evening"));

        System.out.println(" Default data created successfully!");
        displayCashierCredentials();
    }

    private void displayCashierCredentials() {
        System.out.println("\n Sample Cashier Logins:");
        for (Cashier c : cashiers) {
            System.out.println("  - ID: " + c.getId() + " | Password: " + c.getPassword());
        }
    }

    public void saveAllData() {
        DataManager.saveAllData(customers, cashiers, menu, tables, orders);
    }

    private void loadAllData() {
        System.out.println(" Loading saved data...");
        DataManager.loadCounters();
        this.customers = DataManager.loadCustomers();
        this.cashiers = DataManager.loadCashiers();
        this.menu = DataManager.loadMenu();
        this.tables = DataManager.loadTables();
        this.orders = DataManager.loadOrders();
        
        if (cashiers.isEmpty()) {
            createDefaultData();
        } else {
            displayCashierCredentials();
        }
    }

    public Customer registerCustomer(String name, String email, String phone, 
                                     String username, String password, String address) {
        for (Customer c : customers) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                System.out.println(" Username already exists!");
                return null;
            }
        }

        Address addr = new Address(1, address, true);
        if (!addr.validateAddress()) {
            return null;
        }

        Customer customer = new Customer(username, password, false, addr, name, email, phone);
        customers.add(customer);
        
        System.out.println("\n Registration completed!");
        System.out.println(" Your Customer ID: " + customer.getCustomerId());
        System.out.println(" Username: " + username);
        
        saveAllData();
        return customer;
    }

    public Customer findCustomer(String idOrUsername) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(idOrUsername) || 
                c.getUsername().equalsIgnoreCase(idOrUsername)) {
                return c;
            }
        }
        return null;
    }

    public Customer customerLogin(String idOrUsername, String password) {
        Customer customer = findCustomer(idOrUsername);
        if (customer != null && customer.login(idOrUsername, password)) {
            return customer;
        }
        return null;
    }

    public Cashier findCashier(String id) {
        for (Cashier c : cashiers) {
            if (c.getEmployeeId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public Cashier cashierLogin(String id, String password) {
        Cashier cashier = findCashier(id);
        if (cashier != null && cashier.login(id, password)) {
            return cashier;
        }
        return null;
    }

    public Cashier addCashier(String name, String email, String phone, 
                              String password, double salary, String shift) {
        Cashier cashier = new Cashier(name, email, phone, password, salary, shift);
        cashiers.add(cashier);
        System.out.println(" Cashier added: " + cashier.getEmployeeId());
        saveAllData();
        return cashier;
    }

    public Order createOnlineOrder(Customer customer, Map<MenuItem, Integer> items, 
                                   Address deliveryAddress) {
        Order order = new Order(customer.getCustomerId(), items, 
                              Systemmode.ONLINE_DELIVERY, null);
        order.setDeliveryAddress(deliveryAddress);
        order.calculateSubtotal();
        order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
        order.calculateTotal();
        
        orders.add(order);
        System.out.println(" Online order created successfully!");
        
        return order;
    }

    public Order createTakeawayOrder(Cashier cashier, Customer customer, 
                                     Map<MenuItem, Integer> items) {
        Order order = cashier.processTakeawayOrder(customer, items);
        orders.add(order);
        saveAllData();
        return order;
    }

    
    public Order createDineInOrder(Cashier cashier, Customer customer, 
                                   Map<MenuItem, Integer> items, Table table) {
        Order order = cashier.processWalkInOrder(customer, items, table);
        orders.add(order);
        saveAllData();
        return order;
    }

    public boolean processPayment(Order order, double amount, Payment.PaymentMethod method, 
                                  Cashier cashier) {
        boolean success;
        
        if (cashier != null) {
            success = cashier.acceptPayment(order, amount, method);
        } else {
            Payment payment = new Payment(amount, method, order.getOrderId());
            success = payment.processPayment(order.getTotal());
            if (success) {
                order.setPayment(payment);
                order.updateStatus(Status.COMPLETE);
            }
        }
        
        if (success) {
            saveAllData();
        }
        
        return success;
    }

    
    public ArrayList<Table> getAvailableTables() {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.isAvailable()) {
                available.add(table);
            }
        }
        return available;
    }

    
    public Table selectTable(int tableNumber) {
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber && table.isAvailable()) {
                table.assignTable();
                saveAllData();
                return table;
            }
        }
        return null;
    }

    
    public void releaseTable(Table table) {
        table.releaseTable();
        saveAllData();
    }

    
    public ArrayList<MenuItem> getMenuItemsByCategory(String category) {
        return menu.getItemByCategory(category);
    }

    
    public ArrayList<MenuItem> getAvailableMenuItems() {
        return menu.getAllAvialableItems();
    }

    
    public ArrayList<MenuItem> searchMenuItems(String keyword) {
        return menu.searchItems(keyword);
    }

    
    public boolean addMenuItem(String name, String description, double price, 
                               String category, boolean available) {
        MenuItem item = new MenuItem(name, description, price, category, available);
        boolean success = menu.addItem(item);
        if (success) {
            saveAllData();
        }
        return success;
    }

    public boolean subscribeToElite(Customer customer, boolean paid) {
        boolean success = customer.subscribeToElite(paid);
        if (success) {
            saveAllData();
        }
        return success;
    }


    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Cashier> getCashiers() {
        return cashiers;
    }

    public Menu getMenu() {
        return menu;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Systemmanager getSystemManager() {
        return systemManager;
    }

    public ArrayList<Order> getCustomerOrders(String customerId) {
        ArrayList<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().equals(customerId)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public void shutdown() {
        System.out.println("\n Saving all data before exit...");
        saveAllData();
        System.out.println("Thank you for using Restaurant System!");
    }
}
