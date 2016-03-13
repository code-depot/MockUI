package com.easecred.android.mockui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by android on 2/21/16.
 */
public class CreditApplicationBankDetailFragment extends CreditApplicationBaseFragment {

    private CheckBox mCheckBox;
    private ViewGroup mOptionalContent;

    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_bank_info_layout;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CreditApplicationBankDetailFragment.this.startNextActivity();
            }
        });

        mOptionalContent = (ViewGroup)v.findViewById(R.id.optional_content);
        mCheckBox = (CheckBox)v.findViewById(R.id.offline_check);

        if(mCheckBox.isChecked()) {
            //mOptionContent.setVisibility(View.GONE);
        }

        mCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox c = (CheckBox)buttonView;
                if(c.isChecked()) {
                    mOptionalContent.setVisibility(View.GONE);
                }
                else {
                    mOptionalContent.setVisibility(View.VISIBLE);
                }
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTitle.setText(R.string.bank_info);

        return v;
    }

    public void startNextActivity() {
        Intent intent = new Intent(getActivity(), CreditApplicationActivity.class);
        intent.putExtra(CreditApplicationActivity.APPLICATION_PAGE_ID, CreditApplicationFragmentFactory.Page.UPLOAD_DOC);
        startActivity(intent);
    }



}
