package biometric.dm.com.dmbiometric.prepare;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import biometric.dm.com.dmbiometric.R;


public final class BIOBiometricDialogV23 extends BottomSheetDialog implements View.OnClickListener {

    private DMBiometricConfigs configs;
    private BIOBaseBiometric biometric;

    private TextView tvStatus;
    private ImageView ivBiometric;

    BIOBiometricDialogV23(final @NonNull DMBiometricConfigs configs, final BIOBaseBiometric biometric) {
        super(configs.getContext(), configs.getThemeDialogV23());
        this.configs = configs;
        this.biometric = biometric;

        if (configs.getDialogViewV23() == null) {
            setDialogView();
        } else {
            setContentView(configs.getDialogViewV23());
        }
    }

    private void setDialogView() {
        @SuppressLint("InflateParams") final View bottomSheetView = getLayoutInflater().inflate(R.layout.view_bottom_sheet_v23, null);
        setContentView(bottomSheetView);

        Objects.requireNonNull((View) findViewById(R.id.btn_cancel)).setOnClickListener(this);
        ((TextView) Objects.requireNonNull((View) findViewById(R.id.tv_title))).setText(configs.getTitle());
        if (configs.getSubtitle() != null) {
            final TextView textView = findViewById(R.id.tv_subtitle);
            if (textView != null) {
                textView.setText(configs.getSubtitle());
                textView.setVisibility(View.VISIBLE);
            }
        }
        ((TextView) Objects.requireNonNull((View) findViewById(R.id.tv_description))).setText(configs.getDescription());
        ((TextView) Objects.requireNonNull((View) findViewById(R.id.btn_cancel))).setText(configs.getNegativeButtonText());
        tvStatus = findViewById(R.id.tv_status);
        ivBiometric = findViewById(R.id.iv_biometric);
    }

    void updateStatus(final String status, final int color) {
        if (tvStatus != null) {
            if (status != null) {
                new Handler().postDelayed(() -> ivBiometric.animate()
                        .rotationBy(360)
                        .setDuration(200)
                        .setInterpolator(new LinearInterpolator())
                        .withEndAction(() -> {
                            tvStatus.setTextColor(ContextCompat.getColor(configs.getContext(), R.color.status_normal_color));
                            ivBiometric.setImageResource(R.drawable.ic_biometric);
                            tvStatus.setText(R.string.biometric_touch_the_sensor);
                        })
                        .start(), 2500);

                ivBiometric.animate()
                        .rotationBy(360)
                        .setDuration(200)
                        .setInterpolator(new LinearInterpolator())
                        .withEndAction(() -> {
                            ivBiometric.setImageResource(R.drawable.ic_error_outline);
                            tvStatus.setText(status);
                            tvStatus.setTextColor(ContextCompat.getColor(configs.getContext(), color));
                        })
                        .start();

            }
        } else {
            configs.getUpdateStatusV23Listener().onUpdateStatus(status);
        }
    }

    @Override
    public void onClick(final View view) {
        dismiss();
        biometric.onAuthenticationCancelled();
    }
}
