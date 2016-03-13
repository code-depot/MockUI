package com.easecred.android.mockui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by android on 2/27/16.
 */
public class CreditApplicationUploadDocFragment extends CreditApplicationBaseFragment {


    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_upload_doc_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreditApplicationActivity.class);
                intent.putExtra(CreditApplicationActivity.APPLICATION_PAGE_ID, CreditApplicationFragmentFactory.Page.CONTACT_INFO);
                startActivity(intent);
            }
        });


        mPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTitle.setText(R.string.upload_doc);

        return v;
    }

}
