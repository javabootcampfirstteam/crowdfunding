import abstr.UserService;
import impl.UserServiceImpl;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class UrbanSocializerBot extends TelegramLongPollingBot{

	private static final String BOT_NAME = "urban_socializer_bot";
	public static final String BOT_TOKEN = "726296784:AAG70XT-URXz69YvwPaKNQDivImfZAmFCOQ";
	private UserService userService = new UserServiceImpl();


	public UrbanSocializerBot(DefaultBotOptions options) {
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
			String message_text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();

			SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText(message_text);

			if(update.getMessage().getText().equals("хуй")){
				message = new SendMessage().setChatId(chat_id).setText("сам ты хуй");
			}
			SendAnimation animation = new SendAnimation();
			if(update.getMessage().getText().equals("покажи сиськи")){
				//animation = new SendAnimation().setChatId(chat_id).setAnimation(new File("E:\\crowdfunding\\src\\main\\resources\\giphy.gif"));
			}
			try {
				//execute(animation);
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
