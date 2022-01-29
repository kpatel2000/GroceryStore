package com.example.kpmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {

    private boolean isBound;
    private TrackUserTime mService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder binder = (TrackUserTime.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    public void onAddReviewResult(Review review) {
        Utils.addReview(this,review);
        Utils.ChangeUserPoint(this,incomingItem,3);
        ArrayList<Review> reviews = Utils.getReviewsById(this,review.getGroceryItemId());
        if (reviews != null) {
            adapter.setReviews(reviews);
        }
    }

    public static final String GROCERY_ITEM_KEY = "incoming item";
    private TextView txtName,txtDescription,txtPrice,txtAddReview;
    private ImageView itemImage,firstEmptyStar,secondEmptyStar,thirdEmptyStar,firstFilledStar,secondFilledStar,thirdFilledStar;
    private Button btnAddToCart;
    private RecyclerView reviewsRecView;
    private RelativeLayout firstStarRelLayout,secondStarRelLayout,thirdStarRelLayout;
    private GroceryItem incomingItem;
    private MaterialToolbar toolbar;
    private ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);

        initViews();

        setSupportActionBar(toolbar);

        adapter = new ReviewsAdapter();
        Intent intent = getIntent();
        if (intent != null) {
            incomingItem = intent.getParcelableExtra(GROCERY_ITEM_KEY);
            if (null != incomingItem){
                Utils.ChangeUserPoint(this,incomingItem,1);
                txtName.setText(incomingItem.getName());
                txtDescription.setText(incomingItem.getDescription());
                txtPrice.setText(String.valueOf(incomingItem.getPrice() + "$"));
                Glide.with(this)
                        .asBitmap()
                        .load(incomingItem.getImageUrl())
                        .into(itemImage);

                ArrayList<Review> reviews = Utils.getReviewsById(this,incomingItem.getId());
                reviewsRecView.setAdapter(adapter);
                reviewsRecView.setLayoutManager(new LinearLayoutManager(this));

                if (reviews != null) {
                    if (reviews.size()>0){
                        ReviewsAdapter adapter = new ReviewsAdapter();
                        reviewsRecView.setAdapter(adapter);
                        reviewsRecView.setLayoutManager(new LinearLayoutManager(this));
                        adapter.setReviews(reviews);

                    }
                }

                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.addItemToCart(GroceryItemActivity.this,incomingItem);
                        Intent cartIntent = new Intent(GroceryItemActivity.this,CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        
                    }
                });

                txtAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddReviewDialog dialog = new AddReviewDialog();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(GROCERY_ITEM_KEY,incomingItem);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(),"add reviews");
                    }
                });

                handleRating();
            }
        }
    }

    private void handleRating() {
        switch (incomingItem.getRate()){
            case 0:
                firstEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;

            case 1:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;

            case 2:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;

            case 3:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
        }

        firstStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 1) {
                    Utils.changeRate(GroceryItemActivity.this,incomingItem.getId(),1);
                    Utils.ChangeUserPoint(GroceryItemActivity.this,incomingItem,(1-incomingItem.getRate())*2);
                    incomingItem.setRate(1);
                    handleRating();
                }
            }
        });

        secondStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 2) {
                    Utils.changeRate(GroceryItemActivity.this,incomingItem.getId(),2);
                    Utils.ChangeUserPoint(GroceryItemActivity.this,incomingItem,(2-incomingItem.getRate())*2);
                    incomingItem.setRate(2);
                    handleRating();
                }
            }
        });

        thirdStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 3) {
                    Utils.changeRate(GroceryItemActivity.this,incomingItem.getId(),3);
                    Utils.ChangeUserPoint(GroceryItemActivity.this,incomingItem,(3-incomingItem.getRate())*2);
                    incomingItem.setRate(3);
                    handleRating();
                }
            }
        });
    }

    private void initViews() {

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddReview = findViewById(R.id.txtAddReview);
        itemImage = findViewById(R.id.itemImage);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        reviewsRecView = findViewById(R.id.reviewsRecView);
        firstStarRelLayout = findViewById(R.id.firstStarRelLayout);
        secondStarRelLayout = findViewById(R.id.secondStarRelLayout);
        thirdStarRelLayout = findViewById(R.id.thirdStarRelLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this,TrackUserTime.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound){
            unbindService(connection);
        }
    }
}