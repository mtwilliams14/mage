package mage.cards.b;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.ActivateAsSorceryActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.keyword.DefenderAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.game.permanent.token.SoldierColorlessToken;

import java.util.UUID;

public final class BorderJunction extends CardImpl {

    public BorderJunction(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT, CardType.CREATURE}, "{W}");

        this.subtype.add(SubType.WALL);
        this.power = new MageInt(0);
        this.toughness = new MageInt(4);

        // Defender
        this.addAbility(DefenderAbility.getInstance());

        // {3}{W},{T},Sacrifice Border Junction: Create three 1/1 colorless Soldier creature tokens. Activate only as a sorcery
        Ability createTokensAbility = new ActivateAsSorceryActivatedAbility(new CreateTokenEffect(new SoldierColorlessToken(), 3), new ManaCostsImpl<>("{3}{W}"));
        createTokensAbility.addCost(new TapSourceCost());
        createTokensAbility.addCost(new SacrificeSourceCost());
        this.addAbility(createTokensAbility);
    }

    private BorderJunction(final BorderJunction card){
        super(card);
    }

    public BorderJunction copy(){
        return new BorderJunction(this);
    }
}
