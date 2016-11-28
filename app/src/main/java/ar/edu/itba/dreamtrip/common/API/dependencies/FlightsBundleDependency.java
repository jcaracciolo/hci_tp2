package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ar.edu.itba.dreamtrip.common.model.StatusSearch;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public class FlightsBundleDependency extends Dependency {
    private Date startingDate;
    private Integer daysToLoad;
    private String originID;
    private String destinationID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlightsBundleDependency that = (FlightsBundleDependency) o;
        return that == this;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startingDate, daysToLoad, originID, destinationID);
    }

    public FlightsBundleDependency(String originID, String destinationID, Date startingDate, boolean lastMinute) {
        super(DependencyType.FLIGHT_BUNDLE);
        this.startingDate = startingDate;
        this.daysToLoad = lastMinute? 3:7;
        this.originID = originID;
        this.destinationID = destinationID;
        for (int i = 7;i<7+daysToLoad;i++){
            addDependency(new FlightDependency(originID,destinationID,getDateAfterXDays(startingDate,i)));
        }
        this.originID = originID;
        this.destinationID = destinationID;
    }

    private Date getDateAfterXDays(Date date, int days){
        if(date == null) throw new RuntimeException("Null date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
