package br.unisinos.encoderdecoder.service;

import br.unisinos.encoderdecoder.encodes.Encode;
import br.unisinos.encoderdecoder.encodes.EncodeEnum;
import br.unisinos.encoderdecoder.encodes.Golomb;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;

@Service
public class EncoderService {

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

        StringBuilder header = criarBinario(encodeEnum.ordinal());

        if (encode instanceof Golomb golomb) {
            int codigoDivisor = Integer.parseInt(codificador.substring(1));
            StringBuilder divisor = criarBinario(codigoDivisor);
            header.append(divisor);
            golomb.init(codigoDivisor);
        } else {
            header.append(ZERO_BYTE);
        }

        codificacao.append(header);

        return encode;
    }

    public byte[] decode(MultipartFile arquivo) throws IOException {

        try (InputStream input = arquivo.getInputStream()) {

            Encode encode = decodeFactory(input);

            return getBytes(encode.decode(input));
        }
    }

    private Encode decodeFactory(InputStream input) throws IOException {

        int codigoEncode = lerByte(input);
        int divisor = lerByte(input);

        Encode encode = EncodeEnum.values()[codigoEncode].getEncode();

        if (encode instanceof Golomb golomb) {
            golomb.init(divisor);
        }
        return encode;
    }
}
