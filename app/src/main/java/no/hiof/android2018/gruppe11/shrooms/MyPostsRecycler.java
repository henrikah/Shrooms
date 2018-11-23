package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyPostsRecycler extends RecyclerView.Adapter<MyPostsRecycler.ViewHolder>{

    private ArrayList<Post> Posts = new ArrayList<>();
    private Context mContext;
    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    StorageReference mushroomImagesRef;
    String pictureID;
    private static final String TAG = "RecyclerViewAdapter";
    FileDownloadTask.TaskSnapshot snapshot1;


    double firLat;
    double firLon;


    public MyPostsRecycler(ArrayList<Post> mPost, Context mContext, double firLat, double firLon) {
        this.Posts= mPost;
        this.mContext = mContext;
        this.firLat = firLat;
        this.firLon = firLon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_postitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.image.setImageResource(R.drawable.logo);

        double secLat = Posts.get(i).getLocation().getLatitude();
        double secLon = Posts.get(i).getLocation().getLongitude();

        Location oldLoc = new Location("oldLoc");
        oldLoc.setLatitude(firLat);
        oldLoc.setLongitude(firLon);

        Location newLoc = new Location("newLoc");
        newLoc.setLatitude(secLat);
        newLoc.setLongitude(secLon);
        double distance = (Math.sqrt(Math.pow((secLat - firLat),2) + Math.pow((secLon - firLon),2))) * 83.57;


        viewHolder.distance.setText((new DecimalFormat("#.#").format(oldLoc.distanceTo(newLoc) / 1000) ) + " Km");
    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        RelativeLayout feedLayout;
        TextView distance;
        TextView user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.postItemImage);
            feedLayout = itemView.findViewById(R.id.postItemLayout);
            distance = itemView.findViewById(R.id.postItemDistance);
        }

    }



}
