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

## Configuração do keycloak

Abaixo temos o passo-a-passo da configuração do keycloak, criação de realm, client, policy, permission, resource, role e users.

Tela inicial de acesso após subir o servidor keycloak no docker, vamos clicar em "Manage realms".

![pt_01_Tela_Inicial](https://github.com/user-attachments/assets/a3c74661-c570-4ca3-8358-f29a79ce8d0d)

Nesse ponto vamos clicar em "Create realm".

![pt_02_Criando_realm](https://github.com/user-attachments/assets/23db7e0e-5e46-4570-968c-e8447009d0cf)

Aqui vamos definir o nome do nosso realm e se ele está habilitado, após isso vamos clicar em "Create".

![pt_03_Criando_realm](https://github.com/user-attachments/assets/fe3469f4-57f8-465a-9969-7e3f603c02e8)

No menu lateral, vamos clicar em "Clients", os clientes no contexto do keycloak são as nossas aplicações. Vamos criar um client para representar o nosso microservice. Vamos clicar em "Create client".

![pt_04_Criando_client](https://github.com/user-attachments/assets/ce2d1c16-90c3-403f-b959-82be7e12d913)

No primeiro step definimos o nome do nosso client e uma descrição, então clicamos em "Next".

![pt_05_1_Criando_client](https://github.com/user-attachments/assets/a557f2bc-2fd4-4944-af4a-1df60481e953)

No segundo step vamos habilitar as opções "Client authenticaton", "Authorization", "Standard flow" e "Direct access grants".

![pt_05_2_Criando_client](https://github.com/user-attachments/assets/81d6ed3c-0bd3-4c55-85f1-e684045de1a2)

Terceiro step vamos definir as rotas validas para redirecionamento e origens, que em ambas, por ser um cenário de teste, vamos deixar com `*`, assim qualquer origem e redirect é válido. Então clicamos em "Save" finalizando a criação do nosso client.

![pt_05_3_Criando_client](https://github.com/user-attachments/assets/72fa44e5-438e-4718-a002-c45f6199968f)

Vamos então criar um papel, uma "role", ela será necessária quando criarmos nossas policies. Vamos clicar no menu lateral em "Realm roles" e "Create role".

![pt_06_01_criar_role](https://github.com/user-attachments/assets/8849007f-026a-43a4-ba4a-03ebeff6765e)

Definimos o nome e a descrição desse papel, então clicamos em "Save".

![pt_06_02_criar_role](https://github.com/user-attachments/assets/a303236e-b911-4a37-afe7-5060bb72c5cf)

Novamente voltamos ao nosso client, no menu lateral clicamos na tab dele e selecionamos o client criado na listagem.

![pt_06_03_criar_role](https://github.com/user-attachments/assets/b7c37ace-a1f5-48a6-b75a-d9cdf128c6cb)

No client, vamos clicar na tab "Authorization" e "Resources", vamos então excluir o resource padrão, clicando nos três pontinhos e "Delete".

![pt_07_delete_resource_default](https://github.com/user-attachments/assets/c121e51b-7bf9-4219-99c1-f1c173749aff)

Vamos fazer o mesmo processo do print anterior, porém para a "Default Policy.

![pt_08_delete_policy_default](https://github.com/user-attachments/assets/9c086694-0a99-4fa9-aec2-cbf9c296638f)

Vamos então clicar na tab "Resources" novamente e vamos criar uma nova.

![pt_09_create_resource](https://github.com/user-attachments/assets/06584bc8-2708-47eb-bde6-e080af9c901c)

Vamos então definir o nome do recurso, as rotas que desejamos proteger nesse recurso, após isso clicar em "Save".

![pt_10_create_resource](https://github.com/user-attachments/assets/6af13ef8-d13f-41c5-886b-b58e4bead414)

Agora vamos criar a policy.

![pt_11_create_policy](https://github.com/user-attachments/assets/88e87638-c6cb-4458-9529-34552604388b)

Ela será do tipo "role", ou seja, vamos criar uma policy que diz que o usuário precisa ter um determinado papel para acessar aquele recurso.

![pt_12_create_policy_role](https://github.com/user-attachments/assets/d2ba5f87-2a4f-4f2a-8a23-11b61a0d5555)

Buscamos por "realm roles" e selecionamos a role que criamos anteriormente, então clicamos em "Assign".

![pt_12_create_policy_role_1](https://github.com/user-attachments/assets/47557812-d7c9-4af2-ae22-9ed5515a85f8)

Definimos que a role selecionada é obrigatória e clicamos em "Save".

![pt_12_create_policy_role_2](https://github.com/user-attachments/assets/38633c09-5857-4ea9-aa66-5c36a736975c)

Último passo de configuração do client é definir a "permission".

![pt_13_1_create_permission](https://github.com/user-attachments/assets/53449d59-44df-4ecd-9bb0-e632bbf2328d)

Aqui definimos o nome da permissão, qual o recurso(Resource) e qual política(policy) será aplicada. Após definido os valores clicamos em "Save".

![pt_13_2_create_permission.png](https://github.com/user-attachments/assets/18c8bbef-0361-44df-b6a1-9e5b48f5c28d)

Agora no menu lateral, vamos acessar "Users" e criar nosso primeiro usuário, para então realizar a geração de token com ele.

![pt_14_1_create_user](https://github.com/user-attachments/assets/9c4d4c51-5e31-4c18-80c1-4b6d2be52509)

Definimos alguns valores para o usuário, como email validado, nome, email, nome e sobrenome. Clicamos em "Create".

![pt_14_2_create_user](https://github.com/user-attachments/assets/05d907bc-cd2e-417f-9493-ba05e323ef48)

Após criado, podemos definir qual a senha dele. Sim, esse processo em um ambiente real seria orquestrado pela aplicação.

![pt_14_2_create_user.png](https://github.com/user-attachments/assets/2e93a503-e8f7-4caf-b069-da27daff17ff)

Definimos um valor de senha e definimos como não temporário.

![pt_14_3_create_user](https://github.com/user-attachments/assets/b17236c8-f9ea-4436-a547-685208811e77)

Para gerar nosso token precisamos do nome e do **Client Secret** do nosso client, podemos encontrar esses valores em "Credentials".

![pt_15_client_secret](https://github.com/user-attachments/assets/940bea66-c44a-4a05-a310-2c6ca65833a6)

Então, no insomnia vamos definir a URL de geração de token, e os valores no form-url-encoded.

URL Token: `http://localhost:8282/realms/keycloak-sample/protocol/openid-connect/token`

Valores do form-url-endcoded: grant_type, username e password.

![pt_16_1_kc](https://github.com/user-attachments/assets/c57f5219-14a1-46f6-a785-18ce30b18b1d)

No token basic definimos o **nome do client** e o **Client Secret**, que visualizamos onde encontrar a dois prints atrás.

![pt_16_2_kc](https://github.com/user-attachments/assets/436a6606-72a9-467d-9508-1f3ba822c887)

---

## Execução do projeto

Trata-se de um simples end-point que é protegido pelo keycloak, retornando string como response quando usuário autenticado e autorizado.

Podemos executar o projeto com o comando abaixo(**na raiz do projeto**):
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

https://quarkus.io/guides/security-keycloak-authorization