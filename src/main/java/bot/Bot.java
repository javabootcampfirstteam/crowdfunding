package bot;

import model.BotUser;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.abstr.BotUserService;
import service.impl.BotUserServiceImpl;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static String BOT_NAME=BotOptions.BOT_NAME;
    private static String BOT_TOKEN=BotOptions.BOT_TOKEN;


    BotUserService botUserService = BotUserServiceImpl.getInstance();

    public Bot(DefaultBotOptions options) {
        super(options);
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();


        if (message != null & message.hasText()) {
            String messageFromTelegram = message.getText();
            User userFromTelegram = message.getFrom();
            String telegramUserName = userFromTelegram.getFirstName();
            Integer currentUserId = userFromTelegram.getId();
            Long currentChatId = message.getChatId();
            int contextPosition = 0;

            if ("/start".equals(messageFromTelegram)) {
                if (!botUserService.isUserExistById(currentUserId)) {
                    botUserService.addUser(currentUserId, new BotUser(telegramUserName));
                    sendMsg(currentChatId, "Добрый день, вы впервые у нас, добавляем вас в базу\n" + currentUserId  );
                } else {
                    sendMsg(currentChatId, "Привет " + telegramUserName + ", Мы уже знакомы с вами!");
                }
                sendMsg(currentChatId, "/reg - регистрация\n/info - информация");

            } else {

                BotUser currentUser = botUserService.getUser(currentUserId);
                List<String> currentContext = currentUser.getContext();

                if (!messageFromTelegram.isEmpty()){
                    switch (currentContext.get(contextPosition)) {
                        case "/reg": {
                            sendMsg(currentChatId, "Пожалуйста укажите город где вы живете.");
                            currentContext.set(contextPosition++,"/regCity");
                            break;
                        }
                        case "/regCity": {
                            String cityReg = message.getText();
                            currentUser.setUserAddress(cityReg);
                            sendMsg(currentChatId, "Пожалуйста укажите свой телефон.");
                            currentContext.set(contextPosition++,"/regPhone");
                            break;
                        }
                        case "/regPhone": {
                            String phoneReg = message.getText();
                            currentUser.setPhoneNum(phoneReg);
                            sendMsg(currentChatId, "Добро пожаловать: " + telegramUserName + "\n"
                                    + "из города " + currentUser.getUserAddress() + ", тел. номер: "
                                    + currentUser.getPhoneNum());
                            currentContext.set(contextPosition++, "/final");
                            break;
                        }
                        case "/final": {

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
                  //  int contextPosition = 0;
                    switch (currentContext.get(contextPosition++)) {

                        case "/info": {
                            break;
                        }
                        case "/list_activities": {
                            // проверка роли
                            sendMsg(currentChatId, "список");
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

    public String getBotUsername() {
        return BOT_NAME;
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
}
