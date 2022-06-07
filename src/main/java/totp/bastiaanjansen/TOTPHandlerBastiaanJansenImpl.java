package totp.bastiaanjansen;


import com.google.zxing.WriterException;
import totp.TOTPHandler;

import java.io.IOException;

public class TOTPHandlerBastiaanJansenImpl implements TOTPHandler {

    @Override
    public String generateSecretKey() {
        return TOTPHandler.super.generateSecretKey();
    }

    @Override
    public String getTOTPCode(String secretKey) {
        return null;
    }

    @Override
    public boolean verifyTOTP(String secretKey, String submittedOTP) {
        return false;
    }

    @Override
    public String getBarCodeURL(String secretKey, String account, String issuer) {
        return null;
    }

    @Override
    public void saveQRCodeToFile(String barCodeData, String filePath, int height, int width) throws WriterException, IOException {

    }
}
