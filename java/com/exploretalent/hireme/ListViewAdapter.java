package com.exploretalent.hireme;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<ListViewInfo_Class>{

    private Context context;
   // private HashMap<Integer,Information_Class> list=new HashMap<>();
    private ArrayList arrayList;
    int resource;
    private int lastPosition = -1;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListViewInfo_Class> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource = resource;
    }
    static class ViewHolder{
        TextView nameTxt;
        TextView locationTxt;
        TextView addressTxt;
        TextView distanceTxt;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getName();
        String email = getItem(position).getEmail();
        String workType = getItem(position).getWorktype();
        String address = getItem(position).getAddress();
        String location = getItem(position).getLocation();
        String contactNo = getItem(position).getContactNo();
        Integer distance = getItem(position).getDistance();
        String key = getItem(position).getKey();


        ListViewInfo_Class listViewInfo_class = new ListViewInfo_Class(name,email,workType,address,location,contactNo,distance,key);

        final View result;

        ViewHolder holder;

        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.nameTxt = (TextView) convertView.findViewById(R.id.nameID);
            holder.locationTxt = (TextView) convertView.findViewById(R.id.locationID);
            holder.addressTxt = (TextView) convertView.findViewById(R.id.addressID);
            holder.distanceTxt = (TextView) convertView.findViewById(R.id.distanceID);

            result = convertView;
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder)convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition)? R.anim.load_down_anim : R.anim.load_up_anim);
        result.setAnimation(animation);
        lastPosition = position;

        holder.nameTxt.setText(listViewInfo_class.getName());
        holder.locationTxt.setText(listViewInfo_class.getLocation());
        holder.addressTxt.setText(listViewInfo_class.getAddress());
        holder.distanceTxt.setText(listViewInfo_class.getDistance().toString()+"m");

        return convertView;



    }
}
