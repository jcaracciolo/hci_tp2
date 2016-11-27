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

import ar.edu.itba.dreamtrip.Deals.DealsFromActivity;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.TrackedDestinations.TrackedLegFragment;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.DealLoadType;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedFlightsDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.TrackedLegsDependency;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.main.FlightTracker;


public class LegDealsCheckTask extends AsyncTaskInformed<Object,Void,ArrayList<String>> {
    Context context;
    private Integer idNotification = 0;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    public LegDealsCheckTask(Context context){
        this.context=context;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new TrackedLegsDependency(context,false, DealLoadType.LAST_MINUTE_DEALS));
        return dependencies;
    }

    @Override
    protected ArrayList<String > doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> notifications = new ArrayList<>();

        SettingsManager settingsManager = SettingsManager.getInstance(context);
        Collection<Deal> deals = dataHolder.getTrackedDeals();
        for (Deal deal: deals) {
            String identifier = deal.getDealIdentifier();
            boolean shown = settingsManager.dealIsShown(identifier);
            if(!shown){
                settingsManager.setDealShown(identifier,true);
                notifications.add("NEW DEAL to " + deal.getDestinationDescription() +
                                    "\nStarting at $"+deal.getPrice());
            }
        }

        return notifications;
    }

    @Override
    protected void onPostExecute(ArrayList<String> flightCards) {
        if(!TrackedLegFragment.isActive){
            for (String s: flightCards){
                pushNotification(s);
            }
        } else {
            toast("didnt show deal notifications, app active");
        }
    }


    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }

    private void pushNotification(String msg){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_airplane_l)
                        .setContentTitle("Dreamtrip")
                        .setContentText(msg)
                        .setSound(soundUri)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, FlightTracker.class);
        resultIntent.putExtra("selectedTab",2);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
//        stackBuilder.addNextIntent(changeTabIntent);
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
