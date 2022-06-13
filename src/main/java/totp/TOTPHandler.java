package totp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static totp.Utils.getDataUriForImage;

public interface TOTPHandler {

    String getTOTPCode();

    boolean verifyTOTP(String submittedOTP);

    String getBarCodeURL();

    default void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            writeToStream(outputStream, barCodeData, height, width);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    default String getQRCodeDataAsURL(String data, int height, int width) {
        byte[] imageData = getQRCodeDataAsByteArray(data, height, width);

        String mimeType = "image/png";
        String uri = getDataUriForImage(imageData, mimeType);

        System.out.println("\t URI: \t" + uri);

        return uri;
    }

    private byte[] getQRCodeDataAsByteArray(String data, int height, int width) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeToStream(outputStream, data, height, width);

            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToStream(OutputStream outputStream, String data, int height, int width) throws WriterException, IOException {
        // com.google.zxing:core > MultiFormatWriter > QRCodeWriter
        Writer writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, height, width);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
    }
}
