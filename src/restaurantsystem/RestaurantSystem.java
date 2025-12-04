package restaurantsystem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import restaurantsystemdao.*;
import java.sql.SQLException;

public class RestaurantSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("   WELCOME TO RESTAURANT SYSTEM");
        System.out.println("================================\n");

        // Load data from database
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Cashier> cashiers = new ArrayList<>();
        ArrayList<Delivery> deliveryPersons = new ArrayList<>();
        ArrayList<Table> tables = new ArrayList<>();

        try {
            customers = CustomerDAO.getAllCustomers();
            cashiers = CashierDAO.getAllCashiers();
            deliveryPersons = DeliveryDAO.getAllDeliveryPersons();
            tables = TableDAO.listAllTables();
            System.out.println("Data loaded from database successfully!");
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
            e.printStackTrace();
        }

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register (New Customer)");
            System.out.println("2. Login");
            System.out.println("3. View Menu");
            System.out.println("4. View All Tables");
            System.out.println("5. Exit");
            System.out.println("================================");
            System.out.print("Choose an option: ");

            int mainChoice = -1;
            try {
                mainChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (mainChoice == 1) 
            {
                // Register new customer using the existing method in Customer class
                Customer.registerCustomer(customers, scanner);

            } else if (mainChoice == 2) {
                // Login
                System.out.println("\n=== LOGIN ===");
                System.out.print("Enter your ID (C### / CA### / D###): ");
                String id = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (id.toUpperCase().startsWith("C") && !id.toUpperCase().startsWith("CA")) {
                    // Customer login
                    try {
                        CustomerDAO dao = new CustomerDAO();
                        Customer customer = dao.findById(id);
                        if (customer != null && customer.login(id, password)) {
                            handleCustomerMenu(customer, scanner);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else if (id.toUpperCase().startsWith("CA")) {
                    // Cashier login
                    try {
                        Cashier cashier = CashierDAO.findById(id);
                        if (cashier != null && cashier.login(id, password)) {
                            handleCashierMenu(cashier, scanner, tables);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else if (id.toUpperCase().startsWith("D")) {
                    // Delivery person login
                    try {
                        Delivery delivery = DeliveryDAO.findById(id);
                        if (delivery != null && delivery.login(id, password)) {
                            handleDeliveryMenu(delivery, scanner);
                        } else {
                            System.out.println("Login failed! Invalid credentials.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }

                } else {
                    System.out.println("Invalid ID format!");
                }

            } else if (mainChoice == 3) {
                // View menu
                System.out.println("\n========== MENU ==========");
                ArrayList<MenuItem> menuItems = MenuitemDAO.listAllMenuItems();
                if (menuItems.isEmpty()) {
                    System.out.println("No menu items available.");
                } else {
                    for (MenuItem item : menuItems) {
                        System.out.println(item);
                    }
                }

            } else if (mainChoice == 4) {
                // View all tables
                System.out.println("\n========== ALL TABLES ==========");
                tables = TableDAO.listAllTables();
                if (tables.isEmpty()) {
                    System.out.println("No tables found.");
                } else {
                    for (Table table : tables) {
                        System.out.println(table);
                    }
                }

            } else if (mainChoice == 5) {
                System.out.println("Thank you for using our system!");
                scanner.close();
                System.exit(0);

            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleCustomerMenu(Customer customer, Scanner scanner) {
        boolean loggedIn = true;

        // Load necessary data
        ArrayList<Delivery> deliveryPersons = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        Menu menu = new Menu();

        try {
            deliveryPersons = DeliveryDAO.getAllDeliveryPersons();
            // Load menu items
            ArrayList<MenuItem> menuItems = MenuitemDAO.listAllMenuItems();
            // Populate the menu object with items
            for (MenuItem item : menuItems) {
                menu.addItem(item);
            }

            // Load customer's address from database if not already loaded
            if (customer.getAddress() == null) {
                java.util.List<Address> addresses = AddressDAO.getAddressesByCustomerId(customer.getCustomerId());
                if (!addresses.isEmpty()) {
                    // Find the default address or use the first one
                    Address defaultAddress = null;
                    for (Address addr : addresses) {
                        if (addr.isDefault()) {
                            defaultAddress = addr;
                            break;
                        }
                    }
                    // If no default, use the first address
                    if (defaultAddress == null && !addresses.isEmpty()) {
                        defaultAddress = addresses.get(0);
                    }
                    customer.setAddress(defaultAddress);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("Hello, " + customer.getName() + "!");
            System.out.println("====================================");
            System.out.println("1. Online Delivery");
            System.out.println("2. View Profile");
            System.out.println("3. Subscribe to Elite");
            System.out.println("4. Logout");
            System.out.println("====================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                // Online Delivery Order
                System.out.println("\n=== ONLINE DELIVERY ORDER ===");

                Address deliveryAddress = customer.getAddress();

                // Check if customer has an address
                if (deliveryAddress == null) {
                    System.out.println("No delivery address on file.");
                    System.out.print("Enter delivery address: ");
                    String newAddr = scanner.nextLine();
                    deliveryAddress = new Address(1, newAddr, true);
                    customer.setAddress(deliveryAddress);

                    // Update customer in database with new address
                    try {
                        CustomerDAO dao = new CustomerDAO();
                        dao.update(customer);
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not save address: " + e.getMessage());
                    }
                } else {
                    System.out.println("Delivery Address: " + deliveryAddress.getFullAddress());
                    System.out.print("Use this address? (y/n): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("n")) {
                        System.out.print("Enter new address: ");
                        String newAddr = scanner.nextLine();
                        deliveryAddress = new Address(2, newAddr, false);
                    }
                }

                String testZone = Delivery.determineZone(deliveryAddress);
                if (testZone == null) {
                    System.out.println("\n================================");
                    System.out.println("      ORDER CANCELLED");
                    System.out.println("================================");
                    System.out.println("Delivery is not available for this address.");
                    System.out.println("Please try a different address or use another order type.");
                    System.out.println("================================\n");
                    continue;
                }

                System.out.println("\nDelivery Zone: " + Delivery.getZoneName(testZone));
                System.out.println("Delivery Fee: EGP " + String.format("%.2f", Delivery.getBaseFee(testZone)));
                System.out.println("Estimated Time: " + Delivery.getDeliveryTimeMinutes(testZone) + " minutes");

                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
                if (items.isEmpty()) {
                    System.out.println("No items selected!");
                    continue;
                }

                Order order = new Order(customer.getCustomerId(), items,
                                       Systemmode.ONLINE_DELIVERY, deliveryAddress);

                if (order.getStatus() == Status.CANCELLED) {
                    System.out.println("\n================================");
                    System.out.println("      ORDER CANCELLED");
                    System.out.println("================================");
                    System.out.println("Delivery is not available for this address.");
                    System.out.println("================================\n");
                    continue;
                }

                order.calculateSubtotal();
                order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
                order.calculateTotal();
                System.out.println(order.getOrderSummary());

                System.out.print("\nConfirm order? (y/n): ");
                String orderConfirm = scanner.nextLine();

                if (!orderConfirm.equalsIgnoreCase("y")) {
                    System.out.println("Order cancelled.");
                    continue;
                }

                // Process payment
                System.out.println("\n=== PAYMENT ===");
                System.out.println("Total Amount: EGP " + String.format("%.2f", order.getTotal()));
                System.out.println("Select Payment Method:");
                System.out.println("1. Cash");
                System.out.println("2. Credit Card");
                System.out.println("3. Debit Card");
                System.out.println("4. Mobile Wallet");
                System.out.print("Choice: ");

                int paymentChoice = -1;
                try {
                    paymentChoice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    scanner.nextLine();
                    continue;
                }

                Payment.PaymentMethod paymentMethod;
                switch (paymentChoice) {
                    case 1 -> paymentMethod = Payment.PaymentMethod.CASH;
                    case 2 -> paymentMethod = Payment.PaymentMethod.CREDIT_CARD;
                    case 3 -> paymentMethod = Payment.PaymentMethod.DEBIT_CARD;
                    case 4 -> paymentMethod = Payment.PaymentMethod.MOBILE_WALLET;
                    default -> {
                        System.out.println("Invalid choice! Using Cash as default.");
                        paymentMethod = Payment.PaymentMethod.CASH;
                    }
                }

                Payment payment = new Payment(order.getTotal(), paymentMethod, order.getOrderId());
                boolean paymentSuccess = payment.processPayment(order.getTotal());

                if (paymentSuccess) {
                  
                    try {
                        PaymentDAO paymentDAO = new PaymentDAO();
                        int paymentId = paymentDAO.insert(payment);
                        if (paymentId > 0) {
                          
                            order.setPayment(payment);
                          
                            order.updateStatus(Status.PENDING);
                        } else {
                            System.err.println("Error: Could not save payment to database.");
                            order.updateStatus(Status.FAILED);
                            System.out.println("Payment processing failed. Order cancelled.");
                            continue;
                        }
                    } catch (SQLException e) {
                        System.err.println("Error: Could not save payment to database: " + e.getMessage());
                        order.updateStatus(Status.FAILED);
                        System.out.println("Payment processing failed. Order cancelled.");
                        continue;
                    }

                    // Assign delivery person
                    Delivery availableDelivery = null;
                    for (Delivery d : deliveryPersons) {
                        if (d.isAvailable()) {
                            availableDelivery = d;
                            break;
                        }
                    }

                    if (availableDelivery != null) {
                        // First, assign delivery person to the order object
                        order.assignDeliveryPerson(availableDelivery);

                        // Second, accept the order on the delivery person's side
                        if (availableDelivery.acceptOrder(order)) {
                            System.out.println("\nOrder will be delivered by: " + availableDelivery.getName());
                            System.out.println("Phone: " + availableDelivery.getPhoneNumber());

                            // Third, save the order to database WITH the delivery ID
                            try {
                                OrderDAO orderDAO = new OrderDAO();
                                orderDAO.insert(order);
                                System.out.println("Order placed successfully!");
                                System.out.println("Order ID: " + order.getOrderId());
                            } catch (SQLException e) {
                                System.err.println("Error: Could not save order to database: " + e.getMessage());
                                System.out.println("Order created but not saved to database. Please contact support.");
                            }

                            // Finally, update delivery person in database after order is saved
                            try {
                                DeliveryDAO deliveryDAO = new DeliveryDAO();
                                deliveryDAO.update(availableDelivery);
                            } catch (SQLException e) {
                                System.err.println("Warning: Could not update delivery person in database: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("\nNo delivery person available at the moment.");
                        System.out.println("Order will be assigned soon.");

                        // Save order even if no delivery person available
                        try {
                            OrderDAO orderDAO = new OrderDAO();
                            orderDAO.insert(order);
                            System.out.println("\nOrder placed successfully!");
                            System.out.println("Order ID: " + order.getOrderId());
                        } catch (SQLException e) {
                            System.err.println("Error: Could not save order to database: " + e.getMessage());
                            System.out.println("Order created but not saved to database. Please contact support.");
                        }
                    }
                } else {
                    order.updateStatus(Status.FAILED);
                    System.out.println("Payment failed. Order cancelled.");
                }

            } else if (choice == 2) {
                System.out.println(customer.getDetails());
            } else if (choice == 3) {
                customer.subscribeElite(scanner);
                try {
                    CustomerDAO dao = new CustomerDAO();
                    dao.update(customer);
                } catch (SQLException e) {
                    System.err.println("Error updating customer: " + e.getMessage());
                }
            } else if (choice == 4) {
                System.out.println("Logged out!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleCashierMenu(Cashier cashier, Scanner scanner, ArrayList<Table> tables) {
        boolean loggedIn = true;

        // Load menu for ordering
        Menu menu = new Menu();
        ArrayList<MenuItem> menuItems = MenuitemDAO.listAllMenuItems();
        for (MenuItem item : menuItems) {
            menu.addItem(item);
        }

        while (loggedIn) {
            System.out.println("\n========== CASHIER MENU ==========");
            System.out.println("Hello, " + cashier.getName() + "!");
            System.out.println("===================================");
            System.out.println("1. Process Takeaway Order");
            System.out.println("2. Process Dine-In Order");
            System.out.println("3. View All Tables");
            System.out.println("4. Release Table");
            System.out.println("5. View Profile");
            System.out.println("6. View Menu");
            System.out.println("7. Logout");
            System.out.println("===================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                // Process Takeaway Order
                System.out.println("\n=== PROCESS TAKEAWAY ORDER ===");
                System.out.print("Enter customer ID  ");
                String custId = scanner.nextLine();

                Customer customer = null;
                if (custId.equals("0")) {
                    System.out.print("Customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Customer phone: ");
                    String phone = scanner.nextLine();
                    Address tempAddr = new Address(0, "Walk-in", true);
                    customer = new Customer("guest", false, tempAddr, name, "guest@walk-in.com", phone);
                } else {
                    try {
                        CustomerDAO dao = new CustomerDAO();
                        customer = dao.findById(custId);
                    } catch (SQLException e) {
                        System.err.println("Error loading customer: " + e.getMessage());
                    }
                }

                if (customer == null) {
                    System.out.println("Customer not found!");
                    continue;
                }

                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
                if (items.isEmpty()) {
                    System.out.println("No items selected!");
                    continue;
                }

                Order order = cashier.processTakeawayOrder(customer, items);
                cashier.printReceipt(order);

                // Process payment
                System.out.println("\n=== PAYMENT ===");
                System.out.println("Total Amount: EGP " + String.format("%.2f", order.getTotal()));
                System.out.println("Select Payment Method:");
                System.out.println("1. Cash");
                System.out.println("2. Credit Card");
                System.out.println("3. Debit Card");
                System.out.println("4. Mobile Wallet");
                System.out.print("Choice: ");

                int paymentChoice = -1;
                try {
                    paymentChoice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    scanner.nextLine();
                    continue;
                }

                Payment.PaymentMethod paymentMethod;
                switch (paymentChoice) {
                    case 1 -> paymentMethod = Payment.PaymentMethod.CASH;
                    case 2 -> paymentMethod = Payment.PaymentMethod.CREDIT_CARD;
                    case 3 -> paymentMethod = Payment.PaymentMethod.DEBIT_CARD;
                    case 4 -> paymentMethod = Payment.PaymentMethod.MOBILE_WALLET;
                    default -> {
                        System.out.println("Invalid choice! Using Cash as default.");
                        paymentMethod = Payment.PaymentMethod.CASH;
                    }
                }

                boolean paymentSuccess = cashier.acceptPayment(order, order.getTotal(), paymentMethod);

                if (paymentSuccess) {
                    // Save payment and order to database
                    try {
                        PaymentDAO paymentDAO = new PaymentDAO();
                        int paymentId = paymentDAO.insert(order.getPayment());
                        if (paymentId > 0) {
                            OrderDAO orderDAO = new OrderDAO();
                            orderDAO.insert(order);
                            System.out.println("\n Order saved successfully!");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error saving to database: " + e.getMessage());
                    }
                } else {
                    System.out.println("Payment failed. Order cancelled.");
                }

            } else if (choice == 2) {
                // Process Dine-In Order
                System.out.println("\n=== PROCESS DINE-IN ORDER ===");
                System.out.print("Enter customer ID  ");
                String custId = scanner.nextLine();

                Customer customer = null;
                if (custId.equals("0")) {
                    System.out.print("Customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Customer phone: ");
                    String phone = scanner.nextLine();
                    Address tempAddr = new Address(0, "Walk-in", true);
                    customer = new Customer("guest", false, tempAddr, name, "guest@walk-in.com", phone);
                } else {
                    try {
                        CustomerDAO dao = new CustomerDAO();
                        customer = dao.findById(custId);
                    } catch (SQLException e) {
                        System.err.println("Error loading customer: " + e.getMessage());
                    }
                }

                if (customer == null) {
                    System.out.println("Customer not found!");
                    continue;
                }

                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
                if (items.isEmpty()) {
                    System.out.println("No items selected!");
                    continue;
                }

                Order order = cashier.processWalkInOrder(customer, items, tables, scanner);

                if (order != null) {
                    // Update the table status in the database immediately after assignment
                    try {
                        TableDAO.update(order.getTable());
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not update table status in database: " + e.getMessage());
                    }

                    cashier.printReceipt(order);

                    // Process payment
                    System.out.println("\n=== PAYMENT ===");
                    System.out.println("Total Amount: EGP " + String.format("%.2f", order.getTotal()));
                    System.out.println("Select Payment Method:");
                    System.out.println("1. Cash");
                    System.out.println("2. Credit Card");
                    System.out.println("3. Debit Card");
                    System.out.println("4. Mobile Wallet");
                    System.out.print("Choice: ");

                    int paymentChoice = -1;
                    try {
                        paymentChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                        scanner.nextLine();
                        continue;
                    }

                    Payment.PaymentMethod paymentMethod;
                    switch (paymentChoice) {
                        case 1 -> paymentMethod = Payment.PaymentMethod.CASH;
                        case 2 -> paymentMethod = Payment.PaymentMethod.CREDIT_CARD;
                        case 3 -> paymentMethod = Payment.PaymentMethod.DEBIT_CARD;
                        case 4 -> paymentMethod = Payment.PaymentMethod.MOBILE_WALLET;
                        default -> {
                            System.out.println("Invalid choice! Using Cash as default.");
                            paymentMethod = Payment.PaymentMethod.CASH;
                        }
                    }

                    boolean paymentSuccess = cashier.acceptPayment(order, order.getTotal(), paymentMethod);

                    if (paymentSuccess) {
                        // Save payment and order to database
                        try {
                            PaymentDAO paymentDAO = new PaymentDAO();
                            int paymentId = paymentDAO.insert(order.getPayment());
                            if (paymentId > 0) {
                                OrderDAO orderDAO = new OrderDAO();
                                orderDAO.insert(order);
                                System.out.println("\n Order saved successfully!");
                            }
                        } catch (SQLException e) {
                            System.err.println("Error saving to database: " + e.getMessage());
                        }

                        System.out.print("\nHas customer finished? Release table now? (y/n): ");
                        String release = scanner.nextLine();
                        if (release.equalsIgnoreCase("y")) {
                            order.getTable().releaseTable();
                            try {
                                TableDAO.update(order.getTable());
                                System.out.println(" Table released successfully!");
                            } catch (SQLException e) {
                                System.err.println("Error updating table: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Payment failed. Order cancelled.");
                        // Release the table if payment failed
                        if (order.getTable() != null) {
                            order.getTable().releaseTable();
                        }
                    }
                }

            } else if (choice == 3) {
                // View All Tables
                System.out.println("\n========== ALL TABLES ==========");
                tables = TableDAO.listAllTables();
                for (Table table : tables) {
                    System.out.println(table);
                }

            } else if (choice == 4) {
                // Release Table
                System.out.println("\n========== RELEASE TABLE ==========");
                try {
                    tables = TableDAO.listAllTables();
                    ArrayList<Table> occupied = new ArrayList<>();
                    for (Table table : tables) {
                        if (!table.isAvailable()) {
                            occupied.add(table);
                        }
                    }

                    if (occupied.isEmpty()) {
                        System.out.println("All tables are available!");
                        continue;
                    }

                    System.out.println("Occupied Tables:");
                    for (int i = 0; i < occupied.size(); i++) {
                        System.out.println((i + 1) + ". " + occupied.get(i));
                    }

                    System.out.print("\nSelect table to release (0 to cancel): ");
                    int tChoice = -1;
                    try {
                        tChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                        scanner.nextLine();
                        continue;
                    }

                    if (tChoice > 0 && tChoice <= occupied.size()) {
                        Table tableToRelease = occupied.get(tChoice - 1);
                        tableToRelease.releaseTable();
                        TableDAO.update(tableToRelease);
                        System.out.println("Table " + tableToRelease.getTableNumber() + " released successfully!");
                    }
                } catch (SQLException e) {
                    System.err.println("Error: " + e.getMessage());
                }

            } else if (choice == 5) {
                // View Profile
                System.out.println(cashier.getDetails());

            } else if (choice == 6) {
                // View Menu
                System.out.println("\n========== MENU ==========");
                for (MenuItem item : menuItems) {
                    System.out.println(item);
                }

            } else if (choice == 7) {
                // Logout
                System.out.println("Logged out successfully!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleDeliveryMenu(Delivery delivery, Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== DELIVERY MENU ==========");
            System.out.println("Hello, " + delivery.getName() + "!");
            System.out.println("Status: " + delivery.getStatus());
            System.out.println("Available: " + (delivery.isAvailable() ? "Yes" : "No"));
            System.out.println("====================================");
            System.out.println("1. View Current Delivery");
            System.out.println("2. Pickup Order");
            System.out.println("3. Start Delivery");
            System.out.println("4. Complete Delivery");
            System.out.println("5. Toggle Availability");
            System.out.println("6. View Statistics");
            System.out.println("7. View Profile");
            System.out.println("8. View All Delivery Zones");
            System.out.println("9. Logout");
            System.out.println("====================================");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                // View Current Delivery
                System.out.println(delivery.getCurrentDeliveryInfo());

            } else if (choice == 2) {
                // Pickup Order
                if (delivery.getCurrentOrder() == null) {
                    System.out.println("No order assigned to you yet.");
                } else {
                    delivery.pickupOrder();
                    // Update delivery person in database
                    try {
                        DeliveryDAO dao = new DeliveryDAO();
                        dao.update(delivery);
                        System.out.println(" Status updated in database.");
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not update status in database: " + e.getMessage());
                    }
                }

            } else if (choice == 3) {
                // Start Delivery
                if (delivery.getCurrentOrder() == null) {
                    System.out.println("No order assigned to you yet.");
                } else {
                    delivery.startDelivery();
                    // Update delivery person in database
                    try {
                        DeliveryDAO dao = new DeliveryDAO();
                        dao.update(delivery);
                        System.out.println(" Status updated in database.");
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not update status in database: " + e.getMessage());
                    }
                }

            } else if (choice == 4) {
                // Complete Delivery
                if (delivery.getCurrentOrder() == null) {
                    System.out.println("No order assigned to you yet.");
                } else {
                    Order completedOrder = delivery.getCurrentOrder();
                    delivery.completeDelivery();

                    // Update delivery person and order in database
                    try {
                        DeliveryDAO deliveryDAO = new DeliveryDAO();
                        deliveryDAO.update(delivery);

                        // Update order status to DELIVERED
                        OrderDAO orderDAO = new OrderDAO();
                        orderDAO.insert(completedOrder); // This will update since we use ON DUPLICATE KEY UPDATE

                        System.out.println(" Delivery completed and saved to database.");
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not update database: " + e.getMessage());
                    }
                }

            } else if (choice == 5) {
                // Toggle Availability
                System.out.println("\nCurrent Status: " + (delivery.isAvailable() ? "AVAILABLE" : "UNAVAILABLE"));
                System.out.print("Do you want to go " + (delivery.isAvailable() ? "OFFLINE" : "ONLINE") + "? (y/n): ");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y")) {
                    delivery.setAvailable(!delivery.isAvailable());

                    // Update in database
                    try {
                        DeliveryDAO dao = new DeliveryDAO();
                        dao.update(delivery);
                        System.out.println(" Availability updated in database.");
                    } catch (SQLException e) {
                        System.err.println("Warning: Could not update availability in database: " + e.getMessage());
                    }
                }

            } else if (choice == 6) {
                // View Statistics
                System.out.println(delivery.getStatistics());

            } else if (choice == 7) {
                // View Profile
                System.out.println(delivery.getDetails());

            } else if (choice == 8) {
                // View All Delivery Zones
                Delivery.displayAllZones();

            } else if (choice == 9) {
                // Logout
                System.out.println("Logged out successfully!");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}
