package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnDemandJourneyFragment extends Fragment {

    private OnDemandJourneyViewModel mViewModel;

    public static OnDemandJourneyFragment newInstance() {
        return new OnDemandJourneyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_demand_journey_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OnDemandJourneyViewModel.class);
        // TODO: Use the ViewModel
    }

}
