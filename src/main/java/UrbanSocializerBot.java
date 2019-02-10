import abstr.UserService;
import impl.UserServiceImpl;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UrbanSocializerBot extends TelegramLongPollingBot{

	private static final String BOT_NAME = "urban_socializer_bot";
	public static final String BOT_TOKEN = "726296784:AAG70XT-URXz69YvwPaKNQDivImfZAmFCOQ";
	private UserService userService = new UserServiceImpl();


	public UrbanSocializerBot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public String getBotToken() {
		return null;
	}

	public void onUpdateReceived(Update update) {

	}

	public String getBotUsername() {
		return null;
	}
}
