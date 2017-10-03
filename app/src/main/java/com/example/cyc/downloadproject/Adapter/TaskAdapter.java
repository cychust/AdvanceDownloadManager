package com.example.cyc.downloadproject.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.R;

import com.example.cyc.downloadproject.URL.URLDownload;
import com.example.cyc.downloadproject.Utils;

import java.util.List;

/**
 * Created by cyc on 17-10-1.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<URLDownload>tasklist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView downloadorpause;
        TextView filename;
        ImageView fileCategory;
        LinearLayout container;
        ProgressBar progressBar;
        public ViewHolder(View view){
            super(view);
            downloadorpause=(ImageView)view.findViewById(R.id.download_pause);
            filename=(TextView)view.findViewById(R.id.file_name);
            fileCategory=(ImageView)view.findViewById(R.id.file_category);
            container=(LinearLayout)view.findViewById(R.id.ll_pb);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        }


    } public TaskAdapter(List<URLDownload> tasklist){
        this.tasklist=tasklist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_download,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        URLDownload task=tasklist.get(position);
        String filename=task.URLaddress.substring(task.URLaddress.lastIndexOf("/")+1);
        if(Utils.getCategory(filename)==Utils.APK){
            holder.fileCategory.setImageResource(R.drawable.ext_program);
        }else if (Utils.getCategory(filename)==Utils.TXT){
            holder.fileCategory.setImageResource(R.drawable.ext_text);
        }else if (Utils.getCategory(filename)==Utils.OTHER){
            holder.fileCategory.setImageResource(R.drawable.ext_other);
        }else if (Utils.getCategory(filename)==Utils.PIC){
            holder.fileCategory.setImageResource(R.drawable.ext_image);
        }else if (Utils.getCategory(filename)==Utils.ZIP){
            holder.fileCategory.setImageResource(R.drawable.ext_archive);
        }else if (Utils.getCategory(filename)==Utils.MP3){
            holder.fileCategory.setImageResource(R.drawable.ext_music);
        }else if (Utils.getCategory(filename)==Utils.MP4){
            holder.fileCategory.setImageResource(R.drawable.ext_video);
        }
        holder.filename.setText(filename);
        switch (task.state){
            case 2:
                holder.downloadorpause.setImageResource(R.drawable.stat_stop);
                break;
            case 1:
                holder.downloadorpause.setImageResource(R.drawable.stat_start);
                break;
            case 3:
                holder.downloadorpause.setImageResource(R.drawable.stat_full);
            default:break;

        }
        int progress=(int)(task.downloadLength*100/task.fileSize);
        holder.progressBar.setProgress(progress);

    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }
    public void updateView(String url,long progress){
        for (URLDownload task:tasklist){
            if (task.URLaddress.equals(url)){
                task.setDownloadLength(progress);
            }
        }
        notifyDataSetChanged();
    }
}
