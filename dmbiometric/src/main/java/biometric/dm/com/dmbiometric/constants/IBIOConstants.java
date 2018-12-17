package biometric.dm.com.dmbiometric.constants;

public interface IBIOConstants {

    interface DefaultValue {
        String PART_1 = "Hh$V-*?T,>M&7GpwOY](q!3k B:&jz4tZCCnz57'C(qE#8:.I!Y9Oju\"%^'VVu:N91\"YwdW2so,Q&&p]JO!_W)'r](T6(?6cyW7[v[1xq6Q%s4gI%Fg^;a__>";
        String PART_2 = "&5?t;&W-847-\u007F|>1\"tE (ruOJ yq81NZY>VnoBlJynRNr)G&wht4EFe|  u,10BE'c\u007FJ^YHD=yP/yoW%wcxeo6~@,[6 PP\u007F448~|,W.nIiucY\u007FRBiwSFj7#NQe_!yfI[#QVy=oc]A2e~to]PO9)+:;XZ42VcoEENyb-3u6J%(hcUor[$m]`h#YazXV&zxaV`9^e x_gYb^tZ/&0qh7Z2iBC`BDY;wOa&H[bsj^~NT*<#vc l$;=;pMAaU2.9P#hSiKJ PH";
        String KEY_ALIAS = "30f72h707dc0cc4fdb3b41c3dfade4ed6e684764";

        String ANDROID_KEY_STORE = "AndroidKeyStore";
    }

    interface PrefKey {
        String KEY_IV = "VnfsdoBlJyjfdnRNr";
        String DATA = "jz4tdZCC45nz";
    }

    enum EncryptionMode {
        ENCRYPT,
        DECRYPT
    }

    enum FailedType {
        SDK_VERSION_NOT_SUPPORTED,
        BIOMETRIC_AUTHENTICATION_NOT_SUPPORTED,
        BIOMETRIC_AUTHENTICATION_NOT_AVAILABLE,
        BIOMETRIC_AUTHENTICATION_PERMISSION_NOT_GRANTED,
        AUTHENTICATION_HELP,
        AUTHENTICATION_CANCELLED,
        AUTHENTICATION_FAILED,
        AUTHENTICATION_FAILED_EXCEPTION,
        BIOMETRIC_SENSOR_DISABLED,
        INCORRECT_CONFIGS,
        OTHER
    }
}
