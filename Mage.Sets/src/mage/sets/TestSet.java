package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

public final class TestSet extends ExpansionSet {

    private static final TestSet instance = new TestSet();

    public static TestSet getInstance(){
        return instance;
    }

    private TestSet(){
        super("Test Set", "TST", ExpansionSet.buildDate(2024, 1, 1), SetType.CUSTOM_SET);
        this.blockName = "Test";
        this.hasBoosters = false;
        this.hasBasicLands = false;
        cards.add(new SetCardInfo("Kaima's Kin", 1, Rarity.RARE, mage.cards.k.KaimasKin.class));
    }
}
