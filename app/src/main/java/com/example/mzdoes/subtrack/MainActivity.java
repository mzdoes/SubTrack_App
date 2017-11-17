package com.example.mzdoes.subtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView subView;
    private ArrayList<Subscription> subList;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
    }

    //methods
    public void wireWidgets() {
        subView = (ListView) findViewById(R.id.listView_subView);
        subList = new ArrayList<>();
        setSubList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, subList) {
            @NonNull
            @Override
            public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(pos, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                //set text
                text1.setText("$" + subList.get(pos).getPrice());
                text2.setText(subList.get(pos).getName() + ": " + subList.get(pos).getDesc());

                //set appearance
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,8,0,8);
                text1.setTextSize(25);
                text1.setLayoutParams(params);
                text2.setTextSize(15);
                view.setBackgroundColor((int) R.color.colorPrimaryDark);
                return view;
            }
        };
        subView.setAdapter(adapter);
        subView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent i = new Intent(MainActivity.this, SubscriptionView.class);
                i.putExtra("IncomingSubscription", subList.get(pos));
                startActivity(i);
            }
        });
    }

    public void setSubList() {
        subList.add(new Subscription("Spotify", "for retroZEU", 9.99));
        subList.add(new Subscription("Gym", "for Zeu Capua", 39.99));
    }
}
