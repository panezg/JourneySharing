package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private static final String SHARE_PREFS = "sharedPrefs";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String GENDER_POSITION = "SelectedPosition";

    private String name;
    private String phone;
    private int genderSpinnerPosition;

    private SharedPreferences.Editor editor;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    // Create an anonymous implementation of OnClickListener
    private Button.OnClickListener mSaveOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            saveData();
        }
    };

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        Log.d("myTag", "This is my message from button");
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        Log.d("myTag", "Name Saved: " + namesEditText.getText().toString());
        Log.d("myTag", "Phone Saved: " + phoneEditText.getText().toString());
        Log.d("myTag", "Gender saved: " + genderSpinner.getItemAtPosition(genderSpinnerPosition));

        editor.putString(NAME, namesEditText.getText().toString());
        editor.putString(PHONE, phoneEditText.getText().toString());
        editor.putInt(GENDER_POSITION, genderSpinnerPosition);
        editor.apply();

        Toast.makeText(this.getActivity(), "Profile Saved!", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(NAME, "");
        phone = sharedPreferences.getString(PHONE, "");
        genderSpinnerPosition = sharedPreferences.getInt(GENDER_POSITION, 0);
    }

    private void updateView() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        namesEditText.setText(name);
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        phoneEditText.setText(phone);
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        genderSpinner.setSelection(genderSpinnerPosition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button button = getView().findViewById(R.id.save_button);

        // Keep the spinner selected listener works before press the "save" button.
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myTag", "Gender Selected: " + parent.getItemAtPosition(position).toString());
                genderSpinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        button.setOnClickListener(mSaveOnClickListener);
        loadData();
        updateView();
    }
}
