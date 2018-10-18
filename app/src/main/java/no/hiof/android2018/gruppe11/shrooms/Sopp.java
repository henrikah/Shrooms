package no.hiof.android2018.gruppe11.shrooms;


import java.util.ArrayList;
import java.util.List;

public class Sopp {

    private String name;
    private int imageId;

    public String getname() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<Sopp> getData() {

        List<Sopp> data = new ArrayList<>();

        int[] images = {
                R.drawable.sopp1, R.drawable.sopp1,
                R.drawable.sopp1, R.drawable.sopp1,
                R.drawable.sopp1, R.drawable.sopp1,
                R.drawable.sopp1, R.drawable.sopp1,
                R.drawable.sopp1, R.drawable.sopp1
        };

        String[] names = {
                "Fluesopp1", "Fluesopp2",
                "Fluesopp3", "Fluesopp4",
                "Fluesopp5", "Fluesopp6",
                "Fluesopp7", "Fluesopp8",
                "Fluesopp9", "Fluesopp10"
        };

        for (int i = 0; i < images.length; i++) {

            Sopp current = new Sopp();
            current.setName(names[i]);
            current.setImageId(images[i]);
            data.add(current);
        }

        return data;
    }
}