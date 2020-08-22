package edu.uci.ics.team108;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BikeListViewAdapter extends ArrayAdapter<Bike> {
    private ArrayList<Bike> bikes;

    public BikeListViewAdapter(ArrayList<Bike> bikes, Context context) {
        super(context, R.layout.row, bikes);
        this.bikes = bikes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Bike bike = bikes.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subtitle);

        titleView.setText(bike.getName());
        subtitleView.setText(bike.getYear() + " - "
                             + bike.category + " - "
                             + bike.brand_name + " - $"
                             + bike.list_price + " - "
                             + bike.rating + "/10"
                             + " first3store:" + bike.s);// need to cast the year to a string to set the label

        return view;
    }
}