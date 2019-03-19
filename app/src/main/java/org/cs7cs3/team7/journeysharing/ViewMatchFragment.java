package org.cs7cs3.team7.journeysharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.cs7cs3.team7.wifidirect.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewMatchFragment extends Fragment {

    private MainViewModel mViewModel;
    private ListView mListView;
    private ArrayList<String> listData;

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
        ListView matchedGroupListView = matchGroupLayout.findViewById(R.id.matched_group_list);

        //TODO: Need to add more detail to the list, as well as handle clicks that display the profile of users
        /*
        mViewModel.getNamesList().observe(this, namesList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, namesList);
            matchedGroupListView.setAdapter(adapter);
        });
        */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init the layout.
        //TODO: Exception here should be handled.
        View matchGroupLayout = getView().findViewById(R.id.match_group_include);

        //TODO: Exception here should be handled.
        // Inti the ViewModel.
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        // Inti the 'match_group' ListView.
        mListView = matchGroupLayout.findViewById(R.id.matched_group_list);
        initData();
        initAdapter();
    }

    private void initData() {
        listData = new ArrayList<String>();
        Map<String, UserInfo> membersList = mViewModel.getMembersList().getValue();
        for(UserInfo user : membersList.values()) {
            // TODO: What format should the members info be displayed into??
            listData.add(user.toString());
        }
    }

    private void initAdapter() {
        mListView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, listData) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                //TODO: Could set the color and the text format here.
                textView.setTextColor(Color.GREEN);
                textView.setTextSize(25);
                return textView;
            }
        });
    }
    //TODO: Need to add more detail to the list, as well as handle clicks that display the profile of users
}
