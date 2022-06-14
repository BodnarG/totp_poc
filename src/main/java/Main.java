import totp.TOTPHandler;
import totp.Utils;
import totp.amdelamar.TotpHandlerAmdelamarImpl;
import totp.bastiaanjansen.TOTPHandlerBastiaanJansenImpl;
import totp.jchambers.TOTPHandlerJChambersImpl;
import totp.samdjstevens.TOTPHandlerSamDJStevens;
import totp.taimos.TOTPHandlerTaimosImpl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        final long now = System.currentTimeMillis();
        String account = "user@company.com";
        String issuer = "Awesome Company";

//        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl(account, issuer); // works, but supports only SHA1
//        TOTPHandler totpHandler = new TotpHandlerAmdelamarImpl(account, issuer);  // works, but supports only SHA1
//        TOTPHandler totpHandler = new TOTPHandlerJChambersImpl(account, issuer);

//        TOTPHandler totpHandler = new TOTPHandlerBastiaanJansenImpl(account, issuer); // with SHA1 it works, but with SHA256 QR generates invalid code
        TOTPHandler totpHandler = new TOTPHandlerSamDJStevens(account, issuer); // with SHA1 it works

        String barCodeUrl = totpHandler.getBarCodeURL();
        System.out.println("\tbarCodeUrl: \t" + barCodeUrl);
        System.out.println("\tCurrent code: \t" + totpHandler.getTOTPCode());
        totpHandler.getQRCodeDataAsURL(barCodeUrl, 400, 400);

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
