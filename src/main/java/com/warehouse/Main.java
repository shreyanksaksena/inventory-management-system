package main.java.com.warehouse;

import main.java.com.warehouse.model.Category;
import main.java.com.warehouse.model.Item;
import main.java.com.warehouse.service.InventoryManager;
import main.java.com.warehouse.service.InventoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final InventoryManager inventoryManager = new InventoryService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeInventory();

        while (true) {
            displayMenu();
            int choice = getUserChoice();

            try {
                processUserChoice(choice);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

            if (choice == 0) {
                break;
            }
        }

        scanner.close();
    }

    private static void initializeInventory() {
        // Add some sample items
        List<Item> initialItems = Arrays.asList(
                new Item("LAP001", "Gaming Laptop", Category.ELECTRONICS, 10, 5),
                new Item("DSK001", "Office Desk", Category.FURNITURE, 5, 3),
                new Item("CHR001", "Gaming Chair", Category.FURNITURE, 8, 4),
                new Item("PHN001", "Smartphone", Category.ELECTRONICS, 15, 7)
        );

        initialItems.forEach(inventoryManager::addItem);
        System.out.println("Inventory initialized with sample items.");
    }

    private static void displayMenu() {
        System.out.println("\n=== Inventory Management System ===");
        System.out.println("1. View all items");
        System.out.println("2. Add new item");
        System.out.println("3. Update item quantity");
        System.out.println("4. Remove item");
        System.out.println("5. View items by category");
        System.out.println("6. View low stock items");
        System.out.println("7. View top K items by quantity");
        System.out.println("8. View inventory statistics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void processUserChoice(int choice) {
        switch (choice) {
            case 1:
                displayAllItems();
                break;
            case 2:
                addNewItem();
                break;
            case 3:
                updateItemQuantity();
                break;
            case 4:
                removeItem();
                break;
            case 5:
                viewItemsByCategory();
                break;
            case 6:
                viewLowStockItems();
                break;
            case 7:
                viewTopKItems();
                break;
            case 8:
                viewInventoryStatistics();
                break;
            case 0:
                System.out.println("Thank you for using the Inventory Management System!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void displayAllItems() {
        List<Item> allItems = inventoryManager.getAllItems();
        System.out.println("\nAll Items in Inventory:");
        allItems.forEach(Main::displayItem);
    }

    private static void addNewItem() {
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter item ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.println("Available categories:");
        for (Category category : Category.values()) {
            System.out.println(category);
        }
        System.out.print("Enter category: ");
        Category category = Category.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter restock threshold: ");
        int threshold = scanner.nextInt();

        Item newItem = new Item(id, name, category, quantity, threshold);
        inventoryManager.addItem(newItem);
        System.out.println("Item added successfully!");
    }

    private static void updateItemQuantity() {
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter item ID: ");
        String id = scanner.nextLine();

        Item item = inventoryManager.getItem(id);
        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        System.out.print("Enter new quantity: ");
        int newQuantity = scanner.nextInt();

        item.setQuantity(newQuantity);
        inventoryManager.updateItem(item);
        System.out.println("Quantity updated successfully!");
    }

    private static void removeItem() {
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter item ID to remove: ");
        String id = scanner.nextLine();

        inventoryManager.deleteItem(id);
        System.out.println("Item removed successfully!");
    }

    private static void viewItemsByCategory() {
        scanner.nextLine(); // Clear buffer

        System.out.println("Available categories:");
        for (Category category : Category.values()) {
            System.out.println(category);
        }
        System.out.print("Enter category: ");
        Category category = Category.valueOf(scanner.nextLine().toUpperCase());

        List<Item> items = inventoryManager.getItemsByCategory(category);
        System.out.println("\nItems in category " + category + ":");
        items.forEach(Main::displayItem);
    }

    private static void viewLowStockItems() {
        List<Item> lowStockItems = inventoryManager.getItemsBelowThreshold();
        System.out.println("\nLow Stock Items:");
        lowStockItems.forEach(Main::displayItem);
    }

    private static void viewTopKItems() {
        System.out.print("Enter number of items to view: ");
        int k = scanner.nextInt();

        List<Item> topItems = inventoryManager.getTopKItems(k);
        System.out.println("\nTop " + k + " Items by Quantity:");
        topItems.forEach(Main::displayItem);
    }

    private static void viewInventoryStatistics() {
        System.out.println("\nInventory Statistics:");
        System.out.println("Total Item Count: " + inventoryManager.getTotalItemCount());

        System.out.println("\nItems by Category:");
        for (Category category : Category.values()) {
            int count = inventoryManager.getCategoryItemCount(category);
            System.out.println(category + ": " + count + " items");
        }
    }

    private static void displayItem(Item item) {
        System.out.printf("ID: %-8s Name: %-20s Category: %-12s Quantity: %-4d Threshold: %d%n",
                item.getId(), item.getName(), item.getCategory(),
                item.getQuantity(), item.getRestockThreshold());
    }
}