package com.ateam.anchit.phoneotp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.VoiceInteractor;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ListItem> list;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String str1[];
    Context context;
    FloatingActionButton f1, f2;
    TextView t;
    SearchView searchView;
    TextView searchPlate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        /////////////////////////////////
        requestPermission(Manifest.permission.INTERNET,
                101);
        boolean connect = isNetworkAvailable();

        if (!connect) {
            makeDialog(context, connect);
        } else {
            Log.e("Internet ", String.valueOf(connect));

            f1 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
            f1.setOnClickListener(this);
            progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
            progressDialog.setMessage("Loading data");
            progressDialog.show();
            jsonParsing("");

        }
    }

    public void jsonParsing(final String find) {
        ///

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest request = new JsonObjectRequest(getString(R.string.url), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                JSONArray array = response.getJSONArray("data");
                                final String str[] = new String[array.length()];
                                String[] state;

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONArray array1 = array.getJSONArray(i);
                                        str[i] = array1.getString(1); //+ ":" + array1.getString(17);
                                    }


                                state = getUniqueState(str);

                                int j = getStateNo(str);

                                Log.e("State", String.valueOf(j));

                                str1 = new String[j];
                                list = new ArrayList<>();

                                        for (int i = 0; i < j; i++) {
                                            str1[i] = state[i];

                                            if(state[i].toLowerCase().startsWith(find)){
                                                ListItem listItem = new ListItem(str1[i]);
                                                list.add(listItem);
                                            }
                                        }

                                recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                adapter = new MyAdapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });


                    ///
                    requestQueue.add(request);
                }
            });
            t.run();


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
            searchPlate.setHint("Search State");
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
            searchView.setIconified(true);hideSoftKeyboard(MainActivity.this);
            //indViewById(R.id.default_title).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();


        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    private void makeDialog(Context context, boolean connect) {
        if (!connect) {
            Toast.makeText(context, "!!!!Open Connection!!!!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Connection Not Available");
            alert.setCancelable(false);
            alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            alert.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            });
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String[] getUniqueState(String str[]) {
        String state[] = new String[40];
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                state[j] = str[i];
                j++;
                continue;
            }

            if (!Objects.equals(str[i], str[i - 1])) {
                state[j] = str[i];
                j++;
            }
        }
        return state;
    }

    private int getStateNo(String str[]) {
        String state[] = new String[40];
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                state[j] = str[i];
                j++;
                continue;
            }

            if (!Objects.equals(str[i], str[i - 1])) {
                state[j] = str[i];
                j++;
                continue;
            }
        }
        return j;
    }


    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this,
                permissionType);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permissionType}, requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to connect Internet - permission required", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                Toast.makeText(context, "Log Out", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.contact_us:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Contact us");
                alert.setMessage(getString(R.string.alertDialogCOntact));
                alert.setCancelable(true);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View view) {
  /*      if (view == floatingActionButton) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.alertDialogLocation));
            //TODO image for the icon and alertDialod same
            // / alertDialog.setIcon();
            alertDialog.setMessage("Choose the location");
            alertDialog.setCancelable(false);
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.setPositiveButton("My Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                            102);

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, false);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Log.e("Permisiion:", "Checked");
              *//*      locationManager.requestSingleUpdate(criteria, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    },));*//*
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            alertDialog.setMessage((int) location.getLatitude());
                            Toast.makeText(context, "LOcation:" + location.getLatitude() + "\n" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                            Log.e("LOcation", String.valueOf(location.getLatitude()));

                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
                    //locationManager.getLastKnownLocation(provider);
                    Log.e("Gps", "location manager");

                }
            });
            alertDialog.show();

        }*/

        if(view == f1){

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Contact us");
            alert.setMessage(getString(R.string.alertDialogCOntact)+"\n"+"\n"+getString(R.string.alertDialogCOntact1));
            alert.setCancelable(true);
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();

        }
    }


}

