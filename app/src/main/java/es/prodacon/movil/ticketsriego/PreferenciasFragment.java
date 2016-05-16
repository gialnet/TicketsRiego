package es.prodacon.movil.ticketsriego;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by antonio on 16/05/2016.
 */
public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
