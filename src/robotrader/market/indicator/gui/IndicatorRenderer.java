package robotrader.market.indicator.gui;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.JTable;

import java.awt.Color;
import java.awt.Component;

import robotrader.market.IIndicator;
import robotrader.market.IndicatorToolKit;

import java.text.DateFormat;
import java.util.Date;

/**
 * The Indicator Cell Renderer for the Indicator Table.
 * 
 * @author bob
 */
public class IndicatorRenderer extends DefaultTableCellRenderer 
{
	/**
	 * The serial version ID
	 */
	private static final long serialVersionUID = 7140297889035220995L;

	/**
	 * The date format used
	 */
	private static final DateFormat _df =
		//new SimpleDateFormat("MM.dd EEE");
    DateFormat.getDateInstance(DateFormat.SHORT);
	
	/**
	 * The Dark Green Colour
	 */
	private static final Color DARK_GREEN = new Color(200, 255, 200);
	
	/**
	 * The Light Red Colour
	 */
	private static final Color LIGHT_RED = new Color(255, 200, 200);

	/**
	 * Sets the background colour of a table
	 * cell according to the indicator signal.
	 * GREEN indicates buy, RED indicates sell.
	 */
	public Component getTableCellRendererComponent(
		JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column) 
	{
		super.getTableCellRendererComponent(
			table,
			value,
			isSelected,
			hasFocus,
			row,
			column);
		if (column > 2) {
			if (value == null) {
				setBackground(Color.gray);
				return this;
			}
			int v = Integer.parseInt((String) value);
			switch (v) {
				case IIndicator.BUY :
					setBackground(Color.green);
					break;
				case IIndicator.SELL :
					setBackground(Color.red);
					break;
				case IIndicator.HOLD :
					setBackground(Color.white);
					break;
				case IIndicator.OVERSOLD :
					setBackground(DARK_GREEN);
					break;
				case IIndicator.OVERBOUGHT :
					setBackground(LIGHT_RED);
					break;
				default :
					setBackground(Color.gray);
			}
			setText(IndicatorToolKit.toString(v));
		} else {
			if (value instanceof Date) {
				setText(_df.format((Date) value));
			}

			setBackground(Color.gray);
		}
		return this;
	}
}
