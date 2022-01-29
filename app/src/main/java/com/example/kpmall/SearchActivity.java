package com.example.kpmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.example.kpmall.AllCategoriesDialog.ALL_CATEGORIES;
import static com.example.kpmall.AllCategoriesDialog.CALLING_ACTIVITY;

public class SearchActivity extends AppCompatActivity implements AllCategoriesDialog.GetCategory {

    @Override
    public void onGetCategoryResult(String category) {
        ArrayList<GroceryItem> items = Utils.getItemsByCategory(this,category);
        if (items != null) {
            adapter.setItems(items);
            increaseUserPoint(items);
        }
    }

    private MaterialToolbar toolbar;
    private TextView txtFirstCategory,txtSecondCategory,txtThirdCategory,txtAllCategories;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private EditText searchBox;
    private ImageView btnSearch;
    private GroceryItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initBottomNavView();

        setSupportActionBar(toolbar);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        Intent intent = getIntent();
        if (intent != null) {
            String category = intent.getStringExtra("categories");
            if (category != null) {
                ArrayList<GroceryItem> items = Utils.getItemsByCategory(this, category);
                if (items != null) {
                    adapter.setItems(items);
                    increaseUserPoint(items);
                }
            }
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                initSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayList<String >categories = Utils.getCategories(this);
        if (categories != null) {
            if (categories.size()>0){
                if (categories.size() == 1){
                    showCategories(categories,1);
                }else if(categories.size() == 2){
                    showCategories(categories,2);
                }else{
                    showCategories(categories,3);
                }
            }
        }

        txtAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategoriesDialog dialog = new AllCategoriesDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ALL_CATEGORIES,Utils.getCategories(SearchActivity.this));
                bundle.putString(CALLING_ACTIVITY,"search_activity");
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"all categories dialog");
            }
        });
    }

    private void increaseUserPoint(ArrayList<GroceryItem> items){
        for (GroceryItem i: items){
            Utils.ChangeUserPoint(this,i,1);
        }
    }
    private void showCategories(final ArrayList<String> categories, int i) {
        switch (i){
            case 1:
                txtFirstCategory.setVisibility(View.VISIBLE);
                txtFirstCategory.setText(categories.get(0));
                txtSecondCategory.setVisibility(View.GONE);
                txtThirdCategory.setVisibility(View.GONE);
                txtFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });

            case 2:
                txtFirstCategory.setVisibility(View.VISIBLE);
                txtFirstCategory.setText(categories.get(0));
                txtSecondCategory.setVisibility(View.VISIBLE);
                txtSecondCategory.setText(categories.get(1));
                txtThirdCategory.setVisibility(View.GONE);
                txtFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                txtSecondCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(1));
                        if (null != items){
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });

            case 3:
                txtFirstCategory.setVisibility(View.VISIBLE);
                txtFirstCategory.setText(categories.get(0));
                txtSecondCategory.setVisibility(View.VISIBLE);
                txtSecondCategory.setText(categories.get(1));
                txtThirdCategory.setVisibility(View.VISIBLE);
                txtThirdCategory.setText(categories.get(2));
                txtFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                txtSecondCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(1));
                        if (null != items){
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });

                txtThirdCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this,categories.get(2));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });

            default:
                txtFirstCategory.setVisibility(View.GONE);
                txtSecondCategory.setVisibility(View.GONE);
                txtThirdCategory.setVisibility(View.GONE);
                break;
        }
    }

    private void initSearch() {
        if (!searchBox.getText().toString().equals("")){

            String name = searchBox.getText().toString();
            ArrayList<GroceryItem> items = Utils.searchForItems(this,name);
            if (items != null) {
                adapter.setItems(items);
                increaseUserPoint(items);
            }
        }
    }

    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent =new Intent(SearchActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case R.id.search:
                        break;

                    case R.id.cart:
                        Intent cartIntent = new Intent(SearchActivity.this,CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        searchBox = findViewById(R.id.searchBox);
        btnSearch = findViewById(R.id.btnSearch);
        txtFirstCategory = findViewById(R.id.txtFirstCategory);
        txtSecondCategory = findViewById(R.id.txtSecondCategory);
        txtThirdCategory = findViewById(R.id.txtThirdCategory);
        txtAllCategories = findViewById(R.id.txtAllCategories);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new GroceryItemAdapter(this);
    }
}