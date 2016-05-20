
--
-- Tickets de riego
--
CREATE TABLE Tickets (
    id              serial not null,
    nticket         integer,
    canal_compra    varchar(20) default 'papel',
    fecha           timestamp default now(),
    GeoPos          point,
    observaciones   text,
    primary key(id)
);
create index Tickets_nticket on Tickets(nticket);