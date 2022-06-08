package totp.amdelamar;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import totp.TOTPHandler;
import totp.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TotpHandlerAmdelamarImpl implements TOTPHandler {
    private final int length;
    private String secretKey;

    public TotpHandlerAmdelamarImpl(int length) {
        super();
        this.length = length;
        this.secretKey = generateSecretKeyAsString();
    }

    @Override
    public String getTOTPCode() {
        String hexTime = null;
        try {
            hexTime = OTP.timeInHex(System.currentTimeMillis(), 30);
            return OTP.create(secretKey, hexTime, 6, Type.TOTP);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        return submittedOTP.equals(getTOTPCode());
    }

    @Override
    public String getBarCodeURL(String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + Utils.urlEncodeAndReplacePlus(issuer + ":" + account)
                    + "?secret=" + Utils.urlEncodeAndReplacePlus(secretKey)
                    + "&issuer=" + Utils.urlEncodeAndReplacePlus(issuer);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSecretKeyAsString() {
        return OTP.randomBase32(length);
    }
}
