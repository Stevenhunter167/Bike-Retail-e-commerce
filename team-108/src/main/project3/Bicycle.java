package main.project3;

public class Bicycle {
    private String name;
    private String brand;
    private String category;
    private int year;
    private double price;
    private double rating;
    private int vote;

    public Bicycle() {}
    public Bicycle(String name, String brand, String category, int year, double price, double rating, int vote) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.year = year;
        this.price = price;
        this.rating = rating;
        this.vote = vote;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getVote() {
        return vote;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Bicycle{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rating=" + rating +
                ", vote=" + vote +
                '}';
    }
}
