package com.detatech.vitaluser.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.detatech.vitaluser.R;

/**
 * Created by Arbab on 7/17/2019.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
    }
}
