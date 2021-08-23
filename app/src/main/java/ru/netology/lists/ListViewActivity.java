package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    static final String KEY1 = "Key1";
    static final String KEY2 = "Key2";
    static final String DATA = "Data";

    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    ListView list;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditor;
    SwipeRefreshLayout swipeLayout;
    BaseAdapter listContentAdapter;

    ArrayList<Integer> Arlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        myEditor = sharedPref.edit();
        list = findViewById(R.id.list);
        swipeLayout = findViewById(R.id.swiperefresh);

if(SharepRef()) {
    myEditor.putString(DATA, getString(R.string.large_text));
    myEditor.apply();
}
        prepareContent();

        listContentAdapter = createAdapter(simpleAdapterContent);

        list.setAdapter(listContentAdapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdapterContent.clear();
                prepareContent();
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                Arlist.add(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(ListViewActivity.this, values, R.layout.list_layout, new String[]{KEY1, KEY2}, new int[]{R.id.TextOne, R.id.TextTwo});
    }

    @NonNull
    private void prepareContent() {
        String[] arrayContent = sharedPref.getString(DATA, "").split("\n\n");
        for (int i = 0; i < arrayContent.length; i++) {
            Map<String, String> temp = new HashMap<>();
            temp.put(KEY1, arrayContent[i]);
            temp.put(KEY2, String.valueOf(arrayContent[i].length()));
            simpleAdapterContent.add(temp);
        }
    }
    private Boolean SharepRef(){
        String result = sharedPref.getString(DATA, "");
        Log.d("Log", result);
        if(result.equals("")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putIntegerArrayList("One",Arlist);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Arlist = savedInstanceState.getIntegerArrayList("one");
        for(int i = 0; i < simpleAdapterContent.size(); i++){
            for(int k = 0; k < (Arlist != null ? Arlist.size() : 0); k++){
                if (i == Arlist.get(k)){
                    simpleAdapterContent.remove(i);
                }
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}

