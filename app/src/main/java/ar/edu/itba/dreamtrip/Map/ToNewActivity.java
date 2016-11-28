package ar.edu.itba.dreamtrip.Map;

import android.content.Context;
import android.content.Intent;

import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;

/**
 * Created by juanfra on 28/11/16.
 */

public class ToNewActivity {
    Class newActivity;
    Context context;
    String extra;
    String extraKey;

    public ToNewActivity(Class newActivity, Context context, String extra, String extraKey) {
        this.newActivity = newActivity;
        this.context = context;
        this.extra = extra;
        this.extraKey = extraKey;
    }

    public void jump() {
        Intent intent = new Intent(context, newActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(extraKey,extra);
        context.startActivity(intent);
    }
}