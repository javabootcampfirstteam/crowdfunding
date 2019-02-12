package model;


import org.telegram.telegrambots.meta.bots.AbsSender;

public class HelpCommand extends BotCommand {
	public HelpCommand() {
	}

	@Override
	public void execute(BotUser botUser) {

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder
				.append("Available commands:\n")
				.append("/help - show the list of available commands\n")
				.append("/add - add project \n")
				.append("/remove - remove project\n")
				.append("/notifications_on - turn notifications mode on. That means ")
				.append("bot will message you about the latest news about the project\n")
				.append("/notifications_off - turn notifications mode off\n")
				.append("/show_project_list - show the list of your current subscribes\n");
		//sendMessageToUser(messageBuilder.toString());
	}
}