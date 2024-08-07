CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid DEFAULT uuid_generate_v4() NOT NULL UNIQUE PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    email CHARACTER VARYING(100) NOT NULL UNIQUE,
    login CHARACTER VARYING(15) NOT NULL UNIQUE,
    password CHARACTER VARYING(255) NOT NULL,
    profile CHARACTER VARYING(100) NOT NULL,
    preferences TEXT[] NOT NULL,
    bio CHARACTER VARYING(255)
);