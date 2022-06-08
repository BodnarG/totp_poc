import com.google.zxing.WriterException;
import totp.TOTPHandler;
import totp.Utils;
import totp.jchambers.TOTPHandlerJChambersImpl;
import totp.taimos.TOTPHandlerTaimosImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, WriterException, NoSuchAlgorithmException, URISyntaxException {
        String email = "test@gmail.com";
        String companyName = "Awesome Company";

//        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();

        //taimos(email, companyName);
        jChambers(email, companyName);

    }

    private static void taimos(String email, String companyName) throws IOException, WriterException, URISyntaxException {
        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();


        String barCodeUrl = totpHandler.getBarCodeURL(email, companyName);
        //totpHandler.saveQRCodeToFile(barCodeUrl, "QRCode_" + System.currentTimeMillis() + ".png", 400, 400);

        System.out.println("barCodeUrl: \t"+barCodeUrl);


        //Utils.verifyToken(totpHandler, secretKey);
    }

    private static void jChambers(String email, String companyName) throws NoSuchAlgorithmException, URISyntaxException {
        TOTPHandler totpHandler = new TOTPHandlerJChambersImpl();

//        Key key = handler.generateSecretSecurityKey();
        String barCodeUrl = totpHandler.getBarCodeURL(email, companyName);
        //totpHandler.saveQRCodeToFile(barCodeUrl, "JChambers_QRCode_" + System.currentTimeMillis() + ".png", 400, 400);
        System.out.println(/*"code=" + totpHandler.getTOTPCode(key) + */" | url=" + barCodeUrl);

        Utils.verifyToken(totpHandler);
    }

}
