package com.example.jerson.nomadwork.Util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jerson.nomadwork.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup = null;
    private LayoutInflater inflater = null;
    TextView textViewEditar;
    TextView textViewExcluir;

    public PopupAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = inflater.inflate( R.layout.popup, null );
        }
        TextView tv = popup.findViewById( R.id.title );
        tv.setText( marker.getTitle() );
        tv = popup.findViewById( R.id.snippet );
        tv.setText( marker.getSnippet() );
        return (popup);
    }
}
