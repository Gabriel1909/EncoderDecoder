# Trabalho para codificação e decodificação de arquivos

## Requisitos técnicos:

- Java SDK +17
- Maven +3.6.3
- Recomendável a utilização da IDE IntelliJ, ou outra IDE que facilite para subir a aplicação  
- Recomendável postman para requisições

## Descrição

O trabalho consiste na codifição e decodificação de arquivos, com as seguintes estratégias de codificação suportadas: Golomb, Elias-Gamma, Fibonacci, Unária, Delta

Além da codificação, também é possivel adicionar o tratamento de ruídos com as técnicas de CRC-8 e Codewords Hamming.
As funcionalidades de codificação podem ser utilizadas de forma independente, ou codificação e tratamento de ruído ao mesmo tempo

## Utilização 

É um projeto Springboot que funciona através de uma API Rest, rodando na porta 8080. Foi dívido em 6 endpoints, entre codificar/decodificar e utilizar as técnicas bases, tratamento de ruído ou ambas. 

Para subir o projeto, é necessário executar a classe br.unisinos.encoderdecoder.Application

Todos os endpoints usam o método POST, e a adição de um arquivo com o nome de "arquivo", utilizando form-data como o corpo da requisição

Foi disponibilizado uma collection do postman dentro da pasta /arquivos, para facilitar a utilização, assim como os arquivos bases alice29.txt e sum usado para os testes

Os endpoints de codificação (encode) utilizam um PathVariable para definir qual a estratégia de codificação, relacionada a seguinte tabela:

- G4 = Golomb com divisor 4
- E = Elias-Gamma
- F = Fibonacci
- U = Unária
- D = Delta

É importante notar que a codificação Golomb é a única com mais de 1 caracter, onde é inserido o divisor após a letra "G", exemplo: G4, G8, G16

Fazem somente a codificação e decodificação dos arquivos:
- http://localhost:8080/encode/{codificador}
- http://localhost:8080/decode

Fazem somente a adição e tratamento/remoção de ruídos:
- http://localhost:8080/encode/tratamento-ruido/{codificador}
- http://localhost:8080/decode/tratamento-ruido


Fazem as 2 funcionalidades ao mesmo tempo:
- http://localhost:8080/encode/total/{codificador}
- http://localhost:8080/decode/total

## Informações sobre a codificação

O arquivo codificado utiliza 2 bytes como header. O primeiro define qual codificação foi implementada, baseada na seguinte tabela em binário:

- 0 = Golomb
- 1 = Elias-Gamma
- 2 = Fibonacci
- 3 = Unária
- 4 = Delta

O segundo byte informa o divisor usado na codificação Golomb, sendo preenchido como um byte 0 para os outros formatos de codificação. Por ser utilzado 8 bits, o limite de divisor é 511 

O arquivo com tratamento de ruído, usa os mesmos 2 bytes anteriores, porém com a adição de um novo byte de header para informar o cálculo CRC-8. O cálculo de CRC-8 é baseado nos 2 primeiros bytes de header.

No momento de resolver o tratamento de ruído, temos 3 possíveis cenários em relação a ruido:

- CRC-8 calculado não equivale ao CRC-8 no header: é informado um erro na requisição. Provavel que o arquivo tenha sido corrompido.
- 1 bit errado em um codeword Hamming: esse bit é corrigido, é logado uma mensagem no sistema e a decodificação continua.
- Mais de 1 bit errado em um codeword Hamming: é informado erro na requisição