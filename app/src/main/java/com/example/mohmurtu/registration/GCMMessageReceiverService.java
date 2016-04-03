package com.example.mohmurtu.registration;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.SharedPrefUtil;
import com.google.android.gms.gcm.GcmListenerService;

public class GCMMessageReceiverService extends GcmListenerService {
    public GCMMessageReceiverService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        System.out.println("Message received is " + message);
        if(message.equals("Seller Registered"))
            SharedPrefUtil.setBooleanPrefs(Constants.IS_GCM_ID_UPLOADED_IN_SERVER,true);
        else
            sendNotification(message);

    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("productNotification", "true");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String notificationMessage = "";
        int notificaitonId = -1 ;
        if(message.contains("approved")){
            notificaitonId = 0 ; // All approved notificaitons will go to notification id 0
            String approvedMessage = SharedPrefUtil.getStringPrefs(Constants.APPROVED_NOTIFICATION_MESSAGE);
            if(approvedMessage.equals("")){
                notificationMessage = message ;
            }
            else{
                int productCount = Integer.parseInt(approvedMessage.substring(0, 1).trim());
                notificationMessage = (productCount + 1) + " products are approved";
            }
            SharedPrefUtil.setStringPrefs(Constants.APPROVED_NOTIFICATION_MESSAGE, notificationMessage);
        }
        else if(message.contains("rejected")){
            notificaitonId = 1 ; // All rejected notifications will go to notification id 1
            String rejectedMessage = SharedPrefUtil.getStringPrefs(Constants.REJECTED_NOTIFICATION_MESSAGE);
            if(rejectedMessage.equals("")){
                notificationMessage = message;
            }
            else{
                int productCount = Integer.parseInt(rejectedMessage.substring(0, 1).trim());
                notificationMessage = (productCount + 1) + " products are rejected";
            }
            SharedPrefUtil.setStringPrefs(Constants.REJECTED_NOTIFICATION_MESSAGE, notificationMessage);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle("GCM Message")
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificaitonId /* ID of notification */, notificationBuilder.build());
    }
}
