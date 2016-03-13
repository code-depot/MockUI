package com.easecred.android.mockui;

import android.support.v4.app.Fragment;

/**
 * Created by android on 2/21/16.
 */
public class CreditApplicationFragmentFactory {

    public enum  Page {
        PERSONAL_INFO,
        LOAN_INFO,
        BANK_INFO,
        JOB_INFO,
        UPLOAD_DOC,
        CONTACT_INFO
    }


    public static Fragment getCreditApplicationFragment(Page pageId) {

        switch(pageId) {
            case PERSONAL_INFO:
                return new CreditApplicationPersonalInfoFragment();

            case LOAN_INFO:
                return new CreditApplicaionCreditDetailFragment();

            case JOB_INFO:
                return new CreditApplicationJobInfoFragment();

            case BANK_INFO:
                return new CreditApplicationBankDetailFragment();

            case UPLOAD_DOC:
                return new CreditApplicationUploadDocFragment();

            case CONTACT_INFO:
                return new CreditApplicationContactInfoFragment();



        }
        return null;

    }



}
