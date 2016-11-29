package ar.edu.itba.dreamtrip.flightInfo;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.API.dependencies.StatusDependency;
import ar.edu.itba.dreamtrip.common.model.FlightState;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.common.tasks.ImageLoadTask;


public class LoadFlightStatusTask extends AsyncTaskInformed<Object,Void,FlightState>{

    private Context context;
    TextView text;
    TextView destFrom;
    String flightID;
    FlightStateView flightStateView;

    public LoadFlightStatusTask(Context context, String flightID, FlightStateView flightStateView) {
        this.context = context;
        this.flightID = flightID;
        this.flightStateView = flightStateView;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new StatusDependency(flightID, 5));
        return dependencies;
    }

    @Override
    protected FlightState doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<FlightState> arr = dataHolder.getFlightStates();
        for (FlightState fs: arr) {
            if (fs.getIdentifier().equals(flightID)) {
                return fs;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(FlightState fs) {
//        flightStateView.getAirlineLogo().setImageBitmap(fs.getOrigin().get);
        flightStateView.getFlightID().setText(fs.getIdentifier());

        flightStateView.getOriginID().setText(fs.getOrigin().getLocationID());
//        flightStateView.getOriginID().setText(fs.getOrigin().get);
        flightStateView.getDepartureTime().setText(fs.getOrigin().getScheduledHour());

        flightStateView.getDestinationID().setText(fs.getDestination().getLocationID());
//        flightStateView.getOriginID().setText(fs.getDestination().get);
        flightStateView.getArrivalTime().setText(fs.getDestination().getScheduledHour());

        String flightStatus;
        int color = R.color.status_green;
        switch (fs.getStatus()) {
            case SCHEDULED:
                flightStatus = context.getString(R.string.scheduled_flight);
                color = R.color.status_green;
                break;
            case ACTIVE:
                flightStatus = context.getString(R.string.active_flight);
                color = R.color.status_green;
                break;
            case DELAYED:
                flightStatus = context.getString(R.string.delayed_flight);
                color = R.color.status_yellow;
                break;
            case LANDED:
                flightStatus = context.getString(R.string.landed_flight);
                color= R.color.status_blue;
                break;
            case CANCELLED:
                flightStatus = context.getString(R.string.cancelled_flight);
                color = R.color.status_red;
                break;
            default:
                flightStatus = "unknown";
                break;
        }
        flightStateView.getStatusBar().setBackgroundColor(context.getResources().getColor(color));
        flightStateView.getStatus().setText(context.getString(R.string.flight_status_txt) + " " + flightStatus.toLowerCase());
        flightStateView.getEstimatedDepTime().setText(context.getString(R.string.estimated_dep_time_txt) + " " + fs.getOrigin().getEstimateRunwayHour());
        flightStateView.getEstimatedArrTime().setText(context.getString(R.string.estimated_arr_time_txt) + " " + fs.getDestination().getEstimateRunwayHour());

        String terminal =fs.getDestination().getTerminal() == null ? " -" : fs.getDestination().getTerminal();
        flightStateView.getTerminal().setText(context.getString(R.string.terminal_txt) + " " + terminal);

        String gate = fs.getDestination().getGate() == null ? " -" : fs.getDestination().getGate().toString();
        flightStateView.getGate().setText(context.getString(R.string.gate_txt) + " " + gate);

        String luggages = fs.getDestination().getBaggage() == null ? " -" : fs.getDestination().getBaggage().toString();
        flightStateView.getLuggage().setText(context.getString(R.string.luggage_txt) + " " + luggages);
//        new ImageLoadTask(fs.getOrigin()..getLogoUrl(), flightStateView.getAirlineLogo()).execute();
    }

}
