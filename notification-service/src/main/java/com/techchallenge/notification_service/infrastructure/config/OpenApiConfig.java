package com.techchallenge.notification_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service")
                        .version("1.0.0")
                        .description("""
                        Microsserviço responsável pelo gerenciamento e envio de notificações relacionadas às consultas médicas.
                        
                        Este serviço fornece funcionalidades para:
                        * Receber eventos de consultas via RabbitMQ;
                        * Processar notificações de criação, atualização e cancelamento de consultas;
                        * Registrar o histórico de notificações enviadas;
                        * Gerenciar o status das notificações (PENDENTE, ENVIADA ou ERRO);
                        * Simular o envio de lembretes aos pacientes.

                        O Notification Service faz parte da arquitetura de microsserviços do Tech Challenge e é responsável por processar eventos publicados pelo Appointment Service, desacoplando a lógica de notificações do gerenciamento das consultas.
                                
                        **Integrantes do Grupo:**
                        * Felipe Ulson Sora - RM370766
                        * Gabriel Alberto Ferreira Pinto - RM374005
                        * Jeniffer da Nobrega Bandeira - RM3711936
                        * Marco Antônio de Oliveira Gomes - RM372323
                        * Ricardo Aguirra Menendes - RM373817
                        """)
                        .contact(new Contact()
                                .name("Grupo 17 - Postech ADJ")
                                .url("https://github.com/felipesora/tech-challenge-3"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .schemaRequirement(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

    }
}
