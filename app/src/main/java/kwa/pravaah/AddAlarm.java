package kwa.pravaah;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import kwa.pravaah.database.DbManager;

public class AddAlarm extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , AdapterView.OnItemSelectedListener {
    private String POWERON = "ON";
    private String PUMPOFF = "OFF";
    private String intent_off = "000";
    private String time_off = "000";
    public static String  Name,phn;
    public static String Gtime, GAlarm_on;

    private static final int CONTACT_PICK = 1;
    TimePicker setTime;
    Button bt_ON, bt_OFF;
    TextView Phone;
    private DbManager db;
    boolean mFlag;
    Spinner spinner;
    List<String> rows;
    ArrayAdapter<String> dataAdapter;
    TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);



        db=new DbManager(this);
        final int mValue = db.numOfRows();
        if (mValue == 0) {
            mFlag = true;
        } else {
            mFlag = false;
        }


        bt_ON=findViewById(R.id.ON);
        bt_OFF=findViewById(R.id.OFF);

        final Calendar now=Calendar.getInstance();
        setTime=findViewById(R.id.PickTime);
        setTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        setTime.setCurrentMinute(now.get(Calendar.MINUTE));

        tv = findViewById(R.id.name);
        Phone = findViewById(R.id.Phone);

        loadSpinnerData();

        try {

                bt_ON.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar cal = Calendar.getInstance();

                        cal.set(Calendar.HOUR_OF_DAY, setTime.getCurrentHour());
                        cal.set(Calendar.MINUTE, setTime.getCurrentMinute());
                        cal.set(Calendar.SECOND, 00);




                        if (cal.compareTo(now) <= 0) {
                            //Today Set time passed, count to tomorrow
                            cal.add(Calendar.DATE, 1);
                        }
                       // String num = Phone.getText().toString();
                      //  String nm = tv.getText().toString();


                            String time = cal.getTime().toString();
                            time = time.substring(11, 19);

                            Toast.makeText(AddAlarm.this, "Alarm is set @" + time, Toast.LENGTH_SHORT).show();
                            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                            Intent myIntent = new Intent(AddAlarm.this, AlarmReceiver.class);


                            String PhNo = phn + ",2";
                            myIntent.putExtra("Number", PhNo);

                            int alarmID = (int) cal.getTimeInMillis();
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAlarm.this, alarmID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                            assert manager != null;
                            //manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
                    }*/
                            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                            }

                            Toast.makeText(AddAlarm.this, "Shift set", Toast.LENGTH_SHORT).show();
                            String alarmID_to_on = String.valueOf(alarmID);

                            GAlarm_on = alarmID_to_on;
                            if (mFlag) {


                                db.insertUserDetails(phn, Name, POWERON, PUMPOFF, alarmID_to_on, intent_off, time, time_off);

                            } else {
                                db.insertUserDetails(phn, Name, POWERON, PUMPOFF, alarmID_to_on, intent_off, time, time_off);

                            }

                    }

                });

                bt_OFF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                            Calendar cal = Calendar.getInstance();


                            cal.set(Calendar.HOUR_OF_DAY, setTime.getCurrentHour());
                            cal.set(Calendar.MINUTE, setTime.getCurrentMinute());
                            cal.set(Calendar.SECOND, 00);

                            if (cal.compareTo(now) <= 0) {
                                //Today Set time passed, count to tomorrow
                                cal.add(Calendar.DATE, 1);
                            }

                            String time = cal.getTime().toString();
                            time = time.substring(11, 19);
                            Toast.makeText(AddAlarm.this, "Alarm is set @" + time, Toast.LENGTH_SHORT).show();


                            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                            Intent myIntent = new Intent(AddAlarm.this, AlarmReceiver.class);


                            String PhNo = phn + ",3";
                            myIntent.putExtra("Number", PhNo);

                            int alarmID = (int) cal.getTimeInMillis();
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAlarm.this, alarmID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                            assert manager != null;
                            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                            }

                            Toast.makeText(AddAlarm.this, "Shift set", Toast.LENGTH_SHORT).show();

                            String alarmID_to_off = String.valueOf(alarmID);

                            db.addPendingIntent_OFF(GAlarm_on, alarmID_to_off);
                            db.addTime_OFF(GAlarm_on, time);
                            Intent i1 = new Intent(AddAlarm.this, Home.class);
                            startActivity(i1);


                    }


                });

        }
        catch (NullPointerException e) {
            Toast.makeText(this, "Null value", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i2 = new Intent(AddAlarm.this, Home.class);
            startActivity(i2);
        } else if (id == R.id.nav_setAlarm) {
            Intent i2 = new Intent(AddAlarm.this, AddAlarm.class);
            startActivity(i2);

        } else if (id == R.id.nav_reg_pump) {
            Intent i2 = new Intent(AddAlarm.this, Register_pump.class);
            startActivity(i2);

        }
        else if (id == R.id.nav_Cancel) {
            Intent i2 = new Intent(AddAlarm.this, CancelAlarm.class);
            startActivity(i2);

        }

        else if (id == R.id.nav_share) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nClick on this link to download \n\n";
                sAux = sAux + "https://drive.google.com/open?id=14OYE75Bn5zyv9g_JetIXuDlZl9oxZ4PY \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_gsheet) {
            String url ="https://docs.google.com/spreadsheets/d/1IcsEfodyeh7z6l_I15fI2LqKCe3YOrJnPnEQlChDwNo/edit#gid=0";

            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
        else if (id == R.id.nav_EXIT)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         String item = parent.getItemAtPosition(position).toString();
        Cursor cursor=db.getnumber(item);
        if(cursor.getCount()!=0) {

            cursor.moveToFirst();
            phn =  cursor.getString(cursor.getColumnIndex(db.PHN_NO));
            Name = item;


            Phone.setText(phn);
            tv.setText(Name);
        }
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void loadSpinnerData() {
        // database handler

        // Spinner Drop down elements
        rows = db.getPumpDetails();

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rows);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
}
