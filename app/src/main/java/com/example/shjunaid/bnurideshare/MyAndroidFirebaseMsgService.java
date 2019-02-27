package com.example.shjunaid.bnurideshare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Talha on 12/21/2016.
 */
public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("Driver")){
            String mess = remoteMessage.getData().get("message");
            String reqname = remoteMessage.getData().get("requestername");
            String pickuploc = remoteMessage.getData().get("pickuplocation");
            String dest = remoteMessage.getData().get("destination");
            String reqid = remoteMessage.getData().get("requesterid");
            String rideid = remoteMessage.getData().get("rideId");
            NotifyDriver(mess, reqname, pickuploc, dest, reqid, rideid);
        }
        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("PassengerAccepted")){
            String mess = remoteMessage.getData().get("message");
            NotifyPassenger(mess);
        }
        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("PassengerRejected")){
            String mess = remoteMessage.getData().get("message");
            NotifyPassenger(mess);
        }

        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("AllowResetPassword")){
            NotifyResetPassword();
        }

        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("JoinedPassengers")){
            String mess = remoteMessage.getData().get("message");
            NotifyJoinedPassengers(mess);
        }

        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("PassengerLeft")){
            String mess = remoteMessage.getData().get("message");
            NotifyDriverThatPassengerLeft(mess);
        }

        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("TextMessageToPassenger")){
            String mess = remoteMessage.getData().get("message");
            String drivername = remoteMessage.getData().get("drivername");
            NotifyPassengerDriversTextMessage(mess,drivername);
        }
        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("DriverRemovedPassenger")){
            String mess = remoteMessage.getData().get("message");
            NotifyPassengerRemovedByDriver(mess);
        }

        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("RideStarted")){
            String mess = remoteMessage.getData().get("message");
//            String drivername = remoteMessage.getData().get("drivername");
            int rideid = Integer.parseInt(remoteMessage.getData().get("RideId"));
            NotifyRideStartedStatusToPassenger(mess,rideid);
        }
        else if (remoteMessage.getData().get("notifyTo")!= null && remoteMessage.getData().get("notifyTo").equals("RateDriver")){
            String mess = remoteMessage.getData().get("message");
            String drivername = remoteMessage.getData().get("drivername");
            String source = remoteMessage.getData().get("source");
            String destination = remoteMessage.getData().get("destination");
            int driverid = Integer.parseInt(remoteMessage.getData().get("driverid"));
            NotifyRateDriverToPassenger(mess,drivername,driverid,source,destination);
        }

        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //createNotification(remoteMessage.getNotification().getBody(),value);
    }

    private void NotifyPassenger(String message) {
        int id = 112;
        Intent resultIntent = new Intent( this , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this , 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Ride Request Response")
                .setContentText(message)
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyDriver(String message,String reqname,String pickuploc, String dest,String reqid,String rideid) {
        int id = 111;
        Intent resultIntent = new Intent( this , ReceiveRideRequestActivity.class);
        resultIntent.putExtra("RequesterName", reqname);
        resultIntent.putExtra("RequesterId", reqid);
        resultIntent.putExtra("PickupLocation", pickuploc);
        resultIntent.putExtra("Destination", dest);
        resultIntent.putExtra("RideId", rideid);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this , 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Ride Request")
                .setContentText(message)
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyJoinedPassengers(String message) {
        int id = 114;
        Intent resultIntent = new Intent( this , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Joined Ride Alert!!")
                .setContentText("Ride Cancelled")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Ride Detail:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyDriverThatPassengerLeft(String message) {
        int id = 115;
        Intent resultIntent = new Intent( this , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Posted Ride Alert!!")
                .setContentText("Passenger Left")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Posted Ride Detail:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyPassengerDriversTextMessage(String message,String driverName) {
        int id = 116;
        Intent resultIntent = new Intent( this , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Message!!")
                .setContentText(driverName+" sent you a message")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Message:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyPassengerRemovedByDriver(String message) {
        int id = 117;
        Intent resultIntent = new Intent( this , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Ride Alert!!")
                .setContentText("Driver removed you.")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Detail:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }

    private void NotifyRideStartedStatusToPassenger(String message,int rideid) {
        int id = 118;
        Intent resultIntent = new Intent( this , ViewDriverLocation.class);
        resultIntent.putExtra("RideId", rideid);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Ride Alert!!")
                .setContentText("Ride Started.")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Detail:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }
    private void NotifyRateDriverToPassenger(String message,String drivername,int driverid,String source,String destination) {
        int id = 119;
        Intent resultIntent = new Intent( this , RatingDriverActivity.class);
        resultIntent.putExtra("driverid",driverid);
        resultIntent.putExtra("drivername",drivername);
        resultIntent.putExtra("source",source);
        resultIntent.putExtra("destination",destination);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Ride Alert!!")
                .setContentText(drivername+" Ride Completed.")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Detail:");
        bigTextStyle.bigText(message);
        mNotificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }

    private void NotifyResetPassword() {
        int id= 001;
        Intent notifyIntent = new Intent(this, ResetPasswordActivity.class);
        notifyIntent.putExtra("ActivityName", "ResetPasswordActivity");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Password Reset ?")
                .setContentText("You requested password reset.")
                .setAutoCancel( false )
                .setSound(notificationSoundURI)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mNotificationBuilder.build());
    }


    private void createNotification( String messageBody,String pswd) {
        Intent intent = new Intent( this , MainActivity.class );
        intent.putExtra("Password",pswd);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle("Notification form .net")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
