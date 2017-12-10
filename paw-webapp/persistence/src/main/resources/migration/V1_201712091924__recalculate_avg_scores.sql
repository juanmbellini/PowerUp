-- Reset avg_score on games based only on our reviews, discard IGDB's average score
UPDATE games SET avg_score = (SELECT COALESCE(AVG(score), 0) FROM game_scores WHERE game_scores.game_id = games.id);
