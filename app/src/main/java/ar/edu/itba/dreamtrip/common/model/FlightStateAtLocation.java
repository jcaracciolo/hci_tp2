package ar.edu.itba.dreamtrip.common.model;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by juanfra on 22/11/16.
 */

public class FlightStateAtLocation {
    private String locationID;

    private String terminal;
    private Integer gate;

    private Date actualTime;
    private Date scheduledTime;
    private Date scheduledGateTime;
    private Date actualGateTime;

    private Date estimateRunwayTime;
    private Date actualRunwayTime;

    private Integer baggage;
    private Integer runwayDelay;
    private Integer gateDelay;

    public FlightStateAtLocation(String locationID, String terminal, Integer gate, Date actualTime,
                                 Date scheduledGateTime, Date actualGateTime, Date estimateRunwayTime,
                                 Date actualRunwayTime, Integer baggage, Integer runwayDelay,
                                 Integer gateDelay, Date scheduledTime) {
        this.locationID = locationID;
        this.terminal = terminal;
        this.gate = gate;
        this.actualTime = actualTime;
        this.scheduledGateTime = scheduledGateTime;
        this.actualGateTime = actualGateTime;
        this.estimateRunwayTime = estimateRunwayTime;
        this.actualRunwayTime = actualRunwayTime;
        this.baggage = baggage;
        this.runwayDelay = runwayDelay;
        this.gateDelay = gateDelay;
        this.scheduledTime = scheduledTime;
    }

    public String getScheduledHour(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(scheduledTime);
    }

    public String getScheduledDay(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(scheduledTime);
    }

    public String getLocationID() {
        return locationID;
    }

    public String getTerminal() {
        return terminal;
    }

    public Integer getGate() {
        return gate;
    }

    public Date getActualTime() {
        return actualTime;
    }

    public Date getScheduledGateTime() {
        return scheduledGateTime;
    }

    public Date getActualGateTime() {
        return actualGateTime;
    }

    public Date getEstimateRunwayTime() {
        return estimateRunwayTime;
    }

    public String getEstimateRunwayHour() {
        return estimateRunwayTime.toString().split(" ")[3].split(":")[0] + ":" + estimateRunwayTime.toString().split(" ")[3].split(":")[1];
    }

    public Date getActualRunwayTime() {
        return actualRunwayTime;
    }

    public Integer getBaggage() {
        return baggage;
    }

    public Integer getRunwayDelay() {
        return runwayDelay;
    }

    public Integer getGateDelay() {
        return gateDelay;
    }
}
