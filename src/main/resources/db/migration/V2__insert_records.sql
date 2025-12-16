-- =============================================
--  V2__initial_data.sql
--  Initial seed data for users and courses
-- =============================================

-- ================
-- USERS
-- ================

INSERT INTO users (username, email, profile_picture_url, encrypted_password, created_at, role) VALUES
                                                                                             ('teacher_john', 'john@example.com', 'https://example.com/john.png', 'encrypted_pass_123', '2020-05-20', 'ADMIN'),
                                                                                             ('teacher_maria', 'maria@example.com', 'https://example.com/maria.png', 'encrypted_pass_456', '2020-07-30', 'ADMIN'),
                                                                                             ('student_anna', 'anna@example.com', NULL, 'encrypted_pass_789', '2021-07-29', 'USER'),
                                                                                             ('student_pedro', 'pedro@example.com', NULL, 'encrypted_pass_987', '2021-08-29','USER'),
                                                                                             ('teacher_lucas', 'lucas@example.com', NULL, 'pass_001', '2022-01-10', 'ADMIN'),
                                                                                             ('teacher_sara', 'sara@example.com', NULL, 'pass_002', '2022-01-15', 'ADMIN'),
                                                                                             ('teacher_claire', 'claire@example.com', NULL, 'pass_003', '2022-01-20', 'ADMIN'),
                                                                                             ('teacher_michael', 'michael@example.com', NULL, 'pass_004', '2022-02-01', 'ADMIN'),
                                                                                             ('teacher_julia', 'julia@example.com', NULL, 'pass_005', '2022-02-03', 'ADMIN'),
                                                                                             ('teacher_richard', 'richard@example.com', NULL, 'pass_006', '2022-02-05', 'ADMIN'),


                                                                                             ('student_luis', 'luis@example.com', NULL, 'pass_007', '2022-02-10', 'USER'),
                                                                                             ('student_marta', 'marta@example.com', NULL, 'pass_008', '2022-02-12', 'USER'),
                                                                                             ('student_nora', 'nora@example.com', NULL, 'pass_009', '2022-02-15', 'USER'),
                                                                                             ('student_carla', 'carla@example.com', NULL, 'pass_010', '2022-02-17', 'USER'),
                                                                                             ('student_tomas', 'tomas@example.com', NULL, 'pass_011', '2022-03-01', 'USER'),
                                                                                             ('student_javier', 'javier@example.com', NULL, 'pass_012', '2022-03-02', 'USER'),
                                                                                             ('student_sonia', 'sonia@example.com', NULL, 'pass_013', '2022-03-05', 'USER'),
                                                                                             ('student_andres', 'andres@example.com', NULL, 'pass_014', '2022-03-08', 'USER'),
                                                                                             ('student_claudia', 'claudia@example.com', NULL, 'pass_015', '2022-03-09', 'USER'),
                                                                                             ('student_raul', 'raul@example.com', NULL, 'pass_016', '2022-03-11', 'USER'),

                                                                                             ('teacher_elena', 'elena@example.com', NULL, 'pass_017', '2022-03-20', 'ADMIN'),
                                                                                             ('teacher_adrian', 'adrian@example.com', NULL, 'pass_018', '2022-03-25', 'ADMIN'),
                                                                                             ('teacher_brian', 'brian@example.com', NULL, 'pass_019', '2022-03-28', 'ADMIN'),
                                                                                             ('teacher_roberta', 'roberta@example.com', NULL, 'pass_020', '2022-04-05', 'ADMIN'),
                                                                                             ('teacher_omar', 'omar@example.com', NULL, 'pass_021', '2022-04-09', 'ADMIN'),
                                                                                             ('teacher_ines', 'ines@example.com', NULL, 'pass_022', '2022-04-11', 'ADMIN'),
                                                                                             ('teacher_paula', 'paula@example.com', NULL, 'pass_023', '2022-04-15', 'ADMIN'),
                                                                                             ('teacher_felipe', 'felipe@example.com', NULL, 'pass_024', '2022-04-18', 'ADMIN'),
                                                                                             ('teacher_david', 'david@example.com', NULL, 'pass_025', '2022-04-20', 'ADMIN'),
                                                                                             ('teacher_lola', 'lola@example.com', NULL, 'pass_026', '2022-04-25', 'ADMIN');


-- ================
-- COURSES
-- ================

