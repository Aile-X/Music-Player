package com.aile.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

//��ȡͼƬ����
public class BitmapUtils {
	//��ȡָ��·��ͼƬ.
	public static Bitmap getBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	//��ȡ��С�ߴ��ͼƬ.
	public static Bitmap getBitmap(String path, int width, int height) {
		Bitmap bm = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeFile(path);
		int x = opts.outWidth / width;
		int y = opts.outHeight / height;
		opts.inSampleSize = x > y ? x : y;
		opts.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path);
		return bm;
	}
}
