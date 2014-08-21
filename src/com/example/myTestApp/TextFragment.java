package com.example.myTestApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by verzatran on 15.08.14.
 */
public final class TextFragment extends TestFragment implements CoreDelegateClass {
    private static final String KEY_CONTENT = "TextFragment:Content";
    private String mContent = "???";
    public EditText messageText;
    public Button makeMessage;
    public TextView authLabel;
    TextFragment context;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null)
                getActivity().setTitle(Constants.TEXT);
        }
        else {  }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        chekAuth(this);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         context = this;
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text, container, false);
        messageText = (EditText)view.findViewById(R.id.editText);
        makeMessage = (Button)view.findViewById(R.id.button);
        makeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString();
                if (message.length() <= Constants.MESSAGE_LENGHT)
                    CoreUtils.getInstance().makeTweet(message,context,Constants.TAG_TWEET);
                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                messageText.setText("");
                CoreUtils.getInstance().isNeedToUpdateMess = true;
            }
        });
        authLabel = (TextView)view.findViewById(R.id.textView);
        authLabel.setText(CoreUtils.getInstance().userName);
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    public void showLoadingDialog()
    {
        CoreUtils.getInstance().showDialog(getActivity(),R.string.dialog_title, "Ваш статус успешно обновлен!", R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        },true);
    }

    @Override
    public void requestDidFinish(myResponse response) {


        switch (response.tag) {
            case Constants.TAG_CHECK: {
                int code = Integer.valueOf((Integer) response.data);
                if (code == 200) {

                } else if (code == 401) {
                    Intent intent = new Intent(getActivity(), Auth.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }

            break;
            case Constants.TAG_TWEET:
                Handler handler = new Handler(getActivity().getBaseContext().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showLoadingDialog();
                    }
                });


                break;
        }
    }
}
