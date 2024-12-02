package mage.cards.a;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.ActivateAsSorceryActivatedAbility;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.EntersBattlefieldWithXCountersEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.choices.Choice;
import mage.choices.ChoiceImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.counters.CounterType;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetCreaturePermanent;

public final class ArmyReserves extends CardImpl {

    public ArmyReserves(UUID ownerId, CardSetInfo setInfo){
        super(ownerId, setInfo, new CardType[] {CardType.CREATURE}, "{X}{W}{W}");

        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SOLDIER);
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Army Reserves enters with X +1/+1 counters and a defender counter on it.
        Ability counterETBAbility = new EntersBattlefieldAbility(new EntersBattlefieldWithXCountersEffect(CounterType.P1P1.createInstance()));
        counterETBAbility.addEffect(new AddCountersSourceEffect(CounterType.DEFENDER.createInstance(1)).setText("and a defender counter on it."));
        this.addAbility(counterETBAbility);

        // {1}: Move a counter from Army reserves onto another target creature. Activate only as a sorcery.
        ActivateAsSorceryActivatedAbility abilityMoveCounters = new ActivateAsSorceryActivatedAbility(new ArmyReservesMoveCounterEffect(), new ManaCostsImpl<>("{1}"));
        abilityMoveCounters.addTarget(new TargetCreaturePermanent(StaticFilters.FILTER_ANOTHER_TARGET_CREATURE));
        this.addAbility(abilityMoveCounters);
    }

    private ArmyReserves(final ArmyReserves card){
        super(card);
    }

    public ArmyReserves copy(){
        return new ArmyReserves(this);
    }
}

class ArmyReservesMoveCounterEffect extends OneShotEffect {

    ArmyReservesMoveCounterEffect(){
        super(Outcome.Neutral);
        staticText = "Move a counter from Army Reserves onto another target creature. Activate only as a sorcery";
    }

    private ArmyReservesMoveCounterEffect(final ArmyReservesMoveCounterEffect effect){
        super(effect);
    }

    @Override
    public ArmyReservesMoveCounterEffect copy() {
        return new ArmyReservesMoveCounterEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Permanent toPermanent = game.getPermanent(source.getFirstTarget());
        Permanent fromPermanent = game.getPermanent(source.getSourceId());

        if(fromPermanent == null
            || toPermanent == null
            || fromPermanent.getCounters(game).isEmpty()) {
            return false;
        }

        Set<String> possibleCounterNames = new LinkedHashSet<>(fromPermanent.getCounters(game).keySet());
        if(possibleCounterNames.isEmpty()){
            return false;
        }

        Choice moveCounterChoice = new ChoiceImpl(true);
        moveCounterChoice.setMessage("Choose counter to move");
        moveCounterChoice.setChoices(possibleCounterNames);

        if(controller.choose(outcome, moveCounterChoice, game) && possibleCounterNames.contains(moveCounterChoice.getChoice())){
            String counterName = moveCounterChoice.getChoice();
            CounterType counterType = CounterType.findByName(counterName);
            if(counterName == null){
                return false;
            }
            fromPermanent.removeCounters(counterType.getName(), 1, source, game);
            toPermanent.addCounters(counterType.createInstance(), source, game);
        }

        return true;
    }
}