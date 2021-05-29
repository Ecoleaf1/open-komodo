package net.wigoftime.open_komodo.sql;

public class SQLCode {
	public static enum SQLCodeType {CREATE_MAIN_TABLE, CREATE_MODERATION_TABLE, CREATE_BAG_INVENTORY, CREATE_BAG_INVENTORY_TABLE, SET_BAG_INVENTORY, GET_BAG_INVENTORY, GET_LATEST_BAGID, CREATE_WORLD_TABLE,
		SAVE_ITEMS, GET_ITEMS, GET_ACTIVE_TAG, SET_ACTIVE_TAG, GET_JOINDATE, CONTAINS_PLAYER, SET_XP, GET_XP, CREATE_PLAYER, CREATE_MODERATION_PLAYER, CONTAINS_MODERATION_PLAYER, CONTAINS_WORLD_PLAYER, CREATE_WORLD_PLAYER,
		SET_GLOBAL_PERMISSIONS, GET_GLOBAL_PERMISSIONS, SET_NICKNAME, GET_NICKNAME, SET_WORLD_PERMISSIONS, GET_WORLD_PERMISSIONS, SET_INVENTORY, GET_INVENTORY, SET_TIP, GET_TIP, GET_RANKID, SET_RANKID,
		GET_MAIL, COUNT_MAIL, SEND_MAIL, CLEAR_MAIL,GET_MUTEDATE, SET_MUTEDATE, GET_BANDATE, SET_BANDATE, GET_MUTEREASON, SET_MUTEREASON, GET_BANREASON, SET_BANREASON, SET_HOMES, GET_HOMES, GET_POINTS, GET_COINS, SET_POINTS, SET_COINS,
		SET_ITEMS, GET_PETS, SET_PETS}
	
