package keyboard;

import org.apache.commons.codec.binary.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import keyboard.Buttons;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class KeyBoard {

	// знаю ... пока что этот класс дно. но у меня на него есть планы..
	public InlineKeyboardMarkup ligar() {
		return null;
	}

	public InlineKeyboardMarkup createFilterKeyboard(String mensaId, LocalDate date, String selectedValue) {
		List<List<InlineKeyboardButton>> rows = new ArrayList<>();
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(rows);
		return inlineKeyboardMarkup;
	}
}
