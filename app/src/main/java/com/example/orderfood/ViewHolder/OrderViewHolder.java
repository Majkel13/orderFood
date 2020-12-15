package com.example.orderfood.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress, txtOrderMethod, txtTotalPrice;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderMethod = (TextView)itemView.findViewById(R.id.order_method);
        txtTotalPrice = (TextView)itemView.findViewById(R.id.totalPrice);


    }
}
