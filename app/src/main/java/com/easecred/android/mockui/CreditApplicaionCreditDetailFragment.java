package com.easecred.android.mockui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by android on 2/21/16.
 */
public class CreditApplicaionCreditDetailFragment extends CreditApplicationBaseFragment  {


    private Spinner mSpinner;
    private AutoCompleteTextView mReason;

    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_loan_info_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreditApplicationActivity.class);
                intent.putExtra(CreditApplicationActivity.APPLICATION_PAGE_ID, CreditApplicationFragmentFactory.Page.JOB_INFO);
                startActivity(intent);
            }
        });


        mPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTitle.setText(R.string.credit_info);
        setUpTenureSpinner(v);
        setUpReason(v);
        return v;
    }


    private void setUpTenureSpinner(View v) {
        mSpinner = (Spinner) v.findViewById(R.id.tenure_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> values = new ArrayList<>();
        String suffix = " Month";
        for( int i = 1; i < 25; ++i ) {

            values.add( i +  suffix);
            if(i == 1) {
                suffix = " Months";
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,values);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }

    private void setUpReason (View v) {
        mReason = (AutoCompleteTextView) v.findViewById(R.id.reason);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity(),R.array.reason, android.R.layout.simple_dropdown_item_1line);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mReason.setAdapter(adapter);
    }
}


