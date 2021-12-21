package com.developerdepository.scout.HelperClasses.DashboardHelperClasses;

public class CategoriesModel {
    int background, image;
    String title;

    public CategoriesModel() {
    }

    public CategoriesModel(int background, int image, String title) {
        this.background = background;
        this.image = image;
        this.title = title;
    }

    public int getBackground() {
        return background;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