	public static final String getSQLCode(SQLCodeType type) {
		switch (type) {
		case CONTAINS_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s');";
		case CONTAINS_MODERATION_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Moderation` WHERE `UUID` = UNHEX('%s');";
		case CONTAINS_WORLD_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Worlds.%s` WHERE `UUID` = UNHEX('%s');";
		case CREATE_BAG_INVENTORY:
			return "INSERT INTO `OpenKomodo.Bag_Inventory.%s` ( "
					+ "`UUID` , "
					+ "`Inventory` , "
					+ "`ID`) VALUES (" 
					+ "UNHEX('%s'), '', %d);";
		case CREATE_BAG_INVENTORY_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Bag_Inventory.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`ID` INT UNSIGNED, "
					+ "`Inventory` TEXT, UNIQUE (`ID`));";
		case CREATE_MAIN_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Main` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`Nickname` VARCHAR(50) NOT NULL DEFAULT '', "
					+ "`Active Tag` VARCHAR(70) DEFAULT '', "
					+ "`Joined Date` DATE NULL DEFAULT NULL , "
					+ "`Rank ID` TINYINT UNSIGNED DEFAULT 0 , "
					+ "`XP` DECIMAL(10,6) UNSIGNED NOT NULL DEFAULT '0.0' , "
					+ "`Permissions` LONGTEXT NOT NULL , "
					+ "`TIP (USD)` decimal(5,1) UNSIGNED DEFAULT 0.0 , "
					+ "`Points` INT UNSIGNED DEFAULT 720 , "
					+ "`Coins` INT UNSIGNED DEFAULT 0 , "
					+ "`Items` LONGTEXT NOT NULL, "
					+ "`Pets` MEDIUMTEXT NOT NULL, "
					+ "`Homes` LONGTEXT NOT NULL, "
					+ "`Home Limit` INT UNSIGNED DEFAULT 1);";
		case CREATE_MODERATION_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Moderation` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`Mute Date` DATETIME NULL DEFAULT NULL , "
					+ "`Mute Reason` VARCHAR(150) NOT NULL DEFAULT '' , "
					+ "`Ban Date` DATETIME NULL DEFAULT NULL , "
					+ "`Ban Reason` VARCHAR(100) NOT NULL DEFAULT '');";
		case CREATE_PLAYER:
			return "INSERT INTO `OpenKomodo.Main` ("
					+ "`UUID`, "
					+ "`Nickname`, "
					+ "`Permissions`, "
					+ "`Items`, "
					+ "`Pets`, "
					+ "`Homes`, "
					+ "`Joined Date`) VALUES "
					+ "(UNHEX('%s'), '', 'rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAAeA==', '[]', \"rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAAeA==\", 'rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAAeA==', '%s');";
		case CREATE_MODERATION_PLAYER:
			return "INSERT INTO `OpenKomodo.Moderation` ("
					+ "`UUID`, "
					+ "`Mute Date`, "
					+ "`Mute Reason`, "
					+ "`Ban Date`, "
					+ "`Ban Reason` ) VALUES "
					+ "(UNHEX('%s'), DATE('1990-01-01'), '', DATE('1990-01-01'), '');";
		case CREATE_WORLD_PLAYER:
			return "INSERT INTO `OpenKomodo.Worlds.%s` ("
					+ "`UUID`, "
					+ "`Inventory`, "
					+ "`Permissions`) VALUES "
					+ "(UNHEX(\"%s\"), '', 'rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAAeA==');";
		case CREATE_WORLD_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Worlds.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL PRIMARY KEY, "
					+ "`inventory` TEXT DEFAULT NULL, "
					+ "`Permissions` TEXT);";
		case GET_ACTIVE_TAG:
			return "SELECT `Active Tag` FROM `OpenKomodo.Main` WHERE UUID = UNHEX('%s')";
		case GET_BAG_INVENTORY:
			return "SELECT `Inventory` FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') AND `ID` = %d";
		case GET_LATEST_BAGID:
			return "SELECT `ID` FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') ORDER BY `ID` DESC LIMIT 1";
		case GET_BANDATE:
			return "SELECT `Ban Date` FROM `OpenKomodo.Moderation` " +
			"WHERE `UUID` = UNHEX('%s');";
		case GET_BANREASON:
			return "SELECT `Ban Reason` FROM `OpenKomodo.Moderation` " +
			"WHERE `UUID` = UNHEX('%s');";
		case GET_COINS:
			return "SELECT `Coins` FROM `OpenKomodo.Main` " +
			"WHERE `UUID` = UNHEX('%s');";
		case GET_GLOBAL_PERMISSIONS:
			return "SELECT `Permissions` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_HOMES:
			return "SELECT `Homes` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_INVENTORY:
			return "SELECT `Inventory` FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_ITEMS:
			return "SELECT `Items` FROM `OpenKomodo.Main` WHERE UUID = UNHEX('%s');";
		case GET_JOINDATE:
			return "SELECT `Joined Date` FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s');";
		case GET_NICKNAME:
			return "SELECT `Nickname` FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s');";
		case GET_MAIL:
			return "SELECT * FROM `OpenKomodo.Mail` WHERE `Receiver` = UNHEX('%s')";
		case COUNT_MAIL:
			return "SELECT COUNT(`Receiver`)\n " +
					"FROM `OpenKomodo.Mail`\n " +
					"WHERE `Receiver` = UNHEX('%s');";
		case SEND_MAIL:
			return "INSERT INTO `OpenKomodo.Mail` (`Receiver`, `Sender`, `Date`, `Message`) VALUES (UNHEX('%s'), UNHEX('%s'), '%s', \"%s\")";
		case CLEAR_MAIL:
			return "DELETE FROM `OpenKomodo.Mail` WHERE `Receiver` = UNHEX('%s')";
		case GET_MUTEDATE:
			return "SELECT `Mute Date` FROM `OpenKomodo.Moderation` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_MUTEREASON:
			return "SELECT `Ban Reason` FROM `OpenKomodo.Moderation` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_POINTS:
			return "SELECT `Points` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_PETS:
			return "SELECT `Pets` FROM `OpenKomodo.Main` " +
			"WHERE `UUID` =UNHEX('%s');";
		case GET_RANKID:
			return "SELECT `Rank ID` FROM `OpenKomodo.Main` " + 
					"WHERE UUID=UNHEX('%s');";
		case GET_TIP:
			return "SELECT `TIP (USD)` FROM `OpenKomodo.Main` " + 
			"WHERE UUID = UNHEX('%s');";
		case GET_WORLD_PERMISSIONS:
			return "SELECT `Permissions` FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = UNHEX('%s');";
		case GET_XP:
			return "SELECT `XP` FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s')";
		case SAVE_ITEMS:
			return "UPDATE `OpenKomodo.Main` SET `Items` = \"%s\" WHERE `UUID` = UNHEX('%s')";
		case SET_ACTIVE_TAG:
			return "UPDATE `OpenKomodo.Main.%s` SET `Ban Reason` = \"%s\" WHERE `UUID` = UNHEX('%s')";
		case SET_BAG_INVENTORY:
			return "UPDATE `OpenKomodo.Bag_Inventory.%s` SET `Inventory` = \"%s\" WHERE `UUID` = UNHEX('%s') AND `ID` = %d";
		case SET_BANDATE:
			return "UPDATE `OpenKomodo.Moderation` SET `Ban Date` = \"%s\" WHERE `UUID` = UNHEX(\"%s\");";
		case SET_BANREASON:
			return "UPDATE `OpenKomodo.Moderation` SET `Ban Reason` = \"%s\" WHERE `UUID` = UNHEX('%s')";
		case SET_COINS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Coins` = %d " +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_GLOBAL_PERMISSIONS:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Permissions` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_HOMES:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Homes` = \"%s\"" +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_INVENTORY:
			return "UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Inventory` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_ITEMS:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Items` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_NICKNAME:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Nickname` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_MUTEDATE:
			return "UPDATE `OpenKomodo.Moderation` " +
			"SET `Mute Date` = '%s' " +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_MUTEREASON:
			return "UPDATE `OpenKomodo.Moderation` " +
			"SET `Mute Reason` = \"%s\" " +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_PETS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Pets` = \"%s\" " +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_POINTS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Points` = %d " +
			"WHERE `UUID` = UNHEX('%s');";
		case SET_RANKID:
			return "UPDATE `OpenKomodo.Main` " + 
					"SET `Rank ID` = %d " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_TIP:
			return "UPDATE `OpenKomodo.Main` SET `TIP (USD)` = %.1f WHERE `UUID` = UNHEX('%s');";
		case SET_WORLD_PERMISSIONS:
			return "UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Permissions` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');";
		case SET_XP:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `XP` = %.4f " +
					"WHERE `UUID` = UNHEX('%s');";
		default:
			return null;
		
		}
	}
}
