package com.rain;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tx
 * @date 2018/8/23
 */
public class RainView extends View {

    private Paint paint;
    //图片处理
    private Matrix matrix;
    private Random random;
    //判断是否运行的，默认没有
    private boolean isRun;
    //表情包集合
    private List<ItemEmoje> bitmapList;
    //表情图片
    private int imgResId = R.mipmap.dog;

    public RainView(Context context) {
        this(context, null);
    }

    public RainView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        matrix = new Matrix();
        random = new Random();
        bitmapList = new ArrayList<>();
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRun) {
            //用于判断表情下落结束，结束即不再进行重绘
            boolean isInScreen = false;
            for (int i = 0; i < bitmapList.size(); i++) {
                matrix.reset();
                //缩放
                matrix.setScale(bitmapList.get(i).scale, bitmapList.get(i).scale);
                //下落过程坐标
                bitmapList.get(i).x = bitmapList.get(i).x + bitmapList.get(i).offsetX;
                bitmapList.get(i).y = bitmapList.get(i).y + bitmapList.get(i).offsetY;
                if (bitmapList.get(i).y <= getHeight()) {//当表情仍在视图内，则继续重绘
                    isInScreen = true;
                }
                //位移
                matrix.postTranslate(bitmapList.get(i).x, bitmapList.get(i).y);
                canvas.drawBitmap(bitmapList.get(i).bitmap, matrix, paint);
            }
            if (isInScreen) {
                postInvalidate();
            }else {
                release();
            }
        }
    }

    /**
     *释放资源
     */
    private void release(){
        if(bitmapList != null && bitmapList.size()>0){
            for(ItemEmoje itemEmoje : bitmapList){
                if(!itemEmoje.bitmap.isRecycled()){
                    itemEmoje.bitmap.recycle();
                }
            }
            bitmapList.clear();
        }
    }

    public void start(boolean isRun) {
        this.isRun = isRun;
        initData();
        postInvalidate();
    }

    private void initData() {
        release();
        for (int i = 0; i < 20; i++) {
            ItemEmoje itemEmoje = new ItemEmoje();
            itemEmoje.bitmap = BitmapFactory.decodeResource(getResources(), imgResId);
            //起始横坐标在[100,getWidth()-100) 之间
            itemEmoje.x = random.nextInt(getWidth() - 200) + 100;
            //起始纵坐标在(-getHeight(),0] 之间，即一开始位于屏幕上方以外
            itemEmoje.y = -random.nextInt(getHeight());
            //横向偏移[-2,2) ，即左右摇摆区间
            itemEmoje.offsetX = random.nextInt(4) - 2;
            //纵向固定下落12
            itemEmoje.offsetY = 12;
            //缩放比例[0.8,1.2) 之间
            itemEmoje.scale = (float) (random.nextInt(40) + 80) / 100f;
            bitmapList.add(itemEmoje);
        }
    }



}
