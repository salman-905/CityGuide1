package com.developerdepository.scout.HelperClasses.DashboardHelperClasses;

import android.content.Intent;
import android.net.Uri;

public class FeaturedModel {
    int image;
    String title, description;
    double ratingBar;
    String url;
    public FeaturedModel() {
    }

    public FeaturedModel(int image, String title, String description, double ratingBar, String url) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.ratingBar = ratingBar;
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getRatingBar() {return ratingBar;}

    public String getUrl() {
        return url;
    }

}
