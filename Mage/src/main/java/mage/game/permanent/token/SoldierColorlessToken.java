package mage.game.permanent.token;

import mage.MageInt;
import mage.constants.CardType;
import mage.constants.SubType;

public final class SoldierColorlessToken extends TokenImpl {

    public SoldierColorlessToken(){
        super("Soldier Token", "1/1 colorless Soldier creature token");
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.SOLDIER);
        power = new MageInt(1);
        toughness = new MageInt(1);
    }

    private SoldierColorlessToken(final SoldierColorlessToken token){
        super(token);
    }

    public SoldierColorlessToken copy(){
        return new SoldierColorlessToken(this);
    }
}
