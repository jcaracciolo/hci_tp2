package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;

import java.util.Objects;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class TrackedLegsDependency extends Dependency {


    public TrackedLegsDependency(Context context, boolean loadImages) {
        super(DependencyType.TRACKED_LEGS);

        for (String identifier: SettingsManager.getInstance(context).getTrackedLegs()) {
            addDependency(new FlightDealsDependency(identifier.split(" ")[0],loadImages,false));
            addDependency(new FlightDealsDependency(identifier.split(" ")[0],loadImages,true));
        }
    }


}
