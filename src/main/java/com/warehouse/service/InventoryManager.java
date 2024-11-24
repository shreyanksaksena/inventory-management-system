package main.java.com.warehouse.service;

import main.java.com.warehouse.model.Category;
import main.java.com.warehouse.model.Item;
import java.util.List;

public interface InventoryManager {
    void addItem(Item item);
    void updateItem(Item item);
    void deleteItem(String id);
    Item getItem(String id);
    List<Item> getItemsByCategory(Category category);
    void mergeInventory(List<Item> items);
    List<Item> getTopKItems(int k);
    List<Item> getItemsBelowThreshold();
    int getTotalItemCount();
    int getCategoryItemCount(Category category);
    List<Item> getAllItems();
}