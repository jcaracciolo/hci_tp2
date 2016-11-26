package ar.edu.itba.dreamtrip.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ar.edu.itba.dreamtrip.Map.MapsActivity;
import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.airlineInfo.AirlineInfo;
import ar.edu.itba.dreamtrip.airportInfo.AirportInfo;
import ar.edu.itba.dreamtrip.cityInfo.CityInfo;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.tasks.PopulateSearchResultsTask;
import ar.edu.itba.dreamtrip.countryInfo.CountryInfo;
import ar.edu.itba.dreamtrip.flightInfo.FlightInfo;

public class SearchFragment extends Fragment {
    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";
    final static String OPEN_QR = "open_qr_in_search";

    private EditText searchBox;
    private ListView results;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_search, container, false);

        boolean openQR = getActivity().getIntent().getBooleanExtra(OPEN_QR, false);

        if (openQR) {
            openQR();
            getActivity().getIntent().putExtra(OPEN_QR, false);
        }

        v.findViewById(R.id.qr_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQR();
            }
        });

        v.findViewById(R.id.map_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Unknown QR error.", Toast.LENGTH_SHORT).show();
            } else {
                ((EditText)getActivity().findViewById(R.id.searchBox)).setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openQR() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchBox = (EditText) getActivity().findViewById(R.id.searchBox);
        results = (ListView)getActivity().findViewById(R.id.resultList);
        final DataHolder dataholder = DataHolder.getInstance(getContext());

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                View otherSearches = getActivity().findViewById(R.id.other_search_types);
                results.setAdapter(null);
                dataholder.waitForIt(new PopulateSearchResultsTask(getActivity(), results, otherSearches, searchBox.getText().toString()));
            }

        });

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ResultElement elem = (ResultElement) results.getItemAtPosition(position);
                Intent intent = null;

                switch (elem.getDepType()) {
                    case AIRLINES:
                        intent = new Intent(getActivity(), AirlineInfo.class);
                        break;
                    case CITIES:
                        intent = new Intent(getActivity(), CityInfo.class);
                        break;
                    case COUNTRIES:
                        intent = new Intent(getActivity(), CountryInfo.class);
                        break;
                    case AIRPORTS:
                        intent = new Intent(getActivity(), AirportInfo.class);
                        break;
                    case FLIGHT_SEARCH:
                        intent = new Intent(getActivity(), FlightInfo.class);
                        intent.putExtra(RESULT_ID_KEY, ((Flight)elem.getElement()).getIdentifier());
                        startActivity(intent);
                        return;
                    default:
                        throw new RuntimeException("Unknown dependency type.\n");
                }

                intent.putExtra(RESULT_ID_KEY, elem.getElement().getID());
                startActivity(intent);
            }
        });

    }
}