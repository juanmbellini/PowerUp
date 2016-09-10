CREATE SCHEMA IF NOT EXISTS powerUp;
DROP TABLE IF EXISTS powerUp.games CASCADE;
DROP TABLE IF EXISTS powerUp.genres CASCADE;
DROP TABLE IF EXISTS powerUp.platforms CASCADE;
DROP TABLE IF EXISTS powerUp.genresGames CASCADE;
DROP TABLE IF EXISTS powerUp.platformsGames CASCADE;
DROP TABLE IF EXISTS powerUp.keywords CASCADE;
-- DROP TABLE IF EXISTS powerUp.ratings CASCADE;
DROP TABLE IF EXISTS powerUp.developersGames CASCADE;
DROP TABLE IF EXISTS powerUp.publishersGames CASCADE;
DROP TABLE IF EXISTS powerUp.companies CASCADE;

CREATE TABLE IF NOT EXISTS powerUp.games(
	id 		  	serial not null primary key,
	name 		varchar,
	summary		text,
	avgScore 	real,
	release 	date,
);

CREATE TABLE IF NOT EXISTS powerUp.genres(
	id 	 	serial not null primary key,
	name	varchar,
);

CREATE TABLE IF NOT EXISTS powerUp.platforms(
	id 		serial not null primary key,
	name	varchar,
);

CREATE TABLE IF NOT EXISTS powerUp.gameGenres (
	id 		serial not null primary key
	gameId	integer not null,
	genreId integer not null,

	FOREIGN KEY (gameId) REFERENCES powerUp.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genreId) REFERENCES powerUp.genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,genreId)
);

CREATE TABLE IF NOT EXISTS powerUp.platformsGames (
	id 	 		serial not null primary key
	gameId		integer not null,
	platformId 	integer not null,

	FOREIGN KEY (gameId) REFERENCES powerUp.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (platformId) REFERENCES powerUp.platforms (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,platformId)
);

CREATE TABLE IF NOT EXISTS powerUp.keywords (
	id 		serial not null primary key
	gameId	integer not null,
	name 	varchar,

	FOREIGN KEY (gameId) REFERENCES powerUp.games (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- CREATE TABLE IF NOT EXISTS powerUp.ratings(
	
	-- gameId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- userId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- rating integer,
	-- FOREIGN KEY( )gameId REFERENCES powerUp.games(id),
	-- FOREIGN KEY( )userId REFERENCES powerUp.users(id)
-- );

CREATE TABLE IF NOT EXISTS powerUp.companies (
	id 		serial not null primary key,
	name 	varchar,
);

CREATE TABLE IF NOT EXISTS powerUp.developersGames (
	id 			serial not null primary key,
	gameId		integer not null,
	developerId integer not null,

	FOREIGN KEY (gameId) REFERENCES powerUp.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (developerId) REFERENCES powerUp.companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,developerId)
);

CREATE TABLE IF NOT EXISTS powerUp.publishersGames (
	id 			serial not null primary key,
	gameId		integer not null,
	publisherId integer not null,

	FOREIGN KEY (gameId) REFERENCES powerUp.games (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (publisherId) REFERENCES powerUp.companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,publisherId)
);
