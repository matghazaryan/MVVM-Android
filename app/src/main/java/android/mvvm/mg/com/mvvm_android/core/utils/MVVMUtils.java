package android.mvvm.mg.com.mvvm_android.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.constants.IUrls;
import android.support.v4.content.ContextCompat;

import dmutils.com.dmutils.general.DMUtilDimensionConverter;

public final class MVVMUtils {

    public static LayerDrawable changeDrawableColor(final Context context, final String startColor, final String endColor) {

        final int corner = (int) (context.getResources().getDimension(R.dimen.corner_radius_1) / context.getResources().getDisplayMetrics().density);

        final GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor(startColor), Color.parseColor(endColor)});
        gd.setCornerRadius(DMUtilDimensionConverter.dpToPixel(corner, context));

        final GradientDrawable bd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.black_30), ContextCompat.getColor(context, R.color.black_30)});
        bd.setCornerRadius(DMUtilDimensionConverter.dpToPixel(corner, context));

        final Drawable[] layers = {bd, gd};
        final LayerDrawable layerDrawable = new LayerDrawable(layers);


        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, (int) DMUtilDimensionConverter.dpToPixel(0, context), 0, (int) DMUtilDimensionConverter.dpToPixel(1, context), (int) DMUtilDimensionConverter.dpToPixel(2, context));

        return layerDrawable;
    }

    public static String getTransactionUrl(final int page) {
        final String url;
        switch (page) {
            case 0:
                url = IUrls.Method.TRANSACTION_1;
                break;
            case 1:
                url = IUrls.Method.TRANSACTION_2;
                break;
            case 2:
                url = IUrls.Method.TRANSACTION_3;
                break;
            default:
                url = IUrls.Method.TRANSACTION;
        }

        return url;
    }

    public static String getLanguageName(final Context context, final String code) {
        switch (code) {
            case IMVVMConstants.Language.HY:
                return context.getString(R.string.language_armenian);
            case IMVVMConstants.Language.EN:
                return context.getString(R.string.language_english);
            case IMVVMConstants.Language.RU:
                return context.getString(R.string.language_russian);
            default:
                return null;
        }
    }
}
