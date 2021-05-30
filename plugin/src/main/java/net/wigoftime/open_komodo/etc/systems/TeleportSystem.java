package net.wigoftime.open_komodo.etc.systems;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.TpRequest;
import org.bukkit.Sound;

public class TeleportSystem {
    private static final String noRequests = String.format("%s» %sYou don't have any tpa requests", ChatColor.YELLOW, ChatColor.GRAY);
    private static final String sentRequest = String.format("%s» %sTpa request sent to $D", ChatColor.YELLOW, ChatColor.GRAY);
    private static final String acceptedRequester = String.format("%s» %s$N accepted your teleport request", ChatColor.YELLOW, ChatColor.GRAY);
    private static final String acceptedTarget = String.format("%s» %sYou accepted $D's teleport request", ChatColor.YELLOW, ChatColor.GRAY);
    private static final String deniedRequest = String.format("%s» %sYou denied $D's request", ChatColor.YELLOW, ChatColor.GRAY);
    public static final String tpaOff = String.format("%s» %sThey have disabled tpa requests", ChatColor.YELLOW, ChatColor.GRAY);

    private TpRequest tpRequest;

    final CustomPlayer playerCustom;

    public TeleportSystem(CustomPlayer playerCustom) {
        this.playerCustom = playerCustom;
    }

    public void tpaRequest(CustomPlayer requester, TpRequest.tpType type)
    {
        if (!playerCustom.getSettings().isTpaEnabled()) {
            requester.getPlayer().sendMessage(tpaOff);
            return;
        }

        tpRequest = new TpRequest(playerCustom.getPlayer(), requester.getPlayer(), type);

        String requesterMessage = MessageFormat.format(sentRequest, requester.getPlayer(), playerCustom.getPlayer(), null);
        requester.getPlayer().sendMessage(requesterMessage);
        BaseComponent[] requestedTpaMsg = new BaseComponent[3];

        if (type == TpRequest.tpType.TPA)
            requestedTpaMsg[0] = new TextComponent(String.format("%s» %s%s%s Would like to teleport to you\n    ", ChatColor.YELLOW, ChatColor.GRAY, requester.getPlayer().getDisplayName(), ChatColor.GRAY));
        else
            requestedTpaMsg[0] = new TextComponent(String.format("%s» %s%s%s Would like to teleport to them\n    ", ChatColor.YELLOW, ChatColor.GRAY, requester.getPlayer().getDisplayName(), ChatColor.GRAY));

        requestedTpaMsg[1] = new TextComponent(String.format("%s» Accept", ChatColor.DARK_GREEN));
        requestedTpaMsg[1].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));

        BaseComponent[] showTextAccept = {new TextComponent("Accept teleportation request")};
        requestedTpaMsg[1].setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, showTextAccept));

        BaseComponent[] showTextDeny = {new TextComponent("Deny teleportation request")};
        requestedTpaMsg[2] = new TextComponent(String.format("\n    %s» Deny", ChatColor.DARK_RED));
        requestedTpaMsg[2].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"));
        requestedTpaMsg[2].setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, showTextDeny));

        playerCustom.getPlayer().spigot().sendMessage(requestedTpaMsg);
        playerCustom.getPlayer().playSound(playerCustom.getPlayer().getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1f, 1f);
    }

    public void tpaAccept()
    {
        if (tpRequest == null) {
            playerCustom.getPlayer().sendMessage(noRequests);
            return;
        }

        if (tpRequest.getType() == TpRequest.tpType.TPA)
            tpRequest.getRequester().teleport(tpRequest.getTarget());
        else if (tpRequest.getType() == TpRequest.tpType.TPAHERE)
            tpRequest.getTarget().teleport(tpRequest.getRequester());

        String requesterMessage = MessageFormat.format(acceptedRequester, playerCustom.getPlayer(), tpRequest.getRequester(), null);
        String targetMessage = MessageFormat.format(acceptedTarget, playerCustom.getPlayer(), tpRequest.getRequester(), null);

        tpRequest.getRequester().sendMessage(requesterMessage);
        tpRequest.getTarget().sendMessage(targetMessage);
        tpRequest.getTarget().playSound(tpRequest.getTarget().getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 1f, 1f);

        tpRequest = null;
    }

    public void tpaDeny()
    {
        if (tpRequest == null) {
            playerCustom.getPlayer().sendMessage(noRequests);
            return;
        }

        tpRequest = null;
        playerCustom.getPlayer().sendMessage(deniedRequest);
        return;
    }
}
