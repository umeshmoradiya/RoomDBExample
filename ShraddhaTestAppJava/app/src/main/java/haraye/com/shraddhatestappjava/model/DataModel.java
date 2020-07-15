package haraye.com.shraddhatestappjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "datamodel")
public class DataModel {
    @PrimaryKey(autoGenerate = true)
    public int dataid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "image_name")
    public String image_name;

    @ColumnInfo(name = "audio")
    public String audio;

    @ColumnInfo(name = "audio_name")
    public String audio_name;

    @ColumnInfo(name = "description")
    public String description;
}
