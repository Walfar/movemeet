package com.sdp.movemeet.ui.workout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdp.movemeet.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;

    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modellist);
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
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

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDescription);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        holder.mIconIv.setImageResource(modellist.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modellist.get(position).getTitle().equals("Workout #1: Abs")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 3x15 Leg crunches\n• 3x10 Dumbbell Crunches\n• 3x6 Leg Raises\n• 3x15 Hanging Knee Raises\n");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Workout #2: Legs")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 4x5 Barbell Squats\n• 4x25 Barbell Hip Thrust\n• 4x15 Leg Extension\n• 4x20 Standing Calf Raises\n");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Workout #3: Upper Body")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    intent.putExtra("contentTv", "\n• 4x15 Bench Presses\n• 4x25 Overhead Presses\n• 4x15 Barbell Split Squats\n• 4x20 Back Squats\n");
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    public class ViewHolder {
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }
}