package com.learnkafka.domain;

public class Item {

    private Integer id;
    private String itemName;
    private Double price;

    public Item(Integer id, String itemName, Double price) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
    }
    public Item(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                '}';
    }
}
