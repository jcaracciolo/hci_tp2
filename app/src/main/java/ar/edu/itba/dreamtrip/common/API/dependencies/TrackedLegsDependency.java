package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;

import java.util.Objects;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class TrackedLegsDependency extends Dependency {


    public TrackedLegsDependency(Context context, boolean loadImages, DealLoadType loadType) {
        super(DependencyType.TRACKED_LEGS);

        for (String identifier: SettingsManager.getInstance(context).getTrackedLegs()) {
            if(loadType.equals(DealLoadType.LAST_MINUTE_DEALS) || loadType.equals(DealLoadType.BOTH_DEALS))
                addDependency(new FlightDealsDependency(identifier.split(" ")[0],loadImages,true));
            if(loadType.equals(DealLoadType.REGULAR_DEALS) || loadType.equals(DealLoadType.BOTH_DEALS))
                addDependency(new FlightDealsDependency(identifier.split(" ")[0],loadImages,false));
        }
    }


}
