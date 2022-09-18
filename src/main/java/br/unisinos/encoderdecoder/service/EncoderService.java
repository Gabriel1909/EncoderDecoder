package br.unisinos.encoderdecoder.service;

import br.unisinos.encoderdecoder.encodes.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EncoderService {

    public byte[] encode(MultipartFile arquivo, char codificador) throws IOException {

        Encode encode = encodeFactory(codificador);
        return encode.encode(arquivo);
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
        Encode encode = new EliasGamma();
        return encode.decode(arquivo);
    }
}
