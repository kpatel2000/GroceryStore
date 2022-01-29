package com.example.kpmall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.kpmall.GroceryItemActivity.GROCERY_ITEM_KEY;

public class AddReviewDialog extends DialogFragment {

    public interface AddReview{
        void onAddReviewResult(Review review);
    }

    private TextView txtItemName,txtWarning;
    private EditText edtUserName;
    private EditText edtReviews;
    private Button btnAddReviews;
    private AddReview addReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_review,null);
        initViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            GroceryItem item = bundle.getParcelable(GROCERY_ITEM_KEY);
            if (item != null) {
                txtItemName.setText(item.getName());
                btnAddReviews.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {

                        String userName = edtUserName.getText().toString();
                        String review = edtReviews.getText().toString();
                        String date = getCurrentDate();
                        if (userName.equals("") || review.equals("")) {
                            txtWarning.setText("Fill all The Blanks");
                            txtWarning.setVisibility(View.VISIBLE);
                        } else {
                            txtWarning.setVisibility(View.GONE);
                            try {
                                addReview = (AddReview) getActivity();
                                addReview.onAddReviewResult(new Review(item.getId(), userName, review, date));
                                dismiss();
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }

        return builder.create();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("MM-DD-YYYY");
        }
        return sdf.format(calendar.getTime());
    }

    private void initViews(View view) {

        txtItemName = view.findViewById(R.id.txtItemName);
        txtWarning = view.findViewById(R.id.txtWarning);
        edtUserName = view.findViewById(R.id.edtUserName);
        edtReviews = view.findViewById(R.id.edtReviews);
        btnAddReviews = view.findViewById(R.id.btnAddReview);
    }
}
