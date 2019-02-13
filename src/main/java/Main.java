import bot.Bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    private static String PROXY_HOST = "193.112.46.246";
    private static Integer PROXY_PORT = 21071 ;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
   //     botOptions.setProxyHost(PROXY_HOST);
   //     botOptions.setProxyPort(PROXY_PORT);
   //     botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        try {
            botapi.registerBot(new Bot(botOptions));
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
