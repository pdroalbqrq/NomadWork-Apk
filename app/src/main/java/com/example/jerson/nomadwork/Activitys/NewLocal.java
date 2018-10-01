package com.example.jerson.nomadwork.Activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.BasicClass.User;
import com.example.jerson.nomadwork.R;
import com.example.jerson.nomadwork.Util.Banco;
import com.example.jerson.nomadwork.Util.Rotes;
import com.example.jerson.nomadwork.Util.ServiceTaskPost;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewLocal extends AppCompatActivity implements LocationListener {
    Local local;
    User localUser;
Banco b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_new_local );
        localUser = getIntent().getParcelableExtra( "nameLoginNewLocal" );
        Log.i( "testeParcel", "NewLocal: " + localUser.getName() );
        final EditText nameEdit = findViewById( R.id.name_new_local );
        final Spinner wifi = findViewById( R.id.spinnerWifi );
        final ArrayAdapter<CharSequence> adapterWifi = ArrayAdapter.createFromResource( this,
                R.array.arrayWifi, android.R.layout.simple_spinner_item );
        final Spinner energy = findViewById( R.id.spinnerEnergy );
        final ArrayAdapter<CharSequence> adapterEnergy = ArrayAdapter.createFromResource( this,
                R.array.arrayEnergy, android.R.layout.simple_spinner_item );
        final Spinner noise = findViewById( R.id.spinnerNoise );
        final ArrayAdapter<CharSequence> adapterNoise = ArrayAdapter.createFromResource( this,
                R.array.arrayNoise, android.R.layout.simple_spinner_item );
        final Spinner price = findViewById( R.id.spinnerPrice );
        final ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter.createFromResource( this,
                R.array.arrayPrice, android.R.layout.simple_spinner_item );
        adapterWifi.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterEnergy.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterNoise.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterPrice.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        wifi.setAdapter( adapterWifi );
        energy.setAdapter( adapterEnergy );
        noise.setAdapter( adapterNoise );
        price.setAdapter( adapterPrice );
        startGettingLocations();
        local = new Local();
        FloatingActionButton fab = findViewById( R.id.confirm );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                local.setLocalName( nameEdit.getText().toString() );
                int posicaoWifi = wifi.getSelectedItemPosition();
                String itemWifi = (String) adapterWifi.getItem( posicaoWifi );
                int posicaoEnergy = energy.getSelectedItemPosition();
                String itemEnergy = (String) adapterEnergy.getItem( posicaoEnergy );
                int posicaoNoise = noise.getSelectedItemPosition();
                String itemNoise = (String) adapterNoise.getItem( posicaoNoise );
                int posicaoPrice = price.getSelectedItemPosition();
                String itemPrice = (String) adapterPrice.getItem( posicaoPrice );
                if (nameEdit.getText().toString().isEmpty()
                        || nameEdit.getText().toString().length() > 50
                        || nameEdit.getText().toString().length() < 3
                        || nameEdit.getText().toString().equals( " " )
                        || itemPrice.equals( "Valor" )
                        || itemNoise.equals( "Ambiente" )
                        || itemEnergy.equals( "Tomada" )
                        || itemWifi.equals( "Wi-Fi" )) {
                    Snackbar.make( view, "Preencha os campos corretamente ", Snackbar.LENGTH_LONG ).show();
                } else {
                    local.setNameCreator( localUser.getName() );
                    local.setPrice( itemPrice );
                    local.setNoise( itemNoise );
                    local.setEnergy( itemEnergy );
                    local.setInternet( itemWifi );
                    consumirServicio();
                    Log.i( "Teste 5:", local.getLocalName()
                            + "|" + local.getInternet()
                            + "|" + local.getEnergy()
                            + "|" + local.getNoise()
                            + "|" + local.getLatitude()
                            + "|" + local.getLongitude()
                            + "|" + local.getNameCreator()
                    );
                    b = new Banco( getApplicationContext() );
                    b.insertInto( local );
                    Intent intent = new Intent( getApplicationContext(), MapsActivity.class );
                    intent.putExtra( "nameLogin", localUser );
                    startActivity( intent );
                    finish();
                }
            }
        } );
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), MapsActivity.class );
        intent.putExtra( "nameLogin", localUser );
        startActivity( intent );
        finish();
        startGettingLocations();
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
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

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

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this );

            }
        } else {
            Toast.makeText( this, "Não é possível obter a localização", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        local.setLatitude( location.getLatitude() );
        local.setLongitude( location.getLongitude() );
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

    public void consumirServicio() {
        ServiceTaskPost task = new ServiceTaskPost( this, Rotes.METHOD_HTTP_POST, local );
        task.execute();
        startGettingLocations();
    }
}

