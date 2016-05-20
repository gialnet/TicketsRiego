--
-- Guardar un tikect como procesado el servicio de suministro
--
CREATE OR REPLACE FUNCTION SaveTicketDone(
    vnTicket integer,
    vLati  double precision,
    vLongi double precision,
    vComment text
)
returns VOID
AS
$body$
DECLARE
xnTicket integer;
BEGIN

-- proteger la doble insercción de un ticket
SELECT nTicket INTO xnTicket FROM Tickets where nTicket = vnTicket;

IF xnTicket IS NULL THEN
    -- Los valores de fecha se toman mediante la función NOW()
    INSERT INTO Tickets (nTicket, GeoPos, observaciones) VALUES (vnTicket, POINT(vLati, vLongi), vComment);
ELSE
    -- En caso de meterlo dos veces nos quedamos con la fecha y geo posición del último
    UPDATE Tickets set GeoPos=POINT(vLati, vLongi), fecha = NOW(), observaciones = vComment
    WHERE nTicket = vnTicket;
END IF;


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;
