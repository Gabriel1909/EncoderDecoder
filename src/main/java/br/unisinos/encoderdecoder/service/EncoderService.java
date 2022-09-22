package br.unisinos.encoderdecoder.service;

import br.unisinos.encoderdecoder.encodes.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EncoderService {

    public static final String ZERO = "0";
    public static final String UM = "1";
    public static final int ZERO_BYTE = 48;

    public byte[] encode(MultipartFile arquivo, char codificador) throws IOException {

        Encode encode = encodeFactory(codificador);
        StringBuilder codificacao = new StringBuilder();

        try (InputStream input = arquivo.getInputStream()) {
            return getBytes(encode.encode(input, codificacao));
        }
    }

    private Encode encodeFactory(char codificador) {

        return switch (codificador) {
            case 'G', 'g' -> new Golomb();
            case 'E', 'e' -> new EliasGamma();
            case 'F', 'f' -> new Fibonacci();
            case 'U', 'u' -> new Unario();
            case 'D', 'd' -> new Delta();
            default -> throw new RuntimeException("Codificador não reconhecido");
        };
    }

    public byte[] decode(MultipartFile arquivo) throws IOException {
        Encode encode = new Unario();

        try (InputStream input = arquivo.getInputStream()) {
            return getBytes(encode.decode(input));
        }
    }

    private byte[] getBytes(StringBuilder decode) {
        return decode.toString().getBytes(StandardCharsets.UTF_8);
    }
}
