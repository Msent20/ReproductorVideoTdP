package Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductorvideo.R;

public class VideoModel {
    String fileName;
    String filePath;

    public VideoModel(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
