package totp.jchambers;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import totp.TOTPHandler;
import totp.Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class TOTPHandlerJChambersImpl implements TOTPHandler {
    final private String account;
    final private String issuer;
    private final TimeBasedOneTimePasswordGenerator totp;
    private Key key;

    public TOTPHandlerJChambersImpl(String account, String issuer) throws NoSuchAlgorithmException {
        this.account = account;
        this.issuer = issuer;
        totp = new TimeBasedOneTimePasswordGenerator(); // default: Duration.ofSeconds(30), length: 6 chars, alg: SHA1
        // TimeBasedOneTimePasswordGenerator(timeStep)
        // TimeBasedOneTimePasswordGenerator(timeStep, passwordLength)
        // TimeBasedOneTimePasswordGenerator(timeStep, passwordLength, algorithm)
                //  DEFAULT_TIME_STEP = Duration.ofSeconds(30)
                //  DEFAULT_PASSWORD_LENGTH = 6
                //    TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA1
                //    TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA256
                //    TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512

        generateSecretSecurityKey();
    }

    @Override
    public String getTOTPCode() {
        final Instant now = Instant.now();
        try {
            return totp.generateOneTimePasswordString(key, now);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        final Instant now = Instant.now();
        try {
            return submittedOTP.equals(totp.generateOneTimePasswordString(key, now));
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBarCodeURL() {

        // TODO make it somehow work

        /*byte[] decodedBytes = Base64.getDecoder().decode(Utils.byteArrayToString(secretKey.getEncoded()).getBytes(StandardCharsets.UTF_8));
        String decodedString = new String(decodedBytes);*/

        String decodedString = new String(Base64.getDecoder().decode(key.getEncoded()));

        try {
            return "otpauth://totp/"
                    + Utils.urlEncodeAndReplacePlus(issuer + ":" + account)
                    + "?secret=" + Utils.urlEncodeAndReplacePlus(decodedString)
                    + "&issuer=" + Utils.urlEncodeAndReplacePlus(issuer);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            writeToStream(out, barCodeData, height, width);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }

    private void generateSecretSecurityKey() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

            // Key length should match the length of the HMAC output (160 bits for SHA-1, 256 bits for SHA-256, and 512 bits for SHA-512).
            // Note that while Mac#getMacLength() returns a length in _bytes,_ KeyGenerator#init(int) takes a key length in _bits._
            final int macLengthInBytes = Mac.getInstance(totp.getAlgorithm()).getMacLength();
            keyGenerator.init(macLengthInBytes * 8);

            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
