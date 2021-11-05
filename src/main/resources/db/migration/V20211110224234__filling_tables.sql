INSERT INTO user_role (id, role_name)
VALUES (1, 'admin'),
       (2, 'user');

INSERT INTO users (id, last_name, first_name, login, password, email, mobile_phone, balance)
VALUES (1, 'Александр', 'Степан', 'stepanow.a@mail.ru', '1111', 'stepanow.a@mail.ru', '8684758965', 235),
       (2, 'Виктор', 'Александр', 'alecsandrow.a@mail.ru', '2222', 'alecsandrow.a@mail.ru','985214', 25);

INSERT INTO periodical_edition (id, price, periodical_edition_description,title, periodical_edition_type,periodicity)
VALUES (1, 20, 'very good', 'The Guardian', 'MAGAZINE', 'WEEKLY'),
       (2, 30, 'good', 'The NY Times', 'NEWSPAPER', 'MONTHLY');

INSERT INTO  periodical_edition_image (id, image_path, periodical_edition_id)
VALUES (1, '455fdv', 1),
       (2, 'dfmdk', 2);

INSERT INTO review (id, user_comment,rating,user_id, periodical_edition_id)
VALUES (1,'good',5,1,1),
       (2,'bad', 4,2,2);



