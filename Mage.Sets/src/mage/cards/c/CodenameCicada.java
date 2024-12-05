package mage.cards.c;

import mage.MageInt;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;

import java.util.UUID;

public final class CodenameCicada extends CardImpl {

    public CodenameCicada(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{W}");

        this.subtype.add(SubType.INSECT);
        this.subtype.add(SubType.SCOUT);
        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // Flying
        this.addAbility(FlyingAbility.getInstance());
    }

    private CodenameCicada(final CodenameCicada card){
        super(card);
    }

    public CodenameCicada copy(){
        return new CodenameCicada(this);
    }
}

enum CodenameCidadaAdjuster{

}
