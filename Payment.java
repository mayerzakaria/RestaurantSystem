package restaurantsystem1;
import java.util.*;
import java.io.Serializable;

    enum PaymentMethod {
        CASH,
        DEBIT_CARD,
        CREDIT_CARD,
        MOBILE_WALLET
    }



public class Payment implements Serializable {
    
    
     enum PaymentMethod {
        CASH,
        DEBIT_CARD,
        CREDIT_CARD,
        MOBILE_WALLET
    }
    
    
    private static final long serialVersionUID = 1L;

 private static final Scanner scanner = new Scanner(System.in);
    private double amount;
    private PaymentMethod paymentMethod;
    private Status status;
    private int orderId;

    // Constructor
    public Payment(double amount, PaymentMethod paymentMethod, int orderId) {
        setAmount(amount);
        setPaymentMethod(paymentMethod);
        this.status = Status.PENDING;
        setOrderId(orderId);

    }

    // Getters and Setters
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    // Validate payment against the order total
    public boolean validatePayment(double orderTotal) {
        if (amount < orderTotal) {
            System.out.println("Insufficient payment! Required: EGP " + orderTotal + 
                               ", Provided: EGP " + amount);
            return false;
        }
        System.out.println("Payment validated successfully.");
        return true;
    }

    // Process the payment
    public boolean processPayment(double orderTotal) {
        if (validatePayment(orderTotal)) {
            status = Status.COMPLETE;
            System.out.println("Payment of EGP " + amount + " via " + paymentMethod + " completed successfully!");
            if (amount > orderTotal) {
                double change = amount - orderTotal;
                System.out.println("Change: EGP " + String.format("%.2f", change));
            }
            return true;
        } else {
            status = Status.FAILED;
            System.out.println("Payment processing failed!");
            return false;
        }
    }
     public static void processPayment(Order order) {
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

         Payment payment = new Payment(amount, method, order.getOrderId());
    boolean success = payment.processPayment(order.getTotal());

    if (success) {
        order.setPayment(payment);
        order.updateStatus(Status.COMPLETE);
    } else {
        order.updateStatus(Status.FAILED);
    }

    System.out.println(payment.getReceipt());
    }

    // Get a formatted receipt
    public String getReceipt() {
        return "\n" + "=".repeat(40) +
               "\n           PAYMENT RECEIPT" +
               "\n" + "=".repeat(40) +
               "\nOrder ID: " + orderId +
               "\nAmount Paid: EGP " + String.format("%.2f", amount) +
               "\nPayment Method: " + paymentMethod +
               "\nStatus: " + status +
               "\n" + "=".repeat(40);
    }

    @Override
    public String toString() {
        return "Payment [Order #" + orderId + 
               ", Amount: EGP " + String.format("%.2f", amount) + 
               ", Method: " + paymentMethod + 
               ", Status: " + status + "]";
    }
}
