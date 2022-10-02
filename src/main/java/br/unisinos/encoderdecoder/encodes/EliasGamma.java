package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;

public class EliasGamma implements Encode {

    public static final int OFFSET = 1;

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        while (arquivo.available() > 0) {
            int ascii = arquivo.read() + OFFSET;

            int log = (int) Math.floor(Math.log10(ascii) / Math.log10(2));

            int resto = (int) (ascii % (Math.pow(2, log)));

            codificacao.append(ZERO.repeat(log));

            codificacao.append(UM);

            if (log > 0 || resto > 0) {

                StringBuilder restoAdicionado = criarBinario(resto, log);

                codificacao.append(restoAdicionado);
            }
        }

        return codificacao;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {
        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();

            int log = 0;

            while (ascii == ZERO_ASCII) {
                log++;
                ascii = arquivo.read();
            }

            StringBuilder binario = new StringBuilder();

            for (int i = 0; i < log; i++) {
                binario.appendCodePoint(arquivo.read());
            }

            int resto = 0;

            if (log > 0) {
                resto = Integer.parseInt(binario.toString(), 2);
            }

            int total = (int) (Math.pow(2, log) + resto);

            retorno.appendCodePoint(total - OFFSET);
        }

        return retorno;
    }
}
