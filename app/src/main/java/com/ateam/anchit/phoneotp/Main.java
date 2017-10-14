package com.ateam.anchit.phoneotp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alok Kumar on 05-Aug-17.
 */

public class Main extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ListItem1> list;
    ProgressDialog progressDialog;
    RequestQueue requestQueue1;
    String stateRe;
    int j;
    ListItem1 listItem;
    ArrayList<String> arrayList;
    SearchView searchView;
    TextView searchPlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        Intent intent = getIntent();
      /*  Log.e("Received Data", stateRe);*/
        Bundle bundle = intent.getExtras();
        stateRe = bundle.getString("value");
        setTitle(stateRe);
        ////////////////////////////////////////////

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();
        jsonParsing("");
    }
        ///
      public void jsonParsing(final String find){
        requestQueue1 = Volley.newRequestQueue(this);
        JsonObjectRequest request1 = new JsonObjectRequest(getString(R.string.url), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray array2 = response.getJSONArray("data");
                    String str[] = new String[array2.length()];
                    String[] state = new String[array2.length()];

                    j = 0;
                  /*  arrayList = new ArrayList<String>();
                    //array list clear for new information strt from 0
                    arrayList.clear();*/
                    list = new ArrayList<>();
                    list.clear();

                    for (int i = 0; i < array2.length(); i++) {
                        JSONArray array1 = array2.getJSONArray(i);

                        // if (array1.getString(1).equals(stateRe)) {
                     

                        if (array1.getString(1).contentEquals(stateRe) &&
                                (array1.getString(3).toLowerCase().startsWith(find.toLowerCase()))) {
                           /* str[i] = array1.getString(1) + "\n" +
                                    array1.getString(2) + "\n" +
                                    array1.getString(3) + "\n" +
                                    array1.getString(4) + "\n" +
                                    array1.getString(5) + "\n" +
                                    array1.getString(6) + "\n" +
                                    array1.getString(7) + "\n" +
                                    array1.getString(11) + "\n";*/
                           /* arrayList.add(j,str[i]);*/
                            j++;
                            listItem = new ListItem1(array1.getString(1),array1.getString(2),array1.getString(3),
                                    array1.getString(4),array1.getString(5),array1.getString(6),array1.getString(7),array1.getString(11));
                            list.add(listItem);

                          Log.e("State Choosed", list.toString());
                            //   tv.setText(tv.getText()+ "\n" + str[i] + "\n");
                        }
                    }
                   /* for (int i = 0; i < j; i++) {
                        *//*str1[i] = str[i];*//*
                       listItem = new ListItem1(arrayList.get(i).toString());
                    list.add(listItem);
                       *//*list.add(listItem);*//*
                    }*/

                    recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    adapter =  new MyAdapter1(getApplicationContext(),list);
                    recyclerView.setAdapter(adapter);
                    Log.e("City.........", String.valueOf(j));
                    Log.e("VAlue of j:  ", String.valueOf(j));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue1.add(request1);
        /////////////////////////////////////////////
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //some operation
                    jsonParsing("");
                    return false;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //some operation
                }
            });
            searchPlate = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search City");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(this);
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        jsonParsing(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        jsonParsing(newText);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.setSubmitButtonEnabled(true);
            //findViewById(R.id.default_title).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
