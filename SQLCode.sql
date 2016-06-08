--
-- Leer un ticket en papel mediante QR para confeccionar la lista de peticiones de riego
-- son los tickets que ponen en el buzón físico del regador, prepago en banco
--
CREATE OR REPLACE FUNCTION ReadTicketQR(
    vnTicket integer,
    vEstanque integer,
    vMinutos integer
)
returns VOID
AS
$body$
DECLARE
xnTicket integer;
xnEstanque integer;
BEGIN

-- proteger la doble insercción de un ticket
SELECT nTicket INTO xnTicket FROM Tickets where nTicket = vnTicket;

IF xnTicket IS NULL THEN
    -- Los valores de fecha se toman mediante la función NOW()
    INSERT INTO Tickets (nTicket, Estanque, Minutos_comprados) VALUES (vnTicket, vEstanque, vMinutos);

ELSE
    -- En caso de meterlo dos veces nos quedamos con la fecha
    UPDATE Tickets set fecha_buy = NOW()
        WHERE nTicket = vnTicket;
END IF;


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;



--
-- Arrancar el motor
--
CREATE OR REPLACE FUNCTION StartEngine()
returns VOID
AS
$body$
DECLARE
    xFecha timestamp;
BEGIN

    -- por defecto sólo tenemos un motor valor 1, la fecha de arranque fecha_start se pone por defecto
    -- a la fecha del sistema, la fecha de parada la ponemos a null y se pondrá en el proceso de Stop

    SELECT fecha_start INTO xFecha FROM UsoMotor WHERE fecha_stop IS NULL;
    IF xFecha IS NULL THEN
        INSERT INTO LogUsoMotor (id_motor, observaciones) values (1, concat('Se arranca el motor fecha y hora de arranque: ', now()));
        INSERT INTO UsoMotor (id_motor,fecha_stop) VALUES (1, null);
    ELSE
        -- En caso de que no se hubiera activado el motor
        -- actualizamos la fecha y hora puesta en marcha
        INSERT INTO LogUsoMotor (id_motor, observaciones) values (1, concat('Se actualiza la fecha y hora de arranque del motor: ', xFecha));
        UPDATE UsoMotor SET fecha_start = now() WHERE fecha_stop IS NULL;
    END IF;

END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;

--
-- Parar el motor
--
CREATE OR REPLACE FUNCTION StopEngine()
returns VOID
AS
$body$
DECLARE
BEGIN

    -- por defecto sólo tenemos un motor valor 1, la fecha de arranque fecha_start se pone por defecto
    -- a la fecha del sistema, la fecha de parada la ponemos a null y se pondrá en el proceso de Stop
    UPDATE UsoMotor SET fecha_stop = now() WHERE fecha_stop IS NULL;
    INSERT INTO LogUsoMotor (id_motor, observaciones) values (1, concat('Se para el motor fecha y hora de parada: ', now()));

END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;

--
--
--
CREATE OR REPLACE FUNCTION DeleteTicket(
    vnTicket integer
)
returns VOID
AS
$body$
DECLARE
xnTicket integer;
xnEstanque integer;
xMinutos integer;
BEGIN

    -- Borrar el ticket
    DELETE FROM Tickets WHERE nTicket = vnTicket;


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;

