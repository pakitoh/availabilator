create database consumer;

CREATE TABLE public.availability (
    "timestamp" timestamp with time zone NOT NULL,
    address text NOT NULL,
    "responseTime" integer NOT NULL,
    "statusCode" smallint NOT NULL,
    matches boolean
);

