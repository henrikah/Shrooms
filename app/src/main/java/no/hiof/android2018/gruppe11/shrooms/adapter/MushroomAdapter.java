package no.hiof.android2018.gruppe11.shrooms.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import no.hiof.android2018.gruppe11.shrooms.R;
import no.hiof.android2018.gruppe11.shrooms.firebase.schema.ShroomSchema;

public class MushroomAdapter extends ArrayAdapter {

    private ArrayList<ShroomSchema> arrayList;
    private Context context;
    public MushroomAdapter(@NonNull Context context, ArrayList<ShroomSchema> arrayList) {
        super(context, R.layout.layout_mushroom_type_item);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_mushroom_type_item, parent, false);
        //    mViewHolder.mFlag = convertView.findViewById(R.id.ivFlag);
            mViewHolder.mName = convertView.findViewById(R.id.tvName);
        //    mViewHolder.mPopulation = convertView.findViewById(R.id.tvPopulation);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //mViewHolder.mFlag.setImageResource(arrayList.get(position));
        mViewHolder.mName.setText(arrayList.get(position).getName());
        //mViewHolder.mPopulation.setText(arrayList.get(position).getDescription());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
        TextView mPopulation;
    }
}
