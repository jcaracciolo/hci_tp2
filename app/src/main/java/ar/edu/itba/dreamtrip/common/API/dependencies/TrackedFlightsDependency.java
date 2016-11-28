package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.jar.Pack200;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class TrackedFlightsDependency extends Dependency {

    private Integer timeout;
    private Date creationDate;

    public TrackedFlightsDependency(Context context, Integer timeout) {
        super(DependencyType.TRACKED_FLIGHTS);
        this.timeout = timeout;

        for (String identifier: SettingsManager.getInstance(context).getTrackedFlights()) {
            addDependency(new StatusDependency(identifier,timeout));
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrackedFlightsDependency that = (TrackedFlightsDependency) o;
        return that == this;
    }

}
