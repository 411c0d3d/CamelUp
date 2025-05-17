package com.oasisstudios.camelupserver.domain.model.turn;

import com.oasisstudios.camelupserver.domain.model.domainclasses.GameConfigDOM;

import java.util.concurrent.TimeUnit;

public class ThinkingTime implements Runnable {

    private GameConfigDOM gameConfigDOM;
    private final long thinkTime;



    public ThinkingTime(GameConfigDOM gameConfigDOM){
        this.gameConfigDOM = gameConfigDOM;
        thinkTime = gameConfigDOM.getThinkingTime();
    }

    @Override
    public void run()
    {
        try {
            TimeUnit.SECONDS.sleep(thinkTime);
        } catch (InterruptedException e) {

        }

    }
}
