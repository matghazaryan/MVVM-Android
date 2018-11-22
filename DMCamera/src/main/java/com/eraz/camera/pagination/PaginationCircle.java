package com.eraz.camera.pagination;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eraz.camera.R;

/**
 * Created by matevosghazaryan on 8/15/16.
 */

public class PaginationCircle {

    private final int selectPosition;
    private int count;
    private Context context;

    public PaginationCircle(final Context context, final int count, final int selectPosition) {
        this.selectPosition = selectPosition;
        this.count = count;
        this.context = context;
    }

    public View drawCircles() {
        final LinearLayout linearLayout = new LinearLayout(context);
        for (int i = 0; i < this.count; i++) {
            final ImageView imageView = new ImageView(context);
            final LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rp.setMargins(20, 0, 0, 0);
            imageView.setLayoutParams(rp);
            if (i == this.selectPosition) {
                setRoundedBackground(imageView, ContextCompat.getColor(context, R.color.pagination_selected_color), ContextCompat.getColor(context, R.color.pagination_selected_color), context.getResources().getInteger(R.integer.circle_radius));
            } else {
                setRoundedBackground(imageView, ContextCompat.getColor(context, R.color.pagination_color), ContextCompat.getColor(context, R.color.pagination_color), context.getResources().getInteger(R.integer.circle_radius));
            }

            linearLayout.addView(imageView);
        }

        return linearLayout;
    }

    @SuppressWarnings("deprecation")
    public void setRoundedBackground(final View view, final int fillColor, final int strokeColor, final int corner) {
        final RoundRectShape rs = new RoundRectShape(new float[]{corner, corner, corner, corner, corner, corner, corner, corner}, null, null);
        final ShapeDrawable sd = new MGCustomShapeDrawable(rs, fillColor, strokeColor, 0);
        sd.setPadding(corner, corner, corner, corner);
        view.setBackgroundDrawable(sd);
    }
}
