# Requirements:
**A MySQL/Mariadb database server is required to run.** When the plugin runs for the first time, the config.yml will be generated to put the database information in. The plugin will stop the server without a database.

### You will need these plugins to run the server:
`ProtocolLib`, `PlayerParticles`

### The plugins that can enhance the server:
`NuVotifier`, `DiscordSRV`, `varlights`, `ViaVersion`

**Running Bukkit will cause troubles, the server uses ChatComponents which is only in spigot, so spigot or higher fork (example paper) will only work.**

**The server must be running spigot or a spigot fork in specifically in `1.16.1`.**

# installation/setup:
Download the plugin from `Releases` on the current page, and put in the plugins folder. Make sure you have the correct server type mentioned above.

Run the server with the plugin, and when the plugin loads the server will stop and the `config.yml` file in the plugin folder will generate. From there, you need to put in your SQL database information. After putting in the correct information, the plugin will load assuming you are running 1.16.1 spigot/spigot fork with the dependencies installed properly.

### Where do I get the world and resource packs?
By default, a resourcepack is assigned when generating the plugin's config file. However it may get deleted if it gets outdated.

Below is where you can set the resource url to be (or download the resourcepack if you so desire), and the world file. It is recommended to get the latest version of the world and resourcepack:

https://drive.google.com/drive/folders/12UEmwj2jWY_M9Hpp-FzHeb730OIESRaq?usp=sharing

### Discord setup [Optional]

**If you have DiscordSRV in your plugins folder, but do not have it setup, the Open Komodo plugin will break.**

Having a discord setup can be useful if you want Discord players & Minecraft players to talk to each other, while adding additonal channels like an automatic server status channel, as well as dontations and a moderation channel to moderate with Discord commands to moderate minecraft users via Discord. When Minecraft players link their discord to the server, it will also display their discord name (and tag) when someone hovers over their name in the Minecraft chat. Here's how to setup a Discord bot.

