package com.example.commerz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<CardItem> itemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewPrice;
        public TextView textViewLocation;

        public CardViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_card);
            textViewTitle = itemView.findViewById(R.id.title_text_card);
            textViewPrice = itemView.findViewById(R.id.price_text_card);
            textViewLocation = itemView.findViewById(R.id.location_text_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public CardAdapter(ArrayList<CardItem> list){
        itemList = list;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(v, mListener);
    }

    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = itemList.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textViewTitle.setText(currentItem.getTextTitle());
        holder.textViewPrice.setText(currentItem.getTextPrice());
        holder.textViewLocation.setText(currentItem.getTextLocation());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
