import model.BotUser;
import model.Project;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static storage.Storage.*;


public class UrbanSocializerBot extends TelegramLongPollingBot implements Serializable {
	private static final String BOT_NAME = "JBootCampFirst";
	public static final String BOT_TOKEN = "779792105:AAE1riYVZ5npKvJFX0a5T_owWN2mE2bHKZk";
	BotUserService botUserService = BotUserServiceImpl.getInstance();


	public UrbanSocializerBot(DefaultBotOptions options) throws IOException {
		super(options);
	}


	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	///////////////-=================================================
	////////Методы для сохранения и загрузки данных из storage.USERS_TABLE
	Map<Integer, BotUser> mapSave = USERS_TABLE;
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
			return USERS_TABLE = (Map<Integer, BotUser>) is.readObject();
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
				try {
					readFile();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (!botUserService.isUserExistById(currentUserId)) {
					botUserService.addUser(new BotUser(), currentUserId);
					sendMsg(currentChatId, "Добрый день, вы впервые у нас, добавляем вас в базу\n");
					sendMsgButton(currentChatId, "/reg");
				} else {
					sendMsg(currentChatId, "Привет " + telegramUserName + ", Мы уже знакомы с вами!");
					sendMsgButton(currentChatId, "/info");
				}
			} else {
				BotUser currentUser = botUserService.getUser(currentUserId);
				currentContext = currentUser.getContext();

				if (currentContext.isEmpty()) {
					switch (messageFromTelegram) {
						case "/reg": {
							sendMsg(currentChatId, "Пожалуйста укажите город где вы живете.");
							currentContext.add("/regCity");
							//sendMsgButton(currentChatId, "");
							break;
						}
						case "/info": {
							sendMsg(currentChatId, "Добро пожаловать.\n" +
									"Здесь вы можете предложить свой проект " +
									"по улучшению городской или районной инфраструктуры. /add_projects\n"
									+ "или ознакомится с текущими предложениями. /show_active_projects");
							sendMsgButton(currentChatId, "/add_projects");
							sendMsgButton2(currentChatId, "/show_active_projects");
							break;
						}
						case "/active_projects": {
							sendMsg(currentChatId, "активные");
							break;
						}
						case "add_project": {

							sendMsg(currentChatId, "Добавление проекта");
							sendMsg(currentChatId, "Введите название для вашего проекта");
							currentContext.add("/add_project");

							break;
						}

						case "/list_projects": {
							sendMsg(currentChatId, "Здесь будут перечислены текущие проекты.");
							break;
						}

						case "/save": {
							try {
								saveFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}

						case "/load": {
							try {
								readFile();
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
							break;
						}

						default: {
							sendMsg(currentChatId, "неизвестная команда");
						}

					}
				} else {
					switch (currentContext.get(contextPosition)) {
						case "/regCity": {
							if (currentContext.size() == ++contextPosition) {
								String cityReg = message.getText();
								currentUser.setUserAddress(cityReg);
								sendMsg(currentChatId, "Пожалуйста укажите свой телефон.");
								currentContext.add("/regPhone");
								//		sendMsgButton(currentChatId, " ");
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
													"Теперь посмотрите что люди предлагают.\n ");
											sendMsgButton(currentChatId, "/info");

											//блок сохранения данных во внешний файл.
											try {
												saveFile();
											} catch (IOException e) {
												e.printStackTrace();
											}
											break;
										}
										break;
									}

								}


							}
							break;
						}

						default: {
							sendMsg(currentChatId, "неизвестная команда");
						}

						case "/add_project": {
							Project project = new Project();

							if (currentContext.size() <= ++contextPosition) {

								project.setProjectId(Storage.PROJECT_ID++);
								project.setTitle(messageFromTelegram);
								project.setAuthorId(currentUserId);
								project.setCreateDate(LocalDateTime.now());
								sendMsg(currentChatId, "Введите описание проета:");
								currentContext.add("/addP__title");
							} else {
								switch (currentContext.get(contextPosition)) {

									case "/addP_title": {
										if (currentContext.size() <= ++contextPosition) {
											sendMsg(currentChatId, "...Сообщение должно быть " +
													" не больше 100 символов");
											// название не должно быть больше  100 знаков
											if (messageFromTelegram.length() > 100 || messageFromTelegram.isEmpty()) {
												sendMsg(currentChatId, "the title is too long, or empty, you can try again! ");
												currentContext.add("/addP_title");
											} else {
												project.setTitle(messageFromTelegram);
												currentContext.add("/addP_description");
											}
											switch (currentContext.get(contextPosition)) {
												case "/addP_description": {
													// описанее не должно быть больше  2000 знаков
													if (currentContext.size() <= ++contextPosition) {
														if (messageFromTelegram.length() > 2000 || messageFromTelegram.isEmpty()) {
															currentContext.add("/addP_description");
															sendMsg(currentChatId, "try again");
														}
														project.setDescription(messageFromTelegram);
														sendMsg(currentChatId, "Description add. Now give me  sum in rub that it'll take " +
																"to do it" +
																"it could be anything in betwixt 1000rub - 1.000.000 rub");
														currentContext.add("/addP_sum");
													} else {
														switch (currentContext.get(contextPosition)) {
															case "/addP_sum": {
																if (currentContext.size() <= ++contextPosition) {
																	if (!messageFromTelegram.isEmpty() && !(Integer.parseInt(messageFromTelegram) < 1000000 && Integer.parseInt(messageFromTelegram) < 1000)) {
																		project.setAllSum(Integer.parseInt(messageFromTelegram));
																		sendMsg(currentChatId, "sum was successfully added");
																		sendMsg(currentChatId, "now you can optionally add photo to your project");
																		currentContext.add("/addP_photo");
																	} else {
																		sendMsg(currentChatId, "Sum too small, or to big");
																		currentContext.add("/addP_photo");

																		switch (currentContext.get(contextPosition)) {
																			case "/addP_photo": {


																				if (currentContext.size() <= ++contextPosition) {
																					// here should be logic for adding photo to the project

																					if (update.hasMessage() && update.getMessage().hasPhoto()) {
																						List<PhotoSize> photos = update.getMessage().getPhoto();
																						// Know file_id
																						String file_id = photos.stream()
																								.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
																								.findFirst()
																								.orElse(null).getFileId();
																						// Know photo width
																						int file_width = photos.stream()
																								.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
																								.findFirst()
																								.orElse(null).getWidth();

																						// Know photo height
																						int file_height = photos.stream()
																								.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
																								.findFirst()
																								.orElse(null).getHeight();
																						// Set photo caption
																						String caption = "file_id: " + file_id + "\nwidth: " + Integer.toString(file_width) + "\nheight: " + Integer.toString(file_height);
																						SendPhoto msg = new SendPhoto()
																								.setChatId(currentChatId)
																								.setPhoto(file_id)
																								.setCaption(caption);

																						try {
																							execute(msg); // Call method to send the photo with caption
																						} catch (TelegramApiException e) {
																							e.printStackTrace();
																						}
																					}
																					//добавляем проект в БД
																					currentContext.clear();
																					break;
																				}
																			//	projectService.addProject(project);
																			}
																			default: {

																				sendMsg(currentChatId, "unknown  command");
																			}
																		}
																	}
																}
															}
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



	private void setContextToUser(Integer userId, String context) {
		BotUser currentUser = botUserService.getUser(userId);
		currentUser.getContext().add(context);
	}

	private void removeLastContextElement(Integer userId) {
		BotUser currentUser = botUserService.getUser(userId);
		currentUser.getContext().remove(currentUser.getContext().size());
	}


		private void sendMsg(Long chatId, String txtMessage){
		SendMessage sendMessage=new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(txtMessage);


		try{
		execute(sendMessage);
		}catch(TelegramApiException e){
		e.printStackTrace();
		}
		}


	public String getBotUsername() {
		return BOT_NAME;
	}
		}


