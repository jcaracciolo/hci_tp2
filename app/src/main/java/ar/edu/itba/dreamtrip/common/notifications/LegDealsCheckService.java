package ar.edu.itba.dreamtrip.common.notifications;

import android.app.job.JobParameters;
import android.app.job.JobService;

import ar.edu.itba.dreamtrip.common.API.DataHolder;

/**
 * Created by vogella on 30.06.16.
 */

public class LegDealsCheckService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
//        String url = params.getExtras().getString(RssApplication.URL);
//        Intent i = new Intent(this, RssDownloadService.class); // starts the RssDownloadService service
//        i.putExtra(RssApplication.URL, url); // some extra data for the service
//        startService(i);
        DataHolder.getInstance(getApplicationContext()).waitForIt(new LegDealsCheckTask(getApplicationContext()));
        return false; // true if we're not done yet
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // true if we'd like to be rescheduled
        return true;
    }


}