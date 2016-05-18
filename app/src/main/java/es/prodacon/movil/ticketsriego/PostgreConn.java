package es.prodacon.movil.ticketsriego;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by antonio on 18/05/2016.
 */
public class PostgreConn extends AsyncTask<Void, Void, Void> {
    static String cadenaConexion = "jdbc:postgresql://37.153.108.75:5432/regantes?" + "user=regantes_prodacon&password=acaPCB-13";

    static String respuestaSql= "vacia";

    public PostgreConn() {
    }

    public String getRespuestaSql (){
        execute();
        return respuestaSql;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultado = null;

        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(cadenaConexion);

            sentencia = conexion.createStatement();

            String consultaSQL = "SELECT version()";

            resultado = sentencia.executeQuery(consultaSQL);

            String respuestaSql = "";

            while (resultado.next()) {
                System.out.println( resultado.getString(1) );
                respuestaSql=resultado.getString(1);
            }

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
