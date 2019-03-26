package org.cs7cs3.team7.journeysharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleJourneyFragment extends Fragment {
    private ListView scheduleList;

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
        scheduleList=(ListView)getActivity().findViewById(R.id.scheduleList);
        SimpleAdapter adapter = new SimpleAdapter(getContext(),getData(),R.layout.vlist,
                new String[]{"orderID","date","time","destination","state"},
                new int[]{R.id.orderID,R.id.date,R.id.time,R.id.destination,R.id.state});
        scheduleList.setAdapter(adapter);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long s=(long)parent.getItemAtPosition(position);
//                Toast.makeText(getContext(),s.get("orderID"),Toast.LENGTH_SHORT).show();

            }
        });
    }


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


}
