package kwa.pravaah;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kwa.pravaah.database.DbManager;

public class CancelAlarm extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    Spinner spinner;
    DbManager db;
    List<String> rows;
    ArrayAdapter<String> dataAdapter;
    TextView tv;
    String item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv= findViewById(R.id.tV2);
        db = new DbManager(getApplicationContext());

        spinner = (Spinner) findViewById(R.id.spinner);
        Button button=(Button)findViewById(R.id.button);

        spinner.setOnItemSelectedListener(this);

        loadSpinnerData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor=db.getPendingIntent(item);
                if(cursor.getCount()!=0) {

                    cursor.moveToFirst();
                    String Pending_intent_to_on = cursor.getString(cursor.getColumnIndex(db.PENDING_INTENT_ON));
                    String Pending_intent_to_off = cursor.getString(cursor.getColumnIndex(db.PENDING_INTENT_OFF));


                    cancelAlarm(Pending_intent_to_on);
                    cancelAlarm(Pending_intent_to_off);
                    db.deleteRow(String.valueOf(spinner.getSelectedItem()));
                    Toast.makeText(CancelAlarm.this, "Alarm deleted Succesfully!!!", Toast.LENGTH_LONG).show();
                    loadSpinnerData();
                    Intent i2 = new Intent(CancelAlarm.this, Home.class);
                    startActivity(i2);
                }
            }
        });

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

        if (id == R.id.nav_home) { Intent i3 = new Intent(CancelAlarm.this,Home.class);
            startActivity(i3);
        } else if (id == R.id.nav_setAlarm) {

            Intent i3 = new Intent(CancelAlarm.this,AddAlarm.class);
            startActivity(i3);

        }
        else if (id == R.id.nav_reg_pump) {
            Intent i2 = new Intent(CancelAlarm.this, Register_pump.class);
            startActivity(i2);

        }else if (id == R.id.nav_Cancel) {
            Intent i3 = new Intent(CancelAlarm.this,CancelAlarm.class);
            startActivity(i3);

        }  else if (id == R.id.nav_share) {
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
            String url = "https://docs.google.com/spreadsheets/d/1IcsEfodyeh7z6l_I15fI2LqKCe3YOrJnPnEQlChDwNo/edit#gid=0";

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
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();
        Cursor cursor=db.getDataUsername(item);
        if(cursor.getCount()!=0) {

            cursor.moveToFirst();
            String name =  cursor.getString(cursor.getColumnIndex(db.NAME));

            tv.setText(name+" :- Shift "+item);
        } // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void loadSpinnerData() {
        // database handler

        // Spinner Drop down elements
         rows = db.getDetails();

        // Creating adapter for spinner
         dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rows);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    public void cancelAlarm(String pndIntent)
    {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(),
               Integer.parseInt(pndIntent),intent,0);
        aManager.cancel(pIntent);
    }
}
