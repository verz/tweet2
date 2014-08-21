package com.example.myTestApp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by verzatran on 16.08.14.
 */
public class MySimpleArrayAdapter extends BaseAdapter {
    Context context;
    LinkedList<String> data;
    private static LayoutInflater inflater = null;

    public MySimpleArrayAdapter(Activity context, LinkedList<String> data)
    {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lestitem, parent, false);

        TextView id = (TextView) rowView.findViewById(R.id.textView2);
        String[] separated = data.get(position).split(",");
        id.setText("id: "+separated[2]);
        TextView time = (TextView)rowView.findViewById(R.id.textView3);
        time.setText(separated[1].replace("0000",""));
        TextView message = (TextView)rowView.findViewById(R.id.textView);
        message.setText("\"" + separated[0]+ "\"" );
        return rowView;
    }

}
