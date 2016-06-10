package es.prodacon.movil.ticketsriego;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by antonio on 10/06/2016.
 */
public class ShowCrono extends AppCompatActivity implements View.OnClickListener {

    Chronometer cronometro;
    Button botonStart;
    Button botonParar;
    String estado="inactivo";
    final int limite=1;
    MyTimerTask myTask;
    String minutos;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crono);
        Bundle extras = getIntent().getExtras();
        minutos = extras.getString("minutos");
        cronometro = (Chronometer) findViewById(R.id.cronometro);
        botonStart = (Button) findViewById(R.id.btn_enter);
        botonStart.setOnClickListener(this);
        botonParar = (Button) findViewById(R.id.btn_stop);
        botonParar.setOnClickListener(this);
        botonParar.setEnabled(false);
        //AjustarAlarma();
    }

    /*
    private void AjustarAlarma()
    {

    }*/

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_enter) {
            if (estado == "inactivo") {
                cronometro.setBase(SystemClock.elapsedRealtime());
                cronometro.start();
                estado = "activo";
                botonParar.setEnabled(true);
                botonStart.setEnabled(false);
                myTask = new MyTimerTask();
                Timer myTimer = new Timer();

                //   Parameters
                //   task  the task to schedule.
                //   delay  amount of time in milliseconds before first execution.
                //   period  amount of time in milliseconds between subsequent executions.
                myTimer.schedule(myTask, 1, 6000);
                return;
            }
        }

        if (v.getId() == R.id.btn_stop)
        {
            cronometro.stop();

            long time = SystemClock.elapsedRealtime() - cronometro.getBase();
            int horas   = (int)(time /3600000);
            int minutos = (int)(time - horas*3600000)/60000;
            int segundos= (int)(time - horas*3600000- minutos*60000)/1000 ;
            String msg= "Tiempo transcurrido: "+ minutos;
            Toast.makeText(getBaseContext(), msg,
                    Toast.LENGTH_LONG).show();
            estado="inactivo";
            botonStart.setEnabled(true);
            botonParar.setEnabled(false);

        }
    }

    /**
     *
     */
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            long time = SystemClock.elapsedRealtime() - cronometro.getBase();
            int horas   = (int)(time /3600000);
            int minutos = (int)(time - horas*3600000)/60000;
            int segundos= (int)(time - horas*3600000- minutos*60000)/1000 ;
            String msg= "Tiempo transcurrido: "+ minutos;
            Log.d("Variable tiempo", "duración: " + msg);
            if (minutos >= limite)
            {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                mp.start();
                this.cancel();
            }

        }
    }
}
