package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;

import java.util.Objects;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class TrackedLegsDependency extends Dependency {

    private Integer timeout;

    public TrackedLegsDependency(Context context, Integer timeout) {
        super(DependencyType.TRACKED_LEGS);
        this.timeout = timeout;

        for (String identifier: SettingsManager.getInstance(context).getTrackedLegs()) {
            addDependency(new FlightDealsDependency(identifier.split(" ")[0],false,false));
            addDependency(new FlightDealsDependency(identifier.split(" ")[0],false,true));
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrackedLegsDependency that = (TrackedLegsDependency) o;
        return that == this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeout);
    }
}
