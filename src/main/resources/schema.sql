CREATE TABLE IF NOT EXISTS courses (
        id SERIAL PRIMARY KEY NOT NULL,
        name VARCHAR(15) NOT NULL UNIQUE,
        description TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY NOT NULL,
        personal_id VARCHAR(15) NOT NULL UNIQUE,
        role TEXT NOT NULL,
        name TEXT NOT NULL,
        surname TEXT NOT NULL,
        about TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS user_course (
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
        UNIQUE (user_id, course_id)
);
CREATE TABLE IF NOT EXISTS activities (
        id SERIAL PRIMARY KEY NOT NULL,
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        start_time TIMESTAMP NOT NULL,
        end_time TIMESTAMP NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);


