package com.example.myTestApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by verzatran on 15.08.14.
 */
public class CoreUtils {
    private static volatile CoreUtils sharedUtils = null;
    public final String API_KEY = "eHaTkH88CW1nhO8zXRBHRY8xQ";
    public final String API_SECRET = "SFpESLwS1F49WzOiwaj1gSkaLijxrF2x8koHk8H9r3NjX63TwP";
    public volatile OAuthService service;
    String authUrl = "";
    Token requestToken;
    Token accessToken;
    String userName = "";
    Boolean isNeedToUpdateMess = false;
    public static CoreUtils getInstance() {

        synchronized (CoreUtils.class){
            if (sharedUtils == null) {
                sharedUtils = new CoreUtils();
            }
        }

        return sharedUtils;
    }
    private CoreUtils() {

    }

    public void makeTweet(final String tweetString,final CoreDelegateClass coreDelegateClass,final int tag)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tweet = null;
                try {
                    tweet = URLEncoder.encode(tweetString, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String urlTweet=Constants.MAKE_TWEET+tweet;
                System.out.println("request: "+urlTweet);
                OAuthRequest request = new OAuthRequest(Verb.POST, urlTweet);
                service.signRequest(accessToken, request);
                System.out.println("REQUEST: " + request.getUrl());
                Response response = request.send();
                int code = response.getCode();
                String body = response.getBody();
                coreDelegateClass.requestDidFinish(new myResponse(tag,response));
                System.out.println(response.getCode());
                System.out.println(response.getBody());

            }
        }).start();
    }

    public AlertDialog showDialog(Activity window,int title, String text, int buttonText, DialogInterface.OnClickListener OnClick,Boolean cancelable)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(window);
        builder.setMessage(text)
                .setTitle(title);
        if (buttonText != 0)
        {
            builder.setPositiveButton(buttonText,OnClick);
        }

        AlertDialog curDialog = builder.create();
        curDialog.setCancelable(cancelable);
        curDialog.show();
        return curDialog;


    }

    public void getUserDetails(final CoreDelegateClass coreDelegateClass,final int tag)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OAuthRequest request = new OAuthRequest(Verb.GET, Constants.USER_DETAILS);
                service.signRequest(accessToken, request); // the access token from step 4
                Response response = request.send();
                String responseString = response.getBody();
                String responseStringDecoded = null;
                try {
                    responseStringDecoded = URLDecoder.decode(responseString, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject jObject = null;
                try {
                    jObject  = new JSONObject(responseStringDecoded);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userName =  jObject.optString("screen_name");
                coreDelegateClass.requestDidFinish(new myResponse(tag,jObject));

            }
        }).start();
    }

    public void chekAuth(final CoreDelegateClass coreDelegateClass,final int tag)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OAuthRequest request = new OAuthRequest(Verb.GET, Constants.VERIFY);
                service.signRequest(accessToken, request);
                System.out.println("REQUEST: " + request.getUrl());
                Response response = request.send();
                int code = response.getCode();
                String body = response.getBody();
                coreDelegateClass.requestDidFinish(new myResponse(tag,code));

            }
        }).start();

    }

    public void getTweets(final CoreDelegateClass coreDelegateClass,final int tag)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OAuthRequest request = new OAuthRequest(Verb.GET, Constants.USER_TWEETS + userName);
                service.signRequest(accessToken, request); // the access token from step 4
                Response response = request.send();
                String responseString = response.getBody();
                String responseStringDecoded = null;
                try {
                    responseStringDecoded = URLDecoder.decode(responseString, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONArray jObject = null;
                try {
                    jObject  = new JSONArray(responseStringDecoded);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                coreDelegateClass.requestDidFinish(new myResponse(tag,jObject));

            }
        }).start();
    }
    public void getAccesToken(final String pincode,final CoreDelegateClass coreDelegateClass,final int tag)
    {

        if (pincode.length() > 0)
        {
            new Thread(new Runnable() {
                public void run() {
                    Verifier v;
                    v = new Verifier(pincode);
                    try {
                        accessToken = service.getAccessToken(requestToken, v);
                    }catch (Exception e)
                    {
                       Log.d("1","" + e);
                    }

                    Log.d("1", "2");
                    coreDelegateClass.requestDidFinish(new myResponse(tag,accessToken));
                }
            }).start();
        }
    }
    public void initState(final CoreDelegateClass coreDelegateClass,final int tag)
    {
        new Thread(new Runnable() {
            public void run()  {
                service = new ServiceBuilder()
                        .provider(TwitterApi.SSL.class)
                        .apiKey(API_KEY)
                        .apiSecret(API_SECRET)
                        .build();
                try {
                    requestToken = service.getRequestToken();
                }catch (Exception e)
                {

                }

                if (requestToken != null)
                {
                    authUrl = service.getAuthorizationUrl(requestToken);
                    coreDelegateClass.requestDidFinish(new myResponse(tag,authUrl) );
                }else
                {
                    coreDelegateClass.requestDidFinish(new myResponse(tag,null) );
                }

                Log.d("1", "2");

            }
        }).start();
    }

}
