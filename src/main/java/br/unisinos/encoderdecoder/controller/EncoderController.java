package br.unisinos.encoderdecoder.controller;

import br.unisinos.encoderdecoder.exception.DecodificacaoException;
import br.unisinos.encoderdecoder.service.EncoderRuidoService;
import br.unisinos.encoderdecoder.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@ControllerAdvice
@RequestMapping
public class EncoderController {

    @Autowired
    private EncoderService encoderService;

    @Autowired
    private EncoderRuidoService encoderRuidoService;

    @PostMapping("/encode/{codificador}")
    public byte[] encode(@RequestPart MultipartFile arquivo, @PathVariable String codificador) throws IOException {
        return encoderService.encode(arquivo, codificador.toUpperCase());
    }

    @PostMapping("/decode")
    public byte[] decode(@RequestPart MultipartFile arquivo) throws IOException {
        return encoderService.decode(arquivo);
    }

    @PostMapping("/encode/tratamento-ruido")
    public byte[] encodeRuido(@RequestPart MultipartFile arquivo) throws IOException {
        return encoderRuidoService.encodeRuido(arquivo);
    }

    @PostMapping("/decode/tratamento-ruido")
    public byte[] decodeRuido(@RequestPart MultipartFile arquivo) throws IOException {
        return encoderRuidoService.decodeRuido(arquivo);
    }

    @PostMapping("/encode/total/{codificador}")
    public byte[] encodeTotal(@RequestPart MultipartFile arquivo, @PathVariable String codificador) throws IOException {
        CustomMultipartFile encode = new CustomMultipartFile(encoderService.encode(arquivo, codificador.toUpperCase()));
        return encoderRuidoService.encodeRuido(encode);
    }

    @PostMapping("/decode/total")
    public byte[] decodeTotal(@RequestPart MultipartFile arquivo) throws IOException {
        CustomMultipartFile decode = new CustomMultipartFile(encoderRuidoService.decodeRuido(arquivo));
        return encoderService.decode(decode);
    }

    @ExceptionHandler(DecodificacaoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String exceptionHandler(DecodificacaoException exception){
        return exception.getMessage();
    }
}