package com.easecred.android.mockui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

public class MessageActivity extends BaseActivity {

    private static final String TAG = "MOCKUI_MESSAGE_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragmentInstance() {
        return new MessageFragment();

    }

    @Override
    protected void startMessageActivity() {

    }

    @Override
    protected void startCreditAppActivity() {

        Log.d(TAG, "profile");
        Intent intent = new Intent(this,CreditApplicationActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getSelectedDrawerItem() {
        return R.id.message;
    }
}