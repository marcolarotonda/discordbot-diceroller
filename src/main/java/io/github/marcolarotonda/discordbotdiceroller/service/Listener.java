package io.github.marcolarotonda.discordbotdiceroller.service;

import io.github.marcolarotonda.discordbotdiceroller.command.Command;
import jakarta.annotation.Nonnull;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Listener extends ListenerAdapter {

    private final List<Command> commandList;
    @Autowired
    public Listener(List<Command> commandList) {
        this.commandList = commandList;
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        List<SlashCommandData> slashCommands = commandList.stream()
                .map(command -> {
                    SlashCommandData slash = Commands.slash(command.getName(), command.getDescription());
                    command.getOptions().ifPresent(slash::addOptions);
                    return slash;
                })
                .toList();

        event.getGuild().updateCommands().addCommands(slashCommands).queue();
    }


    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        commandList.stream()
                .filter(command -> command.getName().equals(event.getName()))
                .findFirst()
                .ifPresent(command -> command.execute(event));
    }

}
