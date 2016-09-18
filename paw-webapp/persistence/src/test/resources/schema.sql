CREATE SCHEMA IF NOT EXISTS power_up;

-- Creation of entity tables
CREATE TABLE IF NOT EXISTS power_up.games(
	id 		  	INTEGER IDENTITY NOT NULL PRIMARY KEY,
	name 		  VARCHAR,
	summary		TEXT,
	avg_score REAL,
	release 	DATE
);
CREATE TABLE IF NOT EXISTS power_up.genres(
	id 	 	INTEGER IDENTITY NOT NULL PRIMARY KEY,
	name	VARCHAR
);
CREATE TABLE IF NOT EXISTS power_up.platforms(
	id 		INTEGER IDENTITY NOT NULL PRIMARY KEY,
	name	VARCHAR
);
CREATE TABLE IF NOT EXISTS power_up.companies (
	id 		INTEGER IDENTITY NOT NULL PRIMARY KEY,
	name 	VARCHAR
);

-- Creation of relationship tables
CREATE TABLE IF NOT EXISTS power_up.game_genres (
	id 		    INTEGER IDENTITY NOT NULL PRIMARY KEY,
	game_id	  INTEGER NOT NULL,
	genre_id  INTEGER NOT NULL,

	FOREIGN KEY (game_id)   REFERENCES power_up.games (id)  ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genre_id)  REFERENCES power_up.genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,genre_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_platforms (
	id 	 		      INTEGER IDENTITY NOT NULL PRIMARY KEY,
	game_id		    INTEGER NOT NULL,
	platform_id	  INTEGER NOT NULL,
	release_date  DATE NOT NULL,

	FOREIGN KEY (game_id)     REFERENCES power_up.games (id)      ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (platform_id) REFERENCES power_up.platforms (id)  ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,platform_id, release_date)	--The same game can be released for the same platform multiple times
);
CREATE TABLE IF NOT EXISTS power_up.keywords (
	id 		INTEGER IDENTITY NOT NULL PRIMARY KEY,
	name 	VARCHAR NOT NULL
);
CREATE TABLE IF NOT EXISTS power_up.game_keywords (
	id 		      INTEGER IDENTITY NOT NULL PRIMARY KEY,
	game_id	    INTEGER NOT NULL,
	keyword_id 	INTEGER NOT NULL,

	FOREIGN KEY (game_id)     REFERENCES power_up.games (id)    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (keyword_id)  REFERENCES power_up.keywords (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS power_up.game_developers (
	id 			      INTEGER IDENTITY NOT NULL PRIMARY KEY,
	game_id		    INTEGER NOT NULL,
	developer_id  INTEGER NOT NULL,

	FOREIGN KEY (game_id)       REFERENCES power_up.games (id)      ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (developer_id)  REFERENCES power_up.companies (id)  ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,developer_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_publishers (
	id 			      INTEGER IDENTITY NOT NULL PRIMARY KEY,
	game_id		    INTEGER NOT NULL,
	publisher_id  INTEGER NOT NULL,

	FOREIGN KEY (game_id)      REFERENCES power_up.games (id)     ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (publisher_id) REFERENCES power_up.companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE(game_id,publisher_id)
);