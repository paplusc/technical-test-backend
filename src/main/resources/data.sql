DROP TABLE IF EXISTS wallet;

CREATE TABLE wallet (
    id bigint primary key,
    balance decimal,
    version bigint
);

insert into wallet (id, balance, version) values (100, 999.99, 0);
insert into wallet (id, balance, version) values (101, 1000.00, 0);
insert into wallet (id, balance, version) values (102, 520.07, 0);