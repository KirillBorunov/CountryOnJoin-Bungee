package sososlik.countryonjoin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Plugin extends net.md_5.bungee.api.plugin.Plugin
{
	public static final String GEOIP_DB_FILENAME = "GeoLite2-Country.mmdb";
	public static final String README_FILENAME = "README.txt";
	public static final String CONFIG_FILENAME = "config.yml";
	public static final String MESSAGES_FILENAME_PATTERN = "messages\\..\\.yml";
	public static final String MESSAGES_BASEDIR = "messages";
	public static final String MESSAGES_FILENAME_FORMAT = "messages.%s.yml";
	public static final String COUNTRYNAMES_FILENAME_PATTERN = "countrynames\\..\\.yml";
	public static final String COUNTRYNAMES_BASEDIR = "countrynames";
	public static final String COUNTRYNAMES_FILENAME_FORMAT = "countrynames.%s.yml";
	public static final String PERMISSIONS_BASE = "countryonjoin";
	public static final String UNKNOWN_COUNTRY_KEY = "unknown";
	public static final String COMMANDS_BASE = "countryonjoin";
	
	private static Plugin instance;
	private DatabaseReader geoipdbreader;
	private Config config;
	
	
	public Plugin()
	{
		instance = this;
	}
	
	@Override
	public void onEnable() 
	{
		
		File dataDir = this.getDataFolder();
		
		if (!dataDir.exists())
		{
			try
			{
				dataDir.mkdir();
			}
			catch(Exception e)
			{
				this.getLogger().severe("Error creating the plugin data directory \"" + dataDir.getAbsolutePath() + "\".");
				e.printStackTrace();
			}
		}
		
		
		//TODO: implement config file VERSIONS for future changes
		
		File configFile = new File(dataDir, CONFIG_FILENAME);
				
		if(!configFile.exists())
		{
			try(InputStream rs = this.getResourceAsStream(CONFIG_FILENAME))
			{
				Files.copy(rs, configFile.toPath());
			} catch (Exception e)
			{
				this.getLogger().severe("Error extracting the file \"" + CONFIG_FILENAME +"\" to \"" + configFile.getAbsolutePath() + "\".");
				e.printStackTrace();
			}
		}
		
		
		//TODO: implement VERSIONS for the messages and countrynames files and update then if in the resource is newer
		//TODO: maybe check for "CUSTOM" version for not overwriting the users changes
		
		File messagesBaseDir = new File(dataDir, MESSAGES_BASEDIR);
		
		if(!messagesBaseDir.exists())
		{
			try
			{
				messagesBaseDir.mkdir();
			}
			catch (Exception e)
			{
				this.getLogger().severe("Error creating " + messagesBaseDir.getAbsolutePath() + " directory.");
				e.printStackTrace();
			}
		}
		
		try(FileSystem fs = FileSystems.newFileSystem(Plugin.class.getResource("/" + MESSAGES_BASEDIR).toURI(), Collections.<String, Object>emptyMap()))
		{
	        Iterator<Path> it = Files.walk(fs.getPath("/" + MESSAGES_BASEDIR), 1).iterator();

	        while(it.hasNext())
	        {
	        	Path p = it.next();
	        	
	        	if(p.toString().equals("/" + MESSAGES_BASEDIR))
	        	{
	        		continue; //The first entry appears to be always the directory which we are enumerating...
	        	}
	        	
	        	File f = new File(messagesBaseDir, p.getFileName().toString());
	        	
	        	if(!f.exists())
	        	{
	        		try (InputStream rs = this.getResourceAsStream(MESSAGES_BASEDIR + "/" + f.getName()))
	        		{
	        			Files.copy(rs, f.toPath());
	        		}
	        		catch (Exception e)
	        		{
	        			this.getLogger().severe("Error extracting the file \"" + MESSAGES_BASEDIR + "/" + f.getName() + "\" to \"" + f.getAbsolutePath() + "\".");
	    				e.printStackTrace();
	        		}
	        	}
	        }
		}
		catch (Exception e)
		{
			this.getLogger().severe("Error extracting the directory \"" + MESSAGES_BASEDIR + "\" to \"" + messagesBaseDir.getAbsolutePath() + "\".");
			e.printStackTrace();
		}
		
		
		File countrynamesBaseDir = new File(dataDir, COUNTRYNAMES_BASEDIR);
		
		if(!countrynamesBaseDir.exists())
		{
			try
			{
				countrynamesBaseDir.mkdir();
			}
			catch (Exception e)
			{
				this.getLogger().severe("Error creating " + countrynamesBaseDir.getAbsolutePath() + " directory.");
				e.printStackTrace();
			}
		}
		
		try(FileSystem fs = FileSystems.newFileSystem(Plugin.class.getResource("/" + COUNTRYNAMES_BASEDIR).toURI(), Collections.<String, Object>emptyMap()))
		{
	        Iterator<Path> it = Files.walk(fs.getPath("/" + COUNTRYNAMES_BASEDIR), 1).iterator();

	        while(it.hasNext())
	        {
	        	Path p = it.next();
	        	
	        	if(p.toString().equals("/" + COUNTRYNAMES_BASEDIR))
	        	{
	        		continue; //The first entry appears to be always the directory which we are enumerating...
	        	}
	        	
	        	File f = new File(countrynamesBaseDir, p.getFileName().toString());
	        	
	        	if(!f.exists())
	        	{
	        		try (InputStream rs = this.getResourceAsStream(COUNTRYNAMES_BASEDIR + "/" + f.getName()))
	        		{
	        			Files.copy(rs, f.toPath());
	        		}
	        		catch (Exception e)
	        		{
	        			this.getLogger().severe("Error extracting the file \"" + COUNTRYNAMES_BASEDIR + "/" + f.getName() + "\" to \"" + f.getAbsolutePath() + "\".");
	    				e.printStackTrace();
	        		}
	        	}
	        }
		}
		catch (Exception e)
		{
			this.getLogger().severe("Error extracting the directory \"" + COUNTRYNAMES_BASEDIR + "\" to \"" + countrynamesBaseDir.getAbsolutePath() + "\".");
			e.printStackTrace();
		}
		
		
		File geoipdbFile = new File(dataDir, GEOIP_DB_FILENAME);
		
		if(!geoipdbFile.exists())
		{
			try(InputStream rs = this.getResourceAsStream(GEOIP_DB_FILENAME))
			{
				Files.copy(rs, geoipdbFile.toPath());
			} 
			catch (Exception e) 
			{
				this.getLogger().severe("Error extracting the file \"" + GEOIP_DB_FILENAME + "\" to \"" + geoipdbFile.getAbsolutePath() + "\".");
				e.printStackTrace();
			}
		}
		
		//TODO: update the GeoIP db file if in the resource is newer
		
		
		File readmeFile = new File(dataDir, README_FILENAME);
		
		if(!readmeFile.exists())
		{
			try(InputStream rs = this.getResourceAsStream(README_FILENAME))
			{
				Files.copy(rs, readmeFile.toPath());
			} 
			catch (Exception e) 
			{
				this.getLogger().severe("Error extracting the file \"" + README_FILENAME + "\" to \"" + readmeFile.getAbsolutePath() + "\".");
				e.printStackTrace();
			}
		}
		
		
		this.reload();

		
		this.getProxy().getPluginManager().registerListener(this, new Listener());
		this.getProxy().getPluginManager().registerCommand(this, new BaseCommand());
		
	}
	
	public void reload()
	{
				
		File dataDir = this.getDataFolder();
		
		
		File configFile = new File(dataDir, CONFIG_FILENAME);

		if(this.config == null)
		{
			this.config = new Config();
		}
		
		try {
			
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
			
			this.config.setBroadcastOnUnknownCountry(config.getBoolean("broadcastOnUnknownCountry"));
			this.config.setBroadcastAltJoinMsgOnUnknownCountry(config.getBoolean("broadcastAltJoinMsgOnUnknownCountry"));
			this.config.setMessagesCulture(config.getString("messages-culture"));
			this.config.setCountrynamesCulture(config.getString("countrynames-culture"));
			this.config.setDebug(config.getBoolean("debug"));
			
		} catch (Exception e) {
			this.getLogger().severe("Error loading the " + configFile.getName() + " file.");
			e.printStackTrace();
		}

		
		File messagesBaseDir = new File(dataDir, MESSAGES_BASEDIR);
		
		File messagesFile = new File(messagesBaseDir, String.format(MESSAGES_FILENAME_FORMAT, this.config.getMessagesCulture()));

		try (InputStreamReader sr = new InputStreamReader(new FileInputStream(messagesFile), "UTF8")) 
		{

			Configuration messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(sr);
			
			this.config.setJoinWithCountryMessage(messages.getString("joinWithCountry"));
			this.config.setJoinWithoutCountryMessage(messages.getString("joinWithoutCountry"));
			
		} catch (Exception e) 
		{
			this.getLogger().severe("Error loading the " + messagesFile.getName() + " file.");
			e.printStackTrace();
		}
		
		
		File countrynamesBaseDir = new File(dataDir, COUNTRYNAMES_BASEDIR);
		
		File countrynamesFile = new File(countrynamesBaseDir, String.format(COUNTRYNAMES_FILENAME_FORMAT, this.config.getCountrynamesCulture()));

		try(InputStreamReader sr = new InputStreamReader(new FileInputStream(countrynamesFile), "UTF8")) 
		{
		
			Configuration countrynames = ConfigurationProvider.getProvider(YamlConfiguration.class).load(sr);

			this.config.getCountryNames().clear(); //need that because a reloaded file maybe not contains all the keys that previous
			
			for(String key : countrynames.getKeys())
			{
				this.config.getCountryNames().put(key, countrynames.getString(key));
			}
			
		} catch (Exception e) 
		{
			this.getLogger().severe("Error loading the " + countrynamesFile.getName() + " file.");
			e.printStackTrace();
		}
		
		
		File geoipdbFile = new File(dataDir, GEOIP_DB_FILENAME);
		
		if(this.geoipdbreader != null)
		{
			try {
				this.geoipdbreader.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		try
		{
			this.geoipdbreader = new DatabaseReader.Builder(geoipdbFile).withCache(new CHMCache()).build();
		}
		catch (IOException e)
		{			
			this.getLogger().severe("Error loading the " + geoipdbFile.getName() + " file.");
			e.printStackTrace();
		}
		
	}

	public static Plugin getInstance()
	{
		return instance;
	}
	
	public DatabaseReader getGeoIPDBReader()
	{
		return this.geoipdbreader;
	}
	
	public Config getConfig()
	{
		return this.config;
	}
	
}
