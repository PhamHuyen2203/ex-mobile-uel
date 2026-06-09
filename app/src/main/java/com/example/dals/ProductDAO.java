package com.example.dals;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.models.Product;

import java.util.ArrayList;

public class ProductDAO {
    public static final String DATABASE_NAME = "K23411TSales.sqlite";
    public static final String TABLE_NAME = "Product";

    public static ArrayList<Product> getProducts(Context context) {
        return getProductsByCategory(context, null);
    }

    public static ArrayList<Product> getProductsByCategory(Context context, String categoryId) {
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            
            String query = "SELECT * FROM " + TABLE_NAME;
            String[] selectionArgs = null;
            if (categoryId != null) {
                query += " WHERE CategoryID = ?";
                selectionArgs = new String[]{categoryId};
            }
            
            cursor = database.rawQuery(query, selectionArgs);
            while(cursor.moveToNext()){
                String pId = cursor.getString(0);
                String pName = cursor.getString(1);
                int quantity = cursor.getInt(2);
                double price = cursor.getDouble(3);
                double coupon = cursor.getDouble(4);
                double VAT = cursor.getDouble(5);
                String cId = cursor.getString(6);
                products.add(new Product(pId, pName, quantity, price, coupon, VAT, cId));
            }
        } catch (Exception e) {
            Log.e("ProductDAO", "Error getting products: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (database != null) database.close();
        }
        return products;
    }
}
