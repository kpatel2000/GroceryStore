package com.example.kpmall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface TotalPrice{
        void getTotalPrice(double price);
    }

    public interface DeleteItem{
        void onDeleteResult(GroceryItem item);
    }

    private TotalPrice totalPrice;
    private DeleteItem deleteItem;
    private ArrayList<GroceryItem> items = new ArrayList<>();
    private Context context;
    private Fragment fragment;

    public CartAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.txtName.setText(items.get(position).getName());
        holder.txtPrice.setText(items.get(position).getPrice()+ "$");
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Deleting...")
                        .setMessage("Are You Sure You Want To Delete This Item?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    deleteItem = (DeleteItem) fragment;
                                    deleteItem.onDeleteResult(items.get(position));
                                }catch (ClassCastException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        calculateTotalPrice();
        notifyDataSetChanged();
    }

    private void calculateTotalPrice(){
        double price = 0;
        for (GroceryItem item: items){
            price += item.getPrice();
        }
        price = Math.round(price*100.00)/100.00;
        try{
            totalPrice = (TotalPrice) fragment;
            totalPrice.getTotalPrice(price);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtPrice,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
