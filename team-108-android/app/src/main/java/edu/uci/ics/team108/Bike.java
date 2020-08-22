package edu.uci.ics.team108;

public class Bike {
    public String id;
    public String name;
    public String year;
    public String brand_name;
    public String category;
    public String list_price;
    public String rating;
    public String s;

    public Bike(String id, String name, String year, String category, String brand_name, String list_price, String rating, String s) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.brand_name = brand_name;
        this.category = category;
        this.list_price = list_price;
        this.rating = rating;
        this.s = s;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }
}