package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;

public class TrackedFlightCardView{

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, TrackedFlightViewModel flight) {

        View cardView=inflater.inflate(R.layout.fragment_tracked_flight_card, container, false);
        ((TextView)cardView.findViewById(R.id.flight_number)).setText(flight.getIdentifier());
        ((TextView)cardView.findViewById(R.id.origin_id)).setText(flight.originID);
        ((TextView)cardView.findViewById(R.id.destination_id)).setText(flight.destinationID);
        ((TextView)cardView.findViewById(R.id.origin_time)).setText(flight.departureHour);
        ((TextView)cardView.findViewById(R.id.destination_time)).setText(flight.arrivalHour);
        ((TextView)cardView.findViewById(R.id.departure_date)).setText(flight.date);

        if(context!=null) {
            Integer color = context.getResources().getColor(R.color.status_green);
            switch (flight.status) {
                case SCHEDULED:
                    color = context.getResources().getColor(R.color.status_green);
                    break;
                case ACTIVE:
                    color = context.getResources().getColor(R.color.status_green);
                    break;
                case DELAYED:
                    color = context.getResources().getColor(R.color.status_orange);
                    break;
                case LANDED:
                    color = context.getResources().getColor(R.color.status_blue);
                    break;
                case CANCELLED:
                    color = context.getResources().getColor(R.color.status_red);
                    break;
            }
            TextView statusText = ((TextView) cardView.findViewById(R.id.status_text));
            statusText.setText(context.getString(flight.getStatusString()));
            statusText.setTextColor(color);
            cardView.findViewById(R.id.flight_status_bar).setBackgroundColor(color);
        }
        return cardView;

    }

}

