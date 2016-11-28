package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Calendar;
import java.util.Date;

import ar.edu.itba.dreamtrip.common.model.StatusSearch;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class StatusDependency extends Dependency {

    private Date creationDate;
    private String flightIdentifier;
    private Integer timeout;

    public StatusDependency(String flightIdentifier, Integer timeout) {
        super(DependencyType.FLIGHT_STATUS);
        if(!flightIdentifier.matches("^\\w{2} \\d{3,4}$"))
            throw new RuntimeException("Illegal flight identifier" + flightIdentifier);
        creationDate = new Date();
        this.flightIdentifier = flightIdentifier;
        this.timeout = timeout;
    }

    public String getAirlineID(){
        return flightIdentifier.split(" ")[0];
    }

    public String getFlightNumber(){
        return flightIdentifier.split(" ")[1];
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getFlightIdentifier() {
        return flightIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StatusDependency that = (StatusDependency) o;

        if(! flightIdentifier.equals(that.flightIdentifier)) return false;
        Integer max = Math.min(timeout,that.timeout) * 1000 * 60;
        return Math.abs(creationDate.getTime() - that.getCreationDate().getTime()) <= max;
    }

    @Override
    public String toString(){
        return getDependencyType() + " " + flightIdentifier ;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + flightIdentifier.hashCode();
        return result;
    }

    private Date getDateAfterXMinutes(Date date, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
