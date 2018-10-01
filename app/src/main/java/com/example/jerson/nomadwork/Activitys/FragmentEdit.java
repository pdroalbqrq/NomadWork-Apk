package com.example.jerson.nomadwork.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.R;
import com.example.jerson.nomadwork.Util.Rotes;
import com.example.jerson.nomadwork.Util.ServiceTaskDelete;
import com.example.jerson.nomadwork.Util.ServiceTaskEdit;
import com.example.jerson.nomadwork.Util.ServiceTaskPost;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEdit extends Fragment {
    Local local;

    public FragmentEdit() {

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
       Bundle args = getArguments();

        if(args != null){
            local = args.getParcelable( "localData" );

        }
        Log.i( "testeParcel", "localFragment: " + local );

        final Spinner wifi = getView().findViewById( R.id.fragSpnWifi );
        final ArrayAdapter<CharSequence> adapterWifi = ArrayAdapter.createFromResource( getContext(),
                R.array.arrayWifi, android.R.layout.simple_spinner_item );
        final Spinner energy = getView().findViewById( R.id.fragSpnEnergy );
        final ArrayAdapter<CharSequence> adapterEnergy = ArrayAdapter.createFromResource( getContext(),
                R.array.arrayEnergy, android.R.layout.simple_spinner_item );
        final Spinner noise = getView().findViewById( R.id.fragSpnNoise );
        final ArrayAdapter<CharSequence> adapterNoise = ArrayAdapter.createFromResource( getContext(),
                R.array.arrayNoise, android.R.layout.simple_spinner_item );
        final Spinner price = getView().findViewById( R.id.fragSpnPrice );
        final ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter.createFromResource( getContext(),
                R.array.arrayPrice, android.R.layout.simple_spinner_item );
        adapterWifi.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterEnergy.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterNoise.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        adapterPrice.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        wifi.setAdapter( adapterWifi );
        energy.setAdapter( adapterEnergy );
        noise.setAdapter( adapterNoise );
        price.setAdapter( adapterPrice );
        Button confirmEdit = getView().findViewById( R.id.buttonEdit );
        Button delete = getView().findViewById( R.id.buttonDelete );
        Button buttonBack = getView().findViewById(R.id.buttonBack);

        confirmEdit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posicaoWifi = wifi.getSelectedItemPosition();
                String itemWifi = (String) adapterWifi.getItem( posicaoWifi );
                int posicaoEnergy = energy.getSelectedItemPosition();
                String itemEnergy = (String) adapterEnergy.getItem( posicaoEnergy );
                int posicaoNoise = noise.getSelectedItemPosition();
                String itemNoise = (String) adapterNoise.getItem( posicaoNoise );
                int posicaoPrice = price.getSelectedItemPosition();
                String itemPrice = (String) adapterPrice.getItem( posicaoPrice );
                if (itemPrice.equals( "Valor" )
                        || itemNoise.equals( "Ambiente" )
                        || itemEnergy.equals( "Tomada" )
                        || itemWifi.equals( "Wi-Fi" )) {
                    Snackbar.make( getView(), "Preencha os campos corretamente ", Snackbar.LENGTH_LONG ).show();
                } else {
                    local.setPrice( itemPrice );
                    local.setNoise( itemNoise );
                    local.setEnergy( itemEnergy );
                    local.setInternet( itemWifi );
                    editarRegistro();

                    Objects.requireNonNull( getActivity() ).onBackPressed();
                }
                Objects.requireNonNull( getActivity() ).onBackPressed();
            }
        } );

        delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull( getActivity() ).onBackPressed();
                deletarRegisto();
            }
        } );

        buttonBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull( getActivity() ).onBackPressed();
            }
        } );

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_fragment_edit, container, false );

    }

    public void deletarRegisto() {
      //  String url = Rotes.METHOD_HTTP_DELETE + local.getLocalId();
       // Log.i( "testeRoute", url );
       // ServiceTaskDelete task = new ServiceTaskDelete( getContext(), url, local );
       // task.execute();
    }

    public void editarRegistro() {
       // String url = Rotes.METHOD_HTTP_PATCH+ local.getLocalId();
       // Log.i( "testeRoute", url );
       // ServiceTaskEdit task = new ServiceTaskEdit( getContext(), url, local );
       // task.execute();
    }

}
