package model;

import org.telegram.telegrambots.meta.bots.AbsSender;
import storage.Storage;

public class StartCommand extends BotCommand {
	public StartCommand() {
	}

	@Override
	public void execute(BotUser botUser) {
		StringBuilder messageBuilder = new StringBuilder();
		String userName = botUser.getUserName();

		messageBuilder.append("Welcome, ")
				.append(userName)
				.append("!\n\n");
		messageBuilder
				.append("I am a urban socializer bot.\n\n")
				.append("I will help you to bla bla bla ")
				.append("and bla bla bla again)))")
				.append("if you want to see all possible command type /help");
	//	sendMessageToUser(botUser, messageBuilder.toString());
		Storage.USERS_TABLE.put(botUser.getUserId(), botUser);
	}
}