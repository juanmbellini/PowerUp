-- Delete duplicate entries of game keywords, game publishers, etc. Also add unique indices to prevent duplicates from reappearing

-- Already constrained:
-- Game developers
-- Game genres
-- Game platforms (game and platform can be repeated, but only if it's on a different release date)
-- Game publishers

-- Game keywords
DELETE FROM game_keywords AS g1 WHERE
  EXISTS(SELECT id from game_keywords AS g2 WHERE g1.id < g2.id AND g1.game_id = g2.game_id AND g1.keyword_id = g2.keyword_id);
CREATE UNIQUE INDEX game_id_keyword_id ON game_keywords (game_id, keyword_id);

-- Game pictures
CREATE UNIQUE INDEX cloudinary_id ON game_pictures (cloudinary_id);

-- Game videos
DELETE FROM game_videos AS g1 WHERE
  EXISTS(SELECT id from game_videos AS g2 WHERE g1.id < g2.id AND g1.game_id = g2.game_id AND g1.video_id = g2.video_id);
CREATE UNIQUE INDEX game_id_video_id ON game_videos (game_id, video_id);
