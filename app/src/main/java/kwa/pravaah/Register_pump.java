package kwa.pravaah;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kwa.pravaah.database.DbManager;

public class Register_pump extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "KWATest: RegPump";
    EditText Phon , sheet_id;
    private DbManager db;
    boolean mFlag;
    Button reg, updateID;
    TextView nm;
    String PhoneNo, Name ,sheet;

    String  num1, num,name;
    private static final int CONTACT_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pump);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        db=new DbManager(this);
        final int mValue = db.numOfRows1();
        if (mValue == 0) {
            mFlag = true;
        } else {
            mFlag = false;
        }

        reg = findViewById(R.id.reg_data);
        Phon = findViewById(R.id.no_phn);
        nm = findViewById(R.id.name);
        sheet_id = findViewById(R.id.sheetid);
        updateID = findViewById(R.id.UpsheetID);


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = Phon.getText().toString();
                sheet = sheet_id.getText().toString();

                name = nm.getText().toString();

                if (num.length() == 10) {
                    num1 = "0" + num;
                } else {
                    num1 = num.replace("+91", "0");//you can instead use Phone.NORMALIZED_NUMBER if you're using a high-enough API level
                }
                if (num1.length() >= 11) {
                    if (mFlag) {
                        db.insertPumpDetails(num1, name, sheet);
                        Log.i(TAG,"Db : inserted!");
                        Intent i1 = new Intent(Register_pump.this, Register_pump.class);
                        startActivity(i1);
                        Toast.makeText(Register_pump.this, "inserted!!", Toast.LENGTH_LONG).show();
                    } else {
                        db.insertPumpDetails(num1, name, sheet);
                        Intent i1 = new Intent(Register_pump.this, Register_pump.class);
                        Log.i(TAG,"Db : inserted!");
                        startActivity(i1);
                        Toast.makeText(Register_pump.this, "inserted!!", Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(Register_pump.this, "Choose a valid number !!!", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(Register_pump.this,Register_pump.class);
                    startActivity(i1);
                }

            }
        });

        updateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheet = sheet_id.getText().toString();
                num = Phon.getText().toString();
                if (num.length() == 10) {
                    num1 = "0" + num;
                } else {
                    num1 = num.replace("+91", "0");//you can instead use Phone.NORMALIZED_NUMBER if you're using a high-enough API level
                }
                if(db.Checknumber(num1))
                {
                    db.UpdateSheeetID(num1,sheet);
                    Intent i1 = new Intent(Register_pump.this, Register_pump.class);
                    startActivity(i1);

                    Toast.makeText(Register_pump.this, "Successfully Updated!!!", Toast.LENGTH_SHORT).show();

               }
               else {
                    Intent i1 = new Intent(Register_pump.this,Register_pump.class);
                    startActivity(i1);

                    Toast.makeText(Register_pump.this, "Register First!!!", Toast.LENGTH_SHORT).show();
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


        if (id == R.id.nav_home) {
            Intent i1 = new Intent(Register_pump.this,Home.class);
            startActivity(i1);
        } else if (id == R.id.nav_setAlarm) {
            Intent i1 = new Intent(Register_pump.this,AddAlarm.class);
            startActivity(i1);
        }
        else if (id == R.id.nav_reg_pump) {
            Intent i2 = new Intent(Register_pump.this, Register_pump.class);
            startActivity(i2);

        }else if (id == R.id.nav_Cancel) {
            Intent i1 = new Intent(Register_pump.this,CancelAlarm.class);
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

    public void contactPickerOnClick(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICK);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case CONTACT_PICK:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }


    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            PhoneNo = cursor.getString(phoneIndex);
            Name = cursor.getString(nameIndex);
            // Set the value to the textviews
            //textView1.setText(name);
            /*if (PhoneNo.length() == 13)
                PhoneNo = PhoneNo.substring(3, 13);
            else if (PhoneNo.length() == 14)
                PhoneNo= PhoneNo.substring(4,14);
*/

            Phon.setText(PhoneNo);
            nm.setText(Name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
