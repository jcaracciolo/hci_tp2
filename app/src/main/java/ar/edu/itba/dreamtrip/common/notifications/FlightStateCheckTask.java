package ar.edu.itba.dreamtrip.common.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.main.FlightTracker;


public class FlightStateCheckTask extends AsyncTaskInformed<Object,Void,ArrayList<String>> {
    Context context;
    private Integer idNotification = 0;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    public FlightStateCheckTask(Context context){
        this.context=context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
//        dependencies.add(new AirlinesReviewDependency("AR"));
//        dependencies.add(new FlightDealsDependency("BUE",true));
        dependencies.add(new TrackedFlightsDependency(context,5));
        return dependencies;
    }

    @Override
    protected ArrayList<String > doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> notifications = new ArrayList<>();

        SettingsManager settingsManager = SettingsManager.getInstance(context);
        Collection<FlightState> flightStates = dataHolder.getTrackedFlightStates();
        for (FlightState flightState: flightStates) {
            String identifier = flightState.getIdentifier();
            FlightStatus loadedStatus = flightState.getStatus();
            FlightStatus oldStatus = settingsManager.getFlightStatus(identifier);
            if(!loadedStatus.equals(oldStatus)){
                settingsManager.setFlightStatus(identifier,loadedStatus);
                notifications.add(identifier + " changed to " + loadedStatus);
            }
        }

        return notifications;
    }


    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> flightCards) {
        if(! FlightTracker.isActive){
            for (String s: flightCards){
                pushNotification(s);
            }
        } else {
            toast("didnt show flight notifications, app active");
        }
    }

    private void pushNotification(String msg){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_airplane_l)
                        .setContentTitle("Dreamtrip")
                        .setContentText(msg)
                        .setSound(soundUri);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, FlightTracker.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(idNotification++, mBuilder.build());
    }
}
