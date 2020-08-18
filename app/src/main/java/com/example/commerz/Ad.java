package com.example.commerz;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Ad {

    private String title;
    private String details;
    private Long price;
    private String stringLocation;
    private String category;
    private Boolean showMail;
    private Boolean showPhone;
    private String creatorUID;
    private Date dateCreated;
    private String currency;
    private String imageUri;

    public Ad() {
    }

    public Ad(String details, Long price, String currency, String category, Boolean showMail, Boolean showPhone,
              String stringLocation, String title, String creatorUID, Date dateCreated, String imageUri) {
        this.currency = currency;
        this.category = category;
        this.title = title;
        this.details = details;
        this.price = price;
        this.stringLocation = stringLocation;
        this.showMail = showMail;
        this.showPhone = showPhone;
        this.creatorUID = creatorUID;
        this.dateCreated = dateCreated;
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDetails() {
        return details;
    }

    public Long getPrice() {
        return price;
    }

    public String getStringLocation() {
        return stringLocation;
    }

    public String getCategory() {
        return category;
    }

    public Boolean getShowMail() {
        return showMail;
    }

    public Boolean getShowPhone() {
        return showPhone;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}
