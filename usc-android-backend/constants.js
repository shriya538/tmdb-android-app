/* jshint node: true */
/*jshint esversion: 6 */
"use strict";
let Unique_API_KEY = "a90c87de4b61bb6a7f6ecc1de4b4c3c4";

let TRENDING_MOVIES =
  "https://api.themoviedb.org/3/trending/movie/day?api_key=" + Unique_API_KEY;
let image_pre = "https://image.tmdb.org/t/p/w500";
let avatar_pre = "https://image.tmdb.org/t/p/original/";
let TOP_RATED_MOVIES =
  "https://api.themoviedb.org/3/movie/top_rated?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let POPULAR_MOVIES =
  "https://api.themoviedb.org/3/movie/popular?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let TRENDING_TV =
  "https://api.themoviedb.org/3/trending/tv/day?api_key=" + Unique_API_KEY;
let TOP_RATED_TV =
  "https://api.themoviedb.org/3/tv/top_rated?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let POPULAR_TV =
  "https://api.themoviedb.org/3/tv/popular?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let CURRENTLY_PLAYING_MOVIES =
  "https://api.themoviedb.org/3/movie/now_playing?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let MOVIE_DETAILS =
  "https://api.themoviedb.org/3/movie/${input_string}?api_key=" +
  Unique_API_KEY +
  "&language=en-US&page=1";
let VIDEO_PRE = "https://www.youtube.com/watch?v=";
let default_poster = "https://cinemaone.net/images/movie_placeholder.png";
let default_backdrop =
  "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
let cast_default_path =
  "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/person-placeholder.png";

module.exports = {
  Unique_API_KEY: Unique_API_KEY,
  TRENDING_MOVIES: TRENDING_MOVIES,
  image_pre: image_pre,
  TOP_RATED_MOVIES: TOP_RATED_MOVIES,
  POPULAR_MOVIES: POPULAR_MOVIES,
  TRENDING_TV: TRENDING_TV,
  TOP_RATED_TV: TOP_RATED_TV,
  POPULAR_TV: POPULAR_TV,
  CURRENTLY_PLAYING_MOVIES: CURRENTLY_PLAYING_MOVIES,
  MOVIE_DETAILS: MOVIE_DETAILS,
  avatar_pre: avatar_pre,
  VIDEO_PRE: VIDEO_PRE,
  default_poster: default_poster,
  default_backdrop: default_backdrop,
  cast_default_path: cast_default_path,
};
