package com.example.carapp.RecyclerViewPackage;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carapp.R;

import java.util.ArrayList;

public class CarRvAdapter extends RecyclerView.Adapter<CarRvAdapter.CarViewHolder> {
    private Context context;
    private ArrayList<Car> cars;
    private OnRecyclerClickListener listener;

    public CarRvAdapter(Context context, ArrayList<Car> cars, OnRecyclerClickListener listener) {
        this.context = context;
        this.cars = cars;
        this.listener = listener;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_layout, null, false);
        CarViewHolder carViewHolder = new CarViewHolder(view);
        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car c = cars.get(position);

        if (c.getImage() != null)
            holder.iv.setImageURI(Uri.parse(c.getImage()));
        else
            holder.iv.setImageResource(R.drawable.car);

        holder.tv_model.setText(c.getModel());
        holder.tv_color.setText(c.getColor());

        try {
            holder.tv_color.setTextColor(Color.parseColor(c.getColor()));
        } catch (Exception e) {
        }

        holder.tv_dbl.setText(String.valueOf(c.getDpl()));
        holder.iv.setTag(c.getId());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv_model, tv_color, tv_dbl;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.custom_item_image);
            tv_model = itemView.findViewById(R.id.custom_item_model);
            tv_color = itemView.findViewById(R.id.custom_item_color);
            tv_dbl = itemView.findViewById(R.id.custom_item_dpl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) iv.getTag();
                    listener.onItemClick(id);
                }
            });
        }
    }
}
