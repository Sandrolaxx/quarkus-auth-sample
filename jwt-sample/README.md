# Autenticação JWT no Quarkus

## 1. O que é JWT?

JSON Web Token (**JWT**) é um padrão para **autenticação e troca de informações seguras** entre partes. Ele é amplamente utilizado para autenticação de usuários em APIs REST e aplicações web.

### 1.1 Como funciona o JWT?

- O servidor gera um **token JWT** quando um usuário faz login.
- O token é assinado digitalmente e pode conter informações como ID do usuário e roles.
- O cliente (ex: frontend ou outra API) armazena o JWT e o envia nas requisições para o servidor.
- O servidor valida o token e permite ou nega o acesso.

Um **JWT** é composto por **três partes**:
1. **Header**: Contém o tipo de token e o algoritmo de assinatura.
2. **Payload**: Contém as informações (claims) do usuário.
3. **Signature**: Assinatura gerada com uma chave secreta ou certificado.

Exemplo de um JWT codificado:
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJTQU1QTEUtSldULUFQSSIsInVwbiI6InNhbmRyb2xheHhAZ21haWwuY29tIiwiZ3JvdXBzIjpbIlVTRVIiXSwidXVpZCI6ImNkY2I4N2NjLTU2MWMtNDYzMi05ZTU2LWFjODU3YWFmOGRkNyIsImF1ZCI6InVzaW5nLWp3dCIsImlhdCI6MTc0NTgzMTQ5MywiZXhwIjoxNzQ1ODMyMjEzLCJqdGkiOiI2ZGZkMTg0Ny1mZDJhLTRkMmMtODMyZS0xZmE2YTI4ODA2NzQifQ.S5X-7pxtZ0UqlEENIEg7-RXz-NciU5-p-zwHjedYXu95_coL994ZcRi5_S9TGtQZFw8AiNs9VaVmy7pTTZIC8cJmb5ZvX435k_oMcv88CBLkLLIaK7lc8SMBL5c9RngqUpGvrYYo1J8TkIDQyk6CBan4V1AfiPTteh0wzbWnVpYnKAOQotwMrPNMahu3pmk48LatlfCg2YUKRTLfExIwJzBAsjPxpOgfwLH2NamgRCz5Deu3xlZZM6ymfAs2SeCb9MeVa_ImYEQRBmzD_sTLjIfRMxqNnHkJzsL_D1TYKV8NzKorNBIIa1nnfgxO8VB9MC1Jt0UpNV53MljTevycrg
```

## 2. Implementando JWT no Quarkus

### 2.1 Configurando dependências
Para usar JWT no **Quarkus**, adicione as seguintes dependências ao `pom.xml` (Maven):

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>
```

### 2.2 Gerando par de chaves

Para verificar e assinar nosso token, precisamos de uma chave pública e uma privada, respectivamente. Para realizar esse processo, vamos utilizar a ferramenta **OpenSSL**, disponível para Windows e Linux. Para gerar nossos certificados, vamos executar o comando abaixo:

```bash
openssl pkcs8 -topk8 -inform PEM -in publicKey.pem -out privateKey.pem -nocrypt
openssl rsa -in publicKey.pem -pubout -outform PEM -out publicKey.pem
```

Essas chaves adicionamos na pasta resources, ao lado do `application.properties`.

### 2.2 Configuração no `application.properties`
Adicione as configurações para habilitar JWT no Quarkus:
```properties
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=SAMPLE-JWT-API
# Utilizado no fluxo com arquivo GenerateToken
smallrye.jwt.sign.key.location=privateKey.pem
```

- `mp.jwt.verify.publickey.location` indica onde está localizado o **chave pública** usada para verificar o JWT.
- `mp.jwt.verify.issuer` define a origem válida dos tokens, pode ser uma URL.
- `smallrye.jwt.sign.key.location` indica onde está localizado o **chave privada** usada para assinar o JWT.

### 2.3 Criando uma API Segura

Crie um endpoint protegido por JWT e outro público:

```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.inject.Inject;

@Path("/user")
public class SecureResource {

    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ADMIN"})
    public String securedEndpoint() {
        return "Usuário autenticado: " + jwt.getName();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String publicEndpoint() {
        return "End-point público: " + jwt.getName();
    }

}
```

- `@RolesAllowed({"ADMIN"})` → Apenas usuários com a role "ADMIN" podem acessar.
- `@Inject JsonWebToken jwt` → Permite acessar informações do JWT dentro da API.
- `publicEndpoint` → Pelo fato de não definirmos um @RolesAllowed o end-point por padrão é tratado como público.

### 2.4 Gerando Tokens JWT

Podemos encontrar a implementação simplificada utilizando muitas funcionalidades da lib no arquivo [GenerateToken](/src/main/java/com/aktie/auth/GenerateToken.java).

Podemos encontrar a implementação mais completa e implementada na "mão" no arquivo [TokenUtils](/src/main/java/com/aktie/auth/TokenUtils.java).

Após gerar o token no end-point **/auth** podemos utilizar o mesmo nas requests onde este é obrigatório:

**Header param**
```http
Authorization: Bearer <TOKEN>
```

## 3. Conclusão
- O **JWT** é um método seguro para autenticação em APIs.
- O **Quarkus** facilita a implementação de autenticação baseada em JWT.
- Tokens são verificados automaticamente via configurações do Quarkus.

Para mais detalhes, consulte a [documentação oficial do Quarkus JWT](https://quarkus.io/guides/security-jwt).

---

## 4. Execução do projeto

Trata-se de um CRUD simples, criado apenas para apresentar os conceitos de autenticação. Se deseja executar esse projeto em sua máquina, será necessário executar o postgres, recomendo a utilização do docker e deixo o comando abaixo para criação do mesmo.

```bash
docker run --name pg-sample-jwt -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres:16.2-alpine3.19
```

Após isso, podemos executar o projeto com o comando abaixo(**na raiz do projeto**):
```bash
./mvnw quarkus:dev
```

Comando para gerar o build(Versão de prod):
```bash
./mvnw clean package
```

Executando build(Versão de prod):
```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Documentação oficial

https://quarkus.io/guides/security-jwt