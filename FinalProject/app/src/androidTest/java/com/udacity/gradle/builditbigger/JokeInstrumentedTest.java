package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Pair;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kavitha on 11/1/2016.
 */
public class JokeInstrumentedTest {
    @Test
    public void tellJoke() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AsyncTaskForBackend et = new AsyncTaskForBackend();
        String msg = et.doInBackground(new Pair<Context, String>(appContext, "kids"));
        String kidsJoke = "Why did the elephant sit on the marshmallow?So he wouldn't fall into the hot chocolate.";;
        assertEquals(kidsJoke,msg);

    }

}