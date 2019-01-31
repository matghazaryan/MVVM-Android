package android.mvvm.mg.com.mvvm_android.core.base;

public interface DMBaseIApplicationMethods extends DMBaseIConstants {

    default int[] getSwipeRefreshColors() {
        return new int[]{};
    }

    default SwipeType isSwipeDefaultWorkLikeLoader() {
        return SwipeType.SWIPE_FOR_REFRESH;
    }
}
