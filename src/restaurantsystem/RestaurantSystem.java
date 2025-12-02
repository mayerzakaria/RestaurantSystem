package restaurantsystem;
//
//import java.util.*;
//
//
//
//import util.DB;
//import java.sql.Connection;
//import java.time.LocalDateTime;
//import java.util.ArrayList; 
//public class RestaurantSystem 
//{
//   
//    
//    private static void initializeSystem(Menu menu, ArrayList<Table> tables,ArrayList<Cashier> cashiers, ArrayList<Delivery> deliveryPersons) {
//        menu.addItem(new MenuItem("Burger", "Beef burger with cheese", 80.0, "Main", true));
//        menu.addItem(new MenuItem("Pizza", "Margherita pizza", 120.0, "Main", true));
//        menu.addItem(new MenuItem("Pasta", "Creamy pasta with chicken", 95.0, "Main", true));
//        menu.addItem(new MenuItem("Salad", "Fresh green salad", 45.0, "Appetizer", true));
//        menu.addItem(new MenuItem("Cola", "Soft drink", 20.0, "Beverage", true));
//        menu.addItem(new MenuItem("Juice", "Fresh orange juice", 30.0, "Beverage", true));
//        
//        for (int i = 1; i <= 3; i++) {
//            tables.add(new Table(i, 2, Table.TableStatus.AVAILABLE));
//        }
//        for (int i = 4; i <= 6; i++) {
//            tables.add(new Table(i, 4, Table.TableStatus.AVAILABLE));
//        }
//        for (int i = 7; i <= 10; i++) {
//            tables.add(new Table(i, 6, Table.TableStatus.AVAILABLE));
//        }
//        
//        cashiers.add(new Cashier("Ahmed Ali", "ahmed@restaurant.com", "0123456789", 
//                                "cash123", 5000, "Morning"));
//        cashiers.add(new Cashier("Sara Mohamed", "sara@restaurant.com", "0111222333", 
//                                "cash456", 5000, "Evening"));
//        
//        deliveryPersons.add(new Delivery("Mahmoud Hassan", "mahmoud@restaurant.com", 
//                                         "0100111222", "delivery123"));
//        deliveryPersons.add(new Delivery("Ali Ahmed", "ali@restaurant.com", 
//                                         "0122333444", "delivery456"));
//        
//        System.out.println("System initialized successfully!");
//        System.out.println("\nSample Logins:");
//        System.out.println("Cashiers:");
//        for (Cashier c : cashiers) {
//            System.out.println("  - ID: " + c.getId() + " | Password: " + c.getPassword());
//        }
//        System.out.println("Delivery Persons:");
//        for (Delivery d : deliveryPersons) {
//            System.out.println("  - ID: " + d.getId() + " | Password: " + d.getPassword());
//        }
//    }
//    
//    private static void editOrder(Order order, Menu menu, Customer customer, Scanner scanner) {
//        boolean editing = true;
//        
//        while (editing) {
//            System.out.println("\n========== EDIT ORDER ==========");
//            System.out.println("Current Total: EGP " + String.format("%.2f", order.getTotal()));
//            
//            System.out.println("\nCurrent Items:");
//            int index = 1;
//            for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
//                System.out.println(index + ". " + entry.getKey().getName() + 
//                                 " x" + entry.getValue() + 
//                                 " - EGP " + String.format("%.2f", 
//                                 entry.getKey().getPrice() * entry.getValue()));
//                index++;
//            }
//            
//            System.out.println("\n1. Add Item");
//            System.out.println("2. Remove Item");
//            System.out.println("3. Update Quantity");
//            System.out.println("4. Done Editing");
//            System.out.println("================================");
//            System.out.print("Choose an option: ");
//            
//            int choice = -1;
//            try {
//                choice = scanner.nextInt();
//                scanner.nextLine();
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input!");
//                scanner.nextLine();
//                continue;
//            }
//            
//            if (choice == 1) {
//                System.out.println("\n=== ADD ITEMS ===");
//                Map<MenuItem, Integer> newItems = menu.selectMenuItems(scanner);
//                
//                if (!newItems.isEmpty()) {
//                    for (Map.Entry<MenuItem, Integer> entry : newItems.entrySet()) {
//                        order.addItem(entry.getKey(), entry.getValue());
//                    }
//                    order.calculateSubtotal();
//                    order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                    order.calculateTotal();
//                    System.out.println("\nItems added successfully!");
//                }
//                
//            } else if (choice == 2) {
//                System.out.println("\n=== REMOVE ITEM ===");
//                
//                if (order.getItems().isEmpty()) {
//                    System.out.println("No items in order!");
//                    continue;
//                }
//                
//                ArrayList<MenuItem> itemList = new ArrayList<>(order.getItems().keySet());
//                
//                System.out.println("\nSelect item to remove:");
//                for (int i = 0; i < itemList.size(); i++) {
//                    MenuItem item = itemList.get(i);
//                    int qty = order.getItems().get(item);
//                    System.out.println((i + 1) + ". " + item.getName() + " x" + qty);
//                }
//                
//                System.out.print("\nItem number (0 to cancel): ");
//                int itemChoice = -1;
//                try {
//                    itemChoice = scanner.nextInt();
//                    scanner.nextLine();
//                } catch (InputMismatchException e) {
//                    System.out.println("Invalid input!");
//                    scanner.nextLine();
//                    continue;
//                }
//                
//                if (itemChoice > 0 && itemChoice <= itemList.size()) {
//                    order.removeItem(itemList.get(itemChoice - 1));
//                    order.calculateSubtotal();
//                    order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                    order.calculateTotal();
//                } else if (itemChoice != 0) {
//                    System.out.println("Invalid selection!");
//                }
//                
//            } else if (choice == 3) {
//                System.out.println("\n=== UPDATE QUANTITY ===");
//                
//                if (order.getItems().isEmpty()) {
//                    System.out.println("No items in order!");
//                    continue;
//                }
//                
//                ArrayList<MenuItem> itemList = new ArrayList<>(order.getItems().keySet());
//                
//                System.out.println("\nSelect item to update:");
//                for (int i = 0; i < itemList.size(); i++) {
//                    MenuItem item = itemList.get(i);
//                    int qty = order.getItems().get(item);
//                    System.out.println((i + 1) + ". " + item.getName() + " (Current: x" + qty + ")");
//                }
//                
//                System.out.print("\nItem number (0 to cancel): ");
//                int itemChoice = -1;
//                try {
//                    itemChoice = scanner.nextInt();
//                    scanner.nextLine();
//                } catch (InputMismatchException e) {
//                    System.out.println("Invalid input!");
//                    scanner.nextLine();
//                    continue;
//                }
//                
//                if (itemChoice > 0 && itemChoice <= itemList.size()) {
//                    System.out.print("Enter new quantity: ");
//                    int newQty = -1;
//                    try {
//                        newQty = scanner.nextInt();
//                        scanner.nextLine();
//                    } catch (InputMismatchException e) {
//                        System.out.println("Invalid input!");
//                        scanner.nextLine();
//                        continue;
//                    }
//                    
//                    if (newQty > 0) {
//                        order.updateQuantity(itemList.get(itemChoice - 1), newQty);
//                        order.calculateSubtotal();
//                        order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                        order.calculateTotal();
//                    } else {
//                        System.out.println("Quantity must be greater than 0!");
//                    }
//                } else if (itemChoice != 0) {
//                    System.out.println("Invalid selection!");
//                }
//                
//            } else if (choice == 4) {
//                if (order.getItems().isEmpty()) {
//                    System.out.println("\nWarning: Order is empty!");
//                    System.out.print("Continue anyway? (y/n): ");
//                    String confirm = scanner.nextLine();
//                    if (confirm.equalsIgnoreCase("y")) {
//                        editing = false;
//                    }
//                } else {
//                    System.out.println("\nOrder editing complete!");
//                    editing = false;
//                }
//            } else {
//                System.out.println("Invalid choice!");
//            }
//        }
//    }
//    
//    private static void processPayment(Order order, Scanner scanner) {
//        System.out.println("\n--- PAYMENT ---");
//        System.out.println("Total: EGP " + order.getTotal());
//        System.out.println("Select payment method:");
//        System.out.println("1. Cash");
//        System.out.println("2. Credit Card");
//        System.out.println("3. Debit Card");
//        System.out.println("4. Mobile Wallet");
//        System.out.print("Choice: ");
//        
//        int payChoice = -1;
//        try {
//            payChoice = scanner.nextInt();
//            scanner.nextLine();
//        } catch (InputMismatchException e) {
//            scanner.nextLine();
//            payChoice = 1;
//        }
//        
//        Payment.PaymentMethod method = switch (payChoice) {
//            case 2 -> Payment.PaymentMethod.CREDIT_CARD;
//            case 3 -> Payment.PaymentMethod.DEBIT_CARD;
//            case 4 -> Payment.PaymentMethod.MOBILE_WALLET;
//            default -> Payment.PaymentMethod.CASH;
//        };
//        
//        System.out.print("Enter payment amount: EGP ");
//        double amount = order.getTotal();
//        try {
//            amount = scanner.nextDouble();
//            scanner.nextLine();
//        } catch (InputMismatchException e) {
//            scanner.nextLine();
//        }
//        
//        Payment payment = new Payment(amount, method, order.getOrderId());
//        if (payment.processPayment(order.getTotal())) {
//            order.setPayment(payment);
//            order.updateStatus(Status.COMPLETE);
//        } else {
//            order.updateStatus(Status.FAILED);
//        }
//    }
//    
//    public static void main(String[] args) {
//        
//       
//        
//        
//        ArrayList<Customer> customers = new ArrayList<>();
//        ArrayList<Cashier> cashiers = new ArrayList<>();
//        ArrayList<Delivery> deliveryPersons = new ArrayList<>();
//        Menu menu = new Menu();
//        ArrayList<Table> tables = new ArrayList<>();
//        ArrayList<Order> orders = new ArrayList<>();
//        Scanner scanner = new Scanner(System.in);
//        
//        System.out.println("================================");
//        System.out.println("   WELCOME TO RESTAURANT SYSTEM");
//        System.out.println("================================\n");
//        
//        if (DataManager.dataExists()) {
//            DataManager.loadAllData(customers, cashiers, deliveryPersons, orders, menu, tables);
//        } else {
//            initializeSystem(menu, tables, cashiers, deliveryPersons);
//        }
//        
//        while (true) {
//            System.out.println("\n========== MAIN MENU ==========");
//            System.out.println("1. Register (New Customer)");
//            System.out.println("2. Login");
//            System.out.println("3. View Menu");
//            System.out.println("4. Exit");
//            System.out.println("================================");
//            System.out.print("Choose an option: ");
//            
//            int mainChoice = -1;
//            try {
//                mainChoice = scanner.nextInt();
//                scanner.nextLine();
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input!");
//                scanner.nextLine();
//                continue;
//            }
//            
//            if (mainChoice == 1) {
//                Customer.registerCustomer(customers, scanner);
//                DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                
//            } else if (mainChoice == 2) {
//                System.out.println("\n=== LOGIN ===");
//                System.out.print("Enter your ID (CUST### / CH### / DP###): ");
//                String id = scanner.nextLine();
//                System.out.print("Enter password: ");
//                String password = scanner.nextLine();
//                
//                if (id.toUpperCase().startsWith("CUST")) {
//                    Customer customer = Customer.findCustomer(id, customers);
//                    if (customer != null && customer.login(id, password)) {
//                        boolean customerLoggedIn = true;
//                        while (customerLoggedIn) {
//                            System.out.println("\n========== CUSTOMER MENU ==========");
//                            System.out.println("Hello, " + customer.getName() + "!");
//                            System.out.println("====================================");
//                            System.out.println("1. Online Delivery");
//                            System.out.println("2. View Profile");
//                            System.out.println("3. Subscribe to Elite");
//                            System.out.println("4. Logout");
//                            System.out.println("====================================");
//                            System.out.print("Choose an option: ");
//                            
//                            int custChoice = -1;
//                            try {
//                                custChoice = scanner.nextInt();
//                                scanner.nextLine();
//                            } catch (InputMismatchException e) {
//                                System.out.println("Invalid input!");
//                                scanner.nextLine();
//                                continue;
//                            }
//                            
//                            if (custChoice == 1) {
//                                System.out.println("\n=== ONLINE DELIVERY ORDER ===");
//                                System.out.println("Delivery Address: " + customer.getAddress().getFullAddress());
//                                System.out.print("Use this address? (y/n): ");
//                                String confirm = scanner.nextLine();
//                                
//                                Address deliveryAddress = customer.getAddress();
//                                if (confirm.equalsIgnoreCase("n")) {
//                                    System.out.print("Enter new address: ");
//                                    String newAddr = scanner.nextLine();
//                                    deliveryAddress = new Address(2, newAddr, false);
//                                }
//                                
//                                String testZone = Delivery.determineZone(deliveryAddress);
//                                if (testZone == null) {
//                                    System.out.println("\n================================");
//                                    System.out.println("      ORDER CANCELLED");
//                                    System.out.println("================================");
//                                    System.out.println("Delivery is not available for this address.");
//                                    System.out.println("Please try a different address or use another order type.");
//                                    System.out.println("================================\n");
//                                    continue;
//                                }
//                                
//                                System.out.println("\nDelivery Zone: " + Delivery.getZoneName(testZone));
//                                System.out.println("Delivery Fee: EGP " + String.format("%.2f", Delivery.getBaseFee(testZone)));
//                                System.out.println("Estimated Time: " + Delivery.getDeliveryTimeMinutes(testZone) + " minutes");
//                                
//                                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
//                                if (items.isEmpty()) {
//                                    System.out.println("No items selected!");
//                                    continue;
//                                }
//                                
//                                Order order = new Order(customer.getCustomerId(), items, 
//                                                       Systemmode.ONLINE_DELIVERY, deliveryAddress);
//                                
//                                if (order.getStatus() == Status.CANCELLED) {
//                                    System.out.println("\n================================");
//                                    System.out.println("      ORDER CANCELLED");
//                                    System.out.println("================================");
//                                    System.out.println("Delivery is not available for this address.");
//                                    System.out.println("================================\n");
//                                    continue;
//                                }
//                                
//                                order.calculateSubtotal();
//                                order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                                order.calculateTotal();
//                                System.out.println(order.getOrderSummary());
//                                
//                                System.out.print("\nWould you like to edit this order before payment? (y/n): ");
//                                String editChoice = scanner.nextLine();
//                                
//                                if (editChoice.equalsIgnoreCase("y")) {
//                                    editOrder(order, menu, customer, scanner);
//                                    System.out.println("\n=== UPDATED ORDER ===");
//                                    System.out.println(order.getOrderSummary());
//                                }
//                                
//                                processPayment(order, scanner);
//                                
//                                if (order.getStatus() == Status.COMPLETE) {
//                                    orders.add(order);
//                                    
//                                    Delivery availableDelivery = null;
//                                    for (Delivery d : deliveryPersons) {
//                                        if (d.isAvailable()) {
//                                            availableDelivery = d;
//                                            break;
//                                        }
//                                    }
//                                    
//                                    if (availableDelivery != null) {
//                                        if (availableDelivery.acceptOrder(order)) {
//                                            order.assignDeliveryPerson(availableDelivery);
//                                            System.out.println("\nOrder will be delivered by: " + availableDelivery.getName());
//                                            System.out.println("Phone: " + availableDelivery.getPhoneNumber());
//                                        }
//                                    } else {
//                                        System.out.println("\nNo delivery person available at the moment.");
//                                        System.out.println("Order will be assigned soon.");
//                                    }
//                                    
//                                    System.out.println("\nOrder placed successfully!");
//                                    System.out.println("Order ID: " + order.getOrderId());
//                                    DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                                }
//                                
//                            } else if (custChoice == 2) {
//                                System.out.println(customer.getDetails());
//                            } else if (custChoice == 3) {
//                                customer.subscribeElite(scanner);
//                            } else if (custChoice == 4) {
//                                System.out.println("Logged out!");
//                                customerLoggedIn = false;
//                            } else {
//                                System.out.println("Invalid choice!");
//                            }
//                        }
//                        DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                    } else {
//                        System.out.println("Login failed! Invalid credentials.");
//                    }
//                    
//                } else if (id.toUpperCase().startsWith("CH")) {
//                    Cashier cashier = Cashier.findCashier(id, cashiers);
//                    if (cashier != null && cashier.login(id, password)) {
//                        boolean cashierLoggedIn = true;
//                        while (cashierLoggedIn) {
//                            System.out.println("\n========== CASHIER MENU ==========");
//                            System.out.println("Hello, " + cashier.getName() + "!");
//                            System.out.println("===================================");
//                            System.out.println("1. Process Takeaway Order");
//                            System.out.println("2. Process Dine-In Order");
//                            System.out.println("3. View All Tables");
//                            System.out.println("4. Release Table");
//                            System.out.println("5. View Profile");
//                            System.out.println("6. View Menu");
//                            System.out.println("7. Logout");
//                            System.out.println("===================================");
//                            System.out.print("Choose an option: ");
//                            
//                            int cashChoice = -1;
//                            try {
//                                cashChoice = scanner.nextInt();
//                                scanner.nextLine();
//                            } catch (InputMismatchException e) {
//                                System.out.println("Invalid input!");
//                                scanner.nextLine();
//                                continue;
//                            }
//                            
//                            if (cashChoice == 1) {
//                                System.out.println("\n=== PROCESS TAKEAWAY ORDER ===");
//                                System.out.print("Enter customer ID (or 0 for walk-in): ");
//                                String custId = scanner.nextLine();
//                                
//                                Customer customer = null;
//                                if (custId.equals("0")) {
//                                    System.out.print("Customer name: ");
//                                    String name = scanner.nextLine();
//                                    Address tempAddr = new Address(0, "Walk-in", true);
//                                    customer = new Customer("guest", false, tempAddr, name, "n/a", "n/a");
//                                } else {
//                                    customer = Customer.findCustomer(custId, customers);
//                                }
//                                
//                                if (customer == null) {
//                                    System.out.println("Customer not found!");
//                                    continue;
//                                }
//                                
//                                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
//                                if (items.isEmpty()) {
//                                    System.out.println("No items selected!");
//                                    continue;
//                                }
//                                
//                                Order order = cashier.processTakeawayOrder(customer, items);
//                                
//                                System.out.print("\nWould you like to edit this order before payment? (y/n): ");
//                                String editChoice = scanner.nextLine();
//                                
//                                if (editChoice.equalsIgnoreCase("y")) {
//                                    editOrder(order, menu, customer, scanner);
//                                    order.calculateSubtotal();
//                                    order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                                    order.calculateTotal();
//                                    System.out.println("\n=== UPDATED ORDER ===");
//                                    cashier.printReceipt(order);
//                                } else {
//                                    cashier.printReceipt(order);
//                                }
//                                
//                                orders.add(order);
//                                processPayment(order, scanner);
//                                cashier.acceptPayment(order, order.getTotal(), order.getPayment().getPaymentMethod());
//                                DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                                
//                            } else if (cashChoice == 2) {
//                                System.out.println("\n=== PROCESS DINE-IN ORDER ===");
//                                System.out.print("Enter customer ID (or 0 for walk-in): ");
//                                String custId = scanner.nextLine();
//                                
//                                Customer customer = null;
//                                if (custId.equals("0")) {
//                                    System.out.print("Customer name: ");
//                                    String name = scanner.nextLine();
//                                    Address tempAddr = new Address(0, "Walk-in", true);
//                                    customer = new Customer("guest", false, tempAddr, name, "n/a", "n/a");
//                                } else {
//                                    customer = Customer.findCustomer(custId, customers);
//                                }
//                                
//                                if (customer == null) {
//                                    System.out.println("Customer not found!");
//                                    continue;
//                                }
//                                
//                                Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
//                                if (items.isEmpty()) {
//                                    System.out.println("No items selected!");
//                                    continue;
//                                }
//                                
//                                Order order = cashier.processWalkInOrder(customer, items, tables, scanner);
//                                
//                                if (order != null) {
//                                    System.out.print("\nWould you like to edit this order before payment? (y/n): ");
//                                    String editChoice = scanner.nextLine();
//                                    
//                                    if (editChoice.equalsIgnoreCase("y")) {
//                                        editOrder(order, menu, customer, scanner);
//                                        order.calculateSubtotal();
//                                        order.applyEliteDiscount(customer.isEliteCustomer(), customer.isSubscriptionActive());
//                                        order.calculateTotal();
//                                        System.out.println("\n=== UPDATED ORDER ===");
//                                        cashier.printReceipt(order);
//                                    } else {
//                                        cashier.printReceipt(order);
//                                    }
//                                    
//                                    orders.add(order);
//                                    processPayment(order, scanner);
//                                    cashier.acceptPayment(order, order.getTotal(), order.getPayment().getPaymentMethod());
//                                    
//                                    System.out.print("\nHas customer finished? Release table now? (y/n): ");
//                                    String release = scanner.nextLine();
//                                    if (release.equalsIgnoreCase("y")) {
//                                        order.getTable().releaseTable();
//                                    }
//                                    
//                                    DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                                }
//                                
//                            } else if (cashChoice == 3) {
//                                System.out.println("\n========== ALL TABLES ==========");
//                                for (Table table : tables) {
//                                    System.out.println(table);
//                                }
//                            } else if (cashChoice == 4) {
//                                System.out.println("\n========== RELEASE TABLE ==========");
//                                ArrayList<Table> occupied = new ArrayList<>();
//                                for (Table table : tables) {
//                                    if (!table.isAvailable()) {
//                                        occupied.add(table);
//                                    }
//                                }
//                                
//                                if (occupied.isEmpty()) {
//                                    System.out.println("All tables are available!");
//                                    continue;
//                                }
//                                
//                                System.out.println("Occupied Tables:");
//                                for (int i = 0; i < occupied.size(); i++) {
//                                    System.out.println((i + 1) + ". " + occupied.get(i));
//                                }
//                                
//                                System.out.print("\nSelect table to release (0 to cancel): ");
//                                int tChoice = -1;
//                                try {
//                                    tChoice = scanner.nextInt();
//                                    scanner.nextLine();
//                                } catch (InputMismatchException e) {
//                                    scanner.nextLine();
//                                    continue;
//                                }
//                                
//                                if (tChoice > 0 && tChoice <= occupied.size()) {
//                                    occupied.get(tChoice - 1).releaseTable();
//                                    DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                                }
//                            } else if (cashChoice == 5) {
//                                System.out.println(cashier.getDetails());
//                            } else if (cashChoice == 6) {
//                                menu.displayMenu();
//                            } else if (cashChoice == 7) {
//                                System.out.println("Logged out successfully!");
//                                cashierLoggedIn = false;
//                            } else {
//                                System.out.println("Invalid choice!");
//                            }
//                        }
//                        DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                    } else {
//                        System.out.println("Login failed! Invalid credentials.");
//                    }
//                    
//                } else if (id.toUpperCase().startsWith("DP")) {
//                    Delivery delivery = null;
//                    for (Delivery d : deliveryPersons) {
//                        if (d.getId().equalsIgnoreCase(id)) {
//                            delivery = d;
//                            break;
//                        }
//                    }
//                    
//                    if (delivery != null && delivery.login(id, password)) {
//                        boolean deliveryLoggedIn = true;
//                        while (deliveryLoggedIn) {
//                            System.out.println("\n========== DELIVERY MENU ==========");
//                            System.out.println("Hello, " + delivery.getName() + "!");
//                            System.out.println("====================================");
//                            System.out.println("1. View Current Order");
//                            System.out.println("2. Pickup Order");
//                            System.out.println("3. Start Delivery");
//                            System.out.println("4. Complete Delivery");
//                            System.out.println("5. View Statistics");
//                            System.out.println("6. View Profile");
//                            System.out.println("7. Logout");
//                            System.out.println("====================================");
//                            System.out.print("Choose an option: ");
//                            
//                            int delChoice = -1;
//                            try {
//                                delChoice = scanner.nextInt();
//                                scanner.nextLine();
//                            } catch (InputMismatchException e) {
//                                System.out.println("Invalid input!");
//                                scanner.nextLine();
//                                continue;
//                            }
//                            
//                            if (delChoice == 1) {
//                                System.out.println(delivery.getCurrentDeliveryInfo());
//                            } else if (delChoice == 2) {
//                                delivery.pickupOrder();
//                                DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                            } else if (delChoice == 3) {
//                                delivery.startDelivery();
//                                DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                            } else if (delChoice == 4) {
//                                delivery.completeDelivery();
//                                DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                            } else if (delChoice == 5) {
//                                System.out.println(delivery.getStatistics());
//                            } else if (delChoice == 6) {
//                                System.out.println(delivery.getDetails());
//                            } else if (delChoice == 7) {
//                                System.out.println("Logged out!");
//                                deliveryLoggedIn = false;
//                            } else {
//                                System.out.println("Invalid choice!");
//                            }
//                        }
//                        DataManager.saveAllDataSilent(customers, cashiers, deliveryPersons, orders, menu, tables);
//                    } else {
//                        System.out.println("Login failed! Invalid credentials.");
//                    }
//                    
//                } else {
//                    System.out.println("Invalid ID format!");
//                }
//                
//            } else if (mainChoice == 3) {
//                menu.displayMenu();
//            } else if (mainChoice == 4) {
//                DataManager.saveAllData(customers, cashiers, deliveryPersons, orders, menu, tables);
//                System.out.println("Thank you for using our system!");
//                scanner.close();
//                System.exit(0);
//            } else {
//                System.out.println("Invalid choice!");
//            }
//        }
//    }
//}



