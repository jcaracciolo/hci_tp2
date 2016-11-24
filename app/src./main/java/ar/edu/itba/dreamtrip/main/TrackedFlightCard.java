package ar.edu.itba.dreamtrip.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.ImageType;
import ar.edu.itba.dreamtrip.main.viewModels.TrackedFlightViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackedFlightCard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackedFlightCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackedFlightCard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FLIGHT = "getFlight";

    private OnFragmentInteractionListener mListener;
    public TrackedFlightViewModel flight;
    public Context context;

    public TrackedFlightCard() {
        // Required empty public constructor
    }

    public static TrackedFlightCard newInstance(TrackedFlightViewModel flight) {
        TrackedFlightCard fragment = new TrackedFlightCard();
        Bundle args = new Bundle();
        args.putSerializable(FLIGHT, flight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        // Inflate the layout for this fragment
        if (getArguments() != null) {
            this.flight = (TrackedFlightViewModel) getArguments().getSerializable(FLIGHT);
        }
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        System.out.println("REACHED ATTACH");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

