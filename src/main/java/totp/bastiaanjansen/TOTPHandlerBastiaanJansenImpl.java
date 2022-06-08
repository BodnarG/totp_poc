package totp.bastiaanjansen;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;
import totp.TOTPHandler;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;

/**
 * Implementation of TOTPHandler using https://github.com/BastiaanJansen/otp-java
 */
public class TOTPHandlerBastiaanJansenImpl implements TOTPHandler {
    private byte[] secret; // Base32 encoded byte[]
    private TOTP totp;

    public TOTPHandlerBastiaanJansenImpl() {
        super();
        initTOTP();
    }

    @Override
    public String getTOTPCode() {
        return totp.now();
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        return totp.verify(submittedOTP);
    }

    @Override
    public String getBarCodeURL(String account, String issuer) {
        try {
            return totp.getURI(issuer, account).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTOTP() {
        initSecret();
        TOTP.Builder builder = new TOTP.Builder(secret);

        builder
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA1)
//                .withAlgorithm(HMACAlgorithm.SHA256)
                .withPeriod(Duration.ofSeconds(30));

        totp = builder.build();
    }

    private void initSecret() {
        //bits: this should be greater than or equal to the length of the HMAC algorithm type
        //        HMACAlgorithm.SHA1
        //        HMACAlgorithm.SHA256
        //        HMACAlgorithm.SHA512
//        int bits = 256;
//        secret = SecretGenerator.generate(bits);
        secret = SecretGenerator.generate(); // << will be initialized by default 160 bits
        System.out.println("\tSecret: \t" + Arrays.toString(secret));
    }
}