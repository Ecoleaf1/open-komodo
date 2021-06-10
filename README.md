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
To setup discord, you need to use their discord configuration file in the DiscordSRV's plugin folder. **If you have DiscordSRV in your plugins folder, but do not have it setup, the Open Komodo plugin will break.**

In DiscordSRV's config file, go to `Channels` and set the first column as any name that you wish, and paste the discord channel ID in the second column (Write both of them within the quotation marks).

##### How to get the Discord Channel ID
First, go into your discord settings > Advanced, and turn on Developer Mode
![tutorial 1](https://i.imgur.com/G3uCjuC.png)

Right click on the discord channel that you want the minecraft chat in and click `Copy ID`
![tutorial 2](https://i.imgur.com/kCgxfH1.png)

And paste it in to the second column in the DiscordSRV's config file.
![Tutorial 3](https://i.imgur.com/xkRN9GY.png)

Save the config file and restart the server. Open Komodo will automatically start sending the minecraft chat to the discord channel.

_**It is recommended to turn on Experiment WebhookChatMessageDelivery in the DiscordSRV's config. It makes the minecraft chat on discord a lot cleaner.**_

![Tutorial 4](https://i.imgur.com/spuvMbo.png)

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

# Compiling & Building:
Use the buildtools from spigot to generate craftbucket 1.16.1 and put the craftbukkit-1.16.1-R0.1-SNAPSHOT.jar file into "External Dependencies". This is the only maunal dependency that you will need to add. For more information about BuildTools, check out the guide from the spigot team (https://www.spigotmc.org/wiki/buildtools/).

You will also need to install maven to compile & build the plugin.

After installing maven, go in the plugin folder/directory in the terminal/command prompt, and run the command: mvn install

When the build is completed by maven, go in the target folder in the plugin folder/directory that maven generated. From there you can grab the open-komodo plugin.
