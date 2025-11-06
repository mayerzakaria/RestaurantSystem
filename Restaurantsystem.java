package restaurantsystem1;

import java.util.*;

public class Restaurantsystem {

    private static  = new ArrayList<>();
   
    private static Menu menu = new Menu();
    private static ArrayList<Table> tables = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        initializeSystem();
        
        // بداية النظام
        System.out.println("================================");
        System.out.println("   WELCOME TO RESTAURANT SYSTEM");
        System.out.println("================================\n");
        
        // الشاشة الرئيسية
        mainMenu();
    }
    
    private static void initializeSystem() {
        // إضافة عناصر للمينيو
        menu.addItem(new MenuItem("Burger", "Beef burger with cheese", 80.0, "Main", true));
        menu.addItem(new MenuItem("Pizza", "Margherita pizza", 120.0, "Main", true));
        menu.addItem(new MenuItem("Pasta", "Creamy pasta with chicken", 95.0, "Main", true));
        menu.addItem(new MenuItem("Salad", "Fresh green salad", 45.0, "Appetizer", true));
        menu.addItem(new MenuItem("Cola", "Soft drink", 20.0, "Beverage", true));
        menu.addItem(new MenuItem("Juice", "Fresh orange juice", 30.0, "Beverage", true));
        
        // إنشاء طاولات
        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i, 4, TableStatus.AVAILABLE));
        }
        
        // إنشاء cashiers للتجربة
        Systemmanager sm = new Systemmanager(Systemmode.walk_in);
        cashiers.add(new Cashier("Ahmed Ali", "ahmed@restaurant.com", "0123456789", "cash123", 5000, "Morning", sm));
        cashiers.add(new Cashier("Sara Mohamed", "sara@restaurant.com", "0111222333", "cash456", 5000, "Evening", sm));
        
        System.out.println("System initialized successfully!");
        System.out.println("Sample Cashier Logins:");
        for (Cashier c : cashiers) {
            System.out.println("  - ID: " + c.getId() + " | Password: " + c.getPassword());
        }
        System.out.println();
    }
    
    private static void mainMenu() {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register (New Customer)");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Thank you for using our system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    private static void registerCustomer() {
        
        System.out.println("Please remember your credentials for login.");
        // تسجيل دخول تلقائي بعد التسجيل
        customerMenu(customer);
    }
    
    private static void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter your ID (CUST### or CH###): ");
        String id = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        // تحديد نوع المستخدم من الـ ID
        if (id.startsWith("CUST")) {
            // Customer login
            Customer customer = findCustomer(id);
            if (customer != null && customer.login(id, password)) {
                customerMenu(customer);
            } else {
                System.out.println(" Login failed! Invalid credentials.");
            }
        } else if (id.startsWith("CH") || id.startsWith("EMP")) {
            // Cashier login
            Cashier cashier = findCashier(id);
            if (cashier != null && cashier.login(id, password)) {
                cashierMenu(cashier);
            } else {
                System.out.println(" Login failed! Invalid credentials.");
            }
        } else {
            System.out.println(" Invalid ID format!");
        }
    }
    
//    private static void customerMenu(Customer customer) {
//        System.out.println("\n========== CUSTOMER MENU ==========");
//        System.out.println("Hello, " + customer.getName() + "!");
//        System.out.println("Choose order type:");
//        System.out.println("1. Online Delivery");
//        System.out.println("2. Takeaway");
//        System.out.println("3. Dine-In");
//        System.out.println("4. View Profile");
//        System.out.println("5. Subscribe to Elite");
//        System.out.println("6. Logout");
//        System.out.print("Choose an option: ");
//        
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//        
//        switch (choice) {
//            case 1:
//                placeOnlineOrder(customer);
//                break;
//            case 2:
//                placeTakeawayOrder(customer);
//                break;
//            case 3:
//                placeDineInOrder(customer);
//                break;
//            case 4:
//                System.out.println("\n" + customer.getDetails());
//                customerMenu(customer);
//                break;
//            case 5:
//                subscribeElite(customer);
//                customerMenu(customer);
//                break;
//            case 6:
//                System.out.println("Logged out successfully!");
//                return;
//            default:
//                System.out.println("Invalid choice!");
//                customerMenu(customer);
//        }
//    }
    
    private static void cashierMenu(Cashier cashier) {
        System.out.println("\n========== CASHIER MENU ==========");
        System.out.println("Hello, " + cashier.getName() + "!");
        System.out.println("Choose service type:");
        System.out.println("1. Process Takeaway Order");
        System.out.println("2. Process Dine-In Order");
        System.out.println("3. View Available Tables");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                cashierProcessTakeaway(cashier);
                break;
            case 2:
                cashierProcessDineIn(cashier);
                break;
            case 3:
                viewTables();
                cashierMenu(cashier);
                break;
            case 4:
                System.out.println("Logged out successfully!");
                return;
            default:
                System.out.println("Invalid choice!");
                cashierMenu(cashier);
        }
    }
    
