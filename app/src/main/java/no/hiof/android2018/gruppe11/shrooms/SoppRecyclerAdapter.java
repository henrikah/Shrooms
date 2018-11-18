package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;


public class SoppRecyclerAdapter extends RecyclerView.Adapter<SoppRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SoppRecycler";
    private FirebaseFirestore db;


    private List<Sopp> soppListe;
    private LayoutInflater layoutInflater;

    public SoppRecyclerAdapter(Context context, List<Sopp> soppListe) {
        layoutInflater = LayoutInflater.from(context);

        this.soppListe = soppListe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        db = FirebaseFirestore.getInstance();

        View itemView = layoutInflater.inflate(R.layout.layout_soppitem, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        Sopp soppShow = soppListe.get(i);
        viewHolder.setSopp(soppShow);
    }

    @Override
    public int getItemCount() {

        return soppListe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text;
        TextView text2;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.soppBilde);
            text2 = itemView.findViewById(R.id.soppText2);
            text = itemView.findViewById(R.id.soppText);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

        public void setSopp(Sopp sopp){
            image.setImageResource(R.drawable.sopp1);
            text.setText(sopp.getNavn());
            text2.setText(sopp.getBeskrivelse());

        }
    }


}
