package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Post> Posts = new ArrayList<>();
    private Context mContext;
    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    StorageReference httpsReference;
    StorageReference mushroomImagesRef;
    String pictureID;
    private static final String TAG = "RecyclerViewAdapter";
    FileDownloadTask.TaskSnapshot snapshot1;


    double firLat;
    double firLon;


    //Db forsøk 2



    public RecyclerViewAdapter(ArrayList<Post> mPost, Context mContext) {
        this.Posts= mPost;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SingleShotLocationProvider.requestSingleUpdate(mContext,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        firLat = location.latitude;
                        firLon = location.longitude;
                    }
                });



        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_feeditem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        https://console.firebase.google.com/project/shrooms-462ab/storage/shrooms-462ab.appspot.com/files~2FbrukerBilder~2F
        pictureID = Posts.get(i).getBildeNavn();
        final ViewHolder viewHolder2 = viewHolder;
        mushroomImagesRef = mStorageRef.child("brukerBilder/"+pictureID);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String bildeNavnUtenJPG = pictureID.substring(0, pictureID.length() - 4);


        mStorageRef.child("brukerBilder/"+pictureID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imgURL = uri.toString();
                Glide.with( viewHolder.image).load(imgURL).into(viewHolder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



            /*try {
                final File localFile = File.createTempFile(bildeNavnUtenJPG, "png");

                final ViewHolder viewHolder2 = viewHolder;
                Log.d(TAG,"LocalFile funket fint.");
                mStorageRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                viewHolder2.image.setImageResource(localFile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...

                    }
                });
            }
            catch (IOException e){
                e.getMessage();
                Log.d(TAG,"LocalFile fikk feil");
            }*/


        viewHolder.image.setImageResource(R.drawable.logo);

        viewHolder.title.setText(Posts.get(i).getTitle());

        viewHolder.user.setText(Posts.get(i).getUser());

        double secLat = Posts.get(i).getLocation().getLatitude();
        double secLon = Posts.get(i).getLocation().getLongitude();

        double distance = (Math.sqrt(Math.pow((secLat - firLat),2) + Math.pow((secLon - firLon),2))) * 110.57;
        viewHolder.distance.setText(distance + " Km");
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



}
