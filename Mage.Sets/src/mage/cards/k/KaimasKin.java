package mage.cards.k;

import java.util.UUID;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldOrTurnedFaceUpTriggeredAbility;
import mage.abilities.common.TurnedFaceUpSourceTriggeredAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.MorphAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.permanent.ModifiedPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;

public final class KaimasKin extends CardImpl {

	public KaimasKin(UUID ownerId, CardSetInfo setInfo) {
		super(ownerId, setInfo, new CardType[] {CardType.CREATURE}, "{5}{R}{R}");
		this.subtype.add(SubType.BOAR);
		this.subtype.add(SubType.SPIRIT);
		this.power = new MageInt(4);
		this.toughness = new MageInt(3);
		
		// Megamorph {R}{R}{R}{R}
		this.addAbility(new MorphAbility(this, new ManaCostsImpl<>("{R}{R}{R}{R}"), true));
		
		// When Kaima's Kin is turned face up, each modified creature you control deals damage equal to its power to each opponent.
		this.addAbility(new EntersBattlefieldOrTurnedFaceUpTriggeredAbility(new KaimasKinEffect()));
		
	}

	private KaimasKin(final KaimasKin card) {
		super(card);
	}
	
	public Card copy() {
		// TODO Auto-generated method stub
		return new KaimasKin(this);
	}

}

class KaimasKinEffect extends OneShotEffect {
	
	private static final FilterControlledCreaturePermanent filter =
            new FilterControlledCreaturePermanent("modified creature you control");

    static {
        filter.add(ModifiedPredicate.instance);
    }
    
    KaimasKinEffect(){
    	super(Outcome.Damage);
    	this.staticText = "each modified creature you control deals damage equal to its power to each opponent.";
    }
    
    private KaimasKinEffect(final KaimasKinEffect effect) {
    	super(effect);
    }
    
    @Override
    public KaimasKinEffect copy() {
    	return new KaimasKinEffect(this);
    }

	@Override
	public boolean apply(Game game, Ability source) {
		
		Player controller = game.getPlayer(source.getControllerId());
		
		if(controller == null) {
			return false;
		}
		
		boolean result = false;
		
		for(UUID opponentId: game.getOpponents(source.getControllerId())) {
			Player opponent = game.getPlayer(opponentId);
			
			if(opponent != null) {
				for(Permanent permanent: game.getBattlefield().getActivePermanents(filter, controller.getId(), game)){
					result |= 0 < opponent.damage(permanent.getPower().getValue(), permanent.getId(), source, game);
				}
			}
			
		}
		return result;
	}

}

