package com.example.jerson.nomadwork.Util;

import android.util.Log;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.BasicClass.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;


/**
 * Created by Jerson on 12/05/2018.
 */

public class Utils {
    public static List<Local> getInformacaoLocal() {
        List<Local> loca = new ArrayList<Local>();
        String json;
        json = "{\"results\":" + UtilsJson.getJSONFromAPI( Rotes.METHOD_HTTP_GET ) + "}";
        Log.i( "Teste 0 ", "Este é o JSON:" + json );

        try {

            JSONArray array;
            JSONObject jsonObj = new JSONObject( json );
            array = jsonObj.getJSONArray( "results" );

            for (int i = 0; i < array.length(); i++) {
                Local local = new Local();
                JSONObject objArray = array.getJSONObject( i );
                local.setLocalId( objArray.getInt( "idLocation" ) );
                local.setLocalName( objArray.getString( "name" ) );
                local.setLongitude( objArray.getDouble( "longitude" ) );
                local.setLatitude( objArray.getDouble( "latitude" ) );
                local.setEnergy( objArray.getString( "energy" ) );
                local.setInternet( objArray.getString( "wifi" ) );
                local.setNoise( objArray.getString( "noise" ) );
                local.setPrice( objArray.getString( "price" ) );
                loca.add( local );
            }
            Log.i( "Teste 1 ", "Objeto Local criado a partir do JSON" );
            return loca;

        } catch (JSONException e) {
            Log.i( "Teste 1 ", "Erro: Objeto Local não foi criado" );
            return null;
        }

    }

    public static List<User> getInformacaoUser() {
        List<User> users = new ArrayList<User>();
        String json;
        json = "{\"results\":" + UtilsJson.getJSONFromAPI( Rotes.USER) + "}";
        Log.i( "Teste 0 ", "Este é o JSON (GET):" + json );

        try {

            JSONArray array;
            JSONObject jsonObj = new JSONObject( json );
            array = jsonObj.getJSONArray( "results" );

            for (int i = 0; i < array.length(); i++) {
                User user = new User();
                JSONObject objArray = array.getJSONObject( i );
                user.setUserId( objArray.getInt( "user_id" ) );
                user.setName( objArray.getString( "name" ) );
                user.setLatitude( objArray.getDouble( "latUser" ) );
                user.setLongitude( objArray.getDouble( "lngUser" ) );
                users.add( user );
            }
            Log.i( "Teste 1 ", "Objeto usuário criado a partir do JSON" );
            return users;

        } catch (JSONException e) {
            Log.i( "Teste 1 ", "Erro: Objeto usuário não foi criado" + e );
            return null;
        }

    }

//    public static Local postLocal(Local local) {
//        String json;
//        String url;
//        url = Rotes.LOCATION;
//        JSONObject objLocal = new JSONObject();
//        try {
//            objLocal.put( "name", local.getLocalName() );
//            objLocal.put( "latitude", local.getLatitude() );
//            objLocal.put( "longitude", local.getLongitude() );
//            objLocal.put( "wifi", local.getInternet() );
//            objLocal.put( "energy", local.getEnergy() );
//            objLocal.put( "noise", local.getNoise() );
//            objLocal.put( "price", local.getPrice() );
//            objLocal.put( "nameCreator",local.getNameCreator() );
//            json = String.valueOf( url );
//            UtilsJson.postJSON( json );
//            Log.i( "TesteJsonLocal", String.valueOf( json ) );
//            return local;
//        } catch (JSONException e) {
//            Log.i( "TesteJsonLocal", "Erro: Objeto Local não foi enviado" + e );
//        }
//        return null;
//
//
//    }

}