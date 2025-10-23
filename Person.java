/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem;

import java.util.*;
public class Person 
{
    public String name;
    public String email;
    public String phoneNumber;

    public Person(String name, String email, String phoneNumber) {
      setName(name) ;
       setEmail(email);
       setPhoneNumber(phoneNumber);
    }

    public String getDetails() {
        return "Name: " + name + "\nEmail: " + email + "\nPhone: " + phoneNumber;
    }

    public void updateInfo() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter new name: ");
        this.name = sc.nextLine();

        System.out.print("Enter new email: ");
        this.email = sc.nextLine();

        System.out.print("Enter new phone number: ");
        this.phoneNumber = sc.nextLine();

        System.out.println("Information updated successfully!");
    }

  
    public boolean login() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String inputName = sc.nextLine();

        System.out.print("Enter your email: ");
        String inputEmail = sc.nextLine();

        
        if (this.name.equalsIgnoreCase(inputName) && this.email.equalsIgnoreCase(inputEmail)) {
            System.out.println("Basic identity verified for " + name);
            return true;
        } else {
            System.out.println("Invalid name or email.");
            return false;
        }
    }

    public void makeOrder() {
        System.out.println(name + " is making an order...");
    }

    
    
    // getters & setters 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    
}
