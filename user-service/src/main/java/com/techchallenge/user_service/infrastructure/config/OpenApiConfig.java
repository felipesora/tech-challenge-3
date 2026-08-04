package com.techchallenge.user_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service")
                        .version("1.0.0")
                        .description("""
                        Microsserviço responsável pelo gerenciamento de usuários do sistema.
                                
                        Este serviço fornece funcionalidades para:
                        * Cadastro de usuários;
                        * Consulta de usuários;
                        * Atualização de dados cadastrais;
                        * Gerenciamento dos tipos de usuário;
                        * Ativação e desativação de usuários.
                    
                        O User Service faz parte da arquitetura de microsserviços do Tech Challenge e é responsável pelas informações de identidade dos usuários da aplicação.
                        
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
                                .url("http://springdoc.org")));
    }
}
