package mage.abilities.effects.common;

import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.costs.Cost;
import mage.abilities.costs.mana.ManaCost;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.util.ManaUtil;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA. User: Loki Date: 21.12.10 Time: 9:21
 */
public class SacrificeSourceUnlessPaysEffect extends OneShotEffect {

    protected Cost cost;
    protected DynamicValue genericMana;

    public SacrificeSourceUnlessPaysEffect(Cost cost) {
        super(Outcome.Sacrifice);
        this.cost = cost;
    }

    public SacrificeSourceUnlessPaysEffect(DynamicValue genericMana) {
        super(Outcome.Detriment);
        this.genericMana = genericMana;
    }

    public SacrificeSourceUnlessPaysEffect(final SacrificeSourceUnlessPaysEffect effect) {
        super(effect);
        if (effect.cost != null) {
            this.cost = effect.cost.copy();
        }
        if (effect.genericMana != null) {
            this.genericMana = effect.genericMana.copy();
        }
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        Permanent sourcePermanent = game.getPermanentOrLKIBattlefield(source.getSourceId());
        if (player != null && sourcePermanent != null) {
            Cost costToPay;
            String costValueMessage;
            if (cost != null) {
                costToPay = cost.copy();
                costValueMessage = costToPay.getText();
            } else {
                costToPay = ManaUtil.createManaCost(genericMana, game, source, this);
                costValueMessage = "{" + genericMana.calculate(game, source, this) + "}";
            }
            String message;
            if (costToPay instanceof ManaCost) {
                message = "Would you like to pay " + costValueMessage + " to prevent sacrifice effect?";
            } else {
                message = costValueMessage + " to prevent sacrifice effect?";
            }

            costToPay.clearPaid();
            if (costToPay.canPay(source, source, source.getControllerId(), game)
                    && player.chooseUse(Outcome.Benefit, message, source, game)
                    && costToPay.pay(source, game, source, source.getControllerId(), false, null)) {
                game.informPlayers(player.getLogName() + " chooses to pay " + costValueMessage + " to prevent sacrifice effect");
                return true;
            }

            game.informPlayers(player.getLogName() + " chooses not to pay " + costValueMessage + " to prevent sacrifice effect");
            if (source.getSourceObjectZoneChangeCounter() == game.getState().getZoneChangeCounter(source.getSourceId())
                    && game.getState().getZone(source.getSourceId()) == Zone.BATTLEFIELD) {
                sourcePermanent.sacrifice(source, game);
            }
            return true;
        }
        return false;
    }

    @Override
    public SacrificeSourceUnlessPaysEffect copy() {
        return new SacrificeSourceUnlessPaysEffect(this);
    }

    @Override
    public String getText(Mode mode) {
        if (staticText != null && !staticText.isEmpty()) {
            return staticText;
        }

        StringBuilder sb = new StringBuilder("sacrifice {this} unless you ");
        String costText = cost != null ? cost.getText() : "{X}";

        if (costText.toLowerCase(Locale.ENGLISH).startsWith("discard")
                || costText.toLowerCase(Locale.ENGLISH).startsWith("remove")
                || costText.toLowerCase(Locale.ENGLISH).startsWith("return")
                || costText.toLowerCase(Locale.ENGLISH).startsWith("put")
                || costText.toLowerCase(Locale.ENGLISH).startsWith("exile")
                || costText.toLowerCase(Locale.ENGLISH).startsWith("sacrifice")) {
            sb.append(costText.substring(0, 1).toLowerCase(Locale.ENGLISH));
            sb.append(costText.substring(1));
        } else {
            sb.append("pay ").append(costText);
        }

        return sb.toString();
    }
}
