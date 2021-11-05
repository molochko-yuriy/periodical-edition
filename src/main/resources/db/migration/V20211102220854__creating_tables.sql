CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    last_name    VARCHAR(64)  NOT NULL,
    first_name   VARCHAR(64)  NOT NULL,
    login        VARCHAR(32)  NOT NULL,
    password     VARCHAR(256) NOT NULL,
    email        VARCHAR(32)  NOT NULL,
    mobile_phone VARCHAR(16)  NOT NULL,
    balance      INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login),
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS user_role
(
    id        BIGINT      NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(16) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_role_unique UNIQUE (role_name)
);

CREATE TABLE IF NOT EXISTS user_role_link
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT user_role_link_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT user_rile_link_role
        FOREIGN KEY (role_id)
            REFERENCES user_role (id)
);

CREATE TABLE IF NOT EXISTS subscription
(
    id             BIGINT                  NOT NULL AUTO_INCREMENT,
    price          INT                     NOT NULL,
    payment_status ENUM ('PAID', 'UNPAID') NOT NULL,
    user_id        BIGINT                  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_subscription_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS periodical_edition
(
    id                             BIGINT                                  NOT NULL AUTO_INCREMENT,
    periodical_edition_type        ENUM ('NEWSPAPER', 'MAGAZINE')          NOT NULL,
    price                          INT                                     NOT NULL,
    periodicity                    ENUM ('WEEKLY', 'MONTHLY', 'QUARTERLY') NOT NULL,
    periodical_edition_description VARCHAR(1024)                           NOT NULL,
    title                          VARCHAR(64)                             NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS review
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    user_comment          VARCHAR(256) NOT NULL,
    rating                INT          NULL,
    user_id               BIGINT       NOT NULL,
    periodical_edition_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT rating_users_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT rating_periodical_edition_fk
        FOREIGN KEY (periodical_edition_id)
            REFERENCES periodical_edition (id)
);

CREATE TABLE IF NOT EXISTS periodical_edition_image
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    image_path            VARCHAR(512) NOT NULL,
    periodical_edition_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT periodical_edition_periodical_edition_image_fk
        FOREIGN KEY (periodical_edition_id)
            REFERENCES periodical_edition (id)
);

CREATE TABLE IF NOT EXISTS content
(
    id                    INT    NOT NULL AUTO_INCREMENT,
    start_date            DATE   NOT NULL,
    expiration_date       DATE   NOT NULL,
    price                 INT    NOT NULL,
    subscription_id       BIGINT NOT NULL,
    periodical_edition_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT content_subscription_fk
        FOREIGN KEY (subscription_id)
            REFERENCES subscription (id),
    CONSTRAINT content_periodical_edition
        FOREIGN KEY (periodical_edition_id)
            REFERENCES periodical_edition (id)
);

COMMIT;