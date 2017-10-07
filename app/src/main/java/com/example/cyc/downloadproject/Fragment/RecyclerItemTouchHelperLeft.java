package com.example.cyc.downloadproject.Fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.cyc.downloadproject.Application.MyApplication;
import com.example.cyc.downloadproject.R;

/**
 * Created by cyc on 17-10-7.
 */

public class RecyclerItemTouchHelperLeft extends ItemTouchHelper.Callback {
    private static final String TAg="RecyclerItemTouchHelperLeft";
    private final ItemTouchHelperCallback helperCallback;
    public RecyclerItemTouchHelperLeft(ItemTouchHelperCallback helperCallback){
        this.helperCallback=helperCallback;
    }



    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        helperCallback.onItemDalete(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
            View view= viewHolder.itemView;
            Resources resources= MyApplication.getContext().getResources();
            Bitmap bitmap= BitmapFactory.decodeResource(resources, R.drawable.stat_error);
            int padding=10;
            int maxDrawWidth=2*padding+bitmap.getWidth();//最大绘制宽度
            Paint paint=new Paint();
            paint.setColor(resources.getColor(R.color.deleteBackground));
            int x=Math.round(Math.abs(dX));
            int drawWidth=Math.min(x,maxDrawWidth);//实际绘画宽度
            int itemTop=view.getBottom()-view.getHeight();

            //
            if (dX<0){
                c.drawRect(view.getRight(),itemTop,drawWidth,view.getBottom(),paint);
                if (x>padding){
                    Rect rect=new Rect();
                    rect.left=view.getRight();
                    rect.top=itemTop+(view.getBottom()-itemTop-bitmap.getHeight()/2);
                    int maxLeft=rect.left+bitmap.getWidth();
                    rect.right=Math.min(x,maxLeft);
                    rect.bottom=rect.top+bitmap.getHeight();
                    Rect rect1=null;
                    if (x<maxLeft){
                        rect1=new Rect();
                        rect1.left=view.getRight();
                        rect1.top=0;
                        rect1.bottom=bitmap.getHeight();
                        rect1.right=rect1.left+bitmap.getWidth()-padding;
                    }
                    c.drawBitmap(bitmap,rect1,rect,paint);
                }
                view.setTranslationX(dX);
            }
        }
    }

    public interface ItemTouchHelperCallback{
        void onItemDalete(int position);
    }
}
