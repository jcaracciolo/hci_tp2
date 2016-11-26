package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class FlightDealsDependency extends Dependency {
    public String getOriginID() {
        return originID;
    }

    private String originID;
    private Date creationDate;

    public FlightDealsDependency(String originID, boolean loadImages, boolean lastMinute) {
        super(loadImages? (lastMinute? DependencyType.LAST_MINUTE_DEALS : DependencyType.DEALS)
                : (lastMinute? DependencyType.LAST_MINUTE_DEALS_DATA: DependencyType.DEALS_DATA));
        if(loadImages ){
            Dependency imageURLDep = new Dependency(lastMinute? DependencyType.LAST_MINUTE_DEALS_IMAGES_URLS
                                            : DependencyType.DEALS_IMAGES_URLS);
            Dependency imageDep = new Dependency(lastMinute? DependencyType.LAST_MINUTE_DEALS_IMAGES
                                            : DependencyType.DEALS_IMAGES);
            Dependency dataDep = new FlightDealsDependency(originID,false,lastMinute);

            imageURLDep.addDependency(dataDep);

                imageDep.addDependency(dataDep);
                imageDep.addDependency(imageURLDep);
                addDependency(imageDep);
            addDependency(dataDep);
        }
        creationDate = new Date();
        this.originID = originID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlightDealsDependency that = (FlightDealsDependency) o;
        if(! Objects.equals(originID, that.originID)) return false;
        Date currentDate = new Date();
        return currentDate.after(getDateAfterXMinutes(creationDate,12));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), originID);
    }


    private Date getDateAfterXMinutes(Date date, int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }
}
