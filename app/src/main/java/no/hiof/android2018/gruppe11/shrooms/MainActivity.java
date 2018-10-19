package no.hiof.android2018.gruppe11.shrooms;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Sjekker om brukeren allerede er logget inn eller ikke
        if(currentUser != null){
            Intent myIntent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(myIntent);
        }else {

            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        //Gjemmer Actionbaren:
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Post post = new Post("Halla",123,"Kappa1231321321");
        //savePostToDatabase(post);


    }
    public  void savePostToDatabase(Post post){
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("Title",post.getTitle());
        postMap.put("Distance", post.getDistance());
        postMap.put("userID",post.getUser());

        db.collection("Posts").document().set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // det gikk bra
            }
        });
    }
}
