CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_picture_url VARCHAR(512),
    encrypted_password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

CREATE TABLE courses (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         language VARCHAR(50),
                         level VARCHAR(50),
                         duration INT NOT NULL DEFAULT 0,
                         user_id INT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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



-- CREATE TABLE order_items (
--                              id INT AUTO_INCREMENT PRIMARY KEY,
--                              user_id INT NOT NULL,
--                              course_id INT NOT NULL,
--                              total_price NUMERIC(12,2) NOT NULL,
--                              access_expiry DATE,
--                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--                              FOREIGN KEY (course_id) REFERENCES courses(id)
-- );

--
-- CREATE TABLE cart_items (
--                             id INT AUTO_INCREMENT PRIMARY KEY,
--                             order_items_id INT NOT NULL,
--                             course_id INT NOT NULL,
--                             price NUMERIC(12,2) NOT NULL,
--                             purchase_date TIMESTAMP NOT NULL DEFAULT NOW(),
--                             FOREIGN KEY (order_items_id) REFERENCES order_items(id) ON DELETE CASCADE,
--                             FOREIGN KEY (course_id) REFERENCES courses(id)
-- );

CREATE TABLE orders (
                             id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             user_id INT NOT NULL,
                             order_status ENUM('PENDING', 'PROCESSING','PAYED') NOT NULL DEFAULT 'PENDING',
                             paid_date timestamp NULL DEFAULT NULL,
                             createdAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             KEY orders_FK (user_id),
                             CONSTRAINT orders_FK FOREIGN KEY (user_id) REFERENCES users (id)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE
);

CREATE TABLE order_items (
                                  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  order_id INT NOT NULL,
                                  course_id INT NOT NULL,
                                  base_price DECIMAL(19,2) NOT NULL,
                                  quantity int(11) NOT NULL,
                                  KEY order_items_order_FK (order_id),
                                  KEY order_items_course_FK (course_id),
                                  CONSTRAINT order_items_order_FK FOREIGN KEY (order_id) REFERENCES orders (id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                                  CONSTRAINT order_items_product_FK FOREIGN KEY (course_id) REFERENCES courses (id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
);
