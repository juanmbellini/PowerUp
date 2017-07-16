CREATE SEQUENCE user_follow_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE user_follow (
    id integer NOT NULL,
    follower_id integer NOT NULL,
    followed_id integer NOT NULL,
    date timestamp with time zone DEFAULT now() NOT NULL
);