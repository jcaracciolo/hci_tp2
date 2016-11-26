package ar.edu.itba.dreamtrip.common.notifications;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;


/**
 * Created by Julian Benitez on 11/25/2016.
 */
public class TrackedChangesManager {
    private static TrackedChangesManager ourInstance ;
    private Context context;


    private static final Integer flightStateCheckJobID = 1;
    private static final Integer legDealsCheckJobID = 2;

    public static TrackedChangesManager getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new TrackedChangesManager(context.getApplicationContext());
        }
        return ourInstance;
    }

    private TrackedChangesManager(Context context) {
        this.context = context;
    }

    private static Integer kJobID = 0;
    private FlightStateCheckService myJobService;

    public void setupChecks(){
        setupLegDealsCheck();
        setupFlightStateCheck();
    }

    public void setupLegDealsCheck(){
        ComponentName mServiceComponent = new ComponentName(context,LegDealsCheckService.class);
        JobInfo.Builder builder = new JobInfo.Builder(legDealsCheckJobID, mServiceComponent);
//        builder.setMinimumLatency(5 * 1000); // wait at least
//        builder.setOverrideDeadline(20 * 1000); // maximum delay
        builder.setPeriodic(24 * 60 * 60 * 1000);
        builder.setPeriodic(30 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
        builder.setRequiresDeviceIdle(false); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(legDealsCheckJobID);
        jobScheduler.schedule(builder.build());
    }

    public void setupFlightStateCheck(){
        ComponentName mServiceComponent = new ComponentName(context,FlightStateCheckService.class);
        JobInfo.Builder builder = new JobInfo.Builder(flightStateCheckJobID, mServiceComponent);
//        builder.setMinimumLatency(5 * 1000); // wait at least
//        builder.setOverrideDeadline(20 * 1000); // maximum delay
        builder.setPeriodic(getFlightStateCheckInterval() * 10 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
        builder.setRequiresDeviceIdle(false); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(flightStateCheckJobID);
        jobScheduler.schedule(builder.build());
    }

    private Integer getFlightStateCheckInterval(){
        return SettingsManager.getInstance(context).getFlightStateCheckInterval();
    }
}