##### Create discord bot
Go to the discord developer portal website (https://discord.com/developers/applications) and click on `New Application`.

![tutorial 1](https://i.imgur.com/IML2rMJ.png)

Create a name for the application (This is not the discord bot name)

![tutorial 2](https://i.imgur.com/ND7V4Z7.png)

Go to build a bot and create your discord bot. From there you can change the username and icon.

So the plugin knows what the bot is, copy your token (Do not share with anyone, as the token gives full access to the bot).

![tutorial 3](https://i.imgur.com/PcUuXPZ.png)

And paste it into the DiscordSRV config file

![tutorial 4](https://i.imgur.com/Gp8D0IT.png)

***You will need to enable server members intent permission to have your bot working.*** To enable it, go under the bot settings in the Discord Developer Portal, and enable `Server Members Intent`.

![Tutorial 5](https://i.imgur.com/xQFZhgn.png)

open DiscordSRV's config file and create a pair or use an existing one in `Channels`. The first column of the pair is where you set the type (does not need to match your discord channel), and the second column for channel IDs. The guide to get the type and get channel IDs are below this section.

![Tutorial 3](https://i.imgur.com/zklKrUC.png)

Save the config file and restart the server. Open Komodo will automatically start sending the minecraft chat to the discord channel.

_**It is recommended to turn on Experiment WebhookChatMessageDelivery in the DiscordSRV's config. It makes the minecraft chat on discord a lot cleaner.**_

![Tutorial 4](https://i.imgur.com/spuvMbo.png)

##### What channel types are there?
At current, there are `minecraft-chat`, `donations`, `moderation`. The types are self explanatory.

To use them, just set the exact values (case sensitive) as the first column in a channel pair.

![tutorial channels](https://i.imgur.com/w1f2R6q.png)

##### How to get the Discord Channel ID
First, go into your discord settings > Advanced, and turn on Developer Mode. You will need this to enable an option to get IDs.
![tutorial 1](https://i.imgur.com/G3uCjuC.png)

Right click on the discord channel that you want to use, click `Copy ID`.
![tutorial 2](https://i.imgur.com/kCgxfH1.png)

And paste it in to the second column (where there a lot of zeros from the image below) in a pair in the DiscordSRV's config file.
![Tutorial 3](https://i.imgur.com/zklKrUC.png)

##### How do I invite the discord bot to a  server?
Under `OAuth2` in the Discord Developer Portal, copy the `Client ID` and replace `INSERT_CLIENT_ID_HERE` in the url with the `client ID`. After doing so, click on the link below and invite your discord bot to the server.
https://discord.com/oauth2/authorize?client_id=INSERT_CLIENT_ID_HERE&scope=bot&permissions=3187146351

If you want more options for permsissions, click the link below.
https://discordapi.com/permissions.html#0.

From there you can adjust your general permissions before you invite it to a discord server. Once you are done adjusting your permissions, paste in your `Client ID`, and click on the link that the website provided.

### NuVotifier setup [Optional]
Plenty of minecraft server providers already offer how to setup votifier (Such as https://help.pebblehost.com/en/article/how-to-set-up-votifier-14bx1hi/). Nevertheless, below is how you can setup your plugin.

As always, run the server with the plugin to generate the plugin folder.

Setup an additonal port setup for Votifier before you move on to the next step. (Example, if you are self hosting, you will need to port forward another port).

With that, open the NuVotifier's config log and paste in your additonal port in `port`.

![Tutorial 5](https://i.imgur.com/YX4ZDxm.png)

Save the config file and restart the server for changes to take effect. There are some sites that tests Votifier (Example: https://mctools.org/votifier-tester).

#### What do you put in as the IP Address / Port?
The ip address is the same as the server's ip address. As for the port, the Votifier port that you setup from above.

#### How do I get my public key?

Go into the `Votifier` plugin folder, go into `rsa` folder and open up `public.key`.

![Tutorial 6](https://i.imgur.com/r321CQP.png)

Copy ***everything*** in the `public.key` file, and pasting it is your public key (Don't share your private key! Keep it safe!).

### Varlights [Optional, but recommended]
Varlights is a plugin to create light without the need for block light sources. Varlights store data within world folders, so using the komodo world will generate the additional lights automatically.

### ViaVersions [Optional, but recommended]
ViaVersions is a plugin that allows players who join in newer versions of minecraft to play on the server. Without this plugin, players will have to join the server with the exact version (1.16.1).

### Setup tipping on the server on store platforms
When making a package, add the /gentip command, as it adds tipping to a player. For the usage of the command, check out the commands section below.

# Commands:
 `/tutorial` - Go to the tutorial
`/help` - Displays basic help message
`/rank help` - Displays breif summary of how ranks works
`/money help` - Displays breif summary of how money works
`/message {username} {message}` - Send someone a message
`/msg {username} {message}` - Send someone a message
`/r` - Reply back to a message

`/emote` - displays emotes
`/emote {Emote Name}` - use an solo emote
`/emote {Emote Name} {Username}` - use a target emote
`/emote reload` - reloads emote

`/tpa {Username}` - Request to teleport to a player
`/tp {Username}` - Request to teleport to a player
`/tpahere {Username}` - Request to have a player teleport to you
`/tphere {Username}` - Request to have a player teleport to you
`/atp {Username}` - Teleport to a player without a request
`/atphere {Username}` - Make a player teleport to you without a request

`/home help` - Displays basic summary on how homes work

`/pay {Username} {Amount} {Currency Type: Coins/Points}` - Donating to a player

`/genpay {Username} {Amount} {Currency Type: Coins/Points}` - Generate currency to a player

`/gentip {uuid} {amount}` - Add tip balance to player when they donate to the server (use this command when someone donates to the server. As stated above in ***Setup tipping on the server on store platforms***).

`/promote list-perms {username}` - Displays what a player permissions have
`/promote list-perms {username} {World Name}` - Displays what a player permissions have in that world

`/promote player add {Username} {Permission Node}` - Add a permission to a player
`/promote player add {Username} {World Name} {Permission Node}` - Add a permission to a player in a spefific world
`/promote player remove {Username} {Permission Node}` - Remove a permission to a player
`/promote player remove {Username} {World Name} {Permission Node}` - Remove a permission to a player in a spefific world

`/promote rank {Username} {Rank name}` - Set player's rank
`/promote rank {Username} DEFAULT` - Set player rank back to default

`/propshop` - To open the propshop
`/hats` - To open the hats menu

`/mail` -  Check mail
`/mail send {Username} {Message}` - Send someone a message
`/mail clear` - To clear mail

`/marriage` - Access Marriage menu
`/marriage marry {Username}` - Marry a player
`/marriage divorce {Username}` - Divorce a player

`/adminkick {Username}` - Kick a player off the server
`/adminkick {Username} {Reason}` - Kick a player displaying the reason on their kicked screen

`/mute {Username} {Duration}` - Mute a player
`/mute {Username} {Duration} {Reason}` - Mute a player displaying their reason for it
`/ban {Username} {Duration}` - Ban a player
`/ban {Username} {Duration} {Reason}` - Ban a player displaying their reason for it

## For duration, here is the format:
5**n** = 5 minutes
7h = 7 hours
10d = 10 days
2w = 2 weeks
5m = 5 months
5y = 5 years

Example:
`/ban aPlayerName 5d` - Ban a player called 'aPlayerName' for 5 days

Since there is no `/unban` nor `/unmute`, `/mute {username} 0d` or `/ban {username} 0d` is the replacement.

# Permission Nodes:
`openkomodo.abilities.flight` - Gives a player access to flight (/fly)

`openkomodo.abilities.colornick` - Gives a player access to have Bukkit colors in their nicknames.
`openkomodo.abilities.morecolornick` - Gives a player access to RGB hex colors in their nicknames.

`openkomodo.pets.access` - Allows a player to use the pets feature in Open Komodo.
`openkomodo.particles.access` - Allows a player to use the particles feature in Open Komodo.

`openkomodo.build.buildmode` - Gives a player access to the build mode option (Permissions to change the enviroment in buildmode is below. Just gives permission for players to use /build)
`openkomodo.build.place` - Allows a player to place blocks in buildmode.
`openkomodo.build.break` - Allows a player to break blocks in buildmode.
`openkomodo.build.change` - Allows a player to change (like placing/taking armour in armour stands) in buildmode.
`openkomodo.build.hurt` - Allows a player to hurt entities in buildmode.
`openkomodo.build.drop` - Allows a player to drop items while in buildmode.
`openkomodo.teleport.builderworld` - Allows a player to teleport to the builderworld, where builders build before the main world gets updated.

`openkomodo.builder.furnituremenu` - Gives a player access to the furniture shop for better decorations. [Very Experimental] [Feature Disabled]

`openkomodo.chat.ignoreswear` - Swear filter ignores a players, giving player a bypass for the anti swear system.
`openkomodo.chat.ignorespam` - Give a player to bypass the anti-spam.

`openkomodo.admin.promote` - Gives a player permisison to use an ***admin*** command to use /promote. Which is used to add/remove ranks & permissions.

`openkomodo.admin.genpay` - Gives a player permission to use an ***admin*** command to use /genpay, which is used to generate points/coins to players.
`openkomodo.console.gentip` - Gives a player permission to use a ***console*** command to use /gentip, which is used to generate donations onto the server. I do not recommend giving this to anyone, and the command should only be used if something went wrong with a Player's donation.

`openkomodo.admin.emote.reload` - Gives a player access to an ***admin*** command to use `/emote reload`, which is used to refresh emotes if the `emotes.yml` file is changed.
`openkomodo.admin.rank.reload` - Gives a player access to an ***admin*** command to use `/rank reload`, which is used to refresh ranks if the `ranks.yml` file is changed.

`openkomodo.mod.teleport` - Gives a player access to a ***mod*** command of `/atp`, which is used to teleport to a player without approval
`openkomodo.mod.mute` - Gives a player access to a ***mod*** command of `/mute`, which is used to mute players
`openkomodo.mod.kick` - Gives a player access to a ***mod*** command of `/kick`, which is used to kick players out of the game
`openkomodo.mod.ban` - Gives a player access to a ***mod*** command of `/ban`, which is used to ban players
`openkomodo.mod.chatmonitor` - Gives a player access to a ***mod*** command of `/monitor`, which is used to monitor all the chat messages sent by players regardless if the player is close or not.
`openkomodo.mod.invisible` - Gives a player access to be able to turn themselves invisible with the ***mod*** command `/invis`.
`openkomodo.mod.seeotherinvisible` - Gives a player a ***mod*** access to see other players who are invisible to be visible.

`openkomodo.manager.monitorabuse` - Alerts a player when staff tries doing something suspicious. [Very Experimental]
`openkomodo.admin.visiblefullerrors` - When player gets an error, it will give out the full error. [Very Experimental]


# Compiling & Building:
Use the buildtools from spigot to generate craftbucket 1.16.1 and put the craftbukkit-1.16.1-R0.1-SNAPSHOT.jar file into "External Dependencies". This is the only maunal dependency that you will need to add. For more information about BuildTools, check out the guide from the spigot team (https://www.spigotmc.org/wiki/buildtools/).

You will also need to install maven to compile & build the plugin.

After installing maven, go in the plugin folder/directory in the terminal/command prompt, and run the command: mvn install

When the build is completed by maven, go in the target folder in the plugin folder/directory that maven generated. From there you can grab the open-komodo plugin.
