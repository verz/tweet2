package com.example.myTestApp;

/**
 * Created by verzatran on 15.08.14.
 */
public class Constants {
    static final int TAG_USER = 0;
    static final int TAG_INIT = 1;
    static final int TAG_ACCESS = 2;
    static final int TAG_TWEETS = 3;
    static final int TAG_TWEET = 4;
    static final int TAG_CHECK = 5;
    static final int MESSAGE_LENGHT = 128;
    static final String LIST = "List of Tweets";
    static final String TEXT = "Make a  Tweet";
    static final String AUTH = "Authorization";

    static final private String base =  "https://api.twitter.com/";
    static final String AUTH_TEXT = base + "oauth/authorize";
    static final String MAKE_TWEET = base +"1.1/statuses/update.json?status=";
    static final String USER_DETAILS = base +"1.1/account/settings.json";
    static final String VERIFY = base +"1.1/account/verify_credentials.json";
    static final String USER_TWEETS = base +"1.1/statuses/user_timeline.json?screen_name=";

}
