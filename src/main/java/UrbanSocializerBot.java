import keyboard.KeyboardFactory;
import model.BotUser;
import model.Project;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import service.abstr.BotUserService;
import service.abstr.ProjectService;
import service.impl.BotUserServiceImpl;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.impl.ProjectServiceImpl;
import storage.Storage;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class UrbanSocializerBot extends TelegramLongPollingBot implements Serializable {
	private static final String BOT_NAME = "JBootCampFirst";
	private static final String BOT_TOKEN = "779792105:AAE1riYVZ5npKvJFX0a5T_owWN2mE2bHKZk";
	private BotUserService botUserService = BotUserServiceImpl.getInstance();
	private ProjectService projectService = ProjectServiceImpl.getInstance();

	UrbanSocializerBot(DefaultBotOptions options) {
		super(options);
	}


	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	public void onUpdateReceived(Update update) {

		List<String> currentContext;
		Message message = update.getMessage();

		if (message != null) {
			String messageFromTelegram = message.getText();
			User userFromTelegram = message.getFrom();
			Integer currentUserId = userFromTelegram.getId();
			Long currentChatId = message.getChatId();
			int contextPosition = 0;

			if ("/start".equals(messageFromTelegram)) {
				if (!botUserService.isUserExistById(currentUserId)) {
					botUserService.addUser(new BotUser(), currentUserId);
				}
				sendMsg(currentChatId, "Добрый день\n");
				BotUser currentUser = botUserService.getUser(currentUserId);

				StringBuilder firstMessage = new StringBuilder("/reg - регистрация\n/info - информация\n/active_projects - проекты\n");
				if (currentUser.isRegCompleated()) {
					firstMessage.append("/add_project - добавить проект");
				}
					sendMsg(currentChatId, firstMessage.toString());

			} else {
				BotUser currentUser = botUserService.getUser(currentUserId);
				currentContext = currentUser.getContext();

				if (currentContext.isEmpty()) {
					switch (messageFromTelegram) {
						case "/reg": {
							sendMsg(currentChatId, "Регистрация.");
							sendMsg(currentChatId, "Пожалуйста, укажите ваш адрес.");
							currentContext.add("/reg");
							break;
						}
						case "/info": {
							sendMsg(currentChatId, "Добро пожаловать.\n" +
									"Здесь вы можете предложить свой проект " +
									"по улучшению городской или районной инфраструктуры.");
							break;
						}
						case "/active_projects": {
							for (Map.Entry<Integer, Project> entry:
								 Storage.PROJECTS_TABLE.entrySet()) {
								sendMsg(currentChatId,"Название: " + entry.getValue().getTitle() +"\n"
								+"Описание: " + entry.getValue().getDescription() +"\n"
								+"Сумма: " + entry.getValue().getAllSum() + "\n"
								+"Собрано: " + entry.getValue().getCurrentSum() + "\n"
								+"Создатель: " + botUserService.getUser(entry.getValue().getAuthorId()).getName());
								SendPhoto sendPhoto = new SendPhoto().setPhoto(entry.getValue().getPhoto()).setChatId(currentChatId);
								try {
									execute(sendPhoto);
								} catch (TelegramApiException e) {
									e.printStackTrace();
								}
							}
							break;
						}
						case "/add_project": {
							if (currentUser.isRegCompleated()){
								sendMsg(currentChatId, "Добавление проекта");
								sendMsg(currentChatId, "Введите название для вашего проекта");
								currentContext.add("/add_project");
							}
							break;
						}
						default: {
							sendMsg(currentChatId, "Неизвестная команда.");
						}

					}
				} else {
					switch (currentContext.get(contextPosition)) {
						case "/reg": {
							if (currentContext.size() <= (contextPosition + 1)) {
								BotUser user = botUserService.getUser(currentUserId);
								user.setAddress(messageFromTelegram);
								sendMsg(currentChatId, "Введите имя:");
								currentContext.add("/name");
							} else {
								switch (currentContext.get(++contextPosition)) {
									case "/name": {

										if (currentContext.size() <= (contextPosition + 1)) {
											BotUser user = botUserService.getUser(currentUserId);
											user.setName(messageFromTelegram);
											sendMsg(currentChatId, "Введите номер:");
											currentContext.add("/number");
										} else {

											switch (currentContext.get(++contextPosition)) {
												case "/number": {
													BotUser user = botUserService.getUser(currentUserId);
													user.setPhoneNumber(messageFromTelegram);
													user.setRegCompleated(true);
													currentContext.clear();
													sendMsg(currentChatId, "Регистрация окончена.");
													sendMsg(currentChatId, "/info - информация\n/active_projects - проекты");
													break;
												}
												default: {
													sendMsg(currentChatId, "Неизвестная команда.");
												}
											}
										}
									}
								}
							}
							break;
						}
						case "/add_project": {

							if (currentContext.size() == (contextPosition+ 1 )) {
								Project project = new Project();
								project.setProjectId(Storage.PROJECT_ID++);
								project.setTitle(messageFromTelegram);
								project.setAuthorId(currentUserId);
								project.setCreateDate(LocalDateTime.now());
								projectService.addProject(project);
								sendMsg(currentChatId, "Введите описание проета:");
								sendMsg(currentChatId, "(Сообщение должно быть " +
										" не больше 100 символов)");
								currentContext.add("/description" + "{" + project.getProjectId() + "}");
							} else {
								switch (extractContextString(currentContext.get(++contextPosition))) {

									case "/description": {
										if (currentContext.size() == (contextPosition+1)) {

											// название не должно быть больше  100 знаков
											if (messageFromTelegram.length() > 100 || messageFromTelegram.isEmpty()) {
												sendMsg(currentChatId, "Описание слишком длинное, введите еще раз:");
											} else {

												Project project = projectService.getProjectById(extractId(currentContext.get(contextPosition)));
												project.setDescription(messageFromTelegram);
												sendMsg(currentChatId, "Введите необходимую к сбору сумму, в рублях:");
												currentContext.add("/sum" + "{" + project.getProjectId() + "}");
											}
										}else {


											switch (extractContextString(currentContext.get(++contextPosition))) {
												case "/sum": {
													if (currentContext.size() == (contextPosition+1)) {
														Project project = projectService.getProjectById(extractId(currentContext.get(contextPosition)));

														project.setAllSum(Integer.parseInt(messageFromTelegram));
														sendMsg(currentChatId,"Приложите фото: ");
														currentContext.add("/photo" + "{" + project.getProjectId() + "}");
													} else {
														switch (extractContextString(currentContext.get(++contextPosition))) {
															case "/photo": {
																Project project = projectService.getProjectById(extractId(currentContext.get(contextPosition)));
																project.setPhoto(message.getPhoto().get(0).getFileId());
																sendMsg(currentChatId,"Добавление проекта завершено.");

																sendMsg(currentChatId,"/reg - регистрация\n/info - информация\n/active_projects - проекты\n/add_project - добавить проект");
																currentContext.clear();
																break;
															}
														}
													}
												}

											}
										}
									}
									break;
								}
							}
							break;
						}
						default: {
							sendMsg(currentChatId, "Неизвестная команда.");
						}
					}
				}
			}
		}
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
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public static int extractId(String s){
		return Integer.parseInt(s.substring(s.lastIndexOf("{")+1, s.lastIndexOf("}")));
	}

	public static String extractContextString(String s){
		return s.substring(0, s.lastIndexOf("{"));
	}



	public String getBotUsername() {
		return BOT_NAME;
	}



	///////////////-=================================================
	////////Методы для сохранения и загрузки данных из storage.USERS_TABLE
	Map<Integer, BotUser> mapSave = Storage.USERS_TABLE;
	private static final String PATH = "C:\\git\\file.txt";

	public void saveFile()
			throws IOException {
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
			os.writeObject(mapSave);
			//	os.close();
			System.out.println("HashMap data is saved.");
		}
	}

	public Map<Integer, BotUser> readFile()
			throws ClassNotFoundException, IOException {
		try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
			return Storage.USERS_TABLE = (Map<Integer, BotUser>) is.readObject();
		}
	}

	/////////////////-==================================
	public synchronized void setButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow keyboardFirstRow = new KeyboardRow();

		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		keyboardFirstRow.add(new KeyboardButton(sendMessage.getText()));
		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	private void sendMsgButton(Long chatId, String buttonText) {
		SendMessage sendMessageButton = new SendMessage();
		sendMessageButton.enableMarkdown(false);
		sendMessageButton.setChatId(chatId);
		sendMessageButton.setText(buttonText);

		try {
			setButtons(sendMessageButton);
			execute(sendMessageButton);

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	public synchronized void setButtons2(SendMessage sendMessage2) {
		ReplyKeyboardMarkup replyKeyboardMarkup2 = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();

		sendMessage2.setReplyMarkup(replyKeyboardMarkup2);
		replyKeyboardMarkup2.setSelective(true);
		replyKeyboardMarkup2.setResizeKeyboard(true);
		replyKeyboardMarkup2.setOneTimeKeyboard(false);

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(new KeyboardButton("/add_project"));
		keyboardFirstRow.add(new KeyboardButton("/show_active_projects"));

		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup2.setKeyboard(keyboard);
	}

	private void sendMsgButton2(Long chatId2, String buttonText2) {
		SendMessage sendMessageButton2 = new SendMessage();
		sendMessageButton2.enableMarkdown(false);
		sendMessageButton2.setChatId(chatId2);
		sendMessageButton2.setText(buttonText2);

		try {
			setButtons2(sendMessageButton2);
			execute(sendMessageButton2);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	////////////----==============================













}



