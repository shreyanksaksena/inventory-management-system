package test.java.com.warehouse.service;

import main.java.com.warehouse.model.Category;
import main.java.com.warehouse.model.Item;
import main.java.com.warehouse.service.InventoryService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class InventoryServiceTest {
    private InventoryService inventoryService;
    private Item testItem;

    @Before
    public void setUp() {
        inventoryService = new InventoryService();
        testItem = new Item("TEST001", "Test Item", Category.ELECTRONICS, 5, 3);
    }

    @Test
    public void testAddItem() {
        inventoryService.addItem(testItem);
        assertEquals(testItem, inventoryService.getItem("TEST001"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullItem() {
        inventoryService.addItem(null);
    }

    @Test
    public void testUpdateItem() {
        inventoryService.addItem(testItem);
        Item updatedItem = new Item("TEST001", "Updated Item", Category.ELECTRONICS, 10, 4);
        inventoryService.updateItem(updatedItem);
        Item retrievedItem = inventoryService.getItem("TEST001");
        assertEquals("Updated Item", retrievedItem.getName());
        assertEquals(10, retrievedItem.getQuantity());
        assertEquals(4, retrievedItem.getRestockThreshold());
    }

    @Test
    public void testUpdateItemCategory() {
        inventoryService.addItem(testItem);
        Item updatedItem = new Item("TEST001", "Test Item", Category.FURNITURE, 5, 3);
        inventoryService.updateItem(updatedItem);
        assertEquals(0, inventoryService.getItemsByCategory(Category.ELECTRONICS).size());
        assertEquals(1, inventoryService.getItemsByCategory(Category.FURNITURE).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExistentItem() {
        Item newItem = new Item("NONEXISTENT", "New Item", Category.ELECTRONICS, 5, 3);
        inventoryService.updateItem(newItem);
    }

    @Test
    public void testDeleteItem() {
        inventoryService.addItem(testItem);
        inventoryService.deleteItem("TEST001");
        assertNull(inventoryService.getItem("TEST001"));
    }

    @Test
    public void testMergeInventory() {
        inventoryService.addItem(testItem);
        List<Item> newItems = Arrays.asList(
                new Item("TEST001", "Test Item", Category.ELECTRONICS, 3, 3),
                new Item("TEST002", "New Item", Category.FURNITURE, 2, 2)
        );
        inventoryService.mergeInventory(newItems);
        assertEquals(8, inventoryService.getItem("TEST001").getQuantity()); // 5 + 3
        assertNotNull(inventoryService.getItem("TEST002"));
    }

    @Test
    public void testGetTopKItems() {
        inventoryService.addItem(new Item("TEST001", "Low Quantity", Category.ELECTRONICS, 5, 3));
        inventoryService.addItem(new Item("TEST002", "Medium Quantity", Category.ELECTRONICS, 10, 3));
        inventoryService.addItem(new Item("TEST003", "High Quantity", Category.ELECTRONICS, 15, 3));

        List<Item> topItems = inventoryService.getTopKItems(2);
        assertEquals(2, topItems.size());
        assertEquals(15, topItems.get(0).getQuantity());
        assertEquals(10, topItems.get(1).getQuantity());
    }

    @Test
    public void testGetItemsBelowThreshold() {
        inventoryService.addItem(new Item("TEST001", "Below Threshold", Category.ELECTRONICS, 2, 5));
        inventoryService.addItem(new Item("TEST002", "Above Threshold", Category.ELECTRONICS, 10, 5));

        List<Item> lowStockItems = inventoryService.getItemsBelowThreshold();
        assertEquals(1, lowStockItems.size());
        assertEquals("TEST001", lowStockItems.get(0).getId());
    }

    @Test
    public void testGetCategoryItemCount() {
        inventoryService.addItem(new Item("TEST001", "Electronics 1", Category.ELECTRONICS, 3, 2));
        inventoryService.addItem(new Item("TEST002", "Electronics 2", Category.ELECTRONICS, 2, 2));
        inventoryService.addItem(new Item("TEST003", "Furniture 1", Category.FURNITURE, 4, 2));

        assertEquals(5, inventoryService.getCategoryItemCount(Category.ELECTRONICS));
        assertEquals(4, inventoryService.getCategoryItemCount(Category.FURNITURE));
    }

    @Test
    public void testGetTotalItemCount() {
        inventoryService.addItem(new Item("TEST001", "Item 1", Category.ELECTRONICS, 3, 2));
        inventoryService.addItem(new Item("TEST002", "Item 2", Category.FURNITURE, 2, 2));

        assertEquals(5, inventoryService.getTotalItemCount());
    }
}