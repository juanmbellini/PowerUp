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
DROP TABLE IF EXISTS power_up.game_pictures CASCADE;

--DROP TABLE IF EXISTS power_up.ratings;



-- Creation of entity tables
CREATE TABLE IF NOT EXISTS power_up.games(
  id 		  	SERIAL NOT NULL PRIMARY KEY,
  name 		  VARCHAR,
  summary		TEXT,
  avg_score REAL,
  release 	DATE
);
CREATE TABLE IF NOT EXISTS power_up.genres(
  id 	 	SERIAL NOT NULL PRIMARY KEY,
  name	VARCHAR
);
CREATE TABLE IF NOT EXISTS power_up.platforms(
  id 		SERIAL NOT NULL PRIMARY KEY,
  name	VARCHAR
);
CREATE TABLE IF NOT EXISTS power_up.companies (
  id 		SERIAL NOT NULL PRIMARY KEY,
  name 	VARCHAR
);

-- Creation of relationship tables
CREATE TABLE IF NOT EXISTS power_up.game_genres (
  id 		    SERIAL NOT NULL PRIMARY KEY,
  game_id	  INTEGER NOT NULL,
  genre_id  INTEGER NOT NULL,

  FOREIGN KEY (game_id)   REFERENCES power_up.games (id)  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (genre_id)  REFERENCES power_up.genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE(game_id,genre_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_platforms (
  id 	 		      SERIAL NOT NULL PRIMARY KEY,
  game_id		    INTEGER NOT NULL,
  platform_id	  INTEGER NOT NULL,
  release_DATE  DATE NOT NULL,

  FOREIGN KEY (game_id)     REFERENCES power_up.games (id)     ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (platform_id) REFERENCES power_up.platforms (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE(game_id,platform_id, release_DATE)	--A game can be released for the same platform several times (i.e. remake)
);
CREATE TABLE IF NOT EXISTS power_up.keywords (
  id 		SERIAL NOT NULL PRIMARY KEY,
  name 	VARCHAR NOT NULL
);
CREATE TABLE IF NOT EXISTS power_up.game_keywords (
  id 		      SERIAL NOT NULL PRIMARY KEY,
  game_id	    INTEGER NOT NULL,
  keyword_id 	INTEGER NOT NULL,

  FOREIGN KEY (game_id)     REFERENCES power_up.games (id)    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (keyword_id)  REFERENCES power_up.keywords (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS power_up.game_developers (
  id 			      SERIAL NOT NULL PRIMARY KEY,
  game_id		    INTEGER NOT NULL,
  developer_id  INTEGER NOT NULL,

  FOREIGN KEY (game_id)       REFERENCES power_up.games (id)      ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (developer_id)  REFERENCES power_up.companies (id)  ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE(game_id,developer_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_publishers (
  id 			      SERIAL NOT NULL PRIMARY KEY,
  game_id		    INTEGER NOT NULL,
  publisher_id  INTEGER NOT NULL,

  FOREIGN KEY (game_id)       REFERENCES power_up.games (id)      ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (publisher_id)  REFERENCES power_up.companies (id)  ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE(game_id,publisher_id)
);
CREATE TABLE IF NOT EXISTS power_up.game_pictures(
  id            SERIAL NOT NULL PRIMARY KEY,
  cloudinary_id VARCHAR NOT NULL,
  game_id       INTEGER NOT NULL,
  width         INTEGER,
  height        INTEGER,

  FOREIGN KEY (game_id) REFERENCES power_up.games(id)
);
COMMIT;
