package com.sdp.movemeet.view.workout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdp.movemeet.R;
import com.sdp.movemeet.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Workout> modelList;
    ArrayList<Workout> arrayList;

    public ListViewAdapter(Context context, List<Workout> modelList) {
        this.context = context;
        this.modelList = modelList;
        inflater = LayoutInflater.from(this.context);
        this.arrayList = new ArrayList<Workout>();
        this.arrayList.addAll(modelList);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);

            holder.titleTv = view.findViewById(R.id.mainTitle);
            holder.descTv = view.findViewById(R.id.mainDescription);
            holder.iconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the results into textviews
        holder.titleTv.setText(modelList.get(position).getTitle());
        holder.descTv.setText(modelList.get(position).getDesc());
        //set the result in imageview
        holder.iconIv.setImageResource(modelList.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modelList.get(position).getTitle().equals("Workout #1: Abs")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(context, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 3x15 Leg crunches\n• 3x10 Dumbbell Crunches\n• 3x6 Leg Raises\n• 3x15 Hanging Knee Raises\n");
                    context.startActivity(intent);
                }
                if (modelList.get(position).getTitle().equals("Workout #2: Legs")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(context, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 4x5 Barbell Squats\n• 4x25 Barbell Hip Thrust\n• 4x15 Leg Extension\n• 4x20 Standing Calf Raises\n");
                    context.startActivity(intent);
                }
                if (modelList.get(position).getTitle().equals("Workout #3: Upper Body")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(context, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 4x15 Bench Presses\n• 4x25 Overhead Presses\n• 4x15 Barbell Split Squats\n• 4x20 Back Squats\n");
                    context.startActivity(intent);
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView titleTv, descTv;
        ImageView iconIv;
    }
}