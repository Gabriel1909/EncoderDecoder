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
    public static final int UM_BYTE = 49;

    public byte[] encode(MultipartFile arquivo, String codificador) throws IOException {

        StringBuilder codificacao = new StringBuilder();
        Encode encode = encodeFactory(codificador, codificacao);

        try (InputStream input = arquivo.getInputStream()) {
            return getBytes(encode.encode(input, codificacao));
        }
    }

    private Encode encodeFactory(String codificador, StringBuilder codificacao) {

        EncodeEnum encodeEnum = EncodeEnum.valueOf(codificador.substring(0, 1));
        Encode encode = encodeEnum.getEncode();

        codificacao.append(encodeEnum.ordinal());

        if (encode instanceof Golomb golomb){
            char divisor = codificador.charAt(1);
            codificacao.append(divisor);
            golomb.init(divisor);
        } else {
            codificacao.append(0);
        }

        return encode;
    }

    public byte[] decode(MultipartFile arquivo) throws IOException {


        try (InputStream input = arquivo.getInputStream()) {

            Encode encode = decodeFactory(input);

            return getBytes(encode.decode(input));
        }
    }

    private Encode decodeFactory(InputStream input) throws IOException {
        int codigoEncode = Character.getNumericValue(input.read());
        char divisor = (char) input.read();

        Encode encode = EncodeEnum.values()[codigoEncode].getEncode();

        if (encode instanceof Golomb golomb){
            golomb.init(divisor);
        }
        return encode;
    }

    private byte[] getBytes(StringBuilder decode) {
        return decode.toString().getBytes(StandardCharsets.ISO_8859_1);
    }
}
