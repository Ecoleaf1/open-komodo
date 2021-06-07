package net.wigoftime.open_komodo.sql;

public class SQLCode {
	public static enum SQLCodeType {CREATE_MAIN_TABLE, CREATE_MODERATION_TABLE, CREATE_BAG_INVENTORY, CREATE_BAG_INVENTORY_TABLE, CREATE_MAIL_TABLE, SET_BAG_INVENTORY, GET_BAG_INVENTORY, GET_LATEST_BAGID, CREATE_WORLD_TABLE,
		SAVE_ITEMS, GET_ITEMS, GET_ACTIVE_TAG, SET_ACTIVE_TAG, GET_JOINDATE, CONTAINS_PLAYER, SET_XP, GET_XP, CREATE_PLAYER, CREATE_MODERATION_PLAYER, CONTAINS_MODERATION_PLAYER, CONTAINS_WORLD_PLAYER, CREATE_WORLD_PLAYER,
		SET_GLOBAL_PERMISSIONS, GET_GLOBAL_PERMISSIONS, SET_NICKNAME, GET_NICKNAME, SET_WORLD_PERMISSIONS, GET_WORLD_PERMISSIONS, SET_INVENTORY, GET_INVENTORY, SET_TIP, GET_TIP, GET_RANKID, SET_RANKID,
		GET_MAIL, COUNT_MAIL, SEND_MAIL, CLEAR_MAIL,GET_MUTEDATE, SET_MUTEDATE, GET_BANDATE, SET_BANDATE, GET_MUTEREASON, SET_MUTEREASON, GET_BANREASON, SET_BANREASON, SET_HOMES, GET_HOMES, GET_POINTS, GET_COINS, SET_POINTS, SET_COINS,
		SET_ITEMS, GET_PETS, SET_PETS, CREATE_MARRY_TABLE, ADD_MARRY, REMOVE_MARRY, GET_MARRY_PARTNERS};

	public static void setup() {

	}

