# Autenticação Keycloak no Quarkus

## 1. O que é keycloak?

O Keycloak é uma solução open source para gerenciar identidade e acesso (IAM - Identity and Access Management). Ele oferece funcionalidades prontas para proteger aplicações e serviços, como autenticação de usuários, autorização de acesso, login único (SSO), autenticação via redes sociais, integração com diretórios corporativos (como LDAP e Active Directory) e emissão de tokens JWT.

### 1.1 Como funciona o Keycloak?

O Keycloak atua como um servidor de identidade. Quando uma aplicação precisa autenticar um usuário:
 - Ela redireciona o usuário para o Keycloak.
 - O Keycloak realiza a autenticação (com login e senha, rede social, LDAP etc.).
 - Após autenticar, o Keycloak retorna informações (como um token JWT) para a aplicação.
 - A aplicação usa esse token para autorizar acessos e proteger recursos.

O Keycloak também fornece interfaces administrativas para criar usuários, configurar políticas de segurança, criar clientes (aplicações) e gerenciar permissões de forma centralizada.

## Executando o servidor Keycloak

Para executar um servidor para ambiente de desenvolvimento, podemos realizar o comando abaixo:

```
docker run --name kc-server-dev -p 8282:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.2.1 start-dev
```

### ⚠ATENÇÃO!

Lembrando que em ambiente de produção é necessário se aprofundar no tópico para criar um servidor KC configurado corretamente, esse comando acima não serve para esse contexto, apenas desenvolvimento.

Os clientes cadastrados nem estão sendo persistidos, apenas salvos no conteiner docker, que nem volume tem, uma configuração de produção terá SSL e um banco de dados postgres apartado somente para isso!