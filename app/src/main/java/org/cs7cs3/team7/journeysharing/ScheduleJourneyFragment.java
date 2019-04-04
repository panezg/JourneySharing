package org.cs7cs3.team7.journeysharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ScheduleJourneyFragment extends Fragment {
    private ListView scheduleList;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mViewModel;

    static ScheduleJourneyFragment newInstance() {
        return new ScheduleJourneyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_journey_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel= ViewModelProviders.of(getActivity(), viewModelFactory).get(MainViewModel.class);
        scheduleList=(ListView)getActivity().findViewById(R.id.scheduleList);
       // ScheduleJourneyFragment scheduleJourneyFragment = new ScheduleJourneyFragment();
        Map<Integer, String> posToJourneyId = new HashMap<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(),getListOfHistory(posToJourneyId),R.layout.vlist,
                new String[]{"date","time","destination","state"},
                new int[]{R.id.orderID,R.id.date,R.id.time,R.id.destination,R.id.state});
        scheduleList.setAdapter(adapter);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.setIsOnlineModel(true);
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                Log.d("JINCHI", map.get("date").toString());
                mViewModel.setSelectedIndex(posToJourneyId.get(position));
                Fragment viewMatchFragment = ViewMatchFragment.newInstance();
                loadFragment(viewMatchFragment);
            }
        });
    }


    private List getListOfHistory(Map<Integer, String> posToJourneyId){
        List<Map<String,Object>> showList = new ArrayList<>();

        //convert online date to hashmap and add the hashmap to the showList
        Map<String, JourneyRequest> onlineRequestInfos = mViewModel.getListOfHistory().getValue();

        Map<String, Object> map = new HashMap<>();
        int pos = 0;
        for(String journeyID : onlineRequestInfos.keySet()) {
            //TODO: add oderId
            JourneyRequest journeyRequest = onlineRequestInfos.get(journeyID);
            posToJourneyId.put(pos, journeyID);
            String date = journeyRequest.getDate();
            String des = journeyRequest.getDestination();
            String time = journeyRequest.getTime();
            JourneyRequest.JourneyRequestStatus state = journeyRequest.getState();
            map.put("date", date);
            map.put("time", time);
            map.put("destination", des);
            map.put("state", state);
            showList.add(map);
            pos++;
        }
        return showList;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        //TODO: exception here should be handled.
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

/*
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);
        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        map.put("orderID","111222333444");
        map.put("date","01/01/2019");
        map.put("time","12:00pm");
        map.put("destination","tcd");
        map.put("state","undone");
        list.add(map);

        return list;
    }

*/
}
