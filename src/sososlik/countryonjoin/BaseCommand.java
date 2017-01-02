package sososlik.countryonjoin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class BaseCommand extends Command
{
	
	
	public BaseCommand() 
	{
		super(Plugin.COMMANDS_BASE);
	}

	@Override
	public void execute(CommandSender sender, String[] args) 
	{
		//TODO: split this to sub-commands classes
		//TODO: print help and/or available commands
		//TODO: maybe the messages should come from the messages file?
		//TODO: display a header or about box
		//TODO: a command for view/modify the configuration
		
		if(args.length == 0)
		{
			this.replyError(sender, "You must specify a sub-command!");
		}
		else
		{
			switch(args[0].toLowerCase())
			{
			case "reload":
				
				if(sender.hasPermission(Plugin.PERMISSIONS_BASE + ".command.reload"))
				{
					if(args.length > 1)
					{
						this.replyError(sender, "Too many arguments!");
					}
									
					this.replyInfo(sender, "Reloading the configuration...");
					
					Plugin.getInstance().reload();
					Listener.getInstance().reload();
					
					this.replyInfo(sender, "Configuration reloaded.");
				}
				else
				{
					this.replyError(sender, "You don't have permission to use that command!");
				}
				
				break;
			default:
				this.replyError(sender, "Unknown sub-command!");
			}
		}
	}
	
	private void replyError(CommandSender sender, String text)
	{
		sender.sendMessage(new ComponentBuilder(text).color(ChatColor.RED).create());
	}
	
	private void replyInfo(CommandSender sender, String text)
	{
		sender.sendMessage(new ComponentBuilder(text).create());
	}
	
}
