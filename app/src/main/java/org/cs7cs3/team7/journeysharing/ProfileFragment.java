package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private static final String NAME = "names";
    private static final String PHONE = "phone";
    private static final String GENDER_POSITION = "SelectedPosition";
    private static final String PROFILE = "profile shared preferences";

    private MainViewModel mViewModel;
    private SharedPreferences sharedPreferences;

    private int genderItemIndexSelected = 0;

    // Create an anonymous implementation of OnClickListener
    private Button.OnClickListener mSaveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            save();
        }
    };

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final Button button = view.findViewById(R.id.save_button);

        // Keep the spinner selected listener works before press the "save" button.
        View userInfoLayout = view.findViewById(R.id.user_info_include);
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myTag", "Gender Selected: " + parent.getItemAtPosition(position).toString());
                genderItemIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        button.setOnClickListener(mSaveOnClickListener);
        //updateView();
    }

    private void save() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        Log.d("myTag", "This is my message from button");
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        mViewModel.getNames().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                namesEditText.setText(msg);
            }
        });
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        mViewModel.getPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                phoneEditText.setText(msg);
            }
        });
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        mViewModel.getGenderItemIndexSelected().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer msg) {
                genderSpinner.setSelection(msg);
            }
        });
        Log.d("myTag", "Name Saved: " + namesEditText.getText().toString());
        Log.d("myTag", "Phone Saved: " + phoneEditText.getText().toString());
        Log.d("myTag", "Gender saved: " + genderSpinner.getSelectedItem());
        mViewModel.setNames(namesEditText.getText().toString());
        mViewModel.setPhone(phoneEditText.getText().toString());
        mViewModel.setGenderItemIndexSelected(genderItemIndexSelected);
        saveProfileToLocal();
        Toast.makeText(this.getActivity(), "Profile Saved!", Toast.LENGTH_SHORT).show();
        Log.d("myTag", "Name Saved? " + sharedPreferences.getString(NAME, "").equals(mViewModel.getNames().getValue()));
        Log.d("myTag", "Phone Saved? " + sharedPreferences.getString(PHONE, "").equals(mViewModel.getPhone().getValue()));
        //Log.d("myTag", "Gender Position Saved? " + sharedPreferences.getInt(GENDER_POSITION, Integer.MAX_VALUE).equals(mViewModel.getGenderItemIndexSelected()));

    }

    /*
    private void updateView() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        namesEditText.setText(mViewModel.getNames().getValue());
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        phoneEditText.setText(mViewModel.getPhone().getValue());
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        genderSpinner.setSelection(mViewModel.getGenderItemIndexSelected().getValue());
    }
    */

    /*
    private void loadData() {
        mViewModel.getNames().setValue(sharedPreferences.getString(NAME, ""));
        mViewModel.getPhone().setValue(sharedPreferences.getString(PHONE, ""));
        getGenderItemIndexSelected().setValue(sharedPreferences.getInt(GENDER_POSITION, 0));
    }
    */

    public void saveProfileToLocal() {
        //TODO: Exception shoudl be handled here
        sharedPreferences = getActivity().getSharedPreferences(PROFILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, mViewModel.getNames().getValue());
        editor.putString(PHONE, mViewModel.getPhone().getValue());
        editor.putInt(GENDER_POSITION, mViewModel.getGenderItemIndexSelected().getValue());
        editor.apply();
    }
}