//    private static void placeOnlineOrder(Customer customer) {
//        System.out.println("\n=== ONLINE DELIVERY ORDER ===");
//        
//        // تأكيد العنوان
//        System.out.println("Delivery Address: " + customer.getAddress().getFulladdress());
//        System.out.print("Use this address? (y/n): ");
//        String confirm = scanner.nextLine();
//        
//        Address deliveryAddress = customer.getAddress();
//        if (confirm.equalsIgnoreCase("n")) {
//            System.out.print("Enter new delivery address: ");
//            String newAddr = scanner.nextLine();
//            deliveryAddress = new Address(2, newAddr, false);
//        }
//        
//        // اختيار الأصناف
//        Map<MenuItem, Integer> items = selectMenuItems();
//        
//        if (items.isEmpty()) {
//            System.out.println("No items selected!");
//            customerMenu(customer);
//            return;
//        }
//        
//        // إنشاء الطلب
//        Order order = new Order(customer, items, Systemmode.online_delivery, null);
//        order.setDeliveryAddress(deliveryAddress);
//        order.calculateSubtotal();
//        order.applyEliteDiscount();
//        order.calculateTotal();
//        
//        System.out.println(order.getOrderSummary());
//        
//        // الدفع
//        processPayment(order);
//        
//        System.out.println("\n Your order will be delivered to: " + deliveryAddress.getFulladdress());
//        customerMenu(customer);
//    }
//    
//    private static void placeTakeawayOrder(Customer customer) {
//        System.out.println("\n=== TAKEAWAY ORDER ===");
//        
//        // اختيار الأصناف
//        Map<MenuItem, Integer> items = selectMenuItems();
//        
//        if (items.isEmpty()) {
//            System.out.println("No items selected!");
//            customerMenu(customer);
//            return;
//        }
//        
//        // إنشاء الطلب
//        Order order = new Order(customer, items, Systemmode.takeAway, null);
//        order.calculateSubtotal();
//        order.applyEliteDiscount();
//        order.calculateTotal();
//        
//        System.out.println(order.getOrderSummary());
//        
//        // الدفع
//        processPayment(order);
//        
//        System.out.println("\n Your order will be ready for pickup in 15-20 minutes!");
//        customerMenu(customer);
//    }
//    
//    private static void placeDineInOrder(Customer customer) {
//        System.out.println("\n=== DINE-IN ORDER ===");
//        
//        // اختيار طاولة
//        Table selectedTable = selectTable();
//        if (selectedTable == null) {
//            System.out.println("No available tables!");
//            customerMenu(customer);
//            return;
//        }
//        
//        // اختيار الأصناف
//        Map<MenuItem, Integer> items = selectMenuItems();
//        
//        if (items.isEmpty()) {
//            System.out.println("No items selected!");
//            selectedTable.releaseTable();
//            customerMenu(customer);
//            return;
//        }
//        
//        // إنشاء الطلب
//        Order order = new Order(customer, items, Systemmode.walk_in, selectedTable);
//        order.calculateSubtotal();
//        order.applyEliteDiscount();
//        order.calculateTotal();
//        
//        System.out.println(order.getOrderSummary());
//        
//        // الدفع
//        processPayment(order);
//        
//        System.out.println("\n Enjoy your meal at Table " + selectedTable.getTableNumber() + "!");
//        customerMenu(customer);
//    }
//    
    private static void cashierProcessTakeaway(Cashier cashier) {
        System.out.println("\n=== CASHIER: PROCESS TAKEAWAY ===");
        
        System.out.print("Enter customer ID (or 0 for walk-in customer): ");
        String custId = scanner.nextLine();
        
        Customer customer;
        if (custId.equals("0")) {
            // عميل بدون حساب
            System.out.print("Customer name: ");
            String name = scanner.nextLine();
            Address tempAddr = new Address(0, "Walk-in", true);
            customer = new Customer("guest", "guest", false, tempAddr, name, "n/a", "n/a");
        } else {
            customer = findCustomer(custId);
            if (customer == null) {
                System.out.println("Customer not found!");
                cashierMenu(cashier);
                return;
            }
        }
        
        Map<MenuItem, Integer> items = selectMenuItems();
        
        if (items.isEmpty()) {
            cashierMenu(cashier);
            return;
        }
        
        Order order = cashier.processTakeawayOrder(customer, items);
        cashier.printReceipt(order);
        
        processPaymentCashier(order, cashier);
        
        cashierMenu(cashier);
    }
    
    private static void cashierProcessDineIn(Cashier cashier) {
        System.out.println("\n=== CASHIER: PROCESS DINE-IN ===");
        
        System.out.print("Enter customer ID (or 0 for walk-in customer): ");
        String custId = scanner.nextLine();
        
        Customer customer;
        if (custId.equals("0")) {
            System.out.print("Customer name: ");
            String name = scanner.nextLine();
            Address tempAddr = new Address(0, "Walk-in", true);
            customer = new Customer("guest", "guest", false, tempAddr, name, "n/a", "n/a");
        } else {
            customer = findCustomer(custId);
            if (customer == null) {
                System.out.println("Customer not found!");
                cashierMenu(cashier);
                return;
            }
        }
        
        Table table = selectTable();
        if (table == null) {
            System.out.println("No available tables!");
            cashierMenu(cashier);
            return;
        }
        
        Map<MenuItem, Integer> items = selectMenuItems();
        
        if (items.isEmpty()) {
            table.releaseTable();
            cashierMenu(cashier);
            return;
        }
        
        Order order = cashier.processWalkInOrder(customer, items, table);
        cashier.printReceipt(order);
        
        processPaymentCashier(order, cashier);
        
        cashierMenu(cashier);
    }
    
