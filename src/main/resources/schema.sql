CREATE TABLE IF NOT EXISTS films (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"description" varchar(200) NOT NULL,
	"release_date" date NOT NULL,
	"duration" bigint NOT NULL,
	"rating" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS users (
	"id" serial NOT NULL UNIQUE,
	"email" varchar(255) NOT NULL UNIQUE,
	"login" bigint NOT NULL,
	"name" bigint,
	"birthday" date NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS film_genres (
	"film_id" serial NOT NULL,
	"genre" bigint NOT NULL,
	PRIMARY KEY ("film_id", "genre")
);

CREATE TABLE IF NOT EXISTS genres (
	"genre_id" serial NOT NULL primary key auto_increment,
	"genre_name" varchar(255) NOT NULL UNIQUE,
	PRIMARY KEY ("genre_id")
);

CREATE TABLE IF NOT EXISTS mpa_rating (
	"rating_id" serial NOT NULL primary key auto_increment,
	"mpa_name" bigint NOT NULL UNIQUE,
	PRIMARY KEY ("rating_id")
);

CREATE TABLE IF NOT EXISTS friends (
	"user_id" bigint NOT NULL,
	"friend_id" bigint NOT NULL,
	PRIMARY KEY ("user_id", "friend_id")
);

CREATE TABLE IF NOT EXISTS film_likes (
	"film_id" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	PRIMARY KEY ("film_id")
);

ALTER TABLE films ADD CONSTRAINT IF NOT EXISTS "films_fk5" FOREIGN KEY ("rating") REFERENCES mpa_rating("rating_id");


ALTER TABLE film_genres ADD CONSTRAINT IF NOT EXISTS "film_genres_fk0" FOREIGN KEY ("film_id") REFERENCES films("id");

ALTER TABLE film_genres ADD CONSTRAINT IF NOT EXISTS "film_genres_fk1" FOREIGN KEY ("genre") REFERENCES genres("genre_id");


ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS "friends_fk0" FOREIGN KEY ("user_id") REFERENCES users("id");

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS "friends_fk1" FOREIGN KEY ("friend_id") REFERENCES users("id");
ALTER TABLE film_likes ADD CONSTRAINT IF NOT EXISTS "film_likes_fk0" FOREIGN KEY ("film_id") REFERENCES films("id");

ALTER TABLE film_likes ADD CONSTRAINT IF NOT EXISTS "film_likes_fk1" FOREIGN KEY ("user_id") REFERENCES users("id");