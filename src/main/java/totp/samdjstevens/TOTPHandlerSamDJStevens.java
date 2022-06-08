package totp.samdjstevens;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import totp.TOTPHandler;
import totp.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

public class TOTPHandlerSamDJStevens implements TOTPHandler {

    private final String secretKey;
    private String email;
    private String companyName;

    public TOTPHandlerSamDJStevens(String email, String companyName) {
        this.email = email;
        this.companyName = companyName;
        this.secretKey = generateSecretKeyAsString();
    }

    @Override
    public String getTOTPCode() {
        return null;
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secretKey, submittedOTP);
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

    @Override
    public void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) {
//        QrData data = new QrData.Builder()
//                .label(email)
//                .secret(secretKey)
//                .issuer(companyName)
//                .algorithm(HashingAlgorithm.SHA1) // More on this below
//                .digits(6)
//                .period(30)
//                .build();

        //saveQRCodeToFile(data.toString(), filePath, height, width);

//        QrGenerator generator = new ZxingPngQrGenerator();
//        byte[] imageData;
//        try {
//            imageData = generator.generate(data);
//        } catch (QrGenerationException e) {
//            throw new RuntimeException(e);
//        }
//        String mimeType = generator.getImageMimeType();
//        String dataUri = getDataUriForImage(imageData, mimeType);

        TOTPHandler.super.saveQRCodeToFile(barCodeData, filePath, height, width);
    }

    private String generateSecretKeyAsString() {
        DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }
}
