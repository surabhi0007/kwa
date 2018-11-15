package kwa.pravaah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import kwa.pravaah.database.AsyncResponse;
import kwa.pravaah.database.DbManager;
import kwa.pravaah.database.Pushdata;
import kwa.pravaah.model.message;

public class SMSListener extends BroadcastReceiver {
    private static final String TAG = "KWATest: smslistener";

   public static String no;
    String pwr;
    String pmp;
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        msgBody = msgBody.replace("EVENT HRS", "EVENTHRS");
                        msgBody = msgBody.replace("\n", "\",\"");
                        msgBody = msgBody.replace(" ", "\"");
                        msgBody = "{\"" + msgBody + "\"}";
                        JSONObject msgJson = new JSONObject(msgBody);
                        Log.e("Incoming message", msgJson.toString());

                        Gson gson = new Gson();
                        message message = gson.fromJson(msgBody, message.class);
                        if (msg_from != null && msg_from.length() > 0) {
                            message.setSender(msg_from);
                        }
                        Log.e("Incoming message", msgJson.toString());
                        Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show();

                        DbManager dbManager = new DbManager(context);
                        no = message.getSender().toString();
                        no = no.replace("+91", "0");
                      //  no = no.substring(3,13);
                        String pow = message.getPower().toString();
                        String pump = message.getPump().toString();

                        try {
                           /* boolean isInserted = dbManager.insertUserDetails(message.getSender(), "", message.getPower(), message.getPump(), ""
                                    , "", "", "");*/

                           boolean isInserted = dbManager.UpdateDetails(no,pow,pump);


                            String[] params = {message.getSender(), message.getPower(), message.getPump(), message.getErr(),
                                    message.getEventHrs(), message.getAuto(), message.getTm()};
                            //new Pushdata().execute(params);
                            Pushdata asyncTask = new Pushdata(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {

                                    if ((String)output == "Success") {
                                        Toast.makeText(context, "Sheet Updated Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error in Sheet Updation", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                            asyncTask.execute(params);


                            if (isInserted) {
                                Cursor cursor=dbManager.getPowerStatus(no);
                                if(cursor.getCount()!=0) {
                                    cursor.moveToFirst();
                                  String  power = cursor.getString(cursor.getColumnIndex(dbManager.POWER));

                                    Log.e("inserted", "successfully ,Power:" + power);
                                    Toast.makeText(context, "message inserted to db , Power Status : " +power, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
