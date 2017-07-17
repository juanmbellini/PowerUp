CREATE TABLE review_likes (
    id integer NOT NULL,
    user_id integer NOT NULL,
    review_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);

ALTER TABLE review_likes OWNER TO "paw-2016b-02";

CREATE SEQUENCE review_likes_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE review_likes_id_seq OWNER TO "paw-2016b-02";

ALTER SEQUENCE review_likes_id_seq OWNED BY review_likes.id;

