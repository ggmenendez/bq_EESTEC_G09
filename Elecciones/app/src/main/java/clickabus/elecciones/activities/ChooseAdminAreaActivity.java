package clickabus.elecciones.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.clickabus.elecciones.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import clickabus.elecciones.connection.NetFunctions;
import clickabus.elecciones.database.DataBaseContract;
import clickabus.elecciones.database.DataBaseHelper;
import clickabus.elecciones.model.AdministrativeArea;
import clickabus.elecciones.model.Party;
import clickabus.elecciones.model.UserAnswer;

public class ChooseAdminAreaActivity extends ActionBarActivity {

    private ArrayList<AdministrativeArea> administrativeAreas = new ArrayList<>();
    private Location loc;
    private Spinner spAdminAreas;
    private ArrayAdapter<String> arrayAdapter;

    private static final String ADMIN_AREA = "admin_area";
    private static final String SOURCE = "source";

    private int sourceActivity = 0;

    private static final int TEST_ACTIVITY = 1;
    private static final int STATISTICS_ACTIVITY = 2;

    private LinearLayout relativeLayout;

    private LocationManager locManager;

    private LocationListener locListener;

    private Geolocate geolocated;


    int publicidad = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_admin_area);

        relativeLayout = (LinearLayout)findViewById(R.id.resultMain);
        Drawable background = relativeLayout.getBackground();
        background.setAlpha(1000);
        // Get the Bundle Object
        Bundle bundleObject = getIntent().getExtras();

        // Get int Bundle
        sourceActivity = bundleObject.getInt(SOURCE);

        startLocation();

        spAdminAreas = (Spinner) findViewById(R.id.spnAdminAreas);
        Button btnGeolocation = (Button)findViewById(R.id.btnGeolocation);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        administrativeAreas = dataBaseHelper.getAdminAreas();

        String[] spinnerData = new String[administrativeAreas.size()];
        String[] shortNames = new String[administrativeAreas.size()];

        int i = 0;
        for(AdministrativeArea a : administrativeAreas){
            spinnerData[i] = a.getName();
            shortNames[i] = a.getShortName();
            i++;
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        spAdminAreas.setAdapter(arrayAdapter);
        spAdminAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_back){
            finish();
        }
        if(id == R.id.action_next){
            showResult(this.findViewById(R.id.action_next));
        }
        return super.onOptionsItemSelected(item);
    }


    private void startLocation()
    {
        //Obtenemos una referencia al LocationManager
        locManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Si el GPS no está habilitado
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                loc =
                        locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //Nos registramos para recibir actualizaciones de la posición
                locListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        loc = location;
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                };

                locManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 30000, 0, locListener);
            }else{

            }
        }else {
            //Obtenemos la última posición conocida
            loc =
                    locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Nos registramos para recibir actualizaciones de la posición
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    loc = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

            };

            locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 30000, 0, locListener);
        }



    }

    public void geolocate(View w){

        //Si el GPS no está habilitado
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                loc =
                        locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }else{
                Toast.makeText(getApplicationContext(), "Por favor, active su GPS.", Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            //Obtenemos la última posición conocida
            loc =
                    locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        geolocated = new Geolocate();
        geolocated.execute();
    }

    public void showResult(View w){
        Intent i = null;
        switch (sourceActivity){
            case TEST_ACTIVITY:
                i = new Intent(getApplicationContext(), TestActivity.class);
                publicidad = 1;
                break;
            case STATISTICS_ACTIVITY:
                i = new Intent(getApplicationContext(), StatisticsActivity.class);
                publicidad = 1;
                break;
            default:
                break;
        }
        if(geolocated!=null){
        if(geolocated.getStatus()== AsyncTask.Status.PENDING || geolocated.getStatus()== AsyncTask.Status.RUNNING){
            geolocated.cancel(true);
        }}

        String adminAreaSelected = spAdminAreas.getSelectedItem().toString();
        AdministrativeArea adminAreaChoosed = getAdminAreaByName(adminAreaSelected);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ADMIN_AREA, adminAreaChoosed);
        if(locListener!=null && locManager!=null) locManager.removeUpdates(locListener);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    private AdministrativeArea getAdminAreaByName(String name){
        for(AdministrativeArea a : administrativeAreas){
            if(a.getName().equalsIgnoreCase(name)){
                return a;
            }
        }
        return null;
    }
    /**
     * Subclase para geolocalizar en segundo plano.
     */
    private class Geolocate extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog pDialog;
        private String adminAreaShortName;

        protected Boolean doInBackground(Void... voids) {

            NetFunctions functions = new NetFunctions();

            while(loc==null){
                if(this.isCancelled()) return false;
                // Do nothing.
            }

            if(locListener!=null && locManager!=null) locManager.removeUpdates(locListener);
            adminAreaShortName = functions.getAdministrativeArea(String.valueOf(loc.getLatitude()),
                    String.valueOf(loc.getLongitude()));

            return adminAreaShortName != null && adminAreaShortName != "";

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
            if(result){
                Toast.makeText(getApplicationContext(), "Geolocalización completa.", Toast.LENGTH_LONG).show();
                String name = "";
                for(AdministrativeArea a: administrativeAreas){
                    if(adminAreaShortName.equalsIgnoreCase(a.getShortName())){
                        name = a.getName();
                        break;
                    }
                }

                spAdminAreas.setSelection(arrayAdapter.getPosition(name));
            }
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChooseAdminAreaActivity.this);
            pDialog.setMessage("Geolocalizando...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }
}
