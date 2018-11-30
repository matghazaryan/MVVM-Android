package biometric.dm.com.dmbiometric.utils;

import biometric.dm.com.dmbiometric.constants.IBIOConstants;

public final class DMPrepareUtils {

    public static String beforeEncryption(final String json) {
        return IBIOConstants.DefaultValue.PART_1 +
                json +
                IBIOConstants.DefaultValue.PART_2;
    }

    public static String afterDecryption(final String decryptedText) {
        return decryptedText.replace(IBIOConstants.DefaultValue.PART_1, "").replace(IBIOConstants.DefaultValue.PART_2, "");
    }
}
