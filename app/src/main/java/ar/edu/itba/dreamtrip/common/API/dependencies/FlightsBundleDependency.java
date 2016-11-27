package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Date;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public class FlightsBundleDependency extends Dependency {
    private Date startingDate;
    private Integer daysToLoad;
    private String originID;
    private String destinationID;

    public FlightsBundleDependency(String originID, String destinationID, Date startingDate, Integer daysToLoad) {
        super(DependencyType.FLIGHT_BUNDLE);
        this.startingDate = startingDate;
        this.daysToLoad = daysToLoad;
        this.originID = originID;
        this.destinationID = destinationID;
    }

}
