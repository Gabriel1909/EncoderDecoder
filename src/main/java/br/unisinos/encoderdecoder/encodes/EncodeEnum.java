package br.unisinos.encoderdecoder.encodes;

public enum EncodeEnum {

    G(new Golomb()),
    E(new EliasGamma()),
    F(new Fibonacci()),
    U(new Unario()),
    D(new Delta());

    private final Encode encode;

    public Encode getEncode() {
        return encode;
    }

    EncodeEnum(Encode encode) {
        this.encode = encode;
    }
}