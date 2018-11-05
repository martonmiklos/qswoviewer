/*      */ package com.atollic.truestudio.swv.charts;
/*      */ 
/*      */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*      */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*      */ import com.atollic.truestudio.swv.core.ui.Messages;
/*      */ import java.text.DateFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import org.eclipse.swt.custom.ScrolledComposite;
/*      */ import org.eclipse.swt.graphics.Color;
/*      */ import org.eclipse.swt.graphics.Font;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.FileDialog;
/*      */ import org.eclipse.swt.widgets.ScrollBar;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.swtchart.Chart;
/*      */ import org.swtchart.IAxis;
/*      */ import org.swtchart.IAxisSet;
/*      */ import org.swtchart.IAxisTick;
/*      */ import org.swtchart.ILegend;
/*      */ import org.swtchart.ILineSeries;
/*      */ import org.swtchart.ILineSeries.PlotSymbolType;
/*      */ import org.swtchart.ISeries.SeriesType;
/*      */ import org.swtchart.ISeriesSet;
/*      */ import org.swtchart.ITitle;
/*      */ import org.swtchart.Range;
/*      */ 
/*      */ public abstract class SWVChart extends Chart
/*      */ {
/*   41 */   private boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV);
/*      */ 
/*   43 */   private final DateFormat MILISECONDS_FORMAT = new SimpleDateFormat("sS");
/*      */   private static final int STYLE = 256;
/*   45 */   private final Font FONT = new Font(Display.getDefault(), "Tahoma", 10, 0);
/*   46 */   private Color FONT_COLOR = new Color(Display.getDefault(), 0, 0, 0);
/*   47 */   private Color DEFAULT_GRAPH_COLOR = new Color(Display.getDefault(), 255, 0, 0);
/*   48 */   public Color INFO_FOREGROUND_COLOR = Display.getCurrent().getSystemColor(28);
/*   49 */   public Color INFO_BACKGROUND_COLOR = Display.getCurrent().getSystemColor(29);
/*      */   private static final int INITIAL_Y_MAX = 5;
/*      */   private static final int INITIAL_Y_MIN = 0;
/*   52 */   private static final String[] EXTENSIONS = { "*.jpeg", "*.jpg", "*.png" };
/*      */   private static final int MAX_NR_SCRALLBAR_STEPS = 100000000;
/*   54 */   private int nrOfScrollbarSteps = 1;
/*      */   public static final int OPTIMAL_NR_BARS_AT_1600_PIXELS = 150;
/*   56 */   public static long OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS = 15000000L;
/*      */   private boolean fillBarArea;
/*   59 */   private double graphWidthMod = 1.0D;
/*      */   private Color currentGraphColor;
/*      */   private double currentWidth;
/*      */   private double xMin;
/*      */   private double xMax;
/*      */   private double displayedYMax;
/*      */   private double minDisplayedYMax;
/*      */   protected double realYMax;
/*      */   private double displayedYMin;
/*      */   private double realYMin;
/*   72 */   private double yAxisModSize = 1.0D;
/*   73 */   private double yAxisMod = 1.0D;
/*      */   private ScrollBar hBar;
/*      */   private ILineSeries lineSerie;
/*      */   private static final String LINE_ID = "Line";
/*   79 */   private boolean titleIsEnabled = true;
/*      */ 
/*   81 */   private static final double[] INITIAL_XSERIE = { 0.0D, 1.0D };
/*   82 */   private static final double[] INITIAL_YSERIE = { 0.0D, 0.0D };
/*      */   private double xMin_width;
/*      */   private double seriesFirstX;
/*      */   private double seriesLastX;
/*   87 */   private boolean isSerieOfGenericEvents = true;
/*      */   private double coreClock;
/*      */   private int currentScale;
/*   91 */   private int pixelWidth = 0;
/*   92 */   private boolean sizeIsChanged = false;
/*      */   private int prevEventTime;
/*   95 */   private Shell toolTip = null;
/*      */   private int toolTipTime;
/*   97 */   private boolean debugIsPaused = false;
/*      */ 
/*      */   public SWVChart(Composite parent, int scale_type, String title, Color startColor, boolean toBeFilled)
/*      */   {
/*  113 */     super(parent, 536871168);
/*  114 */     if (!this.hasPerm) {
/*  115 */       OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS /= 5L;
/*      */     }
/*  117 */     setScale(scale_type);
/*  118 */     this.currentGraphColor = startColor;
/*  119 */     this.fillBarArea = toBeFilled;
/*  120 */     createAxis(title);
/*      */ 
/*  122 */     createGraphs();
/*  123 */     setInitialSeries(getScale());
/*  124 */     initializeValues(title);
/*  125 */     setMinDisplayedYMax(2.0D);
/*      */ 
/*  127 */     ILegend legend = getLegend();
/*  128 */     legend.setVisible(false);
/*      */ 
/*  131 */     getHorizontalBar().setEnabled(false);
/*  132 */     getHorizontalBar().setVisible(false);
/*  133 */     this.hBar = ((ScrolledComposite)getParent()).getHorizontalBar();
/*  134 */     setNrOfScrollbarSteps();
/*  135 */     this.hBar.setSelection(getNrOfScrollbarSteps());
/*  136 */     this.hBar.addListener(13, this);
/*      */ 
/*  138 */     addListener(37, this);
/*  139 */     getPlotArea().addListener(32, this);
/*  140 */     getPlotArea().addListener(3, this);
/*      */ 
/*  143 */     showAll();
/*      */   }
/*      */ 
/*      */   public boolean hasPerm()
/*      */   {
/*  150 */     return this.hasPerm;
/*      */   }
/*      */ 
/*      */   public void setHasPerm(boolean hasPerm)
/*      */   {
/*  157 */     this.hasPerm = hasPerm;
/*      */   }
/*      */ 
/*      */   public void setCoreClock(double coreClock)
/*      */   {
/*  164 */     this.coreClock = coreClock;
/*      */   }
/*      */ 
/*      */   public double getCoreClock()
/*      */   {
/*  171 */     return this.coreClock;
/*      */   }
/*      */ 
/*      */   protected void setMinDisplayedYMax(double minDisplayedYMax)
/*      */   {
/*  178 */     this.minDisplayedYMax = minDisplayedYMax;
/*      */   }
/*      */ 
/*      */   private double getMinDisplayedYMax()
/*      */   {
/*  185 */     return this.minDisplayedYMax;
/*      */   }
/*      */ 
/*      */   public void initializeValues(String title) {
/*  189 */     setPixelWidth();
/*  190 */     this.xMin_width = getOptimalWidth();
/*  191 */     getTitle().setVisible(false);
/*  192 */     getAxisSet().getYAxis(0).getTitle().setText(title);
/*  193 */     getAxisSet().getXAxis(0).setRange(new Range(0.0D, this.xMin_width));
/*      */   }
/*      */ 
/*      */   public void setInitialSeries(int scale_type)
/*      */   {
/*  201 */     setPixelWidth();
/*  202 */     setXMin(0.0D);
/*  203 */     this.xMax = getOptimalWidth();
/*  204 */     setSeriesLastX(getOptimalWidth());
/*  205 */     this.lineSerie.setXSeries(INITIAL_XSERIE);
/*  206 */     this.lineSerie.getXSeries()[1] = getSeriesLastX();
/*  207 */     this.lineSerie.setYSeries(INITIAL_YSERIE);
/*  208 */     this.lineSerie.getYSeries()[1] = 0.0D;
/*  209 */     this.displayedYMax = 5.0D;
/*  210 */     this.realYMax = 5.0D;
/*  211 */     this.displayedYMin = 0.0D;
/*  212 */     this.realYMin = 0.0D;
/*  213 */     setCurrentWidth();
/*  214 */     this.graphWidthMod = 1.0D;
/*  215 */     setSerieOfGenericEvents(true);
/*  216 */     this.prevEventTime = 0;
/*  217 */     resetYMax(5.0D);
/*  218 */     resetYMin(0.0D);
/*  219 */     setSizeIsToBeChanged(true);
/*      */   }
/*      */ 
/*      */   public void setPixelWidth()
/*      */   {
/*  226 */     Rectangle rect = getPlotArea().getBounds();
/*  227 */     this.pixelWidth = ((rect != null) && (rect.width != 0) ? rect.width : 1500);
/*      */   }
/*      */ 
/*      */   private void createAxis(String title)
/*      */   {
/*  235 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  236 */     IAxis yAxis = getAxisSet().getYAxis(0);
/*      */ 
/*  238 */     ITitle xTitle = xAxis.getTitle();
/*  239 */     xTitle.setForeground(this.FONT_COLOR);
/*  240 */     xTitle.setFont(this.FONT);
/*  241 */     ITitle yTitle = yAxis.getTitle();
/*  242 */     yTitle.setForeground(this.FONT_COLOR);
/*  243 */     yTitle.setText(title);
/*  244 */     yTitle.setFont(this.FONT);
/*  245 */     IAxisTick xTick = xAxis.getTick();
/*  246 */     IAxisTick yTick = yAxis.getTick();
/*  247 */     xTick.setForeground(this.FONT_COLOR);
/*      */ 
/*  257 */     xTitle.setVisible(false);
/*  258 */     if (title.isEmpty()) {
/*  259 */       yTitle.setVisible(false);
/*  260 */       this.titleIsEnabled = false;
/*      */     }
/*  262 */     yTick.setForeground(this.FONT_COLOR);
/*  263 */     yTick.setTickMarkStepHint(8);
/*      */   }
/*      */ 
/*      */   public void createGraphs()
/*      */   {
/*  270 */     if (this.currentGraphColor == null) {
/*  271 */       this.currentGraphColor = this.DEFAULT_GRAPH_COLOR;
/*      */     }
/*  273 */     this.lineSerie = ((ILineSeries)getSeriesSet().createSeries(ISeries.SeriesType.LINE, "Line"));
/*  274 */     this.lineSerie.setLineColor(this.currentGraphColor);
/*  275 */     enablePlotSymbol(false);
/*  276 */     this.lineSerie.setSymbolSize(3);
/*  277 */     this.lineSerie.setAntialias(0);
/*  278 */     this.lineSerie.setLineWidth(1);
/*  279 */     this.lineSerie.enableArea(this.fillBarArea);
/*  280 */     this.lineSerie.enableStep(true);
/*      */   }
/*      */ 
/*      */   public void enableTitle(Boolean onOff)
/*      */   {
/*  287 */     if (!getTitle().getText().isEmpty()) {
/*  288 */       ITitle yTitle = getAxisSet().getYAxis(0).getTitle();
/*  289 */       yTitle.setVisible(onOff.booleanValue());
/*  290 */       redraw();
/*  291 */       this.titleIsEnabled = onOff.booleanValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enableScrollBar(boolean onOff)
/*      */   {
/*  300 */     if ((onOff) && (!isDebugPaused()))
/*      */     {
/*  302 */       onOff = false;
/*      */     }
/*  304 */     if ((onOff) && (getSeriesLastX() <= getOptimalWidth()))
/*      */     {
/*  306 */       onOff = false;
/*      */     }
/*  308 */     if (getXserie() == null) onOff = false;
/*  309 */     if (isScrollBarEnabled() != onOff)
/*      */     {
/*  311 */       this.hBar.setEnabled(onOff);
/*  312 */       this.hBar.setVisible(onOff);
/*  313 */       if (onOff) {
/*  314 */         setNrOfScrollbarSteps();
/*  315 */         this.hBar.setSelection(getNrOfScrollbarSteps());
/*  316 */         moveTo(getSeriesLastX() - getOptimalWidth());
/*      */       }
/*  318 */       update();
/*  319 */       updateLayout();
/*  320 */       redraw();
/*      */     }
/*  322 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/*  323 */       getToolTip().dispose();
/*      */   }
/*      */ 
/*      */   private void setNrOfScrollbarSteps()
/*      */   {
/*  333 */     int preliminaryNrOfScrollbarSteps = (int)(getEndOfX() / getOptimalWidth() * this.pixelWidth);
/*  334 */     int oldNrOfScrollbarSteps = getNrOfScrollbarSteps();
/*  335 */     if (preliminaryNrOfScrollbarSteps != oldNrOfScrollbarSteps) {
/*  336 */       this.nrOfScrollbarSteps = (preliminaryNrOfScrollbarSteps <= 100000000 ? preliminaryNrOfScrollbarSteps : 100000000);
/*      */ 
/*  338 */       if ((isScrollBarEnabled()) && (oldNrOfScrollbarSteps > getNrOfScrollbarSteps()))
/*      */       {
/*  340 */         this.hBar.setEnabled(false);
/*  341 */         this.hBar.setVisible(false);
/*  342 */         this.hBar.setEnabled(true);
/*  343 */         this.hBar.setVisible(true);
/*      */       }
/*  345 */       if (this.hBar.getMaximum() < getNrOfScrollbarSteps()) {
/*  346 */         this.hBar.setMaximum(getNrOfScrollbarSteps());
/*      */       }
/*  348 */       if (this.hBar.getMinimum() < 0) {
/*  349 */         this.hBar.setMinimum(0);
/*      */       }
/*  351 */       this.hBar.setPageIncrement(getNrOfScrollbarSteps() / 50);
/*  352 */       this.hBar.setIncrement(getNrOfScrollbarSteps() / 200);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getNrOfScrollbarSteps()
/*      */   {
/*  360 */     return this.nrOfScrollbarSteps == 0 ? 1 : this.nrOfScrollbarSteps;
/*      */   }
/*      */ 
/*      */   public double getScrollbarSelection()
/*      */   {
/*  367 */     int hSelection = this.hBar.getSelection();
/*  368 */     double lastSelectableValue = getHighestSelectableValue();
/*  369 */     int selectableScrollbarSteps = getNrOfScrollbarSteps() - this.hBar.getSize().x;
/*  370 */     return hSelection * lastSelectableValue / selectableScrollbarSteps;
/*      */   }
/*      */ 
/*      */   public void setScrollbarSelection(double selectedValue)
/*      */   {
/*  379 */     if (isScrollBarEnabled()) {
/*  380 */       double lastSelectableValue = getHighestSelectableValue();
/*  381 */       int selectableScrollbarSteps = getNrOfScrollbarSteps() - this.hBar.getSize().x;
/*  382 */       int newSelectionValue = (int)(selectedValue / lastSelectableValue * selectableScrollbarSteps);
/*  383 */       this.hBar.setSelection(newSelectionValue);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isScrollBarEnabled()
/*      */   {
/*  392 */     if (this.hBar == null) {
/*  393 */       return false;
/*      */     }
/*  395 */     return this.hBar.getEnabled();
/*      */   }
/*      */ 
/*      */   public void setDebugIsPaused(boolean onOff)
/*      */   {
/*  403 */     if (this.debugIsPaused != onOff) {
/*  404 */       this.debugIsPaused = onOff;
/*  405 */       enableScrollBar(onOff);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isDebugPaused()
/*      */   {
/*  413 */     return this.debugIsPaused;
/*      */   }
/*      */ 
/*      */   public boolean isTitleEnabled()
/*      */   {
/*  420 */     return this.titleIsEnabled;
/*      */   }
/*      */ 
/*      */   public void switchScale()
/*      */   {
/*  428 */     if (getScale() == 0)
/*  429 */       setScale(1);
/*      */     else {
/*  431 */       setScale(0);
/*      */     }
/*  433 */     createAxis(getTitle().getText());
/*  434 */     initializeValues(getTitle().getText());
/*  435 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/*  436 */       getToolTip().dispose();
/*  437 */     redraw();
/*      */   }
/*      */ 
/*      */   public int getScale()
/*      */   {
/*  445 */     return this.currentScale;
/*      */   }
/*      */ 
/*      */   public void setScale(int scaleType)
/*      */   {
/*  453 */     this.currentScale = scaleType;
/*      */   }
/*      */ 
/*      */   public void setSpan(double first, double last)
/*      */   {
/*  464 */     setCurrentWidth();
/*  465 */     double firstInSerie = getFirstX();
/*  466 */     double lastInSerie = getEndOfX();
/*  467 */     setXMin(first < firstInSerie ? firstInSerie : first);
/*  468 */     this.xMax = (last > lastInSerie ? lastInSerie : last);
/*  469 */     if (this.xMax < this.xMin) {
/*  470 */       double tmp = this.xMin;
/*  471 */       setXMin(this.xMax);
/*  472 */       this.xMax = tmp;
/*  473 */     } else if (Double.compare(this.xMax, this.xMin) == 0) {
/*  474 */       this.xMax += this.xMin_width;
/*      */     }
/*  476 */     Range range = new Range(this.xMin, this.xMax);
/*  477 */     getAxisSet().getXAxis(0).setRange(range);
/*  478 */     setScrollbarSelection(this.xMin);
/*  479 */     redraw();
/*      */   }
/*      */ 
/*      */   public void showAll()
/*      */   {
/*  485 */     setSpan(getFirstX(), getEndOfX());
/*      */   }
/*      */ 
/*      */   public void moveTo(double newXMin)
/*      */   {
/*  495 */     double firstInSerie = getFirstX();
/*  496 */     if (newXMin < firstInSerie) {
/*  497 */       newXMin = firstInSerie;
/*      */     }
/*  499 */     if (newXMin > getHighestSelectableValue()) {
/*  500 */       newXMin = getHighestSelectableValue();
/*      */     }
/*  502 */     setXMin(newXMin);
/*  503 */     this.xMax = (this.xMin + getCurrentWidth());
/*  504 */     if (Double.compare(this.xMax, this.xMin) == 0) {
/*  505 */       this.xMax += this.xMin_width;
/*      */     }
/*  507 */     getAxisSet().getXAxis(0).setRange(new Range(this.xMin, this.xMax));
/*  508 */     redraw();
/*      */   }
/*      */ 
/*      */   public void moveToEnd()
/*      */   {
/*  517 */     this.xMax = getEndOfX();
/*  518 */     setXMin(this.xMax - getOptimalWidth());
/*  519 */     if (isScrollBarEnabled())
/*      */     {
/*  521 */       this.hBar.setSelection(getNrOfScrollbarSteps());
/*      */     }
/*  523 */     moveTo(this.xMin);
/*      */   }
/*      */ 
/*      */   private void setCurrentWidth()
/*      */   {
/*  530 */     this.currentWidth = getOptimalWidth();
/*      */   }
/*      */ 
/*      */   public double getCurrentWidth()
/*      */   {
/*  537 */     return this.currentWidth;
/*      */   }
/*      */ 
/*      */   private double getHighestSelectableValue()
/*      */   {
/*  544 */     return getEndOfX() - getCurrentWidth();
/*      */   }
/*      */ 
/*      */   public long getOptimalWidthInCycles()
/*      */   {
/*  554 */     return ()(getOptimalGraphWidthInMicroSeconds() / 1000000.0D * getCoreClock());
/*      */   }
/*      */ 
/*      */   public double getOptimalWidthInSeconds()
/*      */   {
/*  563 */     return getOptimalGraphWidthInMicroSeconds() / 1000000.0D;
/*      */   }
/*      */ 
/*      */   public double getOptimalWidth()
/*      */   {
/*  572 */     return getScale() == 0 ? getOptimalWidthInCycles() : getOptimalWidthInSeconds();
/*      */   }
/*      */ 
/*      */   public double getXMin()
/*      */   {
/*  580 */     return this.xMin;
/*      */   }
/*      */ 
/*      */   public long getXMinAsCycles()
/*      */   {
/*  588 */     double min = getXMin();
/*  589 */     if (getScale() == 1) {
/*  590 */       min *= getCoreClock();
/*      */     }
/*  592 */     return ()min;
/*      */   }
/*      */ 
/*      */   public void setXMin(double newXMin)
/*      */   {
/*  600 */     double max = getHighestSelectableValue();
/*  601 */     if (newXMin > max) newXMin = max;
/*  602 */     this.xMin = newXMin;
/*  603 */     if (this.xMin < 0.0D)
/*  604 */       this.xMin = 0.0D;
/*      */   }
/*      */ 
/*      */   public double getFirstX()
/*      */   {
/*  611 */     double[] serie = getXserie();
/*  612 */     if ((serie == null) || (serie.length < 3))
/*      */     {
/*  614 */       if (isSizeToBeChanged()) {
/*  615 */         return this.xMin;
/*      */       }
/*  617 */       double value = getEndOfX() - getOptimalWidth();
/*  618 */       if (value < 0.0D) value = 0.0D;
/*  619 */       return value;
/*      */     }
/*  621 */     return getSeriesFirstX();
/*      */   }
/*      */ 
/*      */   public void setSeriesFirstX(double seriesFirstX)
/*      */   {
/*  628 */     this.seriesFirstX = seriesFirstX;
/*      */   }
/*      */ 
/*      */   private double getSeriesFirstX()
/*      */   {
/*  638 */     return this.seriesFirstX;
/*      */   }
/*      */ 
/*      */   protected double getSeriesLastX()
/*      */   {
/*  647 */     double[] serie = getXserie();
/*  648 */     if ((serie == null) || (serie.length == 0)) {
/*  649 */       return this.xMin_width;
/*      */     }
/*  651 */     return this.seriesLastX;
/*      */   }
/*      */ 
/*      */   public void setSeriesLastX(double seriesLastX)
/*      */   {
/*  658 */     this.seriesLastX = seriesLastX;
/*      */   }
/*      */ 
/*      */   private double getEndOfX()
/*      */   {
/*  668 */     double last = getSeriesLastX();
/*  669 */     double[] serie = getXserie();
/*  670 */     double optimalEnd = getOptimalWidth() + this.xMin;
/*  671 */     if ((serie != null) && (serie.length > 2)) {
/*  672 */       if (last > optimalEnd) {
/*  673 */         return last;
/*      */       }
/*  675 */       return optimalEnd;
/*      */     }
/*  677 */     return last;
/*      */   }
/*      */ 
/*      */   protected double[] getXserie()
/*      */   {
/*  684 */     return this.lineSerie.getXSeries();
/*      */   }
/*      */ 
/*      */   protected double[] getYserie()
/*      */   {
/*  689 */     return this.lineSerie.getYSeries();
/*      */   }
/*      */ 
/*      */   public void setSerieOfGenericEvents(boolean isSerieOfGenericEvents)
/*      */   {
/*  698 */     this.isSerieOfGenericEvents = isSerieOfGenericEvents;
/*      */   }
/*      */ 
/*      */   public boolean isSerieOfGenericEvents()
/*      */   {
/*  707 */     return this.isSerieOfGenericEvents;
/*      */   }
/*      */ 
/*      */   public double getSelectionFromPixel(double xPixel)
/*      */   {
/*  718 */     return this.xMin + getCurrentWidth() * (xPixel / this.pixelWidth);
/*      */   }
/*      */ 
/*      */   protected int getSerieIndex(double position)
/*      */   {
/*  726 */     int x = 0;
/*  727 */     if (position < getFirstX()) {
/*  728 */       return 0;
/*      */     }
/*  730 */     if (position > getSeriesLastX()) {
/*  731 */       x = getXserie().length - 1;
/*  732 */       if (x < 1) {
/*  733 */         x = 1;
/*      */       }
/*  735 */       return x;
/*      */     }
/*      */ 
/*  738 */     while ((x < getXserie().length) && (getXserie()[x] <= position)) {
/*  739 */       x++;
/*      */     }
/*  741 */     if (x < 1) {
/*  742 */       x = 1;
/*      */     }
/*  744 */     return x - 1;
/*      */   }
/*      */ 
/*      */   protected int getYValueAt(double position)
/*      */   {
/*  752 */     return (int)getYserie()[getSerieIndex(position)];
/*      */   }
/*      */ 
/*      */   protected void setSeries(double[] newXserie, double[] newYserie)
/*      */   {
/*  761 */     if (newXserie.length > 1) {
/*  762 */       if (this.lineSerie != null) {
/*  763 */         this.lineSerie.setXSeries(newXserie);
/*  764 */         this.lineSerie.setYSeries(newYserie);
/*  765 */         if (isScrollBarEnabled()) {
/*  766 */           setNrOfScrollbarSteps();
/*      */         }
/*      */       }
/*      */ 
/*  770 */       if (getAxisSet().getYAxis(0).getRange().lower < this.displayedYMin)
/*  771 */         getAxisSet().getYAxis(0).setRange(new Range(this.displayedYMin, this.displayedYMax));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enablePlotSymbol(boolean onOff)
/*      */   {
/*  780 */     if (onOff)
/*  781 */       this.lineSerie.setSymbolType(ILineSeries.PlotSymbolType.CROSS);
/*      */     else
/*  783 */       this.lineSerie.setSymbolType(ILineSeries.PlotSymbolType.NONE);
/*      */   }
/*      */ 
/*      */   public int getOptimalNrBars()
/*      */   {
/*  791 */     if (this.pixelWidth == 0) {
/*  792 */       return 150;
/*      */     }
/*  794 */     return this.pixelWidth * 150 / 1550;
/*      */   }
/*      */ 
/*      */   public double getOptimalGraphWidthInMicroSeconds()
/*      */   {
/*      */     double width;
/*      */     double width;
/*  805 */     if (this.pixelWidth == 0)
/*  806 */       width = OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS * this.graphWidthMod;
/*      */     else {
/*  808 */       width = this.pixelWidth * OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS / 1550.0D * this.graphWidthMod;
/*      */     }
/*  810 */     return width;
/*      */   }
/*      */ 
/*      */   public void multiplyGraphWidthMod(double multiplier)
/*      */   {
/*  819 */     this.graphWidthMod *= multiplier;
/*  820 */     setCurrentWidth();
/*  821 */     moveTo(this.xMin);
/*      */ 
/*  823 */     if ((isDebugPaused()) && (isScrollBarEnabled()) && (getEndOfX() <= getOptimalWidth()))
/*      */     {
/*  825 */       enableScrollBar(false);
/*      */     }
/*  827 */     if ((isDebugPaused()) && (!isScrollBarEnabled()) && (getEndOfX() > getOptimalWidth()))
/*      */     {
/*  829 */       enableScrollBar(true);
/*      */     }
/*  831 */     if (isScrollBarEnabled())
/*  832 */       setNrOfScrollbarSteps();
/*      */   }
/*      */ 
/*      */   public void multiplyYMaxMod(double multiplier)
/*      */   {
/*  840 */     double diff = this.yAxisModSize * multiplier * 2.0D;
/*  841 */     if ((this.yAxisModSize + diff > 0.0D) && (this.yAxisModSize + diff < 7.0D) && (
/*  842 */       ((int)((this.realYMax + this.realYMax / 15.0D) * this.yAxisMod) > 2.0D) || (this.yAxisModSize + diff > this.yAxisModSize))) {
/*  843 */       this.yAxisModSize += diff;
/*      */     }
/*      */ 
/*  846 */     this.yAxisMod = this.yAxisModSize;
/*  847 */     setYMax(getRealYMax());
/*  848 */     redraw();
/*      */   }
/*      */ 
/*      */   protected double getRealYMax()
/*      */   {
/*  857 */     return this.realYMax;
/*      */   }
/*      */ 
/*      */   protected double getRealYMin()
/*      */   {
/*  866 */     return this.realYMin;
/*      */   }
/*      */ 
/*      */   public void adjustYAxis()
/*      */   {
/*  872 */     if (getYserie() == null)
/*      */       return;
/*      */     int stopIndex;
/*      */     int startIndex;
/*      */     int stopIndex;
/*  877 */     if (isScrollBarEnabled())
/*      */     {
/*  879 */       int startIndex = getSerieIndex(getXMin());
/*  880 */       stopIndex = getSerieIndex(this.xMax);
/*      */     }
/*      */     else {
/*  883 */       startIndex = 0;
/*  884 */       stopIndex = getYserie().length - 1;
/*      */     }
/*      */ 
/*  887 */     double[] ySerie = Arrays.copyOfRange(getYserie(), startIndex, stopIndex);
/*  888 */     Arrays.sort(ySerie);
/*      */ 
/*  890 */     double yMax = ySerie.length > 1 ? ySerie[(ySerie.length - 1)] : 5.0D;
/*  891 */     resetYMax(yMax);
/*  892 */     double yMin = ySerie.length > 1 ? ySerie[0] : 0.0D;
/*  893 */     resetYMin(yMin);
/*  894 */     redraw();
/*      */   }
/*      */ 
/*      */   protected void setYMax(double maxValue)
/*      */   {
/*  904 */     if (maxValue > this.realYMax) {
/*  905 */       this.realYMax = maxValue;
/*      */     }
/*  907 */     this.displayedYMax = ((this.realYMax + this.realYMax / 10.0D) * this.yAxisMod);
/*  908 */     if (this.displayedYMax < getMinDisplayedYMax()) {
/*  909 */       this.displayedYMax = getMinDisplayedYMax();
/*      */     }
/*  911 */     getAxisSet().getYAxis(0).setRange(new Range(this.displayedYMin, this.displayedYMax));
/*      */   }
/*      */ 
/*      */   protected void setYMin(double minValue)
/*      */   {
/*  921 */     if (minValue < this.realYMin) {
/*  922 */       this.realYMin = minValue;
/*      */     }
/*  924 */     this.displayedYMin = ((this.realYMin - this.realYMin / 10.0D) * this.yAxisMod);
/*  925 */     if (this.displayedYMin > 0.0D) {
/*  926 */       this.displayedYMin = 0.0D;
/*      */     }
/*  928 */     getAxisSet().getYAxis(0).setRange(new Range(this.displayedYMin, this.displayedYMax));
/*      */   }
/*      */ 
/*      */   public void resetYMax(double maxValue)
/*      */   {
/*  937 */     this.realYMax = maxValue;
/*  938 */     this.yAxisMod = 1.0D;
/*  939 */     this.yAxisModSize = 1.0D;
/*  940 */     setYMax(maxValue);
/*      */   }
/*      */ 
/*      */   public void resetYMin(double minValue)
/*      */   {
/*  948 */     this.realYMin = minValue;
/*  949 */     this.yAxisMod = 1.0D;
/*  950 */     this.yAxisModSize = 1.0D;
/*  951 */     setYMin(minValue);
/*      */   }
/*      */ 
/*      */   private void setPrevEventTime(int prevEventTime)
/*      */   {
/*  959 */     this.prevEventTime = prevEventTime;
/*      */   }
/*      */ 
/*      */   private int getPrevEventTime()
/*      */   {
/*  966 */     return this.prevEventTime;
/*      */   }
/*      */ 
/*      */   private void setToolTipTime(int toolTipTime)
/*      */   {
/*  973 */     this.toolTipTime = toolTipTime;
/*      */   }
/*      */ 
/*      */   private int getToolTipTime()
/*      */   {
/*  980 */     return this.toolTipTime;
/*      */   }
/*      */ 
/*      */   public void handleEvent(Event event)
/*      */   {
/*  991 */     switch (event.type) {
/*      */     case 13:
/*  993 */       if (isScrollBarEnabled()) {
/*  994 */         int eventTime = Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date()));
/*  995 */         int diff = eventTime - getPrevEventTime();
/*  996 */         if ((diff > 100) || (diff < -100)) {
/*  997 */           handleScrollBarEvent();
/*      */         }
/*  999 */         setPrevEventTime(eventTime);
/*      */       }
/* 1001 */       break;
/*      */     case 11:
/* 1003 */       handleResizeEvent();
/* 1004 */       updateLayout();
/* 1005 */       redraw();
/* 1006 */       update();
/* 1007 */       break;
/*      */     case 37:
/* 1009 */       if ((isDebugPaused()) && (isScrollBarEnabled())) {
/* 1010 */         int eventTime = Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date()));
/* 1011 */         int diff = eventTime - getPrevEventTime();
/* 1012 */         if ((diff > 100) || (diff < -100)) {
/* 1013 */           handleMouseWheelEvent(event);
/*      */         }
/* 1015 */         setPrevEventTime(eventTime);
/*      */       }
/* 1017 */       break;
/*      */     case 32:
/* 1019 */       if (isScrollBarEnabled()) {
/* 1020 */         int eventTime = Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date()));
/* 1021 */         int diff = eventTime - getPrevEventTime();
/* 1022 */         if ((diff > 100) || (diff < -100)) {
/* 1023 */           diff = eventTime - getToolTipTime();
/* 1024 */           if (((diff > 1500) || (diff < -1500)) && 
/* 1025 */             (getToolTip() != null) && (!getToolTip().isDisposed())) {
/* 1026 */             getToolTip().dispose();
/*      */           }
/*      */         }
/* 1029 */         setPrevEventTime(eventTime);
/*      */       }
/* 1031 */       break;
/*      */     case 3:
/* 1033 */       if (isDebugPaused()) {
/* 1034 */         handleToolTipEvent(event);
/* 1035 */         setToolTipTime(Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date())));
/*      */       }
/* 1037 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleScrollBarEvent()
/*      */   {
/* 1048 */     double hSelection = getScrollbarSelection();
/* 1049 */     if (hSelection == 0.0D)
/* 1050 */       setXMin(0.0D);
/* 1051 */     else if (hSelection > getHighestSelectableValue())
/* 1052 */       setXMin(getHighestSelectableValue());
/*      */     else {
/* 1054 */       setXMin(hSelection);
/*      */     }
/* 1056 */     moveTo(this.xMin);
/* 1057 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1058 */       getToolTip().dispose();
/* 1059 */     updateLayout();
/* 1060 */     redraw();
/* 1061 */     update();
/*      */   }
/*      */ 
/*      */   private void handleResizeEvent()
/*      */   {
/* 1069 */     setPixelWidth();
/* 1070 */     setSizeIsToBeChanged(true);
/* 1071 */     getAxisSet().getYAxis(0).adjustRange();
/* 1072 */     Range yrange = new Range(0.0D, this.displayedYMax);
/* 1073 */     getAxisSet().getYAxis(0).setRange(yrange);
/* 1074 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1075 */       getToolTip().dispose();
/* 1076 */     if (isScrollBarEnabled())
/*      */     {
/* 1078 */       setNrOfScrollbarSteps();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleMouseWheelEvent(Event event)
/*      */   {
/* 1087 */     double change = getCurrentWidth() / 30.0D;
/* 1088 */     if (event.count > 0)
/* 1089 */       setXMin(this.xMin + change);
/*      */     else {
/* 1091 */       setXMin(this.xMin - change);
/*      */     }
/* 1093 */     moveTo(this.xMin);
/* 1094 */     setScrollbarSelection(this.xMin);
/* 1095 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1096 */       getToolTip().dispose();
/* 1097 */     redraw();
/* 1098 */     update();
/*      */   }
/*      */ 
/*      */   public boolean isSizeToBeChanged()
/*      */   {
/* 1107 */     return this.sizeIsChanged;
/*      */   }
/*      */ 
/*      */   public void setSizeIsToBeChanged(boolean yes)
/*      */   {
/* 1116 */     this.sizeIsChanged = yes;
/*      */   }
/*      */ 
/*      */   public void openSaveAsDialog()
/*      */   {
/* 1125 */     FileDialog dialog = new FileDialog(getShell(), 8192);
/* 1126 */     dialog.setText(Messages.SAVE_AS_DIALOG_TITLE);
/* 1127 */     dialog.setFilterExtensions(EXTENSIONS);
/* 1128 */     String filename = dialog.open();
/* 1129 */     if (filename == null)
/*      */       return;
/*      */     int format;
/*      */     int format;
/* 1133 */     if ((filename.endsWith(".jpg")) || (filename.endsWith(".jpeg"))) {
/* 1134 */       format = 4;
/*      */     }
/*      */     else
/*      */     {
/*      */       int format;
/* 1135 */       if (filename.endsWith(".png"))
/* 1136 */         format = 5;
/*      */       else
/* 1138 */         format = -1;
/*      */     }
/* 1140 */     if (format != -1) {
/* 1141 */       save(filename, format);
/*      */     }
/* 1143 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1144 */       getToolTip().dispose();
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1149 */     if (!isDisposed()) {
/* 1150 */       this.FONT_COLOR.dispose();
/* 1151 */       if (!this.DEFAULT_GRAPH_COLOR.isDisposed()) {
/* 1152 */         this.DEFAULT_GRAPH_COLOR.dispose();
/*      */       }
/* 1154 */       if (!this.currentGraphColor.isDisposed()) {
/* 1155 */         this.currentGraphColor.dispose();
/*      */       }
/*      */     }
/* 1158 */     super.dispose();
/*      */   }
/*      */ 
/*      */   public abstract void handleToolTipEvent(Event paramEvent);
/*      */ 
/*      */   public void setToolTip(Shell toolTip)
/*      */   {
/* 1175 */     this.toolTip = toolTip;
/*      */   }
/*      */ 
/*      */   public Shell getToolTip()
/*      */   {
/* 1182 */     return this.toolTip;
/*      */   }
/*      */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVChart
 * JD-Core Version:    0.6.2
 */