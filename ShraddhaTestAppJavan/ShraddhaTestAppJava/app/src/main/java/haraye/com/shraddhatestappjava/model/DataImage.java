package haraye.com.shraddhatestappjava.model;

import android.net.Uri;

public class DataImage {
    private String name = null;
    private Uri imageUri = null;

    public DataImage(String name, Uri uri){
        this.name = name;
        this.imageUri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
