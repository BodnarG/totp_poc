package totp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public interface TOTPHandler {

    String getTOTPCode();

    boolean verifyTOTP(String submittedOTP);

    String getBarCodeURL(String account, String issuer);

    default void saveQRCodeToFile(String barCodeData, String filePath, int height, int width){
        // com.google.zxing:core > MultiFormatWriter
        BitMatrix matrix = null;
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    default void saveQRCodeToFile(String account, String issuer, String filePath, int height, int width) {
        String url = getBarCodeURL(account, issuer);
        saveQRCodeToFile(url, filePath, height, width);
    }
}
