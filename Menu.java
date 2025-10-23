/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem;

import java.util.*;
import java.text.SimpleDateFormat; 



public class Menu 
{
    
     private Date lastUpdate;
    private ArrayList<MenuItem> items;
    
    private void updateLastUpdate()
    {
        this.lastUpdate=new Date();
    }
    
    public Menu() 
    {
        this.lastUpdate = new Date();
        this.items=new ArrayList<>();
    }

    public Date getLastUpdate() 
    {
        return lastUpdate;
    }
    
    public ArrayList<MenuItem> getMenuItems()
    {
        return items;
    }
    
    public void addItem(MenuItem item)
    {
        items.add(item);
        updateLastUpdate();
    }
    
    public void removeItem(String itemName)
    {
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            if(item.getName().equalsIgnoreCase(itemName)){
                items.remove(i);
                updateLastUpdate();
                break;
            }
        }
    }
    
    public void updateItem(String itemName, MenuItem newItem)
    {
        for(int i=0;i<items.size();i++){
            if(items.get(i).getName().equalsIgnoreCase(itemName)){
                items.set(i, newItem);
                updateLastUpdate();
                break;
            }
        }
    }
    
    public ArrayList<MenuItem> searchItems(String keyword)
    {
        ArrayList<MenuItem> results=new ArrayList<>();
        for(MenuItem item:items){
            if(item.getName().toLowerCase().contains(keyword.toLowerCase()))
                results.add(item);
        }
        return results;
    }
    
    public ArrayList<MenuItem> getItemByCategory(String category)
    {
        ArrayList<MenuItem> categoryItems= new ArrayList<>();
        for(MenuItem item:items){
            if(item.getCategory().equalsIgnoreCase(category)){
                categoryItems.add(item);
            }
        }
        return categoryItems;
    }
    
    public ArrayList<MenuItem> getAllAvialableItems()
    {
        ArrayList<MenuItem> avialableItems=new ArrayList<>();
        for(MenuItem item:items){
            if(item.isIsAvailable()){
                avialableItems.add(item);
            }
        }
        return avialableItems;
    }
    
    public void showLastUpdate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Last updated on: " + formatter.format(lastUpdate));
    }
    
}