INSERT INTO courses (title, description, price, language, level, user_id) VALUES
                                                                              ('English for Beginners', 'Basic English course focused on daily vocabulary.', 29.99, 'English', 'Beginner', 1),
                                                                              ('Advanced Business English', 'Business English for professionals.', 59.99, 'English', 'Advanced', 1),
                                                                              ('Spanish for Travelers', 'Spanish phrases for travel.', 19.99, 'Spanish', 'Beginner', 2),
                                                                              ('French Intermediate Conversation', 'Improve your French conversational skills.', 39.99, 'French', 'Intermediate', 2),
                                                                              ('German Basics A1', 'Learn German from scratch.', 24.99, 'German', 'Beginner', 5),
                                                                              ('German Intermediate B1', 'Improve grammar and vocabulary.', 34.99, 'German', 'Intermediate', 5),
                                                                              ('Portuguese Essentials', 'Useful phrases and pronunciation.', 22.99, 'Portuguese', 'Beginner', 6),
                                                                              ('Italian for Beginners', 'Start speaking Italian today.', 25.99, 'Italian', 'Beginner', 6),
                                                                              ('Japanese Hiragana', 'Learn Japanese writing system.', 27.99, 'Japanese', 'Beginner', 7),
                                                                              ('Japanese Vocabulary Boost', 'Improve your Japanese words and expressions.', 31.99, 'Japanese', 'Intermediate', 7),

                                                                              ('Korean Hangul 101', 'Learn Hangul alphabet.', 20.99, 'Korean', 'Beginner', 8),
                                                                              ('Korean Conversation Practice', 'Speak with confidence.', 35.99, 'Korean', 'Intermediate', 8),
                                                                              ('Chinese Mandarin Basics', 'Learn tones and essential words.', 28.99, 'Chinese', 'Beginner', 9),
                                                                              ('Chinese Intermediate', 'Improve Mandarin fluency.', 37.99, 'Chinese', 'Intermediate', 9),
                                                                              ('Russian Alphabet & Basics', 'Master Cyrillic and basic sentences.', 23.99, 'Russian', 'Beginner', 10),
                                                                              ('Russian Conversation', 'Enhance fluency through dialogues.', 33.99, 'Russian', 'Intermediate', 10),
                                                                              ('Arabic Alphabet 1', 'Learn Arabic script.', 22.99, 'Arabic', 'Beginner', 21),
                                                                              ('Arabic Vocabulary', 'Expand your Arabic vocabulary.', 29.99, 'Arabic', 'Intermediate', 21),
                                                                              ('Dutch for Beginners', 'Start speaking Dutch today.', 24.99, 'Dutch', 'Beginner', 22),
                                                                              ('Dutch Intermediate', 'Improve your Dutch skills.', 32.99, 'Dutch', 'Intermediate', 22),

                                                                              ('Swedish Basics', 'Essential Swedish skills.', 26.99, 'Swedish', 'Beginner', 23),
                                                                              ('Swedish Conversation', 'Speak Swedish confidently.', 38.99, 'Swedish', 'Intermediate', 23),
                                                                              ('Norwegian Essentials', 'Basic Norwegian course.', 25.99, 'Norwegian', 'Beginner', 24),
                                                                              ('Norwegian Conversation', 'Improve speaking fluency.', 34.99, 'Norwegian', 'Intermediate', 24),
                                                                              ('Polish for Beginners', 'Learn basic Polish phrases.', 21.99, 'Polish', 'Beginner', 25),
                                                                              ('Polish Grammar Basics', 'Intro to Polish grammar.', 28.99, 'Polish', 'Intermediate', 25),
                                                                              ('Greek Basics', 'Learn Greek alphabet & essentials.', 26.99, 'Greek', 'Beginner', 26),
                                                                              ('Greek Conversation', 'Improve spoken Greek.', 35.99, 'Greek', 'Intermediate', 26),
                                                                              ('Japanese Basics', 'Learn Japanese alphabet & essentials.', 16.99, 'Japanese', 'Beginner', 30),
                                                                              ('Japanese Conversation', 'Improve spoken Japanese.', 25.99, 'Japanese', 'Intermediate', 30);

insert into languages (code, name) values
                                        ('en', 'English'),
                                        ('es', 'Spanish'),
                                        ('fr', 'French'),
                                        ('de', 'German'),
                                        ('pt', 'Portuguese'),
                                        ('it', 'Italian'),
                                        ('ja', 'Japanese'),
                                        ('ko', 'Korean'),
                                        ('zh', 'Chinese'),
                                        ('ru', 'Russian'),
                                        ('ar', 'Arabic'),
                                        ('nl', 'Dutch'),
                                        ('sv', 'Swedish'),
                                        ('no', 'Norwegian'),
                                        ('pl', 'Polish'),
                                        ('gr', 'Greek');

-- ================
-- LEVELS (CEFR + Custom levels)
-- ================
insert into levels (name) values
                              ('A1'),
                              ('A2'),
                              ('B1'),
                              ('B2'),
                              ('C1'),
                              ('C2'),
                              ('BASIC'),
                              ('INTERMEDIATE'),
                              ('ADVANCED');

