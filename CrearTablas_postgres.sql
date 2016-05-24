
--
-- Tickets de riego
--
CREATE TABLE Tickets (
    id                  serial not null,
    estanque            integer,
    nticket             integer,
    canal_compra        varchar(20) default 'papel',
    minutos_comprados   integer,
    fecha               timestamp default now(),
    GeoPos              point,
    observaciones       text,
    primary key(id)
);
create index Tickets_nticket on Tickets(nticket);
create index Tickets_estanque on Tickets(estanque);

--
-- Saldo en minutos de un estanque
--
CREATE TABLE SaldoEstanque (
    estanque            integer not null,
    minutos_comprados   integer,
    minutos_saldo       integer,
    primary key(estanque)
);

--
-- Movimientos llenado agua
--
CREATE TABLE ServiciosEstanque (
    estanque            integer,
    minutos_servidos    integer,
    fecha               timestamp default now()
);
create index ServiciosEstanque_estanque on ServiciosEstanque(estanque);