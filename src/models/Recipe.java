package models;

import java.util.Map;

public class Recipe {
    private final Map<String, Double> components; // clave: ingredient name, value: quantity

    public Recipe(Map<String, Double> components) {
        this.components = components;
    }

    public Map<String, Double> getComponents() {
        return components;
    }
    
}
