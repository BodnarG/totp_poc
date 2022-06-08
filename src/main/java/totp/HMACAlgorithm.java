package totp;

public enum HMACAlgorithm {

    SHA1("HmacSHA1", 160),
    SHA256("HmacSHA256", 256),
    SHA512("HmacSHA512", 512);

        private final String name;
        private final int bits;

        HMACAlgorithm(String name, int bits) {
            this.name = name;
            this.bits = bits;
        }

        public String getHMACName() {
            return name;
        }

    public int getBitSize() {
        return bits;
    }
    public int getByteSize() {
        return bits/8; // 1 byte 8 bits
    }
}
