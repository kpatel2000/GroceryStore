package com.example.kpmall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondCartFragment extends Fragment {

    public static final String ORDER_KEY = "order_key";
    private EditText edtAddress,edtZipCode,edtPhoneNumber,edtEmail;
    private Button btnNext,btnBack;
    private TextView txtWarning;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_second,container,false);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder,type);
                if (order != null) {
                    edtAddress.setText(order.getAddress());
                    edtPhoneNumber.setText(order.getPhoneNumber());
                    edtEmail.setText(order.getEmail());
                    edtZipCode.setText(order.getZipCode());
                }
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,new FirstCartFragment());
                transaction.commit();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    txtWarning.setVisibility(View.GONE);

                    ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
                    if (null != cartItems){
                        Order order = new Order();
                        order.setItems(cartItems);
                        order.setAddress(edtAddress.getText().toString());
                        order.setPhoneNumber(edtPhoneNumber.getText().toString());
                        order.setEmail(edtEmail.getText().toString());
                        order.setZipCode(edtZipCode.getText().toString());
                        order.setTotalPrice(calculateTotalPrice(cartItems));

                        Gson gson = new Gson();
                        String jsonOrder = gson.toJson(order);
                        Bundle bundle = new Bundle();
                        bundle.putString(ORDER_KEY,jsonOrder);

                        ThirdCartFragment thirdCartFragment = new ThirdCartFragment();
                        thirdCartFragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,thirdCartFragment);
                        transaction.commit();
                    }
                }else{
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please Fill All the Fields");
                }
            }
        });
        return view;
    }

    private double calculateTotalPrice(ArrayList<GroceryItem> items){
        double price = 0;
        for (GroceryItem item: items){
            price += item.getPrice();
        }
        price = Math.round(price*100.00)/100.00;
        return price;
    }

    private boolean validateData() {
        if (edtAddress.getText().toString().equals("")||edtPhoneNumber.getText().toString().equals("")||
            edtZipCode.getText().toString().equals("")||edtEmail.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    private void initViews(View view) {
        edtAddress = view.findViewById(R.id.edtAddress);
        edtZipCode = view.findViewById(R.id.edtZipCode);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnBack = view.findViewById(R.id.btnBack);
        btnNext = view.findViewById(R.id.btnNext);
        txtWarning = view.findViewById(R.id.txtWarning);
    }
}
