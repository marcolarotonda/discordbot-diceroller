package io.github.marcolarotonda.discordbotdiceroller.configuration;

import io.github.marcolarotonda.discordbotdiceroller.command.Command;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommandListProvider {

    @Bean
    public List<Command> commandList() {
        return new ArrayList<>();
    }
}
