--
-- Guardar un tikect como procesado el servicio de suministro
-- El tipo de ticket es de prepago, en papel mediante codigos QR
--
CREATE OR REPLACE FUNCTION SaveTicketDone(
    vnTicket integer,
    vEstanque integer,
    vMinutos integer,
    vLati  double precision,
    vLongi double precision,
    vComment text
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
    INSERT INTO Tickets (nTicket, Estanque, Minutos_comprados, GeoPos, observaciones) VALUES (vnTicket, vEstanque, vMinutos, POINT(vLati, vLongi), vComment);
    -- Buscar el estanque por si no existe
    SELECT estanque INTO xnEstanque FROM SaldoEstanque WHERE estanque = vEstanque;
       IF xnEstanque IS NULL THEN
          INSERT INTO SaldoEstanque (estanque, minutos_comprados, minutos_saldo) VALUES (vEstanque, vMinutos, vMinutos);
       ELSE
          UPDATE SaldoEstanque SET minutos_comprados=minutos_comprados+vMinutos, minutos_saldo=minutos_saldo+vMinutos
          WHERE estanque = vEstanque;
       END IF;
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


--
-- Comprar un ticket de riego a través de la WEB
-- El tipo de ticket es de prepago, en papel mediante codigos QR
--
CREATE OR REPLACE FUNCTION BuyTicketWeb(
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


    -- Los valores de fecha se toman mediante la función NOW()
    INSERT INTO Tickets (Estanque, Minutos_comprados, canal_compra) VALUES (vEstanque, vMinutos, 'WEB');

    -- Buscar el estanque por si no existe
    SELECT estanque INTO xnEstanque FROM SaldoEstanque WHERE estanque = vEstanque;

       IF xnEstanque IS NULL THEN
          INSERT INTO SaldoEstanque (estanque, minutos_comprados, minutos_saldo) VALUES (vEstanque, vMinutos, vMinutos);
       ELSE
          UPDATE SaldoEstanque SET minutos_comprados=minutos_comprados+vMinutos, minutos_saldo=minutos_saldo+vMinutos
          WHERE estanque = vEstanque;
       END IF;

END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;


--
-- BORRAR un Ticket
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

-- Buscar los datos del ticket que vamos a borrar
SELECT nTicket,Estanque,Minutos_comprados INTO xnTicket,xnEstanque,xMinutos FROM Tickets where nTicket = vnTicket;

IF xnTicket IS NULL THEN
    -- Error se intenta borrar un ticket que no existe
ELSE
    -- Borrar el ticket
    DELETE FROM Tickets WHERE nTicket = vnTicket;
    UPDATE SaldoEstanque SET minutos_saldo = minutos_saldo - xMinutos WHERE estanque = xnEstanque;
END IF;


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;



--
-- LLENAR AGUA
--
CREATE OR REPLACE FUNCTION PutWater(
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

-- disminuir el saldo
UPDATE SaldoEstanque SET minutos_saldo = minutos_saldo - vMinutos WHERE estanque = vEstanque;

-- insertar la prestación del servicio
INSERT INTO ServiciosEstanque (estanque, minutos_servidos) VALUES (vEstanque, vMinutos);


END;
$body$
LANGUAGE 'plpgsql'
VOLATILE
SECURITY INVOKER
COST 100;
