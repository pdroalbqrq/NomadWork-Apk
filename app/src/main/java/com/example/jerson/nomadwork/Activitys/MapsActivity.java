package com.example.jerson.nomadwork.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.BasicClass.User;
import com.example.jerson.nomadwork.R;
import com.example.jerson.nomadwork.Util.PopupAdapter;
import com.example.jerson.nomadwork.Util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private ProgressDialog load;
    LatLng currentLocationLatLong;
    User localUser;
    String wifi;
    String energy;
    String noise;
    String price;
    String creator;
    int localId;
    FloatingActionButton fab, add;
    Local local;
    Marker mark;
    long MIN_TIME_FOR_UPDATES = 0;



    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }



    @Override
    public void onInfoWindowClick(Marker marker) {

        if(marker.getTag() == "100000000000"){
            Log.i("passou","passou");
        }else {
            fab.hide();
            add.hide(); 
            local.setLocalId( Integer.parseInt( String.valueOf( marker.getTag() ) ) );
            Bundle args = new Bundle();
            args.putParcelable( "localData", local);
            Log.i( "testeParcel", "localData-maps: " + local.getLocalId());
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            FragmentEdit fragment = new FragmentEdit();
            fragment.setArguments( args );
            transaction.add( R.id.frameFragment, fragment );
            transaction.addToBackStack( null );
            transaction.commit();
            Log.i("Testelocaltag", String.valueOf( marker.getTag() ) );}


    }

    @SuppressLint("StaticFieldLeak")
    private class GetJsonLocal extends AsyncTask<Void, Void, List<Local>> {
        @Override
        protected List<Local> doInBackground(Void... voids) {
            List<Local> loc = Utils.getInformacaoLocal();
            Log.i( "Teste 2 ", "Objeto Local criado na MapsActivity" );
            return loc;
        }

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<Local> location) {
            float hue = 360;  //(Range: 0 to 360)
            float bright = 0;
            for (int i = 0; i < location.size(); i++) {
                local = location.get( i );
                localId = local.getLocalId();
                wifi = local.getInternet();
                energy = local.getEnergy();
                noise = local.getNoise();
                price = local.getPrice();
                creator = local.getNameCreator();
//                Log.i( "Teste lista", local.getLocalName()
//                        + "|" + local.getLatitude()
//                        + "|" + local.getLongitude()
//                        + "|" + wifi
//                        + "|" + energy
//                        + "|" + noise
//                        + "|" + price
//                        + "|" + creator );
                LatLng loc = new LatLng( local.getLatitude(), local.getLongitude() );
                mark =  mMap.addMarker( new MarkerOptions()
                        .position( loc )
                        .title( local.getLocalName() )
                        .snippet( "Internet: " + wifi + "\n"
                                + "Energia: " + energy + "\n"
                                + "Barulho: " + noise + "\n"
                                + "Preço: " + price + "\n"
                                + "Criador: " + "Equipe Nomad Work" + "\n")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerlocais)));

                mark.setTag(localId);

            }

        }
    }


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission( perm )) {
                result.add( perm );
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission( permission ) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this );
        alertDialog.setTitle( "GPS desativado!" );
        alertDialog.setMessage( "Ativar GPS?" );
        alertDialog.setPositiveButton( "Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                startActivity( intent );
            }
        } );

        alertDialog.setNegativeButton( "Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        } );

        alertDialog.show();
    }


    private void startGettingLocations() {

        LocationManager lm = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        boolean isGPS = lm.isProviderEnabled( LocationManager.GPS_PROVIDER );
        boolean isNetwork = lm.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        this.MIN_TIME_FOR_UPDATES = 0;
        long MIN_TIME_BW_UPDATES = this.MIN_TIME_FOR_UPDATES;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add( ACCESS_FINE_LOCATION );
        permissions.add( android.Manifest.permission.ACCESS_COARSE_LOCATION );
        permissionsToRequest = findUnAskedPermissions( permissions );

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions( permissionsToRequest.toArray( new String[permissionsToRequest.size()] ),
                            ALL_PERMISSIONS_RESULT );
                    canGetLocation = false;
                }
            }
        }


        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission( this,
                ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText( this, "Permissão negada", Toast.LENGTH_SHORT ).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this );
                this.MIN_TIME_FOR_UPDATES = 20000 * 20;

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this );
                this.MIN_TIME_FOR_UPDATES = 20000 * 20;
            }
        } else {
            Toast.makeText( this, "Não é possível obter a localização", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        localUser = getIntent().getParcelableExtra( "nameLogin" );
        Log.i( "testeParcel", "Maps: " + localUser.getName() );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MIN_TIME_FOR_UPDATES = 0;
                startGettingLocations();
                GetJsonLocal download = new GetJsonLocal();
                download.execute();
                Snackbar.make( view, "Carregando...", Snackbar.LENGTH_LONG ).show();
            }
        } );
        add = findViewById( R.id.add );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), NewLocal.class );
                intent.putExtra( "nameLoginNewLocal", localUser );
                startActivity( intent );
                finish();
            }
        } );



    }

    @Override
    public void onBackPressed() {
        fab.show();
        add.show();
        Intent intent = new Intent( getApplicationContext(), MapsActivity.class );
        intent.putExtra( "nameLogin", localUser );
        startActivity( intent );
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        startGettingLocations();
        mMap = googleMap;
        fab.show();
        add.show();
    }



    @Override
    public void onLocationChanged(Location location) {

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        //Add Current Location  Marker
        currentLocationLatLong = new LatLng( location.getLatitude(), location.getLongitude() );
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position( currentLocationLatLong );
        markerOptions.title( "Você está aqui" );
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerprincipal));

        currentLocationMarker = mMap.addMarker( markerOptions );
        currentLocationMarker.setTag( "100000000000" );
        //move to new location
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom( 15 ).target( currentLocationLatLong ).build();
        mMap.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );
        GetJsonLocal download = new GetJsonLocal();
        download.execute();
        mMap.setInfoWindowAdapter( new PopupAdapter( getLayoutInflater() ) );
        mMap.setOnInfoWindowClickListener( this );

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

}

