package android.mvvm.mg.com.mvvm_android.utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.mvvm.mg.com.mvvm_android.R;
import android.support.v4.content.ContextCompat;

import java.util.Map;

import dmutils.com.dmutils.general.DMDimensionConverter;

public class MVVMUtils {

    public static void showInvalidData(Map<String, ObservableField<String>> mapUiFields, final Map<String, String> errorMap) {
        if (mapUiFields != null && errorMap != null) {
            for (final Map.Entry<String, String> entry : errorMap.entrySet()) {
                final ObservableField<String> field = mapUiFields.get(entry.getKey());
                if (field != null) {
                    field.set(entry.getValue());
                }
            }
        }
    }

    public static LayerDrawable changeDrawableColor(final Context context, final String startColor, final String endColor) {

        final int corner = (int) (context.getResources().getDimension(R.dimen.corner_radius_1) / context.getResources().getDisplayMetrics().density);

        final GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor(startColor), Color.parseColor(endColor)});
        gd.setCornerRadius(DMDimensionConverter.dpToPixel(corner, context));

        final GradientDrawable bd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.black_30), ContextCompat.getColor(context, R.color.black_30)});
        bd.setCornerRadius(DMDimensionConverter.dpToPixel(corner, context));

        final Drawable[] layers = {bd, gd};
        final LayerDrawable layerDrawable = new LayerDrawable(layers);


        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, (int) DMDimensionConverter.dpToPixel(0, context), 0, (int) DMDimensionConverter.dpToPixel(1, context), (int) DMDimensionConverter.dpToPixel(2, context));

        return layerDrawable;
    }
}
