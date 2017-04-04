DELETE FROM games WHERE id IN (
    SELECT DISTINCT id FROM games WHERE (
        NOT EXISTS (SELECT * FROM game_developers WHERE games.id = game_id)
        AND NOT EXISTS (SELECT * FROM game_publishers WHERE games.id = game_id)
        AND NOT EXISTS (SELECT * FROM game_platforms WHERE games.id = game_id)
        AND summary IS NULL
    )
)
