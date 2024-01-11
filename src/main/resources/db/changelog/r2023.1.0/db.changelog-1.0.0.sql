--liquibase formatted sql
--changeset @Sam:1.0.0_Initial Tables

-- **DO NOT CHANGE BELOW SCHEMA NAME ** SYSTEM GENERATED - START**
SET search_path TO pets;
-- **DO NOT CHANGE ABOVE SCHEMA NAME ** SYSTEM GENERATED - END**

CREATE TABLE PETS (
    uuid uuid,
    name VARCHAR(50) ,
    CONSTRAINT pets_pk PRIMARY KEY(uuid)
);