package mage.cards.f;

import java.util.UUID;

import mage.abilities.effects.common.CreateTokenControllerTargetEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.game.permanent.token.FoodToken;
import mage.target.common.TargetArtifactPermanent;

public final class FeedingBoulder extends CardImpl{
	
	public FeedingBoulder(UUID ownerId, CardSetInfo setInfo) {
		super(ownerId, setInfo, new CardType[] {CardType.INSTANT}, "{C}");
		
		//Target artifact's controller creates two food tokens
		this.getSpellAbility().addEffect(new CreateTokenControllerTargetEffect(new FoodToken(), 2, false));
		this.getSpellAbility().addTarget(new TargetArtifactPermanent());
	}
	
	private FeedingBoulder(final FeedingBoulder card) {
		super(card);
	}

	public Card copy() {
		return new FeedingBoulder(this);
	}

}
