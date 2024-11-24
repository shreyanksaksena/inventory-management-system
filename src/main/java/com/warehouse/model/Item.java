package main.java.com.warehouse.model;

import java.util.Objects;

public class Item {
    private final String id;
    private String name;
    private Category category;
    private int quantity;
    private int restockThreshold;

    public Item(String id, String name, Category category, int quantity, int restockThreshold) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.restockThreshold = restockThreshold;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getRestockThreshold() {
        return restockThreshold;
    }

    // Setters (except for id which is final)
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setRestockThreshold(int restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                ", restockThreshold=" + restockThreshold +
                '}';
    }
}