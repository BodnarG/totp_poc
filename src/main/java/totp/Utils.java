package totp;

import totp.jchambers.TOTPHandlerJChambersImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

//    public static void infinityGeneratingCodes(String secretKey) {
//        TOTPHandler totpHandler = new TOTPHandlerTaimosImpl();
//        String lastCode = null;
//        while (true) {
//            String code = totpHandler.getTOTPCode(secretKey);
//            if (!code.equals(lastCode)) {
//                System.out.println(code);
//            }
//            lastCode = code;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//        }
//    }

    public static String urlEncodeAndReplacePlus(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8).replace("+", "%20");
    }


    public static void verifyToken(TOTPHandler totpHandler, String secretKey) {
        System.out.println("Please write in the OTP code:");
        Scanner scanner = new Scanner(System.in);

        String code = scanner.nextLine();
        boolean success = false;
        if (totpHandler instanceof TOTPHandlerJChambersImpl handler
                && totpHandler.verifyTOTP(handler.getKey(), code)) {
            success = true;
        } else {
            if (totpHandler.verifyTOTP(secretKey, code)) {
                success = true;
            }
        }

        String classname = totpHandler.getClass().getCanonicalName();
        if(success) {
            System.out.println(classname + " > Logged in successfully");
        } else {
            System.out.println(classname + " > Invalid 2FA Code");
            System.out.println(classname + " > Try again!");
            verifyToken(totpHandler, secretKey);
        }

    }

    public static String byteArrayToString(byte[] arr) {
        StringBuilder builder = new StringBuilder();
        for(byte b : arr) {
            builder.append(b);
        }
        return builder.toString();
    }


}