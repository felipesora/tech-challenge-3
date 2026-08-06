package com.techchallenge.appointment_service.infrastructure.config;

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
                        .title("Appointment Service")
                        .version("1.0.0")
                        .description("""
                        Microsserviço responsável pelo gerenciamento de consultas médicas do sistema.
                                
                        Este serviço fornece funcionalidades para:
                        * Agendamento de consultas;
                        * Consulta de consultas agendadas;
                        * Atualização de consultas;
                        * Cancelamento de consultas;
                        * Consulta do histórico por meio de GraphQL;
                        * Publicação de eventos para o serviço de notificações via RabbitMQ.
                                
                        O Appointment Service faz parte da arquitetura de microsserviços do Tech Challenge e é responsável por toda a regra de negócio relacionada ao agendamento de consultas entre médicos, enfermeiros e pacientes.
                        
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
