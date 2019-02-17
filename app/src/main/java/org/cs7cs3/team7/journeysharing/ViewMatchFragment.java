package org.cs7cs3.team7.journeysharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewMatchFragment extends Fragment {
    private ViewMatchViewModel viewModel;

    public static ViewMatchFragment newInstance() {
        return new ViewMatchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ViewMatchViewModel.class);
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

        viewModel.getNamesList().observe(this, namesList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, namesList);
            matchedGroupListView.setAdapter(adapter);
        });
    }

    //TODO: Need to add more detail to the list, as well as handle clicks that display the profile of users
}
