/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem;

/**
 *
 * @author Mayer
 */
public class Payment {
    private double amount ;
    private String paymentMethod;
    private String status;
    private Order order;

    //constructor 
    public Payment(double amount , String paymentMethod, Order order ){
        setAmount(amount);
        setPaymentMethod(paymentMethod);
        this.status="pending";
        setOrder(order);
    }
    // Validate payment
     public boolean validatePayment() {
        if (order == null) {
            System.out.println("No order linked to this payment!");
            return false;
        }
        if (this.amount != order.getTotal()) {
            System.out.println("Payment validation failed! Amount mismatch.");
            return false;
        }
        System.out.println("Payment validated successfully.");
        return true;
    
     }
      // Process payment
        public void processPayment() {
        if (validatePayment()) {
            this.status = "Completed";
            System.out.println("Payment of " + amount + " EGP via " + paymentMethod + " completed successfully!");
        } else {
            this.status = "Failed";
            System.out.println("Payment failed!");
        }
    }
        // Generate receipt
    public String getReceipt() {
        return "----- Payment Receipt -----" +
               "\nOrder ID: " + (order != null ? order.getOrderId() : "N/A") +
               "\nAmount Paid: " + amount + " EGP" +
               "\nPayment Method: " + paymentMethod +
               "\nStatus: " + status +
               "\n---------------------------";
    }
    
    
    
    //Getters and setters 
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    
  
}
