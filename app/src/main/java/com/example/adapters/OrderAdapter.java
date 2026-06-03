package com.example.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411tapp.R;
import com.example.models.DataWarehouse;
import com.example.models.Order;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    Activity context;
    int resource;
    List<Order> objects;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public OrderAdapter(@NonNull Activity context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            row = inflater.inflate(this.resource, null);
        }

        TextView txtOrderID = row.findViewById(R.id.txtOrderID);
        TextView txtOrderDate = row.findViewById(R.id.txtOrderDate);
        TextView txtOrderStatus = row.findViewById(R.id.txtOrderStatus);
        TextView txtOrderTotal = row.findViewById(R.id.txtOrderTotal);

        Order order = this.objects.get(position);
        txtOrderID.setText(order.getOrderID());
        txtOrderDate.setText(sdf.format(order.getOrderDate()));

        // Set status text and colors
        switch (order.getOrderStatus()) {
            case COMPLETED:
                txtOrderStatus.setText("Completed");
                txtOrderStatus.getBackground().setTint(context.getColor(R.color.light_green_bg));
                txtOrderStatus.setTextColor(context.getColor(R.color.order_status_complete));
                break;
            case NOT_PAYMENT:
                txtOrderStatus.setText("Not Payment");
                txtOrderStatus.getBackground().setTint(context.getColor(R.color.light_yellow_bg));
                txtOrderStatus.setTextColor(context.getColor(R.color.order_status_not_payment));
                break;
            case ON_LOGISTICS:
                txtOrderStatus.setText("On Logistics");
                txtOrderStatus.getBackground().setTint(context.getColor(R.color.light_blue_bg));
                txtOrderStatus.setTextColor(context.getColor(R.color.order_status_on_logistics));
                break;
            case COMPLAINT:
                txtOrderStatus.setText("Complaint");
                txtOrderStatus.getBackground().setTint(context.getColor(R.color.light_red_bg));
                txtOrderStatus.setTextColor(context.getColor(R.color.order_status_complaint));
                break;
            default:
                txtOrderStatus.setText(order.getOrderStatus().toString());
                txtOrderStatus.getBackground().setTint(context.getColor(R.color.grey_white));
                txtOrderStatus.setTextColor(context.getColor(R.color.light_grey));
                break;
        }

        double total = DataWarehouse.sumOfMoneyForOrder(order);
        txtOrderTotal.setText("$" + String.format("%.2f", total));

        return row;
    }
}