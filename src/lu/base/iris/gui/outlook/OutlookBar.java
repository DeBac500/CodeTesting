package lu.base.iris.gui.outlook;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;


/**
 * Implements Outlook like bar with sweep animation.
 * <p>
 * Example of the usage:
 * <pre>
 * // Creates outlook bar
 * OutlookBar bar = new OutlookBar();
 *
 * // Initializes outlook bar content
 * bar.setAnimationSpeed(50);
 * bar.addPanel("Panel", new JPanel());
 * bar.addPanel("Button", new JButton("xxx"));
 * bar.addPanel("Label", new JLabel("xxx"));
 * bar.addPanel("Text", new JTextPane());
 * bar.addPanel("Auto Panel");
 * bar.removePanel(0);
 * </pre>
 * 
 * @author xcapt
 * @author klinst
 */
public class OutlookBar extends JComponent implements ActionListener 
{
  /**
   * Serial ID 
   */
  private static final long serialVersionUID = 7962103839355094027L;


  /**
   * The list of contained items.
   */
  private ArrayList items = new ArrayList();


  /**
   * The current selected item.
   */
  private int currentIndex = -1;


  /**
   * The speed of sweep animation.
   */
  private int tempo = 10;


  /**
   * Constructs this bar with default parameters.
   */
  public OutlookBar() 
  {
    this.setLayout(new BorderLayout());
  }

  /**
   * Performs actions from user inteface and local timers.
   * @param e an object which describes occured event.
   */
  public void actionPerformed(ActionEvent e) 
  {
    if (e.getSource() instanceof JButton) 
    {
      int index = getPanelIndex(((JButton) e.getSource()).getText());
      if (currentIndex >= 0 && currentIndex != index)
        sweepBar(index);
    }
  }

  /**
   * Gets the current animation speed.
   * @result the current speed of sweep animation in ms.
   */
  public int getTempo() 
  {
    return tempo;
  }


  /**
   * Sets the new animation speed.
   * @param animationSpeed the new speed of sweep animation in ms.
   */
  public void setTempo(int tempo) 
  {
    this.tempo = tempo;
  }


  /**
   * Gets selected panel in this bar.
   * @result an index of current selected panel.
   */
  public int getSelectedIndex() 
  {
    return this.currentIndex;
  }

  /**
   * Sets current selected item of this bar.
   * @param index an index of new selected panel.
   */
  public void setSelectedIndex(int index) 
  {
    if (currentIndex != index) {
      currentIndex = index;
      updateContent();
    }
  }

  /**
   * Gets an index of panel with specified name.
   * @param the name of a existed panel.
   * @result an index of panel with specified name.
   */
  public int getPanelIndex(String name) 
  {
    for (int i = 0; i < items.size(); i++) 
    {
      OutlookBarItem current = (OutlookBarItem) items.get(i);
      if (current.getButton().getText().equals(name))
        return i;
    }
    return -1;
  }

  /**
   * Gets an index of panel with specified component.
   * @param component the component of a existed panel.
   * @result an index of panel with specified component.
   */
  public int getPanelIndex(Component component) 
  {
    for (int i = 0; i < items.size(); i++) 
    {
      OutlookBarItem current = (OutlookBarItem) items.get(i);
      if (current.getComponent() == component)
        return i;
    }
    return -1;
  }

  /**
   * Gets a count of current available panels.
   * @result a count of available panels.
   */
  public int getPanelCount() 
  {
    return items.size();
  }

  /**
   * Gets a panel with specified index.
   * @param an index of a existed panel.
   * @result a panel with specified index.
   */
  public Component getPanel(int index) {
    return ((OutlookBarItem) items.get(index)).getComponent();
  }

  /**
   * Gets a panel with specified name.
   * @param the name of a existed panel.
   * @result a panel with specified name.
   */
  public Component getPanel(String name) 
  {
    for (int i = 0; i < items.size(); i++) 
    {
      OutlookBarItem current = (OutlookBarItem) items.get(i);
      if (current.getButton().getText().equals(name))
        return current.getComponent();
    }
    return null;
  }


  /**
   * Gets a panel title with specified panel index.
   * @param an index of a existed panel.
   * @result a title of panel with specified index.
   */
  public String getPanelTitle(int index) 
  {
    return ((OutlookBarItem) items.get(index)).getButton().getText();
  }

  /**
   * Adds a panel with specified name.
   * @param name a name of new panel.
   * @result a JPanel component of new panel.
   */
  public Component addPanel(String name) {
    return addPanel(name, new JPanel());
  }

  /**
   * Adds a panel with specified name.
   * @param name a name of new panel.
   * @param component a component of new panel.
   * @result a JPanel component of new panel.
   */
  public Component addPanel(String name, Component component) 
  {
    OutlookBarItem item = new OutlookBarItem(name, component);


    items.add(item);
    item.getButton().addActionListener(this);
    if (currentIndex < 0) currentIndex = 0;
    updateContent();


    return component;
  }


