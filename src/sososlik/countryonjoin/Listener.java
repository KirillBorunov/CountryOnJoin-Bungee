package sososlik.countryonjoin;

import java.net.InetAddress;
import java.text.MessageFormat;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryResponse;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

public class Listener implements net.md_5.bungee.api.plugin.Listener
{
	private static Listener instance;
	private MessageFormat withCountryformatter;
	private MessageFormat withoutCountryformatter;
		
	public static Listener getInstance()
	{
		return instance;
	}
			
	public Listener() 
	{
		instance = this;
	}

	private MessageFormat getWithCountryFormatter() 
	{
		if(this.withCountryformatter == null)
		{
			this.withCountryformatter = new MessageFormat(Plugin.getInstance().getConfig().getJoinWithCountryMessage());
		}
		return this.withCountryformatter;
	}
	
	private MessageFormat getWithoutCountryFormatter() 
	{
		if(this.withoutCountryformatter == null)
		{
			this.withoutCountryformatter = new MessageFormat(Plugin.getInstance().getConfig().getJoinWithoutCountryMessage());
		}
		return this.withoutCountryformatter;
	}
	
	public void reload()
	{
		this.withCountryformatter = null;
		this.withoutCountryformatter = null;
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {

		InetAddress playerAddress = event.getPlayer().getAddress().getAddress();
		
		String playerCountryKey = Plugin.UNKNOWN_COUNTRY_KEY;
		String playerCountryDisplayName;
		
		
		if(Plugin.getInstance().getConfig().getDebug())
		{
			Plugin.getInstance().getLogger().info("Player " + event.getPlayer().getDisplayName() + " joined with address " + playerAddress.toString());
		}	
		
		
		//player with the "hide" permission do not reveal the country
		if(event.getPlayer().hasPermission(Plugin.PERMISSIONS_BASE + ".hide"))
		{
			if(Plugin.getInstance().getConfig().getDebug())
			{
				Plugin.getInstance().getLogger().info("Hiding the country for player " + event.getPlayer().getDisplayName() + " with address " + playerAddress.toString());
			}	
		}
		else
		{
			try
			{
				CountryResponse res = Plugin.getInstance().getGeoIPDBReader().country(playerAddress);
				
				if(res != null)
				{
					playerCountryKey = res.getCountry().getIsoCode();
				}
			}
			catch (AddressNotFoundException e)
			{
				if(Plugin.getInstance().getConfig().getDebug())
				{
					Plugin.getInstance().getLogger().warning("Country not found for player " + event.getPlayer().getDisplayName() + " joined with address " + playerAddress.toString());
				}	
			}
			catch (Exception e)
			{
				Plugin.getInstance().getLogger().severe("Error on country lookup for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
				e.printStackTrace();
			}
		}

		
		if(playerCountryKey.equals(Plugin.UNKNOWN_COUNTRY_KEY) && !Plugin.getInstance().getConfig().getBroadcastOnUnknownCountry())
		{
			return;
		}
		
		
		playerCountryDisplayName = Plugin.getInstance().getConfig().getCountryNames().get(playerCountryKey);
		
		if(playerCountryDisplayName == null)
		{
			playerCountryDisplayName = playerCountryKey;
			Plugin.getInstance().getLogger().warning("Country name not defined for \"" + playerCountryKey + "\" key.");
		}
		
		
		BaseComponent[] message = null;
		
		try
		{
			String resultText;
			if(playerCountryKey.equals(Plugin.UNKNOWN_COUNTRY_KEY) && Plugin.getInstance().getConfig().getBroadcastAltJoinMsgOnUnknownCountry())
			{
				resultText = this.getWithoutCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName()});
			}
			else
			{
				resultText = this.getWithCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName(), playerCountryDisplayName});
			}
			message = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', resultText));
		}
		catch (Exception e)
		{
			Plugin.getInstance().getLogger().severe("Error creating a join message for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
			e.printStackTrace();
		}
		
		
		if (message == null || message.length == 0)
		{
			Plugin.getInstance().getLogger().warning("The resulting join message is empty for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
		}
		else
		{
			ProxyServer.getInstance().broadcast(message);
		}
		
	}
	
}
