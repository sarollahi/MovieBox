package com.aastudio.sarollahi.moviebox.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.android.sarollahi.moviebox.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {

    //Database Name
    public static final String DATABASE_NAME = "movieBox";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Data Type
    private static final String TEXT = " TEXT ";
    private static final String INTEGER = " INTEGER ";

    //Tables Name
    private static final String TABLE_SEEN = "movie";
    private static final String TABLE_FAVORITE = "favorite";

    //SEEN Table - column name
    private static final String S_ID = "id";
    private static final String S_TITLE = "title";
    private static final String S_YEAR = "year";
    private static final String S_PLOT = "plot";
    private static final String S_POSTER = "poster";
    private static final String S_TYPE = "type";
    private static final String S_MOVIEID = "movieId";
    private static final String S_DATEADDED = "DateItemAdded";
    private static final String S_ORIGINALLANGUAGE = "originalLanguage";


    //FAVORITE Table - column name
    private static final String F_ID = "id";
    private static final String F_TITLE = "title";
    private static final String F_YEAR = "year";
    private static final String F_PLOT = "plot";
    private static final String F_POSTER = "poster";
    private static final String F_TYPE = "type";
    private static final String F_MOVIEID = "movieId";
    private static final String F_DATEADDED = "DateItemAdded";
    private static final String F_ORIGINALLANGUAGE = "originalLanguage";

    //Table Create Statement

    //Movies table
    private static final String CREATE_TABLE_SEEN = "CREATE TABLE " + TABLE_SEEN + " ( " +
            S_ID + INTEGER + "," +
            S_TITLE + TEXT + "," +
            S_YEAR + TEXT + "," +
            S_PLOT + TEXT + "," +
            S_POSTER + TEXT + "," +
            S_TYPE + TEXT + "," +
            S_MOVIEID + TEXT + "," +
            S_DATEADDED + TEXT + "," +
            S_ORIGINALLANGUAGE + TEXT + "," +
            "PRIMARY KEY (" + S_ID + ")" +
            ")";


    //Movies table
    private static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_FAVORITE + " ( " +
            F_ID + INTEGER + "," +
            F_TITLE + TEXT + "," +
            F_YEAR + TEXT + "," +
            F_PLOT + TEXT + "," +
            F_POSTER + TEXT + "," +
            F_TYPE + TEXT + "," +
            F_MOVIEID + TEXT + "," +
            F_DATEADDED + TEXT + "," +
            F_ORIGINALLANGUAGE + TEXT + "," +
            "PRIMARY KEY (" + F_ID + ")" +
            ")";

    //Table Delete Statement

    //Movies Table
    private static final String DELETE_SEEN = "DROP TABLE IF EXISTS " + TABLE_SEEN;

    //Favorite Table
    private static final String DELETE_FAVORITE = "DROP TABLE IF EXISTS " + TABLE_FAVORITE;

    //context
    private Context mContext;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create tables
        db.execSQL(CREATE_TABLE_SEEN);
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //reinitialize db
        db.execSQL(DELETE_SEEN);
        db.execSQL(DELETE_FAVORITE);

        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db) {

        db.execSQL(DELETE_SEEN);
        db.execSQL(DELETE_FAVORITE);
    }

    //create a new movie
    public void addMovie(Movie movie) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(S_ID, Integer.parseInt(movie.getMovieId()));
        values.put(S_TITLE, movie.getTitle());
        values.put(S_YEAR, movie.getYear());
        values.put(S_PLOT, movie.getPlot());
        values.put(S_POSTER, movie.getPoster());
        values.put(S_TYPE, movie.getType());
        values.put(S_MOVIEID, movie.getMovieId());
        values.put(S_DATEADDED, java.lang.System.currentTimeMillis());
        values.put(S_ORIGINALLANGUAGE, movie.getOriginalLanguage());


        // Create a new map of values, where column names are the keys
        db.insert(TABLE_SEEN, null, values);
    }

    //create a new tvshow
    public void addTv(TvShow tv) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(S_ID, Integer.parseInt(tv.getMovieId()));
        values.put(S_TITLE, tv.getTitle());
        values.put(S_YEAR, tv.getYear());
        values.put(S_PLOT, tv.getPlot());
        values.put(S_POSTER, tv.getPoster());
        values.put(S_TYPE, tv.getType());
        values.put(S_MOVIEID, tv.getMovieId());
        values.put(S_DATEADDED, java.lang.System.currentTimeMillis());
        values.put(S_ORIGINALLANGUAGE, tv.getOriginalLanguage());


        // Create a new map of values, where column names are the keys
        db.insert(TABLE_SEEN, null, values);
    }

    //create a new favorite
    public void addFavorite(Movie movie) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(F_ID, Integer.parseInt(movie.getMovieId()));
        values.put(F_TITLE, movie.getTitle());
        values.put(F_YEAR, movie.getYear());
        values.put(F_PLOT, movie.getPlot());
        values.put(F_POSTER, movie.getPoster());
        values.put(F_TYPE, movie.getType());
        values.put(F_MOVIEID, movie.getMovieId());
        values.put(F_DATEADDED, java.lang.System.currentTimeMillis());
        values.put(F_ORIGINALLANGUAGE, movie.getOriginalLanguage());


        // Create a new map of values, where column names are the keys
        db.insert(TABLE_FAVORITE, null, values);
    }

    //get all movies
    public List<Movie> getAllmovies() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Movie> movieList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SEEN, new String[]{
                S_ID, S_MOVIEID, S_DATEADDED,
                S_ORIGINALLANGUAGE, S_PLOT, S_POSTER, S_TITLE, S_YEAR}, "type=?", new String[]{"m"}, null, null, S_DATEADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setPoster(cursor.getString(cursor.getColumnIndex(S_POSTER)));
                movie.setMovieId(cursor.getString(cursor.getColumnIndex(S_MOVIEID)));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(S_ORIGINALLANGUAGE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(S_TITLE)));
                movie.setPlot(cursor.getString(cursor.getColumnIndex(S_PLOT)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(S_YEAR)));


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(S_DATEADDED)))
                        .getTime());

                movie.setDateItemAdded(formatedDate);

                movieList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return movieList;
    }

    //get all tvshows
    public List<TvShow> getAlltvshows() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<TvShow> tvShowList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SEEN, new String[]{
                S_ID, S_MOVIEID, S_DATEADDED,
                S_ORIGINALLANGUAGE, S_PLOT, S_POSTER, S_TITLE, S_YEAR}, "type=?", new String[]{"t"}, null, null, S_DATEADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                TvShow tvShow = new TvShow();
                tvShow.setPoster(cursor.getString(cursor.getColumnIndex(S_POSTER)));
                tvShow.setMovieId(cursor.getString(cursor.getColumnIndex(S_MOVIEID)));
                tvShow.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(S_ORIGINALLANGUAGE)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndex(S_TITLE)));
                tvShow.setPlot(cursor.getString(cursor.getColumnIndex(S_PLOT)));
                tvShow.setYear(cursor.getString(cursor.getColumnIndex(S_YEAR)));


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(S_DATEADDED)))
                        .getTime());

                tvShow.setDateItemAdded(formatedDate);

                tvShowList.add(tvShow);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tvShowList;
    }

    //get all fav-movies
    public List<Movie> getAllfavmovies() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Movie> movieList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_FAVORITE, new String[]{
                S_ID, S_MOVIEID, S_DATEADDED,
                S_ORIGINALLANGUAGE, S_PLOT, S_POSTER, S_TITLE, S_YEAR}, "type=?", new String[]{"m"}, null, null, S_DATEADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setPoster(cursor.getString(cursor.getColumnIndex(S_POSTER)));
                movie.setMovieId(cursor.getString(cursor.getColumnIndex(S_MOVIEID)));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(S_ORIGINALLANGUAGE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(S_TITLE)));
                movie.setPlot(cursor.getString(cursor.getColumnIndex(S_PLOT)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(S_YEAR)));


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(S_DATEADDED)))
                        .getTime());

                movie.setDateItemAdded(formatedDate);

                movieList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return movieList;
    }

    //get all fav-tvshows
    public List<TvShow> getAllfavtvshows() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<TvShow> tvShowList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_FAVORITE, new String[]{
                S_ID, S_MOVIEID, S_DATEADDED,
                S_ORIGINALLANGUAGE, S_PLOT, S_POSTER, S_TITLE, S_YEAR}, "type=?", new String[]{"t"}, null, null, S_DATEADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                TvShow tvShow = new TvShow();
                tvShow.setPoster(cursor.getString(cursor.getColumnIndex(S_POSTER)));
                tvShow.setMovieId(cursor.getString(cursor.getColumnIndex(S_MOVIEID)));
                tvShow.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(S_ORIGINALLANGUAGE)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndex(S_TITLE)));
                tvShow.setPlot(cursor.getString(cursor.getColumnIndex(S_PLOT)));
                tvShow.setYear(cursor.getString(cursor.getColumnIndex(S_YEAR)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(S_DATEADDED)))
                        .getTime());

                tvShow.setDateItemAdded(formatedDate);

                tvShowList.add(tvShow);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tvShowList;
    }

    //get all movies id
    public ArrayList<String> getmovieId() {

        ArrayList<String> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + S_ID + " FROM " + TABLE_SEEN;

        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(S_ID));
                idList.add(String.valueOf(id));
            }
        }
        c.close();
        return idList;
    }

    //movie seen or not
    public int getmovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorc = db.rawQuery("SELECT * FROM " + TABLE_SEEN + " WHERE id='" + id + "'", null);

        int seen;
        if (cursorc.moveToFirst()) {
            seen = 0;
        } else {
            seen = 1;
        }

        return seen;
    }

    //movie fav or not
    public int getfav(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorc = db.rawQuery("SELECT * FROM " + TABLE_FAVORITE + " WHERE id='" + id + "'", null);

        int fav;
        if (cursorc.moveToFirst()) {
            fav = 0;
        } else {
            fav = 1;
        }

        return fav;
    }

    //delete a movie
    public void deleteMovie(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SEEN, S_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    //delete a fav
    public void deleteFavorite(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVORITE, F_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    //close database
    public void closeDB() {

        SQLiteDatabase db = this.getReadableDatabase();

        if (db != null && db.isOpen())
            db.close();
    }


    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, mContext.getString(R.string.backup_completed), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.db_not_backup), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, mContext.getString(R.string.import_completed), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.db_not_import), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void deleteDB() {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            mContext.deleteDatabase(DATABASE_NAME);
            Toast.makeText(mContext, mContext.getString(R.string.db_delete), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.db_not_found), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
