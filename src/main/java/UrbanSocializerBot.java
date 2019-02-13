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

		List<String> currentContext = new ArrayList<>();
		Message message = update.getMessage();

		if (message != null & message.hasText()) {
			String messageFromTelegram = message.getText();
			User userFromTelegram = message.getFrom();
			String telegramUserName = userFromTelegram.getFirstName();
			Integer currentUserId = userFromTelegram.getId();
			Long currentChatId = message.getChatId();
			//здесть не прававельно создается екземпляр BotUser
			BotUser botUser = new BotUser();
			if ("/start".equals(messageFromTelegram)) {
				botUserService.addUser(botUser, currentUserId);
				sendMsg(currentChatId, "Привет " + telegramUserName + ", вы впервые у нас, добавляем вас в базу");
				sendMsg(currentChatId, "/reg - регистрация\n/info - информация");

			} else {


				BotUser currentUser = botUserService.getUser(currentUserId);
				currentContext = currentUser.getContext();


				if (currentContext.isEmpty()) {

					switch (messageFromTelegram) {
						case "/reg": {
							sendMsg(currentChatId, "регстрация");
							sendMsg(currentChatId, "Введитте имя");
							setContextToUser(currentUserId, "/reg");
							break;
						}
						case "/info": {
							sendMsg(currentChatId, "информация");
							setContextToUser(currentUserId, "/info");
							break;
						}
						default: {
							sendMsg(currentChatId, "неизвестная команда");
						}
					}


				} else {
					int contextPosition = 0;
					switch (currentContext.get(contextPosition++)) {
						case "/reg": {

							if (currentContext.size() == contextPosition) {
								//ДОБАВЛЕНИЕ  ИМЕНИ
							} else {
								switch (currentContext.get(contextPosition++)) {
								}
							}
							break;
						}
						case "/info": {


							break;
						}
						case "/list_activities": {
							// проверка роли
							sendMsg(currentChatId, "список");
							break;
						}
						case "show_active_projects": {
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
							keyboardFactory.createKeyboardRow(2,3,true,true,true, "test1", "test2", "test3");

							//projectDao.getProjectById()
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
	//private void getProjectList(int indexFrom, int indexTo){ }
	public String getBotUsername() {
		return BOT_NAME;
	}
}



/*
*
* public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		//достаем userId и userName из полученного сообщения
		String userName = update.getMessage().getFrom().getFirstName();
		Integer userId = update.getMessage().getFrom().getId();
		String message_text = update.getMessage().getText();
		long chat_id = update.getMessage().getChatId();
		SendMessage message = new SendMessage() // Create a message object object
				.setChatId(chat_id)
				.setText(message_text);


		switch (message_text) {
			case "/start":
				if (BotUserServiceImpl.getInstance().isUserExistById(userId)) {
					message.setText(userName + userId);
				}
				try {
					execute(message);  // Sending our message object to USERS_TABLE
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

				break;

			case "/login":
				//пробуюю проверить зареган ли клиен
				// и если нет то внести в базу
				if (BotUserServiceImpl.getInstance().isUserExistById(userId)) {
					message.setText(userName + userId);
				} else {
					//create user, set user fields, and add to the storage.
					BotUser botUser = new BotUser();
					botUser.setUserId(userId);
					botUser.setUserName(userName);
					botUser.setUserAddress("user address");
					BotUserServiceImpl.getInstance().addUser(botUser, userId);
					USERS_TABLE.put(botUser.getUserId(), botUser);
				}

				try {
					execute(message);  // Sending our message object to USERS_TABLE
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

				break;

			case "/createProject":

				List<KeyboardRow> keyboard1 = new ArrayList<>();
				KeyboardRow keyboardRowOne = new KeyboardRow();
				ReplyKeyboard replyKeyboard1 = new ReplyKeyboardRemove();
				keyboardRowOne.add("repKeyRemove");
				keyboard1.add(keyboardRowOne);
				//ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

				message = new SendMessage().setChatId(chat_id).setText("test");
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

				break;
			case "/help":
				// выводит список доступных команд для для пользователя
				message = new SendMessage().setChatId(chat_id).setText(
						"See list of the command\n" +
								"/help\n" +
								"/projectList\n" +
								"/pay\n" +
								"/menu\n" +
								"/beck" +
								"/createProject");


				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;
			case "/test":
				// пока что сдесть просто пробуюю как работают кнопки
				Message message1 = new Message();
				SendMessage sendMessage1 = new SendMessage();
				sendMessage1.enableMarkdown(true);

				// Создаем клавиуатуру
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				sendMessage1.setReplyMarkup(replyKeyboardMarkup);

				// Опционально. Этот параметр нужен, чтобы показывать
				// клавиатуру только определённым пользователям.
				// Цели: 1) пользователи, которые были @упомянуты
				// в поле text объекта Message;
				// 2) если сообщения бота является ответом
				// (содержит поле reply_to_message_id), авторы этого сообщения.
				replyKeyboardMarkup.setSelective(true);

				// Опционально. Указывает клиенту подогнать
				// высоту клавиатуры под количество кнопок
				// (сделать её меньше, если кнопок мало).
				// По умолчанию False, то есть клавиатура
				// всегда такого же размера, как и стандартная клавиатура устройства.
				replyKeyboardMarkup.setResizeKeyboard(true);


				// iОпционально. Указывает клиенту скрыть клавиатуру после
				// использования (после нажатия на кнопку). Её по-прежнему
				// можно будет открыть через иконку в поле ввода сообщения. По умолчанию False.
				replyKeyboardMarkup.setOneTimeKeyboard(true);

				// Создаем список строк клавиатуры
				List<KeyboardRow> keyboard = new ArrayList<>();

				// Первая строчка клавиатуры
				KeyboardRow keyboardFirstRow = new KeyboardRow();
				// Добавляем кнопки в первую строчку клавиатуры
				keyboardFirstRow.add("project");
				keyboardFirstRow.add("help");
				keyboardFirstRow.add("help2");


				// Вторая строчка клавиатуры
				KeyboardRow keyboardSecondRow = new KeyboardRow();
				// Добавляем кнопки во вторую строчку клавиатуры
				keyboardSecondRow.add("donate");
				keyboardSecondRow.add("contact");
				keyboardSecondRow.add("contact2");

				//Third row keyboard
				KeyboardRow keyboardThirdRow = new KeyboardRow();

				// Add buttons into the third row of the keyboard

				keyboardThirdRow.add("exit");
				keyboardThirdRow.add("beck");

				// Добавляем все строчки клавиатуры в список
				keyboard.add(keyboardFirstRow);
				keyboard.add(keyboardSecondRow);
				keyboard.add(keyboardThirdRow);
				// устанваливаем этот список нашей клавиатуре
				replyKeyboardMarkup.setKeyboard(keyboard);

				sendMessage1.setChatId(message.getChatId().toString());
				sendMessage1.setReplyToMessageId(message1.getMessageId());
				sendMessage1.setText("project");
				try {
					execute(sendMessage1);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

				break;
			case "/project1":

				//	List<KeyboardButton> keyboardButtons = new ArrayList<>();
				//	KeyboardButton keyboardButton = new KeyboardButton();
				//	KeyboardRow keyboardRow = new KeyboardRow();
				//	ReplyKeyboard replyKeyboard = new ReplyKeyboardMarkup();

			case "/exit":
				//ReplyKeyboardMarkup replyKeyboardMarkup1 = createYesOrNoKeyboard();

				try {

					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;
		}
	}*/