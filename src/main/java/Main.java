import com.google.zxing.WriterException;
import totp.TOTPHandler;
import totp.Utils;
import totp.jchambers.TOTPHandlerJChambersImpl;
import totp.taimos.TOTPHandlerTaimosImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, WriterException, URISyntaxException, NoSuchAlgorithmException {
        final long now = System.currentTimeMillis();
        String email = "test_" + now + "_@gmail.com";
        String companyName = "Awesome_Company_" + now;

//        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();
//        TOTPHandler totpHandler = new TOTPHandlerBastiaanJansenImpl(); // QR generates invalid code
        TOTPHandler totpHandler = new TOTPHandlerJChambersImpl();

        String barCodeUrl = totpHandler.getBarCodeURL(email, companyName);
        System.out.println("\tbarCodeUrl: \t" + barCodeUrl);
        System.out.println("\tCurrent code: " + totpHandler.getTOTPCode());

        totpHandler.saveQRCodeToFile(barCodeUrl, "QRCode_" + now + ".png", 400, 400);

//        testCode(totpHandler);
        testCodeWithDebugPrintForNewCode(totpHandler);
    }

    private static void testCode(TOTPHandler totpHandler) {
        Utils.verifyToken(totpHandler);
    }

    private static void testCodeWithDebugPrintForNewCode(TOTPHandler totpHandler) {
        try {
            List<Callable<Object>> processesToRun = new ArrayList<>();
            Callable<Object> c1 = () -> {
                Utils.verifyToken(totpHandler);
                return null;
            };
            Callable<Object> c2 = () -> {
                Utils.printCodes(totpHandler);
                return null;
            };
            processesToRun.add(c1);
            processesToRun.add(c2);
            ExecutorService exec = Executors.newFixedThreadPool(2);
            exec.invokeAll(processesToRun);

        } catch (Exception ignored) {

        }
    }
}
