package mage.abilities.dynamicvalue.common;

import mage.ObjectColor;
import mage.abilities.Ability;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.abilities.hint.Hint;
import mage.abilities.hint.ValueHint;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;

import java.util.stream.Collectors;

public enum ApprovalCount implements DynamicValue {

    instance;

    private final String message;
    private final String reminder;
    private final Hint hint;

    ApprovalCount(){
        this.message = "Your approval count ";
        this.reminder = "Your approval count is equal to the number of colors among" +
                "permanents you control";
        this.hint = new ValueHint(this.message.replace("your a", "A"), this);
    }

    @Override
    public int calculate(Game game, Ability sourceAbility, Effect effect) {
        return getColorUnion(game, sourceAbility).getColorCount();
    }

    static ObjectColor getColorUnion(Game game, Ability ability) {
        ObjectColor color = new ObjectColor();
        for (Permanent permanent : game.getBattlefield().getActivePermanents(
                StaticFilters.FILTER_CONTROLLED_PERMANENT, ability.getControllerId(), game
        )) {
            color.addColor(permanent.getColor(game));
            if (color.getColorCount() >= 5) {
                break;
            }
        }
        return color;
    }

    @Override
    public ApprovalCount copy() {
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString(){
        return "X";
    }

    public String getReminder(){
        return reminder;
    }

    public Hint getHint(){
        return hint;
    }
}

enum ApprovalCountHint implements Hint {
    instance;

    @Override
    public String getText(Game game, Ability ability) {
        ObjectColor color = ApprovalCount.getColorUnion(game, ability);
        return "Colors among permanents you control: " + color.getColorCount() + (
                color.getColorCount() > 0
                        ? color
                        .getColors()
                        .stream()
                        .map(ObjectColor::getDescription)
                        .collect(Collectors.joining(", ", " (", ")")) : ""
        );
    }

    @Override
    public ApprovalCountHint copy() {
        return this;
    }
}