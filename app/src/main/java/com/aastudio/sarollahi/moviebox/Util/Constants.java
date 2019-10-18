package com.aastudio.sarollahi.moviebox.Util;


public class Constants {

    //API
    private static final String KEY = "d6658a851664f8c3586905e0d74fa858";
    public static final String app_id = "ca-app-pub-5705312979060396~4475984521";
    public static final String app_unit_id = "ca-app-pub-5705312979060396/5917537635";
    public static final String app_unit_id_details = "ca-app-pub-5705312979060396/5519722437";
    public static final String app_unit_id_reviews = "ca-app-pub-5705312979060396/6581522660";
    //public static final String app_unit_id = "ca-app-pub-3940256099942544/6300978111";

    //OMDB API
    public static final String URL_LEFT = "http://www.omdbapi.com/?s=";
    public static final String URL = "http://www.omdbapi.com/?i=";
    public static final String API_KEY = "&apikey=cf599b18";
    public static final String URL_PAGE = "&page=";

    //search movie/tv by name and year (popup)
    public static final String MAIN_URL_LEFT="https://api.themoviedb.org/3/search/movie?api_key="+KEY+"&query=";
    public static final String TV_SHOWS = "https://api.themoviedb.org/3/search/tv?api_key="+KEY+"&query=";
    public static final String MAIN_URL_RIGHT_MOVIE="&year=";
    public static final String MAIN_URL_RIGHT_tV="&first_air_date_year=";

    //Search actor by name
    public static final String MAIN_FIND_ACROT_BY_NAME ="https://api.themoviedb.org/3/search/person?api_key="+KEY+"&append_to_response=credits&query=";

    //Search movie/tv by actor id
    public static final String MAIN_FIND_MOVIE_BY_ACTOR_LEFT ="https://api.themoviedb.org/3/person/";
    public static final String MAIN_FIND_MOVIE_BY_ACTOR_RIGHT ="/movie_credits?api_key="+KEY+"&language=en-US";
    public static final String MAIN_FIND_TV_BY_ACTOR_LEFT ="https://api.themoviedb.org/3/person/";
    public static final String MAIN_FIND_TV_BY_ACTOR_RIGHT ="/tv_credits?api_key="+KEY+"&language=en-US";

    //get Movie information
    public static final String MAIN_INFO_URL_LEFT="https://api.themoviedb.org/3/movie/";
    public static final String MAIN_INFO_URL_RIGHT="?api_key="+KEY+"&append_to_response=videos,images,credits,similar,recommendations,external_ids";
    public static final String MAIN_INFO_URL_RIGHT_REVIEWS="?api_key="+KEY+"&append_to_response=reviews";

    //get Tv information
    public static final String TV_SHOW_INFO_LEFT="https://api.themoviedb.org/3/tv/";
    public static final String TV_SHOW_INFO_RIGHT="?api_key="+KEY+"&append_to_response=videos,credits,external_ids,images,recommendations,similar";

    //get Season/episodes information
    public static final String TV_SHOW_Episode_Left="https://api.themoviedb.org/3/tv/";
    public static final String TV_SHOW_Episode_MIDDLE="/season/";
    public static final String TV_SHOW_Episode_RIGHT="?api_key="+KEY+"&append_to_response=account_states,credits,external_ids,images,videos";

    //get Person information
    public static final String PERSON_INFO_URL_LEFT="https://api.themoviedb.org/3/person/";
    public static final String PERSON_INFO_URL_RIGHT="?api_key="+KEY+"&append_to_response=credits,external_ids";
    public static final String PERSON_INFO_URL_RIGHT_2="?api_key="+KEY+"&append_to_response=images";

    //get first page movie/Tv list
    public static final String UP_COMING_URL="https://api.themoviedb.org/3/movie/upcoming?api_key="+KEY+"&page=";
    public static final String POPULAR_URL="https://api.themoviedb.org/3/movie/popular?api_key="+KEY+"&page=";
    public static final String NOW_PLAYING_URL="https://api.themoviedb.org/3/movie/now_playing?api_key="+KEY+"&page=";
    public static final String TOP_RATED_URL="https://api.themoviedb.org/3/movie/top_rated?api_key="+KEY+"&page=";

    //get Genres
    public static final String MOVIE_GENRE_URL="https://api.themoviedb.org/3/genre/movie/list?api_key="+KEY+"&language=en-US";
    public static final String TV_GENRE_URL="https://api.themoviedb.org/3/genre/tv/list?api_key="+KEY+"&language=en-US";

    //find best movies by genre
    public static final String BEST_MOVIE_GENRE_URL_LEFT="https://api.themoviedb.org/3/discover/movie?api_key="+KEY+"&sort_by=popularity.desc&primary_release_date.gte=";
    public static final String BEST_MOVIE_GENRE_URL_MIDDLE = "-01-01&primary_release_date.lte=";
    public static final String BEST_MOVIE_GENRE_URL_RIGHT = "-12-31&with_genres=";

    //find best tv shows by genre
    public static final String BEST_TV_GENRE_URL_LEFT="https://api.themoviedb.org/3/discover/tv?api_key="+KEY+"&sort_by=popularity.desc&first_air_date.gte=";
    public static final String BEST_TV_GENRE_URL_MIDDLE = "-01-01&first_air_date.lte=";
    public static final String BEST_TV_GENRE_URL_RIGHT = "-12-31&with_genres=";



    public static final int BANNER_WIDTH = 320;
    public static final int BANNER_HEIGHT = 50;



}
