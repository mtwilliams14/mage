package mage.cards.m;

import mage.MageIdentifier;
import mage.MageObjectReference;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.Cost;
import mage.abilities.costs.Costs;
import mage.abilities.costs.CostsImpl;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.effects.AsThoughEffectImpl;
import mage.abilities.effects.ReplacementEffectImpl;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.game.stack.Spell;
import mage.players.Player;
import mage.watchers.Watcher;

import java.util.*;

/**
 * @author TheElk801
 */
public final class MaestrosAscendancy extends CardImpl {

    public MaestrosAscendancy(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{U}{B}{R}");

        // Once during each of your turns, you may cast an instant or sorcery spell from your graveyard by sacrificing a creature in addition to paying its other costs. If a spell cast this way would be put into your graveyard, exile it instead.
        Ability ability = new SimpleStaticAbility(new MaestrosAscendancyCastEffect())
                .setIdentifier(MageIdentifier.MaestrosAscendencyAlternateCast);
        ability.addEffect(new MaestrosAscendancyExileEffect());
        this.addAbility(ability, new MaestrosAscendancyWatcher());
    }

    private MaestrosAscendancy(final MaestrosAscendancy card) {
        super(card);
    }

    @Override
    public MaestrosAscendancy copy() {
        return new MaestrosAscendancy(this);
    }
}

class MaestrosAscendancyCastEffect extends AsThoughEffectImpl {

    MaestrosAscendancyCastEffect() {
        super(AsThoughEffectType.CAST_FROM_NOT_OWN_HAND_ZONE, Duration.WhileOnBattlefield, Outcome.AIDontUseIt);
        staticText = "once during each of your turns, you may cast an instant or sorcery spell " +
                "from your graveyard by sacrificing a creature in addition to paying its other costs.";
    }

    private MaestrosAscendancyCastEffect(final MaestrosAscendancyCastEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public MaestrosAscendancyCastEffect copy() {
        return new MaestrosAscendancyCastEffect(this);
    }

    @Override
    public boolean applies(UUID objectId, Ability source, UUID affectedControllerId, Game game) {
        if (!source.isControlledBy(affectedControllerId)
                || !game.isActivePlayer(affectedControllerId)) {
            return false;
        }
        Card card = game.getCard(objectId);
        Player player = game.getPlayer(affectedControllerId);
        if (card == null
                || player == null
                || !card.isOwnedBy(affectedControllerId)
                || !card.isInstantOrSorcery(game)
                || !game.getState().getZone(objectId).match(Zone.GRAVEYARD)
                || MaestrosAscendancyWatcher.checkPlayer(source, game)) {
            return false;
        }
        Costs<Cost> newCosts = new CostsImpl<>();
        newCosts.addAll(card.getSpellAbility().getCosts());
        newCosts.add(new SacrificeTargetCost(StaticFilters.FILTER_PERMANENT_CREATURE));
        player.setCastSourceIdWithAlternateMana(
                card.getId(), card.getManaCost(), newCosts,
                MageIdentifier.MaestrosAscendencyAlternateCast
        );
        return true;
    }
}

class MaestrosAscendancyExileEffect extends ReplacementEffectImpl {

    MaestrosAscendancyExileEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Exile);
        staticText = "If a spell cast this way would be put into your graveyard, exile it instead";
    }

    private MaestrosAscendancyExileEffect(final MaestrosAscendancyExileEffect effect) {
        super(effect);
    }

    @Override
    public MaestrosAscendancyExileEffect copy() {
        return new MaestrosAscendancyExileEffect(this);
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        ((ZoneChangeEvent) event).setToZone(Zone.EXILED);
        return false;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.ZONE_CHANGE;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        ZoneChangeEvent zEvent = (ZoneChangeEvent) event;
        Spell spell = game.getSpellOrLKIStack(zEvent.getTargetId());
        return zEvent.getToZone() == Zone.GRAVEYARD
                && spell != null
                && MaestrosAscendancyWatcher.checkSpell(spell, source, game);
    }
}

class MaestrosAscendancyWatcher extends Watcher {

    private final Map<MageObjectReference, Set<UUID>> playerMap = new HashMap<>();
    private final Map<MageObjectReference, Set<MageObjectReference>> spellMap = new HashMap<>();

    MaestrosAscendancyWatcher() {
        super(WatcherScope.GAME);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.SPELL_CAST
                && event.hasApprovingIdentifier(MageIdentifier.MaestrosAscendencyAlternateCast)
                && event.getApprovingObject() != null) {
            playerMap.computeIfAbsent(
                    event.getApprovingObject()
                            .getApprovingMageObjectReference(),
                    x -> new HashSet<>()
            ).add(event.getPlayerId());
            spellMap.computeIfAbsent(
                    event.getApprovingObject()
                            .getApprovingMageObjectReference(),
                    x -> new HashSet<>()
            ).add(new MageObjectReference(event.getTargetId(), game));
        }
    }

    @Override
    public void reset() {
        super.reset();
        playerMap.clear();
        spellMap.clear();
    }

    static boolean checkPlayer(Ability source, Game game) {
        return game
                .getState()
                .getWatcher(MaestrosAscendancyWatcher.class)
                .playerMap
                .getOrDefault(new MageObjectReference(source.getSourcePermanentIfItStillExists(game), game), Collections.emptySet())
                .contains(source.getControllerId());
    }

    static boolean checkSpell(Spell spell, Ability source, Game game) {
        return game
                .getState()
                .getWatcher(MaestrosAscendancyWatcher.class)
                .spellMap
                .getOrDefault(new MageObjectReference(source.getSourcePermanentOrLKI(game), game), Collections.emptySet())
                .contains(new MageObjectReference(spell, game));
    }
}
