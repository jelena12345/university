CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL
);
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
CREATE TABLE IF NOT EXISTS student_course (
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id)
);
