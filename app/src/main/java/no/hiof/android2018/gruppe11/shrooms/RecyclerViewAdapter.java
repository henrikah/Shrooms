package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Post> Posts = new ArrayList<>();
    private Context mContext;
    private FirebaseFirestore db;

    public RecyclerViewAdapter(ArrayList<Post> mPost, Context mContext) {
        this.Posts= mPost;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_feeditem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.image.setImageResource(R.drawable.logo);
        viewHolder.title.setText(Posts.get(i).getTitle());
        viewHolder.distance.setText(Posts.get(i).getDistance() + " Km");
        viewHolder.user.setText(Posts.get(i).getUser());
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
