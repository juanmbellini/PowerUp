CREATE SEQUENCE user_follow_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE user_follow (
  id          INTEGER                                NOT NULL,
  follower_id INTEGER                                NOT NULL,
  followed_id INTEGER                                NOT NULL,
  date        TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);