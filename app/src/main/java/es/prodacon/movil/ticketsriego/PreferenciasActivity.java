package es.prodacon.movil.ticketsriego;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by antonio on 16/05/2016.
 */
public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}
