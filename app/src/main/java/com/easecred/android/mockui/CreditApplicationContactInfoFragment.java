package com.easecred.android.mockui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.jar.Manifest;

/**
 * Created by android on 2/21/16.
 */
public class CreditApplicationContactInfoFragment  extends CreditApplicationBaseFragment {



    private EditText mPhone;
    private static final int READ_PHONE_NUMBER = 10;
    private static String sPhoneNumber = null;

    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_contact_info_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        mNext = (Button) v.findViewById(R.id.next);
        mNext.setText(R.string.submit);

        mPrev = (Button) v.findViewById(R.id.previous);
        mPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTitle = (TextView) v.findViewById(R.id.head_title);
        mTitle.setText(R.string.contact_info);

        mPhone = (EditText) v.findViewById(R.id.phone);
        updatePhoneNumber();
        ;
        return v;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.d("test", "hello" + requestCode);
        switch (requestCode) {

            case READ_PHONE_NUMBER:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sPhoneNumber = getPhoneNumber();
                    updateUI();
                    break;
                }
                else {
                    sPhoneNumber = "";
                }

        }

    }


    public void updateUI() {
        mPhone.setText(sPhoneNumber);
    }

    public void updatePhoneNumber() {

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_NUMBER);
        }
        else {
            sPhoneNumber = getPhoneNumber();
            updateUI();

        }
    }

    public String getPhoneNumber() {

        TelephonyManager tMgr = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phone ="";
        if (tMgr != null) {
            phone = tMgr.getLine1Number();
        }
        Log.d("test", phone);
        return phone;
    }


}
