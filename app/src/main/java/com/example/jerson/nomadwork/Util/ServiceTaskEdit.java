package com.example.jerson.nomadwork.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.jerson.nomadwork.BasicClass.Local;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceTaskEdit extends AsyncTask<Void, Void, String> {
    //variables del hilo
    private Context httpContext;//contexto
    ProgressDialog progressDialog;//dialogo cargando
    public String resultadoapi;
    public String linkrequestAPI;//link  para consumir el servicio rest
    public String wifi;
    public String energy;
    public String noise;
    public String price;
    public int id;

    //constructor del hilo (Asynctask)
    public ServiceTaskEdit(Context ctx, String linkAPI, Local local) {
        this.httpContext = ctx;
        this.linkrequestAPI = linkAPI;
        this.wifi = local.getInternet();
        this.energy = local.getEnergy();
        this.noise = local.getNoise();
        this.price = local.getPrice();
        this.id = local.getLocalId();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
          progressDialog = ProgressDialog.show( httpContext, "Procesando Solicitação", "por favor, aguarde" );
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;

        String wsURL = linkrequestAPI;
        Log.i( "testeUrlp", wsURL );
        URL url = null;
        try {
            // se crea la conexion al api: http://localhost:15009/WEBAPIREST/api/persona
            url = new URL( wsURL );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //crear el objeto json para enviar por POST
            JSONObject parametrosPost = new JSONObject();
            parametrosPost.put( "idLocation", id+"" );
            parametrosPost.put( "wifi", wifi + "" );
            parametrosPost.put( "energy", energy + "" );
            parametrosPost.put( "noise", noise + "" );
            parametrosPost.put( "price", price + "" );
            Log.i( "TesteparametrosPost", String.valueOf( parametrosPost ) );
            Log.i( "TesteMetodo1", httpContext
                    + "|" + linkrequestAPI
                    + "|" + wifi
                    + "|" + energy
                    + "|" + noise
                    + "|" + price
            );
            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout( 15000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "PATCH" );// se puede cambiar por delete ,put ,etc
            urlConnection.setDoInput( true );
            urlConnection.setDoOutput( true );


            //OBTENER EL RESULTADO DEL REQUEST
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
            writer.write( String.valueOf( parametrosPost ) );
            writer.flush();
            writer.close();
            os.close();
            String t = String.valueOf( parametrosPost ) ;
            Log.i( "testewrite", t);
            int responseCode = urlConnection.getResponseCode();// conexion OK?
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() ) );

                StringBuffer sb = new StringBuffer( "" );
                String linea = "";
                while ((linea = in.readLine()) != null) {
                    sb.append( linea );
                    break;

                }
                in.close();
                result = sb.toString();
            } else {
                result = new String( "Error: " + responseCode );

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i( "erro1: ", String.valueOf( e ) );
        } catch (IOException e) {
            e.printStackTrace();
            Log.i( "erro2: ", String.valueOf( e ) );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i( "erro3: ", String.valueOf( e ) );
        } catch (Exception e) {
            e.printStackTrace();
            Log.i( "erro4: ", String.valueOf( e ) );
        }

        Log.i( "ResultPost", result );
        return result;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute( s );
         progressDialog.dismiss();
        resultadoapi = s;
        Toast.makeText( httpContext, resultadoapi, Toast.LENGTH_LONG ).show();//mostrara una notificacion con el resultado del request

    }

}