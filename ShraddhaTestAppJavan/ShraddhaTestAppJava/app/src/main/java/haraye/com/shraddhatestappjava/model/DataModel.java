package haraye.com.shraddhatestappjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "datamodel")
public class DataModel {
    @PrimaryKey(autoGenerate = true)
    public int dataid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @Ignore
    public ArrayList<String> image;

    @Ignore
    public ArrayList<String> audio;


}
