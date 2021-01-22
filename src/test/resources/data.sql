INSERT INTO courses(name, description) VALUES('name', 'description');
INSERT INTO courses(name, description) VALUES('name2', 'description2');
INSERT INTO professors(personal_id, name, surname, qualification) VALUES('1', 'name', 'surname', 'qualification');
INSERT INTO professors(personal_id, name, surname, qualification) VALUES('2', 'name2', 'surname2', 'qualification2');
INSERT INTO students(personal_id, name, surname) VALUES('1', 'name', 'surname');
INSERT INTO students(personal_id, name, surname) VALUES('2', 'name2', 'surname2');
INSERT INTO student_course(student_id, course_id) VALUES(1, 1);
INSERT INTO student_course(student_id, course_id) VALUES(2, 2);
INSERT INTO activities(professor_id, course_id, start_time, end_time) VALUES(1, 1, '1970-01-01 02:00:00.123', '1970-01-01 02:00:00.456'),
                                                                             (1, 1, '1970-01-01 02:00:00.789', '1970-01-01 02:00:00.101');
