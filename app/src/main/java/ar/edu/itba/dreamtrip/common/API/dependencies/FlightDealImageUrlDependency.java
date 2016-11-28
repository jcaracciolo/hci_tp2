package ar.edu.itba.dreamtrip.common.API.dependencies;

import ar.edu.itba.dreamtrip.common.model.Deal;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public class FlightDealImageUrlDependency extends Dependency {

    public FlightDealImageUrlDependency(boolean lastMinute) {
        super(lastMinute? DependencyType.LAST_MINUTE_DEALS_IMAGES_URLS
                : DependencyType.DEALS_IMAGES_URLS);
    }


    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
