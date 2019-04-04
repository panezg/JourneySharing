package org.cs7cs3.team7.journeysharing;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ViewMatchFragment extends Fragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mViewModel;
    private ListView mListView;
    private ArrayList<Map<String, Object>> listData;
    private ListView detailsList;

    public static ViewMatchFragment newInstance() {
        return new ViewMatchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_match_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        View matchGroupLayout = view.findViewById(R.id.match_group_include);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init the layout.
        //TODO: Exception here should be handled.
        View matchGroupLayout = getView().findViewById(R.id.match_group_include);
        LinearLayout layout = getView().findViewById(R.id.fucking_layout);

        //TODO: Exception here should be handled.
        // Inti the ViewModel.
        mViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainViewModel.class);

        // Inti the 'match_group' ListView.
        mListView = matchGroupLayout.findViewById(R.id.matched_group_list);
        detailsList = layout.findViewById(R.id.match_details_list);

        initData();
        initAdapter();
    }

    private void initData() {
        listData = new ArrayList<>();
        //int index = mViewModel.getSelectedIndex().getValue();
        //TODO: Find the correct result by the index id saved at the last page.
        List<User> membersList;
        if(mViewModel.getIsOnlineModel().getValue() != null && !mViewModel.getIsOnlineModel().getValue()) {
            membersList = mViewModel.getMembersList().getValue();
        } else {
            // Online Model
            String journeyID = mViewModel.getSelectedIndex().getValue();
            membersList = mViewModel.getResultsOfOnlineModel().getValue().get(journeyID);
        }
        if(membersList != null) {
            for(User u : membersList) {
                Map<String, Object> data = new HashMap<>();
                data.put("Name ", "Name: " + u.getNames());
                data.put("Phone ", "Phone#: " + u.getPhoneNum());
                data.put("Gender ", "Gender: " + u.getGender());
                listData.add(data);
            }
        }
    }

    private void initAdapter() {
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listData, R.layout.members_list,
                new String[]{"Name ","Phone ","Gender ",},
                new int[]{R.id.memberName, R.id.memberPhone, R.id.memberGender});
        mListView.setAdapter(adapter);
        if(mViewModel.getIsOnlineModel().getValue() != null && !mViewModel.getIsOnlineModel().getValue()) {
            detailsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, new ArrayList<>()));
        } else {
            String journeyID = mViewModel.getSelectedIndex().getValue();
            JourneyRequest journeyRequest = mViewModel.getListOfHistory().getValue().get(journeyID);
            ArrayList<String> dataList = new ArrayList<>();
            dataList.add("Time:        " + journeyRequest.getTime());
            dataList.add("Start Point: " + journeyRequest.getStartPoint());
            dataList.add("Destination: " + journeyRequest.getDestination());
            dataList.add("Method:      " + journeyRequest.getMethod());
            detailsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, dataList));
        }
    }
}
