package com.example.jerson.nomadwork.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.BasicClass.User;
import com.example.jerson.nomadwork.R;
import com.example.jerson.nomadwork.Util.Rotes;
import com.example.jerson.nomadwork.Util.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog load;
    User localUser;
    int userId;
    String name;
    Double latitude;
    Double longitude;



    private class GetJsonUser extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> use = Utils.getInformacaoUser();
            Log.i( "Teste 2.1 ", "Objeto Usuário criado na LoginActivity" );
            return use;
        }

        protected void onPreExecute() {
            load = ProgressDialog.show( LoginActivity.this, "Por favor Aguarde ...",
                    "Recuperando Informações do Servidor..." );

        }


        @Override
        protected void onPostExecute(List<User> userList) {

            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);
                userId = user.getUserId();
                name = user.getName();
                latitude = user.getLatitude();
                longitude = user.getLongitude();

                Log.i( "Teste lista", user.getUserId()
                        + "|" + user.getName()
                        + "|" + user.getLatitude()
                        + "|" + user.getLongitude()
                );
            }
            load.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        final EditText nameLogin = findViewById( R.id.txtName );
        Button access = findViewById( R.id.button );
        GetJsonUser download = new GetJsonUser();
        download.execute();
        localUser = new User();
        access.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameLogin.getText().toString().isEmpty()) {
                    Snackbar.make( view, "Preencha os campos corretamente ", Snackbar.LENGTH_LONG ).show();
                } else if(nameLogin.getText().toString().length() > 50
                       || nameLogin.getText().toString().length() < 3){
                    Snackbar.make( view, "Preencha o login com mais de 3 e no máximo 50 caracteres", Snackbar.LENGTH_LONG ).show();
                } else {
                    localUser.setName( nameLogin.getText().toString().replace( " ","" ).trim());

                    Intent intent = new Intent( getApplicationContext(), MapsActivity.class );
                    intent.putExtra( "nameLogin", localUser );
                    startActivity( intent );
                    Log.i( "testeParcel", "nameLogin: " + localUser.getName() );
                    finish();
                }
            }
        } );
    }
}
