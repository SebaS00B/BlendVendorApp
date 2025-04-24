package models;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map <String, Ingredient> items = new HashMap<>();

    public void addIngredient(Ingredient ing) {
        items.put(ing.getName(), ing);
    }

    public boolean canMake (Recipe recipe){
    for (Map.Entry<String, Double> entry : recipe.getComponents().entrySet()) {
        Ingredient ing = items.get(entry.getKey());
        if (ing == null || ing.getQuantity() < entry.getValue()) return false;
    }
    return true;}
    
    public void useIngredients(Recipe recipe) {
        recipe.getComponents().forEach((name, amt) ->
            items.get(name).deduct(amt)
        );
    }

    public void listInventory() {
        System.out.println("");
        System.out.println("Current Inventory:");
        items.values().forEach(ing ->
            System.out.printf("- %s: %.2f %s%n", ing.getName(), ing.getQuantity(), ing.getUnit())
        );
    }

    public boolean isBelowThreshold(Recipe recipe, int count) {
        for (Map.Entry<String, Double> entry : recipe.getComponents().entrySet()) {
            Ingredient ing = items.get(entry.getKey());
            if (ing.getQuantity() < entry.getValue() * count) {
                return true;
            }
        }
        return false;
    }
    public double calculateRecipeCost(Recipe r) {
        return r.getComponents().entrySet().stream()
            .mapToDouble(e -> items.get(e.getKey()).getCostPerUnit() * e.getValue())
            .sum();
    }
}

