package org.cs7cs3.team7.journeysharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewMatch extends AppCompatActivity {

    TextView timeDispaly;
    ListView custoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewMatch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,testData());
        custoList.setAdapter(adapter);
        initialWork();
    }

    public void initialWork(){
        timeDispaly = (TextView) findViewById(R.id.viewMatch_timeDispaly);
        custoList = (ListView) findViewById(R.id.viewMatch_cusList);
    }

    public ArrayList<String> testData(){
        ArrayList<String> List = new ArrayList<String>();
        List.add("SAM");
        List.add("SINDY");
        List.add("CHIU");
        return List;
    }
}
