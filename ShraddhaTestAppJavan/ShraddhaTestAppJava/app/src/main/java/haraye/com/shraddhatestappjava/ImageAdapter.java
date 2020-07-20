package haraye.com.shraddhatestappjava;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import haraye.com.shraddhatestappjava.model.DataImage;

public class ImageAdapter extends BaseAdapter {

    ArrayList<DataImage> dataImages = new ArrayList<>();
    Context context = null;

    ImageAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataImages.size();
    }

    @Override
    public Object getItem(int position) {
        return dataImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataImage data = this.dataImages.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dataView = inflater.inflate(R.layout.gridview_image_item, null);
        ImageView imageView = dataView.findViewById(R.id.imgData);
        imageView.setImageURI(data.getImageUri());

        return dataView;
    }

    public void addData(DataImage dataImage){
        if(dataImages == null)
            dataImages = new ArrayList<>();

        dataImages.add(dataImage);
        notifyDataSetChanged();
    }

    public void addAllImages(List<DataImage> dataImagess){
        dataImages = new ArrayList<>();
        dataImages.addAll(dataImagess);
        notifyDataSetChanged();
    }

    public List<DataImage> getAllImages(){
        return this.dataImages;
    }
}
