package br.unisinos.encoderdecoder.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {

    private Utils(){}

    public static final String ZERO = "0";
    public static final String UM = "1";
    public static final int ZERO_ASCII = 48;
    public static final int UM_ASCII = 49;
    public static final int TAMANHO_BYTE = 8;
    public static final String ZERO_BYTE = "00000000";

    public static StringBuilder criarBinario(int valor) {
        return criarBinario(valor, TAMANHO_BYTE);
    }

    public static StringBuilder criarBinario(int valor, int quantidade) {
        StringBuilder retorno = new StringBuilder(Integer.toBinaryString(valor));
        retorno.insert(0, ZERO.repeat(quantidade - retorno.length()));
        return retorno;
    }

    public static int lerByte(InputStream input) throws IOException {
        return lerByte(input, TAMANHO_BYTE);
    }

    public static int lerByte(InputStream arquivo, int quantidade) throws IOException {
        return Integer.parseInt(lerByteString(arquivo, quantidade).toString(), 2);
    }

    public static StringBuilder lerByteString(InputStream arquivo, int quantidade) throws IOException {
        StringBuilder retorno = new StringBuilder();

        for (int i = 0; i < quantidade && arquivo.available() > 0; i++) {
            retorno.appendCodePoint(arquivo.read());
        }

        return retorno;
    }


    public static byte[] getBytes(StringBuilder decode) {
        return decode.toString().getBytes(StandardCharsets.ISO_8859_1);
    }
}
