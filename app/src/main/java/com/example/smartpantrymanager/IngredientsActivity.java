package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private RecyclerView pantryRecyclerView;
    private TextView emptyMessage;

    private PantryAdapter adapter;

    private ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> quantities = new ArrayList<>();
    private ArrayList<String> units = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredients);

        databaseHelper = new DatabaseHelper(this);

        pantryRecyclerView =
                findViewById(R.id.pantryRecyclerView);

        emptyMessage =
                findViewById(R.id.emptyMessage);

        Button addButton =
                findViewById(R.id.addIngredientButton);

        Button backButton =
                findViewById(R.id.backButton);

        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    IngredientsActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

        loadIngredients();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadIngredients();
        }
    }

    private void loadIngredients() {

        ids.clear();
        names.clear();
        quantities.clear();
        units.clear();

        Cursor cursor = databaseHelper.getAllIngredients();

        while (cursor.moveToNext()) {

            ids.add(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    )
            );

            names.add(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    )
            );

            quantities.add(
                    String.valueOf(
                            cursor.getDouble(
                                    cursor.getColumnIndexOrThrow("quantity")
                            )
                    )
            );

            units.add(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    )
            );
        }

        cursor.close();

        if (names.isEmpty()) {

            emptyMessage.setVisibility(TextView.VISIBLE);
            pantryRecyclerView.setVisibility(RecyclerView.GONE);

        } else {

            emptyMessage.setVisibility(TextView.GONE);
            pantryRecyclerView.setVisibility(RecyclerView.VISIBLE);

            adapter = new PantryAdapter(
                    ids,
                    names,
                    quantities,
                    units,
                    this::editIngredient,
                    this::deleteIngredient
            );

            pantryRecyclerView.setAdapter(adapter);
        }
    }

    private void editIngredient(int id) {

        Intent intent = new Intent(
                IngredientsActivity.this,
                AddEditIngredientActivity.class
        );

        intent.putExtra("ingredient_id", id);

        startActivity(intent);
    }

    private void deleteIngredient(int id) {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete this ingredient?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    boolean deleted =
                            databaseHelper.deleteIngredient(id);

                    if (deleted) {

                        Toast.makeText(
                                this,
                                "Ingredient deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadIngredients();

                    } else {

                        Toast.makeText(
                                this,
                                "Unable to delete ingredient",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}