package no.hiof.android2018.gruppe11.shrooms;


import android.app.Activity;
import android.content.Intent;


public class FilePick {
    private static final int READ_REQUEST_CODE = 42;

    public  static void getPhoto(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

       activity.startActivityForResult(intent, READ_REQUEST_CODE);

    }
    // Eksempel på implementering av bildehåndtering i activityen som kaller på denne klassen
    /*      @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            Bitmap bm;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                object.setImage(bm);
            }
        }*/

}
