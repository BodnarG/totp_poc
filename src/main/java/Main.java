import com.google.zxing.WriterException;
import totp.TOTPHandler;
import totp.Utils;
import totp.taimos.TOTPHandlerTaimosImpl;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, WriterException {
        String email = "test@gmail.com";
        String companyName = "Awesome Company";

        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();

        String secretKey = totpHandler.generateSecretKey();
        String barCodeUrl = totpHandler.getBarCodeURL(secretKey, email, companyName);
        totpHandler.saveQRCodeToFile(barCodeUrl, "QRCode_" + System.currentTimeMillis() + ".png", 400, 400);

        System.out.println("secretKey: \t"+secretKey);
        System.out.println("barCodeUrl: \t"+barCodeUrl);


        Utils.verifyToken(totpHandler, secretKey);

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


    }
}
