package ws.lernwerkstatt.cal.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

public class Utils {
	public static final String DATE_FORMAT_STRING = "d.M.yy";
	public static final String SHORTEN_POSTFIX = "...";
	public static final String DEL = "entf";
	public static final String EDIT = "edit";
	public static final String NEW = "neu";
	public static final String OK = "ok";
	public static final String SAVE = "speichern";
	public static final String FILTER = "filter";
	public static final String CANCEL = "abbrechen";
	public static final String ADD = "anlegen";
	public static final String CHANGE = "&auml;ndern";
	public static final String PRINT = "drucken";
	public static final DateTimeFormat DATE_FORMAT = DateTimeFormat
			.getFormat(DATE_FORMAT_STRING);
	public static final Format DATEBOX_FORMAT = new DateBox.DefaultFormat(
			DATE_FORMAT);
	public static final int BUTTON_SPACING = 5;
	public static final int FIELD_HEIGHT = 20;
	public static final int BUTTON_WIDTH = 80;
	public static final int ROW_HEIGHT = 40;
	public static final int LISTBOX_WIDTH = 135;

	
	public static boolean isNotEmpty(String text) {
		return text != null && text.matches(".*\\w.*");
	}

	public static boolean isEmpty(String text) {
		return !isNotEmpty(text);
	}

	public static String shorten(String text, int length) {
		if (text == null || text.length() <= length) {
			return text;
		}
		if (length < SHORTEN_POSTFIX.length()) {
			return text.substring(0, length);
		}
		return text.substring(0, length - SHORTEN_POSTFIX.length())
				+ SHORTEN_POSTFIX;
	}

//	public static void formatLeftCenter(Panel panel, Widget widget,
//			int width, int height) {
//		format(panel, widget, width, height, HasHorizontalAlignment.ALIGN_LEFT,
//				HasVerticalAlignment.ALIGN_MIDDLE);
//	}
//
//	public static void formatRightCenter(Panel panel, Widget widget,
//			int width, int height) {
//		format(panel, widget, width, height,
//				HasHorizontalAlignment.ALIGN_RIGHT,
//				HasVerticalAlignment.ALIGN_MIDDLE);
//	}
//
//	public static void formatLeftTop(Panel panel,
//			Widget widget, int width, int height) {
//		format(panel, widget, width, height,
//				HasHorizontalAlignment.ALIGN_LEFT,
//				HasVerticalAlignment.ALIGN_TOP);
//	}
//
//	public static void formatCenter(Panel panel,
//			Widget widget, int width, int height) {
//		format(panel, widget, width, height,
//				HasHorizontalAlignment.ALIGN_CENTER,
//				HasVerticalAlignment.ALIGN_MIDDLE);
//	}
	
//	public static void format(Panel panel, Widget widget, int widthPx,
//			int heightPx, final HorizontalAlignmentConstant horizontalAlign,
//			final VerticalAlignmentConstant verticalAlign) {
//		panel.add(widget);
//		final String width = "" + widthPx + "px";
//		final String height = "" + heightPx + "px";
//		widget.setSize(width , height);
//		if (panel instanceof CellPanel) {
//			CellPanel cPanel = (CellPanel) panel;
//			cPanel.setCellVerticalAlignment(widget, verticalAlign);
//			cPanel.setCellHorizontalAlignment(widget, horizontalAlign);
//			cPanel.setCellWidth(widget, width);
//		}
//	}
//
//	public static int min(int int1, int int2) {
//		if (int1 < int2) {
//			return int1;
//		}
//		return int2;
//	}


	


}
