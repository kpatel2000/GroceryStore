package com.example.kpmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView newItemsRecView, popularItemsRecView, suggestedItemsRecView;
    private GroceryItemAdapter newItemsAdapter, popularItemsAdapter, suggestedItemsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initView(view);
        initBottomNavView();

//        Utils.clearSharedPreferences(getActivity());

        return view;
    }

    private void initRecViews() {

        newItemsAdapter = new GroceryItemAdapter(getActivity());
        newItemsRecView.setAdapter(newItemsAdapter);
        newItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        popularItemsAdapter = new GroceryItemAdapter(getActivity());
        popularItemsRecView.setAdapter(popularItemsAdapter);
        popularItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        suggestedItemsAdapter = new GroceryItemAdapter(getActivity());
        suggestedItemsRecView.setAdapter(suggestedItemsAdapter);
        suggestedItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        ArrayList<GroceryItem> allItems = Utils.getAllItems(getActivity());
        if (allItems != null) {
            final Comparator<GroceryItem> newItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem o1, GroceryItem o2) {
//                    return newItemsComparator(o1,o2);
                    return o1.getId() - o2.getId();
                }
            };

//            Collections.sort(allItems,newItemsComparator);
            Comparator<GroceryItem> reverseComparator = Collections.reverseOrder(newItemsComparator);
            Collections.sort(allItems, reverseComparator);
            newItemsAdapter.setItems(allItems);
        }

        ArrayList<GroceryItem> popularItems = Utils.getAllItems(getActivity());
        if (popularItems != null) {
            Comparator<GroceryItem> popularItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem o1, GroceryItem o2) {
//                    return newItemsComparator(o1,o2);
                    return o1.getPopularityPoint() - o2.getPopularityPoint();
                }
            };

            Collections.sort(popularItems, Collections.reverseOrder(popularItemsComparator));
            popularItemsAdapter.setItems(popularItems);
        }

        ArrayList<GroceryItem> suggestedItems = Utils.getAllItems(getActivity());
        if (suggestedItems != null) {
            Comparator<GroceryItem> suggestedItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem o1, GroceryItem o2) {
                    return o1.getUserPoint() - o2.getUserPoint();
                }
            };

            Collections.sort(suggestedItems, Collections.reverseOrder(suggestedItemsComparator));
            suggestedItemsAdapter.setItems(suggestedItems);

        }

    }

//    private int newItemsComparator(GroceryItem o1, GroceryItem o2) {
//
//        if (o1.getId()> o2.getId()){
//            return 1;
//        }else if(o1.getId()<o2.getId()){
//            return -1;
//        }else{
//            return 0;
//        }
//    }

    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.home);  //By Default home selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;

                    case R.id.search:
                        Intent intent = new Intent(getActivity(),SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case R.id.cart:
                        Intent cartIntent = new Intent(getActivity(),CartActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
        initRecViews();
    }

    private void initView(View view) {

        bottomNavigationView = view.findViewById(R.id.bottomNavigation);
        newItemsRecView = view.findViewById(R.id.newItemsRecView);
        popularItemsRecView = view.findViewById(R.id.popularItemsRecView);
        suggestedItemsRecView = view.findViewById(R.id.suggestedItemsRecView);
    }
}
