package com.example.commerz;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AdCardAdapter extends FirestoreRecyclerAdapter<Ad, AdCardAdapter.AdCardHolder> {
    private OnItemClickListener listener;

    public AdCardAdapter(@NonNull FirestoreRecyclerOptions<Ad> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull AdCardHolder holder, int position, @NonNull Ad model) {
        holder.textTitle.setText(model.getTitle());
        holder.textLocation.setText(model.getStringLocation());
        holder.textPrice.setText(model.getPrice().toString() + " " + model.getCurrency());
        String stringUri = "https://firebasestorage.googleapis.com/v0/b/commerz-2ca14.appspot.com/o/images%2F" +
                model.getImageUri() + "?alt=media";
        Uri u = Uri.parse(stringUri);
        Picasso.get()
                .load(u)
                .fit()
                .placeholder(R.drawable.ic_loud_upload)
                .centerCrop()
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public AdCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,
                parent, false);
        return new AdCardHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class AdCardHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textPrice;
        TextView textLocation;
        ImageView imageView;

        public AdCardHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.title_text_card);
            textPrice = itemView.findViewById(R.id.price_text_card);
            textLocation = itemView.findViewById(R.id.location_text_card);
            imageView = itemView.findViewById(R.id.image_view_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }
}
