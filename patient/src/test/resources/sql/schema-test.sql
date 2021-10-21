--
-- Creation of patienttest DB
--
CREATE
    DATABASE IF NOT EXISTS patienttest;

USE
    patienttest;

--
-- Remove previous versions of tables
--
DROP TABLE IF EXISTS patients;

--
-- Create table patients if not exists
--
create table if not exists patients
(
    id        int auto_increment primary key,
    address   varchar(150) not null,
    birthdate date         not null,
    firstname varchar(64)  not null,
    gender    varchar(1)   not null,
    lastname  varchar(64)  not null,
    phone     varchar(15)  not null
);