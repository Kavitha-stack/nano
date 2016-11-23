/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.android.udacity.kavitha.finalapp.jokebackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.jokeproducer.JokeTeller;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "jokebackend.finalapp.kavitha.udacity.android.com",
                ownerName = "jokebackend.finalapp.kavitha.udacity.android.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        JokeTeller jokeTeller = new JokeTeller();
        if (name == null) {
            response.setData(jokeTeller.fetchJoke());
        } else {
            response.setData(jokeTeller.fetchJoke(name));
        }
        return response;
    }

    @ApiMethod(name = "sayJokes")
    public MyBean sayJokes(@Named("name") String name) {
        MyBean response = new MyBean();
        JokeTeller jokeTeller = new JokeTeller();
        if (name == null) {
            response.setData(jokeTeller.fetchJoke());
        } else {
            response.setData(jokeTeller.fetchJoke(name));
        }
        return response;
    }

}
