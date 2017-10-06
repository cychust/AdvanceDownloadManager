package com.example.cyc.downloadproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cyc.downloadproject.R;

import com.example.cyc.downloadproject.Service.DownloadService;
import com.example.cyc.downloadproject.URL.URLDownload;
import com.example.cyc.downloadproject.Utils;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by cyc on 17-10-1.
 */

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.ViewHolder>{

    public ArrayList<URLDownload>tasklist=null;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView downloadorpause;
        TextView filename;
        ImageView fileCategory;
        LinearLayout container;
        ProgressBar progressBar;
        TextView fileSizeTv;

        TextView downloadLength;
        View itmview;
        public ViewHolder(View view){
            super(view);
            itmview=view;
            downloadorpause=(ImageView) view.findViewById(R.id.download_pause);
            filename=(TextView)view.findViewById(R.id.file_name);
            fileCategory=(ImageView)view.findViewById(R.id.file_category);
            container=(LinearLayout)view.findViewById(R.id.ll_pb);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            fileSizeTv=(TextView)view.findViewById(R.id.fileSize);

            downloadLength=(TextView)view.findViewById(R.id.downloadLength);
        }


    } public DoneAdapter(ArrayList<URLDownload> tasklist, Context context){
        this.tasklist=tasklist;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_download,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

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

                holder.downloadorpause.setImageResource(R.drawable.stat_full);
        holder.itmview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                URLDownload task=tasklist.get(position);

                    String directory = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS).getPath();
                    String filename = task.URLaddress.substring(task.URLaddress.lastIndexOf("/") + 1);
                    String name=directory+filename;
               /* File file=new File(name);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(view.getContext(),"com.example.cyc.downloadproject",file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                if (view.getContext()!=null) {
                    Log.d("context","sucess");
                    view.getContext().startActivity(intent);
                }*/
                   // view.getContext().startService(Utils.ApkFile(name));
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(new File(name));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                view.getContext().startActivity(intent);
            }
        });

        holder.progressBar.setProgress(100);

        holder.fileSizeTv.setText(Utils.getFileSize(task.fileSize));
        holder.downloadLength.setText(Utils.getFileSize(task.fileSize));
    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }
    public void updateViewArray(ArrayList<URLDownload> lists){
        this.tasklist=lists;
        notifyDataSetChanged();


    }
    public void updateViewSome(int position,ArrayList<URLDownload>lists){
        if (lists.size()<tasklist.size()){
            notifyItemRemoved(position);
            this.tasklist=lists;
        }
        else if (lists.size()>tasklist.size()){
            this.tasklist=lists;
            notifyItemInserted(tasklist.size());
        }
    }

    public void uptedaProgress(int id,int progress,double speed){
        URLDownload urlDownload=tasklist.get(id);

        if (urlDownload.fileSize*2==progress){
            notifyItemRemoved(id);
        }else {
            urlDownload.setDownloadLength(progress);
            notifyItemChanged(id);
            notifyDataSetChanged();
        }
    }
    public void add(URLDownload task){
        tasklist.add(task);
        notifyItemInserted(tasklist.size()-1);
    }
}
