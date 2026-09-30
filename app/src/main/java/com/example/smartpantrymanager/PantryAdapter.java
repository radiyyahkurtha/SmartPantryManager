package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface OnEditClickListener {
        void onEdit(int id);
    }

    public interface OnDeleteClickListener {
        void onDelete(int id);
    }

    private final ArrayList<Integer> ids;
    private final ArrayList<String> names;
    private final ArrayList<String> quantities;
    private final ArrayList<String> units;

    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;

    public PantryAdapter(
            ArrayList<Integer> ids,
            ArrayList<String> names,
            ArrayList<String> quantities,
            ArrayList<String> units,
            OnEditClickListener editListener,
            OnDeleteClickListener deleteListener) {

        this.ids = ids;
        this.names = names;
        this.quantities = quantities;
        this.units = units;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        holder.name.setText(names.get(position));

        holder.quantity.setText(
                "Quantity: " +
                        quantities.get(position) +
                        " " +
                        units.get(position)
        );

        holder.editButton.setOnClickListener(v ->
                editListener.onEdit(ids.get(position))
        );

        holder.deleteButton.setOnClickListener(v ->
                deleteListener.onDelete(ids.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;
        Button editButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemName);
            quantity = itemView.findViewById(R.id.itemQuantity);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}