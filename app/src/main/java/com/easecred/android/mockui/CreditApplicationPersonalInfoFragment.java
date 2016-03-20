package com.easecred.android.mockui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by android on 2/19/16.
 */
public class CreditApplicationPersonalInfoFragment extends CreditApplicationBaseFragment {


    private Button mDob;
    private AppCompatSpinner mGender;
    private ImageButton mProfileButton;
    private ImageButton mPhotoIcon;

    private boolean mIsCameraEnabled;
    private String mTempPhotoFile;
    private String mCropPhotoFile;


    //ui stuff
    private EditText mFirstName;
    private EditText mLastName;
    private RadioButton mMale;
    private RadioButton mFemale;

    public static final int REQUEST_SNAP_IMAGE = 1;
    public static final int REQUEST_LOAD_IMAGE = 2;
    public static final int REQUEST_CROP_IMAGE = 3;


    private static final String TAG = "CreditPersonalInfo";

    @Override
    public int getFragmentLayoutID() {
        return R.layout.credit_application_personal_info_layout;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsCameraEnabled = CreditPhotoUtils.isCameraIntentRegistered((BaseActivity) getActivity());


        if(mIsCameraEnabled) {
            mTempPhotoFile = getClickedPhotoPath();
            mCropPhotoFile = getClickedPhotoPath();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        mPrev.setVisibility(View.GONE);
        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreditApplicationActivity.class);
                intent.putExtra(CreditApplicationActivity.APPLICATION_PAGE_ID, CreditApplicationFragmentFactory.Page.LOAN_INFO);
                startActivity(intent);
            }
        });
        mTitle.setText(R.string.personal_info);

        mDob = (Button)v.findViewById(R.id.dob);
        mDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.setTargetFragment(CreditApplicationPersonalInfoFragment.this, 0);
                fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }


        });

        mProfileButton = (ImageButton) v.findViewById(R.id.photo);
        mPhotoIcon= (ImageButton)v.findViewById(R.id.photo_icon);

        mProfileButton.setOnClickListener(new PhotoClick());
        mPhotoIcon.setOnClickListener(new PhotoClick());

        mFirstName = (EditText)v.findViewById(R.id.first_name);
        mLastName = (EditText)v.findViewById(R.id.last_name);
        mMale = (RadioButton)v.findViewById(R.id.male);
        mFemale = (RadioButton)v.findViewById(R.id.female);
        onProfileUpdate();

        return v;
    }


        public void onProfileUpdate() {
            UserAccountInfo profile  = UserAccountInfo.getInstance();

            mFirstName.setText(profile.getFirstName());
            mLastName.setText(profile.getLastName());
            if(profile.getGender() == UserAccountInfo.Gender.FEMALE) {
                mFemale.setChecked(true);
            }
            else {
                mMale.setChecked(true);
            }
       }



    public void onPhotoOK(UploadPhotoDialog.PhotoChoice choice) {

        switch (choice) {

            case  TAKE: {
                Log.d("PersonalInfo", "take");
                Uri uri = Uri.fromFile(new File(mTempPhotoFile));
                Intent captureIntent = CreditPhotoUtils.getPhotoPickerIntent(uri);
                startActivityForResult(captureIntent, REQUEST_SNAP_IMAGE);
                break;
            }
            case  UPLOAD: {
                Log.d("PersonalInfo", "upload");
                Uri uri = Uri.fromFile(new File(mTempPhotoFile));
                Intent galleryIntent = CreditPhotoUtils.getPhotoGalleryIntent(uri);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_LOAD_IMAGE);
                break;
            }
            case  REMOVE: {
                Log.d("PersonalInfo", "remove");
                Uri uri = Uri.fromFile(new File(mTempPhotoFile));
                CreditPhotoUtils.deletePhoto(getActivity(),uri);
                break;
            }
        }
    }

    private void updateDOB(int year,int month,int day) {
        StringBuilder format = new StringBuilder();
        format.append(day).append("-").append(month).append("-").append(year);
        mDob.setText(format.toString());
    }

    private String getClickedPhotoPath() {
        File externalFilesDir = getActivity()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(externalFilesDir,"self_photo_" + UUID.randomUUID() + ".img").getPath();
    }


    public  static class DatePickerFragment extends DialogFragment
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
            ((CreditApplicationPersonalInfoFragment)getTargetFragment()).updateDOB(year, month, day);

        }
    }


    public class PhotoClick implements  View.OnClickListener {
        @Override
        public void onClick(View v) {
            DialogFragment fragment = new UploadPhotoDialog();
            fragment.setTargetFragment(CreditApplicationPersonalInfoFragment.this, 0);
            fragment.show(getActivity().getSupportFragmentManager(), "Photo");
        }
    }

    public static class UploadPhotoDialog extends DialogFragment {

        private RadioGroup mChoice;

        enum PhotoChoice {
            UPLOAD,
            TAKE,
            REMOVE

        };

        private PhotoChoice mPhotoChoice = PhotoChoice.TAKE;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo, null);


            mChoice = (RadioGroup) v.findViewById(R.id.photo_set_view);
            mChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.snap) {
                        Log.d("personalinfo", "snap");
                        mPhotoChoice = PhotoChoice.TAKE;
                    } else if (checkedId == R.id.upload) {
                        Log.d("personalinfo", "upload");
                        mPhotoChoice = PhotoChoice.UPLOAD;
                    } else if (checkedId == R.id.remove) {
                        Log.d("personalinfo", "remove");
                        mPhotoChoice = PhotoChoice.REMOVE;
                    }
                }
            });


            AlertDialog  alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),android.R.style.Theme_Holo_Dialog))
                    .setTitle("Update Photo")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((CreditApplicationPersonalInfoFragment)getTargetFragment()).onPhotoOK(mPhotoChoice);
                        }

                    })
                    .setNegativeButton("Cancel", null)
                    .setView(v).setCancelable(false)
                    .create();

            return alertDialog;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("PersonalInfo", "The result code is " + resultCode + " "  + requestCode);
        if(resultCode == Activity.RESULT_OK) {

            String logMsg = "Load_Image";
            switch(requestCode) {

                case REQUEST_SNAP_IMAGE:
                    logMsg = "Snap_Image";
                case REQUEST_LOAD_IMAGE :
                    Log.d(TAG,logMsg);
                    final Uri inputUri;
                    if (data != null && data.getData() != null) {
                        Uri extUri = data.getData();
                        inputUri = Uri.fromFile(new File(mTempPhotoFile));
                        Log.d("PersonalInfo", "strring data" + inputUri.toString() + " path " + inputUri.getPath());
                        CreditPhotoUtils.savePhotoFromUriToUri(getActivity(),extUri,inputUri,false);
                     } else {
                        inputUri = Uri.fromFile(new File(mTempPhotoFile));
                        Log.d("PersonalInfo", "strring" + inputUri.toString() + " path " + inputUri.getPath());
                    }
                    InputStream in = null;
                    try {
                        Uri cropUri = Uri.fromFile(new File(mCropPhotoFile));
                        startActivityForResult(CreditPhotoUtils.getCropImageIntent(inputUri,cropUri, getPhotoPickSize()), REQUEST_CROP_IMAGE);

                    } catch (Exception e) {
                        Log.d("PersonalInfo", e.getMessage());
                    }
                    finally {
                        Closeables.closeQuietly(in);
                    }
                    break;

                case REQUEST_CROP_IMAGE:
                    final Uri cropUri;
                    if (data != null && data.getData() != null) {
                        Uri extUri = data.getData();
                        cropUri = Uri.fromFile(new File(mCropPhotoFile));
                        Log.d("PersonalInfo", "load crop data file" + cropUri.toString() + " path " + cropUri.getPath());
                        CreditPhotoUtils.savePhotoFromUriToUri(getActivity(),extUri,cropUri,false);
                    } else {
                        cropUri = Uri.fromFile(new File(mCropPhotoFile));
                        Log.d("PersonalInfo", "load crop default file" + cropUri.toString() + " path " + cropUri.getPath());
                    }

                    try {
                        Bitmap imageBitmap = CreditPhotoUtils.getBitmapFromUri(getActivity(), cropUri);
                        mProfileButton.setImageBitmap(imageBitmap);
                    }
                    catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                        Toast.makeText(getActivity(),"oops something went wrong while loading image",Toast.LENGTH_SHORT).show();
                    }

            }
        }

    }


    private int getPhotoPickSize() {

        return 720;
    }


    private BroadcastReceiver mOnLoginComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent filter) {
            int loginStatus = filter.getIntExtra(LoginManager.MOCK_UI_EVENTS,LoginManager.PROFILE_UPDATE_FAILED);

            if(loginStatus == LoginManager.PROFILE_UPDATE_SUCCESS) {
                onProfileUpdate();

            }

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(LoginManager.LOGIN_NOTIFCATION);
        getActivity().registerReceiver(mOnLoginComplete, filter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mOnLoginComplete);
        super.onStop();
    }


}
