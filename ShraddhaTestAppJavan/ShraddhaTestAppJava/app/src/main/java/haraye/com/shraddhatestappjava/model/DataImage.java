package haraye.com.shraddhatestappjava.model;

import android.net.Uri;

public class DataImage {
    private String name = null;
    private Uri imageUri = null;
    private Boolean isSaved = false;

    public DataImage(String name, Uri uri, Boolean isSaved){
        this.name = name;
        this.imageUri = uri;
        this.isSaved = isSaved;
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

    public Boolean isSaved() {
        return isSaved;
    }

    public void isSaved(Boolean saved) {
        isSaved = saved;
    }
}
