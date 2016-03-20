package com.easecred.android.mockui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.FacebookSdk;

/**
 * Created by android on 3/19/16.
 */
public class  UserAccountInfo {

    private static UserAccountInfo sUserAccountInfo;

    public  static UserAccountInfo getInstance() {

        if(sUserAccountInfo == null) {
            sUserAccountInfo =  new UserAccountInfo();
        }
        return sUserAccountInfo;
    }



    public enum Gender {
        MALE,
        FEMALE,
        UNKOWN
    }


    private String mCustomerId;
    private String mFirstName;
    private String mEmail;
    private Gender mGender;
    private String mLastName;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Gender getGender() {
        return mGender;
    }

    public void setGender(Gender gender) {
        mGender = gender;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }


}

