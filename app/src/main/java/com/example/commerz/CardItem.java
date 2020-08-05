package com.example.commerz;

public class CardItem {
    private int imageResource;
    private String textTitle;
    private String textPrice;
    private String textLocation;

    public CardItem(int imageResource, String textTitle, String textPrice, String textLocation) {
        this.imageResource = imageResource;
        this.textTitle = textTitle;
        this.textPrice = textPrice;
        this.textLocation = textLocation;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public String getTextLocation() {
        return textLocation;
    }
}
