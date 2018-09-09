> 文章链接：[https://mp.weixin.qq.com/s/yQXn-YjEFSW1X7A7CcuaVg](https://mp.weixin.qq.com/s/yQXn-YjEFSW1X7A7CcuaVg)

众所周知，微信聊天中我们输入一些关键词会有表情雨下落，比如输入「生日快乐」「么么哒」会有相应的蛋糕、亲吻的表情雨下落，今天就来完成这个表情雨下落的效果。   
先来看下效果，真·狗头雨·落！  

![](https://user-gold-cdn.xitu.io/2018/9/8/165b78219baf8137?w=265&h=433&f=gif&s=1751555)   

确认表情的模型，定义属性
```
public class ItemEmoje {
    //坐标
    public int x;
    public int y;
    // 横向偏移
    public int offsetX;
    //纵向偏移
    public int offsetY;
    //缩放
    public float scale;
    //图片资源
    public Bitmap bitmap;
}
```
自定义RainView 表情下落视图，初始化变量。
```
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
}
```
初始化表情雨数据，确认每个表情的起始位置，下落过程中横向、纵向的偏移，以及缩放大小。
```
private void initData() {
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
```
下落过程通过 onDraw进行绘制，不断的计算横纵坐标，达到下落效果。
```
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
```
提供start() 方法触发。
```
public void start(boolean isRun) {
    this.isRun = isRun;
    initData();
    postInvalidate();
}
```
布局文件
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.rain.RainView
        android:id="@+id/testView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <Button
        android:id="@+id/btn_dog"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:text="真·狗头雨·落！" />

    <Button
        android:id="@+id/btn_cake"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_marginLeft="10dp"
        android:layout_toRightOf="@+id/btn_dog"
        android:text="蛋糕雨" />

</RelativeLayout>
```
activity 点击事件触发
```

btnCake.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //蛋糕图片
        rainView.setImgResId(R.mipmap.cake);
        rainView.start(true);
    }
});
btnDog.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //狗头图片
        rainView.setImgResId(R.mipmap.dog);
        rainView.start(true);
    }
});
```


github地址：[https://github.com/taixiang/rain_emoji](https://github.com/taixiang/rain_emoji)

欢迎关注我的博客：[https://www.manjiexiang.cn/](https://www.manjiexiang.cn/)  

更多精彩欢迎关注微信号：春风十里不如认识你  
一起学习，一起进步，有问题随时联系，一起解决！！！

![](https://user-gold-cdn.xitu.io/2018/8/12/1652cd77eaebeb98?w=900&h=540&f=jpeg&s=64949)    
