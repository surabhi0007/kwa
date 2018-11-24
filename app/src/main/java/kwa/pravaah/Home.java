package kwa.pravaah;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import kwa.pravaah.database.DbManager;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "KWATesting";
    private static final int PHONE_REQUEST = 101;
    private static final int SMS_REQUEST = 102;
    private static final int RECEIVE_SMS_REQUEST = 103;



    private TableLayout tableLayout;

    DbManager db;

    private boolean isReadSmsPermissionGranted = false;
    private boolean isReceiveSmsPermissionGranted = false;
    private boolean isPhoneCallPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkPermission();

        tableLayout = (TableLayout) findViewById(R.id.viewtable);
        db = new DbManager(Home.this);

        int numrows = db.numOfRows();
        Cursor cursor = db.viewData();

        if (numrows != 0) {
            try {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {


                        String id_str = cursor.getString(cursor.getColumnIndex(db.ID));
                        String name_str = cursor.getString(cursor.getColumnIndex(db.NAME));
                        String power_str = cursor.getString(cursor.getColumnIndex(db.POWER));
                        String pump_str = cursor.getString(cursor.getColumnIndex(db.PUMP));
                        String on_str = cursor.getString(cursor.getColumnIndex(db.TIME_ON));
                        String off_str = cursor.getString(cursor.getColumnIndex(db.TIME_OFF));

                        View tableRow = LayoutInflater.from(this).inflate(R.layout.content_home, null, false);


                        TextView id = (TextView) tableRow.findViewById(R.id.id);
                        TextView Name = (TextView) tableRow.findViewById(R.id.name);
                        TextView Power = (TextView) tableRow.findViewById(R.id.power);
                        TextView Pump = (TextView) tableRow.findViewById(R.id.pump);
                        TextView ON = (TextView) tableRow.findViewById(R.id.ON);
                        TextView OFF = (TextView) tableRow.findViewById(R.id.OFF);

                          id.setText(id_str.toString());
                        Name.setText(name_str.toString());
                        Power.setText(power_str.toString());
                        Pump.setText(pump_str.toString());
                        ON.setText(on_str.toString());
                        OFF.setText(off_str.toString());
                        tableLayout.addView(tableRow);

                    }
                }
            }catch (Exception e){

            }
        }
    }

    public void checkPermission(){
        if(!isPhoneCallPermissionGranted){
            askPhoneCallPermission();
        }else{
            if(!isReceiveSmsPermissionGranted){
                askReceiveSmsPermission();
            }else if (!isReadSmsPermissionGranted){
                askReadSmsPermission();
            }
        }
    }

    public void askPhoneCallPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: No permission.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PHONE_REQUEST);
        } else {
            Log.i(TAG, "onCreate: Phone permitted");
            isPhoneCallPermissionGranted = true;
        }
    }

    public void askReceiveSmsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: No permission.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_REQUEST);
        } else {
            Log.i(TAG, "onCreate: Messaging permitted");
            isReadSmsPermissionGranted = true;
        }
    }

    public void askReadSmsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: No permission.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_REQUEST);
        } else {
            Log.i(TAG, "onCreate: Messaging permitted");
            isReceiveSmsPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PHONE_REQUEST) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted");
                Toast.makeText(this, "Call permission Granted!!!", Toast.LENGTH_SHORT).show();
                checkPermission();
            } else {
                Toast.makeText(this, "No permission to use Phone. App won't work as expected", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onRequestPermissionsResult: Permission denied");
                isPhoneCallPermissionGranted = true;
                checkPermission();
            }
        }
        if (requestCode == SMS_REQUEST) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted");
                Toast.makeText(this, "SMS permission Granted!!!", Toast.LENGTH_SHORT).show();
                checkPermission();
               } else {
                Toast.makeText(this, "No permission to use SMS. App won't work as expected", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onRequestPermissionsResult: Permission denied");
                isReadSmsPermissionGranted = true;
                checkPermission();
            }
        }
        if (requestCode == RECEIVE_SMS_REQUEST) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted");
                Toast.makeText(this, "SMS receive permission Granted!!!", Toast.LENGTH_SHORT).show();
                checkPermission();
            } else {
                Toast.makeText(this, "No permission to use Receive SMS. App won't work as expected", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onRequestPermissionsResult: Permission denied");
                isReceiveSmsPermissionGranted = true;
                checkPermission();
            }
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
            Intent i1 = new Intent(Home.this,Home.class);
            startActivity(i1);
        } else if (id == R.id.nav_setAlarm) {
            Intent i1 = new Intent(Home.this,AddAlarm.class);
            startActivity(i1);
        }
        else if (id == R.id.nav_reg_pump) {
            Intent i2 = new Intent(Home.this, Register_pump.class);
            startActivity(i2);

        }else if (id == R.id.nav_Cancel) {
            Intent i1 = new Intent(Home.this,CancelAlarm.class);
            startActivity(i1);
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
}
