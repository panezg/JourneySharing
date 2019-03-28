package org.cs7cs3.team7.journeysharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dyhdyh.widget.loadingbar.LoadingBar;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;
import org.cs7cs3.team7.journeysharing.Models.MatchingResultInfo;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;
import org.cs7cs3.team7.wifidirect.CommsManagerFactory;
import org.cs7cs3.team7.wifidirect.ICommsManager;
import org.cs7cs3.team7.wifidirect.Message;
import org.cs7cs3.team7.wifidirect.Utility;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class OnDemandJourneyFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView fromAddress;
    private TextView toAddress;
    private Button fromButton;
    private Button toButton;
    private Spinner spinner;
    private ICommsManager commsManager;

    private Button setDate, timeSet;
//    private P2PNetworkManager networkManager;
    private TextView showDate,showTime;
    private UserInfo userInfo;

    private boolean isRealTime;

    private View mParent;
    private Button searchButton;
    private Spinner genderSpinner;
    private Spinner methodSpinner;

    private final Semaphore waitingForMatchResult = new Semaphore(1);
    private String TAG = "myTag";

    static OnDemandJourneyFragment newInstance() {
        return new OnDemandJourneyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_demand_journey_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init the layout.
        //TODO: Exception here should be handled.
        View addressLayout = getView().findViewById(R.id.address_include);
        View preferencesLayout = getView().findViewById(R.id.preferences_include);

        //TODO: Exception here should be handled.
        // Inti the ViewModel.
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        LinearLayout layout = getView().findViewById(R.id.linear_layout);
        // Inti the 'fromAddress' TextView and automatically update the View content.
        spinner = layout.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String content=parent.getSelectedItem().toString();
//                Toast.makeText(getContext(),content,Toast.LENGTH_SHORT).show();
                if(content.equals("Real Time")){
                    isRealTime = true;
                    fromButton.setEnabled(false);
                    Toast.makeText(getContext(),content,Toast.LENGTH_SHORT).show();
                }else{
                    isRealTime = false;
                    fromButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"asd",Toast.LENGTH_SHORT).show();
            }
        });
        fromAddress = addressLayout.findViewById(R.id.from_text);
        mViewModel.getFrom().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                fromAddress.setText(msg);
            }
        });

        // Inti the 'toAddress' TextView and automatically update the View content.
        toAddress = addressLayout.findViewById((R.id.to_text));
        ViewModelProviders.of(getActivity()).get(MainViewModel.class).getTo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                toAddress.setText(msg);
            }
        });

        // Init the 'from' button and Set the onClick event listener.
        fromButton = getView().findViewById(R.id.from_button);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setIsDestination(false);
                Fragment fromFragment = TypeAddressFragment.newInstance();
                loadFragment(fromFragment);
            }
        });

        // Init the 'to' button.
        toButton = addressLayout.findViewById(R.id.to_button);
        //getView().findViewById(R.id.)
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setIsDestination(true);
                Fragment fromFragment = TypeAddressFragment.newInstance();
                loadFragment(fromFragment);
            }
        });

        //SetTime button in the addressLayout
        setDate = addressLayout.findViewById(R.id.SetDateButton);
        showDate = addressLayout.findViewById(R.id.ShowDate);
        showTime = addressLayout.findViewById(R.id.ShowTime);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance();
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String date=dateFormat.format(calendar.getTime());
                        showDate.setText(date);
                        Toast.makeText(getContext(), date.getClass().getName(), Toast.LENGTH_SHORT).show();
                        mViewModel.setDate(date);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
