package com.example.mzdoes.subtrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class SubscriptionView extends AppCompatActivity {

    TextView nameView, descView, priceView, dueDateView;
    ImageButton editButton;
    Subscription subscription;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent i = getIntent();
        subscription = i.getParcelableExtra("IncomingSubscription");
        index = i.getIntExtra("Index", 999);
        wireWidgets();
        setViews();
    }

    public void wireWidgets() {
        nameView = (TextView) findViewById(R.id.textView_name);
        descView = (TextView) findViewById(R.id.textView_desc);
        priceView = (TextView) findViewById(R.id.textView_price);
        dueDateView = (TextView) findViewById(R.id.textView_dueDate);
        editButton = (ImageButton) findViewById(R.id.imageButton_edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open edit dialog, delete oldItem from subList, add newItem to subList, save subList
                ArrayList<Subscription> subList = null;
                try {
                    subList = Utility.readList(SubscriptionView.this.getApplicationContext(), "apk");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (subList != null && subList.size() != 0) {
                    AlertDialog addSubDialog = new AlertDialog.Builder(SubscriptionView.this).create();
                    LayoutInflater inflater = (SubscriptionView.this).getLayoutInflater();
                    View aV = inflater.inflate(R.layout.dialog_newsub, null);
                    addSubDialog.setView(aV);
                    addSubDialog.setTitle("Edit Subscription:");
                    final EditText nameEditText = (EditText) aV.findViewById(R.id.editText_name);
                    final EditText descEditText = (EditText) aV.findViewById(R.id.editText_desc);
                    final EditText priceEditText = (EditText) aV.findViewById(R.id.editText_price);
                    final EditText monthEditText = (EditText) aV.findViewById(R.id.editText_month);
                    final EditText dayEditText = (EditText) aV.findViewById(R.id.editText_day);

                    nameEditText.setHint("Name (" + subscription.getName() +")");
                    descEditText.setHint("Description (" + subscription.getDesc() +")");
                    priceEditText.setHint("Name (" + subscription.getPrice() +")");
                    monthEditText.setHint("Next Month (" + subscription.getNextMonth() +")");
                    dayEditText.setHint("Next Day (" + subscription.getNextDay() +")");

                    addSubDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final ArrayList<Subscription> finalSubList = subList;
                    addSubDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d("dialog", "onClick: " + (nameEditText == null));
                                    String name = nameEditText.getText().toString();
                                    String desc = descEditText.getText().toString();
                                    double price = Double.parseDouble(priceEditText.getText().toString());
                                    int nextMonth = Utility.getInt(monthEditText.getText().toString());
                                    int nextDay = Utility.getInt(dayEditText.getText().toString());
                                    Subscription newSub = new Subscription("" + name, "" + desc, price,
                                            nextMonth, nextDay);
                                    SubscriptionView.this.subscription = newSub;
                                    setViews();
                                    finalSubList.set(index, newSub);
                                    try {
                                        Utility.saveList(SubscriptionView.this.getApplicationContext(), "apk", finalSubList);
                                        ;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    addSubDialog.show();
                }
            }
        });
    }

    public void setViews() {
        nameView.setText(subscription.getName());
        descView.setText(subscription.getDesc());
        priceView.setText("$" + subscription.getPrice());
        dueDateView.setText("next due: " + subscription.getNextMonth() + "/" + subscription.getNextDay());
    }

}
