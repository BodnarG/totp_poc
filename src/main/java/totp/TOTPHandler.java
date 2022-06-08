package totp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base32;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SecureRandom;

public interface TOTPHandler {

    default String generateSecretKeyAsString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    String getTOTPCode();

    boolean verifyTOTP(String submittedOTP);

    String getBarCodeURL(String account, String issuer) throws URISyntaxException;

    default void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) throws WriterException, IOException {
        // com.google.zxing:core > MultiFormatWriter
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    default void saveQRCodeToFile(String account, String issuer, String filePath, int height, int width) throws WriterException, IOException, URISyntaxException {
        String url = getBarCodeURL(account, issuer);
        saveQRCodeToFile(url, filePath, height, width);
    }
}
