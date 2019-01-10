package android.mvvm.mg.com.mvvm_android.core.base;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseArray;

/**
 * This class which has few functions for to put and to get data in the MutableLiveData
 */
final class DMBaseSharedViewModel extends ViewModel implements DMBaseISharedViewModel {

    private final SparseArray<MutableLiveData<Object>> sharedDataSparseArray = new SparseArray<>();
    private final SparseArray<Object> bagOfSentSharedDataSparseArray = new SparseArray<>();

    /**
     * This function for get LiveData from SparseArray by sendCode key
     *
     * @param sendCode     sendCode like key for save MutableLiveData in sparseArray
     * @param <SharedData> it is data type which put in the MutableLiveData
     * @return LiveData by sendCode key
     */
    @SuppressWarnings("unchecked")
    private <SharedData> LiveData<SharedData> getBaseSharedData(final int sendCode) {
        final LiveData<Object> data = sharedDataSparseArray.get(sendCode);
        if (data == null) {
            sharedDataSparseArray.put(sendCode, new MutableLiveData<>());
        }

        return (LiveData<SharedData>) sharedDataSparseArray.get(sendCode);
    }

    /**
     * This function for put data in the MutableLiveData and add to SparseArray  by sendCode key
     *
     * @param sendCode     sendCode like key for save MutableLiveData in sparseArray
     * @param data         it is data for put in the MutableLiveData
     * @param <SharedData> it is data type which put in the MutableLiveData
     */
    private <SharedData> void sendBaseSharedData(final int sendCode, final SharedData data) {
        MutableLiveData<Object> mutableLiveData = sharedDataSparseArray.get(sendCode);
        if (mutableLiveData == null) {
            sharedDataSparseArray.put(sendCode, new MutableLiveData<>());
        }

        mutableLiveData = sharedDataSparseArray.get(sendCode);
        if (mutableLiveData != null) {
            mutableLiveData.setValue(data);
        }
    }

    /**
     * To put data by key sendCode in the MutableLiveData in mSharedViewModel
     *
     * @param sendCode     sendCode like key for save MutableLiveData in sparseArray
     * @param data         it is data for put in the MutableLiveData
     * @param <SharedData> it is data type which put in the MutableLiveData
     */
    @Override
    public <SharedData> void sendSharedData(final int sendCode, final SharedData data) {
        bagOfSentSharedDataSparseArray.remove(sendCode);
        sendBaseSharedData(sendCode, data);
    }

    /**
     * To get any data each time when subscribing on MutableLiveData
     *
     * @param owner        LifecycleOwner can be from activity or fragment
     * @param sendCode     sendCode like key for save MutableLiveData in sparseArray
     * @param listener     listener for to get data from MutableLiveData
     * @param <SharedData> it is data type which put in the MutableLiveData
     */
    @SuppressWarnings("unchecked")
    @Override
    public <SharedData> void getSharedData(final LifecycleOwner owner, final int sendCode, final DMBaseIOnSharedDataListener<SharedData> listener) {
        getBaseSharedData(sendCode).observe(owner, data -> {

            final Object sharedData = bagOfSentSharedDataSparseArray.get(sendCode);

            if (sharedData == null || data == null || sharedData.hashCode() != data.hashCode()) {
                bagOfSentSharedDataSparseArray.put(sendCode, data);
                listener.onDataTransferred((SharedData) data);
            }
        });
    }

    /**
     * To get any data each time when subscribing on liveData when data is available or exist
     *
     * @param owner        LifecycleOwner can be from activity or fragment
     * @param sendCode     sendCode like key for save MutableLiveData in sparseArray
     * @param listener     listener for to get data from MutableLiveData
     * @param <SharedData> it is data type which put in the MutableLiveData
     */
    @SuppressWarnings("unchecked")
    @Override
    public <SharedData> void getSharedDataAlways(final LifecycleOwner owner, final int sendCode, final DMBaseIOnSharedDataListener<SharedData> listener) {
        getBaseSharedData(sendCode).observe(owner, data -> {
            bagOfSentSharedDataSparseArray.put(sendCode, data);
            listener.onDataTransferred((SharedData) data);
        });
    }
}
