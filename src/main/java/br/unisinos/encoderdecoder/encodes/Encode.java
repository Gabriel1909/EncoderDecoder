package br.unisinos.encoderdecoder.encodes;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface Encode {

    byte[] encode(MultipartFile arquivo) throws IOException;
    byte[] decode(MultipartFile arquivo) throws IOException;
}
