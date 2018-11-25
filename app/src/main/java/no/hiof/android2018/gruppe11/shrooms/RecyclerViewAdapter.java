package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Post> Posts = new ArrayList<>();
    private Context mContext;
    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    StorageReference mushroomImagesRef;
    String pictureID;
    private static final String TAG = "Errorbro";
    FileDownloadTask.TaskSnapshot snapshot1;
    String generatedFilePath;
    String fullEmail;

    double firLat;
    double firLon;


    public RecyclerViewAdapter(ArrayList<Post> mPost, Context mContext,double firLat, double firLon) {
        this.Posts= mPost;
        this.mContext = mContext;
        this.firLon = firLon;
        this.firLat = firLat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_feeditem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        pictureID = Posts.get(i).getBildeNavn();
        //pictureID = Posts.get(i).getTitle();
        mushroomImagesRef = mStorageRef.child("brukerBilder/"+pictureID);
        //String bildeNavnUtenJPG = pictureID.substring(0, pictureID.length() - 4);
       final ViewHolder viewHolder2 = viewHolder;

        final long ONE_MEGABYTE = 1024 * 1024;

             mushroomImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                generatedFilePath = uri.toString();
                Picasso.get().load(generatedFilePath).into(viewHolder2.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"UTVIKLING ERROR: " + exception.getMessage() );
            }
        });

        viewHolder.image.setImageResource(R.drawable.logo);

        viewHolder.title.setText(Posts.get(i).getTitle());
        //viewHolder.user.setText(getNickName(i));
        viewHolder.user.setText(Posts.get(i).getUser().substring(0,10));


        double secLat = Posts.get(i).getLocation().getLatitude();
        double secLon = Posts.get(i).getLocation().getLongitude();

        Location oldLoc = new Location("oldLoc");
        oldLoc.setLatitude(firLat);
        oldLoc.setLongitude(firLon);

        Location newLoc = new Location("newLoc");
        newLoc.setLatitude(secLat);
        newLoc.setLongitude(secLon);
        double distance = (Math.sqrt(Math.pow((secLat - firLat),2) + Math.pow((secLon - firLon),2))) * 83.57;
        final Post kuk = Posts.get(i);

        viewHolder.distance.setText((new DecimalFormat("#.#").format(oldLoc.distanceTo(newLoc) / 1000) ) + " Km");

        viewHolder.feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapActivityOpenPost.class);

                intent.putExtra("lat", kuk.location.getLatitude());
                intent.putExtra("lon", kuk.location.getLongitude());

                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        RelativeLayout feedLayout;
        TextView distance;
        TextView user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.feedItemImage);
            title = itemView.findViewById(R.id.feedItemTitle);
            feedLayout = itemView.findViewById(R.id.feedItemLayout);
            distance = itemView.findViewById(R.id.feedItemDistance);
            user = itemView.findViewById(R.id.feedItemUser);
        }

    }


    public String getNickName(int i){

        final int j = i;

        String[] tempArrString;
        FirebaseFirestore.getInstance().collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if( Posts.get(j).getUser().equals( document.getId() ) ){
                                    fullEmail = document.get("Email").toString();

                                    Log.w("Neger kuk", fullEmail+" "+document.get("Email").toString() + "nr: "+j);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        if(fullEmail == null){
          //  Log.w("Neger penis", Posts.get(j).getUser());
           // Log.w("Neger penis", Posts.get(j).getTitle());
            Log.w("Neger kuk2", fullEmail+" "+Posts.get(j).getUser() + "nr: "+j);
            return "username";
        }
        /*tempArrString = fullEmail.split("@");

            List<String> stringList = new ArrayList<String>(Arrays.asList(tempArrString));
            return stringList.get(0);*/
           return fullEmail;
        }




}
