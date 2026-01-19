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
INSERT INTO courses (title, description, price, language, level, duration, user_id) VALUES
                                                                                        -- English courses (5 total)
                                                                                        ('English for Beginners', 'Basic English course focused on daily vocabulary.', 29.99, 'English', 'A2', 40, 1),
                                                                                        ('Advanced Business English', 'Business English for professionals.', 59.99, 'English', 'Advanced', 60, 1),
                                                                                        ('English Conversation Practice', 'Improve your speaking fluency.', 44.99, 'English', 'B2', 50, 1),
                                                                                        ('English Grammar Masterclass', 'Complete grammar course for all levels.', 49.99, 'English', 'Intermediate', 55, 1),
                                                                                        ('IELTS Preparation Course', 'Prepare for IELTS exam with confidence.', 79.99, 'English', 'C2', 70, 1),

                                                                                        -- Spanish courses (5 total)
                                                                                        ('Spanish for Travelers', 'Spanish phrases for travel.', 19.99, 'Spanish', 'Beginner', 30, 2),
                                                                                        ('Spanish Grammar Mastery', 'Master Spanish grammar rules.', 39.99, 'Spanish', 'Intermediate', 48, 2),
                                                                                        ('Advanced Spanish Literature', 'Explore Spanish classic literature.', 54.99, 'Spanish', 'C2', 55, 2),
                                                                                        ('Spanish Medical Terminology', 'Spanish for healthcare professionals.', 64.99, 'Spanish', 'Advanced', 45, 2),
                                                                                        ('Spanish for Kids', 'Fun Spanish learning for children.', 24.99, 'Spanish', 'A1', 25, 2),

                                                                                        -- French courses (5 total)
                                                                                        ('French Basics A1', 'Start learning French from zero.', 24.99, 'French', 'Beginner', 35, 2),
                                                                                        ('French Intermediate Conversation', 'Improve your French conversational skills.', 39.99, 'French', 'Intermediate', 50, 2),
                                                                                        ('French Pronunciation Workshop', 'Perfect your French accent.', 34.99, 'French', 'B1', 28, 2),
                                                                                        ('French for Tourism', 'French language for hospitality industry.', 44.99, 'French', 'Intermediate', 42, 2),
                                                                                        ('Advanced French Writing', 'Master formal and creative French writing.', 59.99, 'French', 'C1', 58, 2),

                                                                                        -- German courses (5 total)
                                                                                        ('German Basics A1', 'Learn German from scratch.', 24.99, 'German', 'Beginner', 35, 5),
                                                                                        ('German Intermediate B1', 'Improve grammar and vocabulary.', 34.99, 'German', 'Intermediate', 45, 5),
                                                                                        ('German for Engineers', 'Technical German for engineering professionals.', 69.99, 'German', 'C2', 60, 5),
                                                                                        ('German Culture & Language', 'Learn German through cultural immersion.', 44.99, 'German', 'C1', 48, 5),
                                                                                        ('TestDaF Preparation', 'Prepare for German proficiency test.', 74.99, 'German', 'Advanced', 65, 5),

                                                                                        -- Portuguese courses (4 total)
                                                                                        ('Portuguese Essentials', 'Useful phrases and pronunciation.', 22.99, 'Portuguese', 'Beginner', 32, 6),
                                                                                        ('Portuguese for Business', 'Business Portuguese communication.', 42.99, 'Portuguese', 'Advanced', 48, 6),
                                                                                        ('Brazilian Portuguese vs European', 'Understand the differences.', 36.99, 'Portuguese', 'Intermediate', 40, 6),
                                                                                        ('Portuguese Music & Language', 'Learn through Brazilian music.', 29.99, 'Portuguese', 'Beginner', 35, 6),

                                                                                        -- Italian courses (5 total)
                                                                                        ('Italian for Beginners', 'Start speaking Italian today.', 25.99, 'Italian', 'Beginner', 38, 6),
                                                                                        ('Italian Culture & Language', 'Learn Italian through culture.', 49.99, 'Italian', 'B2', 52, 6),
                                                                                        ('Italian Cooking & Language', 'Learn Italian while cooking.', 54.99, 'Italian', 'Beginner', 40, 6),
                                                                                        ('Advanced Italian Grammar', 'Master complex Italian structures.', 44.99, 'Italian', 'Advanced', 50, 6),
                                                                                        ('Italian for Opera Lovers', 'Understand opera in original language.', 59.99, 'Italian', 'Intermediate', 45, 6),

                                                                                        -- Japanese courses (6 total)
                                                                                        ('Japanese Hiragana & Katakana', 'Learn Japanese writing systems.', 27.99, 'Japanese', 'Beginner', 42, 7),
                                                                                        ('Japanese Vocabulary Boost', 'Improve your Japanese words and expressions.', 31.99, 'Japanese', 'B2', 48, 7),
                                                                                        ('Japanese Business Communication', 'Professional Japanese for work.', 64.99, 'Japanese', 'Advanced', 60, 7),
                                                                                        ('Japanese Kanji Mastery Level 1', 'Learn essential Kanji characters.', 39.99, 'Japanese', 'Beginner', 50, 7),
                                                                                        ('Japanese Anime & Manga Language', 'Learn through popular media.', 34.99, 'Japanese', 'Intermediate', 38, 7),
                                                                                        ('JLPT N3 Preparation', 'Prepare for Japanese proficiency test.', 69.99, 'Japanese', 'Intermediate', 65, 7),

                                                                                        -- Korean courses (5 total)
                                                                                        ('Korean Hangul 101', 'Learn Hangul alphabet.', 20.99, 'Korean', 'Beginner', 28, 8),
                                                                                        ('Korean Conversation Practice', 'Speak with confidence.', 35.99, 'Korean', 'Intermediate', 44, 8),
                                                                                        ('Korean K-Drama Language', 'Learn Korean through popular dramas.', 45.99, 'Korean', 'Intermediate', 50, 8),
                                                                                        ('Korean K-Pop Vocabulary', 'Learn Korean through K-Pop songs.', 29.99, 'Korean', 'Beginner', 32, 8),
                                                                                        ('TOPIK Preparation Course', 'Prepare for Korean proficiency exam.', 74.99, 'Korean', 'Advanced', 70, 8),

                                                                                        -- Chinese courses (5 total)
                                                                                        ('Chinese Mandarin Basics', 'Learn tones and essential words.', 28.99, 'Chinese', 'Beginner', 36, 9),
                                                                                        ('Chinese Intermediate', 'Improve Mandarin fluency.', 37.99, 'Chinese', 'Intermediate', 52, 9),
                                                                                        ('Chinese Business Language', 'Mandarin for business professionals.', 69.99, 'Chinese', 'Advanced', 65, 9),
                                                                                        ('Chinese Characters Writing', 'Master Chinese calligraphy basics.', 44.99, 'Chinese', 'Beginner', 45, 9),
                                                                                        ('HSK 4 Preparation', 'Prepare for HSK level 4 exam.', 64.99, 'Chinese', 'Intermediate', 60, 9),

                                                                                        -- Russian courses (4 total)
                                                                                        ('Russian Alphabet & Basics', 'Master Cyrillic and basic sentences.', 23.99, 'Russian', 'Beginner', 34, 10),
                                                                                        ('Russian Conversation', 'Enhance fluency through dialogues.', 33.99, 'Russian', 'Intermediate', 46, 10),
                                                                                        ('Russian Literature Reading', 'Read Russian classics in original.', 54.99, 'Russian', 'Advanced', 55, 10),
                                                                                        ('Russian for Travelers', 'Essential Russian for tourism.', 26.99, 'Russian', 'Beginner', 30, 10),

                                                                                        -- Arabic courses (4 total)
                                                                                        ('Arabic Alphabet & Writing', 'Learn Arabic script.', 22.99, 'Arabic', 'Beginner', 30, 21),
                                                                                        ('Arabic Vocabulary Builder', 'Expand your Arabic vocabulary.', 29.99, 'Arabic', 'Intermediate', 40, 21),
                                                                                        ('Modern Standard Arabic', 'Learn formal Arabic language.', 49.99, 'Arabic', 'Intermediate', 50, 21),
                                                                                        ('Quranic Arabic Basics', 'Understand classical Arabic texts.', 44.99, 'Arabic', 'Advanced', 48, 21),

                                                                                        -- Dutch courses (4 total)
                                                                                        ('Dutch for Beginners', 'Start speaking Dutch today.', 24.99, 'Dutch', 'Beginner', 35, 22),
                                                                                        ('Dutch Intermediate', 'Improve your Dutch skills.', 32.99, 'Dutch', 'Intermediate', 45, 22),
                                                                                        ('Dutch for Expats', 'Essential Dutch for living in Netherlands.', 39.99, 'Dutch', 'Beginner', 40, 22),
                                                                                        ('NT2 Exam Preparation', 'Prepare for Dutch proficiency exam.', 69.99, 'Dutch', 'Advanced', 65, 22),

                                                                                        -- Swedish courses (4 total)
                                                                                        ('Swedish Basics', 'Essential Swedish skills.', 26.99, 'Swedish', 'Beginner', 38, 23),
                                                                                        ('Swedish Conversation', 'Speak Swedish confidently.', 38.99, 'Swedish', 'Intermediate', 50, 23),
                                                                                        ('Swedish for Work', 'Professional Swedish communication.', 49.99, 'Swedish', 'Intermediate', 45, 23),
                                                                                        ('Swedish Culture & Society', 'Language through Swedish culture.', 44.99, 'Swedish', 'Advanced', 48, 23),

                                                                                        -- Norwegian courses (3 total)
                                                                                        ('Norwegian Essentials', 'Basic Norwegian course.', 25.99, 'Norwegian', 'Beginner', 36, 24),
                                                                                        ('Norwegian Conversation', 'Improve speaking fluency.', 34.99, 'Norwegian', 'Intermediate', 48, 24),
                                                                                        ('Norwegian for Business', 'Professional Norwegian language.', 54.99, 'Norwegian', 'Advanced', 52, 24),

                                                                                        -- Polish courses (3 total)
                                                                                        ('Polish for Beginners', 'Learn basic Polish phrases.', 21.99, 'Polish', 'Beginner', 32, 25),
                                                                                        ('Polish Grammar Basics', 'Intro to Polish grammar.', 28.99, 'Polish', 'Intermediate', 42, 25),
                                                                                        ('Polish Advanced Communication', 'Master complex Polish conversations.', 44.99, 'Polish', 'Advanced', 50, 25),

                                                                                        -- Greek courses (3 total)
                                                                                        ('Greek Basics', 'Learn Greek alphabet & essentials.', 26.99, 'Greek', 'Beginner', 38, 26),
                                                                                        ('Greek Conversation', 'Improve spoken Greek.', 35.99, 'Greek', 'Intermediate', 46, 26),
                                                                                        ('Ancient Greek for Beginners', 'Introduction to classical Greek.', 49.99, 'Greek', 'Advanced', 55, 26);

-- ================
-- LANGUAGES
-- ================
     INSERT INTO languages (code, name) VALUES
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
    ('el', 'Greek');

-- ================
-- LEVELS
-- ================
insert into levels (name) values
                              ('A1'),
                              ('A2'),
                              ('B1'),
                              ('B2'),
                              ('C1'),
                              ('C2'),
                              ('Beginner'),
                              ('Intermediate'),
                              ('Advanced');

