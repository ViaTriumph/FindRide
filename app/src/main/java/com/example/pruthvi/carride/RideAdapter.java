package com.example.pruthvi.carride;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RideAdapter extends ArrayAdapter<Ride> implements View.OnClickListener {


    private ArrayList<Ride> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView dName;
        TextView date;
        ImageView photo;
        TextView time;
        TextView fare;
    }

    /**
     *
     * @param data
     * @param context
     */
    public RideAdapter(ArrayList<Ride> data, Context context) {
        super(context, R.layout.ride_list, data);
        this.dataSet = data;
        this.mContext=context;

    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position=(Integer) view.getTag();
        Object object= getItem(position);
    }

    private int lastPosition = -1;

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view fo item row.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Ride ride = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ride_list, parent, false);
            viewHolder.dName = (TextView) convertView.findViewById(R.id.driveName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.profileImage);
            viewHolder.time= (TextView) convertView.findViewById(R.id.time);
            viewHolder.fare=(TextView)convertView.findViewById(R.id.fareText);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;


        viewHolder.dName.setText(ride.getDriverName());
        viewHolder.date.setText(ride.getDate());
        Uri pic=Uri.parse(ride.getPhoto());
        Picasso.with(mContext).load(pic).transform(new CircleTransform()).into(viewHolder.photo);
        viewHolder.time.setText(ride.getTime());
        viewHolder.fare.setText(ride.getFare());
        // Return the completed view to render on screen


        return convertView;
    }
}
