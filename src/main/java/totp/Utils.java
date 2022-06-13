package totp;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    private Utils(){
        // private constructor
        // Class not meant to be instantiated
    }

    public static void printCodes(TOTPHandler totpHandler) {
        String lastCode = null;
        while (true) {
            String code = totpHandler.getTOTPCode();
            if (!code.equals(lastCode)) {
                System.out.println("\t\tnew code: " + code);
            }
            lastCode = code;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    public static String urlEncodeAndReplacePlus(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static void verifyToken(TOTPHandler totpHandler) {
        System.out.println("Please write in the OTP code:");
        Scanner scanner = new Scanner(System.in);

        String code = scanner.nextLine();
        if (totpHandler.verifyTOTP(code)) {
            System.out.println("Valid 2FA Code");
        } else {
            System.out.println("Invalid 2FA Code");
            System.out.println("Try again!");
            verifyToken(totpHandler);
        }
    }

    /**
     * Given the raw data of an image and the mime type, returns
     * a data URI string representing the image for embedding in
     * HTML/CSS.
     *
     * @param data The raw bytes of the image.
     * @param mimeType The mime type of the image.
     * @return The data URI string representing the image.
     */
    public static String getDataUriForImage(byte[] data, String mimeType) {
        String encodedData = new String(new Base64().encode(data));

        return String.format("data:%s;base64,%s", mimeType, encodedData);
    }

//    public static String byteArrayToString(byte[] arr) {
//        StringBuilder builder = new StringBuilder();
//        for(byte b : arr) {
//            builder.append(b);
//        }
//        return builder.toString();
//    }
}