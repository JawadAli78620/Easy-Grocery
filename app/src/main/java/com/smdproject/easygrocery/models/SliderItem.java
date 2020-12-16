package com.smdproject.easygrocery.models;

import android.net.Uri;

public class SliderItem {

    private String description;
    private Uri imageUri;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
