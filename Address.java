/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

/**
 *
 * @author Mayer
 */
public class Address 
{
    private int addressid;
    private String fulladdress;
    private boolean isDefault;

    public Address(int addressid, String fulladdress, boolean isDefault)
    {
       setAddressid (addressid) ;
         setFulladdress(fulladdress) ;
         setIsDefault (isDefault);
    }

    public int getAddressid() {
        return addressid;
    }

    public void setAddressid(int addressid) 
    {
        this.addressid = addressid;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault; // if true means he prefered the address (first address) & false ( means the second address )
    }

    @Override
    public String toString() {
        return "Address ID: " + addressid + "\nFull Address: " + fulladdress +  "\nDefault: " + (isDefault ? "Yes" : "No");
    }
    public boolean validateAddress() {
        if (fulladdress == null || fulladdress.trim().isEmpty()) {
            System.out.println("Address cannot be empty!");
            return false;
        }
        return true;
    }
    
    
    
    
}
