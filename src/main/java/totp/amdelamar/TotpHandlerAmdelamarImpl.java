package totp.amdelamar;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import totp.HMACAlgorithm;
import totp.TOTPHandler;
import totp.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of TOTPHandler using https://github.com/amdelamar/jotp
 */
public class TotpHandlerAmdelamarImpl implements TOTPHandler {
    final private String account;
    final private String issuer;
    private final String secretKey;

    public TotpHandlerAmdelamarImpl(String account, String issuer) {
        super();
        this.account = account;
        this.issuer = issuer;
        this.secretKey = generateSecretKeyAsString();
    }

    @Override
    public String getTOTPCode() {
        try {
            String hexTime = OTP.timeInHex(System.currentTimeMillis(), 30);
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
    public String getBarCodeURL() {
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
        return OTP.randomBase32(HMACAlgorithm.SHA1.getByteSize());
    }
}
