package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText quantityInput;
    private EditText unitInput;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.nameInput);
        quantityInput = findViewById(R.id.quantityInput);
        unitInput = findViewById(R.id.unitInput);

        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != -1) {

            setTitle("Edit Ingredient");

            android.database.Cursor cursor =
                    databaseHelper.getIngredient(ingredientId);

            if (cursor.moveToFirst()) {

                nameInput.setText(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                quantityInput.setText(
                        String.valueOf(
                                cursor.getDouble(
                                        cursor.getColumnIndexOrThrow("quantity")
                                )
                        )
                );

                unitInput.setText(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );
            }

            cursor.close();

        } else {

            setTitle("Add Ingredient");

            unitInput.setText("item");
        }

        saveButton.setOnClickListener(v -> saveIngredient());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveIngredient() {

        String name = nameInput.getText().toString().trim();
        String quantityText =
                quantityInput.getText().toString().trim();

        String unit = unitInput.getText().toString().trim();

        if (name.isEmpty()) {

            nameInput.setError("Enter an ingredient name");
            nameInput.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            quantityInput.setError("Enter a quantity");
            quantityInput.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            unit = "item";
        }

        try {

            double quantity = Double.parseDouble(quantityText);

            if (quantity <= 0) {

                quantityInput.setError(
                        "Quantity must be greater than 0"
                );

                return;
            }

            boolean success;

            if (ingredientId == -1) {

                success = databaseHelper.addIngredient(
                        name,
                        quantity,
                        unit
                );

            } else {

                success = databaseHelper.updateIngredient(
                        ingredientId,
                        name,
                        quantity,
                        unit
                );
            }

            if (success) {

                Toast.makeText(
                        this,
                        ingredientId == -1
                                ? "Ingredient added successfully"
                                : "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Unable to save ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (NumberFormatException e) {

            quantityInput.setError("Enter a valid number");
        }
    }
}