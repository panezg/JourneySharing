package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.ViewModelProviders;

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

    private ProfileViewModel viewModel;
    private int genderItemIndexSelected = 0;
    private EditText name_edit;


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
        viewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getContext())).get(ProfileViewModel.class);
        //itemSelector.setOnClickListener(item -> {
        //    model.select(item);
        //});
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
        name_edit = userInfoLayout.findViewById(R.id.names_text);

        updateView();
    }

    private void save() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        Log.d("myTag", "This is my message from button");
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        Log.d("myTag", "Name Saved: " + namesEditText.getText().toString());
        Log.d("myTag", "Phone Saved: " + phoneEditText.getText().toString());
        Log.d("myTag", "Gender saved: " + genderSpinner.getSelectedItem());
        viewModel.getNames().setValue(namesEditText.getText().toString());
        viewModel.getPhone().setValue(phoneEditText.getText().toString());
        viewModel.getGenderItemIndexSelected().setValue(genderItemIndexSelected);
        Log.d("myTag", "Gender saved: " + genderSpinner.getSelectedItem());
        viewModel.saveData();
        Toast.makeText(this.getActivity(), "Profile Saved!", Toast.LENGTH_SHORT).show();
    }

    private void updateView() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        namesEditText.setText(viewModel.getNames().getValue());
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        phoneEditText.setText(viewModel.getPhone().getValue());
        Spinner genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        genderSpinner.setSelection(viewModel.getGenderItemIndexSelected().getValue());
    }
}
