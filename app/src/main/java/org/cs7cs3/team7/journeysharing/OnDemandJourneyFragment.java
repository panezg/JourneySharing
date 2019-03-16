package org.cs7cs3.team7.journeysharing;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyhdyh.widget.loadingbar.LoadingBar;

import org.cs7cs3.team7.wifidirect.Message;
import org.cs7cs3.team7.wifidirect.NetworkManager;
import org.cs7cs3.team7.wifidirect.Utility;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.security.auth.callback.Callback;

public class OnDemandJourneyFragment extends Fragment {
    
    private MainViewModel mViewModel;
    private TextView fromAddress;
    private TextView toAddress;
    private Button fromButton;
    private Button toButton;
    private NetworkManager networkManager;

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

        // Inti the 'fromAddress' TextView and automatically update the View content.
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

        LinearLayout layout = getView().findViewById(R.id.linear_layout);
        mParent = layout.findViewById(R.id.content);
        //getView().findViewById(R.layout.on_demand_journey_fragment).findViewById();

        networkManager = new NetworkManager(this.getActivity());
        searchButton = (Button) layout.getChildAt(5);
        searchButton.setOnClickListener(view -> {
            Log.d("JINCHI", "in onClick sendButton handler");
            // Sent the user's info to the server.
            Message message = new Message();
            // TODO: Need to test the format of UserInfo got via getSender().getValue()
            // Sent the user's info to the server, including @param name, @param phoneNum and @param destination
            message.setSender(mViewModel.getSender().getValue());
            try {
                waitingForMatchResult.acquire();
                Log.d("JINCHI", "current num of semaphore: " + waitingForMatchResult.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            networkManager.sendMessage(message);
            Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

            // Register the messageReceiver
            onListeningReceiveEvent();
            LoadingBar.show(mParent);
            Toast.makeText(this.getActivity(), "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();

            // Waiting the match to finish and received the message from server.
            waitForMatchAndSkip();
        });

        // Faking a Semaphore to testing the 'waitingForMatch->skip to ViewMatchModel Fragment' logic.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                waitingForMatchResult.release();
            }
        }).start();
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
}
