package ar.edu.itba.dreamtrip.TrackedFlights;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ar.edu.itba.dreamtrip.R;

public class TrackedFlightCardView{

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, TrackedFlightViewModel flight) {

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

        swipeLayout.setSwipeEnabled(false);

//        swipeLayout.findViewById(R.id.swipe_button).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        System.out.println("TOUCHED");
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        return true;
//                }
//                return false;
//            }
//        });

        swipeLayout.findViewById(R.id.swipe_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                    swipeLayout.open();
                else
                    swipeLayout.close();

                System.out.println("TOUCHED");
            }
        });

        swipeLayout.setSwipeEnabled(false);

        View cardView=swipeLayout.findViewById(R.id.flight_card);
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
        return swipeLayout;

    }

}

