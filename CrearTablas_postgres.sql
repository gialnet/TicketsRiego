
--
-- Tickets de riego
--
CREATE TABLE Tickets (
    id                  serial not null,
    estanque            integer,
    nticket             integer,
    canal_compra        varchar(20) default 'papel',
    minutos_comprados   integer,
    minutos_servidos    integer,
    fecha               timestamp default now(),
    GeoPos              point,
    observaciones       text,
    primary key(id)
);
create index Tickets_nticket on Tickets(nticket);