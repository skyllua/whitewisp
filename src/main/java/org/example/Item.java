package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.*;

@JsonAutoDetect
public class Item {
    private int id;
    private String link;
    private String brand;
    private String productName;
    private String currency;
    private float price;
    private List<String> simpleColors;
//    private Map<String, Item> colors;

    public Item(int id, String link) {
        this.id = id;
        this.link = link;
        simpleColors = new ArrayList<>();
//        colors = new HashMap<>();
    }

    public void addColor(String color) {
        simpleColors.add(color);
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSimpleColors(List<String> simpleColors) {
        this.simpleColors = simpleColors;
    }

//    public void setColors(Map<String, Item> colors) {
//        this.colors = colors;
//    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductName() {
        return productName;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() {
        return price;
    }

    public List<String> getSimpleColors() {
        return simpleColors;
    }

//    public Map<String, Item> getColors() {
//        return colors;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                Float.compare(item.price, price) == 0 &&
                Objects.equals(link, item.link) &&
                Objects.equals(brand, item.brand) &&
                Objects.equals(productName, item.productName) &&
                Objects.equals(simpleColors, item.simpleColors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, brand, productName, price, simpleColors);
    }
}
