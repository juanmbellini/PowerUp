//package ar.edu.itba.paw.webapp.persistence;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.function.Consumer;
//
///**
// * Class used to download data from <a href="igdb.com">IGDB.com</a> and generate SQL INSERT files.
// * <b>NOTE: </b> Do not use this class often. It has an API key which has a limited number of requests per day which
// * will get exceeded under heavy use (and this class makes heavy use of the API).
// *
// * @author Juan Li Puma
// */
//public class IgdbDownloader {
//    private static final String IGDB_API_BASE_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/",
//            IGDB_API_KEY = "yZykcOVSIsmshHJqdjCtRKXqIvHap1IC6HPjsnFzCYzfhEb7vv";
//    private static IgdbDownloader instance;
////    private static final DateTimeFormatter ISO8601Formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//
//    private final FileWriter gamesFile, genresFile, platformsFile, companiesFile, keywordsFile, gameGenresFile, gamePlatformsFile, gameKeywordsFile, gameDevelopersFile, gamePublishersFile;
//
//    private IgdbDownloader() {
//        try {
//            gamesFile = new FileWriter(new File("..//dataBase/games.sql"));
//            genresFile = new FileWriter(new File("..//dataBase/genres.sql"));
//            platformsFile = new FileWriter(new File("..//dataBase/platforms.sql"));
//            companiesFile = new FileWriter(new File("..//dataBase/companies.sql"));
//            keywordsFile = new FileWriter(new File("..//dataBase/keywords.sql"));
//            gameGenresFile = new FileWriter(new File("..//dataBase/gameGenres.sql"));
//            gamePlatformsFile = new FileWriter(new File("..//dataBase/gameplatforms.sql"));
//            gameKeywordsFile = new FileWriter(new File("..//dataBase/gameKeywords.sql"));
//            gameDevelopersFile = new FileWriter(new File("..//dataBase/gameDevelopers.sql"));
//            gamePublishersFile = new FileWriter(new File("..//dataBase/gamePublishers.sql"));
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    public static IgdbDownloader getInstance() {
//        if (instance == null) {
//            instance = new IgdbDownloader();
//        }
//        return instance;
//    }
//
//    public static void main(String[] args) {
//        IgdbDownloader me = getInstance();
//        me.downloadAllGenres();
//        System.out.println("------------------------------------------------------");
//        me.downloadAllCompanies();
//        System.out.println("------------------------------------------------------");
//        me.downloadAllPlatforms();
//        System.out.println("------------------------------------------------------");
//        me.downloadAllKeywords();
//        System.out.println("------------------------------------------------------");
//        me.downloadAllGames();
//    }
//
//    private void downloadAllKeywords() {
//        try {
//            paginate("keywords/?fields=*", new SqlConsumer(keywordsFile) {
//                @Override
//                public void accept(JSONObject keyword) {
//                    String query = "INSERT INTO keywords (\"id\", \"name\") VALUES (" + keyword.getInt("id") + ", '" + escapeQuotesToPostgres(keyword.getString("name")) + "');\n";
//                    try {
//                        System.out.print(query);
//                        this.fw.write(query);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (UnirestException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadAllGenres() {
//        try {
//            paginate("genres/?fields=*", new SqlConsumer(genresFile) {
//                @Override
//                public void accept(JSONObject genre) {
//                    String query = "INSERT INTO genres (\"id\", \"name\") VALUES (" + genre.getInt("id") + ", '" + escapeQuotesToPostgres(genre.getString("name")) + "');\n";
//                    try {
//                        System.out.print(query);
//                        this.fw.write(query);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (UnirestException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadAllPlatforms() {
//        try {
//            paginate("platforms/?fields=*", new SqlConsumer(platformsFile) {
//                @Override
//                public void accept(JSONObject platform) {
//                    String query = "INSERT INTO platforms (\"id\", \"name\") VALUES (" + platform.getInt("id") + ", '" + escapeQuotesToPostgres(platform.getString("name")) + "');\n";
//                    try {
//                        System.out.print(query);
//                        this.fw.write(query);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (UnirestException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadAllCompanies() {
//        try {
//            paginate("companies/?fields=*", new SqlConsumer(companiesFile) {
//                @Override
//                public void accept(JSONObject company) {
//                    String query = "INSERT INTO companies (\"id\", \"name\") VALUES (" + company.getInt("id") + ", '" + escapeQuotesToPostgres(company.getString("name")) + "');\n";
//                    try {
//                        System.out.print(query);
//                        this.fw.write(query);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (UnirestException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadAllGames() {
//        try {
//            gameGenresFile.write("BEGIN;\nINSERT INTO game_genres (\"game_id\", \"genre_id\") \n" +
//                    "SELECT val.game_id, val.id\n" +
//                    "FROM (\n" +
//                    "\tVALUES\n");
//            gameDevelopersFile.write("BEGIN;\nINSERT INTO game_developers (\"game_id\", \"developer_id\")\nSELECT val.game_id, val.id\n" +
//                    "FROM (\n" +
//                    "\tVALUES\n");
//            gamePublishersFile.write("BEGIN;\nINSERT INTO game_publishers (\"game_id\", \"publisher_id\")\nSELECT val.game_id, val.id\n" +
//                    "FROM (\n" +
//                    "\tVALUES\n");
//            gamePlatformsFile.write("BEGIN;\nINSERT INTO game_platforms (\"game_id\", \"platform_id\")\nSELECT val.game_id, val.id\n" +
//                    "FROM (\n" +
//                    "\tVALUES\n");
//            gameKeywordsFile.write("BEGIN;\nINSERT INTO game_keywords (\"game_id\", \"keyword_id\")\nSELECT val.game_id, val.id\n" +
//                    "FROM (\n" +
//                    "\tVALUES\n");
//            gamesFile.write("BEGIN;\n");
//            gamesFile.write("SET DATESTYLE TO ISO, YMD;\n");
//            gamesFile.write("\\encoding utf8;\n");
//            paginate("games/?fields=*&order=name%3Aasc", new SqlConsumer() {
//                @Override
//                public void accept(JSONObject game) {
//                    //Game
//                    String name = "'" + escapeQuotesToPostgres(game.getString("name")) + "'",
//                            summary = "null",
//                            dateStr = "null";
//                    if (game.has("summary")) {
//                        summary = "'" + escapeQuotesToPostgres(game.getString("summary")) + "'";
//                    }
//                    if (game.has("first_release_date")) {
//                        dateStr = "'" + getISODateString(game.getLong("first_release_date")) + "'";
//                    }
//                    String gameQuery = "INSERT INTO games VALUES (" + game.getInt("id") + ", " + name + ", " + summary + ", " + "0, " + dateStr + ");\n";
//                    StringBuilder genreQueries = new StringBuilder(),
//                            developerQueries = new StringBuilder(),
//                            publisherQueries = new StringBuilder(),
//                            platformQueries = new StringBuilder(),
//                            keywordQueries = new StringBuilder();
//                    //Genres
//                    if (game.has("genres")) {
//                        JSONArray genres = game.getJSONArray("genres");
//                        for (int i = 0; i < genres.length(); i++) {
//                            genreQueries.append("(").append(game.getInt("id")).append(", ").append(genres.getInt(i)).append("),\n");
//                        }
//                    }
//                    //Developers
//                    if (game.has("developers")) {
//                        JSONArray developers = game.getJSONArray("developers");
//                        for (int i = 0; i < developers.length(); i++) {
//                            developerQueries.append("(").append(game.getInt("id")).append(", ").append(developers.getInt(i)).append("),\n");
//                        }
//                    }
//                    //Publishers
//                    if (game.has("publishers")) {
//                        JSONArray publishers = game.getJSONArray("publishers");
//                        for (int i = 0; i < publishers.length(); i++) {
//                            publisherQueries.append("(").append(game.getInt("id")).append(", ").append(publishers.getInt(i)).append("),\n");
//                        }
//                    }
//                    //Platforms
//                    if(game.has("release_dates")) {
//                        JSONArray releaseDates = game.getJSONArray("release_dates");
//                        for (int i = 0; i < releaseDates.length(); i++) {
//                            JSONObject release = releaseDates.getJSONObject(i);
//                            if(release.has("platform") && release.has("date")) {
//                                platformQueries.append("(").append(game.getInt("id")).append(", ").append(release.getInt("platform")).append(", '").append(getISODateString(release.getLong("date"))).append("'),\n");
//                            }
//                        }
//                    }
//                    //Keywords
//                    if(game.has("keywords")) {
//                        JSONArray keywords = game.getJSONArray("keywords");
//                        for (int i = 0; i < keywords.length(); i++) {
//                            keywordQueries.append("(").append(game.getInt("id")).append(", ").append(keywords.getInt(i)).append("),\n");
//                        }
//                    }
//                    try {
//                        IgdbDownloader.this.gamesFile.write(gameQuery);
//                        System.out.println(gameQuery);
//                        IgdbDownloader.this.gameGenresFile.write(genreQueries.toString());
//                        System.out.println(genreQueries.toString());
//                        IgdbDownloader.this.gameDevelopersFile.write(developerQueries.toString());
//                        System.out.println(developerQueries.toString());
//                        IgdbDownloader.this.gamePublishersFile.write(publisherQueries.toString());
//                        System.out.println(publisherQueries.toString());
//                        IgdbDownloader.this.gameKeywordsFile.write(keywordQueries.toString());
//                        System.out.println(keywordQueries.toString());
//                        IgdbDownloader.this.gamePlatformsFile.write(platformQueries.toString());
//                        System.out.println(platformQueries.toString());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            gamesFile.write("COMMIT;\n");
//            gamesFile.close();
//            gameKeywordsFile.write(") AS val (game_id, id)\n" +
//                    "INNER JOIN games ON val.game_id = games.id\n" +
//                    "INNER JOIN keywords ON val.id = keywords.id\n" +
//                    "ON CONFLICT DO NOTHING;\n" +
//                    "\n" +
//                    "COMMIT;\n");
//            gameKeywordsFile.close();
//            gamePlatformsFile.write(") AS val (game_id, id)\n" +
//                    "INNER JOIN games ON val.game_id = games.id\n" +
//                    "INNER JOIN platforms ON val.id = platforms.id\n" +
//                    "ON CONFLICT DO NOTHING;\n" +
//                    "\n" +
//                    "COMMIT;\n");
//            gamePlatformsFile.close();
//            gameGenresFile.write(") AS val (game_id, id)\n" +
//                    "INNER JOIN games ON val.game_id = games.id\n" +
//                    "INNER JOIN genres ON val.id = genres.id\n" +
//                    "ON CONFLICT DO NOTHING;\n" +
//                    "\n" +
//                    "COMMIT;\n");
//            gameGenresFile.close();
//            gameDevelopersFile.write(") AS val (game_id, id)\n" +
//                    "INNER JOIN games ON val.game_id = games.id\n" +
//                    "INNER JOIN companies ON val.id = companies.id\n" +
//                    "ON CONFLICT DO NOTHING;\n" +
//                    "\n" +
//                    "COMMIT;\n");
//            gameDevelopersFile.close();
//            gamePublishersFile.write(") AS val (game_id, id)\n" +
//                    "INNER JOIN games ON val.game_id = games.id\n" +
//                    "INNER JOIN companies ON val.id = companies.id\n" +
//                    "ON CONFLICT DO NOTHING;\n" +
//                    "\n" +
//                    "COMMIT;\n");
//            gamePublishersFile.close();
//            System.out.println("WARNING: The SQL files for the relationship tables are invalid, they have a trailing comma on the last row. Go edit those before running the queries.");
//        } catch (UnirestException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * IGDB's free API access tier limits queries to 50 results per page, and offset + pageSize must be less that or
//     * equal to 10000. This function "paginates" through results until it receives an empty page.
//     *
//     * @param request  The paginateable request to perform numerous times.
//     * @param offset   Where, in the total result set, the current page should startTransaction.
//     * @param pageSize The page size. Max is 50.
//     * @param output   Consumer for the produced output.
//     * @throws UnirestException On API errors.
//     */
//    public void paginate(String request, int offset, int pageSize, SqlConsumer output) throws UnirestException {
//        boolean done = false;
//        do {
//            JSONArray page = api(request + "&limit=" + pageSize + "&offset=" + offset).getBody().getArray();
//            for (int i = 0; i < page.length(); i++) {
//                JSONObject entry = page.getJSONObject(i);
//                if (entry.has("Err")) {
//                    System.err.println(entry.getJSONObject("Err").getString("message"));
////                    throw new RuntimeException();
//                    return;
//                } else {
//                    output.accept(entry);
//                }
//            }
//            if (page.length() == 0) {
//                done = true;
//            } else {
//                offset += page.length();
//            }
//        } while (!done);
//        try {
//            output.endTransaction();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void paginate(String request, int offset, int pageSize, Consumer<JSONObject> consumer) throws UnirestException {
//        paginate(request, offset, pageSize, new SqlConsumer() {
//            @Override
//            public void accept(JSONObject jsonObject) {
//                consumer.accept(jsonObject);
//            }
//        });
//    }
//
//    public void paginate(String request, int offset, SqlConsumer output) throws UnirestException {
//        paginate(request, offset, 50, output);
//    }
//
//    public void paginate(String request, SqlConsumer output) throws UnirestException {
//        paginate(request, 0, 50, output);
//    }
//
//    private HttpResponse<JsonNode> api(String request) throws UnirestException {
//        return Unirest.get(IGDB_API_BASE_URL + request)
//                .header("X-Mashape-Key", IGDB_API_KEY)
//                .header("Accept", "application/json")
//                .asJson();
//    }
//
//    /**
//     * Converts single simple quotes to two single quotes (' => ''), as specified by PostgreSQL.
//     *
//     * @param text The text to escape.
//     * @return The escaped text.
//     */
//    private String escapeQuotesToPostgres(String text) {
//        return text.replace("'", "''");
//    }
//
//    /**
//     * Converts a timestamp to YYYY-MM-DD format.
//     *
//     * @param timestamp The timestamp to convert.
//     * @return The converted timestamp.
//     */
//    private String getISODateString(long timestamp) {
//        return new DateTime(timestamp).toString(ISO8601Formatter);
//    }
//
//    /**
//     * Abstract class for accepting JSON objects returned by the IGDB API and transforming the data into SQL INSERT statements.
//     */
//    private abstract class SqlConsumer implements Consumer<JSONObject> {
//        protected FileWriter fw;
//        private boolean isFinished = false;
//
//        SqlConsumer() {}
//
//        SqlConsumer(FileWriter fw) throws IOException {
//            this(fw, new String[]{});
//        }
//
//        SqlConsumer(FileWriter fw, String... initialCommands) throws IOException {
//            this.fw = fw;
//            for (String i : initialCommands) {
//                fw.write(i);
//            }
//            fw.write("BEGIN;\n");
//        }
//
//        /**
//         * Marks the transaction as ended in the file specified in {@link #SqlConsumer(FileWriter)}.
//         */
//        void endTransaction() throws IOException {
//            if(fw != null) {
//                if(!isFinished) {
//                    fw.write("COMMIT;\n");
//                    fw.close();
//                    isFinished = true;
//                } else {
//                    throw new IllegalStateException("Transaction already finished.");
//                }
//            }
//        }
//    }
//
////    private class GamesSqlConsumer extends SqlConsumer {
////        SqlConsumer genres, developers, publishers, keywords, platforms;
////
////        GamesSqlConsumer() throws IOException {
////            super(IgdbDownloader.this.gamesFile, "SET DATESTYLE TO ISO, YMD;\n", "\\encoding utf8;\n");
////            genres = new SqlConsumer(IgdbDownloader.this.genresFile) {
////                @Override
////                public void accept(JSONObject genre) {
////
////                }
////            }
////        }
////
////        @Override
////        public void accept(JSONObject jsonObject) {
////
////        }
////    }
//}
