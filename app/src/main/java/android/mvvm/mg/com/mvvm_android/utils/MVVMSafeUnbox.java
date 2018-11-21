package android.mvvm.mg.com.mvvm_android.utils;

public class MVVMSafeUnbox {

    public static int safeUnbox(final java.lang.Integer boxed) {
        return boxed == null ? 0 : boxed;
    }

    public static long safeUnbox(final java.lang.Long boxed) {
        return boxed == null ? 0L : boxed;
    }

    public static short safeUnbox(final java.lang.Short boxed) {
        return boxed == null ? 0 : (short) boxed;
    }

    public static byte safeUnbox(final java.lang.Byte boxed) {
        return boxed == null ? 0 : (byte) boxed;
    }

    public static char safeUnbox(final java.lang.Character boxed) {
        return boxed == null ? '\u0000' : boxed;
    }

    public static double safeUnbox(final java.lang.Double boxed) {
        return boxed == null ? 0.0 : boxed;
    }

    public static float safeUnbox(final java.lang.Float boxed) {
        return boxed == null ? 0f : boxed;
    }

    public static boolean safeUnbox(final java.lang.Boolean boxed) {
        return boxed != null && boxed;
    }
}
