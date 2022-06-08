package totp.taimos;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import totp.HMACAlgorithm;
import totp.TOTPHandler;
import totp.Utils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * Implementation of TOTPHandler using https://mvnrepository.com/artifact/de.taimos/totp/1.0
 */
public class TOTPHandlerTaimosImpl implements TOTPHandler {
    private String secretKey;

    public TOTPHandlerTaimosImpl() {
        super();
        initSecret();
    }

    private void initSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[HMACAlgorithm.SHA1.getByteSize()];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        secretKey = base32.encodeToString(bytes);
        System.out.println("\tSecret: \t" + secretKey);
    }

    @Override
    public String getTOTPCode() {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
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
}
