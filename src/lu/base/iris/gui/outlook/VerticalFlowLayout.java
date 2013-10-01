package lu.base.iris.gui.outlook;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.Serializable;


/**
 * VerticalFlowLayout description : FlowLayout extension allowing auto
 * arrangment of items.
 */
class VerticalFlowLayout extends FlowLayout
	implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -5020444540119514422L;


/**
   * The aligment at the top of container.
   */
  public static final int TOP = 0;


  /**
   * The aligment at the center of container.
   */
  public static final int MIDDLE = 1;


  /**
   * The aligment at the bottom of container.
   */
  public static final int BOTTOM = 2;


  /**
   * The horizontal gap value.
   */
  private int hgap;


  /**
   * The vertical gap value.
   */
  private int vgap;


  /**
   * The horizontal fill value.
   */
  private boolean hfill;


  /**
   * The vertical fill value.
   */
  private boolean vfill;


  /**
   * Constructs this layout with default properties.
   */
  public VerticalFlowLayout() {
    this(TOP, 5, 5, true, false);
  }


  /**
   * Constructs this layout and assignes class properties.
   * @param fill the fill to edge flag.
   */
  public VerticalFlowLayout(boolean hfill, boolean vfill){
    this(TOP, 5, 5, hfill, vfill);
  }


  /**
   * Constructs this layout and assignes class properties.
   * @param align the alignment value.
   */
  public VerticalFlowLayout(int align) {
    this(align, 5, 5, true, false);
  }


  /**
   * Constructs this layout and assignes class properties.
   * @param align the alignment value.
   * @param fill the fill to edge flag.
   */
  public VerticalFlowLayout(int align, boolean hfill, boolean vfill) {
    this(align, 5, 5, hfill, vfill);
  }


  /**
   * Constructs this layout and assignes class properties.
   * @param align the alignment value.
   * @param hgap the horizontal gap variable.
   * @param vgap the vertical gap variable.
   * @param fill the fill to edge flag.
   */
  public VerticalFlowLayout(int align, int hgap, int vgap,
    boolean hfill, boolean vfill) {
    setAlignment(align);
    this.hgap = hgap;
    this.vgap = vgap;
    this.hfill = hfill;
    this.vfill = vfill;
  }


  /**
   * Gets the horizontal gap between components.
   * @result the horizontal gap between components.
   */
  public int getHgap() {
    return hgap;
  }


  /**
   * Sets the horizontal gap between components.
   * @param hgap a new horizontal gap value.
   */
  public void setHgap(int hgap) {
    super.setHgap(hgap);
    this.hgap = hgap;
  }


  /**
   * Gets the vertical gap between components.
   * @result the vertical gap between components.
   */
  public int getVgap() {
    return vgap;
  }


  /**
   * Sets the vertical gap between components.
   * @param vgap a new vertical gap value.
   */
  public void setVgap(int vgap) {
    super.setVgap(vgap);
    this.vgap = vgap;
  }


  /**
   * Returns the preferred dimensions given the components
   * in the target container.
   * @param target the component to lay out
   */
  public Dimension preferredLayoutSize(Container target) {
    Dimension tarsiz = new Dimension(0, 0);


    for (int i = 0 ; i < target.getComponentCount(); i++) {
      Component m = target.getComponent(i);
      if (m.isVisible()) {
        Dimension d = m.getPreferredSize();
        tarsiz.width = Math.max(tarsiz.width, d.width);
        if (i > 0) {
          tarsiz.height += vgap;
        }
        tarsiz.height += d.height;
      }
    }
    Insets insets = target.getInsets();
    tarsiz.width += insets.left + insets.right + hgap*2;
    tarsiz.height += insets.top + insets.bottom + vgap*2;
    return tarsiz;
  }


  /**
   * Counts the minimum size needed to layout the target container.
   * @param container a container of visual components to be aligned.
   */
  public Dimension minimumLayoutSize(Container container) {
    Dimension size = new Dimension(0, 0);


    for (int i = 0 ; i < container.getComponentCount(); i++) {
      Component current = container.getComponent(i);
      if (current.isVisible()) {
          Dimension d = current.getMinimumSize();
          if (size.width < d.width)
            size.width = d.width;
          if (i > 0)
            size.height += vgap;
          size.height += d.height;
      }
    }
    Insets insets = container.getInsets();
    size.width += insets.left + insets.right + hgap*2;
    size.height += insets.top + insets.bottom + vgap*2;
    return size;
  }


  /**
   * Sets a new vertical fill.
   * @param vfill a new vertical fill value.
   */
  public void setVerticalFill(boolean vfill) {
    this.vfill = vfill;
  }


  /**
   * Gets a current vertical fill value.
   * @result a current vertical fill value.
   */
  public boolean getVerticalFill() {
    return vfill;
  }


  /**
   * Sets a new horizontal fill.
   * @param hfill a new horizontal fill value.
   */
  public void setHorizontalFill(boolean hfill) {
    this.hfill = hfill;
  }


  /**
   * Gets a current horizontal fill value.
   * @result a current horizontal fill value.
   */
  public boolean getHorizontalFill() {
    return hfill;
  }


  /**
   * Arranges components defined by first to last within the target
   * container using the bounds box defined.
   * @param target the container
   * @param x the x coordinate of the area
   * @param y the y coordinate of the area
   * @param width the width of the area
   * @param height the height of the area
   * @param first the first component of the container to place
   * @param last the last component of the container to place
   */
  private void arrange(Container container, int x, int y, int width, int height,
    int first, int last) {


    int align = getAlignment();
    if (align == MIDDLE) y += height  / 2;
    if (align == BOTTOM) y += height;

    for (int i = first ; i < last ; i++) {
      Component current = container.getComponent(i);
        Dimension d = current.getSize();
      if (current.isVisible()) {
        int px = x + (width - d.width) / 2;
        current.setLocation(px, y);
        y += vgap + d.height;
      }
    }
  }


  /**
   * Lays out the container.
   * @param container the container to lay out.
   */
  public void layoutContainer(Container container) {
    Insets insets = container.getInsets();
    int maxheight = container.getSize().height
      - (insets.top + insets.bottom + vgap * 2);
    int maxwidth = container.getSize().width
      - (insets.left + insets.right + hgap * 2);
    int numcomp = container.getComponentCount();
    int x = insets.left + hgap;
    int y = 0  ;
    int colw = 0, start = 0;


    for (int i = 0 ; i < numcomp ; i++) {
      Component current = container.getComponent(i);
      if (current.isVisible()) {
        Dimension d = current.getPreferredSize();
        // fit last component to remaining height
        if ((this.vfill) && (i == (numcomp-1))) {
          d.height = Math.max((maxheight - y), current.getPreferredSize().height);
        }


        // fit componenent size to container width
        if (this.hfill) {
          current.setSize(maxwidth, d.height);
          d.width = maxwidth;
        } else {
          current.setSize(d.width, d.height);
        }


        if (y  + d.height > maxheight) {
          arrange(container, x, insets.top + vgap, colw, maxheight-y, start, i);
          y = d.height;
          x += hgap + colw;
          colw = d.width;
          start = i;
        } else {
          if (y > 0) y += vgap;
          y += d.height;
          colw = Math.max(colw, d.width);
        }
      }
    }
    arrange(container, x, insets.top + vgap, colw, maxheight - y, start, numcomp);
  }
}



