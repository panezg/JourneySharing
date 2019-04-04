package org.cs7cs3.team7.journeysharing;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MainViewModel mViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            //depending on state, would need to change to use ViewMatchFragment
            case R.id.nav_schedule_journey:
                //refreshScheduledList();
                Fragment scheduleJourneyFragment = ScheduleJourneyFragment.newInstance();
                loadFragment(scheduleJourneyFragment);
                return true;
            case R.id.nav_on_demand_journey:
                Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance();
                loadFragment(onDemandJourneyFragment);
                return true;
            case R.id.nav_profile:
                loadFragment(new ProfileFragment());
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("JINCHI", "OnCreate Activity");

        setContentView(R.layout.main_activity);

        this.configureDagger();

        ////
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        //mViewModel.init(getApplicationContext(), this);
        Log.d("JINCHI", "done");
        //profileViewModel.init();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //loading default fragment
        //TODO: If basic flow hasn't completed, then load Profile; else, load default
        //need some kind of global property

        //Useful in case we want the Profile Fragment page to load info from other users
        ProfileFragment fragment = new ProfileFragment();
        //Bundle bundle = new Bundle();
        //bundle.putString(ProfileFragment.UID_KEY, "");
        //fragment.setArguments(bundle);

        loadFragment(fragment);
        //CommsManagerFactory.setSimulatorModeOn(true);

        //TODO: don't allow navigation
        //else
        //Fragment scheduleJourneyFragment = ScheduleJourneyFragment.newInstance();
        //loadFragment(scheduleJourneyFragment);
    }

    // --- Dagger configuration

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    // --- Other methods

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        //transaction.addToBackStack(fragment.toString());
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void refreshScheduledList() {
        // TODO: Refresh data according to the response from server end.
    }

}