package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;

public class TrackedFlightCardView{
    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    public static SwipeLayout getView(final Context context, LayoutInflater inflater, ViewGroup container, TrackedFlightViewModel flight) {

        final SwipeLayout swipeLayout =  (SwipeLayout)inflater.inflate(R.layout.fragment_swipeable_tracked_flight_card, container, false);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));
        swipeLayout.setSwipeEnabled(false);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                swipeLayout.setSwipeEnabled(false);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(context.getString(R.string.multiselectionIntent));
                context.sendBroadcast(intent);
                return true;
            }
        });

        View cardView=swipeLayout.findViewById(R.id.flight_card);
        ((TextView)cardView.findViewById(R.id.flight_number)).setText(flight.getIdentifier());
        ((TextView)cardView.findViewById(R.id.origin_id)).setText(flight.originID);
        ((TextView)cardView.findViewById(R.id.destination_id)).setText(flight.destinationID);
        ((TextView)cardView.findViewById(R.id.origin_time)).setText(flight.departureHour);
        ((TextView)cardView.findViewById(R.id.destination_time)).setText(flight.arrivalHour);
        ((TextView)cardView.findViewById(R.id.departure_date)).setText(flight.date);

        if(context!=null) {
            Integer color = TrackedFlightCardView.getColor(context,flight.status);
            TextView statusText = ((TextView) cardView.findViewById(R.id.status_text));
            statusText.setText(context.getString(flight.getStatusString()));
            statusText.setTextColor(color);
            cardView.findViewById(R.id.flight_status_bar).setBackgroundColor(color);
        }
        return swipeLayout;

    }

    public static SwipeLayout fillView(final Context context, LayoutInflater inflater, final TrackedFlightViewModel flight, View view) {

        final SwipeLayout swipeLayout =  (SwipeLayout)view;
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));
        swipeLayout.setSwipeEnabled(false);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                    flight.selected=true;
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                flight.selected=false;
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                swipeLayout.open(SwipeLayout.DragEdge.Left);
                return true;
            }
        });

        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flight.selected)
                    swipeLayout.close();
                else{
                    String id = flight.getIdentifier();
                    Intent intent = new Intent(context, FlightInfo.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(RESULT_ID_KEY,id);
                    context.startActivity(intent);
                }
            }
        });

        swipeLayout.findViewById(R.id.selected_tracked_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getResources().getString(R.string.deleteFlightIntent));
                intent.putExtra(context.getResources().getString(R.string.RetrieveDeleteFlightIntent),flight.getIdentifier());
                context.sendBroadcast(intent);
            }
        });



        View cardView=swipeLayout.findViewById(R.id.flight_card);
        ((TextView)cardView.findViewById(R.id.flight_number)).setText(flight.getIdentifier());
        ((TextView)cardView.findViewById(R.id.origin_id)).setText(flight.originID);
        ((TextView)cardView.findViewById(R.id.destination_id)).setText(flight.destinationID);
        ((TextView)cardView.findViewById(R.id.origin_time)).setText(flight.departureHour);
        ((TextView)cardView.findViewById(R.id.destination_time)).setText(flight.arrivalHour);
        ((TextView)cardView.findViewById(R.id.departure_date)).setText(flight.date);

        if(context!=null) {
            Integer color = TrackedFlightCardView.getColor(context,flight.status);
            TextView statusText = ((TextView) cardView.findViewById(R.id.status_text));
            statusText.setText(context.getString(flight.getStatusString()));
            statusText.setTextColor(color);
            cardView.findViewById(R.id.flight_status_bar).setBackgroundColor(color);
        }

        String isLandTablet = context.getResources().getString(R.string.isTablet);
        if (isLandTablet.equals("true")) {
            //TODO fill with more DATA
            String s = flight.terminal == null ? " - " : flight.terminal;
            ((TextView)view.findViewById(R.id.terminal)).setText(context.getResources().getString(R.string.terminal_txt) + " " + s);

            s = flight.gate == null ? " - " : flight.gate.toString();
            ((TextView)view.findViewById(R.id.gate)).setText(context.getResources().getString(R.string.gate_txt) + " " + s);

            s = flight.luggage == null ? " - " : flight.luggage.toString();
            ((TextView)view.findViewById(R.id.luggage)).setText(context.getResources().getString(R.string.luggage_txt) + " " + s);
        }

        return swipeLayout;

    }

    public static Integer getColor(Context context,FlightStatus status){
            Integer color = context.getResources().getColor(R.color.status_green);
            switch (status) {
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
        return color;
    }

}