import java.util.ArrayList;
import java.util.Scanner;
import restaurantsystemdao.TableDAO;

public class RestaurantSystem {
    public static void main(String[] args) {
     // Example cashier IDs that exist in your cashier table
        String cashier1 = "C001";
        String cashier2 = "C002";
        String cashier3 = "C003";

        // Create tables with cashier IDs
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(new Table(1, 4, Table.TableStatus.AVAILABLE, cashier1));
        tables.add(new Table(2, 2, Table.TableStatus.AVAILABLE, cashier2));
        tables.add(new Table(3, 6, Table.TableStatus.AVAILABLE, cashier3));
        tables.add(new Table(4, 4, Table.TableStatus.AVAILABLE, cashier1));
        tables.add(new Table(5, 2, Table.TableStatus.AVAILABLE, cashier2));

        // Insert tables into the database
        for (Table t : tables) {
            TableDAO.insertRestaurantTable(t);
        }

        // List all tables from the database
        System.out.println("\n--- All Tables ---");
        ArrayList<Table> dbTables = TableDAO.listAllTables();
        for (Table t : dbTables) {
            System.out.println(t);
        }

        // Test selecting a table for a party
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nSelect a table for 3 people:");
        Table selected = Table.selectTableForCapacity(dbTables, scanner, 3);
        if (selected != null) {
            System.out.println("Selected table: " + selected);
        }

        scanner.close();
    }
}