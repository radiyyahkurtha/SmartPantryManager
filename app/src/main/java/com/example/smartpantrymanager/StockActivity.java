package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StockActivity extends AppCompatActivity {

    private TextView stockMessage;
    private Button backButton;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock);

        // Connect to database
        databaseHelper = new DatabaseHelper(this);

        // Connect to XML elements
        stockMessage = findViewById(R.id.stockMessage);
        backButton = findViewById(R.id.backButton);

        // Load ingredients from database
        loadStock();

        // Back to Dashboard
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadStock() {

        Cursor cursor = databaseHelper.getAllIngredients();

        if (cursor.getCount() == 0) {

            stockMessage.setText("No stock has been added yet.");

            cursor.close();
            return;
        }

        StringBuilder stock = new StringBuilder();

        stock.append("Current Stock:\n\n");

        while (cursor.moveToNext()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("quantity")
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow("unit")
            );

            stock.append("• ")
                    .append(name)
                    .append(" - ")
                    .append(quantity)
                    .append(" ")
                    .append(unit)
                    .append("\n");
        }

        cursor.close();

        stockMessage.setText(stock.toString());
    }
}