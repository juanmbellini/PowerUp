\set ON_ERROR_STOP on
BEGIN;

CREATE SCHEMA IF NOT EXISTS power_up;

-- Drop of relationship tables
DROP TABLE IF EXISTS power_up.game_genres CASCADE;
DROP TABLE IF EXISTS power_up.game_platforms CASCADE;
DROP TABLE IF EXISTS power_up.game_keywords CASCADE;
DROP TABLE IF EXISTS power_up.game_developers CASCADE;
DROP TABLE IF EXISTS power_up.game_publishers CASCADE;

-- Drop of entity tables
DROP TABLE IF EXISTS power_up.games CASCADE;
DROP TABLE IF EXISTS power_up.genres CASCADE;
DROP TABLE IF EXISTS power_up.platforms CASCADE;
DROP TABLE IF EXISTS power_up.companies CASCADE;
DROP TABLE IF EXISTS power_up.keywords CASCADE;

--DROP TABLE IF EXISTS power_up.ratings;



-- Creation of entity tables
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
CREATE TABLE IF NOT EXISTS power_up.platforms(
	id 		serial not null primary key,
	name	varchar
);
CREATE TABLE IF NOT EXISTS power_up.companies (
	id 		serial not null primary key,
	name 	varchar
);

-- Creation of relationship tables
CREATE TABLE IF NOT EXISTS power_up.game_genres (
	id 		serial not null primary key,
	game_id	integer not null,
	genre_id integer not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES power_up.genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,genre_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_platforms (
	id 	 		serial not null primary key,
	game_id		integer not null,
	platform_id	integer not null,
	release_date date not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (platform_id) REFERENCES power_up.platforms (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,platform_id, release_date)	--The same game can be released for the same platform multiple times (i.e. remaster)
);
CREATE TABLE IF NOT EXISTS power_up.keywords (
	id 		serial not null primary key,
	name 	varchar not null
);
CREATE TABLE IF NOT EXISTS power_up.game_keywords (
	id 		serial not null primary key,
	game_id	integer not null,
	keyword_id 	integer not null,

	FOREIGN KEY (game_id) REFERENCES power_up.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (keyword_id) REFERENCES power_up.keywords (id) ON DELETE CASCADE ON UPDATE CASCADE
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
