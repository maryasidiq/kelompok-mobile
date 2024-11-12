package com.example.pamkelompok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<LaptopAdapter.LaptopViewHolder> {

    private Context context;
    private List<Laptop> laptops;
    private OnLaptopListener onLaptopListener;

    public interface OnLaptopListener {
        void onEditClick(Laptop laptop);
        void onDeleteClick(Laptop laptop);
    }

    public LaptopAdapter(Context context, List<Laptop> laptops, OnLaptopListener onLaptopListener) {
        this.context = context;
        this.laptops = laptops;
        this.onLaptopListener = onLaptopListener;
    }

    @NonNull
    @Override
    public LaptopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.laptop_item, parent, false);
        return new LaptopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaptopViewHolder holder, int position) {
        Laptop laptop = laptops.get(position);
        holder.laptopName.setText(laptop.getNama());
        holder.laptopBrand.setText(laptop.getMerk());
        holder.laptopPrice.setText(laptop.getHarga());
        holder.laptopReleaseYear.setText(laptop.getTahunRilis());

        // Set a placeholder image
        holder.laptopImage.setImageResource(R.drawable.baseline_laptop_24);

        // Edit Button Click
        holder.editButton.setOnClickListener(v -> onLaptopListener.onEditClick(laptop));

        // Delete Button Click
        holder.deleteButton.setOnClickListener(v -> onLaptopListener.onDeleteClick(laptop));
    }

    @Override
    public int getItemCount() {
        return laptops.size();
    }

    public class LaptopViewHolder extends RecyclerView.ViewHolder {
        ImageView laptopImage;
        TextView laptopName, laptopBrand, laptopPrice, laptopReleaseYear;
        Button editButton, deleteButton;

        public LaptopViewHolder(@NonNull View itemView) {
            super(itemView);
            laptopImage = itemView.findViewById(R.id.laptopImage);
            laptopName = itemView.findViewById(R.id.laptopName);
            laptopBrand = itemView.findViewById(R.id.laptopBrand);
            laptopPrice = itemView.findViewById(R.id.laptopPrice);
            laptopReleaseYear = itemView.findViewById(R.id.laptopReleaseYear);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
