package com.example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amardeep on 10/22/2017.
 */

class RecipeIngredient implements Parcelable {
    private Double quantity;
    private String measure;
    private String ingredient;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    protected RecipeIngredient(Parcel in) {
        quantity = in.readByte() == 0x00 ? null : in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(quantity);
        }
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipeIngredient> CREATOR = new Parcelable.Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };
}
