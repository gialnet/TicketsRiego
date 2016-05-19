
--
-- Tikect de riego
--
CREATE TABLE Tickets (
    id              serial not null,
    nticket         varchar(20),
    canal_compra    varchar(20) default 'papel',
    fecha           timestamp default now(),
    GeoPos          point,
    observaciones   text,
    primary key(id)
);