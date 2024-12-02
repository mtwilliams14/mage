package mage.cards.b;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.CostAdjuster;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.dynamicvalue.common.ApprovalCount;
import mage.abilities.effects.common.TapTargetEffect;
import mage.abilities.keyword.LifelinkAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.game.Game;
import mage.util.CardUtil;

import java.util.UUID;

public final class BrazenBlackcoats extends CardImpl {

    public BrazenBlackcoats(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[] {CardType.CREATURE}, "{1}{W}");

        this.addSubType(SubType.HUMAN);
        this.addSubType(SubType.SOLDIER);
        this.power = new MageInt(2);
        this.toughness = new MageInt(1);

        // Lifelink
        this.addAbility(LifelinkAbility.getInstance());

        //Approval - {5},{T}: Tap target creature. This ability costs {X} less to activate,
        //where X is the number of colors among permanents you control
        Ability ability = new SimpleActivatedAbility(new TapTargetEffect(), new ManaCostsImpl<>("{5}"));
        ability.addCost(new TapSourceCost());
        ability.setCostAdjuster(BrazenBlackcoatsAdjuster.instance);
        this.addAbility(ability);
    }

    private BrazenBlackcoats(final BrazenBlackcoats card){
        super(card);
    }

    public BrazenBlackcoats copy(){
        return new BrazenBlackcoats(this);
    }
}

enum BrazenBlackcoatsAdjuster implements CostAdjuster {
    instance;

    @Override
    public void adjustCosts(Ability ability, Game game){
        int value = ApprovalCount.instance.calculate(game, ability, null);
        if(value > 0){
            CardUtil.reduceCost(ability, value);
        }
    }
}
