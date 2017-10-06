package com.example.cyc.downloadproject;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.cyc.downloadproject.Adapter.MyFragmentAdapter;
import com.example.cyc.downloadproject.Adapter.TaskAdapter;

import com.example.cyc.downloadproject.Fragment.DoneFragment;

import com.example.cyc.downloadproject.SQL.DBHelper;
import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.Service.DownloadService;
import com.example.cyc.downloadproject.URL.URLDownload;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener
{
    private static final int COMPUTESIZE=1;
    private static final int UPDATEVIEW=2;

    private static final int UPDATESOMEVIEW=4;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton floatingActionButton;
    private CharSequence filename;
    private String URLAddress;
    private ArrayList<URLDownload> listDownloaded=new ArrayList<URLDownload>();
    private ArrayList<URLDownload> listDownloading=new ArrayList<URLDownload>();

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private MyReceive receiver=null;
    private PagerFragment downloadingFragment;
    private DoneFragment doneFragment;
    private Handler handler1;
    private FragmentManager fragmentManager;
    private MyFragmentAdapter fragmentPagerAdapter;
    private List<String> titles = new ArrayList<String>();
    private Sqlite sqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
        initViewPager();

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        sqlite=new Sqlite(this);

        listDownloading=sqlite.getURLDoanloading();
        listDownloaded=sqlite.getURLDoanloaded();
        receiver=new MyReceive();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.cyc.progress");
        registerReceiver(receiver,filter);

        handler1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case UPDATEVIEW:
                        if (fragmentPagerAdapter.getCurrentFragment().equals(downloadingFragment))
                        {
                            downloadingFragment.adapter.updateViewArray(listDownloading);

                        }else {
                            doneFragment.adapter.updateViewArray(listDownloaded);
                        }
                        break;
                    case UPDATESOMEVIEW:
                        int position=msg.arg1;
                        if (fragmentPagerAdapter.getCurrentFragment().equals(downloadingFragment))
                        {
                            downloadingFragment.adapter.updateViewSome(position,listDownloading);

                        }else {
                            doneFragment.adapter.updateViewSome(position,listDownloaded);
                        }
                    default:break;
                }
            }
        };

    }


    @Override
    protected void onDestroy() {
        sqlite.updateURLDownload(listDownloading);
        sqlite.updateURLDownload(listDownloaded);
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        sqlite.closeDB();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        listDownloading=sqlite.getURLDoanloading();
        listDownloaded=sqlite.getURLDoanloaded();
        if(receiver==null){
            receiver=new MyReceive();
            IntentFilter filter=new IntentFilter();
            filter.addAction("com.example.cyc.progress");
            registerReceiver(receiver,filter);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (receiver==null){
            receiver=new MyReceive();
            IntentFilter filter=new IntentFilter();
            filter.addAction("com.example.cyc.progress");
            MainActivity.this.registerReceiver(receiver,filter);
        }*/
    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

   /*     expandableListView = (ExpandableListView) findViewById(R.id.expandableView);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);*/


        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        titles = new ArrayList<String>();
        titles.add("任务");
        titles.add("已下载");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        listDownloading=new ArrayList<URLDownload>();
        fragmentManager = getSupportFragmentManager();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final AlertDialog alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.show();
                View view1 = getLayoutInflater().inflate(R.layout.dialog, null);
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable());
                WindowManager.LayoutParams lm = window.getAttributes();
                lm.gravity = Gravity.CENTER;
                int width = getWindowManager().getDefaultDisplay().getWidth();
                lm.width = width;
                lm.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lm);

                window.setContentView(view1);
                final EditText editText = (EditText) window.findViewById(R.id.url_edit);
                final EditText nameEdit = (EditText) window.findViewById(R.id.name_edit);
                editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            alertDialog.getWindow()
                                    .clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        }
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence!=null){
                        String s=charSequence.toString();
                           try {
                               String start =s.toString().substring(0,1);
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                        filename=s;
                        nameEdit.setText(filename);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        URLAddress=editText.getText().toString();
                        if (URLAddress.contains("/")){
                        try{
                            filename=URLAddress.substring(URLAddress.lastIndexOf("/"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        nameEdit.setText(filename);
                        }
                    }
                });

                nameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
                final Button cancelButton = (Button) window.findViewById(R.id.cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                final Button downloadStartBtn= (Button) window.findViewById(R.id.start_download);
                downloadStartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String url ="http://dldir1.qq.com/music/clntupate/QQMusic72282.apk";
                                //"https://wenku.baidu.com/browse/downloadrec?doc_id=df74fb90daef5ef7ba0d3c8f&
                        // http://rpcs.myapp.com/myapp/rcps/d/10000609/com.tencent.qq.music_10000609_170926144928a.apk"
                        // http://dldir1.qq.com/music/clntupate/QQMusic72282.apk;
                         final Handler handler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                if(msg.what==COMPUTESIZE) {
                                    int filesize=(int)msg.getData().getLong("contentLength");
                                    Toast.makeText(MainActivity.this,"all:"+filesize,Toast.LENGTH_SHORT).show();
                                    Log.d("all",filesize+"en");
                                    long oldTime=System.currentTimeMillis();
                                    start(new URLDownload(url, 1, 0, filesize,oldTime));
                                   /* Message message=new Message();
                                    message.what=UPDATEVIEW;
                                    handler1.sendMessage(message);
                                    Message message1=new Message();
                                    message1.what=SETADAPTER;
                                    handler1.sendMessage(message1);*/
                                }
                            }
                        };

                           new Thread(new Runnable() {
                               @Override
                               public void run() {

                                   try {
                                      synchronized (this){long contentL = Utils.getContentLength(url);
                                          Message message = new Message();
                                          Bundle bundle = new Bundle();
                                          bundle.putLong("contentLength", contentL);
                                          message.what = COMPUTESIZE;
                                          message.setData(bundle);
                                          handler.sendMessage(message);}



                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }).start();
                        final String url1 ="http://s1.music.126.net/download/android/CloudMusic_3.4.1.133604_official.apk";

                        final Handler handler2=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                if(msg.what==COMPUTESIZE) {
                                    int filesize=(int)msg.getData().getLong("contentLength");
                                    Toast.makeText(MainActivity.this,"all:"+filesize,Toast.LENGTH_SHORT).show();
                                    Log.d("all",filesize+"en");
                                    long oldTime=System.currentTimeMillis();
                                    start(new URLDownload(url1, 1, 0, filesize,oldTime));
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    synchronized (this){long contentL = Utils.getContentLength(url1);
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("contentLength", contentL);
                                        message.what = COMPUTESIZE;
                                        message.setData(bundle);
                                        handler2.sendMessage(message);}



                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        alertDialog.dismiss();


                    }
                });
                final Button edit=(Button)window.findViewById(R.id.edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                break;


        }

    }



    @Override
    public void onPageScrollStateChanged(int state) {
       /* if (state==2){
            Message message=new Message();
            message.what=UPDATEVIEW;
            handler1.sendMessage(message);
        }
        else if (state==1){
            Message message=new Message();
            message.what=UPDATEVIEW;
            handler1.sendMessage(message);
        }*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       /* Message message=new Message();
        message.what=UPDATEVIEW;
        handler1.sendMessage(message);*/
    }

    private void start(URLDownload newTask){
        listDownloading.add(newTask);
        downloadingFragment.adapter.tasklist.add(newTask);
        downloadingFragment.adapter.notifyItemInserted(downloadingFragment.adapter.tasklist.size()-1);
        Intent intent=new Intent(MainActivity.this,DownloadService.class);
        intent.putExtra("Url",newTask.URLaddress);
        intent.putExtra("flag",DownloadService.STARTDOWNLOAD);
        intent.putExtra("FileSize",newTask.fileSize);
        startService(intent);
    }


    private void initViewPager() {

        downloadingFragment = new PagerFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("param1", String.valueOf(0));
        downloadingFragment.setArguments(bundle2);
        fragments.add(downloadingFragment);

        doneFragment=new DoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", String.valueOf(1));
        doneFragment.setArguments(bundle);
        fragments.add(doneFragment);

        fragmentPagerAdapter = new MyFragmentAdapter(fragments, titles, fragmentManager, MainActivity.this);
        //给ViewPager设置适配器
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(viewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        fragmentPagerAdapter.setPrimaryItem(viewPager,0,downloadingFragment);

    }

    class MyReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(DownloadService.UPDATEPRGRESS)) {
                String url = intent.getStringExtra("Url");
                int progress = intent.getIntExtra("progress", 0);
                for (int i = 0; i < listDownloading.size(); i++) {
                    URLDownload task = listDownloading.get(i);
                    if (task.URLaddress.equals(url)) {
                        task.downloadLength = progress;
                        downloadingFragment.adapter.uptedaProgress(url, task.downloadLength);

                      /*     Message message=new Message();
                           message.what=UPDATESOMEVIEW;
                           message.arg1=i;
                           handler1.sendMessage(message);*/
                    }
                }
            } else if (intent.getAction().equals(DownloadService.FINISHED)) {
                String url = intent.getStringExtra("Url");
                for (int i = 0; i < listDownloading.size(); i++) {
                    URLDownload task = listDownloading.get(i);
                    if (task.URLaddress.equals(url)) {
                        task.state = 3;
                        listDownloaded.add(task);
                        listDownloading.remove(task);
                        downloadingFragment.adapter.remove(url);

                        doneFragment.adapter.add(task);
                    }
                }
            /*else if (intent.getAction().equals("com.example.cyc.progress1")) {
                Bundle bundle = intent.getExtras();
                String url = bundle.getString("Url");
                Long progress = bundle.getLong("progress");

                for (int i = 0; i < listDownloading.size(); i++) {
                    URLDownload task = listDownloading.get(i);
                    if (task.URLaddress.equals(url)) {
                        task.fileSize = progress;
                    }
                }
            }*/
            }
        }
    }

}
