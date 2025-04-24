package models;

import java.time.LocalDate;

public class Sale {
    private LocalDate date;
    private Recipe recipe;
    private Size size;
    private double price;
    
    public Sale(LocalDate date, Recipe recipe, Size size, double price) {
        this.date = date;
        this.recipe = recipe;
        this.size = size;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
