package com.jokeproducer;

/**
 * Created by Kavitha on 11/2/2016.
 * Based on the joke type fetchJoke will return its respective jokes.
 */
public class JokeTeller {

    public String fetchJoke()
    {
        return Constants.defaultJoke;
    }
    public String fetchJoke(String type)
    {
        String msg = Constants.defaultJoke;
        if(type.equalsIgnoreCase("kids")){
            msg = Constants.kidsJoke;
        }else if(type.equalsIgnoreCase("sports")){
            msg = Constants.sportsJoke;
        }else if(type.equalsIgnoreCase("movies")){
            msg = Constants.moviesJoke;
        }
        return msg;
    }
}
