package com.example.commerz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> imageUriList;


    public ImageAdapter(Context context, List<String> uriArrayList) {
        mContext = context;

        imageUriList = new ArrayList<>();
        if (!uriArrayList.isEmpty()) {
            for (int i = 0; i < uriArrayList.size(); i++) {
                imageUriList.add("https://firebasestorage.googleapis.com/v0/b/commerz-2ca14.appspot.com/o/images%2F" +
                        uriArrayList.get(i) + "?alt=media"
                );
            }
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        Picasso.get()
                .load(imageUriList.get(position))
                .fit()
                .placeholder(R.drawable.ic_loud_upload)
                .centerCrop()
                .into(imageView);
        container.addView(imageView);


        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
