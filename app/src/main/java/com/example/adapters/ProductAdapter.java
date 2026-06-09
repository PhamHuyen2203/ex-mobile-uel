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
import com.example.models.Product;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;
    int resource;

    public ProductAdapter(@NonNull Activity context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            row = inflater.inflate(this.resource, parent, false);
        }

        Product product = getItem(position);

        TextView txtProductId = row.findViewById(R.id.txtProductId);
        TextView txtProductName = row.findViewById(R.id.txtProductName);
        TextView txtProductPrice = row.findViewById(R.id.txtProductPrice);
        TextView txtProductQuantity = row.findViewById(R.id.txtProductQuantity);

        if (product != null) {
            txtProductId.setText(product.getProductId());
            txtProductName.setText(product.getProductName());
            txtProductPrice.setText(String.format(Locale.getDefault(), "$%,.2f", product.getPrice()));
            txtProductQuantity.setText("Qty: " + product.getQuantity());
        }

        return row;
    }
}
