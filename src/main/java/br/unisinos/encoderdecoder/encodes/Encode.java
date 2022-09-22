package br.unisinos.encoderdecoder.encodes;

import java.io.IOException;
import java.io.InputStream;

public interface Encode {

    StringBuilder encode(InputStream arquivo, StringBuilder codificacao) throws IOException;
    StringBuilder decode(InputStream arquivo) throws IOException;
}
