package com.easecred.android.mockui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by android on 2/27/16.
 */
public class CreditApplicationJobInfoFragment  extends CreditApplicationBaseFragment{


    private Spinner mSpinner;
    private ViewGroup mJodContent;
    private Button mJodDate;

    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_job_info_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        mNext = (Button) v.findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreditApplicationActivity.class);
                intent.putExtra(CreditApplicationActivity.APPLICATION_PAGE_ID, CreditApplicationFragmentFactory.Page.BANK_INFO);
                startActivity(intent);
            }
        });
        ;

        mPrev = (Button) v.findViewById(R.id.previous);
        mPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTitle = (TextView) v.findViewById(R.id.head_title);
        mTitle.setText(R.string.job_info);

        mJodDate = (Button) v.findViewById(R.id.jod_date);
        mJodDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });



        mJodContent  =  (ViewGroup)v.findViewById(R.id.jod_content);
        setupEmpStatusSpinner(v);



        return v;
    }


    private void setupEmpStatusSpinner(View v) {
        mSpinner = (Spinner)v.findViewById(R.id.emp_status_spinner);
        mSpinner = (Spinner) v.findViewById(R.id.emp_status_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity(),R.array.status_array, android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) parent.getAdapter();
                String value = adapter.getItem(position).toString();

                if (getString(R.string.noob).equals(value)) {
                    mJodContent.setVisibility(View.VISIBLE);
                } else {
                    mJodContent.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
                Log.d("JOBINFO","nothing selected");
                mJodContent.setVisibility(View.GONE);
            }
        });
    }

    public  class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return  new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog, this, year,month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            StringBuilder format = new StringBuilder();
            format.append(day).append("-").append(month).append("-").append(year);
            mJodDate.setText(format.toString());

        }


    }

}
