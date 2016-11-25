package ar.edu.itba.dreamtrip.opinions;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.HtmlManipulator;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.main.ResultElement;
import java.net.URI;
/**
 * Created by Julian Benitez on 11/19/2016.
 */

public class ReviewsAdapter extends BaseAdapter {
    Context context;
    ArrayList<ReviewModel> data;
    private static LayoutInflater inflater = null;

    public ReviewsAdapter(Context context, ArrayList<ReviewModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.fragment_opinion_card, null);
        }

        TextView text = (TextView) vi.findViewById(R.id.opinion_text);
        String msg = HtmlManipulator.replaceHtmlEntities(URLDecoder.decode(data.get(position).comment));

        if (msg.equals("")) {
            msg = context.getResources().getString(R.string.no_comment);
            text.setTypeface(null, Typeface.ITALIC);
        }
        text.setText(msg);

        Integer recommendResource;
        Integer recommendColor;
        if (data.get(position).recomends) {
            recommendResource = R.drawable.ic_thumb_up;
            recommendColor = R.color.status_green;
        } else {
            recommendResource = R.drawable.ic_thumb_down;
            recommendColor = R.color.status_red;
        }

        ImageView thumb = (ImageView) vi.findViewById(R.id.recommend_icon);
        thumb.setImageResource(recommendResource);
        thumb.setColorFilter(context.getResources().getColor(recommendColor));

        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.comment_rating);
        ratingBar.setRating(data.get(position).rating / 2);

        return vi;
    }
}
