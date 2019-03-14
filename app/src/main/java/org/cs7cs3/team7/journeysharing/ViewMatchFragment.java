package org.cs7cs3.team7.journeysharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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
        //TODO: Read the data from the mainViewModel.
        listData = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            listData.add("这是第 " + i + " 条数据");
        }
    }

    private void initAdapter() {
        mListView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, listData) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                //TODO: Could set the color and the text format here.
                textView.setTextColor(Color.BLUE);
                textView.setTextSize(36);
                return textView;
            }
        });
    }
    //TODO: Need to add more detail to the list, as well as handle clicks that display the profile of users
}
