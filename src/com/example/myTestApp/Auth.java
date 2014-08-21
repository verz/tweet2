package com.example.myTestApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by verzatran on 20.08.14.
 */
public class Auth extends Activity  implements CoreDelegateClass{


    WebView webview = null;
    EditText pin = null;
    Button makePin = null;
    public Auth context;
    public Handler handler;
    AlertDialog loadingDialog;
    public static Activity Auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(Constants.AUTH);
        Auth = this;
        if (myViewPager.pagerActivity != null)
            myViewPager.pagerActivity.finish();

        handler = new Handler(getBaseContext().getMainLooper());
        webview = (WebView) findViewById(R.id.webView);
        makePin = (Button)findViewById(R.id.button);
        pin = (EditText)findViewById(R.id.editText);
        makePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pincode = pin.getText().toString();
                CoreUtils.getInstance().getAccesToken(pincode,context,Constants.TAG_ACCESS);
                InputMethodManager inputManager =
                        (InputMethodManager) context.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                makePin.setEnabled(false);
                pin.setEnabled(false);
            }
        });

        context = this;

        webview.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;

        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
            }

            public void onPageFinished(WebView view, String url) {
                if (loadingDialog.isShowing())
                    loadingDialog.cancel();
                if (url.equals(Constants.AUTH_TEXT)) {
                    makePin.setEnabled(true);
                    pin.setEnabled(true);
                }
            }

        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        makeConnection();
    }

    public void makeConnection() {
        CoreUtils.getInstance().initState(context, Constants.TAG_INIT);
        showLoadingDialog();
    }

    @Override
    public void requestDidFinish(myResponse response) {

        switch (response.tag) {
            case Constants.TAG_INIT:
                if (response.data == null)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlertDialog();
                        }
                    });

                }else {
                    final String url = response.data.toString();
                    webview.post(new Runnable() {
                        @Override
                        public void run() {
                            webview.loadUrl(url);
                        }
                    });
                }
                break;
            case Constants.TAG_ACCESS:
                if (response.data != null) {

                    CoreUtils.getInstance().getUserDetails(context, Constants.TAG_USER);
                }else
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showInfoPinDialog();

                        }
                    });

                }
                break;
            case Constants.TAG_USER:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showGreetingDialog();
                    }
                });
                break;
        }
    }

//    public AlertDialog showDialog(int title, String text, int buttonText, DialogInterface.OnClickListener OnClick,Boolean cancelable)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(text)
//                .setTitle(title);
//        if (buttonText != 0)
//        {
//            builder.setPositiveButton(buttonText,OnClick);
//        }
//
//            AlertDialog curDialog = builder.create();
//            curDialog.setCancelable(cancelable);
//            curDialog.show();
//        return curDialog;
//
//
//    }

    public void showAlertDialog()
    {
        CoreUtils.getInstance().showDialog(this,R.string.dialog_title, "Интернет соединение отсутствует!", R.string.try_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                makeConnection();
            }
        },false);
    }

    public void showGreetingDialog()
    {


        CoreUtils.getInstance().showDialog(this,R.string.dialog_title, "Добро пожаловать," + CoreUtils.getInstance().userName + "!", R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                startPager();
            }
        },false);

    }

    public void showInfoPinDialog()
    {
        CoreUtils.getInstance().showDialog(this,R.string.dialog_title, "Неверно введен Пин - код, попробуйте ещё раз", R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                pin.setText("");
                makeConnection();
            }
        },false);
    }

    public void showLoadingDialog()
    {
        loadingDialog = CoreUtils.getInstance().showDialog(this,R.string.dialog_title, "Идет загрузка страницы, подождите...", 0, null,false);
    }

    public void startPager() {
        Intent intent = new Intent(context, myViewPager.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
