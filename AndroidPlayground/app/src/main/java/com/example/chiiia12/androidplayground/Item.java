package com.example.chiiia12.androidplayground;

import android.databinding.BaseObservable;

public class Item extends BaseObservable {

    public final String text;

    public Item(String text) {
        this.text = text;
    }
}
