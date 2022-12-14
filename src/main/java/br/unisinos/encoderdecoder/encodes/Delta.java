package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;
import static java.lang.Math.abs;

public class Delta implements Encode {

    public static final int OFFSET = 9;

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        int ultimoAscii = arquivo.read();

        StringBuilder binarioInicial = criarBinario(ultimoAscii, OFFSET);

        codificacao.append(binarioInicial);

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();

            int diferenca = ultimoAscii - ascii;

            if (diferenca == 0) {
                codificacao.append(ZERO);
                continue;
            }

            codificacao.append(UM);

            codificacao.append(diferenca > 0 ? ZERO : UM);

            StringBuilder binario = criarBinario(abs(diferenca));

            codificacao.append(binario);
            ultimoAscii = ascii;
        }

        return codificacao;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {

        int ultimoAscii = lerByte(arquivo, OFFSET);

        StringBuilder retorno = new StringBuilder();
        retorno.appendCodePoint(ultimoAscii);

        while (arquivo.available() > 0) {

            if (arquivo.read() == ZERO_ASCII) {
                retorno.appendCodePoint(ultimoAscii);
                continue;
            }

            int sinal = arquivo.read();

            int caracter = lerByte(arquivo, OFFSET - 1);

            if (sinal == ZERO_ASCII) {
                caracter *= -1;
            }

            int resultado = ultimoAscii + caracter;
            retorno.appendCodePoint(resultado);

            ultimoAscii = resultado;
        }

        return retorno;
    }
}
