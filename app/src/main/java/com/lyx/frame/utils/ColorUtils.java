package com.lyx.frame.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;

import java.util.Random;

/**
 * ColorUtils
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class ColorUtils {
    public static View getViewByPosition(ListView listView, int position) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /**
     * 返回随机不重复的5种颜色
     */
    public static int[] getRandomColors() {
        int[] colors = {android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple,
        };
        Random rand = new Random();
        int count = colors.length;
        for (int i = 0; i < colors.length; i++) {
            int index = rand.nextInt(count--);
            if (index == count) continue;
            colors[index] = colors[index] ^ colors[count];
            colors[count] = colors[count] ^ colors[index];
            colors[index] = colors[index] ^ colors[count];
        }
        return colors;
    }

    public static int getRandomHexColor() {
        Integer color = new Random().nextInt();
        String colorStr = String.format("#%08x", color).toUpperCase();
        return Color.parseColor(colorStr);
    }

    public static SpannableStringBuilder shadeColorText(Context context, CharSequence text) {
        int[] colors = getRandomColors();
        int startColor = context.getResources().getColor(colors[0]);
        int endColor = context.getResources().getColor(colors[1]);

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int size = builder.length();

        for (int i = 0; i < size; i++) {
            int red = (int) (redStart + ((redEnd - redStart) * i / size + 0.5));
            int greed = (int) (greenStart + ((greenEnd - greenStart) * i / size + 0.5));
            int blue = (int) (blueStart + ((blueEnd - blueStart) * i / size + 0.5));
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.argb(255, red, greed, blue));
            builder.setSpan(foregroundColorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }


}
