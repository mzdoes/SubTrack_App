package com.example.mzdoes.subtrack;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ListView subView;
    public ArrayList<Subscription> subList;
    private ArrayList<String> today;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundResource(R.color.colorPrimaryLight);

        try {
            wireWidgets();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        payUp();
        getReminders();
        sendNotification();

    }

    //methods
    public void wireWidgets() throws IOException, ClassNotFoundException {
        today = new ArrayList<>();
        subView = (ListView) findViewById(R.id.listView_subView);
        subList = new ArrayList<>();
        //subList.add(new Subscription("Spotify", "for me", 9.99, 12, 8, 11, 8));
        try {
            setSubList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, subList) {
            @SuppressLint("ResourceAsColor")
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

                text1.setTextColor(Color.WHITE);
                text2.setTextColor(Color.WHITE);
                view.setBackgroundResource(R.color.colorPrimaryLighter);

                return view;
            }
        };
        subView.setAdapter(adapter);
        subView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent i = new Intent(MainActivity.this, SubscriptionView.class);
                i.putExtra("IncomingSubscription", (Parcelable) subList.get(pos));
                i.putExtra("Index", pos);
                startActivity(i);
            }
        });
        subView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Delete this task?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                subList.remove(pos);

                                try {
                                    Utility.saveList(MainActivity.this.getApplicationContext(), "apk", subList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adapter.clear();
                                adapter.addAll(subList);
                                adapter.notifyDataSetChanged();
                                MainActivity.this.updateTitle();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });
        updateTitle();
    }

    public void setSubList() throws IOException, ClassNotFoundException {
        try {
            subList = Utility.readList(MainActivity.this.getApplicationContext(), "apk");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateTitle() {
        double monthlyCost = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (Subscription sub: subList) {monthlyCost += sub.getPrice();}
        this.setTitle("subTrack ($" + df.format(monthlyCost) + "/mo.)");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_menuButton:
                AlertDialog addSubDialog = new AlertDialog.Builder(MainActivity.this).create();
                LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
                View aV = inflater.inflate(R.layout.dialog_newsub, null);
                addSubDialog.setView(aV);
                addSubDialog.setTitle("Add Subscription:");
                final EditText nameEditText = (EditText) aV.findViewById(R.id.editText_name);
                final EditText descEditText = (EditText) aV.findViewById(R.id.editText_desc);
                final EditText priceEditText = (EditText) aV.findViewById(R.id.editText_price);
                final EditText monthEditText = (EditText) aV.findViewById(R.id.editText_month);
                final EditText dayEditText = (EditText) aV.findViewById(R.id.editText_day);
                addSubDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
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
                                subList.add(newSub);
                                try {
                                   Utility.saveList(MainActivity.this.getApplicationContext(), "apk", subList);;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adapter.clear();
                                adapter.addAll(subList);
                                adapter.notifyDataSetChanged();
                                MainActivity.this.updateTitle();
                            }
                        });
                addSubDialog.show();
                return true;
            case R.id.deleteAll_menuButton:
                AlertDialog deleteDialog = new AlertDialog.Builder(MainActivity.this).create();
                deleteDialog.setTitle("Delete all?");
                deleteDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                subList.removeAll(subList);
                                if (subList.size() == 0) {subList.add(new Subscription(
                                        "To add a new subscription",
                                        "press the add button on the right hand corner",
                                        4.20,
                                        4,
                                        20)); }
                                try {
                                    Utility.saveList(MainActivity.this.getApplicationContext(), "apk", subList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adapter.clear();
                                adapter.addAll(subList);
                                adapter.notifyDataSetChanged();
                                MainActivity.this.updateTitle();
                            }
                        });
                deleteDialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //METHODS
    public void addToSubList(Subscription sub) { subList.add(sub); }

    public void payUp() {
        Calendar calendar = Calendar.getInstance();

        //current day
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);


        for (Subscription subscription : subList) {
            if (subscription.getNextDay() == cDay && subscription.getNextMonth() == cMonth) {
                today.add(subscription.getName() + " ");
                subscription.setDueDate();
            }
        }
    }

    public String getReminders() {
        String reminder = "", message = " ";

        if (today != null && !today.isEmpty()) {
            if (today.size() == 1) {
                message = today.get(0) + "is due for payment";
            } else {
                for (int i = 0; i < today.size() - 1; i++) {
                    reminder += today.get(i);
                    if (today.size() > 2) { reminder += ", "; }
                }
                reminder += "and " + today.get(today.size());
                message = reminder + " are due for payment";
            }
        }
        return message;
    }

    private void sendNotification() {
        if (today != null && !today.isEmpty()) {
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("SubTrack")
                    .setContentText(getReminders());
            Intent resultIntent = new Intent(this, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            nBuilder.setContentIntent(resultPendingIntent);

            int nNotificationId = 001;
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(nNotificationId, nBuilder.build());
        }
        today = null;
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            Utility.saveList(MainActivity.this.getApplicationContext(), "apk", subList);;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            subList = Utility.readList(MainActivity.this.getApplicationContext(), "apk");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        adapter.clear();
        adapter.addAll(subList);
        adapter.notifyDataSetChanged();
        updateTitle();
    }

}
