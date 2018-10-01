package com.example.jerson.nomadwork.Util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Melo on 12/05/2018.
 * Edited by Jerson on 01/06/2018.
 */

public class UtilsJson {
    //Responsavel por carregar o Objeto JSON
    public static String getJSONFromAPI(String url) {
        String retorno = "";
        try {
            URL apiEnd = new URL( url );
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod( "GET" );
            conexao.setReadTimeout( 15000 );
            conexao.setConnectTimeout( 15000 );
            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
            } else {
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString( is );
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i( "Erro", "Objeto JSON não carregado:" + e );
        }

        return retorno;
    }

    //Responsavel por enviar o Objeto JSON


    private static String converterInputStreamToString(InputStream is) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br;
            String linha;
            br = new BufferedReader( new InputStreamReader( is ) );
            while ((linha = br.readLine()) != null) {
                buffer.append( linha );
            }

            br.close();
        } catch (IOException e) {
            Log.i( "Erro", "Objeto JSON não carregado:" + e );
        }

        return buffer.toString();
    }

}
