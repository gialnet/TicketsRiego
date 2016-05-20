package es.prodacon.movil.ticketsriego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MostrarQR extends AppCompatActivity {

    private String sQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_qr);

        Bundle extras = getIntent().getExtras();
        sQR = extras.getString("QRScaneado");
        //Toast.makeText(getBaseContext(), sQR, Toast.LENGTH_LONG).show();
        ProcesarQR();
    }

    /**
     *
     */
    private void ProcesarQR() {

        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) new JSONParser().parse(sQR);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse json " + sQR);
        }

        String Titular = (String) jsonObject.get("Titular");
        String NEstanque = (String) jsonObject.get("NEstanque");
        String nTicket = (String) jsonObject.get("nTicket");
        String Horas = (String) jsonObject.get("Horas");
        String Precio = (String) jsonObject.get("Precio");
        //String Forma_Pago = (String) jsonObject.get("Forma Pago");
        String Email = (String) jsonObject.get("Email");
        String Movil = (String) jsonObject.get("Movil");

        EditText titu = (EditText) findViewById(R.id.nombre);
        titu.setText(Titular);

        EditText esta = (EditText) findViewById(R.id.nestanque);
        esta.setText(NEstanque);

        EditText ticket = (EditText) findViewById(R.id.nticket);
        ticket.setText(nTicket);

        EditText horas = (EditText) findViewById(R.id.horas);
        horas.setText(Horas);

        EditText precio = (EditText) findViewById(R.id.importe);
        precio.setText(Precio);

        EditText email = (EditText) findViewById(R.id.email);
        email.setText(Email);

        EditText movil = (EditText) findViewById(R.id.movil);
        movil.setText(Movil);

        jsonObject = null;
    }


    /**
     * Confirma el ticket
     * @param view
     */
    public void ConfirmarTicket(View view)
    {
        Intent intencion = new Intent();
        intencion.putExtra("resultado","confirma");
        EditText obse = (EditText) findViewById(R.id.observaciones);
        String texto=obse.getText().toString();
        intencion.putExtra("observaciones", texto);
        setResult(RESULT_OK, intencion);
        finish();
    }

    /**
     * Cancelar la lectura del ticket
     * @param view
     */
    public void CancelaTicket(View view)
    {
        Intent intencion = new Intent();
        intencion.putExtra("resultado","cancela");
        setResult(RESULT_CANCELED, intencion);
        finish();
    }

}
