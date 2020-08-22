package edu.uci.ics.team108;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ListViewActivity extends Activity {

    JSONObject bikeList;
    final ArrayList<Bike> bikes = new ArrayList<>();
    int currentPageNumber = 1;
    String key = "";


//    void onMount(int page) {
//        this.bikes.clear();
//        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
//        ListViewActivity x = this;
//        //request type is POST
//        final StringRequest loginRequest = new StringRequest(Request.Method.GET, Global.URL + "search?sorted=-rating;+product_name&page=1&limit=20&offset="+((page-1)*20), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //TODO should parse the json response to redirect to appropriate functions.
//                try{
//                    bikeList = new JSONObject(response);
//                } catch (Exception ignore) {
//
//                }
//                Log.d("response:", response);
//
//
//                //        while (bikeList == null) {}
//                //        bikes.add(new Bike("The Terminal", (short) 2004));
//                //        bikes.add(new Bike("The Final Season", (short) 2007));
//                Iterator<String> keys = bikeList.keys();
//                while (keys.hasNext()) {
//                    String i = keys.next();
//                    try {
//                        JSONObject thisone = bikeList.getJSONObject(i);
//                        String product_id = thisone.getString("product_id");
//                        String product_name = thisone.getString("product_name");
//                        String model_year = thisone.getString("model_year");
//                        String category_name = thisone.getString("category_name");
//                        String list_price = thisone.getString("list_price");
//                        String brand_name = thisone.getString("brand_name");
//                        String rating = thisone.getString("rating");
//                        JSONObject first3stores = thisone.getJSONObject("first3stores");
//                        bikes.add(new Bike(product_id, product_name, model_year, category_name, brand_name, list_price, rating));
//                    } catch (Exception ignore) {
//                        System.out.println(ignore);
//                    }
//                }
//
//                BikeListViewAdapter adapter = new BikeListViewAdapter(bikes, x);
//
//                ListView listView = findViewById(R.id.list);
//                listView.setAdapter(adapter);
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Bike bike = bikes.get(position);
//                        String message = String.format("Clicked on position: %d, name: %s, %s", position, bike.getName(), bike.getYear());
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(x, singlebike.class);
//                        intent.putExtra("product_id", bike.id);
//                        intent.putExtra("product_name", bike.name);
//                        intent.putExtra("model_year", bike.year);
//                        intent.putExtra("category", bike.category);
//                        intent.putExtra("brand_name", bike.brand_name);
//                        intent.putExtra("rating", bike.rating);
//                        intent.putExtra("list_price", bike.list_price);
//                        startActivity(intent);
//                    }
//                });
//
//
//            }
//        },
//                new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("login.error", error.toString());
//                    }
//                }) {
//        };
//        // !important: queue.add is where the login request is actually sent
//        queue.add(loginRequest);
//    }

    void onSearch(String key, int page) {
        this.bikes.clear();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        ListViewActivity x = this;
        //request type is POST
        final StringRequest loginRequest = new StringRequest(Request.Method.GET, Global.URL + "search?product_name=%25"+key+"%25&sorted=-rating;+product_name&page=1&limit=20&offset="+((page-1)*20), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.
                try{
                    bikeList = new JSONObject(response);
                } catch (Exception ignore) {

                }
                Log.d("response:", response);


                //        while (bikeList == null) {}
                //        bikes.add(new Bike("The Terminal", (short) 2004));
                //        bikes.add(new Bike("The Final Season", (short) 2007));
                Iterator<String> keys = bikeList.keys();
                while (keys.hasNext()) {
                    String i = keys.next();
                    try {
                        JSONObject thisone = bikeList.getJSONObject(i);
                        String product_id = thisone.getString("product_id");
                        String product_name = thisone.getString("product_name");
                        String model_year = thisone.getString("model_year");
                        String category_name = thisone.getString("category_name");
                        String list_price = thisone.getString("list_price");
                        String brand_name = thisone.getString("brand_name");
                        String rating = thisone.getString("rating");
                        JSONObject first3stores = thisone.getJSONObject("first3stores");
                        JSONObject store1 = null;
                        JSONObject store2 = null;
                        JSONObject store3 = null;
                        try {
                            store1= first3stores.getJSONObject("0");
                            store2= first3stores.getJSONObject("1");
                            store3= first3stores.getJSONObject("2");
                        } catch (Exception ignore) {}
                        String s1 = (store1 != null) ? store1.getString("store_name") : "";
                        String s2 = (store2 != null) ? store2.getString("store_name") : "";
                        String s3 = (store3 != null) ? store3.getString("store_name") : "";
                        String s = s1 + ", " + s2 + ", " + s3;
                        bikes.add(new Bike(product_id, product_name, model_year, category_name, brand_name, list_price, rating, s));
                    } catch (Exception ignore) {
                        System.out.println(ignore);
                    }
                }

                BikeListViewAdapter adapter = new BikeListViewAdapter(bikes, x);

                ListView listView = findViewById(R.id.list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bike bike = bikes.get(position);
                        String message = String.format("Clicked on position: %d, name: %s, %s", position, bike.getName(), bike.getYear());
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(x, singlebike.class);
                        intent.putExtra("product_id", bike.id);
                        intent.putExtra("product_name", bike.name);
                        intent.putExtra("model_year", bike.year);
                        intent.putExtra("category", bike.category);
                        intent.putExtra("brand_name", bike.brand_name);
                        intent.putExtra("rating", bike.rating);
                        intent.putExtra("list_price", bike.list_price);
                        startActivity(intent);
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
        System.out.println("start ListViewActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("prev");
                if (currentPageNumber != 1) {
                    currentPageNumber -= 1;
                }
                ((TextView) (findViewById(R.id.pagenum))).setText("" + currentPageNumber);
                onSearch(key, currentPageNumber);
            }
        });

        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("next");
                currentPageNumber += 1;
                ((TextView) (findViewById(R.id.pagenum))).setText("" + currentPageNumber);
                onSearch(key, currentPageNumber);
            }
        });

        ((Button) findViewById(R.id.searchBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("search");
                key = (((TextView) (findViewById(R.id.searchBar))).getText()).toString();
                onSearch(key, currentPageNumber);
            }
        });

        //this should be retrieved from the database and the backend server
        onSearch("", 1);

    }
}