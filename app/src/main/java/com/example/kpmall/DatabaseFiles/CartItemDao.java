package com.example.kpmall.DatabaseFiles;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.kpmall.GroceryItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Query("INSERT INTO cart_items(groceryItemId) VALUES (:id)")
    void insert(int id);

    @Query("SELECT grocery_items.id, grocery_items.name, grocery_items.description, " +
            "grocery_items.imageUrl, grocery_items.category, grocery_items.price," +
            "grocery_items.availableAmount, grocery_items.rate, grocery_items.userPoint," +
            "grocery_items.popularityPoint, grocery_items.reviews FROM grocery_items " +
            "INNER JOIN cart_items ON cart_items.groceryItemId = grocery_items.id")
    List<GroceryItem> getAllCartItems();

    @Query("DELETE FROM cart_items WHERE groceryItemId=:id")
    void deleteItemById(int id);

    @Query("DELETE FROM cart_items")
    void clearCart();
}
