-- =============================================
--  V1__init.sql
--  Initial schema for Language Courses Platform
-- =============================================

-- ================
-- USERS & ROLES
-- ================

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_picture_url VARCHAR(512),
    encrypted_password VARCHAR(255) NOT NULL,
    created_at DATE,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

CREATE TABLE courses (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         language VARCHAR(50),
                         level VARCHAR(50),
                         user_id INT NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_courses_user ON courses (user_id);

CREATE TABLE user_courses (
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_courses_user ON user_courses(user_id);
CREATE INDEX idx_user_courses_course ON user_courses(course_id);
-- https://es.wikipedia.org/wiki/ISO_639-1
create table languages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(4) NOT NULL UNIQUE,
    name VARCHAR(15) NOT NULL UNIQUE
);

-- ================
-- LEVELS (CEFR + Custom)
-- ================
create table levels (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE sessions (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         token VARCHAR(255),
                         user_id INT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(id)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE

);

-- CREATE TABLE user_roles (
--                             user_id UUID NOT NULL,
--                             role_name VARCHAR(50) NOT NULL,
--                             PRIMARY KEY (user_id, role_name),
--                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--                             FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE
-- );

-- ================
-- COURSES
-- ================


-- ================
-- SHOPPING CART
-- ================

-- CREATE TABLE shopping_carts (
--                                 id UUID PRIMARY KEY,
--                                 student_id UUID NOT NULL UNIQUE,
--                                 total_price NUMERIC(12,2) DEFAULT 0,
--                                 last_updated TIMESTAMP DEFAULT NOW(),
--                                 FOREIGN KEY (student_id) REFERENCES users(id)
-- );
--
-- CREATE TABLE cart_items (
--                             id UUID PRIMARY KEY,
--                             shopping_cart_id UUID NOT NULL,
--                             course_id UUID NOT NULL,
--                             price NUMERIC(12,2) NOT NULL,
--                             FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts(id) ON DELETE CASCADE,
--                             FOREIGN KEY (course_id) REFERENCES courses(id)
-- );
--
-- -- ================
-- -- ORDERS
-- -- ================
--
-- CREATE TABLE orders (
--                         id UUID PRIMARY KEY,
--                         student_id UUID NOT NULL,
--                         total_paid NUMERIC(12,2) NOT NULL,
--                         status VARCHAR(50) NOT NULL,
--                         purchase_date TIMESTAMP NOT NULL DEFAULT NOW(),
--                         FOREIGN KEY (student_id) REFERENCES users(id)
-- );
--
-- CREATE TABLE order_items (
--                              id UUID PRIMARY KEY,
--                              order_id UUID NOT NULL,
--                              course_id UUID NOT NULL,
--                              paid_price NUMERIC(12,2) NOT NULL,
--                              access_expiry DATE,
--                              FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
--                              FOREIGN KEY (course_id) REFERENCES courses(id)
-- );

-- FIN DEL ARCHIVO
