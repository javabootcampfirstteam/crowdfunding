import dao.impl.BotUserDaoImpl;
import dao.impl.ProjectDaoImpl;
import keyboard.KeyboardFactory;
import model.BotUser;
import model.Project;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Venue;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import service.abstr.BotUserService;
import service.impl.BotUserServiceImpl;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import storage.Storage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static storage.Storage.*;


public class UrbanSocializerBot extends TelegramLongPollingBot implements Serializable {

	private static final String BOT_NAME = "urban_socializer_bot";
	public static final String BOT_TOKEN = "726296784:AAG70XT-URXz69YvwPaKNQDivImfZAmFCOQ";
	BotUserService botUserService = BotUserServiceImpl.getInstance();


	public UrbanSocializerBot(DefaultBotOptions options) throws IOException {
		super(options);
	}


	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	public void onUpdateReceived(Update update) {

		List<String> currentContext;
		Message message = update.getMessage();

		if (message != null & message.hasText()) {
			String messageFromTelegram = message.getText();
			User userFromTelegram = message.getFrom();
			String telegramUserName = userFromTelegram.getFirstName();
			Integer currentUserId = userFromTelegram.getId();
			Long currentChatId = message.getChatId();
			int contextPosition = 0;
			BotUser botUser = new BotUser();
			//здесть не прававельно создается екземпляр BotUser

			if ("/start".equals(messageFromTelegram)) {
				if (!botUserService.isUserExistById(currentUserId)) {
					botUserService.addUser(new BotUser(), currentUserId);
					sendMsg(currentChatId, "Добрый день, вы впервые у нас, добавляем вас в базу\n");
					sendMsg(currentChatId, "/reg - регистрация\n/info - информация");
				} else {
					sendMsg(currentChatId, "Привет " + telegramUserName + ", Мы уже знакомы с вами!");
					sendMsg(currentChatId, "/info - информация");
				}
			}
			else {
				BotUser currentUser = botUserService.getUser(currentUserId);
				currentContext = currentUser.getContext();

				if (currentContext.isEmpty()) {
					switch (messageFromTelegram) {
						case "/reg": {
							sendMsg(currentChatId, "Пожалуйста укажите город где вы живете.");
							currentContext.add("/regCity");
							break;
						}
						case "/info": {
							sendMsg(currentChatId, "Добро пожаловать.\n" +
									"Здесь вы можете предложить свой проект " +
									"по улучшению городской или районной инфраструктуры. /add_project\n"
									+ "или ознакомится с текущими предложениями. /list_project");
						}
						case "/show_active_projects": {
							ProjectDaoImpl projectDao = new ProjectDaoImpl();

							//projectDao.getProjectById()
							break;
						}
						case "add_projects": {
							ProjectDaoImpl projectDao = new ProjectDaoImpl();
							Project project = new Project();

							LocalDateTime localDateTime = LocalDateTime.now();
							project.setProjectName(messageFromTelegram);
							project.setProjectDateCreate(localDateTime);
							project.setAuthorName("Petr");
							project.setProjectDescription(messageFromTelegram);
							project.setProjectId(project.getProjectId());
							project.setAuthorSurname("Hardcock");
							project.setProjectSum(2323);
							PROJECT_ID++;
							projectDao.addProject(project, PROJECT_ID);

							break;
						}
						case "test_kf": {
							KeyboardFactory keyboardFactory = new KeyboardFactory();
							keyboardFactory.createKeyboardRow(2, 3, true, true, true, "test1", "test2", "test3");

							//projectDao.getProjectById()
							break;
						}

						default: {
							sendMsg(currentChatId, "неизвестная команда");
						}

					}
				}
				else if(currentContext.isEmpty()) {
						switch (currentContext.get(contextPosition)) {
							case "/regCity": {
								if (currentContext.size() == ++contextPosition) {
									String cityReg = message.getText();
									currentUser.setUserAddress(cityReg);
									sendMsg(currentChatId, "Пожалуйста укажите свой телефон.");
									currentContext.add("/regPhone");
								} else {
									switch (currentContext.get(contextPosition)) {
										case "/regPhone": {
											if (currentContext.size() == ++contextPosition) {
												String phoneReg = message.getText();
												currentUser.setPhoneNumber(Integer.parseInt(phoneReg));
												sendMsg(currentChatId, "Добро пожаловать: " + telegramUserName + "\n"
														+ "из города " + currentUser.getUserAddress() + ", тел. номер: "
														+ currentUser.getPhoneNumber() + "\n");
												currentContext.clear();
												sendMsg(currentChatId, "Благодарим за регистрацию. " +
														"Теперь посмотрите что люди предлагают.\n /info");
											}
											break;
										}
									}
								}
								break;
							}

							case "/list_project": {
								sendMsg(currentChatId, "Здесь будут перечислены текущие проекты.");
								break;
							}

							default: {
								sendMsg(currentChatId, "неизвестная команда");
							}
						}
					}
				}
			}
		}
	


	// просто тестовый метод
	public static ReplyKeyboardMarkup createYesOrNoKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> commands = new ArrayList<>();
		KeyboardRow commandRow = new KeyboardRow();
		commandRow.add("Yes");
		commandRow.add("No");
		commands.add(commandRow);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(true);
		replyKeyboardMarkup.setKeyboard(commands);
		return replyKeyboardMarkup.setKeyboard(commands);
	}


	public void setButton(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(true);
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(new KeyboardButton("/reg"));
		keyboardRow.add(new KeyboardButton("/info"));
		keyboardRow.add(new KeyboardButton("/start"));
		keyboard.add(keyboardRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	private void setContextToUser(Integer userId, String context) {
		BotUser currentUser = botUserService.getUser(userId);
		currentUser.getContext().add(context);
	}

	private void removeLastContextElement(Integer userId) {
		BotUser currentUser = botUserService.getUser(userId);
		currentUser.getContext().remove(currentUser.getContext().size());
	}


	private void sendMsg(Long chatId, String txtMessage) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(txtMessage);

		try {
//            setButton(sendMessage);
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	public String getBotUsername() {
		return BOT_NAME;
	}
}



