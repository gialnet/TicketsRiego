--
-- Guardar un tikect como procesado el servicio de suministro
--
CREATE OR REPLACE FUNCTION SaveTicketDone(
    vnTikect integer,
    vGeoPos  point
)
returns VOID
AS
$body$
DECLARE

BEGIN

-- Los valores de fecha se toman mediante la función NOW()
INSERT INTO Tickets (nTicket, GeoPos) VALUES (vnTicket, vGeoPos);


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;
