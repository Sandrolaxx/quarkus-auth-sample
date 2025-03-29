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
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

## 2. Implementando JWT no Quarkus

### 2.1 Configurando Dependências
Para usar JWT no **Quarkus**, adicione as seguintes dependências ao `pom.xml` (Maven):

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>
```

### 2.2 Configuração no `application.properties`
Adicione as configurações para habilitar JWT no Quarkus:
```properties
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=SAMPLE-JWT-API
```

- `mp.jwt.verify.publickey.location` indica onde está localizado o **chave pública** usada para verificar o JWT.
- `mp.jwt.verify.issuer` define a origem válida dos tokens, pode ser uma URL.

### 2.3 Criando uma API Segura
Crie um endpoint protegido por JWT:

```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.inject.Inject;

@Path("/secure")
public class SecureResource {

    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ADMIN"})
    public String securedEndpoint() {
        return "Usuário autenticado: " + jwt.getName();
    }
}
```

- `@RolesAllowed({"ADMIN"})` → Apenas usuários com a role "ADMIN" podem acessar.
- `@Inject JsonWebToken jwt` → Permite acessar informações do JWT dentro da API.

### 2.4 Gerando Tokens JWT

Podemos encontra a implementação completa no arquivo [TokenUtils](/src/main/java/com/aktie/auth/TokenUtils.java).

```
Após gerar o token no end-point /auth podemos utilizado nas requests onde o token é obrigatório:
```http
Authorization: Bearer <TOKEN>
```

## 3. Conclusão
- O **JWT** é um método seguro para autenticação em APIs.
- O **Quarkus** facilita a implementação de autenticação baseada em JWT.
- Tokens são verificados automaticamente via configurações do Quarkus.

Para mais detalhes, consulte a [documentação oficial do Quarkus JWT](https://quarkus.io/guides/security-jwt).