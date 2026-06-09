package com.example.dals;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Category;

import java.util.ArrayList;

public class CategoryDAO {
    public static final String DATABASE_NAME = "K23411TSales.sqlite";
    public static final String TABLE_NAME = "Category";

    public static SQLiteDatabase database = null;

    public static ArrayList<Category> getCategories(Context context) {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Truy vấn dữ liệu từ bảng Category
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            String categoryId = cursor.getString(0);
            String categoryName = cursor.getString(1);
            String description = cursor.getString(2);
            categories.add(new Category(categoryId, categoryName, description));
        }

        cursor.close();
        database.close();

        return categories;
    }

    public static long insertCategory(Context context, Category category) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("CategoryId", category.getCategoryID());
        values.put("CategoryName", category.getCategoryName());
        long result = database.insert(TABLE_NAME, null, values);
        database.close();
        return result;
    }
    public static long deleteCategory(Context context, Category category) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        long result = database.delete(TABLE_NAME, "CategoryId=?", new String[]{category.getCategoryID()});
        database.close();
        return result;
    }
}