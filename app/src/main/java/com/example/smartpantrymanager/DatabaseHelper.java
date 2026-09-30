package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Ingredients table
        db.execSQL(
                "CREATE TABLE ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL)"
        );

        // Recipes table
        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "ingredients TEXT NOT NULL, " +
                        "quantities TEXT NOT NULL, " +
                        "units TEXT NOT NULL, " +
                        "steps TEXT NOT NULL, " +
                        "method TEXT NOT NULL)"
        );

        // Add the 20 recipes
        seedRecipes(db);
    }

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Chicken Rice",
                "Chicken,Rice,Onion",
                "500,500,100",
                "g,g,g",
                "Cook the rice. Cook the chicken and onion. Combine everything and serve."
        );

        addRecipe(
                db,
                "Vegetable Fried Rice",
                "Rice,Carrot,Onion,Egg",
                "500,100,100,2",
                "g,g,g,item",
                "Cook the rice. Fry the vegetables. Add the egg and rice and stir-fry."
        );

        addRecipe(
                db,
                "Creamy Pasta",
                "Pasta,Milk,Cheese",
                "250,250,100",
                "g,ml,g",
                "Cook the pasta. Heat the milk and cheese. Mix with the pasta."
        );

        addRecipe(
                db,
                "Chicken Pasta",
                "Chicken,Pasta,Tomato",
                "300,250,2",
                "g,g,item",
                "Cook the pasta and chicken. Add tomato and combine."
        );

        addRecipe(
                db,
                "Omelette",
                "Egg,Cheese,Onion",
                "2,50,50",
                "item,g,g",
                "Beat the eggs. Fry the onion. Add the eggs and cheese and cook."
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "Pasta,Tomato,Onion",
                "250,200,100",
                "g,g,g",
                "Cook the pasta. Cook the tomato and onion. Mix together."
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "Chicken,Bread,Cheese",
                "200,2,50",
                "g,item,g",
                "Cook the chicken. Place it between bread with cheese."
        );

        addRecipe(
                db,
                "Vegetable Soup",
                "Carrot,Potato,Onion",
                "200,300,100",
                "g,g,g",
                "Chop the vegetables. Boil until soft and serve."
        );

        addRecipe(
                db,
                "Mashed Potatoes",
                "Potato,Milk,Butter",
                "500,100,50",
                "g,ml,g",
                "Boil the potatoes. Mash with milk and butter."
        );

        addRecipe(
                db,
                "Cheesy Eggs",
                "Egg,Cheese,Milk",
                "2,50,50",
                "item,g,ml",
                "Beat eggs with milk. Cook and add cheese."
        );

        addRecipe(
                db,
                "Chicken Curry",
                "Chicken,Onion,Tomato,Rice",
                "500,100,200,500",
                "g,g,g,g",
                "Cook chicken with onion and tomato. Serve with rice."
        );

        addRecipe(
                db,
                "Rice and Beans",
                "Rice,Beans,Onion",
                "500,400,100",
                "g,g,g",
                "Cook the rice. Cook the beans and onion. Combine."
        );

        addRecipe(
                db,
                "French Toast",
                "Bread,Egg,Milk",
                "2,2,100",
                "item,item,ml",
                "Beat egg and milk. Dip bread into the mixture and fry."
        );

        addRecipe(
                db,
                "Cheese Toast",
                "Bread,Cheese,Butter",
                "2,100,20",
                "item,g,g",
                "Butter the bread. Add cheese and toast until melted."
        );

        addRecipe(
                db,
                "Vegetable Pasta",
                "Pasta,Carrot,Onion,Tomato",
                "250,100,100,200",
                "g,g,g,g",
                "Cook pasta. Fry vegetables and tomato. Mix together."
        );

        addRecipe(
                db,
                "Rice and Egg",
                "Rice,Egg,Onion",
                "500,2,50",
                "g,item,g",
                "Cook rice. Fry egg and onion. Mix together."
        );

        addRecipe(
                db,
                "Chicken Rice Bowl",
                "Chicken,Rice,Carrot",
                "300,500,100",
                "g,g,g",
                "Cook chicken and carrot. Serve over rice."
        );

        addRecipe(
                db,
                "Creamy Chicken",
                "Chicken,Milk,Cheese",
                "300,200,100",
                "g,ml,g",
                "Cook chicken. Add milk and cheese and simmer."
        );

        addRecipe(
                db,
                "Potato Omelette",
                "Potato,Egg,Onion",
                "300,2,100",
                "g,item,g",
                "Cook potato and onion. Add beaten eggs and cook."
        );

        addRecipe(
                db,
                "Tomato Egg Rice",
                "Rice,Tomato,Egg",
                "500,200,2",
                "g,g,item",
                "Cook rice. Fry tomato and egg. Mix with rice."
        );
    }

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String quantities,
            String units,
            String steps) {

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("quantities", quantities);
        values.put("units", units);
        values.put("steps", steps);

        // Keep method populated as well for compatibility
        // with RecipeDetailActivity if it uses the method column.
        values.put("method", steps);

        db.insert("recipes", null, values);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");

        onCreate(db);
    }

    // =========================
    // INGREDIENT CRUD
    // =========================

    public boolean addIngredient(
            String name,
            double quantity,
            String unit) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        return db.insert(
                "ingredients",
                null,
                values
        ) != -1;
    }

    public Cursor getAllIngredients() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredients ORDER BY name ASC",
                null
        );
    }

    public boolean updateIngredient(
            int id,
            String name,
            double quantity,
            String unit) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        int result = db.update(
                "ingredients",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        return result > 0;
    }

    public boolean deleteIngredient(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                "ingredients",
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        return result > 0;
    }

    // =========================
    // RECIPES
    // =========================

    public Cursor getAllRecipes() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM recipes ORDER BY name ASC",
                null
        );
    }

    // =========================
    // GET ONE INGREDIENT
    // =========================

    public Cursor getIngredient(int id) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredients WHERE id=?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }
}