package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.EncoderService.*;
import static java.lang.Math.abs;

public class Delta implements Encode {

    public static final int OFFSET = 9;

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        int ultimoAscii = arquivo.read();

        StringBuilder binarioInicial = new StringBuilder(Integer.toBinaryString(ultimoAscii));

        while (binarioInicial.length() < OFFSET) {
            binarioInicial.insert(0, ZERO);
        }

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

            StringBuilder binario = new StringBuilder(Integer.toBinaryString(abs(diferenca)));

            while (binario.length() < OFFSET - 1) {
                binario.insert(0, ZERO);
            }

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

            if (arquivo.read() == ZERO_BYTE) {
                retorno.appendCodePoint(ultimoAscii);
                continue;
            }

            int sinal = arquivo.read();

            int caracter = lerByte(arquivo, OFFSET - 1);

            if (sinal == ZERO_BYTE) {
                caracter *= -1;
            }

            int resultado = ultimoAscii + caracter;
            retorno.appendCodePoint(resultado);

            ultimoAscii = resultado;
        }

        return retorno;
    }

    private int lerByte(InputStream arquivo, int quantidade) throws IOException {
        StringBuilder caracterString = new StringBuilder();

        for (int i = 0; i < quantidade; i++) {
            caracterString.appendCodePoint(arquivo.read());
        }

        return Integer.parseInt(caracterString.toString(), 2);
    }
}
