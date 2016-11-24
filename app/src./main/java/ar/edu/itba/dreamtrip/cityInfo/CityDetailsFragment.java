package ar.edu.itba.dreamtrip.cityInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.itba.dreamtrip.Deals.DealsFromActivity;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;

public class CityDetailsFragment extends Fragment {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    private OnFragmentInteractionListener mListener;

    public CityDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_destination_details, container, false);

        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra(RESULT_ID_KEY);

        ImageView img = (ImageView) v.findViewById(R.id.destination_icon);
        img.setImageResource(R.drawable.ic_city);

        DataHolder dataHolder = DataHolder.getInstance(getActivity());
        TextView text = (TextView) v.findViewById(R.id.destination_name);
        TextView destFrom = (TextView) v.findViewById(R.id.destinations_from_button);
        dataHolder.waitForIt(new LoadCityInfoTask(getContext(), text, destFrom, id));

        v.findViewById(R.id.destinations_from_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DealsFromActivity.class);
                //intent.putExtra(INTENT_TO_DEALS_FROM, )
                startActivity(intent);
            }

        });

        // Inflate the layout for this fragment
        return v;
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
