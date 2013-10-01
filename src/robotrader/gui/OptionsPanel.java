package robotrader.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import robotrader.gui.traders.TradersFilePanel;
import robotrader.gui.traders.TradersService;

import lu.base.iris.AppToolkit;
import lu.base.iris.services.UserPrefService;

/**
 * Options panel for application wide options. 
 * 
 * @author cycle
 *
 */
public class OptionsPanel extends JPanel{
	private static final long serialVersionUID = 7777629748902227280L;
	
	private JCheckBox _hideChartCheckBox;
	private boolean _hideChartsDuringSim = false;
	
	/**
	 * The UserPrefService from which options are retrieved and set.
	 */
	private UserPrefService _ups;
	private TradersService _traderssrv;
	
	
	
	public OptionsPanel(){
		_ups = (UserPrefService)AppToolkit.getService(UserPrefService.class);
		
		this.setLayout(new BorderLayout());
		JTabbedPane jtp = new JTabbedPane();
		
		
		jtp.addTab("View Options", getViewOptionsPanel());
		jtp.addTab("Data Options", getDataOptionsPanel());
		this.add(jtp, BorderLayout.CENTER);
		
		JButton applyButton = new JButton(new confirmAction());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(applyButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.add(buttonPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * Get the "View Options" section.
	 * @return
	 */
	protected JPanel getViewOptionsPanel(){
		JPanel viewPanel = new JPanel();
		
		if(_ups != null){
			String curVal = _ups.getStringValue("Options.HideChartsDuringSimulation", Boolean.toString(false));
			if(curVal.equals(Boolean.toString(true))){
				_hideChartsDuringSim = true;
			}
		}
		
		_hideChartCheckBox = new JCheckBox("Hide charts during simulation (May improve performance)",_hideChartsDuringSim);
		viewPanel.add(_hideChartCheckBox);
		
		return viewPanel;
	}
	
	protected JPanel getDataOptionsPanel(){
		JPanel dataPanel = new JPanel(new GridLayout(2,1));
		
		_traderssrv = (TradersService) AppToolkit.getService(TradersService.class);
		if(_traderssrv != null){
			dataPanel.add(new TradersFilePanel(_traderssrv.getTradersFile()));
		}
		
		return dataPanel;
	}

	/**
	 * Apply the settings in every options section.
	 * @author dells
	 *
	 */
	public class confirmAction extends AbstractAction
	{
		private static final long serialVersionUID = 3219740485289204718L;
		public confirmAction(){
			super("Apply");
		}
		public void actionPerformed(ActionEvent ae) {
			applySettings();
		}
		private void applySettings(){
			if(_ups != null){
				String newVal = Boolean.toString(_hideChartCheckBox.isSelected());
				_ups.setStringValue("Options.HideChartsDuringSimulation",newVal);
			}
		}
	}
}
