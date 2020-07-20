package haraye.com.shraddhatestappjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import haraye.com.shraddhatestappjava.model.DataModel;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataViewHolder> {

    private OnItemClickListner onItemClickListner;

    class DataViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTitle;
        private final TextView dataDescription;
        private final ImageView dataImage;
        private final ImageView dataAudioImage;

        private DataViewHolder(View itemView, final OnItemClickListner listner) {
            super(itemView);
            dataTitle = itemView.findViewById(R.id.txt_title);
            dataDescription = itemView.findViewById(R.id.txt_desc);
            dataImage = itemView.findViewById(R.id.img_data);
            dataAudioImage = itemView.findViewById(R.id.ic_music);

            dataAudioImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onAudioClick(mDataModels.get(position));

                        }
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                if(listner != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listner.onItemClick(mDataModels.get(position));

                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if(listner != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listner.onItemLongClick(mDataModels.get(position));

                    }
                }
                return true;
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<DataModel> mDataModels; // Cached copy of words

    DataListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DataViewHolder(itemView, onItemClickListner);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        if (mDataModels != null) {
            DataModel current = mDataModels.get(position);
            holder.dataTitle.setText(current.title);
            holder.dataDescription.setText(current.description);
            try {
                File imgFile = new File(current.image.get(0));

                if (imgFile.exists()) {

                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.dataImage.setImageBitmap(bitmap);

                }
            } catch (Exception e) {
                holder.dataImage.setImageResource(R.drawable.ic_photo_camera_black_24dp);
                e.printStackTrace();
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.dataTitle.setText("No Title");
            holder.dataDescription.setText("No Description");
        }
    }

    void setData(List<DataModel> dataModels) {
        mDataModels = dataModels;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mDataModels != null)
            return mDataModels.size();
        else return 0;
    }

    public interface OnItemClickListner {
        void onItemClick(DataModel data);
        void onItemLongClick(DataModel data);
        void onAudioClick(DataModel data);

    }

    public void setOnItemClickListner(OnItemClickListner listner){
        onItemClickListner = listner;
    }
}