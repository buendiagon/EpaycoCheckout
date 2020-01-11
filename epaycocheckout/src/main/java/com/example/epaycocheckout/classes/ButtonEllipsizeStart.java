package com.example.epaycocheckout.classes;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ButtonEllipsizeStart extends MaterialButton {
    public ButtonEllipsizeStart(@NonNull Context context) {
        super(context);
    }

    public ButtonEllipsizeStart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonEllipsizeStart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        super.setEllipsize(TextUtils.TruncateAt.START);
    }
}
