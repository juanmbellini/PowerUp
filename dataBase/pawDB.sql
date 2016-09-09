CREATE SCHEMA powerUp
CREATE TABLE IF NOT EXISTS powerUp.games(
	id  	integer,
	name 	varchar(100),
	summary	varchar(3000),
	avgRate	real,
	release date,
	PRIMARY KEY id
);


CREATE TABLE IF NOT EXISTS powerUp.genres
(
	gameId	integer ON DELETE CASCADE ON UPDATE CASCADE,
	name varchar(100),
	FOREIGN KEY gameId REFERENCES powerUp.games (id)
);

CREATE TABLE IF NOT EXISTS powerUp.platforms
(
	gameId	integer ON DELETE CASCADE ON UPDATE CASCADE,
	name varchar(100),
	FOREIGN KEY gameId REFERENCES powerUp.games (id)
);

CREATE TABLE IF NOT EXISTS powerUp.keywords
(
	gameId	integer ON DELETE CASCADE ON UPDATE CASCADE,
	name varchar(100),
	FOREIGN KEY gameId REFERENCES powerUp.games (id)
);

-- CREATE TABLE IF NOT EXISTS powerUp.ratings(
	
	-- gameId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- userId integer ON DELETE CASCADE ON UPDATE CASCADE,
	-- rating integer,
	-- FOREIGN KEY gameId REFERENCES powerUp.games(id),
	-- FOREIGN KEY userId REFERENCES powerUp.users(id)
-- );

CREATE TABLE IF NOT EXISTS powerUp.developers
(
	gameId	integer ON DELETE CASCADE ON UPDATE CASCADE,
	developerId integer ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY gameId REFERENCES powerUp.games (id),
	FOREIGN KEY developerId REFERENCES powerUp.companies (id)
);

CREATE TABLE IF NOT EXISTS powerUp.publishers
(
	gameId	integer ON DELETE CASCADE ON UPDATE CASCADE,
	publisherId integer ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY gameId REFERENCES powerUp.games (id),
	FOREIGN KEY publisherId REFERENCES powerUp.companies (id)
);


CREATE TABLE IF NOT EXISTS powerUp.companies(
	id  	integer,
	name 	varchar(100),
	PRIMARY KEY id
);