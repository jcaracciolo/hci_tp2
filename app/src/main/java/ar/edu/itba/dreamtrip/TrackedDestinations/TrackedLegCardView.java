package ar.edu.itba.dreamtrip.TrackedDestinations;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;
import ar.edu.itba.dreamtrip.common.API.DataHolder;

public class TrackedLegCardView {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    public static View getView(final Context context, LayoutInflater inflater, ViewGroup container, final TrackedLegViewModel leg) {

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
                    leg.selected=true;
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                leg.selected=false;
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                    swipeLayout.open(SwipeLayout.DragEdge.Left);
                }else {
                    swipeLayout.close();
                }
                return true;
            }
        });

        View cardView=swipeLayout.findViewById(R.id.tracked_destination_card);
        ((TextView)cardView.findViewById(R.id.tracked_destination_desc)).setText(leg.destinationDescription);
        ((TextView)cardView.findViewById(R.id.from_destination)).setText(leg.originDescription);
        if(leg.isLastMinute){
            ((TextView)cardView.findViewById(R.id.offer_found)).setText(R.string.offer_found);
            ((TextView)cardView.findViewById(R.id.price_tracked_destination)).setText(leg.price.toString());
        }else{
            ((TextView)cardView.findViewById(R.id.offer_found)).setText(R.string.no_offer_found);

        }

        return swipeLayout;

    }

    public static void fillView(final Context context, final TrackedLegViewModel leg,View view) {

        final SwipeLayout swipeLayout =  (SwipeLayout)view;

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
                leg.selected=true;
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                leg.selected=false;
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                    swipeLayout.open(SwipeLayout.DragEdge.Left);
                }else {
                    swipeLayout.close();
                }
                return true;
            }
        });

        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leg.selected)
                    swipeLayout.close();
                else{
                    String id=leg.destinationID;
                    Intent intent = new Intent(context, CityInfo.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(RESULT_ID_KEY,id);
                    context.startActivity(intent);
                }
            }
        });

        swipeLayout.findViewById(R.id.selected_tracked_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getResources().getString(R.string.deleteDealIntent));
                intent.putExtra(context.getResources().getString(R.string.RetrieveDeleteDealIntent),leg.dealID);
                context.sendBroadcast(intent);
            }
        });

        View cardView=swipeLayout.findViewById(R.id.tracked_destination_card);
        ((TextView)cardView.findViewById(R.id.tracked_destination_desc)).setText(leg.destinationDescription.split(",")[0]);

        ImageView destImage=((ImageView)cardView.findViewById(R.id.destination_img));
        final DataHolder dataholder = DataHolder.getInstance(context);
        dataholder.loadImageIntoView(destImage,leg.image);

        ((TextView)cardView.findViewById(R.id.from_destination)).setText(leg.originDescription);
        if(leg.isLastMinute){
            ((TextView)cardView.findViewById(R.id.offer_found)).setText(R.string.offer_found);
            ((TextView)cardView.findViewById(R.id.price_tracked_destination)).setText(leg.price.toString());
        }else {
            ((TextView) cardView.findViewById(R.id.offer_found)).setText(R.string.no_offer_found);

        }

    }

}

