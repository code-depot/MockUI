package com.easecred.android.mockui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public  class CreditApplicationActivity extends BaseActivity {

    private static final String TAG = "MOCKUI_PROFILE_ACTIVITY";
    public static final String APPLICATION_PAGE_ID = "ApplicationPageId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void startCreditAppActivity() {

    }

    @Override
    protected void startMessageActivity() {
        Log.d(TAG, "message");
        Intent intent = new Intent(this,MessageActivity.class);
        startActivity(intent);

    }

    @Override
    protected int getSelectedDrawerItem() {
        return R.id.credit_apply;
    }


    @Override
    protected Fragment getFragmentInstance() {
        Intent intent = getIntent();

        CreditApplicationFragmentFactory.Page pageId = (CreditApplicationFragmentFactory.Page)intent.getSerializableExtra(APPLICATION_PAGE_ID);

        if(pageId == null) {
            Log.d(TAG,"in get Fragment Instance" + pageId );
            pageId = CreditApplicationFragmentFactory.Page.PERSONAL_INFO;
        }
        else {
            Log.d(TAG,"no pageid :" );
        }
        return CreditApplicationFragmentFactory.getCreditApplicationFragment(pageId);
    }


}
