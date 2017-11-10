package com.lyx.sample.entity;

import com.lyx.frame.annotation.ImageUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Image
 * <p>
 * Created by luoyingxing on 2017/9/25.
 */

public class Image {
    private String title;
    @ImageUrl
    private String url;
    private int type;

    public Image() {
    }

    public Image(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Image(String title, String url, int type) {
        this.title = title;
        this.url = url;
        this.type = type;
    }

    public static List<Image> getImageList() {
        List<Image> list = new ArrayList<>();
        list.add(new Image("火影忍者动漫壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=1945566334,2246200765&fm=27&gp=0.jpg", 1));
        list.add(new Image("狗狗 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://imgstore.cdn.sogou.com/app/a/100540002/609752.jpg", 2));
        list.add(new Image("英雄联盟高清游戏图片桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://pic1.win4000.com/wallpaper/c/5804640eeb375.jpg", 3));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img3.imgtn.bdimg.com/it/u=428230071,3244237116&fm=200&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img4.imgtn.bdimg.com/it/u=3737919190,3601406056&fm=27&gp=0.jpg", 3));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=4135556321,3079210894&fm=27&gp=0.jpg", 1));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=350286811,2708244372&fm=200&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=2487503658,1494418591&fm=200&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img2.imgtn.bdimg.com/it/u=576945198,1322704226&fm=11&gp=0.jpg", 3));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=4127645007,1273356270&fm=27&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=2683431478,4189181113&fm=27&gp=0.jpg", 1));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img3.imgtn.bdimg.com/it/u=2035340593,2888279388&fm=27&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img0.imgtn.bdimg.com/it/u=394962981,3860542191&fm=27&gp=0.jpg", 2));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img0.imgtn.bdimg.com/it/u=3523099303,3893734731&fm=27&gp=0.jpg", 3));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img3.imgtn.bdimg.com/it/u=569927511,3767989042&fm=11&gp=0.jpg", 1));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=751652762,2322387069&fm=27&gp=0.jpg", 3));
        list.add(new Image("桌面壁纸 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=2408941929,751522124&fm=11&gp=0.jpg", 3));
        return list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
