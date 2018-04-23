package com.example.nisch.gitlatestcommit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nisch on 4/22/2018.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> {

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView commit_code;
        TextView commit_message;

    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.activity_main, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_main, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.result);
            viewHolder.commit_code = (TextView) convertView.findViewById(R.id.commitcode);
            viewHolder.commit_message = (TextView) convertView.findViewById(R.id.commitmessage);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.commit_code.setText(dataModel.getCommit_code());
        viewHolder.commit_message.setText(dataModel.getCommit_message());

        // Return the completed view to render on screen
        return convertView;
    }
}