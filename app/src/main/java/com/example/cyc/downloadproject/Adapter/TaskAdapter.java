package com.example.cyc.downloadproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyc.downloadproject.Fragment.RecyclerItemTouchHelperRight;
import com.example.cyc.downloadproject.Fragment.RecyclerItemTouchHelperRight;
import com.example.cyc.downloadproject.R;

import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.Service.DownloadService;
import com.example.cyc.downloadproject.Data.URLDownload;
import com.example.cyc.downloadproject.Data.Utils;

import java.util.ArrayList;

/**
 * Created by cyc on 17-10-1.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
        implements RecyclerItemTouchHelperRight.ItemTouchHelperCallback{

    public ArrayList<URLDownload>tasklist=null;
    private Context context;
    Sqlite sqlite;

    private OnItemClickListener onItemClickListener=null;
    public static interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
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


    } public TaskAdapter(ArrayList<URLDownload> tasklist, Context context){
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
        sqlite=new Sqlite(context);
        switch (Utils.getCategory(filename)){
            case Utils.APK:
                holder.fileCategory.setImageResource(R.drawable.ext_program);
                break;
            case Utils.MP3:
                holder.fileCategory.setImageResource(R.drawable.ext_music);
                break;
            case Utils.MP4:
                holder.fileCategory.setImageResource(R.drawable.ext_video);
                break;
            case Utils.OTHER:
                holder.fileCategory.setImageResource(R.drawable.ext_other);
                break;
            case Utils.PIC:
                holder.fileCategory.setImageResource(R.drawable.ext_image);
                break;
            case Utils.TXT:
                holder.fileCategory.setImageResource(R.drawable.ext_text);
                break;
            case Utils.ZIP:
                holder.fileCategory.setImageResource(R.drawable.ext_archive);
                break;

            default:break;
        }


            holder.filename.setText(filename);
            switch (task.state){
                case 1:
                    holder.downloadorpause.setImageResource(R.drawable.stat_stop);
                    break;
                case 2:
                    holder.downloadorpause.setImageResource(R.drawable.stat_start);
                    break;
                default:break;

            }
            holder.itmview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    URLDownload task=tasklist.get(position);
                    if (task.state!=3) {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(),DownloadService.class);
                        intent.putExtra("Url", task.getURLaddress());
                        intent.putExtra("flag", DownloadService.CHANGESTATE);
                        intent.putExtra("FileSize",task.fileSize);
                        view.getContext().startService(intent);
                        switch (task.state)
                        {
                            case 1:holder.downloadorpause.setImageResource(R.drawable.stat_start);
                                task.state=2;
                                notifyDataSetChanged();
                                Toast.makeText(context,task.getFileName()+"暂停下载",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:holder.downloadorpause.setImageResource(R.drawable.stat_stop);
                                task.state=1;
                                notifyDataSetChanged();
                                Toast.makeText(context,task.getFileName()+"继续下载",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        String directory = Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_DOWNLOADS).getPath();
                        String filename = task.URLaddress.substring(task.URLaddress.lastIndexOf("/") + 1);
                        String name=directory+filename;
                        Uri uri=Uri.fromParts("package",name,null);
                        Intent intent=new Intent(Intent.ACTION_PACKAGE_ADDED,uri);
                        context.startService(intent);

                    }
                }
            });
            int progress=(int)(task.downloadLength*100.0/(task.fileSize));
            holder.progressBar.setProgress(progress);

            holder.fileSizeTv.setText(Utils.getFileSize(task.fileSize));
            holder.downloadLength.setText(Utils.getFileSize(task.downloadLength));
    }

    @Override
    public void onItemDalete(int position) {
        URLDownload urlDownload=tasklist.get(position);
        Intent intent = new Intent();
        intent.setClass(context,DownloadService.class);
        intent.putExtra("Url", urlDownload.getURLaddress());
        intent.putExtra("flag", DownloadService.DALETEDOWNLOAD);
        intent.putExtra("FileSize",urlDownload.fileSize);
        context.startService(intent);

        sqlite.deleteURLDownload(urlDownload.URLaddress);
        tasklist.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }
    public void updateView(String url,int progress){
        for (int i=0;i<tasklist.size();i++){
            URLDownload task=tasklist.get(i);
            if (task.URLaddress.equals(url)){
                task.setDownloadLength(progress);
                notifyItemChanged(i,1);
            }
        }
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

    public void uptedaProgress(String url,int progress){
        for (int i=0;i<tasklist.size();i++) {
            URLDownload urlDownload=tasklist.get(i);
if (urlDownload.URLaddress.equals(url))
     urlDownload.setDownloadLength(progress);
            notifyItemChanged(i);
        }
    }
    public void remove(String url){
        for (int i=0;i<tasklist.size();i++){
            URLDownload urlDownload=tasklist.get(i);
            if (urlDownload.URLaddress.equals(url)){
                notifyItemRemoved(i);
                tasklist.remove(urlDownload);
            }
        }
    }
}
