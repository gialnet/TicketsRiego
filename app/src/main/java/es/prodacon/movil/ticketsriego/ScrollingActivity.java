package es.prodacon.movil.ticketsriego;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.BreakIterator;

import static android.location.LocationManager.GPS_PROVIDER;
import static java.lang.Integer.parseInt;

//public class ScrollingActivity extends AppCompatActivity implements LocationListener,SimpleDialog.OnSimpleDialogListener  {
public class ScrollingActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener  {

    private String QRScaneado;
    //private LocationManager locationManager;
    private boolean QuiereGrabar=true;
    //private Location Posicion;
    private String Observaciones="";

    public ScrollingActivity() {

    }

    /**
     * Escanear un QR
     * @return
     */
    public String getQRScaneado() {
        return QRScaneado;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(GPS_PROVIDER, 2000, 1, (LocationListener) this);*/


    }

    /**
     * Mostrar un dialogo desde el tercer botón
     * @param view
     */
    public void mostrarDialogo(View view){
        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");
    }
/*
    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();

        this.Posicion=location;
        // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps activado!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps apagado!! ",
                Toast.LENGTH_SHORT).show();
    }
*/
    /**
     * Arrancar el motor
     *
     * @param view
     */
    public void execArrancaMotor(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String tlf_motor = mySharedPreferences.getString("tlf_motor", "");


           // Log.d("SEARCH_TLF", "telefono: : " +  tlf_motor);

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tlf_motor)));

            // Anotar en la base de datos Arrancar el motor
            PostgreStartEngine startE = new PostgreStartEngine();
            startE.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parar el motor
     *
     * @param view
     */
    public void execStopMotor(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String tlf_motor = mySharedPreferences.getString("tlf_motor", "");


            // Log.d("SEARCH_TLF", "telefono: : " +  tlf_motor);

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tlf_motor)));

            // Anotar en la base de datos Arrancar el motor
            PostgreStopEngine stopE = new PostgreStopEngine();
            stopE.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferenciasActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * @param view
     */
    public void scanQRCode(View view) {

        //IntentIntegrator integrator = new IntentIntegrator(this);
        //integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);

        new IntentIntegrator(ScrollingActivity.this).initiateScan();


        /*
        String msg = "New Latitude: " + this.Posicion.getLatitude()
                + "New Longitude: " + this.Posicion.getLongitude();*/

        //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();



    }


    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {

                    IntentResult intentResult =
                            IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

                    if (intentResult != null) {

                        String contents = intentResult.getContents();
                        String format = intentResult.getFormatName();

                        this.QRScaneado=contents;

                        Intent intent2 = new Intent(this, MostrarQR.class);
                        intent2.putExtra("QRScaneado", this.QRScaneado);
                        startActivityForResult(intent2,12345);

                        Log.d("SEARCH_QR", "OK, QR: " + contents + ", FORMATO: " + format);
                    } else {
                        Log.e("SEARCH_QR", "IntentResult fue NULL!");
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e("SEARCH_QR", "CANCELADO EL SCANEO");
                }
                break;
            case 12345:
                if (resultCode == Activity.RESULT_OK)
                {
                    // salio por confirmar
                    String res = intent.getExtras().getString("resultado");
                    Observaciones = intent.getExtras().getString("observaciones");
                    PostgreInsertTicket inserta = new PostgreInsertTicket();
                    inserta.execute();
                    //Toast.makeText(getBaseContext(), res , Toast.LENGTH_LONG).show();
                }

        }
    }

    /**
     *
     */
    public void ProcesarQR() {

       // Activar temporizador con alarma
       // Leer las coordenadas GPS
       // Grabar en SQLite
       // Grabar en PostgreSQL

       // Toast.makeText(getBaseContext(), this.QRScaneado ,Toast.LENGTH_LONG).show();
        //Desde la version 3 de android, no se permite abrir una conexión de red desde el thread principal.
        //Por lo tanto se debe crear uno nuevo.

        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");


    }

    /**
     * Arrancar un hilo nuevo para la consulta SQL
     */
    /*
    Thread sqlThread = new Thread() {
        public void run() {


            try {
                Class.forName("org.postgresql.Driver");
                // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://37.153.108.75:5432/regantes", "regantes_prodacon", "acaPCB-13");

                 JSONObject jsonObject = null;


                //
                try {
                    jsonObject = (JSONObject) new JSONParser().parse(getQRScaneado());
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse json " + getQRScaneado());
                }

                String Titular = (String) jsonObject.get("Titular");
                String nTicket = (String) jsonObject.get("nTicket");
                String Horas = (String) jsonObject.get("Horas");
                String Precio = (String) jsonObject.get("Precio");
                String Forma_Pago = (String) jsonObject.get("Forma Pago");

                jsonObject = null;

                //En el stsql se puede agregar cualquier consulta SQL deseada.
                String stsql = "Select version()";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(stsql);
                rs.next();
                System.out.println( rs.getString(1) );
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
        }
    };*/


    @Override
    public void onPossitiveButtonClick() {

        QuiereGrabar=true;
        Toast.makeText(getBaseContext(), R.string.grabar,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativeButtonClick() {

        QuiereGrabar=false;
        Toast.makeText(getBaseContext(), R.string.cancelado ,Toast.LENGTH_LONG).show();
    }


    /*
    new AsyncTask<Integer, Void, Void>(){
        @Override
        protected Void doInBackground(Integer... params) {
            // **Code**
            return null;
        }
    }.execute(1, 2, 3, 4, 5);
     */

    /**
     * Insertar un nuevo ticket leido desde un QR
     */
    public class PostgreInsertTicket extends AsyncTask<Void, Void, Void> {


        private String nTicket;
        private String Estanque;
        private String Minutos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String vQR=getQRScaneado();
            JSONObject jsonObject = null;

            try {
                jsonObject = (JSONObject) new JSONParser().parse(vQR);
            } catch (ParseException e) {
                throw new RuntimeException("Unable to parse json " + vQR);
            }

            nTicket = (String) jsonObject.get("nTicket");
            Estanque = (String) jsonObject.get("NEstanque");
            Minutos = (String) jsonObject.get("Minutos");

            jsonObject = null;

        }

        @Override
        protected Void doInBackground(Void... params) {

            Connection conexion = null;
            CallableStatement sentencia = null;
            ResultSet resultado = null;



            try {
                Class.forName("org.postgresql.Driver");
                 conexion = DriverManager.getConnection("jdbc:postgresql://37.153.108.75:5432/regantes", "regantes_prodacon", "acaPCB-13");



                String consultaSQL = "{ call ReadTicketQR (?,?,?) }";

                sentencia = conexion.prepareCall(consultaSQL);
                sentencia.setInt(1,parseInt(nTicket));
                sentencia.setInt(2,parseInt(Estanque));
                sentencia.setInt(3,parseInt(Minutos));
                //sentencia.setDouble(2,Posicion.getLatitude());
                //sentencia.setDouble(3,Posicion.getLongitude());
                //sentencia.setString(4,Observaciones);


                sentencia.execute();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println("Error: Cant connect!");
                conexion = null;
            } finally {
                if (resultado != null) {
                    try {
                        resultado.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (sentencia != null) {
                    try {
                        sentencia.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
            }
            System.err.println("----- PostgreSQL query ends correctly!-----");
            return null;
        }
    }

    /**
     * Gestión de inicio/parado del motor de riego
     */
    public class PostgreStartEngine extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Connection conexion = null;
            CallableStatement sentencia = null;
            ResultSet resultado = null;



            try {
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection("jdbc:postgresql://37.153.108.75:5432/regantes", "regantes_prodacon", "acaPCB-13");



                String consultaSQL = "{ call StartEngine () }";

                sentencia = conexion.prepareCall(consultaSQL);

                sentencia.execute();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println("Error: Cant connect!");
                conexion = null;
            } finally {
                if (resultado != null) {
                    try {
                        resultado.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (sentencia != null) {
                    try {
                        sentencia.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
            }
            System.err.println("----- PostgreSQL arrancar motor ends correctly!-----");
            return null;
        }
    }

    /**
     * Parar el motor
     */
    public class PostgreStopEngine extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Connection conexion = null;
            CallableStatement sentencia = null;
            ResultSet resultado = null;



            try {
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection("jdbc:postgresql://37.153.108.75:5432/regantes", "regantes_prodacon", "acaPCB-13");



                String consultaSQL = "{ call StoptEngine () }";

                sentencia = conexion.prepareCall(consultaSQL);

                sentencia.execute();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println("Error: Cant connect!");
                conexion = null;
            } finally {
                if (resultado != null) {
                    try {
                        resultado.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (sentencia != null) {
                    try {
                        sentencia.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                    }
                }
            }
            System.err.println("----- PostgreSQL parar motor ends correctly!-----");
            return null;
        }
    }
}