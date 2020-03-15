package Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reproductorvideo.ActividadDelReproductor;
import com.example.reproductorvideo.R;
import com.example.reproductorvideo.contenedorVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Interfaz.ClickListenner;
import Model.VideoModel;

public class VideoItemAdapter extends RecyclerView.Adapter {
    contenedorVideo contenedor;
    List<VideoModel> videoModelList= new ArrayList<>();
    Context contexto;
    ClickListenner clickListenner;
    public VideoItemAdapter(contenedorVideo contenedor,List<VideoModel> videoModelList, Context contexto, ClickListenner clickListenner) {
        this.contenedor=contenedor;
        this.videoModelList = videoModelList;
        this.contexto = contexto;
        this.clickListenner = clickListenner;
    }

    private class VideoHolder extends RecyclerView.ViewHolder {
        private TextView titulo;
        private TextView duracion;
        private TextView filePath;
        private ImageView miniatura;
        private TextView visitasVideo;

        private VideoHolder(View view){
            super(view);
            titulo= view.findViewById(R.id.titulo);
            duracion= view.findViewById(R.id.duracion);
            filePath= view.findViewById(R.id.file_path);
            miniatura= view.findViewById(R.id.miniatura);
            visitasVideo= view.findViewById(R.id.txtVisitas);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent .getContext()).inflate(R.layout.item_row,parent,false);
        return new VideoHolder(item);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoHolder videoHolder= (VideoHolder) holder;
        VideoModel videoModel= videoModelList.get(position);
        videoHolder.titulo.setText(videoModel.getFileName());
        videoHolder.filePath.setText(videoModel.getFilePath());

        try {
            videoHolder.visitasVideo.setText("Visitas: "+ViewsAdapter.seeViews(videoModel.getFilePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        MediaPlayer mediaPlayer= MediaPlayer.create(contexto, Uri.fromFile(new File(videoModel.getFilePath())));

        double msec=0;
        if(mediaPlayer!=null)
            msec= mediaPlayer.getDuration();


        double min=(msec%3600)/60;

        videoHolder.duracion.setText(""+String.format("%.2f",min));

        //muestra la miniatura del video
        Glide.with(contexto)
                .load(videoModel.getFilePath())
                .into(videoHolder.miniatura);

        double finalmsec=msec;
        videoHolder.miniatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalmsec==0) {
                    Toast.makeText(contexto, "Video invalido", Toast.LENGTH_SHORT).show();
                }else{
                    clickListenner.onClickItem(videoModel.getFilePath());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoModelList.size();
    }
}
