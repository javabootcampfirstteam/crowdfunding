import abstr.UserService;
import service.impl.BotUserServiceImpl;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.*;
import java.util.HashMap;

public class UrbanSocializerBot extends TelegramLongPollingBot implements Serializable {

	private static final String BOT_NAME = "JBootCampFirstBot";
	public static final String BOT_TOKEN = "779792105:AAE1riYVZ5npKvJFX0a5T_owWN2mE2bHKZk";
	private UserService userService = BotUserServiceImpl.getInstance();
	HashMap hashMap = new HashMap();
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("C:\\java\\telegram-bot\\objects.dat"));
	ObjectInputStream in =  new ObjectInputStream (new FileInputStream("C:\\java\\telegram-bot\\objects.dat"));


	public UrbanSocializerBot(DefaultBotOptions options) throws IOException {
		super(options);
	}


	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}


	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			// Set variables

			//достаем userId и userName из полученного сообщения
			int userId = update.getMessage().getFrom().getId();
			String userName = update.getMessage().getFrom().getFirstName();

			String message_text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();

			SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText(message_text);

			//читаю в hashmap данные из файла
//			try {
//				hashMap = (HashMap) in.readObject();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}


			//сверяем полученный userId с hashmap key с имеющимися в базе
			if(hashMap.containsKey(userId)){
				message = new SendMessage().setChatId(chat_id).setText(userName + " а я вас знаю!");
			}
			else {
				message = new SendMessage().setChatId(chat_id).setText("О. Вы новенький.\n" + "Привет " + userName);
				//записываю в hashmap полученные данные о пользователе
				hashMap.put(userId, userName);

				//пробую сохранить hashmap во внешний файл
				try {
					out.writeObject(hashMap);
				} catch (IOException e) {
					e.printStackTrace();
				}
				//закрываю поток записи
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(update.getMessage().getText().equals("test")){
				message = new SendMessage().setChatId(chat_id).setText("test пройден");
			}

			try {
				execute(message);  // Sending our message object to USERSTABLE
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}

		}

	}

	public String getBotUsername() {
		return BOT_NAME;
	}
}
