package mage.cards.k;

import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.LoyaltyAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.CompoundCondition;
import mage.abilities.condition.Condition;
import mage.abilities.condition.common.MyTurnCondition;
import mage.abilities.condition.common.SourceHasCounterCondition;
import mage.abilities.decorator.ConditionalContinuousEffect;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.GetEmblemEffect;
import mage.abilities.effects.common.continuous.BecomesCreatureSourceEffect;
import mage.abilities.effects.keyword.SurveilEffect;
import mage.abilities.hint.common.MyTurnHint;
import mage.abilities.keyword.HexproofAbility;
import mage.abilities.keyword.NinjutsuAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.command.emblems.KaitoBaneOfNightmaresEmblem;
import mage.game.permanent.token.TokenImpl;
import mage.players.Player;
import mage.target.common.TargetCardInGraveyard;
import mage.util.CardUtil;
import mage.watchers.common.PlayerLostLifeWatcher;

import java.util.UUID;

public class KaitoBaneOfNightmares extends CardImpl {

    private static final Condition condition = new SourceHasCounterCondition(CounterType.LOYALTY);

    public KaitoBaneOfNightmares(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[]{CardType.PLANESWALKER}, "{2}{U}{B}");

        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.KAITO);
        this.setStartingLoyalty(4);

        // Ninjutsu {1}{U}{B}
        this.addAbility(new NinjutsuAbility("{1}{U}{B}"));

        this.addAbility(new SimpleStaticAbility(new ConditionalContinuousEffect(
                new BecomesCreatureSourceEffect(
                        new KaitoBaneOfNightmaresToken(), null, Duration.WhileOnBattlefield
                ), new CompoundCondition(MyTurnCondition.instance, condition), "During your turn, " +
                "{this} is a 3/4 Ninja creature with hexproof"
        )).addHint(MyTurnHint.instance));

        this.addAbility(new LoyaltyAbility(new GetEmblemEffect(new KaitoBaneOfNightmaresEmblem()), 1));

        Ability ability = new LoyaltyAbility(new SurveilEffect(2, false), 0);
        ability.addEffect(new KayaSpiritsJusticeExileEffect().concatBy(". Then"));
    }

    private KaitoBaneOfNightmares(final KaitoBaneOfNightmares card){
        super(card);
    }

    public KaitoBaneOfNightmares copy(){
        return new KaitoBaneOfNightmares(this);
    }
}

class KaitoBaneOfNightmaresToken extends TokenImpl {

    KaitoBaneOfNightmaresToken() {
        super("", "3/4 Ninja creature");
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.NINJA);
        power = new MageInt(3);
        toughness = new MageInt(4);
        this.addAbility(HexproofAbility.getInstance());
    }

    private KaitoBaneOfNightmaresToken(final KaitoBaneOfNightmaresToken token) {
        super(token);
    }

    @Override
    public KaitoBaneOfNightmaresToken copy() {
        return new KaitoBaneOfNightmaresToken(this);
    }
}

enum KaitoBaneOfNightmaresDynamicValue implements DynamicValue {
    instance;

    @Override
    public int calculate(Game game, Ability sourceAbility, Effect effect) {
        Player controller = game.getPlayer(sourceAbility.getControllerId());
        if (controller == null) {
            return 0;
        }

        PlayerLostLifeWatcher watcher = game.getState().getWatcher(PlayerLostLifeWatcher.class);
        int numOpponentsWhoLostLife = 0;

        if (watcher != null) {
            for (UUID playerId : game.getState().getPlayersInRange(controller.getId(), game)) {
                if (controller.hasOpponent(playerId, game) && watcher.getLifeLost(playerId) > 0) {
                    numOpponentsWhoLostLife++;
                }
            }
        }

        return numOpponentsWhoLostLife;
    }

    @Override
    public DynamicValue copy() {
        return instance;
    }

    @Override
    public String getMessage() {
        return "opponents who lost life this turn";
    }

    @Override
    public String toString() {
        return "1";
    }
}

class KaitoBaneOfNightmaresDrawEffect extends OneShotEffect {

    KaitoBaneOfNightmaresDrawEffect() {
        super(Outcome.Exile);
        staticText = "exile a card from a graveyard";
    }

    private KaitoBaneOfNightmaresDrawEffect(final KaitoBaneOfNightmaresDrawEffect effect) {
        super(effect);
    }

    @Override
    public KaitoBaneOfNightmaresDrawEffect copy() {
        return new KaitoBaneOfNightmaresDrawEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            TargetCardInGraveyard target = new TargetCardInGraveyard();
            target.withNotTarget(true);
            controller.choose(outcome, target, source, game);
            Card card = game.getCard(target.getFirstTarget());
            if (card != null) {
                UUID exileId = CardUtil.getExileZoneId(game, source.getSourceId(), source.getSourceObjectZoneChangeCounter());
                MageObject sourceObject = source.getSourceObject(game);
                String exileName = sourceObject == null ? null : sourceObject.getIdName();
                return controller.moveCardsToExile(card, source, game, true, exileId, exileName);
            }
        }
        return false;
    }
}