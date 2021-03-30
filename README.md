# Spring-Native POC
A simple POC to check Spring Native features

## O que é Spring-Native
O objetivo do projeto Spring Native é prover suporte para compilação de aplicações Spring para executáveis nativos (sem a necessidade da Java Virtual Machine - JVM) usando o compilador [GraalVM](https://www.graalvm.org/) [native-image](https://www.graalvm.org/reference-manual/native-image/).

A ideia é que, comparado com a JVM, native-images possibilitam imagens menores e mais performáticas para vários casos de uso, como microserviços e funções (como Lambda functions). 
Além disso, isto serve como uma alternativa à JVM e provê uma aplicação nativa designada para ser distribuída em containers leves.

Além disso, como o código gerado é nativo, permite vantagens como um startup instantâneo, um pico de performance intantâneo e menor consumo de memória.

## Diferenças
As principais diferenças entre uma aplicação JVM e uma aplicação nativa são:
- A análise estática da aplicação é realizada no momento do build
- As partes não usadas da aplicação são removidas no momento do build
- São necessárias configurações para reflection, resources e proxies dinâmicos
- O classpath é fixo em tempo de build
- Não existe lazy loading de classes, ou seja, todas as classes que forem colocadas no executável são carregadas em memória no tempo de startup
- Algum código poderá ser executado em tempo de build

Além disso, o próprio projeto cita que existem alguns aspectos de aplicações Java que ainda não estão completamente suportados.

## Trade-Offs

Existem alguns pontos negativos que devem ser citados, como:
- O build de um native-image é mais demorado que a geração de uma aplicação normal
- Possui menos otimizações de runtime após sua inicialização
- O uso ainda é experimental (o próprio Spring Native ainda está em modo beta) e temos poucos use-cases em produção,

## Colocando em prática utilizando Buildpack

Buildpacks é um framework para suporte ao runtime de aplicações. Usado pelo Heroku e Cloudfoundry, a ideia é verificar quais são as dependências necesárias para uma aplicação e como configurar a aplicação para se comunicar com o serviço de runtime.

A vantagem é deixar para o desenvolvedor apenas a preocupação com a aplicação, o cluster de runtime com o controle de containers e imagens e o cloud provider com o controle dos clusters.

Spring provê suporte à imagens Buildpacks, o que deixa as imagens geradas bem menores e simples. Sendo assim, vamos usar esta abordagem, explicada na documentação do Spring Native, para gerar a nossa imagem e executar nossa aplicação nativa.

### Parte 1 - Criando o esqueleto da aplicação
Vamos criar uma aplicação Rest para uma calculadora, com as 4 operações básicas apenas.
Esta aplicação foi criada através do [Spring Initializr](https://starter.spring.io), selecionando o Gradle como ferramenta de build/dependências e as dependências Spring Native e Spring Web

### Parte 2 - Buildando e executando a aplicação (JAR)
Para isto basta executar a aplicação normalmente:
```
gradle bootRun
```
No meu computador, o arquivo JAR gerado ficou com 17MB de espaço. Lembrando que, para executá-lo, é necessária a instalação da JVM também

### Parte 2 - Testando a aplicação
Para testar a aplicação, podem ser realizadas requisições aos seguintes endpoints:
- Soma: http://localhost:8080/sum/1/2
- Subtração: http://localhost:8080/sub/1/2
- Multiplicação: http://localhost:8080/mul/1/2
- Divisão: http://localhost:8080/div/1/2

### Parte 2 - Buildando e executando a aplicação (com BuildPack e Native)
Para isto gere a imagem com:
```
gradle bootBuildImage
```
Após algum tempo, ele irá gerar a imagem spring-native-poc:0.0.1-SNAPSHOT

Notem que ele detectou para a construção as seguintes dependências:
```
    [creator]     ===> DETECTING
    [creator]     4 of 11 buildpacks participating
    [creator]     paketo-buildpacks/graalvm        6.0.0
    [creator]     paketo-buildpacks/executable-jar 5.0.0
    [creator]     paketo-buildpacks/spring-boot    4.1.0
    [creator]     paketo-buildpacks/native-image   4.0.0
 ```
Ou seja, a GraalVM, JAR, Spring Boot e a native-image.

Após isto, ele irá realizara compilação e geração da imagem. No meu exemplo, a imagem completa ficou com 89Mb.
Para executar a imagem, basta executar o comando abaixo:
```
docker run -p 8080:8080 spring-native-poc:0.0.1-SNAPSHOT  
```