package com.test.networkvulnerablilitycheck;

import android.graphics.drawable.Drawable;

public class ChildListData {
    // ImageView에 상응
    public Drawable mChildItem;
    public String mChildText;

    public ChildListData(Drawable drawable, String string){
        mChildItem = drawable;
        mChildText = string;
    }

}
