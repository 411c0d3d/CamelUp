package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerDOM;

// Is used by PlayerCardHandler  to reward a certain turn based action.
public class TurnRewardHelper {

    // Enum to define the types of rewards and their associated money value
    public enum Reward {
        ROLLED_DICE(1),
        STEPPED_PLAYERCARD(1);

        private final int money;

        // Constructor to assign the money value to each enum constant
        Reward(int money) {
            this.money = money;
        }

        // Method to get the value of money for the reward
        public int getValue() {
            return money;
        }
    }

    public void rewardStageRollToPlayer(PlayerDOM playerDOM) {
        rewardPlayer(playerDOM, Reward.ROLLED_DICE);
    }

    public void rewardSteppedPlayerCardToPlayer(PlayerDOM playerDOM) {
        rewardPlayer(playerDOM, Reward.STEPPED_PLAYERCARD);
    }

    // Rewards the playerDOM based on the type of reward by adding the corresponding money
    private void rewardPlayer(PlayerDOM playerDOM, Reward reward) {
        if (playerDOM != null) {
            playerDOM.addToMoney(reward.getValue());  // Adds the value associated with the reward
        } else {
            throw new IllegalArgumentException("PlayerDOM to be rewarded is null.");
        }
    }
}

