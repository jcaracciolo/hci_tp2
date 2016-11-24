package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import ar.edu.itba.dreamtrip.R;

public class TrackedLegCardView {

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, TrackedLegViewModel leg) {

        final SwipeLayout swipeLayout =  (SwipeLayout)inflater.inflate(R.layout.fragment_swipeable_tracked_destinations_card, container, false);

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

        View cardView=swipeLayout.findViewById(R.id.tracked_destination_card);
        ((TextView)cardView.findViewById(R.id.tracked_destination_desc)).setText(leg.destinationDescription);
        ((ImageButton)cardView.findViewById(R.id.destination_img)).setImageBitmap(leg.image);
        ((TextView)cardView.findViewById(R.id.from_destination)).setText(leg.originDescription);
        if(leg.isLastMinute){
            ((TextView)cardView.findViewById(R.id.offer_found)).setText(R.string.offer_found);
            ((TextView)cardView.findViewById(R.id.price_tracked_destination)).setText(leg.price.toString());
        }else{
            ((TextView)cardView.findViewById(R.id.offer_found)).setText(R.string.no_offer_found);

        }

        return swipeLayout;

    }

}

