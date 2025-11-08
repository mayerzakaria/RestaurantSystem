/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem1;

import java.io.Serializable;
import java.util.*;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String id;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    

    public Person(String name, String email, String phoneNumber, String password) {
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassword(password);
    }
    
     // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getId() {return id;}
    public void setId(String id) { this.id = id;}
    
    
    

    
  public void updateInfo(String newName, String newEmail, String newPhone) {
        setName(newName);
        setEmail(newEmail);
        setPhoneNumber(newPhone);
        System.out.println(" Information updated successfully!");
    }
  
  
     public boolean login(String inputId, String inputPassword) {
        if (this.id != null && this.id.equalsIgnoreCase(inputId) && 
            this.password != null && this.password.equals(inputPassword)) {
            System.out.println(" Login successful! Welcome, " + name);
            return true;
        } else {
            System.out.println(" Invalid ID or password.");
            return false;
        }
    }


   
    public void makeOrder() {        
        System.out.println(name + " is making an order...");
    }
    
        public String getDetails() {
        return "Name: " + name + "\nEmail: " + email + "\nPhone: " + phoneNumber;
    }
    
}
 