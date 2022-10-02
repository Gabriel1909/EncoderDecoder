package br.unisinos.encoderdecoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static br.unisinos.encoderdecoder.service.Utils.*;

@Service
public class EncoderRuidoService {

    private static final int TAMANHO_CODEWORD = 4;
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

            adicionarCrc(codigoEncode, divisor, codificacao);

            adicionarHamming(codificacao, input);

            return getBytes(codificacao);
        }
    }

    private void adicionarCrc(int codigoEncode, int divisor, StringBuilder codificacao) {

        StringBuilder total = new StringBuilder(criarBinario(codigoEncode));
        total.append(criarBinario(divisor));

        total.append(ZERO.repeat(8));

        int crc = calcularCrc(total);

        codificacao.append(criarBinario(crc));
    }

    private int calcularCrc(StringBuilder divisor) {

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

        return calcularCrc(new StringBuilder(Integer.toBinaryString(resultado)));
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

    public byte[] decodeRuido(MultipartFile arquivo) {
        return null;
    }
}
