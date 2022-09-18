package br.unisinos.encoderdecoder.encodes;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EliasGamma implements Encode {

    public static final String ZERO = "0";
    public static final String UM = "1";
    public static final int OFFSET = 1;
    public static final int ZERO_BYTE = 48;

    @Override
    public byte[] encode(MultipartFile arquivo) throws IOException {

        StringBuilder retorno = new StringBuilder();

        try (InputStream input = arquivo.getInputStream()) {
            while (input.available() > 0) {
                int ascii = input.read() + OFFSET;

                double log = Math.floor(Math.log10(ascii) / Math.log10(2));

                int resto = (int) (ascii % (Math.pow(2, log)));

                for (int i = 0; i < log; i++) {
                    retorno.append(ZERO);
                }

                retorno.append(UM);

                if (log > 0 || resto > 0){

                    StringBuilder restoAdicionado = new StringBuilder(Integer.toBinaryString(resto));

                    while (restoAdicionado.length() < log){
                        restoAdicionado.insert(0, ZERO);
                    }

                    retorno.append(restoAdicionado);
                }
            }
        }

        return retorno.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decode(MultipartFile arquivo) throws IOException {
        StringBuilder retorno = new StringBuilder();

        try (InputStream input = arquivo.getInputStream()) {
            while (input.available() > 0) {
                int ascii = input.read();

                int log = 0;

                while (ascii == ZERO_BYTE) {
                    log++;
                    ascii = input.read();
                }

                StringBuilder binario = new StringBuilder();

                for (int i = 0; i < log; i++) {
                    binario.appendCodePoint(input.read());
                }

                int resto = 0;

                if (log > 0){
                    resto = Integer.parseInt(binario.toString(), 2);
                }

                int total = (int) (Math.pow(2, log) + resto);

                retorno.appendCodePoint(total - OFFSET);
            }
        }

        return retorno.toString().getBytes(StandardCharsets.UTF_8);
    }
}
