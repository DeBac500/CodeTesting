package robotrader.gui.reports;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import robotrader.engine.event.Event;
import robotrader.engine.event.IObserver;
import robotrader.gui.EngineService;
import robotrader.gui.HtmlReport;

import lu.base.iris.AppToolkit;
import lu.base.iris.gui.BaseGUIModule;
import lu.base.iris.gui.DefaultViewComponent;
import lu.base.iris.services.TranslatedAction;

/**
 * A report module which is responsible for
 * creating, displaying and saving of html reports.
 * 
 * @author xcapt
 * @author klinst
 */
public class ReportModule extends BaseGUIModule 
{
  /**
   * A single list action as an abstract action
   * array
   */
	private AbstractAction[] _actions;
  
  /**
   * The report jpanel
   */
	private JPanel _rpane;
	
	/**
	 * The current selected report.
	 */
	private HtmlReport _currentReport;
	
	/**
	 * Action for saving the current report.
	 */
	private saveReportAction _saveReport = new saveReportAction();
  
	/**
	 * Gets the engine service from the application toolkit.
   * Adds a new Observer to the service. If the engine
   * has been stopped then a new Html report is generated
   * with the service data model. The report is added
   * to the report service.
   *   
   * @see lu.base.iris.IModule#init()
	 */
	public boolean init() {
		_rpane = new JPanel(new BorderLayout());
		
		final EngineService srv = (EngineService)AppToolkit.getService(EngineService.class);
		srv.addObserver(
				new IObserver() {
					public void onStopped() {
						HtmlReport report = new HtmlReport(srv.getModel());
						ReportService rsrv = (ReportService)AppToolkit.getService(ReportService.class);
						rsrv.addReport(report);
					}
			        public void onUpdate(Event evt){}
					public void onStarted(){}
				}
		);
		
		return super.init();
	}

	/**
     * Constructs the report module.
	 */
	public ReportModule() 
	{
		super("ReportModule");
	}

	/**
     * Gets a List Action as a 1D array
     * 
     * @return A list action array with 1 member
	 * @see lu.base.iris.gui.AbstractSwingModule#getActions()
	 */
	public AbstractAction[] getActions() {
		if (_actions == null) {
			_actions = new AbstractAction[] { new ListAction()};
		}
		return _actions;
	}

  /**
   * The List Action displays all the
   * generated reports.
   *  
   * @author xcapt
   * @author klinst
   */
	public class ListAction extends TranslatedAction 
	{
			/**
	     * The Serial ID.
	     */
	    private static final long serialVersionUID = 1312715042214164771L;
	    
	    /**
	     * Creates a new list action. 
	     */
	    ListAction() 
	    {
				super(getRef() + ".list");
		}

	    /**
	     * Gets the Report Service from the
	     * application toolkit. Creates a new
	     * default view component with the
	     * report panel containing the list
	     * model of the report sevice. Creates
	     * a new window and shows it.
	     */
		public void execute() {
			ReportService srv =
				(ReportService) AppToolkit.getService(ReportService.class);

			DefaultViewComponent c =
				new DefaultViewComponent(
					getReportPanel(srv.getListModel()),
					"Report List");
			getWindowManager().makeWindow(c).setVisible(true);
		}
		
	    /**
	     * @return true
	     */
	    public boolean isEnabled() {
				return true;
		}
	}

