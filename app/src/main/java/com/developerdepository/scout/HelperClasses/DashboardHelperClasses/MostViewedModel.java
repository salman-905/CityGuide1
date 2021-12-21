package com.developerdepository.scout.HelperClasses.DashboardHelperClasses;

public class MostViewedModel {
    int image;
    String title, description;

    public MostViewedModel() {
    }

    public MostViewedModel(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
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
}
