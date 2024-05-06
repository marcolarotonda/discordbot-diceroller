package io.github.marcolarotonda.discordbotdiceroller.command.impl;

import io.github.marcolarotonda.dicerollerutil.enumeration.DiceType;
import io.github.marcolarotonda.dicerollerutil.enumeration.RollType;
import io.github.marcolarotonda.dicerollerutil.model.RollOption;
import io.github.marcolarotonda.dicerollerutil.model.RollResult;
import io.github.marcolarotonda.discordbotdiceroller.command.Command;
import io.github.marcolarotonda.discordbotdiceroller.service.DiceService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RollCmd implements Command {

    private final DiceService diceService;
    private final int defaultDie;

    @Autowired
    public RollCmd(@Value("${application.default-die}") int defaultDie,
            DiceService diceService) {
        this.defaultDie = defaultDie;
        this.diceService = diceService;
    }

    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getDescription() {
        return "roll a dice";
    }

    @Override
    public Optional<List<OptionData>> getOptions() {
        OptionData diceType = new OptionData(OptionType.INTEGER, "dice", "sides of the dice to roll", false)
                .addChoice("4", 4)
                .addChoice("6", 6)
                .addChoice("8", 8)
                .addChoice("10", 10)
                .addChoice("12", 12)
                .addChoice("20", 20)
                .addChoice("100", 100);

        OptionData modifier = new OptionData(OptionType.INTEGER, "modifier", "value to add to the result", false);

        OptionData quantity = new OptionData(OptionType.INTEGER, "quantity", "number of dices to roll", false)
                .setMinValue(1);

        OptionData rollType = new OptionData(OptionType.STRING, "mode", "advantage/disadvantage/normal", false)
                .addChoice("normal", "NORMAL")
                .addChoice("advantage", "ADVANTAGE")
                .addChoice("disadvantage", "DISADVANTAGE");

        OptionData reroll = new OptionData(OptionType.BOOLEAN, "reroll", "reroll ones and twos for the first time", false);
        return Optional.of(List.of(diceType,
                modifier,
                quantity,
                rollType,
                reroll));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int dice;
        try {
            dice = event.getOption("dice").getAsInt();
        } catch (NullPointerException e) {
            dice = this.defaultDie;
        }
        DiceType diceType = DiceType.valueOf(String.format("D%d", dice));

        int modifier;
        try {
            modifier = event.getOption("modifier").getAsInt();
        } catch (NullPointerException e) {
            modifier = 0;
        }

        int quantity;
        try {
            quantity = event.getOption("quantity").getAsInt();
        } catch (NullPointerException e) {
            quantity = 1;
        }

        String mode;
        try {
            mode = event.getOption("mode").getAsString();
        } catch (NullPointerException e) {
            mode = "NORMAL";
        }

        RollType rollType = RollType.valueOf(mode);
        boolean reroll;
        try {
            reroll = event.getOption("reroll").getAsBoolean();
        } catch (NullPointerException e) {
            reroll = false;
        }

        RollOption rollOption = RollOption.builder()
                .diceType(diceType)
                .modifier(modifier)
                .quantity(quantity)
                .rollType(rollType)
                .merit(reroll)
                .build();
        RollResult roll = diceService.roll(rollOption);
        List<Integer> singleResults = roll.getSingleResults();
        int total = roll.getTotal();
        String unformatted = """
                **SUMMARY**
                User: %s
                DiceType: %s
                Quantity: %d
                Modifier: %d
                Mode %s
                Reroll: %b
                
                **RESULTS**
                Rolls: %s
                Total %d
                """;
        String message = String.format(unformatted,
                event.getUser().getName(),
                diceType,
                quantity,
                modifier,
                mode,
                reroll,
                singleResults,
                total);
        event.reply(message).queue();
    }
}
