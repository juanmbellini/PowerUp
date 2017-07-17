ALTER TABLE user_follow OWNER TO "paw-2016b-02";

ALTER TABLE user_follow_seq OWNER TO "paw-2016b-02";

ALTER SEQUENCE user_follow_seq OWNED BY user_follow.id;

ALTER TABLE ONLY user_follow ALTER COLUMN id SET DEFAULT nextval('user_follow_seq'::regclass);

ALTER TABLE ONLY user_follow
    ADD CONSTRAINT user_follow_pkey PRIMARY KEY (id);

ALTER TABLE ONLY user_follow
    ADD CONSTRAINT user_follow_follower_id_followed_id_key UNIQUE (follower_id, followed_id);