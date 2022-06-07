package totp;

import com.google.zxing.WriterException;
import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.security.SecureRandom;

public interface TOTPHandler {

    default String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    default byte[] generateSecret() {throw new UnsupportedOperationException();}

    default String getTOTPCode(String secretKey){throw new UnsupportedOperationException();}

    default String getTOTPCode(byte[] secret){throw new UnsupportedOperationException();}

    boolean verifyTOTP(String secretKey, String submittedOTP);

    String getBarCodeURL(String secretKey, String account, String issuer);

    void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) throws WriterException, IOException;
}