  /**
   * Removes panel with specified index.
   * @param index an index of removing panel.
   */
  public void removePanel(int index) 
  {
    if (currentIndex >= index) currentIndex--;
    if (currentIndex < 0 && items.size() > 0)
      currentIndex = 0;
    items.remove(index);
    updateContent();
  }


  /**
   * Removes panel with specified name.
   * @param name a name of removing panel.
   */
  public void removePanel(String name) {
    removePanel(getPanelIndex(name));
  }


  /**
   * Removes panel with specified component.
   * @param component a component of removing panel.
   */
  public void removePanel(Component component) {
    removePanel(getPanelIndex(component));
  }


  /**
   * Updates content of this bar.
   */
  private void updateContent() {
    super.removeAll();


    if (items.size() >= 0 && currentIndex >= 0 && currentIndex < items.size()) {
      JPanel topPanel = null;
      JPanel bottomPanel = null;
      int topItems = currentIndex + 1;
      int bottomItems = items.size() - topItems;

      if (topItems > 0) {
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(topItems, 1));
        super.add(topPanel, BorderLayout.NORTH);
      } else
        topPanel = null;

      if (bottomItems > 0) {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(bottomItems, 1));
        super.add(bottomPanel, BorderLayout.SOUTH);
      }

      super.add(((OutlookBarItem) items.get(currentIndex)).getComponent(),
        BorderLayout.CENTER);

