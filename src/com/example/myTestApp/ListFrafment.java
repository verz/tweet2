package com.example.myTestApp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by verzatran on 15.08.14.
 */
public final class ListFrafment extends TestFragment implements CoreDelegateClass {
    private static final String KEY_CONTENT = "ListFrafment:Content";
    private String mContent = "???";
    public LinkedList<String> myStringArray;
    public  ListView listView = null;
    public MySimpleArrayAdapter adapter;
    AlertDialog loadingDialog;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null)
                getActivity().setTitle(Constants.LIST);


            if (CoreUtils.getInstance().isNeedToUpdateMess)
            {
                getList();
                showLoadingDialog();
                CoreUtils.getInstance().isNeedToUpdateMess = false;
            }


        }
        else {  }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        chekAuth(this);
        showLoadingDialog();

    }
    public void showLoadingDialog()
    {
        loadingDialog = CoreUtils.getInstance().showDialog(getActivity(),R.string.dialog_title, "Идет загрузка данных, подождите...", 0, null,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }
    public void getList()
    {
        CoreUtils.getInstance().getTweets(this,Constants.TAG_TWEETS);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.events, container, false);
        myStringArray = new LinkedList<String>();
        adapter = new MySimpleArrayAdapter(getActivity(),myStringArray);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        //getList();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void requestDidFinish(myResponse response) {

        switch (response.tag) {
            case Constants.TAG_CHECK:
            {
                int code = Integer.valueOf((Integer) response.data);
                if (code == 200)
                {
                    getList();
                }else if (code == 401)
                {
                    Intent intent = new Intent(getActivity(), Auth.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }

                break;
            case Constants.TAG_TWEETS:
            {
                if (myStringArray.size()>0)
                    myStringArray.clear();
                JSONArray data = (JSONArray)response.data;
                if (data.length() == 0)
                    myStringArray.addLast("Список ваших сообщений пуст"+","+" "+","+" ");
                for (int i = 0; i < data.length(); i++)
                {
                    String text = null;
                    try {
                        text = data.getJSONObject(i).optString("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String date = null;
                    try {
                        date = data.getJSONObject(i).optString("created_at");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject user = null;
                    try {
                        user = data.getJSONObject(i).optJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String nick = null;
                    nick = user.optString("screen_name");


                    myStringArray.addLast(text+","+date+","+nick);
                }
                Handler handler = new Handler(getActivity().getBaseContext().getMainLooper());
                handler.post( new Runnable() {
                    @Override
                    public void run() {
                        if(loadingDialog.isShowing())
                            loadingDialog.cancel();
                        adapter.notifyDataSetChanged();
                    }
                } );
            }
                break;
        }
    }
}
