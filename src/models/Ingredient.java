package models;

public class Ingredient {
    private String name;
    private double quantity; 
    private Unit unit;       // ML or G
    private double costPerUnit;


    public Ingredient(String name, double quantity, Unit unit, double costPerUnit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void deduct(double amount) { 
        this.quantity -= amount; 
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }
    
}
