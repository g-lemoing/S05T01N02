package cat.itacademy.s05.t01.n01.model;

import jakarta.validation.constraints.NotNull;

public class GameAction {
    @NotNull(message = "Action is required")
    private Action action;
    private int betAmount;

    public GameAction(){
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
}
