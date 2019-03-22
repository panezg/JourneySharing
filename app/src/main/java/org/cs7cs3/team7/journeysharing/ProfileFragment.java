package org.cs7cs3.team7.journeysharing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.cs7cs3.team7.wifidirect.INetworkManager;
import org.cs7cs3.team7.wifidirect.Message;
import org.cs7cs3.team7.wifidirect.NetworkManagerFactory;
import org.cs7cs3.team7.wifidirect.Utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class ProfileFragment extends Fragment {

    private static final String NAME = "names";
    private static final String PHONE = "phone";
    private static final String GENDER_POSITION = "SelectedPosition";
    private static final String PROFILE = "profile shared preferences";
    private static final String UNIQUE_ID = "id for sending http request";

    // All the components.
    private MainViewModel mViewModel;
    private SharedPreferences sharedPreferences;
    private EditText namesEditText;
    private EditText phoneEditText;
    private Spinner genderSpinner;
    private Button saveProfileButton;
    private Button start;
    private Button sendButton;
    private EditText msg;
    private INetworkManager networkManager;

    static ProfileFragment newInstance() {
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
        //Nothing to do;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init the ViewModel.
        //TODO: Exception here should be handled.
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        // Init the shared preferences with the local data already had.
        sharedPreferences = getActivity().getSharedPreferences(PROFILE, 0);

        loadDataFromLocal();

        // Init the profile layout.
        View userInfoLayout = getView().findViewById(R.id.user_info_include);

        // Init the 'EditText' form.
        namesEditText = userInfoLayout.findViewById(R.id.names_text);
        mViewModel.getNames().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                namesEditText.setText(msg);
                Log.d("myTag", "Name in MainView Model Saved: " + mViewModel.getNames().getValue());
            }
        });

        // Init the 'EditText' form.
        phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        mViewModel.getPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                phoneEditText.setText(msg);
                Log.d("myTag", "Phone in MainView Model Saved: " + mViewModel.getPhone().getValue());
            }
        });

        // Init the 'Gender' spinner.
        genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        mViewModel.getGenderItemIndexSelected().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer msg) {
                genderSpinner.setSelection(msg);
                Log.d("myTag", "Gender in MainView Model saved: " + mViewModel.getGenderItemIndexSelected().getValue());
            }
        });

        // Init the 'Save' saveProfileButton.
        saveProfileButton = getView().findViewById(R.id.save_button);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        msg = (EditText)getView().findViewById(R.id.msgTv);
        start = getView().findViewById(R.id.start);
        /*start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JINCHI","in onClick button handler");
                networkManager.initiateWiFiP2PGroupFormation();
            }
        });*/

        sendButton = getView().findViewById(R.id.sendButton);
        /*sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JINCHI", "in onClick sendButton handler");
                Message message = new Message();
                message.setMessageText(msg.getText().toString());
                networkManager.sendMessage(message,false);
            }
        });
        networkManager = NetworkManagerFactory.getNetworkManager(this.getActivity());*/

        //local broadcast message receiver to listen to message sent from peers
        /*BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = Utility.fromJson(intent.getStringExtra("message"));
                Utility.toast(message.getMessageText(),getContext());
                Log.d("JINCHI", "Local broadcast received in general receiver1: " + message);
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(messageReceiver, new IntentFilter("MESSAGE_RECEIVED"));*/
    }

    // Load data from the shared preferences file in local.
    private void loadDataFromLocal() {
        mViewModel.getNames().setValue(sharedPreferences.getString(NAME, ""));
        mViewModel.getPhone().setValue(sharedPreferences.getString(PHONE, ""));
        mViewModel.getGenderItemIndexSelected().setValue(sharedPreferences.getInt(GENDER_POSITION, 0));
    }

    private void saveData() {
        String phone = phoneEditText.getText().toString();
        String names = namesEditText.getText().toString();
        int index = genderSpinner.getSelectedItemPosition();
        mViewModel.setNames(names);
        mViewModel.setPhone(phone);
        mViewModel.setGenderItemIndexSelected(index);
        mViewModel.saveUserProfile();
        saveProfileToLocal();
    }

    // Save the data currently at the ViewModel to the local file.
    public void saveProfileToLocal() {
        Log.d("myTag", "This is my message from Save saveProfileButton");

        //TODO: Exception here should be handled.
        //Init the sharedPreferences.
        sharedPreferences = getActivity().getSharedPreferences(PROFILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, mViewModel.getNames().getValue());
        editor.putString(PHONE, mViewModel.getPhone().getValue());

        //TODO: Exception here should be handled.
        editor.putInt(GENDER_POSITION, mViewModel.getGenderItemIndexSelected().getValue());
        editor.apply();

        // Test for if the shared preferences have saved the data successfully.
        Log.d("myTag", "Name Saved Correctly? " + sharedPreferences.getString(NAME, "al;kdjakdl").equals(mViewModel.getNames().getValue()));
        Log.d("myTag", "Phone Saved Correctly? " + sharedPreferences.getString(PHONE, "alsdkjal").equals(mViewModel.getPhone().getValue()));
        Log.d("myTag", "Gender Position Saved Correctly? " + (sharedPreferences.getInt(GENDER_POSITION, Integer.MAX_VALUE) == (mViewModel.getGenderItemIndexSelected().getValue())));

        // Toast message for user.
        Toast.makeText(this.getActivity(), "Profile Saved!", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onResume() {
        super.onResume();
        Log.d("JINCHI", "in onResume() of MainActivity");
        Log.d("JINCHI", "WiFi Direct Broadcast receiver registered with intent filter");
        networkManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("JINCHI", "in onPause() of MainActivity");
        networkManager.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("JINCHI", "in onStop() of MainActivity");
        networkManager.onStop();
    }

    @Override
    public void onDestroy() {
        //TODO: Need to review this
        super.onDestroy();
        Log.d("JINCHI", "in onDestroy() of MainActivity");
        networkManager.onDestroy();
        Log.d("JINCHI", "in onDestroy() of MainActivity");
    }*/
}
