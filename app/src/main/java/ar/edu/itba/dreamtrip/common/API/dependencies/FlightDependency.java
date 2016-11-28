package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ar.edu.itba.dreamtrip.common.model.StatusSearch;

public class FlightDependency extends Dependency {

    private String  originID, destinationID;
    private Date departureDate;
    private Boolean requiresStatus = false;
    private StatusSearch statusSearch;
    private String departureDateToSearch;
    private Integer days;

    public FlightDependency(String originID, String destinationID, Date departureDate) {
        super(DependencyType.FLIGHTS);
        days =0;
        this.originID = originID;
        this.destinationID = destinationID;
        setDepartureDate(departureDate);
    }

    private void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        departureDateToSearch = format.format(departureDate);
    }

    public FlightDependency(String originID, String destinationID, Date departureDate, Integer days) {
        super(DependencyType.FLIGHTS);
        this.days =days;
        this.originID = originID;
        this.destinationID = destinationID;
        setDepartureDate(departureDate);
    }

    public FlightDependency(StatusSearch statusSearch){
        super(DependencyType.FLIGHTS);
        days = 7;
        addDependency(new StatusSearchDependency(statusSearch));
        this.statusSearch = statusSearch;
        requiresStatus = true;
    }


    public Boolean isValid(){
        if(requiresStatus) return statusSearch.getValid();
        else return true;
    }


    public String getOriginID() {
        if(requiresStatus) return statusSearch.getOriginAirportID();
        return originID;
    }

    public String getDestinationID() {
        if(requiresStatus) return statusSearch.getDestinationAirportID();
        return destinationID;
    }

    public Date getDepartureDate() {
        if(requiresStatus) return getDateAfterXDays(statusSearch.getDeparture(),days);

        return getDateAfterXDays(departureDate,days);
    }
    @Override
    public String toString(){
        if(requiresStatus) return getDependencyType() + " " + days + " " + statusSearch;
        else return getDependencyType() + " " + originID + " to " + destinationID + " on " + getDepartureDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FlightDependency that = (FlightDependency) o;

        if(requiresStatus != that.requiresStatus) return false;

        if(requiresStatus){
            if(! statusSearch.equals(that.statusSearch)) return false;
            if(! days.equals(that.days)) return false;
            return true;
        } else {
            if(! originID.equals(that.originID)) return false;
            if(! days.equals(that.days)) return false;
            if(! destinationID.equals(that.destinationID)) return false;
            if(! departureDateToSearch.equals(that.departureDateToSearch)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        Integer result=0;
        if(requiresStatus) {
            result = 31 * result + statusSearch.hashCode();
            result = 31 * result + days.hashCode();
        } else {
            result = 31 * result + originID.hashCode();
            result = 31 * result + days.hashCode();
            result = 31 * result + destinationID.hashCode();
            result = 31 * result + departureDateToSearch.hashCode();
        }
        return result;
    }

    private Date getDateAfterXDays(Date date, int days){
        if(date == null) throw new RuntimeException("Null date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
