package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static br.unisinos.encoderdecoder.service.EncoderService.*;
import static java.util.Arrays.asList;

public class Fibonacci implements Encode {

    private static final int OFFSET = 1;
    private static final List<Integer> FIBO = asList(1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377);

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {
        while (arquivo.available() > 0) {
            int ascii = arquivo.read() + OFFSET;

            while (ascii > 0) {

                int indiceMaiorFibo = 0;

                StringBuilder codigoAtual = new StringBuilder();

                for (int fiboAtual = FIBO.get(0); fiboAtual <= ascii; fiboAtual = FIBO.get(indiceMaiorFibo++)) ;

                indiceMaiorFibo -= 2;

                for (; indiceMaiorFibo >= 0; indiceMaiorFibo--) {
                    int fibo = FIBO.get(indiceMaiorFibo);

                    if (ascii >= fibo) {
                        codigoAtual.insert(0, UM);
                        ascii -= fibo;
                    } else {
                        codigoAtual.insert(0, ZERO);
                    }
                }

                codigoAtual.append(UM);
                codificacao.append(codigoAtual);
            }
        }

        return codificacao;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {
        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {

            int indice = 0;
            int soma = 0;

            boolean ultimoUm = false;

            while (true) {
                int ascii = arquivo.read();

                if (ascii == UM_BYTE) {
                    if (ultimoUm) {
                        break;
                    }

                    int numero = FIBO.get(indice);
                    soma += numero;
                }

                indice++;
                ultimoUm = UM_BYTE == ascii;
            }

            retorno.appendCodePoint(soma - OFFSET);
        }

        return retorno;
    }
}