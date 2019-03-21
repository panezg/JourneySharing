package org.cs7cs3.team7.journeysharing;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dyhdyh.widget.loadingbar.LoadingBar;

import org.cs7cs3.team7.wifidirect.INetworkManager;
import org.cs7cs3.team7.wifidirect.Message;

import org.cs7cs3.team7.wifidirect.NetworkManagerFactory;
import org.cs7cs3.team7.wifidirect.UserInfo;
import org.cs7cs3.team7.wifidirect.Utility;

import org.cs7cs3.team7.wifidirect.NetworkManager;
import org.cs7cs3.team7.wifidirect.UserInfo;
import org.cs7cs3.team7.wifidirect.Utility;


import java.text.DateFormat;
import java.util.ArrayList;
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
    private INetworkManager networkManager;


    private Button setDate,timeSet;
//    private NetworkManager networkManager;
    private TextView showDate,showTime;
    private UserInfo userInfo;

    private View mParent;
    private Button searchButton;
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

        //TODO: Exception here should be handled.
        // Inti the ViewModel.
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        LinearLayout layout = getView().findViewById(R.id.linear_layout);
        // Inti the 'fromAddress' TextView and automatically update the View content.
        spinner=layout.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String content=parent.getSelectedItem().toString();
//                Toast.makeText(getContext(),content,Toast.LENGTH_SHORT).show();
                if(content.equals("Real Time")){
                    fromButton.setEnabled(false);
                    Toast.makeText(getContext(),content,Toast.LENGTH_SHORT).show();
                }else{
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
        setDate=addressLayout.findViewById(R.id.SetDateButton);
        showDate=addressLayout.findViewById(R.id.ShowDate);
        showTime=addressLayout.findViewById(R.id.ShowTime);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat=DateFormat.getDateInstance();
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

        timeSet=addressLayout.findViewById(R.id.SetTimeButton);
        DateFormat timeFormate=DateFormat.getTimeInstance();
        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String time=timeFormate.format(calendar.getTime());
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

        mParent = layout.findViewById(R.id.content);
        //getView().findViewById(R.layout.on_demand_journey_fragment).findViewById();

        networkManager = NetworkManagerFactory.getNetworkManager(this.getActivity());
        searchButton = (Button) layout.getChildAt(5);
        searchButton.setOnClickListener(view -> {
            Log.d("JINCHI", "in onClick sendButton handler");
            // Sent the user's info to the server.
            Message message = new Message();
            // TODO: Need to test the format of UserInfo got via getSender().getValue()
            // Sent the user's info to the server, including @param name, @param phoneNum and @param destination
            Log.d("JINCHI", "Viewmodel: ");
            mViewModel.setSender(new UserInfo(mViewModel.getNames().getValue(),mViewModel.getPhone().getValue(), mViewModel.getTo().getValue(),mViewModel.getDate().getValue(),mViewModel.getTime().getValue()));
            message.setSender(mViewModel.getSender().getValue());
            message.setIntent("SEND_TRIP_REQUEST");
            try {
                waitingForMatchResult.acquire();
                Log.d("JINCHI", "current num of semaphore: " + waitingForMatchResult.toString());
                Log.d("JINCHI", "when sending -- message.toString(): " + message.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            networkManager.sendMessage(message,true);
            Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

            // Register the messageReceiver
            onListeningReceiveEvent();
            LoadingBar.show(mParent);
            Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

            // Waiting the match to finish and received the message from server.
            waitForMatchAndSkip();
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
        BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = Utility.fromJson(intent.getStringExtra("message"));
                Utility.toast(message.getMessageText(),getContext());
                Log.d("JINCHI", "Local broadcast received in general receiver: " + message);
                // TODO: Need to check the membersList<UserInfo> from the message.
                mViewModel.setMembersList(message.getList());
                waitingForMatchResult.release();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(messageReceiver, new IntentFilter("MESSAGE_RECEIVED"));
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
    }


}
