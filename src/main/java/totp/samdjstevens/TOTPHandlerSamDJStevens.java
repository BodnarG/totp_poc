package totp.samdjstevens;

import com.google.zxing.WriterException;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.CodeGenerationException;
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

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * Implementation of TOTPHandler using https://github.com/samdjstevens/java-totp
 */
public class TOTPHandlerSamDJStevens implements TOTPHandler {
    final private String account;
    final private String issuer;

    private final String secretKey;

    public TOTPHandlerSamDJStevens(String account, String issuer) {
        this.account = account;
        this.issuer = issuer;
        this.secretKey = generateSecretKeyAsString();
    }

    @Override
    public String getTOTPCode() {
        try {
            TimeProvider timeProvider = new SystemTimeProvider();
            long currentBucket = Math.floorDiv(timeProvider.getTime(), 30);
//            CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA256);
            CodeGenerator codeGenerator = new DefaultCodeGenerator();
            return codeGenerator.generate(secretKey, currentBucket);
        } catch (CodeGenerationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
//        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA256);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secretKey, submittedOTP);
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

    @Override
    public void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) {

        try {
            QrData data = new QrData.Builder()
                    .label(issuer + ":" + account)
//                    .label("ISSUER_PART:ACCOUNT_NAME@issuer_name_from_account_address.com") // issuer >> what is defined before the ":"
//                    .label("ACCOUNT_NAME@issuer_name_from_account_address.com") // issuer >> (domain) from email address
//                    .label("ACCOUNT_NAME_NOT_EMAIL") // issuer >>  the issuer from the "issuer" variable
                    .secret(secretKey)
                    .issuer(issuer)
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();

            QrGenerator generator = new ZxingPngQrGenerator();

            byte[] imageData = generator.generate(data);

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                // write png's byte[] to file
                outputStream.write(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (QrGenerationException e) {
            e.printStackTrace();
        }
    }

    private String generateSecretKeyAsString() {
        DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();  // numCharacters = 32 >> 32*5 bits = 160 bits > SHA1
        return secretGenerator.generate();
    }
}
