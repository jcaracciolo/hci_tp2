package ar.edu.itba.dreamtrip.countryInfo;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CountryDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountryDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountryDetailsFragment extends Fragment {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";
    public final static String INTENT_TO_DEALS_FROM = "ar.itba.edu.dreamtrip.deals_id";
    public final static String INTENT_TO_DEALS_DESCR = "ar.itba.edu.dreamtrip.deals_descr";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CountryDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CountryDetailsFragment newInstance(String param1, String param2) {
        CountryDetailsFragment fragment = new CountryDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_destination_details, container, false);

        Intent intent = getActivity().getIntent();
        final String id = intent.getStringExtra(RESULT_ID_KEY);

        ImageView img = (ImageView) v.findViewById(R.id.destination_icon);
        img.setImageResource(R.drawable.ic_place);

        final DataHolder dataHolder = DataHolder.getInstance(getActivity());
        TextView text = (TextView) v.findViewById(R.id.destination_name);
        TextView destFrom = (TextView) v.findViewById(R.id.destinations_from_button);
        dataHolder.waitForIt(new LoadCountryInfoTask(getContext(), text, destFrom, id));

        v.findViewById(R.id.destinations_from_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DealsFromActivity.class);
                intent.putExtra(INTENT_TO_DEALS_FROM,id);
                intent.putExtra(INTENT_TO_DEALS_DESCR, dataHolder.getCountryById(id).getName());
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