//    private static Map<MenuItem, Integer> selectMenuItems() {
//        Map<MenuItem, Integer> selectedItems = new HashMap<>();
//        
//        System.out.println("\n========== MENU ==========");
//        ArrayList<MenuItem> availableItems = menu.getAllAvialableItems();
//        
//        for (int i = 0; i < availableItems.size(); i++) {
//            MenuItem item = availableItems.get(i);
//            System.out.println((i + 1) + ". " + item.getInfo());
//        }
//        
//        System.out.println("\nSelect items (enter 0 to finish):");
//        
//        while (true) {
//            System.out.print("Item number: ");
//            int itemNum = scanner.nextInt();
//            
//            if (itemNum == 0) break;
//            
//            if (itemNum < 1 || itemNum > availableItems.size()) {
//                System.out.println("Invalid item number!");
//                continue;
//            }
//            
//            System.out.print("Quantity: ");
//            int qty = scanner.nextInt();
//            
//            MenuItem selectedItem = availableItems.get(itemNum - 1);
//            selectedItems.put(selectedItem, qty);
//            
//            System.out.println(" Added: " + selectedItem.getName() + " x" + qty);
//        }
//        
//        scanner.nextLine(); // consume newline
//        return selectedItems;
//    }
    
//    private static Table selectTable() {
//        System.out.println("\n========== AVAILABLE TABLES ==========");
//        ArrayList<Table> availableTables = new ArrayList<>();
//        
//        for (Table table : tables) {
//            if (table.isAvailable()) {
//                availableTables.add(table);
//                System.out.println(availableTables.size() + ". " + table.toString());
//            }
//        }
//        
//        if (availableTables.isEmpty()) {
//            return null;
//        }
//        
//        System.out.print("Select table number: ");
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//        
//        if (choice < 1 || choice > availableTables.size()) {
//            System.out.println("Invalid choice!");
//            return null;
//        }
//        
//        Table selected = availableTables.get(choice - 1);
//        selected.assignTable();
//        return selected;
//    }
    
    private static void viewTables() {
        System.out.println("\n========== ALL TABLES ==========");
        for (Table table : tables) {
            System.out.println(table.toString());
        }
    }
    
//    private static void processPayment(Order order) {
//        System.out.println("\n--- PAYMENT ---");
//        System.out.println("Total: EGP " + order.getTotal());
//        System.out.println("Select payment method:");
//        System.out.println("1. Cash");
//        System.out.println("2. Credit Card");
//        System.out.println("3. Debit Card");
//        System.out.println("4. Mobile Wallet");
//        System.out.print("Choice: ");
//        
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//        
//        PaymentMethod method;
//        switch (choice) {
//            case 1: method = PaymentMethod.CASH; break;
//            case 2: method = PaymentMethod.CREDIT_CARD; break;
//            case 3: method = PaymentMethod.DEBIT_CARD; break;
//            case 4: method = PaymentMethod.MOBILE_WALLET; break;
//            default: method = PaymentMethod.CASH;
//        }
//        
//        System.out.print("Enter payment amount: ");
//        double amount = scanner.nextDouble();
//        scanner.nextLine();
//        
//        Payment payment = new Payment(amount, method, order);
//        payment.processPayment();
//    }
    
    private static void processPaymentCashier(Order order, Cashier cashier) {
        System.out.println("\n--- PAYMENT ---");
        System.out.println("Total: EGP " + order.getTotal());
        System.out.println("Select payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.println("4. Mobile Wallet");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        PaymentMethod method;
        switch (choice) {
            case 1: method = PaymentMethod.CASH; break;
            case 2: method = PaymentMethod.CREDIT_CARD; break;
            case 3: method = PaymentMethod.DEBIT_CARD; break;
            case 4: method = PaymentMethod.MOBILE_WALLET; break;
            default: method = PaymentMethod.CASH;
        }
        
        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        cashier.acceptPayment(order, amount, method);
    }
    
    private static void subscribeElite(Customer customer) {
        System.out.println("\n=== ELITE MEMBERSHIP ===");
        System.out.println("Fee: EGP " + customer.getSubscriptionFee());
        System.out.println("Benefits: 10% discount on all orders");
        System.out.print("Subscribe? (y/n): ");
        String choice = scanner.nextLine();
        
        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Process payment of EGP " + customer.getSubscriptionFee() + "? (y/n): ");
            String confirm = scanner.nextLine();
            customer.subscribeToElite(confirm.equalsIgnoreCase("y"));
        }
    }
//    
//    private static Customer findCustomer(String id) {
//        for (Customer c : customers) {
//            if (c.getCustomerId().equals(id)) {
//                return c;
//            }
//        }
//        return null;
    }
    

