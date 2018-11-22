package com.eraz.camera.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.eraz.camera.pagination.PaginationCircle;

public class Utils {

    public static void drawPaginationCircles(final Context context, final LinearLayout layout, final int circleCount, final int position) {
        layout.removeAllViews();
        final PaginationCircle paginationCircle = new PaginationCircle(context, circleCount, position);
        final View view = paginationCircle.drawCircles();
        layout.addView(view);
    }
}
