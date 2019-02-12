package model;

import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public abstract class BotCommand {
	private List<String> messagesHistory = new ArrayList<>();

	public BotCommand() {
	}

	public BotCommand(List<String> messages) {
		messagesHistory = messages;
	}

	public abstract void execute(BotUser user);

	public void addMessage(String message) {
		messagesHistory.add(message);
	}

	public List<String> getMessagesHistory() {
		return messagesHistory;
	}
}