package android.mvvm.mg.com.mvvm_android.room.models.card;

import android.arch.persistence.room.Entity;
import android.mvvm.mg.com.mvvm_android.room.models.MVVMRoomBase;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Card extends MVVMRoomBase {

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("exp_date_year")
    private String expDateYear;

    @JsonProperty("exp_date_month")
    private String expDateMonth;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("card_color1")
    private String cardColor1;

    @JsonProperty("card_color2")
    private String cardColor2;


    public String getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpDateYear() {
        return expDateYear;
    }

    public String getExpDateMonth() {
        return expDateMonth;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getCardColor1() {
        return cardColor1;
    }

    public String getCardColor2() {
        return cardColor2;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpDateYear(String expDateYear) {
        this.expDateYear = expDateYear;
    }

    public void setExpDateMonth(String expDateMonth) {
        this.expDateMonth = expDateMonth;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setCardColor1(String cardColor1) {
        this.cardColor1 = cardColor1;
    }

    public void setCardColor2(String cardColor2) {
        this.cardColor2 = cardColor2;
    }
}
