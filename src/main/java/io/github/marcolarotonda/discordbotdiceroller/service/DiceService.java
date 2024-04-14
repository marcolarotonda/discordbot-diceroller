package io.github.marcolarotonda.discordbotdiceroller.service;

import io.github.marcolarotonda.dicerollerclient.service.DiceRollerService;
import io.github.marcolarotonda.dicerollerutil.model.RollOption;
import io.github.marcolarotonda.dicerollerutil.model.RollResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiceService {

    private final DiceRollerService client;

    @Autowired
    public DiceService(DiceRollerService client) {
        this.client = client;
    }

    public RollResult roll(RollOption rollOption) {
        return client.roll(rollOption);
    }

    public int rollGetTotal(RollOption rollOption) {
        return roll(rollOption).getTotal();
    }
}
