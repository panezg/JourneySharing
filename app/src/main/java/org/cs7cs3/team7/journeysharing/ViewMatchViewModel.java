package org.cs7cs3.team7.journeysharing;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewMatchViewModel extends ViewModel {
    private MutableLiveData<List<String>> namesList;

    public MutableLiveData<List<String>> getNamesList() {
        if (namesList == null) {
            namesList = new MutableLiveData<List<String>>();
            loadData();
        }
        return namesList;
    }

    private void loadData() {
        List<String> listNames = new ArrayList<String>();
        //TODO: Replace with async call
        listNames.add("Paras");
        listNames.add("Jinxi");
        listNames.add("Gustavo");
        this.namesList.setValue(listNames);
    }

}
