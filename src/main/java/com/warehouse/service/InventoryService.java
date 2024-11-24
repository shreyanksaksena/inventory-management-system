package main.java.com.warehouse.service;

import main.java.com.warehouse.model.Category;
import main.java.com.warehouse.model.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryService implements InventoryManager {
    private final Map<String, Item> inventory;
    private final Map<Category, List<Item>> categoryMap;

    public InventoryService() {
        this.inventory = new HashMap<>();
        this.categoryMap = new HashMap<>();
        for (Category category : Category.values()) {
            categoryMap.put(category, new ArrayList<>());
        }
    }

    @Override
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        inventory.put(item.getId(), item);
        categoryMap.get(item.getCategory()).add(item);
    }

    @Override
    public void updateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        Item existingItem = inventory.get(item.getId());
        if (existingItem == null) {
            throw new IllegalArgumentException("Item not found in inventory");
        }

        // If category has changed, update category mappings
        if (!existingItem.getCategory().equals(item.getCategory())) {
            categoryMap.get(existingItem.getCategory()).remove(existingItem);
            categoryMap.get(item.getCategory()).add(item);
        }

        existingItem.setName(item.getName());
        existingItem.setCategory(item.getCategory());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setRestockThreshold(item.getRestockThreshold());
    }

    @Override
    public void deleteItem(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }

        Item item = inventory.remove(id);
        if (item != null) {
            categoryMap.get(item.getCategory()).remove(item);
        }
    }

    @Override
    public Item getItem(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        return inventory.get(id);
    }

    @Override
    public List<Item> getItemsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return new ArrayList<>(categoryMap.get(category));
    }

    @Override
    public void mergeInventory(List<Item> items) {
        if (items == null) {
            throw new IllegalArgumentException("Items list cannot be null");
        }

        for (Item item : items) {
            Item existingItem = inventory.get(item.getId());
            if (existingItem != null) {
                // If item exists, update quantity
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                // If item doesn't exist, add it
                addItem(item);
            }
        }
    }

    @Override
    public List<Item> getTopKItems(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }

        return inventory.values().stream()
                .sorted((i1, i2) -> Integer.compare(i2.getQuantity(), i1.getQuantity())) // Sort by quantity descending
                .limit(k)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsBelowThreshold() {
        return inventory.values().stream()
                .filter(item -> item.getQuantity() < item.getRestockThreshold())
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalItemCount() {
        return inventory.values().stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }

    @Override
    public int getCategoryItemCount(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        return categoryMap.get(category).stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }
    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(inventory.values());
    }
}