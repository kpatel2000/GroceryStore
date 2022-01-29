package com.example.kpmall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static com.example.kpmall.SecondCartFragment.ORDER_KEY;

public class PaymentResultFragment extends Fragment {

    private TextView txtMessage;
    private Button btnShopping;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_result,container,false);

        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder,type);
                if (order != null) {
                    txtMessage.setText("Payment Was Successful\nYour Order will arrive Soon");
                    Utils.clearCartItems(getActivity());
                    for (GroceryItem item: order.getItems()){
                        Utils.increasePopularityPoint(getActivity(),item,1);
                        Utils.ChangeUserPoint(getActivity(),item,4);
                    }
                }else{
                    txtMessage.setText("Payment Failed,\nPlease try again after Sometime");
                }
            }
        }else{
            txtMessage.setText("Payment Failed,\nPlease try again after Sometime");
        }
        return view;
    }

    private void initViews(View view) {
        txtMessage = view.findViewById(R.id.txtMessage);
        btnShopping = view.findViewById(R.id.btnShopping);

    }
}
