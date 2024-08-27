insert into USERS ("email", "login", "name", "birthday")
values('mail@mail.com', 'user', 'name', '2000-01-20');

insert into USERS ("email", "login", "name", "birthday")
values('gmail@mail.com', 'user2', 'name2', '2001-01-20');

insert into MPA_RATING ("mpa_name")
values('PG');

insert into MPA_RATING ("mpa_name")
values('PG-13');

insert into FILMS ("name","description","release_date","duration","rating")
values('film','description','2012-01-20',10,1);

insert into GENRES ("genre_name")
values('action');

insert into GENRES ("genre_name")
values('horror');


insert into FILM_GENRES ("film_id", "genre")
values(1,2);

insert into FILMS ("name","description","release_date","duration","rating")
values('film2','description2','2012-01-21',20,2);

insert into FILM_GENRES ("film_id", "genre")
values(2,1);


insert into FILM_LIKES ("film_id", "user_id") values(1, 1);
insert into FILM_LIKES ("film_id", "user_id") values(2, 1);
insert into FILM_LIKES ("film_id", "user_id") values(2, 2);
insert into FRIENDS ("user_id", "friend_id") values(1, 2);

insert into USERS ("email", "login", "name", "birthday")
values('umail@mail.com', 'user3', 'name3', '2004-01-20');

insert into FRIENDS ("user_id", "friend_id") values(3, 2);
insert into FRIENDS ("user_id", "friend_id") values(1, 3);


