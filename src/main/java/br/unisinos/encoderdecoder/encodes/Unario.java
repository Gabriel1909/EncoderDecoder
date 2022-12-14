package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;

public class Unario implements Encode {

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();
            codificacao.append(UM.repeat(ascii));
            codificacao.append(ZERO);
        }

        return codificacao;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {
        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {

            int ascii = arquivo.read();
            int contador = 0;

            while (ascii != ZERO_ASCII) {
                contador++;
                ascii = arquivo.read();
            }

            retorno.appendCodePoint(contador);
        }

        return retorno;
    }
}
