--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.4
-- Dumped by pg_dump version 9.5.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: comment_likes; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE comment_likes (
    id integer NOT NULL,
    user_id integer NOT NULL,
    comment_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE comment_likes OWNER TO "paw-2016b-02";

--
-- Name: comment_likes_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE comment_likes_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE comment_likes_id_seq OWNER TO "paw-2016b-02";

--
-- Name: comment_likes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE comment_likes_id_seq OWNED BY comment_likes.id;


--
-- Name: comments; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE comments (
    id integer NOT NULL,
    user_id integer NOT NULL,
    comment character varying NOT NULL,
    thread_id integer NOT NULL,
    parent_comment_id integer,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE comments OWNER TO "paw-2016b-02";

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE comments_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE comments_id_seq OWNER TO "paw-2016b-02";

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE comments_id_seq OWNED BY comments.id;


--
-- Name: companies; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE companies (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE companies OWNER TO "paw-2016b-02";

--
-- Name: companies_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE companies_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE companies_id_seq OWNER TO "paw-2016b-02";

--
-- Name: companies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE companies_id_seq OWNED BY companies.id;


--
-- Name: game_developers; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_developers (
    id integer NOT NULL,
    game_id integer NOT NULL,
    developer_id integer NOT NULL
);


ALTER TABLE game_developers OWNER TO "paw-2016b-02";

--
-- Name: game_developers_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_developers_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_developers_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_developers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_developers_id_seq OWNED BY game_developers.id;


--
-- Name: game_genres; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_genres (
    id integer NOT NULL,
    game_id integer NOT NULL,
    genre_id integer NOT NULL
);


ALTER TABLE game_genres OWNER TO "paw-2016b-02";

--
-- Name: game_genres_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_genres_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_genres_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_genres_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_genres_id_seq OWNED BY game_genres.id;


--
-- Name: game_keywords; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_keywords (
    id integer NOT NULL,
    game_id integer NOT NULL,
    keyword_id integer NOT NULL
);


ALTER TABLE game_keywords OWNER TO "paw-2016b-02";

--
-- Name: game_keywords_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_keywords_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_keywords_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_keywords_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_keywords_id_seq OWNED BY game_keywords.id;


--
-- Name: game_pictures; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_pictures (
    id integer NOT NULL,
    cloudinary_id character varying NOT NULL,
    game_id integer NOT NULL,
    width integer,
    height integer
);


ALTER TABLE game_pictures OWNER TO "paw-2016b-02";

--
-- Name: game_pictures_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_pictures_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_pictures_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_pictures_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_pictures_id_seq OWNED BY game_pictures.id;


--
-- Name: game_platforms; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_platforms (
    id integer NOT NULL,
    game_id integer NOT NULL,
    platform_id integer NOT NULL,
    release_date date NOT NULL
);


ALTER TABLE game_platforms OWNER TO "paw-2016b-02";

--
-- Name: game_platforms_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_platforms_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_platforms_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_platforms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_platforms_id_seq OWNED BY game_platforms.id;


--
-- Name: game_play_statuses; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_play_statuses (
    id integer NOT NULL,
    user_id integer NOT NULL,
    game_id integer NOT NULL,
    status text NOT NULL
);


ALTER TABLE game_play_statuses OWNER TO "paw-2016b-02";

--
-- Name: game_play_statuses_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_play_statuses_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_play_statuses_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_play_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_play_statuses_id_seq OWNED BY game_play_statuses.id;


--
-- Name: game_publishers; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_publishers (
    id integer NOT NULL,
    game_id integer NOT NULL,
    publisher_id integer NOT NULL
);


ALTER TABLE game_publishers OWNER TO "paw-2016b-02";

--
-- Name: game_publishers_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_publishers_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_publishers_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_publishers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_publishers_id_seq OWNED BY game_publishers.id;


--
-- Name: game_scores; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_scores (
    id integer NOT NULL,
    user_id integer NOT NULL,
    game_id integer NOT NULL,
    score integer NOT NULL
);


ALTER TABLE game_scores OWNER TO "paw-2016b-02";

--
-- Name: game_scores_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_scores_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_scores_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_scores_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_scores_id_seq OWNED BY game_scores.id;


--
-- Name: game_videos; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE game_videos (
    id integer NOT NULL,
    name character varying NOT NULL,
    video_id character varying NOT NULL,
    game_id integer NOT NULL
);


ALTER TABLE game_videos OWNER TO "paw-2016b-02";

--
-- Name: game_videos_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE game_videos_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE game_videos_id_seq OWNER TO "paw-2016b-02";

--
-- Name: game_videos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE game_videos_id_seq OWNED BY game_videos.id;


--
-- Name: games; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE games (
    id integer NOT NULL,
    name character varying,
    summary text,
    avg_score real,
    release date,
    cover_picture_cloudinary_id character varying,
    counter integer DEFAULT 0 NOT NULL
);


ALTER TABLE games OWNER TO "paw-2016b-02";

--
-- Name: games_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE games_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE games_id_seq OWNER TO "paw-2016b-02";

--
-- Name: games_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE games_id_seq OWNED BY games.id;


--
-- Name: genres; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE genres (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE genres OWNER TO "paw-2016b-02";

--
-- Name: genres_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE genres_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE genres_id_seq OWNER TO "paw-2016b-02";

--
-- Name: genres_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE genres_id_seq OWNED BY genres.id;


--
-- Name: keywords; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE keywords (
    id integer NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE keywords OWNER TO "paw-2016b-02";

--
-- Name: keywords_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE keywords_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE keywords_id_seq OWNER TO "paw-2016b-02";

--
-- Name: keywords_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE keywords_id_seq OWNED BY keywords.id;


--
-- Name: platforms; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE platforms (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE platforms OWNER TO "paw-2016b-02";

--
-- Name: platforms_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE platforms_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE platforms_id_seq OWNER TO "paw-2016b-02";

--
-- Name: platforms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE platforms_id_seq OWNED BY platforms.id;


--
-- Name: reviews; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE reviews (
    id integer NOT NULL,
    game_id integer NOT NULL,
    user_id integer NOT NULL,
    review text NOT NULL,
    story_score integer,
    graphics_score integer,
    audio_score integer,
    controls_score integer,
    fun_score integer,
    date date
);


ALTER TABLE reviews OWNER TO "paw-2016b-02";

--
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE reviews_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE reviews_id_seq OWNER TO "paw-2016b-02";

--
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE reviews_id_seq OWNED BY reviews.id;


--
-- Name: shelf_games; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE shelf_games (
    id integer NOT NULL,
    shelf_id integer NOT NULL,
    game_id integer NOT NULL
);


ALTER TABLE shelf_games OWNER TO "paw-2016b-02";

--
-- Name: shelf_games_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE shelf_games_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE shelf_games_id_seq OWNER TO "paw-2016b-02";

--
-- Name: shelf_games_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE shelf_games_id_seq OWNED BY shelf_games.id;


--
-- Name: shelves; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE shelves (
    id integer NOT NULL,
    name character varying NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE shelves OWNER TO "paw-2016b-02";

--
-- Name: shelves_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE shelves_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE shelves_id_seq OWNER TO "paw-2016b-02";

--
-- Name: shelves_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE shelves_id_seq OWNED BY shelves.id;


--
-- Name: spring_session; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE spring_session (
    session_id character(36) NOT NULL,
    creation_time bigint NOT NULL,
    last_access_time bigint NOT NULL,
    max_inactive_interval integer NOT NULL,
    principal_name character varying(100)
);


ALTER TABLE spring_session OWNER TO "paw-2016b-02";

--
-- Name: spring_session_attributes; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE spring_session_attributes (
    session_id character(36) NOT NULL,
    attribute_name character varying(200) NOT NULL,
    attribute_bytes bytea
);


ALTER TABLE spring_session_attributes OWNER TO "paw-2016b-02";

--
-- Name: thread_likes; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE thread_likes (
    id integer NOT NULL,
    user_id integer NOT NULL,
    thread_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE thread_likes OWNER TO "paw-2016b-02";

--
-- Name: thread_likes_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE thread_likes_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE thread_likes_id_seq OWNER TO "paw-2016b-02";

--
-- Name: thread_likes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE thread_likes_id_seq OWNED BY thread_likes.id;


--
-- Name: threads; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE threads (
    id integer NOT NULL,
    title character varying NOT NULL,
    user_id integer NOT NULL,
    initial_comment character varying DEFAULT ''::character varying NOT NULL,
    hot_value real DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE threads OWNER TO "paw-2016b-02";

--
-- Name: threads_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE threads_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE threads_id_seq OWNER TO "paw-2016b-02";

--
-- Name: threads_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE threads_id_seq OWNED BY threads.id;


--
-- Name: user_authorities; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE user_authorities (
    id integer NOT NULL,
    username character varying NOT NULL,
    authority character varying NOT NULL
);


ALTER TABLE user_authorities OWNER TO "paw-2016b-02";

--
-- Name: user_authorities_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE user_authorities_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE user_authorities_id_seq OWNER TO "paw-2016b-02";

--
-- Name: user_authorities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE user_authorities_id_seq OWNED BY user_authorities.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: paw-2016b-02
--

CREATE TABLE users (
    id integer NOT NULL,
    email character varying NOT NULL,
    username character varying,
    hashed_password character varying NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    profile_picture bytea,
    mime_type character varying
);


ALTER TABLE users OWNER TO "paw-2016b-02";

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: paw-2016b-02
--

CREATE SEQUENCE users_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE users_id_seq OWNER TO "paw-2016b-02";

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw-2016b-02
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comment_likes ALTER COLUMN id SET DEFAULT nextval('comment_likes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comments ALTER COLUMN id SET DEFAULT nextval('comments_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY companies ALTER COLUMN id SET DEFAULT nextval('companies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_developers ALTER COLUMN id SET DEFAULT nextval('game_developers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_genres ALTER COLUMN id SET DEFAULT nextval('game_genres_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_keywords ALTER COLUMN id SET DEFAULT nextval('game_keywords_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_pictures ALTER COLUMN id SET DEFAULT nextval('game_pictures_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_platforms ALTER COLUMN id SET DEFAULT nextval('game_platforms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_play_statuses ALTER COLUMN id SET DEFAULT nextval('game_play_statuses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_publishers ALTER COLUMN id SET DEFAULT nextval('game_publishers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_scores ALTER COLUMN id SET DEFAULT nextval('game_scores_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_videos ALTER COLUMN id SET DEFAULT nextval('game_videos_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY games ALTER COLUMN id SET DEFAULT nextval('games_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY genres ALTER COLUMN id SET DEFAULT nextval('genres_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY keywords ALTER COLUMN id SET DEFAULT nextval('keywords_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY platforms ALTER COLUMN id SET DEFAULT nextval('platforms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY reviews ALTER COLUMN id SET DEFAULT nextval('reviews_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelf_games ALTER COLUMN id SET DEFAULT nextval('shelf_games_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelves ALTER COLUMN id SET DEFAULT nextval('shelves_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY thread_likes ALTER COLUMN id SET DEFAULT nextval('thread_likes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY threads ALTER COLUMN id SET DEFAULT nextval('threads_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY user_authorities ALTER COLUMN id SET DEFAULT nextval('user_authorities_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: comment_likes_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comment_likes
    ADD CONSTRAINT comment_likes_pkey PRIMARY KEY (id);


--
-- Name: comments_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: companies_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);


--
-- Name: game_developers_game_id_developer_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_developers
    ADD CONSTRAINT game_developers_game_id_developer_id_key UNIQUE (game_id, developer_id);


--
-- Name: game_developers_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_developers
    ADD CONSTRAINT game_developers_pkey PRIMARY KEY (id);


--
-- Name: game_genres_game_id_genre_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_genres
    ADD CONSTRAINT game_genres_game_id_genre_id_key UNIQUE (game_id, genre_id);


--
-- Name: game_genres_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_genres
    ADD CONSTRAINT game_genres_pkey PRIMARY KEY (id);


--
-- Name: game_keywords_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_keywords
    ADD CONSTRAINT game_keywords_pkey PRIMARY KEY (id);


--
-- Name: game_pictures_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_pictures
    ADD CONSTRAINT game_pictures_pkey PRIMARY KEY (id);


--
-- Name: game_platforms_game_id_platform_id_release_date_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_platforms
    ADD CONSTRAINT game_platforms_game_id_platform_id_release_date_key UNIQUE (game_id, platform_id, release_date);


--
-- Name: game_platforms_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_platforms
    ADD CONSTRAINT game_platforms_pkey PRIMARY KEY (id);


--
-- Name: game_play_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_play_statuses
    ADD CONSTRAINT game_play_statuses_pkey PRIMARY KEY (id);


--
-- Name: game_play_statuses_user_id_game_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_play_statuses
    ADD CONSTRAINT game_play_statuses_user_id_game_id_key UNIQUE (user_id, game_id);


--
-- Name: game_publishers_game_id_publisher_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_publishers
    ADD CONSTRAINT game_publishers_game_id_publisher_id_key UNIQUE (game_id, publisher_id);


--
-- Name: game_publishers_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_publishers
    ADD CONSTRAINT game_publishers_pkey PRIMARY KEY (id);


--
-- Name: game_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_scores
    ADD CONSTRAINT game_scores_pkey PRIMARY KEY (id);


--
-- Name: game_scores_user_id_game_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_scores
    ADD CONSTRAINT game_scores_user_id_game_id_key UNIQUE (user_id, game_id);


--
-- Name: game_videos_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_videos
    ADD CONSTRAINT game_videos_pkey PRIMARY KEY (id);


--
-- Name: games_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY games
    ADD CONSTRAINT games_pkey PRIMARY KEY (id);


--
-- Name: genres_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY genres
    ADD CONSTRAINT genres_pkey PRIMARY KEY (id);


--
-- Name: keywords_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY keywords
    ADD CONSTRAINT keywords_pkey PRIMARY KEY (id);


--
-- Name: platforms_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY platforms
    ADD CONSTRAINT platforms_pkey PRIMARY KEY (id);


--
-- Name: reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- Name: reviews_user_id_game_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY reviews
    ADD CONSTRAINT reviews_user_id_game_id_key UNIQUE (user_id, game_id);


--
-- Name: shelf_games_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelf_games
    ADD CONSTRAINT shelf_games_pkey PRIMARY KEY (id);


--
-- Name: shelf_games_shelf_id_game_id_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelf_games
    ADD CONSTRAINT shelf_games_shelf_id_game_id_key UNIQUE (shelf_id, game_id);


--
-- Name: shelves_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelves
    ADD CONSTRAINT shelves_pkey PRIMARY KEY (id);


--
-- Name: spring_session_attributes_pk; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_id, attribute_name);


--
-- Name: spring_session_pk; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY spring_session
    ADD CONSTRAINT spring_session_pk PRIMARY KEY (session_id);


--
-- Name: thread_likes_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY thread_likes
    ADD CONSTRAINT thread_likes_pkey PRIMARY KEY (id);


--
-- Name: threads_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY threads
    ADD CONSTRAINT threads_pkey PRIMARY KEY (id);


--
-- Name: user_authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY user_authorities
    ADD CONSTRAINT user_authorities_pkey PRIMARY KEY (id);


--
-- Name: user_authorities_username_authority_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY user_authorities
    ADD CONSTRAINT user_authorities_username_authority_key UNIQUE (username, authority);


--
-- Name: users_email_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_username_key; Type: CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: spring_session_attributes_ix1; Type: INDEX; Schema: public; Owner: paw-2016b-02
--

CREATE INDEX spring_session_attributes_ix1 ON spring_session_attributes USING btree (session_id);


--
-- Name: spring_session_ix1; Type: INDEX; Schema: public; Owner: paw-2016b-02
--

CREATE INDEX spring_session_ix1 ON spring_session USING btree (last_access_time);


--
-- Name: comment_likes_comment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comment_likes
    ADD CONSTRAINT comment_likes_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES comments(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comment_likes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comment_likes
    ADD CONSTRAINT comment_likes_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comments_parent_comment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_parent_comment_id_fkey FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comments_thread_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_thread_id_fkey FOREIGN KEY (thread_id) REFERENCES threads(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comments_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_developers_developer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_developers
    ADD CONSTRAINT game_developers_developer_id_fkey FOREIGN KEY (developer_id) REFERENCES companies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_developers_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_developers
    ADD CONSTRAINT game_developers_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_genres_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_genres
    ADD CONSTRAINT game_genres_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_genres_genre_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_genres
    ADD CONSTRAINT game_genres_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES genres(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_keywords_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_keywords
    ADD CONSTRAINT game_keywords_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_keywords_keyword_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_keywords
    ADD CONSTRAINT game_keywords_keyword_id_fkey FOREIGN KEY (keyword_id) REFERENCES keywords(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_pictures_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_pictures
    ADD CONSTRAINT game_pictures_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_platforms_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_platforms
    ADD CONSTRAINT game_platforms_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_platforms_platform_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_platforms
    ADD CONSTRAINT game_platforms_platform_id_fkey FOREIGN KEY (platform_id) REFERENCES platforms(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_play_statuses_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_play_statuses
    ADD CONSTRAINT game_play_statuses_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_play_statuses_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_play_statuses
    ADD CONSTRAINT game_play_statuses_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_publishers_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_publishers
    ADD CONSTRAINT game_publishers_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_publishers_publisher_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_publishers
    ADD CONSTRAINT game_publishers_publisher_id_fkey FOREIGN KEY (publisher_id) REFERENCES companies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_scores_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_scores
    ADD CONSTRAINT game_scores_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_scores_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_scores
    ADD CONSTRAINT game_scores_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: game_videos_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY game_videos
    ADD CONSTRAINT game_videos_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: reviews_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY reviews
    ADD CONSTRAINT reviews_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: reviews_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY reviews
    ADD CONSTRAINT reviews_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: shelf_games_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelf_games
    ADD CONSTRAINT shelf_games_game_id_fkey FOREIGN KEY (game_id) REFERENCES games(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: shelf_games_shelf_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelf_games
    ADD CONSTRAINT shelf_games_shelf_id_fkey FOREIGN KEY (shelf_id) REFERENCES shelves(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: shelves_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY shelves
    ADD CONSTRAINT shelves_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: spring_session_attributes_fk; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_id) REFERENCES spring_session(session_id) ON DELETE CASCADE;


--
-- Name: thread_likes_thread_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY thread_likes
    ADD CONSTRAINT thread_likes_thread_id_fkey FOREIGN KEY (thread_id) REFERENCES threads(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: thread_likes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY thread_likes
    ADD CONSTRAINT thread_likes_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: threads_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY threads
    ADD CONSTRAINT threads_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_authorities_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw-2016b-02
--

ALTER TABLE ONLY user_authorities
    ADD CONSTRAINT user_authorities_username_fkey FOREIGN KEY (username) REFERENCES users(username);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

