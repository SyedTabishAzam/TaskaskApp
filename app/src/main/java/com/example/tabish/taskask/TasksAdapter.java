package com.example.tabish.taskask;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class TasksAdapter extends ArrayAdapter<Tasks> {

    public enum COLORS {
        RED(R.drawable.red), BLUE(R.drawable.blue),GREEN(R.drawable.green), PURPLE(R.drawable.purple),ORANGE(R.drawable.orange),BLACK(R.drawable.blue),YELLOW(R.drawable.yellow),GRAY(R.drawable.gray);

        private final int number;

        private COLORS(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    public TasksAdapter(Activity context, ArrayList<Tasks> tasks)
    {
        super(context,0,tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the {@link Tasks} object located at this position in list
        Tasks currentTask = getItem(position);

        //check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        //Set uname field in list to current username
        TextView user = (TextView) listItemView.findViewById(R.id.uname);
        user.setText(currentTask.getUser());

        TextView tag = (TextView) listItemView.findViewById(R.id.tag);
        tag.setText(currentTask.getTag());

        TextView ulevel = (TextView) listItemView.findViewById(R.id.ulevel);
        ulevel.setText(currentTask.getTime());
        ulevel.setBackgroundResource(getColorResource(currentTask.getUrgencyColor()).getNumber());


        TextView crit = (TextView) listItemView.findViewById(R.id.clevel);
        crit.setText(currentTask.getCritical());
        crit.setBackgroundResource(getColorResource(currentTask.getCriticalColor()).getNumber());
        //TextView user = (TextView) listItemView.findViewById(R.id.uname);
        //user.setText(currentTask.getUser());

        return listItemView;
    }

    private COLORS getColorResource(String uvalue)
    {
        COLORS result = null;
        switch (uvalue) {
            case "Black":  result =COLORS.BLACK;
                break;
            case "Blue":  result =COLORS.BLUE;
                break;
            case "Yellow":  result =COLORS.YELLOW;
                break;
            case "Gray":  result =COLORS.GRAY;
                break;
            case "Purple":  result =COLORS.PURPLE;
                break;
            case "Red":  result =COLORS.RED;
                break;
            case "Orange":  result =COLORS.ORANGE;
                break;
            case "Green":  result =COLORS.GREEN;
                break;
            default: result =COLORS.RED;
                break;
        }
        return  result;
    }
}
