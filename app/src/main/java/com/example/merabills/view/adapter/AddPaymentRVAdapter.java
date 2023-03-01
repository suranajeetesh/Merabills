package com.example.merabills.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merabills.R;
import com.example.merabills.model.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AddPaymentRVAdapter extends RecyclerView.Adapter<AddPaymentRVAdapter.ViewHolder> {

    private final List<Transaction> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public AddPaymentRVAdapter(Context context, List<Transaction> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_add_payment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction item = mData.get(position);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        holder.myTextView.setText(item.getTitle() + " = " +  currencyFormatter.format(item.getAmounts()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView imgClose;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.txt_name);
            imgClose = itemView.findViewById(R.id.img_close);
            imgClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}