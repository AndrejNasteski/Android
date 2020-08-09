package com.example.commerz;

import java.util.Map;

public class Ad extends Throwable {
    private String Title;
    private String Details;
    private Long Price;
    private String StringLocation;
    private Boolean ShowMail;
    private Boolean ShowPhone;

    public Ad() {

    }

    public Ad(String Details, Long Price, Boolean ShowMail, Boolean ShowPhone, String StringLocation, String Title) {
        this.Title = Title;
        this.Details = Details;
        this.Price = Price;
        this.StringLocation = StringLocation;
        this.ShowMail = ShowMail;
        this.ShowPhone = ShowPhone;
    }

    public void addData(Map<String, Object> map) {
        this.Title = map.get("Title").toString();
        this.Details = map.get("Details").toString();
        this.Price = (Long) map.get("Title");
        this.StringLocation = map.get("StringLocation").toString();
        this.ShowMail = (Boolean) map.get("ShowMail");
        this.ShowPhone = (Boolean) map.get("ShowPhone");
    }

    public String getTitle() {
        return Title;
    }

    public String getDetails() {
        return Details;
    }

    public Long getPrice() {
        return Price;
    }

    public String getStringLocation() {
        return StringLocation;
    }

    public Boolean getShowMail() {
        return ShowMail;
    }

    public Boolean getShowPhone() {
        return ShowPhone;
    }
}
