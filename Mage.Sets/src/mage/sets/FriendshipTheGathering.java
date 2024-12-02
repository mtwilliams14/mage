
package mage.sets;

import mage.cards.CardGraphicInfo;
import mage.cards.ExpansionSet;
import mage.cards.FrameStyle;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 *
 * @author Styxo
 */
public final class FriendshipTheGathering extends ExpansionSet {

    private static final FriendshipTheGathering instance = new FriendshipTheGathering();

    public static FriendshipTheGathering getInstance() {
        return instance;
    }

    private FriendshipTheGathering() {
        super("Friendship the Gathering", "MLP", ExpansionSet.buildDate(2024, 11, 1), SetType.CUSTOM_SET);
        this.blockName = "My Little Pony";
        this.hasBoosters = true;
        this.hasBasicLands = false;
        this.numBoosterLands = 1;
        this.numBoosterCommon = 10;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.numBoosterDoubleFaced = 1;
        this.ratioBoosterMythic = 8;
        this.maxCardNumberInBooster = 271;

        cards.add(new SetCardInfo("Feeding Boulder", 2, Rarity.UNCOMMON, mage.cards.f.FeedingBoulder.class));
    }
}
