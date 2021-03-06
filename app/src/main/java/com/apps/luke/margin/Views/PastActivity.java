package com.apps.luke.margin.Views;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.apps.luke.margin.Model.Event;
import com.apps.luke.margin.Model.EventEntry;
import com.apps.luke.margin.Model.User;
import com.apps.luke.margin.R;
import com.apps.luke.margin.Utils.GlobalVariables;

import java.util.LinkedList;
import java.util.List;

public class PastActivity extends Activity {


    Spinner sEvents;
    TableLayout table;

    List<Event> eventList;
    List<String> eventStrings;
    ArrayAdapter<String> eventAdapter;
    Event currentEvent;

    TextView eventWinner;

    Button back;

    protected GlobalVariables gv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("PastActivity", "Start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_menu);

        gv = (GlobalVariables) this.getApplication();

        back = (Button) findViewById(R.id.bBack);

        sEvents = (Spinner) findViewById(R.id.sEvents);
        table = (TableLayout) findViewById(R.id.tlTable);
        table.setStretchAllColumns(true);
        table.setPadding(5, 5, 5, 5);

        eventWinner = (TextView) findViewById(R.id.tvWinner);
        eventWinner.setText("Setting Text for test");


        eventList = gv.getDatabase().getEventsBasedOnStatus("C");

        eventStrings = new LinkedList<String>();

        for(int i = 0; i < eventList.size(); i++)
        {
            eventStrings.add(eventList.get(i).getEvent_Name());
        }
        eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventStrings);
        sEvents.setAdapter(eventAdapter);

        sEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //int eventID = 0;
                Log.d("onItemSelected","start");
                for (int i = 0; i < eventList.size(); i++) {
                    if (eventList.get(i).getEvent_Name().equals(sEvents.getSelectedItem().toString())) {

                        currentEvent = eventList.get(i);

                        table.removeAllViews();

                        List<EventEntry> entries = gv.getDatabase().getEntriesOfEvent(currentEvent.getEvent_ID());
                        Log.d("Entries Size", Integer.toString(entries.size()));
                        buildTable(entries);

                        int winningUserId = 0;

                        for(int x = 0; x < entries.size(); x++)
                        {
                            if(entries.get(x).getEntry_Margin() == currentEvent.getEvent_Final_Margin())
                            {
                                //u = gv.getDatabase().getUser(entries.get(x).getUser_ID());
                                winningUserId = entries.get(x).getUser_ID();
                            }
                        }

                        User u = gv.getDatabase().getUser(winningUserId);

                        String winner = u.getUser_Name();

                        eventWinner.setText("Event Winner - " + winner + " (" + Integer.toString(currentEvent.getEvent_Final_Margin()) + ")");

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buildTable(List<EventEntry> entries)
    {
        TableRow trHeader = new TableRow(PastActivity.this);

        TextView tvEntryId = new TextView(PastActivity.this);
        tvEntryId.setText("Entry Number");
        tvEntryId.setTextColor(Color.BLACK);
        tvEntryId.setTypeface(Typeface.DEFAULT_BOLD);
        tvEntryId.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tvUserName = new TextView(PastActivity.this);
        tvUserName.setText("User Name");
        tvUserName.setTextColor(Color.BLACK);
        tvUserName.setTypeface(Typeface.DEFAULT_BOLD);
        tvUserName.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tvMargin = new TextView(PastActivity.this);
        tvMargin.setText("Margin");
        tvMargin.setTextColor(Color.BLACK);
        tvMargin.setTypeface(Typeface.DEFAULT_BOLD);
        tvMargin.setGravity(Gravity.CENTER_HORIZONTAL);

        trHeader.addView(tvEntryId);
        trHeader.addView(tvUserName);
        trHeader.addView(tvMargin);

        table.addView(trHeader, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

        for(int i = 0; i < entries.size(); i++)
        {
            TableRow tr = new TableRow(PastActivity.this);
            //OrderLines.get(x);
            //tr.setId(x);
            //tr.setTag(OrderLines_Local.get(x).getProduct_Name());
            //tr.setLayoutParams(new LayoutParams(
            //         LayoutParams.WRAP_CONTENT,
            //         LayoutParams.WRAP_CONTENT));


            TextView tvEId = new TextView(PastActivity.this);
            tvEId.setText(Integer.toString(i+1));
            tvEId.setTextColor(Color.BLACK);
            tvEId.setGravity(Gravity.CENTER_HORIZONTAL);
            tr.addView(tvEId);

            TextView tvUN = new TextView(PastActivity.this);
            User u = gv.getDatabase().getUser(entries.get(i).getUser_ID());
            tvUN.setText(u.getUser_Name());
            tvUN.setTextColor(Color.BLACK);
            tvUN.setGravity(Gravity.CENTER_HORIZONTAL);
            tr.addView(tvUN);

            TextView tvMarg = new TextView(PastActivity.this);
            tvMarg.setText(Integer.toString(entries.get(i).getEntry_Margin()));
            tvMarg.setTextColor(Color.BLACK);
            tvMarg.setGravity(Gravity.CENTER_HORIZONTAL);
            tr.addView(tvMarg);



            //labelTV.setId(500+x);


            //tvEId.setWidth(280);
            //tvEId.setPadding(0, 0, 20, 15);



            table.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


            //labelTV.setLayoutParams(new LayoutParams(
            //        LayoutParams.WRAP_CONTENT,
            //        LayoutParams.WRAP_CONTENT));

            /*
            TextView tvQty = new TextView(Menu_Order_Generation.this);
            //labelTV.setId(1000+x);
            tvQty.setText(Integer.toString(OrderLines_Local.get(x).getOrder_Qty()));
            tvQty.setTextColor(Color.BLACK);
            tvQty.setTypeface(myMethods.tfArial);
            tvQty.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
            //labelTV.setLayoutParams(new LayoutParams(
            //        LayoutParams.WRAP_CONTENT,
            //        LayoutParams.WRAP_CONTENT));

            //String custDetails = tempCust.toString();
            //customerList.add(custDetails);


            tr.addView(tvQty);

            table.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
                */
        }


    }
}