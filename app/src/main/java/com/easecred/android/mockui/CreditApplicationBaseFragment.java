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
public abstract class CreditApplicationBaseFragment extends BaseFragment {

    protected Button mNext;
    protected Button mPrev;
    protected TextView mTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        mPrev = (Button)v.findViewById( R.id.previous);

        mNext = (Button) v.findViewById(R.id.next);

        mTitle = (TextView) v.findViewById(R.id.head_title);


        return v;
    }


}
