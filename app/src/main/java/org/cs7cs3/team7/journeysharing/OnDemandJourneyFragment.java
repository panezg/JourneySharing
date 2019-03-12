package org.cs7cs3.team7.journeysharing;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnDemandJourneyFragment extends Fragment {
    
    private MainViewModel mViewModel;
    private TextView fromAddress;
    private TextView toAddress;
    private Button fromButton;
    private Button toButton;

    private Button searchButton;

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
        //getView().findViewById(R.layout.on_demand_journey_fragment).findViewById();
        searchButton = (Button) layout.getChildAt(5);
        searchButton.setOnClickListener(view -> {
            Log.d(TAG, "buttong pressed");
            Fragment onDemandJourneyFragment = ViewMatchFragment.newInstance();
            loadFragment(onDemandJourneyFragment);
        });

       // View curView = getView().findViewById(R.layout.on_demand_journey_fragment);
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
}
