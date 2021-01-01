CREATE TABLE courses (
    id SERIAL PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    surname TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS professors (
    id SERIAL PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    qualification TEXT NOT NULL
);
