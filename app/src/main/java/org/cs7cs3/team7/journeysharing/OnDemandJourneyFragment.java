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
import android.widget.TextView;

public class OnDemandJourneyFragment extends Fragment {

    private OnDemandJourneyViewModel mViewModel;
    private String TAG = "myTag";

    private static final String START_POINT = "from_where";
    private static final String DESTINATION = "to_where";

    public static OnDemandJourneyFragment newInstance() {
        return new OnDemandJourneyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OnDemandJourneyViewModel.class);
        mViewModel.init();
        Log.d(TAG, "From:"+mViewModel.getFrom().getValue());
        Log.d(TAG, "To:"+mViewModel.getTo().getValue());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_demand_journey_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("myTag", "msg is from 'onViewCreated': ");
        View addressLayout = getView().findViewById(R.id.address_include);
        TextView fromAddress = addressLayout.findViewById(R.id.from_text);
        fromAddress.setText(mViewModel.getFrom().getValue());
        TextView toAddress = addressLayout.findViewById((R.id.to_text));
        toAddress.setText(mViewModel.getTo().getValue());
        final View fromButton = addressLayout.findViewById(R.id.from_button);
        final View toButton = addressLayout.findViewById(R.id.to_button);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setIsDestination(false);
                Fragment fromFragment = TypeAddressFragment.newInstance();
                loadFragment(fromFragment);
            }
        });
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setIsDestination(true);
                Fragment fromFragment = TypeAddressFragment.newInstance();
                loadFragment(fromFragment);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        //transaction.addToBackStack(fragment.toString());
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
