
-- Creation of entity tables
CREATE TABLE IF NOT EXISTS games (
  id                          INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name                        VARCHAR(1024),
  summary                     VARCHAR(1024),
  avg_score                   REAL,
  release                     DATE,
  cover_picture_cloudinary_id VARCHAR(1024),
  counter                     INTEGER          NOT NULL
);
CREATE TABLE IF NOT EXISTS genres (
  id   INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(1024)
);
CREATE TABLE IF NOT EXISTS platforms (
  id   INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(1024)
);
CREATE TABLE IF NOT EXISTS companies (
  id   INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(1024)
);
CREATE TABLE IF NOT EXISTS users (
  id              INTEGER IDENTITY     NOT NULL PRIMARY KEY,
  email           VARCHAR(1024)        NOT NULL,
  username        VARCHAR(1024) DEFAULT NULL,
  hashed_password VARCHAR(1024)        NOT NULL,
  enabled         BOOLEAN DEFAULT TRUE NOT NULL,

  UNIQUE (email),
  UNIQUE (username)
);

-- Creation of relationship tables
CREATE TABLE IF NOT EXISTS game_genres (
  id       INTEGER IDENTITY NOT NULL PRIMARY KEY,
  game_id  INTEGER          NOT NULL,
  genre_id INTEGER          NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (genre_id) REFERENCES genres (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (game_id, genre_id)
);
CREATE TABLE IF NOT EXISTS game_platforms (
  id           INTEGER IDENTITY NOT NULL PRIMARY KEY,
  game_id      INTEGER          NOT NULL,
  platform_id  INTEGER          NOT NULL,
  release_date DATE             NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (platform_id) REFERENCES platforms (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (game_id, platform_id, release_date)  --The same game can be released for the same platform multiple times
);
CREATE TABLE IF NOT EXISTS keywords (
  id   INTEGER IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(1024)    NOT NULL
);
CREATE TABLE IF NOT EXISTS game_keywords (
  id         INTEGER IDENTITY NOT NULL PRIMARY KEY,
  game_id    INTEGER          NOT NULL,
  keyword_id INTEGER          NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (keyword_id) REFERENCES keywords (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS game_developers (
  id           INTEGER IDENTITY NOT NULL PRIMARY KEY,
  game_id      INTEGER          NOT NULL,
  developer_id INTEGER          NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (developer_id) REFERENCES companies (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (game_id, developer_id)
);
CREATE TABLE IF NOT EXISTS game_publishers (
  id           INTEGER IDENTITY NOT NULL PRIMARY KEY,
  game_id      INTEGER          NOT NULL,
  publisher_id INTEGER          NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (publisher_id) REFERENCES companies (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (game_id, publisher_id)
);
CREATE TABLE IF NOT EXISTS game_pictures (
  id            INTEGER IDENTITY NOT NULL PRIMARY KEY,
  cloudinary_id VARCHAR(1024)    NOT NULL,
  game_id       INTEGER          NOT NULL,
  width         INTEGER,
  height        INTEGER,

  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
);
CREATE TABLE IF NOT EXISTS game_scores (
  id      INTEGER IDENTITY NOT NULL PRIMARY KEY,
  user_id INTEGER          NOT NULL,
  game_id INTEGER          NOT NULL,
  score   INTEGER          NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (user_id, game_id)
);
CREATE TABLE IF NOT EXISTS game_play_statuses (
  id      INTEGER IDENTITY NOT NULL PRIMARY KEY,
  user_id INTEGER          NOT NULL,
  game_id INTEGER          NOT NULL,
  status  VARCHAR(1024)    NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (user_id, game_id)
);
CREATE TABLE IF NOT EXISTS user_authorities (
  id        INTEGER IDENTITY NOT NULL PRIMARY KEY,
  username  VARCHAR(1024)    NOT NULL,
  authority VARCHAR(1024)    NOT NULL,

  FOREIGN KEY (username) REFERENCES users (username)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (username, authority)
);
