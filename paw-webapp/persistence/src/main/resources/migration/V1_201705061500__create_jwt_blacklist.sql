CREATE TABLE jwt_blacklist(
  id INT PRIMARY KEY,
  token TEXT,
  valid_until TIMESTAMPTZ
);

CREATE SEQUENCE jwt_blacklist_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER TABLE ONLY jwt_blacklist ALTER COLUMN id SET DEFAULT nextval('jwt_blacklist_id_seq'::regclass);