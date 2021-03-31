package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;

abstract public class CoinSalary {
	public static void coinpayPlayersTimer() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				for (CustomPlayer player : CustomPlayer.getOnlinePlayers()) {
					Rank rank = player.getRank();
					
					if (rank == null) return;
					
					int salery = rank.getSalery(Currency.COINS);
					if (salery < 1) return;
					
					CurrencyClass.genPay(player, salery, Currency.COINS);
				}
			}
		}, 48000, 48000);
	}
}
