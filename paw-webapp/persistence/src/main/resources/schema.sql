BEGIN;

-- Creation of entity tables
CREATE TABLE IF NOT EXISTS games (
  id                          SERIAL  NOT NULL PRIMARY KEY,
  name                        VARCHAR,
  summary                     TEXT,
  avg_score                   REAL DEFAULT 0, -- ALTER TABLE games ALTER avg_score SET DEFAULT 0
  release                     DATE,
  cover_picture_cloudinary_id VARCHAR,
  counter                     INTEGER DEFAULT 0 -- ALTER TABLE games ALTER counter drop not null, alter counter SET DEFAULT 0
);
CREATE TABLE IF NOT EXISTS genres (
  id   SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR
);
CREATE TABLE IF NOT EXISTS platforms (
  id   SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR
);
CREATE TABLE IF NOT EXISTS companies (
  id   SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR
);
CREATE TABLE IF NOT EXISTS keywords (
  id   SERIAL  NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
  id              SERIAL  NOT NULL PRIMARY KEY,
  email           VARCHAR NOT NULL,
  username        VARCHAR          DEFAULT NULL,
  hashed_password VARCHAR NOT NULL,
  enabled         BOOLEAN NOT NULL DEFAULT TRUE,
  profile_picture bytea, --ALTER TABLE users ADD profile_picture bytea
  mime_type       VARCHAR, --ALTER TABLE users ADD mime_type VARCHAR

  UNIQUE (email),
  UNIQUE (username)
);
CREATE TABLE IF NOT EXISTS reviews (
  id             SERIAL  NOT NULL PRIMARY KEY,
  game_id        INTEGER NOT NULL,
  user_id        INTEGER NOT NULL,
  review         TEXT    NOT NULL,
  story_score    INTEGER,
  graphics_score INTEGER,
  audio_score    INTEGER,
  controls_score INTEGER,
  fun_score      INTEGER,
  date           DATE,

  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (user_id, game_id)
);
CREATE TABLE IF NOT EXISTS shelves (
  id         SERIAL      NOT NULL PRIMARY KEY,
  name       VARCHAR     NOT NULL,
  user_id    INTEGER     NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  -- Other fields like whether this shelf is public?

  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE SEQUENCE IF NOT EXISTS shelves_id_seq;

CREATE TABLE IF NOT EXISTS threads(
  id              SERIAL NOT NULL PRIMARY KEY,
  title           VARCHAR NOT NULL,
  user_id         INTEGER NOT NULL,
  initial_comment VARCHAR NOT NULL DEFAULT '',
  hot_value       REAL NOT NULL DEFAULT 0, -- alter table threads add hot_value REAL NOT NULL DEFAULT 0
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE SEQUENCE IF NOT EXISTS threads_id_seq;

CREATE TABLE IF NOT EXISTS comments(
  id                SERIAL NOT NULL PRIMARY KEY,
  user_id           INTEGER NOT NULL,
  comment           VARCHAR NOT NULL,
  thread_id         INTEGER NOT NULL,
  parent_comment_id INTEGER NULL,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (thread_id) REFERENCES threads(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE SEQUENCE IF NOT EXISTS comments_id_seq;

-- Creation of relationship tables
CREATE TABLE IF NOT EXISTS game_genres (
  id       SERIAL  NOT NULL PRIMARY KEY,
  game_id  INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (game_id, genre_id)
);
CREATE TABLE IF NOT EXISTS game_platforms (
  id           SERIAL  NOT NULL PRIMARY KEY,
  game_id      INTEGER NOT NULL,
  platform_id  INTEGER NOT NULL,
  release_DATE DATE    NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (platform_id) REFERENCES platforms (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (game_id, platform_id, release_DATE)  --A game can be released for the same platform several times (i.e. remake)
);

CREATE TABLE IF NOT EXISTS game_keywords (
  id         SERIAL  NOT NULL PRIMARY KEY,
  game_id    INTEGER NOT NULL,
  keyword_id INTEGER NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (keyword_id) REFERENCES keywords (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS game_developers (
  id           SERIAL  NOT NULL PRIMARY KEY,
  game_id      INTEGER NOT NULL,
  developer_id INTEGER NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (developer_id) REFERENCES companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (game_id, developer_id)
);
CREATE TABLE IF NOT EXISTS game_publishers (
  id           SERIAL  NOT NULL PRIMARY KEY,
  game_id      INTEGER NOT NULL,
  publisher_id INTEGER NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (publisher_id) REFERENCES companies (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (game_id, publisher_id)
);
CREATE TABLE IF NOT EXISTS game_pictures (
  id            SERIAL  NOT NULL PRIMARY KEY,
  cloudinary_id VARCHAR NOT NULL,
  game_id       INTEGER NOT NULL,
  width         INTEGER,
  height        INTEGER,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS game_videos (
  id            SERIAL  NOT NULL PRIMARY KEY,
  name          VARCHAR NOT NULL,
  video_id      VARCHAR NOT NULL,
  game_id       INTEGER NOT NULL,

  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS game_scores (
  id      SERIAL  NOT NULL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  score   INTEGER NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (user_id, game_id)
);
CREATE TABLE IF NOT EXISTS game_play_statuses (
  id      SERIAL  NOT NULL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  status  TEXT    NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (user_id, game_id)
);
CREATE TABLE IF NOT EXISTS user_authorities (
  id        SERIAL  NOT NULL PRIMARY KEY,
  username  VARCHAR NOT NULL,
  authority VARCHAR NOT NULL,

  FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (username, authority)
);
CREATE TABLE IF NOT EXISTS shelf_games (
  id       SERIAL  NOT NULL PRIMARY KEY,
  shelf_id INTEGER NOT NULL,
  game_id  INTEGER NOT NULL,

  FOREIGN KEY (shelf_id) REFERENCES shelves (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE (shelf_id, game_id)
);

CREATE TABLE IF NOT EXISTS thread_likes(
  id          SERIAL NOT NULL PRIMARY KEY,
  user_id     INTEGER NOT NULL,
  thread_id   INTEGER NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (thread_id) REFERENCES threads(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE SEQUENCE IF NOT EXISTS thread_likes_id_seq;

CREATE TABLE IF NOT EXISTS comment_likes(
  id          SERIAL NOT NULL PRIMARY KEY,
  user_id     INTEGER NOT NULL,
  comment_id  INTEGER NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE SEQUENCE IF NOT EXISTS comment_likes_id_seq;

-- Spring session storage, adapted from org/springframework/session/jdbc/schema-h2.sql
CREATE TABLE IF NOT EXISTS SPRING_SESSION (
  SESSION_ID CHAR(36),
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (SESSION_ID)
);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (LAST_ACCESS_TIME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
  SESSION_ID CHAR(36),
  ATTRIBUTE_NAME VARCHAR(200),
  ATTRIBUTE_BYTES BYTEA,
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME),
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_ID) REFERENCES SPRING_SESSION(SESSION_ID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_ID);

COMMIT;
