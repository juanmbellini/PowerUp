\set ON_ERROR_STOP on -- Makes psql return error code if something went wrong

BEGIN;

CREATE SCHEMA IF NOT EXISTS power_up;

-- Drop of relationship tables
DROP TABLE IF EXISTS power_up.game_genres;
DROP TABLE IF EXISTS power_up.game_consoles;
DROP TABLE IF EXISTS power_up.game_keywords;
DROP TABLE IF EXISTS power_up.game_developers;
DROP TABLE IF EXISTS power_up.game_publishers;

-- Drop of entities tables
DROP TABLE IF EXISTS power_up.games;
DROP TABLE IF EXISTS power_up.genres;
DROP TABLE IF EXISTS power_up.consoles;
DROP TABLE IF EXISTS power_up.companies;

-- DROP TABLE IF EXISTS power_up.ratings CASCADE;



-- Creation of entities tables
CREATE TABLE IF NOT EXISTS power_up.games(
	id 		  	serial not null primary key,
	name 		varchar,
	summary		text,
	avg_score 	real,
	release 	date
);
CREATE TABLE IF NOT EXISTS power_up.genres(
	id 	 	serial not null primary key,
	name	varchar
);
CREATE TABLE IF NOT EXISTS power_up.consoles(
	id 		serial not null primary key,
	name	varchar
);
CREATE TABLE IF NOT EXISTS power_up.companies (
	id 		serial not null primary key,
	name 	varchar
);

-- Creation of relationships tables
CREATE TABLE IF NOT EXISTS power_up.game_genres (
	id 		serial not null primary key,
	game_id	integer not null,
	genre_id integer not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES power_up.genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,genre_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_consoles (
	id 	 		serial not null primary key,
	game_id		integer not null,
	console_id	integer not null,
	release_date date not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (console_id) REFERENCES power_up.consoles (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,console_id, release_date)	--The same game can be released for the same console multiple times (i.e. remaster)
);
CREATE TABLE IF NOT EXISTS power_up.game_keywords (
	id 		serial not null primary key,
	game_id	integer not null,
	name 	varchar,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS power_up.game_developers (
	id 			serial not null primary key,
	game_id		integer not null,
	developer_id integer not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (developer_id) REFERENCES power_up.companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,developer_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_publishers (
	id 			serial not null primary key,
	game_id		integer not null,
	publisher_id integer not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (publisher_id) REFERENCES power_up.companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,publisher_id)
);

-- CREATE TABLE IF NOT EXISTS power_up.ratings(
	
	-- game_id integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- userId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- rating integer,
	-- FOREIGN KEY( )game_id REFERENCES power_up.games(id),
	-- FOREIGN KEY( )userId REFERENCES power_up.users(id)
-- );

COMMIT;