//                String dateInfor=new String(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH)+1).append("/").append(calendar.get(Calendar.DAY_OF_MONTH)));
            }
        });

        timeSet = addressLayout.findViewById(R.id.SetTimeButton);
        DateFormat timeFormat = DateFormat.getTimeInstance();
        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String time=timeFormat.format(calendar.getTime());
                        showTime.setText(time);
                        mViewModel.setTime(time);
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();
//                String timeInfo=new String(new StringBuilder().append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE)));

            }
        });

        genderSpinner = preferencesLayout.findViewById(R.id.gender_spinner);
        mViewModel.getPreGenderItemIndexSelected().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer msg) {
                genderSpinner.setSelection(msg);
                mViewModel.setGenderPreference(msg == 0 ? "Male" : "Female");
                Log.d("myTag", "Gender in MainView Model saved: " + mViewModel.getGenderItemIndexSelected().getValue());
            }
        });

        methodSpinner = preferencesLayout.findViewById(R.id.method_spinner);
        mViewModel.getPreMethodItemIndexSelected().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer msg) {
                methodSpinner.setSelection(msg);
                mViewModel.setMethodPreference(msg == 0 ? "Walking" : "Taxi");
                Log.d("myTag", "Gender in MainView Model saved: " + mViewModel.getGenderItemIndexSelected().getValue());
            }
        });

        mParent = layout.findViewById(R.id.content);
        //getView().findViewById(R.layout.on_demand_journey_fragment).findViewById();

        commsManager = CommsManagerFactory.getCommsManager(this.getContext());
        //networkManager = NetworkManagerFactory.getNetworkManager(this.getActivity(), null);
        searchButton = (Button) layout.getChildAt(5);
        searchButton.setOnClickListener(view -> {
            Log.d("JINCHI", "in onClick sendButton handler");
            // Sent the user's info to the server.

            // TODO: Need to test the format of UserInfo got via getSender().getValue()
            // Sent the user's info to the server, including @param name, @param phoneNum and @param destination
            Log.d("JINCHI", "Viewmodel: ");

            mViewModel.setSender(new UserInfo(commsManager.getMACAddress() + mViewModel.getNames().getValue(), mViewModel.getNames().getValue(), mViewModel.getPhone().getValue(), mViewModel.getGender().getValue()));
            UserInfo userInfo = new UserInfo(commsManager.getMACAddress() + mViewModel.getNames().getValue(), mViewModel.getNames().getValue(), mViewModel.getPhone().getValue(), mViewModel.getGender().getValue());
            JourneyRequestInfo journeyRequestInfo = new JourneyRequestInfo(userInfo, genderSpinner.getSelectedItem().toString(), methodSpinner.getSelectedItem().toString(), mViewModel.getTo().getValue(), isRealTime);

            //There should be an object or structure defining the journey details, like preferences
            if(isRealTime) {
                try {
                    waitingForMatchResult.acquire();
                    Log.d("JINCHI", "current num of semaphore: " + waitingForMatchResult.toString());
                    //Log.d("JINCHI", "when sending -- message.toString(): " + message.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                commsManager.requestJourneyMatch(journeyRequestInfo);
                journeyRequestInfo.setState(JourneyRequestInfo.JourneyRequestStatus.PENDING);
                mViewModel.setOfflineRecord(journeyRequestInfo);

                Log.d("JINCHI", "OnDemandJourneyFragment: After calling requestJourneyMatch()");
                Log.d("JINCHI", "sender:" + mViewModel.getSender().toString() + '\n' + "");
                Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

                // Register the messageReceiver
                onListeningReceiveEvent();
                LoadingBar.show(mParent);
                Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

                // Waiting the match to finish and received the message from server.
                waitForMatchAndSkip();
            } else {
                journeyRequestInfo.setState(JourneyRequestInfo.JourneyRequestStatus.PENDING);
                journeyRequestInfo.setDate(mViewModel.getDate().getValue());
                journeyRequestInfo.setTime(mViewModel.getTime().getValue());
                journeyRequestInfo.setStartPoint(mViewModel.getFrom().getValue());
                mViewModel.addRecordToList(journeyRequestInfo);
                Toast.makeText(this.getActivity(), "Request Sent! Please check details in the first page ", Toast.LENGTH_SHORT).show();
            }
        });

        // Faking a Semaphore to testing the 'waitingForMatch->skip to ViewMatchModel Fragment' logic.
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                waitingForMatchResult.release();
            }
        }).start();*/
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("myTag", "msg is from 'onViewCreated': do nothing");
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        //TODO: exception here should be handled.
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onListeningReceiveEvent() {
        //local broadcast message receiver to listen to message sent from peers
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MatchingResultInfo matchingResultInfo = intent.getParcelableExtra(Constants.JOURNEY_MATCHED_INTENT_ACTION_PARCELABLE_KEY);
                //Toast.makeText(context, message.getMessageText(), Toast.LENGTH_SHORT).show();
                Log.d("JINCHI", "Local broadcast received in general receiver: " + matchingResultInfo);
                // TODO: Need to check the membersList<UserInfo> from the message.
                mViewModel.setMembersList(matchingResultInfo.getGroupMembers());
                JourneyRequestInfo journeyRequestInfo = new JourneyRequestInfo(userInfo, genderSpinner.getSelectedItem().toString(), methodSpinner.getSelectedItem().toString(), mViewModel.getTo().getValue(), true);
                journeyRequestInfo.setState(JourneyRequestInfo.JourneyRequestStatus.FINISHED);
                mViewModel.setOfflineRecord(journeyRequestInfo);
                waitingForMatchResult.release();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.JOURNEY_MATCHED_INTENT_ACTION));
    }

    private void waitForMatchAndSkip() {
        Thread td = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("JINCHI", "In sub Thread - current num of semaphore: " + waitingForMatchResult.toString());
                    waitingForMatchResult.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Skip to the viewMatchFragment.
                Fragment viewMatchFragment = ViewMatchFragment.newInstance();
                loadFragment(viewMatchFragment);
            }
        });
        td.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("JINCHI", "BEGIN onResume() of OnDemandJourneyFragment");
        commsManager.onResume();
        Log.d("JINCHI", "END onResume() of OnDemandJourneyFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("JINCHI", "BEGIN onPause() of OnDemandJourneyFragment");
        commsManager.onPause();
        Log.d("JINCHI", "END onPause() of OnDemandJourneyFragment");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("JINCHI", "BEGIN onStop() of OnDemandJourneyFragment");
        commsManager.onStop();
        Log.d("JINCHI", "END onStop() of OnDemandJourneyFragment");
    }

    @Override
    public void onDestroy() {
        //TODO: Need to review this
        super.onDestroy();
        Log.d("JINCHI", "BEGIN onDestroy() of OnDemandJourneyFragment");
        commsManager.onDestroy();
        //commsManager.clean();
        Log.d("JINCHI", "END onDestroy() of OnDemandJourneyFragment");
    }
}
