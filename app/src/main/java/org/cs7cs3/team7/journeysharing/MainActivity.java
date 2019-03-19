package org.cs7cs3.team7.journeysharing;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.cs7cs3.team7.wifidirect.NetworkManagerFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            //depending on state, would need to change to use ViewMatchFragment
            case R.id.nav_schedule_journey:
                Fragment scheduleJourneyFragment = ScheduleJourneyFragment.newInstance();
                loadFragment(scheduleJourneyFragment);
                return true;
            case R.id.nav_on_demand_journey:
                Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance();
                loadFragment(onDemandJourneyFragment);
                return true;
            case R.id.nav_profile:
                Fragment profileFragment = ProfileFragment.newInstance();
                loadFragment(profileFragment);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.init();
        setContentView(R.layout.main_activity);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //loading default fragment
        //TODO: If basic flow hasn't completed, then load Profile; else, load default
        //need some kind of global property
        Fragment profileFragment = ProfileFragment.newInstance();
        loadFragment(profileFragment);
        //NetworkManagerFactory.setSimulatorModeOn(true);
        //TODO: don't allow navigation
        //else
        //Fragment scheduleJourneyFragment = ScheduleJourneyFragment.newInstance();
        //loadFragment(scheduleJourneyFragment);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        //transaction.addToBackStack(fragment.toString());
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}