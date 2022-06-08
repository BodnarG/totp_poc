package totp.bastiaanjansen;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;
import totp.TOTPHandler;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;

public class TOTPHandlerBastiaanJansenImpl implements TOTPHandler {
    private byte[] secret;
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
                .withAlgorithm(HMACAlgorithm.SHA256) // SHA1 and SHA512 are also supported
                .withPeriod(Duration.ofSeconds(30));

        totp = builder.build();
    }

    private void initSecret() {
        secret = SecretGenerator.generate();
        System.out.println("\tSecret: \t" + Arrays.toString(secret));
    }
}