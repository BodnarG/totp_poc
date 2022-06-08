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


    public TOTPHandlerBastiaanJansenImpl() {
        super();
        initSecret();
    }

    private void initSecret() {
        secret = SecretGenerator.generate();
        System.out.println("\tSecret: \t" + Arrays.toString(secret));
    }

    @Override
    public String getTOTPCode() {
        return initTOTP().now();
    }

    @Override
    public boolean verifyTOTP(String submittedOTP) {
        return initTOTP().verify(submittedOTP);
    }

    @Override
    public String getBarCodeURL(String account, String issuer) throws URISyntaxException {
        return initTOTP().getURI(issuer, account).toString();
    }

    private TOTP initTOTP() {
        TOTP.Builder builder = new TOTP.Builder(secret);
        builder
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA256) // SHA1 and SHA512 are also supported
                .withPeriod(Duration.ofSeconds(30));

        return builder.build();
    }
}