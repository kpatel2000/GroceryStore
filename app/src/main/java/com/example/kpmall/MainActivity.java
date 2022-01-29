package com.example.kpmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import static com.example.kpmall.AllCategoriesDialog.ALL_CATEGORIES;
import static com.example.kpmall.AllCategoriesDialog.CALLING_ACTIVITY;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
//        Utils.initSharedPreferences(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.drawer_open,R.string.drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cart:
                        Intent cartIntent = new Intent(MainActivity.this,CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;

                    case R.id.categories:
                        AllCategoriesDialog dialog = new AllCategoriesDialog();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(ALL_CATEGORIES, Utils.getCategories(MainActivity.this));
                        bundle.putString(CALLING_ACTIVITY,"main_activity");
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(),"all categories dialog");
                        break;

                    case R.id.about:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("About Us")
                                .setMessage("Designed and Developed by KP at KPCorporation\n"+
                                        "Visit for More")
                                .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent webIntent = new Intent(MainActivity.this,WebsiteActivity.class);
                                        startActivity(webIntent);
                                    }
                                }).create().show();
                        break;

                    case R.id.terms:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Terms")
                                .setMessage("Ther are no Terms, Enjoy using the application")
                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;

                    case R.id.licence:
                        LicenceDialog licenceDialog = new LicenceDialog();
                        licenceDialog.show(getSupportFragmentManager(),"licences");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new MainFragment());
        transaction.commit();
    }

    private void initViews() {

        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
    }
}