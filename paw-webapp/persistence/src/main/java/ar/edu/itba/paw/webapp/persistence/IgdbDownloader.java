package ar.edu.itba.paw.webapp.persistence;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Class used to download data from <a href="igdb.com">IGDB.com</a> and generate SQL INSERT files.
 * <b>NOTE: </b> Do not use this class often. It has an API key which has a limited number of requests per day which
 * will get exceeded under heavy use (and this class makes heavy use of the API).
 * @author Juan Li Puma
 */
public class IgdbDownloader {
    private static final String IGDB_API_BASE_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/",
            IGDB_API_KEY = "yZykcOVSIsmshHJqdjCtRKXqIvHap1IC6HPjsnFzCYzfhEb7vv";
    private static IgdbDownloader instance;
    private static final DateTimeFormatter ISO8601Formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    private final FileWriter gamesFile, genresFile, consolesFile, companiesFile, gameGenresFile, gameConsolesFile, gameKeywordsFile, gameDevelopersFile, gamePublishersFile;

    private IgdbDownloader() {
        try {
            gamesFile = new FileWriter(new File("..//dataBase/games.sql"));
            genresFile = new FileWriter(new File("..//dataBase/genres.sql"));
            consolesFile = new FileWriter(new File("..//dataBase/consoles.sql"));
            companiesFile = new FileWriter(new File("..//dataBase/companies.sql"));
            gameGenresFile = new FileWriter(new File("..//dataBase/gameGenres.sql"));
            gameConsolesFile = new FileWriter(new File("..//dataBase/gameConsoles.sql"));
            gameKeywordsFile = new FileWriter(new File("..//dataBase/gameKeywords.sql"));
            gameDevelopersFile = new FileWriter(new File("..//dataBase/gameDevelopers.sql"));
            gamePublishersFile = new FileWriter(new File("..//dataBase/gamePublishers.sql"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static IgdbDownloader getInstance() {
        if (instance == null) {
            instance = new IgdbDownloader();
        }
        return instance;
    }

    public static void main(String[] args) {
        IgdbDownloader me = getInstance();
        me.downloadAllGenres();
        System.out.println("------------------------------------------------------");
        me.downloadAllCompanies();
        System.out.println("------------------------------------------------------");
        me.downloadAllConsoles();
        me.downloadAllGames();
    }

    public void downloadAllGenres() {
        try {
            genresFile.write("BEGIN;\n");
            paginate("genres/?fields=*", new Consumer<JSONObject>() {
                @Override
                public void accept(JSONObject genre) {
                    String query = "INSERT INTO power_up.genres (\"id\", \"name\") VALUES (" + genre.getInt("id") + ", '" + escapeQuotesToPostgres(genre.getString("name")) + "');\n";
                    try {
                        System.out.print(query);
                        IgdbDownloader.this.genresFile.write(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            genresFile.write("COMMIT;\n");
            genresFile.close();
        } catch (UnirestException|IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllConsoles() {
        try {
            consolesFile.write("BEGIN;\n");
            paginate("platforms/?fields=*", new Consumer<JSONObject>() {
                @Override
                public void accept(JSONObject genre) {
                    String query = "INSERT INTO power_up.consoles (\"id\", \"name\") VALUES (" + genre.getInt("id") + ", '" + escapeQuotesToPostgres(genre.getString("name")) + "');\n";
                    try {
                        System.out.print(query);
                        IgdbDownloader.this.consolesFile.write(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            consolesFile.write("COMMIT;\n");
            consolesFile.close();
        } catch (UnirestException|IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllCompanies() {
        try {
            companiesFile.write("BEGIN;\n");
            paginate("companies/?fields=*", new Consumer<JSONObject>() {
                @Override
                public void accept(JSONObject company) {
                    String query = "INSERT INTO power_up.companies (\"id\", \"name\") VALUES (" + company.getInt("id") + ", '" + escapeQuotesToPostgres(company.getString("name")) + "');\n";
                    try {
                        System.out.print(query);
                        IgdbDownloader.this.companiesFile.write(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            companiesFile.write("COMMIT;\n");
            companiesFile.close();
        } catch (UnirestException|IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllGames() {
        try {
            gameGenresFile.write("BEGIN;\n");
            gameDevelopersFile.write("BEGIN;\n");
            gamePublishersFile.write("BEGIN;\n");
            gamesFile.write("BEGIN;\n");
            gamesFile.write("SET DATESTYLE TO ISO, YMD;\n");
            gamesFile.write("\\encoding utf8;\n");
            paginate("games/?fields=*&order=name%3Aasc", new Consumer<JSONObject>() {
                @Override
                public void accept(JSONObject game) {
                    //Game
                    String name = "'" + escapeQuotesToPostgres(game.getString("name")) + "'",
                            summary = "null",
                            dateStr = "null";
                    if(game.has("summary")) {
                        summary = "'" + escapeQuotesToPostgres(game.getString("summary")) + "'";
                    }
                    if(game.has("first_release_date")) {
                        dateStr = "'" + getISODateString(game.getLong("first_release_date")) + "'";
                    }
                    String gameQuery = "INSERT INTO power_up.games VALUES (" + game.getInt("id") + ", " + name + ", " + summary + ", " + "0, " + dateStr + ");\n";
                    StringBuilder genreQueries = new StringBuilder(),
                                    developerQueries = new StringBuilder(),
                                    publisherQueries = new StringBuilder(),
                                    keywordQueries = new StringBuilder();
                    //Genres
                    if(game.has("genres")) {
                        JSONArray genres = game.getJSONArray("genres");
                        for (int i = 0; i < genres.length(); i++) {
                            genreQueries.append("INSERT INTO power_up.game_genres (\"game_id\", \"genre_id\") VALUES (").append(game.getInt("id")).append(", ").append(genres.getInt(i)).append(") ON CONFLICT DO NOTHING;\n");
                        }
                    }
                    //Developers
                    if(game.has("developers")) {
                        JSONArray developers = game.getJSONArray("developers");
                        for (int i = 0; i < developers.length(); i++) {
                            developerQueries.append("INSERT INTO power_up.game_developers (\"game_id\", \"developer_id\") VALUES (").append(game.getInt("id")).append(", ").append(developers.getInt(i)).append(")  ON CONFLICT DO NOTHING;\n");
                        }
                    }
                    //Publishers
                    if(game.has("publishers")) {
                        JSONArray publishers = game.getJSONArray("publishers");
                        for (int i = 0; i < publishers.length(); i++) {
                            publisherQueries.append("INSERT INTO power_up.game_publishers (\"game_id\", \"publisher_id\") VALUES (").append(game.getInt("id")).append(", ").append(publishers.getInt(i)).append(")  ON CONFLICT DO NOTHING;\n");
                        }
                    }
                    //Consoles
                    //TODO
                    //Keywords
                    //TODO schema declares keyword names should go here rather than keyword IDs.
//                    if(game.has("keywords")) {
//                        JSONArray keywords = game.getJSONArray("keywords");
//                        for (int i = 0; i < keywords.length(); i++) {
//                            keywordQueries.append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (").append(game.getInt("id")).append(", ").append(keywords.getInt(i)).append(");\n");
//                        }
//                    }
                    //TODO game_consoles, basically loop through release dates
                    try {
                        IgdbDownloader.this.gamesFile.write(gameQuery);
                        System.out.println(gameQuery);
                        IgdbDownloader.this.gameGenresFile.write(genreQueries.toString());
                        System.out.println(genreQueries.toString());
                        IgdbDownloader.this.gameDevelopersFile.write(developerQueries.toString());
                        System.out.println(developerQueries.toString());
                        IgdbDownloader.this.gamePublishersFile.write(publisherQueries.toString());
                        System.out.println(publisherQueries.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            gamesFile.write("COMMIT;\n");
            gameGenresFile.write("COMMIT;\n");
            gameDevelopersFile.write("COMMIT;\n");
            gamePublishersFile.write("COMMIT;\n");
            gamesFile.close();
            gameGenresFile.close();
            gameDevelopersFile.close();
            gamePublishersFile.close();
        } catch (UnirestException|IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * IGDB's free API access tier limits queries to 50 results per page, and offset + pageSize must be less that or
     * equal to 10000. This function "paginates" through results until it receives an empty page.
     *
     * @param request The paginateable request to perform numerous times.
     * @param offset Where, in the total result set, the current page should start.
     * @param pageSize The page size. Max is 50.
     * @param output Consumer for the produced output.
     * @throws UnirestException On API errors.
     */
    public void paginate(String request, int offset, int pageSize, Consumer<JSONObject> output) throws UnirestException {
        boolean done = false;
        do {
            JSONArray page = api(request + "&limit=" + pageSize + "&offset=" + offset).getBody().getArray();
            for (int i = 0; i < page.length(); i++) {
                JSONObject entry = page.getJSONObject(i);
                if(entry.has("Err")) {
                    System.err.println(entry.getJSONObject("Err").getString("message"));
//                    throw new RuntimeException();
                    return;
                } else {
                    output.accept(entry);
                }
            }
            if(page.length() == 0) {
                done = true;
            } else {
                offset += page.length();
            }
        } while(!done);
    }

    public void paginate(String request, int offset, Consumer<JSONObject> output) throws UnirestException {
        paginate(request, offset, 50, output);
    }

    public void paginate(String request, Consumer<JSONObject> output) throws UnirestException {
        paginate(request, 0, 50, output);
    }

    private HttpResponse<JsonNode> api(String request) throws UnirestException {
        return Unirest.get(IGDB_API_BASE_URL + request)
                .header("X-Mashape-Key", IGDB_API_KEY)
                .header("Accept", "application/json")
                .asJson();
    }

    /**
     * Converts single simple quotes to two single quotes (' => ''), as specified by PostgreSQL.
     * @param text The text to escape.
     * @return The escaped text.
     */
    private String escapeQuotesToPostgres(String text) {
        return text.replace("'", "''");
    }

    /**
     * Converts a timestamp to YYYY-MM-DD format.
     * @param timestamp The timestamp to convert.
     * @return The converted timestamp.
     */
    private String getISODateString(long timestamp) {
        return new DateTime(timestamp).toString(ISO8601Formatter);
    }
}
