CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(15) NOT NULL UNIQUE,
    description TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY NOT NULL,
    personal_id VARCHAR(15) NOT NULL UNIQUE,
    name TEXT NOT NULL,
    surname TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS professors (
    id SERIAL PRIMARY KEY NOT NULL,
    personal_id VARCHAR(15) NOT NULL UNIQUE,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    qualification TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS student_course (
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id)
);
CREATE TABLE IF NOT EXISTS activities (
    id SERIAL PRIMARY KEY NOT NULL,
    professor_id INT NOT NULL,
    course_id INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (professor_id) REFERENCES professors(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
