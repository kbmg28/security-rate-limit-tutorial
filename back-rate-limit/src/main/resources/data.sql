drop table if exists "PUBLIC"."PERSON";

------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS "PUBLIC"."SEQUENCE_PERSON" START WITH 1 BELONGS_TO_TABLE;

------------------------------------------------------------------------------------------------------------------------

CREATE CACHED TABLE "PUBLIC"."PERSON"(
    "ID_PERSON" BIGINT DEFAULT (NEXT VALUE FOR "PUBLIC"."SEQUENCE_PERSON")
                NOT NULL NULL_TO_DEFAULT SEQUENCE "PUBLIC"."SEQUENCE_PERSON",
    "NAME" VARCHAR(80) NOT NULL,
    "CPF" VARCHAR(11) NOT NULL,
    "EMAIL" VARCHAR(100) NOT NULL,
    "PASSWORD" VARCHAR(200) NOT NULL
);

------------------------------------------------------------------------------------------------------------------------

ALTER TABLE "PUBLIC"."PERSON" ADD CONSTRAINT "PUBLIC"."ID_PERSON_PK" PRIMARY KEY("ID_PERSON");

------------------------------------------------------------------------------------------------------------------------

INSERT INTO "PUBLIC"."PERSON" VALUES
(1, 'Heloisa Francisca Lívia Assunção', '98922592540', 'helo@test.com', '$2a$12$s7HUW9h7BUXcxGrjMB8XJeOPj8/vnPYEN3OyRGft9iBGmfh.u.X8q'), -- tYRxMgRJeP
(2, 'Lívia Sabrina Esther Alves', '97912809910', 'livia@test.com', '$2a$12$aY.AM01RU8.lVmQbnam8ueb0goTgmhXqPVFtgQtQjU05fjzzBbjrC'), -- Ek36excj6T
(3, 'Ricardo Bruno Vicente Lima', '64615549202', 'ricardo@test.com', '$2a$12$AY1b1A9cIYvorX3x2GzA0uOMjWLIKsIM9HySsu0VA79gJbY4v9MPa'), -- XwFKhNW9zf
(4, 'Gael Manoel Augusto Aparício', '33362731104', 'gael@test.com', '$2a$12$KrEIB9tFaEc4j1INQx3DQeO8Jc9rEMhEp9bgWkv45Aj4YhB6l2Juu'), -- RcXzWaGe9B
(5, 'Olivia Agatha Mirella Melo', '15766889500', 'olivia@test.com', '$2a$12$qbXSDxXfw7ZwqzSym9Q3yemOCSeRhFmLy5xKIr8VE3gOWR4P8ctxe'), -- 120773
(6, 'João Lucas Ferreira', '80386341362', 'joao@test.com', '$2a$12$ekpuH1VRa1sJyN00xNJGKuPGDS8EVB4L67/lCqNfNOZMU9mDqRDfy'); -- 123456