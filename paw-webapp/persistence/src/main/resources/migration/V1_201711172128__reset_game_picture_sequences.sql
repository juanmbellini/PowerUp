-- Reset the starting number of the sequence for game picture IDs. The table has gaps in IDs and its sequence
-- is out of sync (ie. it's at a lower number than the maximum assigned ID), so new insertions cause primary
-- key violations. Set the starting number to the max ID, plus one.

-- Restart number was obtained with `SELECT MAX(id)+1 FROM ...`

ALTER SEQUENCE game_pictures_id_seq RESTART WITH 12516;