  /**
   * Constructs the report panel. A list
   * is build using the list model, the
   * html report list renderer and the
   * list selector. 
   *  
   * @param lmodel The list model of reports
   * @return The JScrollPane with the report panels
   */
	JPanel getReportPanel(ListModel lmodel) {
		JPanel pane = new JPanel(new BorderLayout());
		if(lmodel.getSize() > 0){
			JList list = new JList(lmodel);
			list.setCellRenderer(new HTMLReportListRenderer());
			list.addListSelectionListener(new ListSelector());	
			JScrollPane spane = new JScrollPane(list);
			
			JButton saveReportButton = new JButton(_saveReport);
			saveReportButton.setHorizontalAlignment(JButton.CENTER);
			_saveReport.setEnabled(false);

			JPanel buttonPanel = new JPanel();
			buttonPanel.add(saveReportButton);
			buttonPanel.setMaximumSize(saveReportButton.getPreferredSize());
			
			JPanel left = new JPanel();
			left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
			left.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
			
			left.add(spane);
			left.add(buttonPanel);
			
			pane.add(left, BorderLayout.WEST);
			pane.add(_rpane,BorderLayout.CENTER);
			pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			pane.setPreferredSize(new Dimension(760,500));
		}
		else{
			String noReportMessage = "<html>No reports have been generated yet. <br> A report will be generated when you run a simulation.</html>";
			JLabel messageLabel = new JLabel(noReportMessage);
			messageLabel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			pane.add(messageLabel);
		}
		return pane;
	}

  /**
   * Renders the HMTL Report List.
   * 
   * @author xcapt
   * @author klinst
   */
	class HTMLReportListRenderer extends DefaultListCellRenderer 
    {
		/**
	     * The Serial ID
	     */
	    private static final long serialVersionUID = -8360467471869962640L;
	
	    /**
	     * Sets the text of the label as the
	     * file name of the report service.
	     * 
	     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	     */
		public Component getListCellRendererComponent(
			JList arg0,
			Object arg1,
			int arg2,
			boolean arg3,
			boolean arg4) {
			Component c =
				super.getListCellRendererComponent(
					arg0,
					arg1,
					arg2,
					arg3,
					arg4);
				HtmlReport report = (HtmlReport) arg1;

			((JLabel) c).setText(report.createFileName());
			return c;
		}
	}
	
  /**
   * 
   * @author klinst
   */
	class ListSelector implements ListSelectionListener
	{
	  private int previous = -1;
    
	    /**
	     * Constructor. 
	     */
		ListSelector()
		{}

	    /**
	     * Saves the selected reports to files.
	     * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent arg0) {
			if (arg0.getValueIsAdjusting()){
				return;
			}
			int idx = arg0.getFirstIndex();
			if (idx == previous){
				idx = arg0.getLastIndex();
			}
			previous = idx;
			//final HtmlReport report = (HtmlReport)((JList)arg0.getSource()).getModel().getElementAt(idx);
			_currentReport = (HtmlReport)((JList)arg0.getSource()).getModel().getElementAt(idx);
			_saveReport.setEnabled(true);
			
			try {
				_rpane.removeAll();
				_rpane.add(_currentReport.makePanel(_currentReport.getContent()),BorderLayout.CENTER);
				_rpane.revalidate();
			} 
			catch (IOException e) {
				JOptionPane.showMessageDialog(
						//dialog,
						null,
						"File not saved: " + e);
			}
		}
	}
	
	protected class saveReportAction extends AbstractAction {
		/**
         * The Serial ID
         */
        private static final long serialVersionUID = -7957588316581024333L;

        public saveReportAction(){
        	super();
        	putValue(Action.NAME, "Save HTML");
        }
        /**
         * Saves the reports html to file
         */
        public void actionPerformed(ActionEvent arg0) {
					JFileChooser dlg = new JFileChooser(".");
					dlg.setDialogTitle(
						"Select output directory for " + _currentReport.createFileName());
					dlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					if (dlg.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						String path =
							dlg.getSelectedFile().getPath() + _currentReport.createFileName();

						File file = new File(path);
						try {
							_currentReport.saveToFile(file, _currentReport.getContent());
							JOptionPane.showMessageDialog(
								//dialog,
								null,
								"File saved to " + path);
						} 
						catch (IOException e) {
							JOptionPane.showMessageDialog(
								//dialog,
								null,
								"File not saved: " + e);
						}
					}
		}
	};
}
