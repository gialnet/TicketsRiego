package es.prodacon.movil.ticketsriego;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by antonio on 13/07/2016.
 */
public class AuthenticationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        TicketsAuthenticator myAut = new TicketsAuthenticator(this);
        return myAut.getIBinder();
    }
}
