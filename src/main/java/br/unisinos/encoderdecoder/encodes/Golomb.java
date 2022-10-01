package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.EncoderService.*;

public class Golomb implements Encode {

    private final int divisor;
    private final int tamanhoResto;

    public Golomb(char divisor) {
        this.divisor = Character.getNumericValue(divisor);
        this.tamanhoResto = (int) Math.floor(Math.log10(this.divisor) / Math.log10(2));
    }

    @Override
    public StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException {

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();

            int dividir = ascii / divisor;

            codificacao.append(ZERO.repeat(dividir));

            codificacao.append(UM);

            int resto = ascii % divisor;
            StringBuilder restoAdicionado = new StringBuilder(Integer.toBinaryString(resto));

            while (restoAdicionado.length() < tamanhoResto) {
                restoAdicionado.insert(0, ZERO);
            }

            codificacao.append(restoAdicionado);
        }

        return codificacao;
    }

    @Override
    public StringBuilder decode(InputStream arquivo) throws IOException {

        StringBuilder retorno = new StringBuilder();

        while (arquivo.available() > 0) {
            int ascii = arquivo.read();

            int dividir = 0;

            while (ascii == ZERO_BYTE) {
                dividir++;
                ascii = arquivo.read();
            }

            StringBuilder binario = new StringBuilder();

            for (int i = 0; i < tamanhoResto; i++) {
                binario.appendCodePoint(arquivo.read());
            }

            int resto = Integer.parseInt(binario.toString(), 2);

            int total = divisor * dividir + resto;

            retorno.appendCodePoint(total);
        }

        return retorno;
    }
}
