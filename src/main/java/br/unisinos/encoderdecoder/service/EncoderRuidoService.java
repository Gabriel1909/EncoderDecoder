package br.unisinos.encoderdecoder.service;

import br.unisinos.encoderdecoder.exception.DecodificacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;

@Service
public class EncoderRuidoService {

    private static final int TAMANHO_CODEWORD = 4;
    private static final int TAMANHO_CODEWORD_RUIDO = 7;
    public static final String EQUACAO_CRC8 = "100000111";

    @Autowired
    private EncoderService encoderService;

    public byte[] encodeRuido(MultipartFile arquivo) throws IOException {

        try (InputStream input = arquivo.getInputStream()) {

            StringBuilder codificacao = new StringBuilder();

            int codigoEncode = lerByte(input);
            int divisor = lerByte(input);

            codificacao.append(criarBinario(codigoEncode));
            codificacao.append(criarBinario(divisor));

            int crc = calcularCrc(codigoEncode, divisor);

            codificacao.append(criarBinario(crc));

            adicionarHamming(codificacao, input);

            return getBytes(codificacao);
        }
    }

    private int calcularCrc(int codigoEncode, int divisor) {

        StringBuilder total = new StringBuilder(criarBinario(codigoEncode));
        total.append(criarBinario(divisor));

        total.append(ZERO.repeat(8));

        return divisaoCrc(total);
    }

    private int divisaoCrc(StringBuilder divisor) {

        while (divisor.charAt(0) == '0') {
            divisor.deleteCharAt(0);
        }

        StringBuilder equacaoCrc8 = new StringBuilder(EQUACAO_CRC8);
        int tamanhoEquacao = equacaoCrc8.length();

        int diferenca = divisor.length() - tamanhoEquacao;

        equacaoCrc8.append(ZERO.repeat(diferenca));

        int resultado = Integer.parseInt(divisor.toString(), 2) ^ Integer.parseInt(equacaoCrc8.toString(), 2);

        if (Integer.toBinaryString(resultado).length() < tamanhoEquacao) {
            return resultado;
        }

        return divisaoCrc(new StringBuilder(Integer.toBinaryString(resultado)));
    }


    private void adicionarHamming(StringBuilder codificacao, InputStream input) throws IOException {

        while (input.available() > 0) {

            StringBuilder binario = lerByteString(input, TAMANHO_CODEWORD);

            codificacao.append(binario);

            if (binario.length() == TAMANHO_CODEWORD) {
                adicionarHamming4Bits(binario.toString(), codificacao);
            }
        }
    }

    private void adicionarHamming4Bits(String binario, StringBuilder codificacao) {

        char s1 = binario.charAt(0);
        char s2 = binario.charAt(1);
        char s3 = binario.charAt(2);
        char s4 = binario.charAt(3);

        codificacao.appendCodePoint(s1 ^ s2 ^ s3);
        codificacao.appendCodePoint(s2 ^ s3 ^ s4);
        codificacao.appendCodePoint(s1 ^ s3 ^ s4);
    }

    public byte[] decodeRuido(MultipartFile arquivo) throws IOException {

        try (InputStream input = arquivo.getInputStream()) {

            StringBuilder codificacao = new StringBuilder();

            int codigoEncode = lerByte(input);
            int divisor = lerByte(input);

            codificacao.append(criarBinario(codigoEncode));
            codificacao.append(criarBinario(divisor));

            int crcArquivo = lerByte(input);

            int crc = calcularCrc(codigoEncode, divisor);

            if (crcArquivo != crc) {
                throw new DecodificacaoException("CRC incorreto, processo será interrompido.");
            }

            validarHamming(codificacao, input);

            return getBytes(codificacao);
        }
    }

    private void validarHamming(StringBuilder codificacao, InputStream input) throws IOException {

        while (input.available() > 0) {

            StringBuilder binario = lerByteString(input, TAMANHO_CODEWORD_RUIDO);

            if (binario.length() == TAMANHO_CODEWORD_RUIDO) {
                validarHamming4Bits(binario.toString(), codificacao);
            } else {
                codificacao.append(binario);
            }
        }
    }

    private void validarHamming4Bits(String binario, StringBuilder codificacao) {
        char s1 = binario.charAt(0);
        char s2 = binario.charAt(1);
        char s3 = binario.charAt(2);
        char s4 = binario.charAt(3);

        char t1 = binario.charAt(4);
        char t2 = binario.charAt(5);
        char t3 = binario.charAt(6);


        if (!validarCaracterHamming(s1, s2, s3, s4, t1, t2, t3)) {

            if (validarCaracterHamming(inverso(s1), s2, s3, s4, t1, t2, t3)) {
                System.out.println("Alterado bit s1 do codeword hamming");
                s1 = inverso(s1);
            } else if (validarCaracterHamming(s1, inverso(s2), s3, s4, t1, t2, t3)) {
                System.out.println("Alterado bit s2 do codeword hamming");
                s2 = inverso(s2);
            } else if (validarCaracterHamming(s1, s2, inverso(s3), s4, t1, t2, t3)) {
                System.out.println("Alterado bit s3 do codeword hamming");
                s3 = inverso(s3);
            } else if (validarCaracterHamming(s1, s2, s3, inverso(s4), t1, t2, t3)) {
                System.out.println("Alterado bit s4 do codeword hamming");
                s4 = inverso(s4);
            } else {
                throw new DecodificacaoException("Hamming incorreto, mais de 2 alterações no mesmo codeword");
            }
        }

        codificacao.append(s1);
        codificacao.append(s2);
        codificacao.append(s3);
        codificacao.append(s4);
    }

    private boolean validarCaracterHamming(char s1, char s2, char s3, char s4, int t1, int t2, int t3) {
        return ((s1 ^ s2 ^ s3) == t1) &&
                ((s2 ^ s3 ^ s4) == t2) &&
                ((s1 ^ s3 ^ s4) == t3);
    }

    private char inverso(char caracter) {
        return (char) (caracter == ZERO_ASCII ? UM_ASCII : ZERO_ASCII);
    }
}