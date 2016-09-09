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
	gameId  integer,
	name 	varchar,
	summary	varchar,
	avgScore real,
	release date,
	PRIMARY KEY (gameId)
);

CREATE TABLE IF NOT EXISTS powerUp.genres(
	genreId integer,
	name	varchar,
	PRIMARY KEY (genreId)
);
CREATE TABLE IF NOT EXISTS powerUp.platforms(
	platformId integer,
	name	varchar,
	PRIMARY KEY (platformId)
);

CREATE TABLE IF NOT EXISTS powerUp.genresGames
(
	gameId	integer,
	genreId integer,
	FOREIGN KEY (gameId) REFERENCES powerUp.games (gameId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genreId) REFERENCES powerUp.genres (genreId) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,genreId)
);

CREATE TABLE IF NOT EXISTS powerUp.platformsGames
(
	gameId	integer,
	platformId integer,
	FOREIGN KEY (gameId) REFERENCES powerUp.games (gameId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (platformId) REFERENCES powerUp.platforms (platformId) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,platformId)
);

CREATE TABLE IF NOT EXISTS powerUp.keywords
(
	gameId	integer,
	name varchar,
	FOREIGN KEY (gameId) REFERENCES powerUp.games (gameId) ON DELETE CASCADE ON UPDATE CASCADE
);

-- CREATE TABLE IF NOT EXISTS powerUp.ratings(
	
	-- gameId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- userId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- rating integer,
	-- FOREIGN KEY( )gameId REFERENCES powerUp.games(id),
	-- FOREIGN KEY( )userId REFERENCES powerUp.users(id)
-- );

CREATE TABLE IF NOT EXISTS powerUp.companies(
	companyId  	integer,
	name 	varchar,
	PRIMARY KEY (companyId)
);

CREATE TABLE IF NOT EXISTS powerUp.developersGames
(
	gameId	integer,
	developerId integer,
	FOREIGN KEY (gameId) REFERENCES powerUp.games (gameId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (developerId) REFERENCES powerUp.companies (companyId) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,developerId)
);

CREATE TABLE IF NOT EXISTS powerUp.publishersGames
(
	gameId	integer,
	publisherId integer,
	FOREIGN KEY (gameId) REFERENCES powerUp.games (gameId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (publisherId) REFERENCES powerUp.companies (companyId) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(gameId,publisherId)
);
