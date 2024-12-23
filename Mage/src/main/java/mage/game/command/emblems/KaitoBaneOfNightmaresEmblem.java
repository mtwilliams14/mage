package mage.game.command.emblems;

import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.common.continuous.BoostControlledEffect;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.game.command.Emblem;

public class KaitoBaneOfNightmaresEmblem extends Emblem {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent(SubType.NINJA, "Ninjas");

    public KaitoBaneOfNightmaresEmblem(){
        super("Emblem Kaito");
        BoostControlledEffect effect = new BoostControlledEffect(1, 1, Duration.EndOfGame, filter);
        Ability ability = new SimpleStaticAbility(Zone.COMMAND, effect);
        this.getAbilities().add(ability);
    }

    private KaitoBaneOfNightmaresEmblem(final KaitoBaneOfNightmaresEmblem card) {
        super(card);
    }

    @Override
    public KaitoBaneOfNightmaresEmblem copy() {
        return new KaitoBaneOfNightmaresEmblem(this);
    }
}
