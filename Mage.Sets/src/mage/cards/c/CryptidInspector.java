package mage.cards.c;

import mage.MageInt;
import mage.abilities.common.EntersBattlefieldAllTriggeredAbility;
import mage.abilities.common.TurnedFaceUpAllTriggeredAbility;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.keyword.VigilanceAbility;
import mage.abilities.meta.OrTriggeredAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.card.FaceDownPredicate;

import java.util.UUID;

public class CryptidInspector extends CardImpl {

    private static final FilterPermanent filter = new FilterControlledPermanent("a face-down permanent you control");

    static {
        filter.add(FaceDownPredicate.instance);
    }

    private static final FilterPermanent filter2 = new FilterControlledPermanent("{this} or another permanent you control");

    public CryptidInspector(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{G}");
        this.subtype.add(SubType.ELF);
        this.subtype.add(SubType.WARRIOR);

        this.power = new MageInt(2);
        this.toughness = new MageInt(3);

        //Vigilance
        this.addAbility(VigilanceAbility.getInstance());

        //Whenever a face-down permanent you control enters and whenever Cryptid Inspector or another permanent
        // you control is turned face up, put a +1/+1 counter on Cryptid Inspector.
        this.addAbility(new OrTriggeredAbility(Zone.BATTLEFIELD,
                new AddCountersSourceEffect(CounterType.P1P1.createInstance(1)),
                new EntersBattlefieldAllTriggeredAbility(null, filter),
                new TurnedFaceUpAllTriggeredAbility(null, filter2)));
    }

    private CryptidInspector(final CryptidInspector card){
        super(card);
    }

    @Override
    public CryptidInspector copy() {
        return new CryptidInspector(this);
    }
}
