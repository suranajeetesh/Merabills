package com.example.merabills.view.fragments;

import static com.example.merabills.utils.Utils.KEY_SAVE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merabills.R;
import com.example.merabills.model.Transaction;
import com.example.merabills.utils.CustomSharedPreferences;
import com.example.merabills.utils.Utils;
import com.example.merabills.view.adapter.AddPaymentRVAdapter;
import com.example.merabills.view.adapter.PaymentOptionAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Jeetesh Surana.
 */

public class AddPaymentFragment extends Fragment {

    private AddPaymentRVAdapter mAdapter;
    private View view;
    private final ArrayList<Transaction> data = new ArrayList<>();
    private final ArrayList<String> paymentOptions = new ArrayList<>();
    private Button btnSave;
    private TextView addPayment, totalValue;
    private String selectedTitle = "";
    private String cash = "";
    private Dialog dialog;
    private static final String FILENAME = "LastPayment.txt";
    private MyAsyncTask myAsyncTask;
    private CustomSharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_payment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        init(view);
    }


    private void init(@NonNull View view) {
        initPaymentOptions();
        pref = new CustomSharedPreferences(requireActivity());
        btnSave = view.findViewById(R.id.btn_save);
        addPayment = view.findViewById(R.id.txt_add_payment);
        totalValue = view.findViewById(R.id.txt_total_amount_value);
        clickManagement();
        if (pref.getBooleanValue(KEY_SAVE)) {
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute();
        }
    }

    private void clickManagement() {
        addPayment.setOnClickListener(v -> {
            if (!paymentOptions.isEmpty()) {
                addPaymentDialog();
            } else {
                Toast.makeText(requireContext(), getResources().getString(R.string.already_added), Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(v -> {
            if (!data.isEmpty()) {
                String fileData = (new Gson()).toJson(data);
                Utils.saveTextToFile(getActivity(), FILENAME, fileData);
                pref.setBooleanValue(KEY_SAVE, true);
                Toast.makeText(requireContext(), getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), getResources().getString(R.string.valid_save), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPaymentOptions() {
        cash = getResources().getString(R.string.cash);
        paymentOptions.add(getResources().getString(R.string.bank_transfer));
        paymentOptions.add(cash);
        paymentOptions.add(getResources().getString(R.string.credit_card));
        paymentOptions.add(getResources().getString(R.string.other));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.rv_dashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AddPaymentRVAdapter(getActivity(), data);
        mAdapter.setClickListener((view, position) -> {
            if (!paymentOptions.contains(data.get(position).getTitle())) {
                paymentOptions.add(data.get(position).getTitle());
            }
            data.remove(data.get(position));
            setTotalAmount();
            mAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTransactionIfNotExists(Transaction newTransaction) {
        boolean exists = data.stream().anyMatch(transaction -> transaction.getTitle().equals(newTransaction.getTitle()));
        if (!exists) {
            data.add(newTransaction);
            setTotalAmount();
            paymentOptions.remove(selectedTitle);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireContext(), getResources().getString(R.string.entry_already_added), Toast.LENGTH_SHORT).show();
        }
    }

    public void setSpinnerAdapter(Spinner spinner) {
        PaymentOptionAdapter adapter = new PaymentOptionAdapter(paymentOptions);
        spinner.setAdapter(adapter);
    }

    public void addPaymentDialog() {
        // create the custom dialog
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_payment);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // find the views in the layout
        EditText amount = dialog.findViewById(R.id.edt_amount);
        EditText bankDetails = dialog.findViewById(R.id.bank_details);
        EditText transactionRef = dialog.findViewById(R.id.transaction_reference);
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button okay = dialog.findViewById(R.id.btn_ok);
        Spinner spinner = dialog.findViewById(R.id.spinner);

        bankDetails.setVisibility(View.INVISIBLE);
        transactionRef.setVisibility(View.INVISIBLE);

        // set the adapter for the spinner
        setSpinnerAdapter(spinner);
        cancel.setOnClickListener(v -> dialog.dismiss());
        okay.setOnClickListener(v -> {
            String verifiedAmount = amount.getText().toString().trim();
            String verifiedBankDetails = bankDetails.getText().toString().trim();
            String verifiedTransactionRef = transactionRef.getText().toString().trim();
            if (verifiedAmount.isEmpty()) {
                Toast.makeText(requireContext(), getResources().getString(R.string.enter_amount), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!selectedTitle.equalsIgnoreCase(cash) && verifiedBankDetails.isEmpty()) {
                Toast.makeText(requireContext(), getResources().getString(R.string.enter_bank_details), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!selectedTitle.equalsIgnoreCase(cash) && verifiedTransactionRef.isEmpty()) {
                Toast.makeText(requireContext(), getResources().getString(R.string.enter_transaction), Toast.LENGTH_SHORT).show();
                return;
            }

            Transaction transaction;
            if (!selectedTitle.equalsIgnoreCase(cash)) {
                transaction = new Transaction(selectedTitle, verifiedAmount, verifiedBankDetails, verifiedTransactionRef);
            } else {
                transaction = new Transaction(selectedTitle, verifiedAmount, "", "");
            }
            addTransactionIfNotExists(transaction);
            dialog.dismiss();
        });

        spinnerSelection(bankDetails, transactionRef, spinner);

        // show the dialog
        dialog.show();

    }

    private void spinnerSelection(EditText bankDetails, EditText transactionRef, Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTitle = parentView.getItemAtPosition(position).toString();
                boolean isShow = !Objects.equals(selectedTitle, cash);
                setDetailsVisibility(isShow, bankDetails, transactionRef);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do something when nothing is selected
            }
        });
    }

    private void setDetailsVisibility(Boolean isShow, EditText bankDetails, EditText transactionRef) {
        if (isShow) {
            bankDetails.setVisibility(View.VISIBLE);
            transactionRef.setVisibility(View.VISIBLE);
        } else {
            bankDetails.setText("");
            transactionRef.setText("");
            bankDetails.setVisibility(View.INVISIBLE);
            transactionRef.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setTotalAmount() {
        BigDecimal sum = data.stream().map(Transaction::getAmounts).reduce(BigDecimal.ZERO, BigDecimal::add);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedAmount = currencyFormatter.format(sum);
        totalValue.setMovementMethod(new ScrollingMovementMethod());
        totalValue.setText(formattedAmount);
    }

    @Override
    public void onPause() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onPause();
    }

    private static class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private final WeakReference<AddPaymentFragment> fragment;

        // only retain a weak reference to the fragment
        MyAsyncTask(AddPaymentFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return Utils.loadTextFromFile(fragment.get().getActivity(), FILENAME);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(String json) {
            AddPaymentFragment paymentFragment = fragment.get();
            if (paymentFragment == null || paymentFragment.isVisible())
                return;
            if (json != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Transaction>>() {
                }.getType();
                ArrayList<Transaction> list = gson.fromJson(json, type);
                if (!list.isEmpty()) {
                    fragment.get().data.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        fragment.get().paymentOptions.remove(list.get(i).getTitle());
                    }
                    fragment.get().mAdapter.notifyDataSetChanged();
                    fragment.get().setTotalAmount();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            myAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}
