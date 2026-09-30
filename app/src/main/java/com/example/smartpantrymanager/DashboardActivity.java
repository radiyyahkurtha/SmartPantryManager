package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private Button ingredientsButton;
    private Button recipesButton;
    private Button stockButton;
    private Button settingsButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ingredientsButton = findViewById(R.id.ingredientsButton);
        recipesButton = findViewById(R.id.recipesButton);
        stockButton = findViewById(R.id.stockButton);
        settingsButton = findViewById(R.id.settingsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Manage Ingredients
        ingredientsButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    IngredientsActivity.class
            );
            startActivity(intent);
        });

        // Suggested Recipes
        recipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });

        // Check Stock
        stockButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    StockActivity.class
            );
            startActivity(intent);
        });

        // Settings
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }

    // Display the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle toolbar menu selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Pantry
        if (id == R.id.menu_pantry) {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    IngredientsActivity.class
            );
            startActivity(intent);
            return true;
        }

        // Recipes
        if (id == R.id.menu_recipes) {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
            return true;
        }

        // Settings
        if (id == R.id.menu_settings) {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}