	public static final String getSQLCode(SQLCodeType type) {
		switch (type) {
		case CONTAINS_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Main` WHERE `UUID` = ?;";
		case CONTAINS_MODERATION_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Moderation` WHERE `UUID` = ?;";
		case CONTAINS_WORLD_PLAYER:
			return "SELECT `UUID` FROM `OpenKomodo.Worlds.%s` WHERE `UUID` = ?;";
		case CREATE_BAG_INVENTORY:
			return "INSERT INTO `OpenKomodo.Bag_Inventory` ( "
					+ "`UUID` , "
					+ "`Inventory` , "
					+ "`ID`) VALUES (" 
					+ "?, '', ?);";
		case CREATE_BAG_INVENTORY_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Bag_Inventory` ( `UUID` BINARY(16) NOT NULL, `ID` INT UNSIGNED NOT NULL, `Inventory` BLOB NOT NULL, UNIQUE (`ID`));";
		case CREATE_MAIL_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Mail` ( `Receiver` BINARY(16) NOT NULL , `Sender` BINARY(16) NOT NULL , `Date` DATETIME NOT NULL , `Message` VARCHAR(256) NOT NULL)";
		case CREATE_MAIN_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Main` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`Nickname` VARCHAR(50) NOT NULL DEFAULT '', "
					+ "`Active Tag` VARCHAR(70) DEFAULT '', "
					+ "`Joined Date` DATETIME NULL DEFAULT NULL , "
					+ "`Rank ID` TINYINT UNSIGNED DEFAULT 0 , "
					+ "`XP` DECIMAL(10,6) UNSIGNED NOT NULL DEFAULT '0.0' , "
					+ "`Permissions` BLOB NOT NULL , "
					+ "`TIP (USD)` decimal(5,1) UNSIGNED DEFAULT 0.0 , "
					+ "`Points` INT UNSIGNED DEFAULT 720 , "
					+ "`Coins` INT UNSIGNED DEFAULT 0 , "
					+ "`Items` LONGTEXT NOT NULL, "
					+ "`Pets` BLOB NOT NULL, "
					+ "`Homes` BLOB NOT NULL, "
					+ "`Home Limit` INT UNSIGNED DEFAULT 1);";
		case CREATE_MODERATION_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Moderation` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`Mute Date` DATETIME NULL DEFAULT '1970-01-01 00:00:01' , "
					+ "`Mute Reason` VARCHAR(150) NOT NULL DEFAULT '' , "
					+ "`Ban Date` DATETIME NULL DEFAULT '1970-01-01 00:00:01' , "
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
					+ "(?, '', ?, '[]', ?, ?, ?);";
		case CREATE_MODERATION_PLAYER:
			return "INSERT INTO `OpenKomodo.Moderation` ("
					+ "`UUID`, "
					+ "`Mute Reason`, "
					+ "`Ban Reason` ) VALUES "
					+ "(?, '', '');";
		case CREATE_WORLD_PLAYER:
			return "INSERT INTO `OpenKomodo.Worlds.%s` ("
					+ "`UUID`, "
					+ "`Inventory`, "
					+ "`Permissions`) VALUES "
					+ "(?, '', ?);";
		case CREATE_WORLD_TABLE:
			return "CREATE TABLE IF NOT EXISTS `OpenKomodo.Worlds.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL PRIMARY KEY, "
					+ "`inventory` BLOB DEFAULT NULL, "
					+ "`Permissions` BLOB);";
		case GET_ACTIVE_TAG:
			return "SELECT `Active Tag` FROM `OpenKomodo.Main` WHERE UUID = ?";
		case GET_BAG_INVENTORY:
			return "SELECT `Inventory` FROM `OpenKomodo.Bag_Inventory` WHERE `UUID` = ? AND `ID` = ?";
		case GET_LATEST_BAGID:
			return "SELECT COUNT('ID') FROM `OpenKomodo.Bag_Inventory` WHERE `UUID` = ?;";
		case GET_BANDATE:
			return "SELECT `Ban Date` FROM `OpenKomodo.Moderation` " +
			"WHERE `UUID` = ?;";
		case GET_BANREASON:
			return "SELECT `Ban Reason` FROM `OpenKomodo.Moderation` " +
			"WHERE `UUID` = ?;";
		case GET_COINS:
			return "SELECT `Coins` FROM `OpenKomodo.Main` " +
			"WHERE `UUID` = ?;";
		case GET_GLOBAL_PERMISSIONS:
			return "SELECT `Permissions` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = ?;";
		case GET_HOMES:
			return "SELECT `Homes` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = ?;";
		case GET_INVENTORY:
			return "SELECT `Inventory` FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = ?;";
		case GET_ITEMS:
			return "SELECT `Items` FROM `OpenKomodo.Main` WHERE UUID = ?;";
		case GET_JOINDATE:
			return "SELECT `Joined Date` FROM `OpenKomodo.Main` WHERE `UUID` = ?";
		case GET_NICKNAME:
			return "SELECT `Nickname` FROM `OpenKomodo.Main` WHERE `UUID` = ?;";
		case GET_MAIL:
			return "SELECT `Message`, `Sender`, `Date` FROM `OpenKomodo.Mail` WHERE `Receiver` = ?";
		case COUNT_MAIL:
			return "SELECT COUNT(`Receiver`)\n " +
					"FROM `OpenKomodo.Mail`\n " +
					"WHERE `Receiver` = ?;";
		case SEND_MAIL:
			return "INSERT INTO `OpenKomodo.Mail` (`Receiver`, `Sender`, `Date`, `Message`) VALUES (?, ?, ?, ?)";
		case CLEAR_MAIL:
			return "DELETE FROM `OpenKomodo.Mail` WHERE `Receiver` = ?";
		case GET_MUTEDATE:
			return "SELECT `Mute Date` FROM `OpenKomodo.Moderation` " +
					"WHERE `UUID` = ?;";
		case GET_MUTEREASON:
			return "SELECT `Mute Reason` FROM `OpenKomodo.Moderation` " +
					"WHERE `UUID` = ?;";
		case GET_POINTS:
			return "SELECT `Points` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = ?;";
		case GET_PETS:
			return "SELECT `Pets` FROM `OpenKomodo.Main` " +
			"WHERE `UUID` = ?;";
		case GET_RANKID:
			return "SELECT `Rank ID` FROM `OpenKomodo.Main` " + 
					"WHERE UUID= ?;";
		case GET_TIP:
			return "SELECT `TIP (USD)` FROM `OpenKomodo.Main` " + 
			"WHERE UUID = ?;";
		case GET_WORLD_PERMISSIONS:
			return "SELECT `Permissions` FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = ?;";
		case GET_XP:
			return "SELECT `XP` FROM `OpenKomodo.Main` WHERE `UUID` = ?";
		case SAVE_ITEMS:
			return "UPDATE `OpenKomodo.Main` SET `Items` = ? WHERE `UUID` = ?";
		case SET_ACTIVE_TAG:
			return "UPDATE `OpenKomodo.Main.%s` SET `Ban Reason` = ? WHERE `UUID` = ?";
		case SET_BAG_INVENTORY:
			return "UPDATE `OpenKomodo.Bag_Inventory` SET `Inventory` = ? WHERE `UUID` = ? AND `ID` = ?";
		case SET_BANDATE:
			return "UPDATE `OpenKomodo.Moderation` SET `Ban Date` = ? WHERE `UUID` = ?);";
		case SET_BANREASON:
			return "UPDATE `OpenKomodo.Moderation` SET `Ban Reason` = ? WHERE `UUID` = ?";
		case SET_COINS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Coins` = ? " +
			"WHERE `UUID` = ?;";
		case SET_GLOBAL_PERMISSIONS:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Permissions` = ? " +
					"WHERE `UUID` = ?;";
		case SET_HOMES:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Homes` = ?" +
			"WHERE `UUID` = ?;";
		case SET_INVENTORY:
			return "UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Inventory` = ? " +
					"WHERE `UUID` = ?;";
		case SET_ITEMS:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Items` = ? " +
					"WHERE `UUID` = ?;";
		case SET_NICKNAME:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `Nickname` = ? " +
					"WHERE `UUID` = ?;";
		case SET_MUTEDATE:
			return "UPDATE `OpenKomodo.Moderation` " +
			"SET `Mute Date` = ? " +
			"WHERE `UUID` = ?;";
		case SET_MUTEREASON:
			return "UPDATE `OpenKomodo.Moderation` " +
			"SET `Mute Reason` = ? " +
			"WHERE `UUID` = ?;";
		case SET_PETS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Pets` = ? " +
			"WHERE `UUID` = ?;";
		case SET_POINTS:
			return "UPDATE `OpenKomodo.Main` " +
			"SET `Points` = ? " +
			"WHERE `UUID` = ?;";
		case SET_RANKID:
			return "UPDATE `OpenKomodo.Main` " + 
					"SET `Rank ID` = ? " +
					"WHERE `UUID` = ?;";
		case SET_TIP:
			return "UPDATE `OpenKomodo.Main` SET `TIP (USD)` = ? WHERE `UUID` = ?;";
		case SET_WORLD_PERMISSIONS:
			return "UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Permissions` = ? " +
					"WHERE `UUID` = ?;";
		case SET_XP:
			return "UPDATE `OpenKomodo.Main` " +
					"SET `XP` = ? " +
					"WHERE `UUID` = ?;";
		case CREATE_MARRY_TABLE:
			return  "CREATE TABLE IF NOT EXISTS `OpenKomodo.Marriage` ( "
					+ "`Player` BINARY(16) NOT NULL, "
					+ "`Married` BINARY(16) NOT NULL)";
		case ADD_MARRY:
			return "INSERT INTO `OpenKomodo.Marriage` ( `Player`, `Married`) VALUES (?, ?); ";
		case REMOVE_MARRY:
			return "DELETE FROM `OpenKomodo.Marriage` WHERE `Player` = ? AND `Married` = ?";
		case GET_MARRY_PARTNERS:
			return "SELECT `Married` FROM `OpenKomodo.Marriage` WHERE `Player` = ?";

		default:
			return null;
		
		}
	}
}
