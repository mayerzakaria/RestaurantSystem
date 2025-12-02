package restaurantsystem;

import java.io.Serializable;



public class Address implements Serializable 
{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String fullAddress;
    private boolean isDefault;
    private String customerId;
    
   public Address(int id, String fullAddress, boolean isDefault, String customerId) 
   { 
       this.id = id; this.fullAddress = fullAddress; this.isDefault = isDefault; this.customerId = customerId; 
   }
    public Address(int id, String fullAddress, boolean isDefault)
    { 
        this(id, fullAddress, isDefault, null); 
    
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public boolean isDefault() { // prefered 
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    
    
    

    public boolean validateAddress() {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            System.out.println(" Address cannot be empty!");
            return false;
        }
        if (fullAddress.length() < 10) {
            System.out.println(" Address too short! Please provide full address.");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Address ID: " + id + 
               "\nFull Address: " + fullAddress + 
               "\nDefault: " + (isDefault ? "Yes" : "No");
    }
}