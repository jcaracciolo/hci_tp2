package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.HashSet;
import java.util.Objects;

import ar.edu.itba.dreamtrip.common.model.Deal;

/**
 * Created by Julian Benitez on 11/24/2016.
 */

public class LastMinuteFlightDealsDependency extends Dependency {
    public String getOriginID() {
        return originID;
    }

    private String originID;

    public LastMinuteFlightDealsDependency(String originID, boolean loadImages) {
        super(loadImages? DependencyType.LAST_MINUTE_DEALS : DependencyType.LAST_MINUTE_DEALS_DATA);
        if(loadImages){
            Dependency imageURLDep = new Dependency(DependencyType.LAST_MINUTE_DEALS_IMAGES_URLS);
            Dependency imageDep = new Dependency(DependencyType.LAST_MINUTE_DEALS_IMAGES);
            Dependency dataDep = new LastMinuteFlightDealsDependency(originID,false);

            imageURLDep.addDependency(dataDep);

            imageDep.addDependency(dataDep);
            imageDep.addDependency(imageURLDep);

            addDependency(dataDep);
            addDependency(imageDep);
        }
        this.originID = originID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LastMinuteFlightDealsDependency that = (LastMinuteFlightDealsDependency) o;
        return Objects.equals(originID, that.originID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), originID);
    }
}
