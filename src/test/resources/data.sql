INSERT INTO courses(name, description) VALUES('name', 'description');
INSERT INTO courses(name, description) VALUES('name2', 'description2');
INSERT INTO users(personal_id, role, name, surname, about) VALUES('1', 'student','name', 'surname', 'about');
INSERT INTO users(personal_id, role, name, surname, about) VALUES('2', 'professor', 'name2', 'surname2', 'about2');
INSERT INTO user_course(user_id, course_id) VALUES(1, 1);
INSERT INTO user_course(user_id, course_id) VALUES(2, 2);
INSERT INTO events(user_id, course_id, start_time, end_time) VALUES(1, 1, '2012-01-01 02:00:00.123', '2012-01-01 02:00:00.456'),
                                                                             (1, 1, '2012-01-01 02:00:00.789', '2012-01-01 02:00:00.101');
