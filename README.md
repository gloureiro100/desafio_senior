# desafio_senior

Para executar o projeto, basta baixa o repositório e executar os seguintes comandos na raiz do projeto:

mvn package -> para baixar as dependências, buildar e gerar o .jar.

.\mvnw.cmd spring-boot:run -> para executar a aplicação localmente.

A aplicação estará executando em localhost:8080.

A documentação da api está disponível através do Swagger em http://localhost:8080/swagger-ui.html.

É preciso uma conexão a um database POSTGRESQL. As configurações de conexão, usuário e senha estão em \src\main\resources\application.properties e devem ser configuradas antes de iniciar a aplicação.

/api/hospede/forahotel -> Consultar hóspedes que já realizaram o check in e não estão mais no hotel;
/api/hospede/dentrohotel -> Consultar hóspedes que ainda estão no hotel;

Consulta por documento e telefone retornam no máximo um único registro.
Consulta por nome podem retornar mais de um registro.
