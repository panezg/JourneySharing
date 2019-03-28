package org.cs7cs3.team7.journeysharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleJourneyFragment extends Fragment {
    private ListView scheduleList;
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
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        scheduleList=(ListView)getActivity().findViewById(R.id.scheduleList);
        ScheduleJourneyFragment scheduleJourneyFragment= new ScheduleJourneyFragment();
        SimpleAdapter adapter = new SimpleAdapter(getContext(),scheduleJourneyFragment.getListOfHistory(mViewModel.getListOfHistory(),mViewModel.getOfflineRecord()),R.layout.vlist,
                new String[]{"orderID","date","time","destination","state"},
                new int[]{R.id.orderID,R.id.date,R.id.time,R.id.destination,R.id.state});
        scheduleList.setAdapter(adapter);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                Log.d("JINCHI", map.get("date").toString());
                mViewModel.setSelectedIndex(position);
                Fragment viewMatchFragment = ViewMatchFragment.newInstance();
                loadFragment(viewMatchFragment);
            }
        });
    }


    private List getListOfHistory(MutableLiveData<List<JourneyRequestInfo>> listofhistory, MutableLiveData<JourneyRequestInfo> offlineRecord){
        List<Map<String,Object>> showlist = new ArrayList<Map<String, Object>>();

        //convert online date to hashmap and add the hashmap to the showlist
        List<JourneyRequestInfo> onlinerequestInfos=new ArrayList<>();
        onlinerequestInfos=listofhistory.getValue();
        Map<String,Object> map=new HashMap<>();
        int count=1;
        for(JourneyRequestInfo journeyRequestInfo: onlinerequestInfos) {
            if (count == 9) {
                break;
            }
            //TODO: add oderId
            String date = journeyRequestInfo.getDate();
            String des = journeyRequestInfo.getDestination();
            String time = journeyRequestInfo.getTime();
            JourneyRequestInfo.JourneyRequestStatus state = journeyRequestInfo.getState();
            map.put("date", date);
            map.put("time", time);
            map.put("destination", des);
            map.put("state", state);
            showlist.add(map);
            count++;
        }
            //convert offline date to hashmap and add the hashmap to the showlist
            Map<String,Object> offlinemap=new HashMap<>();
            String offlinedate=offlineRecord.getValue().getDate();
            String offlinedes=offlineRecord.getValue().getDestination();
            String offlintime=offlineRecord.getValue().getTime();
            JourneyRequestInfo.JourneyRequestStatus offlinestate=offlineRecord.getValue().getState();
            //TODO: add offline orderID
            offlinemap.put("date",offlinedate);
            offlinemap.put("time",offlintime);
            offlinemap.put("destination",offlinedes);
            offlinemap.put("state",offlinestate);
            showlist.add(0,offlinemap);


        return showlist;
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
