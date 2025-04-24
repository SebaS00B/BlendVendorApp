import models.*;
import java.time.LocalDate;
import java.util.*;


public class App {
    private  static final double PROFIT_MARGIN = 1.5;
    private  static final int LOW_STOCK_THRESHOLD = 4;
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Map<DrinkType, Recipe> recipes = setupRecipes();
        hardcodeStartingInventory(inventory);
        List<Sale> sales = new ArrayList<>();
    
        Scanner sc = new Scanner(System.in);
        printWelcomeMessage();
    
        while (true) {
            printMenu();
            int choice = readInt(sc, "Choose an option: ", 1, 4);
            switch (choice) {
                case 1:
                    inventory.listInventory();
                    
                    break;
                case 2:
                    sellDrink(sc, inventory, recipes, sales);
                    
                    break;
                case 3:
                    reportSales(sc, sales);
                    
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
            }
        }
    }
    
    private static void printWelcomeMessage() {
        System.out.println("==========================================");
        System.out.println("  Welcome to the Beverage Recipe App!   ");
        System.out.println("==========================================");
    }
    
    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Show inventory");
        System.out.println("2. Sell drink");
        System.out.println("3. Daily sales report");
        System.out.println("4. Exit");
    }
    
    private static int readInt(Scanner sc, String prompt, int min, int max) {
        int selection;
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                selection = Integer.parseInt(line);
                if (selection < min || selection > max) {
                    System.out.printf("Please enter a number between %d and %d.%n", min, max);
                } else {
                    return selection;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    private static Map<DrinkType, Recipe> setupRecipes() {
        Map<DrinkType, Recipe> map = new HashMap<>();
        Map<String, Double> base = Map.of(
            "BlendedFruit", 50.0,
            "Ice", 30.0,
            "CondensedMilk", 20.0,
            "Sugar", 8.0
        );
        map.put(DrinkType.STRAWBERRY,
                new Recipe(merge(base, Map.of("StrawberryFruit", 100.0))));
        map.put(DrinkType.BANANA,
                new Recipe(merge(base, Map.of("BananaFruit", 120.0))));
        map.put(DrinkType.MANGO,
                new Recipe(merge(base, Map.of("MangoFruit", 140.0))));
        return map;
    }
    
    private static Map<String, Double> merge(Map<String, Double> a, Map<String, Double> b) {
        Map<String, Double> m = new HashMap<>(a);
        m.putAll(b);
        return m;
    }
    
    private static void hardcodeStartingInventory(Inventory inv) {
        inv.addIngredient(new Ingredient("BlendedFruit", 5000, Unit.ML, 0.002));
        inv.addIngredient(new Ingredient("Ice", 3000, Unit.ML, 0.0001));
        inv.addIngredient(new Ingredient("CondensedMilk", 2000, Unit.ML, 0.005));
        inv.addIngredient(new Ingredient("Sugar", 500, Unit.G, 0.0004));
        inv.addIngredient(new Ingredient("StrawberryFruit", 2000, Unit.G, 0.0013));
        inv.addIngredient(new Ingredient("BananaFruit", 2000, Unit.G, 0.0011));
        inv.addIngredient(new Ingredient("MangoFruit", 2000, Unit.G, 0.0013));
    }
    
    private static void sellDrink(Scanner sc, Inventory inv, Map<DrinkType, Recipe> recs, List<Sale> sales) {
        System.out.println("\n--- SELL DRINK ---");
        Size size = null;
        while (size == null) {
            System.out.print("Enter size (SMALL, MEDIUM, LARGE) or 'C' to cancel: ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("C")) {
                System.out.println("Sale cancelled.");
                return;
            }
            try {
                size = Size.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown size. Please try again.");
            }
        }
    
        List<DrinkType> flavors = new ArrayList<>();
        while (flavors.isEmpty()) {
            System.out.print("Enter flavors (comma-separated, e.g. STRAWBERRY,BANANA,MANGO) or 'C' to cancel: ");
            String line = sc.nextLine().trim().toUpperCase();
            if (line.equals("C")) {
                System.out.println("Sale cancelled.");
                return;
            }
            String[] inputs = line.split(",");
            flavors.clear();
            for (String s : inputs) {
                try {
                    flavors.add(DrinkType.valueOf(s.trim()));
                } catch (IllegalArgumentException e) {
                    System.out.printf("'%s' is not a valid flavor. Please try again.%n", s.trim());
                    flavors.clear();
                    break;
                }
            }
        }
    
        // Build recipe
        Map<String, Double> combined = new HashMap<>();
        double factor = size.getVolume() / 100.0;
        for (DrinkType dt : flavors) {
            recs.get(dt).getComponents().forEach((name, amt) ->
                combined.merge(name, amt * factor, Double::sum)
            );
        }
        Recipe recipe = new Recipe(combined);
    
        // Check stock
        if (!inv.canMake(recipe)) {
            System.out.printf("Not enough ingredients to prepare a %s %s drink.%n",
                size, flavors);
            return;
        }
    
        // Confirm sale
        System.out.printf("About to sell a %s %s drink. Proceed? (Y/N): ", size, flavors);
        String confirm = sc.nextLine().trim().toUpperCase();
        if (!confirm.equals("Y")) {
            System.out.println("Sale cancelled.");
            return;
        }
    
        // Process sale
        inv.useIngredients(recipe);
        double cost = inv.calculateRecipeCost(recipe);
        double price = cost * PROFIT_MARGIN;
        System.out.printf("Sold a %s %s drink for $%.2f.%n",
            size, flavors, price);
    
        // Low stock warning
        checkLowInventoryWarning(inv, recipe, LOW_STOCK_THRESHOLD);
    
        // Record sale
        sales.add(new Sale(LocalDate.now(), recipe, size, price));
    }
    
    private static void reportSales(Scanner sc, List<Sale> sales) {
        System.out.println("\n--- DAILY SALES REPORT ---");
        LocalDate today = LocalDate.now();
    
        
        long count = sales.stream()
            .filter(s -> s.getDate().equals(today))
            .count();
    
        
        double total = sales.stream()
            .filter(s -> s.getDate().equals(today))
            .mapToDouble(Sale::getPrice)
            .sum();
    
        if (count == 0) {
            System.out.println("No sales recorded for today.");
        } else {
           
            sales.stream()
                .filter(s -> s.getDate().equals(today))
                .forEach(s -> System.out.printf(
                    "%s drink sold %s for $%.2f%n",
                    s.getSize(),
                    s.getRecipe().getComponents().keySet(),
                    s.getPrice()
                ));
           
            System.out.printf("Total: %d sales for $%.2f today.%n", count, total);
        }
    }
    
    private static void checkLowInventoryWarning(Inventory inv, Recipe recipe, int count) {
        if (inv.isBelowThreshold(recipe, count)) {
            System.out.println("⚠️ Warning: low inventory. Cannot prepare "
                + count + " more drinks.");
        }
    }
    

}