      for (int i = 0; i < items.size(); i++) {
        OutlookBarItem current = (OutlookBarItem) items.get(i);

        if (i <= currentIndex)
          topPanel.add(current.getButton());
        else
          bottomPanel.add(current.getButton());
      }
    }

    this.revalidate();
    this.repaint();
  }

  /**
   * Updates Look&Feel of user interface.
   */
  public void updateUI() 
  {
    for (int i = 0; i < items.size(); i++) 
    {
      OutlookBarItem current = (OutlookBarItem) items.get(i);
      SwingUtilities.updateComponentTreeUI(current.getButton());
      if (current.getComponent() instanceof JComponent)
        SwingUtilities.updateComponentTreeUI(
          (JComponent) current.getComponent());
    }
    super.updateUI();
  }

  /**
   * Starts to sweep current this OutlookBar content. It adds the
   * specialized sweep panel and creates the sweep timer.
   */
  private void sweepBar(int index) 
  {
    super.removeAll();

    JPanel topPanel = new JPanel();
    JPanel centralPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    for (int i = 0; i < items.size(); i++) 
    {
     OutlookBarItem current = (OutlookBarItem) items.get(i);
      JButton button = new JButton(current.getButton().getText());
      if (i <= index && i <= currentIndex)
        topPanel.add(button);
      else if (i > index && i > currentIndex)
        bottomPanel.add(button);
      else
        centralPanel.add(button);
    }

    if (topPanel.getComponentCount() > 0) {
      topPanel.setLayout(new GridLayout(topPanel.getComponentCount(), 1));
      super.add(topPanel, BorderLayout.NORTH);
    }

    if (bottomPanel.getComponentCount() > 0) {
      bottomPanel.setLayout(new GridLayout(bottomPanel.getComponentCount(), 1));
      super.add(bottomPanel, BorderLayout.SOUTH);
    }

    if (centralPanel.getComponentCount() > 0) {
      centralPanel.setLayout(new GridLayout(centralPanel.getComponentCount(), 1));
    }

    Component first = ((OutlookBarItem) items.get(currentIndex)).getComponent();
    Component second = ((OutlookBarItem) items.get(index)).getComponent();
    SweepPanel sweep = new SweepPanel();
    sweep.reset(first, second, centralPanel, currentIndex - index);
    super.add(sweep, BorderLayout.CENTER);
    this.revalidate();
    this.repaint();

    currentIndex = index;

    SweepThread thread = new SweepThread(sweep);
    try {
      thread.start();
    }
    catch (Exception e) {}
  }

  /**
   * Implements Panel which performs sweep operation in 
   * OutlookBar.
   */
  private class SweepPanel extends JPanel 
  {
    /**
     * The serial ID
     */
    private static final long serialVersionUID = -1796950213776460209L;
    
    /**
     * The initial size of the bottom or top panel
     */
    private int y = 0;
    
    /**
     * The direction of the sweep
     */
    private int dir = 0;
    
    /**
     * The top panel
     */
    private JPanel topPanel = new JPanel();
    
    /**
     * The center panel
     */
    private JPanel centerPanel = new JPanel();
    
    /**
     * The bottom panel
     */
    private JPanel bottomPanel = new JPanel();
    
    /**
     * The move panel
     */
    private JPanel movePanel = new JPanel();

    /**
     * Constructs a new Sweep Panel.
     */
    public SweepPanel() 
    {
      this.setLayout(new BorderLayout());
      movePanel.setLayout(new BorderLayout());
      topPanel.setLayout(new BorderLayout());
      centerPanel.setLayout(new BorderLayout());
      bottomPanel.setLayout(new BorderLayout());
    }

    /**
     * Resets the Sweep Panel. Depending on the
     * direction, the first and second components
     * are either added to the bottom or top panel.
     * The central component is added to the center
     * panel. The center panel and either
     * the top or bottom panel are added to the 
     * move panel, and the move panel and
     * the remaining top or bottom panel are added
     * to this sweep panel.
     * 
     * @param first The first component
     * @param second The second component
     * @param central The central component
     * @param dir The direction to go 
     */
    public void reset(Component first, Component second, 
      Component central, int dir) 
    {
      y = 0;

      this.dir = dir;
      this.removeAll();

      topPanel.removeAll();
      bottomPanel.removeAll();
      centerPanel.removeAll();
      centerPanel.add(central);

      if (dir >= 0) {
        topPanel.add(second);
        bottomPanel.add(first);

        movePanel.add(topPanel, BorderLayout.CENTER);
        movePanel.add(centerPanel, BorderLayout.SOUTH);

        this.add(movePanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.CENTER);
      } 
      else 
      {
        topPanel.add(first);
        bottomPanel.add(second);

        movePanel.add(bottomPanel, BorderLayout.CENTER);
        movePanel.add(centerPanel, BorderLayout.NORTH);

        this.add(movePanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.CENTER);
      }

      topPanel.setPreferredSize(new Dimension(0, 0));
      bottomPanel.setPreferredSize(new Dimension(0, 0));
    }

    /**
     * Increases y by dy. Updates the preferred sizes of 
     * either the top or bottom panel depending on the
     * direction as y.
     * 
     * @param dy The y increase of either panel 
     * @return true if the next step leaves enough
     * space for the center panel, otherwise false
     */
    public boolean step(int dy) 
    {
      y += dy;
      if (dir >= 0)
        topPanel.setPreferredSize(new Dimension(0, y));
      else
        bottomPanel.setPreferredSize(new Dimension(0, y));
      int ph = this.getSize().height - centerPanel.getPreferredSize().height;
      return y+dy < ph && ph != 0;
    }
  }


  /**
   * Implements a thread which manages the sweep process.
   */
  private class SweepThread extends Thread 
  {
    /**
     * The sweep panel
     */
    private SweepPanel sweep = null;

    /**
     * Constructs the Sweep Thread
     * @param sweep The panel for sweeping
     */
    public SweepThread(SweepPanel sweep) 
    {
      this.sweep = sweep;
    }

    /**
     * Runs the sweep thread. Calls the
     * sweep.step() function and sleeps
     * for tempo seconds. If the step returns
     * true, the thread continues, otherwise
     * it finishes.
     */
    public void run() 
    {
      while (sweep.step(20)) 
      {
        sweep.revalidate();
        sweep.repaint();
        try 
        {
          sleep(tempo);
        }
        catch (Exception e) {}
      }
      updateContent();
    }
  }


  /**
   * Presents OutlookBar item with all related properties.
   */
  private class OutlookBarItem 
  {
    /**
     * The button for 
     */
    private JButton button = null;
    
    /**
     * The component
     */
    private Component component = null;

    /**
     * Creates an outlook bar item.
     * 
     * @param name The name for the button
     * @param component The component
     */
    public OutlookBarItem(String name, Component component) 
    {
      this.button = new JButton(name);
      this.component = component;
    }

    /**
     * Gets the button for the component
     * @return The button
     */
    public JButton getButton() 
    {
      return button;
    }

    /**
     * Gets the component for the button
     * @return The component
     */
    public Component getComponent() 
    {
      return component;
    }
  }


  /**
   * Runs this outlook bar as standalone application for test purposes.
   * @param args arguments of command line.
   */
  public static void main(String[] args) 
  {
    // Creates outlook bar
    OutlookBar bar = new OutlookBar();
    // Initializes outlook bar content
    bar.setTempo(50);
    bar.addPanel("Panel", new JPanel());
    bar.addPanel("Button", new JButton("xxx"));
    bar.addPanel("Label", new JLabel("xxx"));
    bar.addPanel("Text", new JTextPane());
    bar.addPanel("Auto Panel");
    bar.removePanel(0);

    // Constructs outlined frame
    JFrame frame = new JFrame("Outlook Bar");
    frame.getContentPane().add(bar);
    frame.setSize(305, 320);
    frame.setLocation(200, 200);
    frame.setDefaultCloseOperation(3);
    frame.setVisible(true);
 }
}



