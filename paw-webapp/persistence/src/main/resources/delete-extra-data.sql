--Incomplete games
DELETE FROM power_up.games
WHERE id IN
      (
        SELECT DISTINCT (id)
        FROM
          (
            --Games without developers
            SELECT DISTINCT (id)
            FROM power_up.games AS games
            WHERE NOT EXISTS(SELECT *
                             FROM power_up.game_developers
                             WHERE game_id = games.id)

            UNION

            --Games without publishers
            SELECT DISTINCT (id)
            FROM power_up.games AS games
            WHERE NOT EXISTS(SELECT *
                             FROM power_up.game_publishers
                             WHERE game_id = games.id)

            UNION

            --Games without summary
            SELECT DISTINCT (id)
            FROM power_up.games
            WHERE summary IS NULL
          ) AS a1
      );

--Unused companies
DELETE FROM power_up.companies
WHERE id IN
      (
        SELECT DISTINCT (id)
        FROM power_up.companies AS companies
        WHERE NOT EXISTS
        (SELECT *
         FROM power_up.game_developers
         WHERE developer_id = companies.id)

        UNION

        SELECT DISTINCT (id)
        FROM power_up.companies AS companies
        WHERE NOT EXISTS
        (SELECT *
         FROM power_up.game_publishers
         WHERE publisher_id = companies.id)
      );

--Unused platforms
DELETE FROM power_up.platforms
WHERE id IN
      (
        SELECT DISTINCT (id)
        FROM power_up.platforms AS platforms
        WHERE NOT EXISTS
        (SELECT *
         FROM power_up.game_platforms
         WHERE platform_id = platforms.id)
      );