package no.hiof.android2018.gruppe11.shrooms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import no.hiof.android2018.gruppe11.shrooms.adapter.MushroomAdapter;
import no.hiof.android2018.gruppe11.shrooms.enumerator.BottomSheetItemType;
import no.hiof.android2018.gruppe11.shrooms.firebase.schema.PostSchema;
import no.hiof.android2018.gruppe11.shrooms.firebase.schema.ShroomSchema;
import no.hiof.android2018.gruppe11.shrooms.global.Permission;
import no.hiof.android2018.gruppe11.shrooms.map.MapThumbnail;

public class NewPostFragment extends Fragment implements BottomSheetFragment.Listener, MapLocationSelectFragment.OnLocationConfirmed, CameraFragment.OnPictureConfirmed {
    private static final String TAG = NewPostFragment.class.getSimpleName();
    /*
        Firebase
     */
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    /*
        Layout
     */
    private ImageView mushroomPic;
    private View v;
    private EditText title, description;
    private Button submitPostButton;
    private Spinner spinner;
    private SupportMapFragment mapFragment;

    /*
        Saved data
     */

    private double lat, lng;
    private int selectedMushroom = 0;
    private ArrayList<ShroomSchema> mushroomList;
    private byte[] mushroomPicBytes;
    private boolean locationConfirmed;
    private int zoom = 12;
    private Uri downloadUri;
    private UploadTask uploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newpost,container,false);
        mushroomList = new ArrayList<>();
        mushroomPic = v.findViewById(R.id.thumbnailView);
        submitPostButton = v.findViewById(R.id.submitPostButton);
        spinner = v.findViewById(R.id.mushroom_type);
        title = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                selectedMushroom = i;
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {
                selectedMushroom = 0;
            }
        });

        Button updateLocationButton = v.findViewById(R.id.updateLocationButton);
        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(getString(R.string.select_on_map));
                arrayList.add(getString(R.string.use_current_location));
                BottomSheetFragment.newInstance(arrayList, BottomSheetItemType.MAP).show(getChildFragmentManager(), "LocationTypeSelector");
            }
        });

        mushroomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(getString(R.string.take_picture_with_camera));
                arrayList.add(getString(R.string.get_picture_from_storage));
                BottomSheetFragment.newInstance(arrayList, BottomSheetItemType.PICTURE).show(getChildFragmentManager(), "PictureTypeSelector");
            }
        });
        submitPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMushroomPicture();
                submitPostButton.setEnabled(false);
            }
        });
        retrieveMushrooms();
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.postMap);
        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));

    }

    private void retrieveMushrooms(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query mushroomQuery = db.collection("Shrooms");

        mushroomQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()){
                        mushroomList.add(new ShroomSchema(document.getId(), (String)document.get("Name"), (String)document.get("Description"), (boolean)document.get("Poisonous")));
                    }
                    spinner.setAdapter(new MushroomAdapter(getContext(), mushroomList));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_retrieving_mushrooms), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*
        Metoden for å da imot bilde fra filepicker
     */
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            Bitmap bm;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    mushroomPicBytes = stream.toByteArray();
                    mushroomPic.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void updateLocation() {
        SingleShotLocationProvider.requestSingleUpdate(this, getActivity(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        lat = location.latitude;
                        lng = location.longitude;
                        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
                        locationConfirmed = true;
                    }
                });
    }
    public void createPost(){

        String titleText = title.getText().toString();
        String descText = description.getText().toString();
        mUser = mAuth.getCurrentUser();
        long timeStamp = System.currentTimeMillis();
        GeoPoint location = (locationConfirmed) ? new GeoPoint(lat, lng) : null;
        PostSchema post = new PostSchema(titleText, descText, mushroomList.get(selectedMushroom).getUid(), downloadUri.toString(), location, timeStamp, mUser.getUid());
        savePost(post);
    }

    // Kode for å lagre bilde til Firebase Storage
    public void uploadMushroomPicture() {
        // Om brukeren ikke har valgt et profilbilde sender vi en tom uri
        if(mushroomPicBytes == null) {
            downloadUri = Uri.parse("");
            createPost();
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference imageReference = storage.getReference().child("postPicture/" + user.getUid() + System.currentTimeMillis() + ".png");

            uploadTask = imageReference.putBytes(mushroomPicBytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    /*
                        Om opplastningen ikke fullførte så fullfører vi uansett registreringen men med tomt bilde.
                     */
                    downloadUri = Uri.parse("");
                    createPost();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return imageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                                createPost();
                            }
                        }
                    });
                }
            });
        }
    }

    public void savePost(PostSchema post){

        Map<String, Object> postMap = post.getFull();

        db.collection("Posts").document()
                .set(postMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent myIntent = new Intent(getActivity(), bottomNavTest.class);
                        startActivity(myIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                submitPostButton.setEnabled(true);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_creating_post), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemClicked(int position, BottomSheetItemType type) {
        if(type == BottomSheetItemType.MAP) {
            if(position == 0) {
                /*
                    Har valgt kart
                  */
                Bundle args = new Bundle();
                args.putDouble("latitude", lat);
                args.putDouble("longitude", lng);
                MapLocationSelectFragment dialog = new MapLocationSelectFragment();
                dialog.setArguments(args);
                dialog.show(getChildFragmentManager(), MapLocationSelectFragment.TAG);
            } else if(position == 1) {
                /*
                    Har valgt å bruke locationservice
                  */
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Permission.GET_LOCATION_REQUEST_CODE);
                } else {
                    updateLocation();
                }
            }
        } else if(type == BottomSheetItemType.PICTURE) {
            if(position == 0) {
                /*
                    Har valgt kamera
                 */
                CameraFragment dialog = new CameraFragment();
                dialog.show(getChildFragmentManager(), CameraFragment.TAG);
            } else if(position == 1) {
                /*
                    Har valgt filepicker
                 */
                FilePick.getPhoto(this);
            }
        }
    }

    @Override
    public void onPictureConfirmed(byte[] picture, Size size) {
        mushroomPicBytes = picture;
        mushroomPic.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));
    }


    @Override
    public void onLocationConfirmed(double latitude, double longitude) {
        lat = latitude;
        lng = longitude;
        mapFragment.getMapAsync(new MapThumbnail(lat, lng, zoom));
        locationConfirmed = true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == Permission.GET_LOCATION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.allow_access_to_location_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}






