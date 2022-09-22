package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.EncoderService.*;

public class Unario implements Encode {

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();
            retorno.append(UM.repeat(ascii));
            retorno.append(ZERO);
        }

        return retorno;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {
        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {

            int ascii = arquivo.read();
            int contador = 0;

            while (ascii != ZERO_BYTE){
                contador++;
                ascii = arquivo.read();
            }

            retorno.appendCodePoint(contador);
        }

        return retorno;
    }
}
