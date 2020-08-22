package edu.uci.ics.team108;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class singlebike extends Activity {

    final ArrayList<Store> stores = new ArrayList<>();

    void onMount(String bikeid) {
        System.out.println("start singlebike.onMount");
        this.stores.clear();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        singlebike x = this;
        //request type is POST
        final StringRequest loginRequest = new StringRequest(Request.Method.GET, Global.URL + "singlebikestores?bikeid="+bikeid+"&offset=0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.

                JSONObject storelist = new JSONObject();
                try{
                    storelist = new JSONObject(response);
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
                Log.d("response:", response);

                //        while (bikeList == null) {}
                //        bikes.add(new Bike("The Terminal", (short) 2004));
                //        bikes.add(new Bike("The Final Season", (short) 2007));
                Iterator<String> keys = storelist.keys();
                while (keys.hasNext()) {
                    String i = keys.next();
                    try {
                        JSONObject thisone = storelist.getJSONObject(i);
                        String store_id = thisone.getString("store_id");
                        String store_name = thisone.getString("store_name");
                        stores.add(new Store(store_name));
                    } catch (Exception ignore) {
                        System.out.println(ignore);
                    }
                }

                StoreListViewAdapter adapter = new StoreListViewAdapter(stores, x);

                ListView listView = findViewById(R.id.storelist);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Store store = stores.get(position);
                        String message = String.format("Clicked on position: %d, name: %s", position, store.name);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }) {
        };
        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlebike);

        Intent intent = getIntent();
        ((TextView)findViewById(R.id.product_name)).setText(intent.getStringExtra("product_name"));
        ((TextView)findViewById(R.id.model_year)).setText(intent.getStringExtra("model_year"));
        ((TextView)findViewById(R.id.category_name)).setText(intent.getStringExtra("category"));
        ((TextView)findViewById(R.id.list_price)).setText(intent.getStringExtra("list_price"));
        ((TextView)findViewById(R.id.rating)).setText(intent.getStringExtra("rating"));
        ((TextView)findViewById(R.id.brand_name)).setText(intent.getStringExtra("brand_name"));

        onMount(intent.getStringExtra("product_id"));
    }
}
