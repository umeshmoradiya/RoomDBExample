package haraye.com.shraddhatestappjava.model;

import android.net.Uri;

public class DataImage {
    private String name = null;
    private Uri imageUri = null;
    private Boolean saved = false;

    public DataImage(String name, Uri uri, Boolean isSaved){
        this.name = name;
        this.imageUri = uri;
        this.saved = isSaved;
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

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }
}
