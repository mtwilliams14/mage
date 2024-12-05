package mage.cards.c;

import mage.abilities.Mode;
import mage.abilities.effects.common.DestroyAllEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.ColorlessPredicate;
import mage.filter.predicate.mageobject.MonocoloredPredicate;
import mage.filter.predicate.mageobject.MulticoloredPredicate;

import java.util.UUID;

public final class ChromaticRift extends CardImpl {

    private static final FilterCreaturePermanent filter
            = new FilterCreaturePermanent("multicolored creatures");

    private static final FilterCreaturePermanent filter2
            = new FilterCreaturePermanent("monocolored creatures");

    private static final FilterCreaturePermanent filter3
            = new FilterCreaturePermanent("colorless creatures");

    static{
        filter.add(MulticoloredPredicate.instance);
        filter2.add(MonocoloredPredicate.instance);
        filter3.add(ColorlessPredicate.instance);
    }

    public ChromaticRift(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{3}{W}{W}");

        // Choose any number -
        this.getSpellAbility().getModes().setMinModes(0);
        this.getSpellAbility().getModes().setMaxModes(3);

        // Destroy all multicolored creatures
        this.getSpellAbility().addEffect(new DestroyAllEffect(filter));

        // Destroy all monocolored creatures
        Mode mode = new Mode(new DestroyAllEffect(filter2));
        this.getSpellAbility().addMode(mode);

        // Destroy all colorless creatures
        Mode mode2 = new Mode(new DestroyAllEffect(filter3));
        this.getSpellAbility().addMode(mode2);

    }

    private ChromaticRift(final ChromaticRift card){
        super(card);
    }

    public ChromaticRift copy(){
        return new ChromaticRift(this);
    }
}
