package br.unisinos.encoderdecoder.encodes;

import org.springframework.web.multipart.MultipartFile;

public class Fibonacci implements Encode {

    @Override
    public byte[] encode(MultipartFile arquivo) {
        return new byte[0];
    }

    @Override
    public byte[] decode(MultipartFile arquivo) {
        return new byte[0];
    }

}
