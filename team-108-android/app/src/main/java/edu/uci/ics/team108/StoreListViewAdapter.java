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

public class StoreListViewAdapter extends ArrayAdapter<Store> {
    private ArrayList<Store> stores;

    public StoreListViewAdapter(ArrayList<Store> stores, Context context) {
        super(context, R.layout.row, stores);
        this.stores = stores;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Store store = stores.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subtitle);

        titleView.setText(store.name);
//        subtitleView.setText(bike.getYear() + "");// need to cast the year to a string to set the label

        return view;
    }
}
