package br.unisinos.encoderdecoder.controller;

import br.unisinos.encoderdecoder.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
public class EncoderController {

    @Autowired
    private EncoderService encoderService;

    @PostMapping("/encode/{codificador}")
    public byte[] encode(@RequestPart MultipartFile arquivo, @PathVariable String codificador) throws IOException {
        return encoderService.encode(arquivo, codificador.toUpperCase());
    }

    @PostMapping("/decode")
    public byte[] decode(@RequestPart MultipartFile arquivo) throws IOException {
        return encoderService.decode(arquivo);
    }
}