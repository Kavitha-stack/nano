package com.android.udacity.kavitha.jokeconsumer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Pair;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by Kavitha on 11/1/2016.
 */
public class JokeInstrumentedTest {
    @Test
    public void tellJoke() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
       /* EndpointsAsyncTask et = new EndpointsAsyncTask();
        String msg = et.doInBackground(new Pair<Context, String>(appContext, "Kavitha.."));
        System.out.println(msg);
        assertEquals("Test Joke",msg);
        */
    }

}