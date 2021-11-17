INSERT INTO user_role (id, role_name)
VALUES (1, 'admin'),
       (2, 'user');

INSERT INTO users (id, last_name, first_name, login, password, email, mobile_phone, balance)
VALUES (1, 'Степанов', 'Александр', 'stepanow.a@mail.ru', '1111', 'stepanow.a@mail.ru', '8684758965', 235),
       (2, 'Александров', 'Виктор', 'alecsandrow.a@mail.ru', '2222', 'alecsandrow.a@mail.ru', '985214', 25),
       (3, 'Степанов', 'Степан', 'stepan.a@mail.ru', '3333', 'stepan.a@mail.ru', '9522585', 235),
       (4, 'Александров', 'Андрей', 'alecsandr.a@mail.ru', '4444', 'alecsandr.a@mail.ru', '7892152', 25);

INSERT INTO USER_ROLE_LINK (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (1, 2);

INSERT INTO periodical_edition (id, price, periodical_edition_description, title, periodical_edition_type, periodicity)
VALUES (1, 20, 'very good', 'The Guardian', 'MAGAZINE', 'WEEKLY'),
       (2, 30, 'good', 'The NY Times', 'NEWSPAPER', 'MONTHLY');

INSERT INTO periodical_edition_image (id, image_path, periodical_edition_id)
VALUES (1, 'D/im/cont', 1),
       (2, 'D/if/nok', 2),
       (3, 'A/im/cont', 1),
       (4, 'A/if/nok', 2),
       (5, 'B/im/cont', 1),
       (6, 'B/if/nok', 2);

INSERT INTO review (id, user_comment, rating, user_id, periodical_edition_id)
VALUES (1, 'good', 5, 1, 1),
       (2, 'bad', 4, 2, 2),
       (3, 'bad', 1, 3, 1),
       (4, 'bad', 2, 4, 2),
       (5, 'bad', 4, 1, 1),
       (6, 'bad', 3, 2, 2);

INSERT INTO subscription (id, price, payment_status, user_id)
VALUES (1, 28, 'PAID', 1),
       (2, 42, 'UNPAID', 1),
       (3, 41, 'PAID', 3),
       (4, 32, 'UNPAID', 4),
       (5, 42, 'PAID', 1),
       (6, 52, 'UNPAID', 2),
       (7, 72, 'PAID', 3),
       (8, 92, 'UNPAID', 4),
       (9, 2, 'PAID', 1),
       (10, 62, 'UNPAID', 2),
       (11, 48, 'PAID', 3);


INSERT INTO content (id, start_date, expiration_date, price, subscription_id, periodical_edition_id)
VALUES (1, '2021-06-05', '2021-07-05', 20, 1, 1),
       (2, '2021-07-06', '2021-08-06', 30, 2, 2),
       (3, '2021-08-07', '2021-09-07', 40, 3, 1),
       (4, '2021-09-08', '2021-10-08', 50, 4, 2);

COMMIT;