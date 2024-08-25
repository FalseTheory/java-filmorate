INSERT INTO USERS ("email", "login", "name", "birthday")
VALUES('mail@mail.com', 'user', 'name', '2000-01-20');

INSERT INTO USERS ("email", "login", "name", "birthday")
VALUES('gmail@mail.com', 'user2', 'name2', '2001-01-20');

INSERT INTO MPA_RATING ("mpa_name")
VALUES('PG');

INSERT INTO FILMS ("name","description","release_date","duration","rating")
VALUES('film','description','2012-01-20',10,1);

INSERT INTO GENRES ("genre_name")
VALUES('action');

INSERT INTO GENRES ("genre_name")
VALUES('horror');

INSERT INTO FILM_GENRES ("film_id", "genre")
VALUES(1,1);

INSERT INTO FILM_GENRES ("film_id", "genre")
VALUES(1,2);

INSERT INTO FILMS ("name","description","release_date","duration","rating")
VALUES('film2','description2','2012-01-21',20,1);

INSERT INTO FILM_GENRES ("film_id", "genre")
VALUES(2,1);

INSERT INTO MPA_RATING ("mpa_name")
VALUES('PG-13');


