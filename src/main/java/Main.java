import com.google.zxing.WriterException;
import totp.TOTPHandler;
import totp.Utils;
import totp.jchambers.TOTPHandlerJChambersImpl;
import totp.taimos.TOTPHandlerTaimosImpl;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, WriterException, NoSuchAlgorithmException {
        String email = "test@gmail.com";
        String companyName = "Awesome Company";

//        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();
//
//        String secretKey = totpHandler.generateSecretKey();
//        String barCodeUrl = totpHandler.getBarCodeURL(secretKey, email, companyName);
//        totpHandler.saveQRCodeToFile(barCodeUrl, "QRCode_" + System.currentTimeMillis() + ".png", 400, 400);
//
//        System.out.println("secretKey: \t"+secretKey);
//        System.out.println("barCodeUrl: \t"+barCodeUrl);
//
//
//        Utils.verifyToken(totpHandler, secretKey);

//        String lastCode = null;
//        while (true) {
//            String code = Utils.getTOTPCode(secretKey);
//            if (!code.equals(lastCode)) {
//                System.out.println(code);
//            }
//            lastCode = code;
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {};
//        }

        //taimos(email, companyName);
        jChambers(email, companyName);

    }

    private static void taimos(String email, String companyName) throws IOException, WriterException {
        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();

        String secretKey = totpHandler.generateSecretKey();
        String barCodeUrl = totpHandler.getBarCodeURL(secretKey, email, companyName);
        //totpHandler.saveQRCodeToFile(barCodeUrl, "QRCode_" + System.currentTimeMillis() + ".png", 400, 400);

        System.out.println("secretKey: \t"+secretKey);
        System.out.println("barCodeUrl: \t"+barCodeUrl);


        //Utils.verifyToken(totpHandler, secretKey);
    }

    private static void jChambers(String email, String companyName) throws NoSuchAlgorithmException, IOException, WriterException {
        TOTPHandler totpHandler = new TOTPHandlerJChambersImpl();
        TOTPHandlerJChambersImpl handler = (TOTPHandlerJChambersImpl) totpHandler;

        Key key = handler.generateSecretSecurityKey();
        String barCodeUrl = handler.getBarCodeURLFromKey(key, email, companyName);
        //totpHandler.saveQRCodeToFile(barCodeUrl, "JChambers_QRCode_" + System.currentTimeMillis() + ".png", 400, 400);
        System.out.println(/*"code=" + totpHandler.getTOTPCode(key) + */" | url=" + barCodeUrl);

        Utils.verifyToken(totpHandler, null);
    }

}
