package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;


public class TrackLegTask extends AsyncTaskInformed<Object,Void,Boolean>{

    private Context context;
    private String identifier;
    private String originID;
    private String destinationID;
    private HashSet<Dependency> dependencies;

    public TrackLegTask(Context context, String identifier) {
        this.context = context;
        if(!identifier.matches(context.getString(R.string.valid_leg_regex)))
            throw new RuntimeException("Illegal identifier to track");
        this.originID = identifier.split(" ")[0];
        this.identifier = identifier;
        this.destinationID = identifier.split(" ")[1];
        dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
    }
    public TrackLegTask(Context context, String originID, String destinationID) {
        this.context = context;
        this.originID = originID;
        this.destinationID = destinationID;
        this.identifier = originID + destinationID;
        if(!identifier.matches(context.getString(R.string.valid_leg_regex)))
            throw new RuntimeException("Illegal identifier to track");
        dependencies = new HashSet<>();
        dependencies.add(new Dependency(DependencyType.AIRPORTS));
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        return dependencies;
    }


    @Override
    protected Boolean doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Boolean tracked = true;
        if(!identifier.matches(context.getString(R.string.valid_leg_regex))){
            tracked = false;
        }
        if(originID.equals(destinationID)) tracked = false;
        if( dataHolder.getCityById(originID) == null && dataHolder.getAirportById(originID)== null ) {
            tracked = false;
        }
        if( dataHolder.getCityById(destinationID) == null && dataHolder.getAirportById(destinationID)== null ) {
            tracked = false;
        }
        SettingsManager se = SettingsManager.getInstance(context);
        if(se.getTrackedLegs().contains(identifier)) tracked = false;
        if(tracked){
            tracked = se.trackLeg(identifier);
        }
        return tracked;
    }
    @Override
    protected void onPostExecute(Boolean successfullyTracked) {
        String msg = context.getString(successfullyTracked? R.string.success_tracking_leg :R.string.error_tracked_leg );
        Toast.makeText(context, msg,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context.getResources().getString(R.string.UpdateTrackedDeals));
        context.sendBroadcast(intent);
    }

}
