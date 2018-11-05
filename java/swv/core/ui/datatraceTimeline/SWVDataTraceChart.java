/*      */ package com.atollic.truestudio.swv.core.ui.datatraceTimeline;
/*      */ 
/*      */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*      */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*      */ import com.atollic.truestudio.swv.charts.SWVDataTraceChartToolTipContentProvider;
/*      */ import com.atollic.truestudio.swv.charts.SWVDataTraceChartToolTipLabelProvider;
/*      */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*      */ import com.atollic.truestudio.swv.core.ui.Messages;
/*      */ import com.atollic.truestudio.swv.core.ui.datatrace.Data;
/*      */ import com.atollic.truestudio.swv.core.ui.datatrace.DataTrace;
/*      */ import com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceUtil;
/*      */ import java.text.DateFormat;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import org.eclipse.jface.viewers.TableViewer;
/*      */ import org.eclipse.jface.viewers.TableViewerColumn;
/*      */ import org.eclipse.swt.custom.ScrolledComposite;
/*      */ import org.eclipse.swt.graphics.Color;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.FileDialog;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.ScrollBar;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Table;
/*      */ import org.eclipse.swt.widgets.TableColumn;
/*      */ import org.eclipse.swt.widgets.Widget;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.swtchart.Chart;
/*      */ import org.swtchart.IAxis;
/*      */ import org.swtchart.IAxisSet;
/*      */ import org.swtchart.IAxisTick;
/*      */ import org.swtchart.ILegend;
/*      */ import org.swtchart.ILineSeries;
/*      */ import org.swtchart.ILineSeries.PlotSymbolType;
/*      */ import org.swtchart.ISeries;
/*      */ import org.swtchart.ISeries.SeriesType;
/*      */ import org.swtchart.ISeriesSet;
/*      */ import org.swtchart.ITitle;
/*      */ import org.swtchart.Range;
/*      */ 
/*      */ public class SWVDataTraceChart extends Chart
/*      */ {
/*   55 */   private boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/*      */   private static final String SPACE = " ";
/*   58 */   private static final String[] EXTENSIONS = { "*.jpeg", "*.jpg", "*.png" };
/*      */   public static final int CYCLE_SCALE = 0;
/*      */   public static final int TIME_SCALE = 1;
/*      */   private static final int MAX_NR_SCRALLBAR_STEPS = 100000000;
/*   62 */   private int nrOfScrollbarSteps = 1;
/*      */   public static final long OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS = 15000000L;
/*      */   public static final int MAX_NR_PLOT_SYMBOLS_AT_1600_PIXELS = 150;
/*   65 */   private final DateFormat MILISECONDS_FORMAT = new SimpleDateFormat("sS");
/*      */   private static final int SERIE_LINE_WIDTH = 1;
/*   68 */   private Color COLOR_CMP_0 = new Color(PlatformUI.getWorkbench().getDisplay(), 218, 165, 32);
/*   69 */   private Color COLOR_CMP_1 = new Color(PlatformUI.getWorkbench().getDisplay(), 200, 0, 0);
/*   70 */   private Color COLOR_CMP_2 = new Color(PlatformUI.getWorkbench().getDisplay(), 0, 0, 200);
/*   71 */   private Color COLOR_CMP_3 = new Color(PlatformUI.getWorkbench().getDisplay(), 0, 200, 50);
/*   72 */   private Color FONT_COLOR = new Color(PlatformUI.getWorkbench().getDisplay(), 0, 0, 0);
/*   73 */   private Color INFO_FOREGROUND_COLOR = Display.getCurrent().getSystemColor(28);
/*   74 */   private Color INFO_BACKGROUND_COLOR = Display.getCurrent().getSystemColor(29);
/*      */   private HashMap<Byte, DataTrace> data;
/*   77 */   private IAxis[] cmpToAxes = new IAxis[4];
/*   78 */   private ILineSeries[] cmpToSeries = new ILineSeries[4];
/*      */ 
/*   80 */   private Range initRangeX = new Range(0.0D, 3.0D);
/*   81 */   private double yGap = 1.0D;
/*   82 */   private Range initRangeY = new Range(0.0D, this.yGap);
/*   83 */   private double latestTime = 0.0D;
/*      */ 
/*   85 */   private boolean plotSymbolEnabled = false;
/*      */ 
/*   87 */   private double coreClock = 2000000.0D;
/*   88 */   private int scale = 1;
/*   89 */   private int widthInPixels = 0;
/*      */   private int prevEventTime;
/*      */   private ScrollBar hBar;
/*   95 */   private boolean debugIsPaused = false;
/*      */ 
/*   98 */   private Shell toolTip = null;
/*   99 */   private Label toolTipLabel = null;
/*  100 */   private TableViewer toolTipTableViewer = null;
/*  101 */   private Table toolTipTable = null;
/*      */   private int toolTipTime;
/*      */ 
/*      */   public SWVDataTraceChart(Composite parent, int style)
/*      */   {
/*  105 */     super(parent, style);
/*  106 */     setPixelWidth();
/*  107 */     getAxisSet().getYAxis(0).getTitle().setVisible(false);
/*  108 */     getAxisSet().getXAxis(0).getTitle().setVisible(false);
/*  109 */     getLegend().setPosition(128);
/*  110 */     getTitle().setVisible(false);
/*  111 */     getAxisSet().getXAxis(0).getTick().setForeground(this.FONT_COLOR);
/*      */ 
/*  114 */     this.hBar = ((ScrolledComposite)getParent()).getHorizontalBar();
/*  115 */     setNrOfScrollbarSteps();
/*  116 */     this.hBar.setSelection(getNrOfScrollbarSteps());
/*  117 */     this.hBar.addListener(13, this);
/*  118 */     enableScrollBar(false);
/*      */ 
/*  120 */     getPlotArea().addListener(32, this);
/*  121 */     getPlotArea().addListener(3, this);
/*      */ 
/*  123 */     reset();
/*      */   }
/*      */ 
/*      */   public synchronized void refresh2()
/*      */   {
/*  130 */     if (this.data == null)
/*      */     {
/*  132 */       reset();
/*      */     }
/*      */     else {
/*  135 */       IAxis xAxis = getAxisSet().getXAxis(0);
/*      */ 
/*  137 */       double xFirst = xAxis.getRange().lower;
/*      */ 
/*  140 */       double xLast = xAxis.getRange().upper;
/*      */ 
/*  143 */       for (int cmpId = 0; cmpId < 4; cmpId++) {
/*  144 */         DataTrace dataTrace = (DataTrace)this.data.get(Byte.valueOf((byte)cmpId));
/*  145 */         if ((dataTrace != null) && (dataTrace.getComparatorConfig() != null)) {
/*  146 */           double xMax = setOneSerie(dataTrace, xFirst, xLast);
/*  147 */           if (xMax > xLast)
/*  148 */             xLast = xMax;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void timeChanged(long latestCycle)
/*      */   {
/*  161 */     double latestTime = getScale() == 1 ? latestCycle / getCoreClock() : latestCycle;
/*      */ 
/*  163 */     setLatestTime(latestTime);
/*      */ 
/*  166 */     for (ISeries serie : getSeriesSet().getSeries()) {
/*  167 */       double[] vals = serie.getXSeries();
/*  168 */       if (vals.length > 0) {
/*  169 */         vals[(vals.length - 1)] = getLatestTime();
/*  170 */         serie.setXSeries(vals);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  175 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  176 */     Range currentRange = xAxis.getRange();
/*      */ 
/*  178 */     if (getLatestTime() > currentRange.upper) {
/*  179 */       double width = currentRange.upper - currentRange.lower;
/*  180 */       currentRange.upper = getLatestTime();
/*  181 */       currentRange.lower = (getLatestTime() - width);
/*  182 */       xAxis.setRange(currentRange);
/*      */     }
/*  184 */     if (isScrollBarEnabled())
/*      */     {
/*  186 */       enableScrollBar(false);
/*  187 */       setNrOfScrollbarSteps();
/*  188 */       enableScrollBar(true);
/*      */     }
/*      */ 
/*  192 */     redraw();
/*  193 */     update();
/*      */   }
/*      */ 
/*      */   private void moveTo(double xPosition)
/*      */   {
/*  203 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  204 */     Range currentRange = xAxis.getRange();
/*      */ 
/*  206 */     double upper = currentRange.upper;
/*  207 */     double lower = currentRange.lower;
/*  208 */     double width = upper - lower;
/*  209 */     double newUpper = xPosition;
/*  210 */     double newLower = xPosition - width;
/*  211 */     if (newLower < 0.0D) {
/*  212 */       newLower = 0.0D;
/*  213 */       newUpper = width;
/*      */     }
/*  215 */     xAxis.setRange(new Range(newLower, newUpper));
/*      */   }
/*      */ 
/*      */   public void zoomOut()
/*      */   {
/*  223 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  224 */     xAxis.zoomOut();
/*  225 */     if (xAxis.getRange().upper > getLatestTime()) {
/*  226 */       moveTo(getLatestTime());
/*      */     }
/*  228 */     if ((xAxis.getRange().lower < 0.0D) || (xAxis.getRange().upper < getStartTimeWidth())) {
/*  229 */       moveTo(getStartTimeWidth());
/*      */     }
/*  231 */     if ((isDebugPaused()) && (isScrollBarEnabled()) && (getLatestTime() <= getStartTimeWidth() * getCurrentZoomfactor()))
/*      */     {
/*  233 */       enableScrollBar(false);
/*      */     }
/*  235 */     if (isScrollBarEnabled())
/*      */     {
/*  237 */       enableScrollBar(false);
/*  238 */       setNrOfScrollbarSteps();
/*  239 */       enableScrollBar(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void zoomIn()
/*      */   {
/*  247 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  248 */     xAxis.zoomIn();
/*  249 */     if (xAxis.getRange().upper > getLatestTime()) {
/*  250 */       moveTo(getLatestTime());
/*      */     }
/*  252 */     if ((xAxis.getRange().lower < 0.0D) || (xAxis.getRange().upper < getStartTimeWidth())) {
/*  253 */       moveTo(getStartTimeWidth());
/*      */     }
/*  255 */     if ((isDebugPaused()) && (!isScrollBarEnabled()) && (getLatestTime() > getStartTimeWidth() * getCurrentZoomfactor()))
/*      */     {
/*  257 */       enableScrollBar(true);
/*      */     }
/*  259 */     if (isScrollBarEnabled())
/*      */     {
/*  261 */       enableScrollBar(false);
/*  262 */       setNrOfScrollbarSteps();
/*  263 */       enableScrollBar(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private double setOneSerie(DataTrace dataTrace, double xFirst, double xLast)
/*      */   {
/*  275 */     ArrayList valueHistory = dataTrace.getHistory();
/*  276 */     if (valueHistory != null)
/*      */     {
/*  278 */       IAxis yAxis = getAxisForDataTrace(dataTrace);
/*      */ 
/*  281 */       ILineSeries serie = getSeriesForDataTrace(dataTrace);
/*      */ 
/*  284 */       Iterator iter = valueHistory.iterator();
/*  285 */       double[] y = new double[valueHistory.size() + 1];
/*  286 */       double[] x = new double[valueHistory.size() + 1];
/*      */ 
/*  288 */       int i = 0;
/*  289 */       int nrOfValuesInView = 0;
/*      */ 
/*  291 */       double yMin = 0.0D;
/*  292 */       double yMax = 0.0D;
/*      */ 
/*  294 */       while (iter.hasNext()) {
/*  295 */         Data data = (Data)iter.next();
/*  296 */         if (this.hasPerm)
/*  297 */           y[i] = Double.parseDouble(SWVDataTraceUtil.convert(data.getDataValue(), data.getType(), 5));
/*      */         else {
/*  299 */           y[i] = 0.0D;
/*      */         }
/*  301 */         if (getScale() == 1)
/*  302 */           x[i] = (data.getCycles() / getCoreClock());
/*      */         else {
/*  304 */           x[i] = data.getCycles();
/*      */         }
/*      */ 
/*  307 */         if ((x[i] >= xFirst) && (x[i] <= xLast)) {
/*  308 */           nrOfValuesInView++;
/*      */         }
/*      */ 
/*  311 */         if (i > 0) {
/*  312 */           if (y[i] > yMax)
/*  313 */             yMax = y[i];
/*  314 */           if (y[i] < yMin)
/*  315 */             yMin = y[i];
/*      */         } else {
/*  317 */           yMin = yMax = y[0];
/*      */         }
/*  319 */         i++;
/*      */       }
/*      */ 
/*  323 */       if (i > 0) {
/*  324 */         x[i] = x[(i - 1)];
/*  325 */         y[i] = y[(i - 1)];
/*      */       }
/*      */ 
/*  328 */       serie.setYAxisId(yAxis.getId());
/*  329 */       serie.setXSeries(x);
/*  330 */       serie.setYSeries(y);
/*      */ 
/*  333 */       setYRange(yAxis, yMin, yMax);
/*      */ 
/*  336 */       enablePlotSymbol(nrOfValuesInView < getMaxNrPlotSymbols(), serie);
/*  337 */       return x[(x.length - 1)];
/*      */     }
/*  339 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   public void adjustYAxis()
/*      */   {
/*  347 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*      */ 
/*  349 */     double firstXinView = xAxis.getRange().lower;
/*  350 */     double lastXinView = xAxis.getRange().upper;
/*      */ 
/*  353 */     for (int cmpId = 0; cmpId < 4; cmpId++) {
/*  354 */       if (this.cmpToSeries[cmpId] != null) {
/*  355 */         double[] ySerie = this.cmpToSeries[cmpId].getYSeries();
/*  356 */         int lowId = getSerieIndex(firstXinView, this.cmpToSeries[cmpId].getXSeries());
/*  357 */         int highId = getSerieIndex(lastXinView, this.cmpToSeries[cmpId].getXSeries());
/*  358 */         double yMax = ySerie[lowId];
/*  359 */         double yMin = ySerie[lowId];
/*  360 */         for (int i = lowId; i <= highId; i++) {
/*  361 */           if (ySerie[i] > yMax)
/*  362 */             yMax = ySerie[i];
/*  363 */           if (ySerie[i] < yMin)
/*  364 */             yMin = ySerie[i];
/*      */         }
/*  366 */         setYRange(this.cmpToAxes[cmpId], yMin, yMax);
/*      */       }
/*      */     }
/*  369 */     updateLayout();
/*  370 */     redraw();
/*  371 */     update();
/*      */   }
/*      */ 
/*      */   protected int getSerieIndex(double position, double[] xSerie)
/*      */   {
/*  380 */     int x = 0;
/*  381 */     int lastPosition = xSerie.length - 1;
/*  382 */     if (position < xSerie[0]) {
/*  383 */       return 0;
/*      */     }
/*  385 */     if (position > xSerie[lastPosition]) {
/*  386 */       x = lastPosition;
/*  387 */       if (x < 1) {
/*  388 */         x = 1;
/*      */       }
/*  390 */       return x;
/*      */     }
/*      */ 
/*  393 */     while ((x <= lastPosition) && (xSerie[x] <= position)) {
/*  394 */       x++;
/*      */     }
/*  396 */     if (x < 1) {
/*  397 */       x = 1;
/*      */     }
/*  399 */     return x - 1;
/*      */   }
/*      */ 
/*      */   private double getStartTimeWidth()
/*      */   {
/*  409 */     double baseWidth = 15000000.0D * getOptimalPixelWidthMultiplicator() / 1000000.0D;
/*  410 */     return getScale() == 0 ? baseWidth * getCoreClock() : baseWidth;
/*      */   }
/*      */ 
/*      */   private double getCurrentZoomfactor()
/*      */   {
/*  419 */     IAxis xAxis = getAxisSet().getXAxis(0);
/*  420 */     Range currentRange = xAxis.getRange();
/*  421 */     double upper = currentRange.upper;
/*  422 */     double lower = currentRange.lower;
/*  423 */     double width = upper - lower;
/*  424 */     return width / getStartTimeWidth();
/*      */   }
/*      */ 
/*      */   private double getOptimalPixelWidthMultiplicator()
/*      */   {
/*      */     double mult;
/*      */     double mult;
/*  429 */     if (this.widthInPixels == 0)
/*  430 */       mult = 1.0D;
/*      */     else {
/*  432 */       mult = this.widthInPixels / 1550.0D;
/*      */     }
/*  434 */     return mult;
/*      */   }
/*      */ 
/*      */   private int getMaxNrPlotSymbols()
/*      */   {
/*  441 */     if (this.widthInPixels == 0) {
/*  442 */       return 150;
/*      */     }
/*  444 */     return this.widthInPixels * 150 / 1550;
/*      */   }
/*      */ 
/*      */   private IAxis getAxisForDataTrace(DataTrace dataTrace)
/*      */   {
/*  453 */     IAxis yAxis = this.cmpToAxes[dataTrace.getComparatorId()];
/*      */ 
/*  455 */     if (yAxis == null) {
/*  456 */       if (!isAxisSet())
/*  457 */         yAxis = getAxisSet().getYAxis(0);
/*      */       else {
/*  459 */         yAxis = getAxisSet().getYAxis(getAxisSet().createYAxis());
/*      */       }
/*      */ 
/*  462 */       this.cmpToAxes[dataTrace.getComparatorId()] = yAxis;
/*      */ 
/*  464 */       yAxis.getTitle().setVisible(false);
/*  465 */       yAxis.getTick().setForeground(getColor(dataTrace.getComparatorId()));
/*      */     }
/*      */ 
/*  468 */     return yAxis;
/*      */   }
/*      */ 
/*      */   private ILineSeries getSeriesForDataTrace(DataTrace dataTrace)
/*      */   {
/*  475 */     int id = dataTrace.getComparatorId();
/*  476 */     ILineSeries serie = this.cmpToSeries[id];
/*  477 */     String symbol = dataTrace.getComparatorConfig().getSymbol();
/*      */ 
/*  479 */     if ((serie != null) && (!serie.getId().equals(symbol)))
/*      */     {
/*  481 */       ISeriesSet set = getSeriesSet();
/*  482 */       set.deleteSeries(serie.getId());
/*  483 */       serie = null;
/*      */     }
/*      */ 
/*  486 */     if (serie == null) {
/*  487 */       serie = (ILineSeries)getSeriesSet().createSeries(ISeries.SeriesType.LINE, symbol);
/*  488 */       serie.setLineColor(getColor(dataTrace.getComparatorId()));
/*  489 */       serie.setLineWidth(1);
/*      */ 
/*  491 */       if (this.plotSymbolEnabled)
/*  492 */         enablePlotSymbol(true, serie);
/*      */       else {
/*  494 */         enablePlotSymbol(false, serie);
/*      */       }
/*  496 */       serie.enableStep(true);
/*  497 */       this.cmpToSeries[id] = serie;
/*      */     }
/*      */ 
/*  500 */     return serie;
/*      */   }
/*      */ 
/*      */   public void setComparatorToFront(int cmpId)
/*      */   {
/*  508 */     for (int i = 0; i < 4; i++) {
/*  509 */       ILineSeries serie = this.cmpToSeries[i];
/*  510 */       if (serie != null)
/*  511 */         if (i == cmpId) {
/*  512 */           serie.setLineWidth(2);
/*  513 */           getSeriesSet().bringToFront(serie.getId());
/*      */         } else {
/*  515 */           serie.setLineWidth(1);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setLatestTime(double latestTime)
/*      */   {
/*  526 */     if ((this.latestTime < latestTime) || (latestTime == 0.0D))
/*  527 */       this.latestTime = latestTime;
/*      */   }
/*      */ 
/*      */   private double getLatestTime()
/*      */   {
/*  535 */     return this.latestTime;
/*      */   }
/*      */ 
/*      */   private Color getColor(byte cmpId)
/*      */   {
/*  544 */     switch (cmpId) { case 0:
/*  545 */       return this.COLOR_CMP_0;
/*      */     case 1:
/*  546 */       return this.COLOR_CMP_1;
/*      */     case 2:
/*  547 */       return this.COLOR_CMP_2;
/*      */     case 3:
/*  548 */       return this.COLOR_CMP_3; }
/*  549 */     return this.COLOR_CMP_0;
/*      */   }
/*      */ 
/*      */   public void setCoreClock(double coreClock)
/*      */   {
/*  557 */     this.coreClock = coreClock;
/*      */   }
/*      */ 
/*      */   public double getCoreClock()
/*      */   {
/*  564 */     return this.coreClock;
/*      */   }
/*      */ 
/*      */   public void enableScrollBar(boolean onOff)
/*      */   {
/*  573 */     if ((onOff) && (!isDebugPaused()))
/*      */     {
/*  575 */       onOff = false;
/*      */     }
/*  577 */     if ((onOff) && (getLatestTime() <= getStartTimeWidth() * getCurrentZoomfactor()))
/*      */     {
/*  579 */       onOff = false;
/*      */     }
/*  581 */     if (isScrollBarEnabled() != onOff) {
/*  582 */       if (!isSerieSet()) onOff = false;
/*  583 */       this.hBar.setEnabled(onOff);
/*  584 */       this.hBar.setVisible(onOff);
/*  585 */       if (onOff) {
/*  586 */         setNrOfScrollbarSteps();
/*  587 */         this.hBar.setSelection(getNrOfScrollbarSteps());
/*      */       }
/*      */     }
/*  590 */     update();
/*  591 */     updateLayout();
/*  592 */     redraw();
/*  593 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/*  594 */       getToolTip().dispose();
/*      */   }
/*      */ 
/*      */   public boolean isScrollBarEnabled()
/*      */   {
/*  601 */     if ((this.hBar == null) || (this.hBar.isDisposed())) {
/*  602 */       return false;
/*      */     }
/*  604 */     return this.hBar.getEnabled();
/*      */   }
/*      */ 
/*      */   public void setDebugIsPaused(boolean onOff)
/*      */   {
/*  612 */     this.debugIsPaused = onOff;
/*  613 */     enableScrollBar(onOff);
/*      */   }
/*      */ 
/*      */   private boolean isDebugPaused()
/*      */   {
/*  620 */     return this.debugIsPaused;
/*      */   }
/*      */ 
/*      */   private void setNrOfScrollbarSteps()
/*      */   {
/*  630 */     double currentOptimalTimeWidth = getStartTimeWidth() * getCurrentZoomfactor();
/*  631 */     double preliminaryNrOfScrollbarSteps = getLatestTime() / currentOptimalTimeWidth * this.widthInPixels;
/*  632 */     this.nrOfScrollbarSteps = ((int)(preliminaryNrOfScrollbarSteps <= 100000000.0D ? preliminaryNrOfScrollbarSteps : 100000000.0D));
/*      */ 
/*  634 */     if (this.hBar.getMaximum() < getNrOfScrollbarSteps()) {
/*  635 */       this.hBar.setMaximum(getNrOfScrollbarSteps());
/*      */     }
/*  637 */     if (this.hBar.getMinimum() < 0) {
/*  638 */       this.hBar.setMinimum(0);
/*      */     }
/*  640 */     this.hBar.setPageIncrement(getNrOfScrollbarSteps() / 50);
/*  641 */     this.hBar.setIncrement(getNrOfScrollbarSteps() / 200);
/*      */   }
/*      */ 
/*      */   private int getNrOfScrollbarSteps()
/*      */   {
/*  648 */     return this.nrOfScrollbarSteps == 0 ? 1 : this.nrOfScrollbarSteps;
/*      */   }
/*      */ 
/*      */   public double getScrollbarSelection()
/*      */   {
/*  656 */     double minX = getStartTimeWidth() * getCurrentZoomfactor();
/*  657 */     int hSelection = this.hBar.getSelection();
/*  658 */     int selectableScrollbarSteps = getNrOfScrollbarSteps() - this.hBar.getSize().x;
/*  659 */     return hSelection * (getLatestTime() - minX) / selectableScrollbarSteps + minX;
/*      */   }
/*      */ 
/*      */   public void switchScale()
/*      */   {
/*  666 */     double currentTime = getAxisSet().getXAxis(0).getRange().upper;
/*  667 */     if (getScale() == 0) {
/*  668 */       setScale(1);
/*  669 */       this.latestTime /= getCoreClock();
/*  670 */       currentTime /= getCoreClock();
/*      */     } else {
/*  672 */       setScale(0);
/*  673 */       this.latestTime *= getCoreClock();
/*  674 */       currentTime *= getCoreClock();
/*      */     }
/*  676 */     resetOptimalWidth();
/*  677 */     refresh2();
/*  678 */     moveTo(currentTime);
/*  679 */     redraw();
/*  680 */     update();
/*  681 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/*  682 */       getToolTip().dispose();
/*      */   }
/*      */ 
/*      */   private int getScale()
/*      */   {
/*  690 */     return this.scale;
/*      */   }
/*      */ 
/*      */   private void setScale(int scaleType)
/*      */   {
/*  698 */     this.scale = scaleType;
/*      */   }
/*      */ 
/*      */   private void reset()
/*      */   {
/*  710 */     for (ISeries serie : getSeriesSet().getSeries()) {
/*  711 */       getSeriesSet().deleteSeries(serie.getId());
/*      */     }
/*      */ 
/*  715 */     for (IAxis axis : getAxisSet().getYAxes()) {
/*  716 */       if (axis.getId() != 0) {
/*  717 */         getAxisSet().deleteYAxis(axis.getId());
/*      */       }
/*      */     }
/*      */ 
/*  721 */     for (int i = 0; i < 4; i++) {
/*  722 */       this.cmpToAxes[i] = null;
/*  723 */       this.cmpToSeries[i] = null;
/*      */     }
/*      */ 
/*  726 */     setLatestTime(0.0D);
/*  727 */     resetOptimalWidth();
/*  728 */     getAxisSet().getYAxis(0).setRange(this.initRangeY);
/*  729 */     getAxisSet().getYAxis(0).getTick().setForeground(this.FONT_COLOR);
/*      */ 
/*  732 */     redraw();
/*  733 */     update();
/*      */   }
/*      */ 
/*      */   private void resetOptimalWidth()
/*      */   {
/*  740 */     setPixelWidth();
/*      */ 
/*  742 */     this.initRangeX = new Range(0.0D, getStartTimeWidth());
/*  743 */     getAxisSet().getXAxis(0).setRange(this.initRangeX);
/*      */   }
/*      */ 
/*      */   public synchronized void setInput(HashMap<Byte, DataTrace> data)
/*      */   {
/*  751 */     this.data = data;
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/*  759 */     super.dispose();
/*  760 */     this.COLOR_CMP_0.dispose();
/*  761 */     this.COLOR_CMP_1.dispose();
/*  762 */     this.COLOR_CMP_2.dispose();
/*  763 */     this.COLOR_CMP_3.dispose();
/*  764 */     this.FONT_COLOR.dispose();
/*      */   }
/*      */ 
/*      */   public void enablePlotSymbol(boolean onOff, ILineSeries serie)
/*      */   {
/*  774 */     if (!this.plotSymbolEnabled) {
/*  775 */       serie.setSymbolType(ILineSeries.PlotSymbolType.NONE);
/*  776 */       return;
/*      */     }
/*  778 */     ILineSeries.PlotSymbolType currentPlotSymbol = serie.getSymbolType();
/*  779 */     if ((onOff) && (currentPlotSymbol != ILineSeries.PlotSymbolType.CROSS))
/*  780 */       serie.setSymbolType(ILineSeries.PlotSymbolType.CROSS);
/*  781 */     else if ((!onOff) && (currentPlotSymbol != ILineSeries.PlotSymbolType.NONE))
/*  782 */       serie.setSymbolType(ILineSeries.PlotSymbolType.NONE);
/*      */   }
/*      */ 
/*      */   public void enablePlotSymbol(boolean onOff)
/*      */   {
/*  791 */     if (onOff) {
/*  792 */       this.plotSymbolEnabled = onOff;
/*      */     }
/*      */ 
/*  795 */     for (ISeries serie : getSeriesSet().getSeries()) {
/*  796 */       if ((serie instanceof ILineSeries)) {
/*  797 */         enablePlotSymbol(onOff, (ILineSeries)serie);
/*      */       }
/*      */     }
/*  800 */     this.plotSymbolEnabled = onOff;
/*      */   }
/*      */ 
/*      */   private boolean isSerieSet()
/*      */   {
/*  808 */     for (int i = 0; i < 4; i++) {
/*  809 */       if (this.cmpToSeries[i] != null) {
/*  810 */         return true;
/*      */       }
/*      */     }
/*  813 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isAxisSet()
/*      */   {
/*  821 */     for (int i = 0; i < 4; i++) {
/*  822 */       if (this.cmpToAxes[i] != null) {
/*  823 */         return true;
/*      */       }
/*      */     }
/*  826 */     return false;
/*      */   }
/*      */ 
/*      */   private void setYRange(IAxis yAxis, double yMin, double yMax)
/*      */   {
/*  839 */     if (yMin == yMax) {
/*  840 */       yMin = yMax + this.yGap;
/*      */     }
/*  842 */     if (yMin == 0.0D) {
/*  843 */       yMin = -0.15D;
/*      */     }
/*  845 */     if (yMax == 0.0D) {
/*  846 */       yMax = 0.15D;
/*      */     }
/*  848 */     double gap = yMax - yMin;
/*      */ 
/*  850 */     yAxis.setRange(new Range(yMin - gap * 0.02D, yMax + gap * 0.02D));
/*      */   }
/*      */ 
/*      */   public void setPixelWidth()
/*      */   {
/*  857 */     Rectangle rect = getPlotArea().getBounds();
/*  858 */     this.widthInPixels = ((rect != null) && (rect.width != 0) ? rect.width : 1500);
/*      */   }
/*      */ 
/*      */   public void openSaveAsDialog()
/*      */   {
/*  879 */     FileDialog dialog = new FileDialog(getShell(), 8192);
/*  880 */     dialog.setText(Messages.SAVE_AS_DIALOG_TITLE);
/*  881 */     dialog.setFilterExtensions(EXTENSIONS);
/*  882 */     String filename = dialog.open();
/*  883 */     if (filename == null)
/*      */       return;
/*      */     int format;
/*      */     int format;
/*  887 */     if ((filename.endsWith(".jpg")) || (filename.endsWith(".jpeg"))) {
/*  888 */       format = 4;
/*      */     }
/*      */     else
/*      */     {
/*      */       int format;
/*  889 */       if (filename.endsWith(".png"))
/*  890 */         format = 5;
/*      */       else
/*  892 */         format = -1;
/*      */     }
/*  894 */     if (format != -1)
/*  895 */       save(filename, format);
/*      */   }
/*      */ 
/*      */   private void setToolTip(Shell toolTip)
/*      */   {
/*  907 */     this.toolTip = toolTip;
/*      */   }
/*      */ 
/*      */   public Shell getToolTip()
/*      */   {
/*  914 */     return this.toolTip;
/*      */   }
/*      */ 
/*      */   private Data[] toolTipData(long timeInCycle)
/*      */   {
/*  923 */     Data[] toolTipValuesIn = new Data[4];
/*  924 */     int i = 0;
/*  925 */     if (this.data != null) {
/*  926 */       for (int cmpId = 0; cmpId < 4; cmpId++) {
/*  927 */         DataTrace dataTrace = (DataTrace)this.data.get(Byte.valueOf((byte)cmpId));
/*  928 */         if ((dataTrace != null) && (dataTrace.getComparatorConfig() != null)) {
/*  929 */           if ((dataTrace.getSWVCoreClock() == 0) || (dataTrace.getSWVCoreClock() < 1)) {
/*  930 */             dataTrace.setSWVCoreClock((int)getCoreClock());
/*      */           }
/*  932 */           int id = dataTrace.findDataId(timeInCycle);
/*  933 */           if (id >= 0) {
/*  934 */             toolTipValuesIn[i] = ((Data)dataTrace.getHistory().get(id));
/*  935 */             i++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  940 */     if (i == 4) {
/*  941 */       return toolTipValuesIn;
/*      */     }
/*      */ 
/*  944 */     return (Data[])Arrays.copyOf(toolTipValuesIn, i);
/*      */   }
/*      */ 
/*      */   public void handleEvent(Event event)
/*      */   {
/*  956 */     switch (event.type) {
/*      */     case 13:
/*  958 */       if (isScrollBarEnabled()) {
/*  959 */         int eventTime = Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date()));
/*  960 */         int diff = eventTime - getPrevEventTime();
/*  961 */         if ((diff > 100) || (diff < -100)) {
/*  962 */           handleScrollBarEvent(event);
/*      */         }
/*  964 */         setPrevEventTime(eventTime);
/*      */       }
/*  966 */       break;
/*      */     case 11:
/*  968 */       handleResizeEvent();
/*  969 */       updateLayout();
/*  970 */       redraw();
/*  971 */       break;
/*      */     case 3:
/*  984 */       if (isScrollBarEnabled()) {
/*  985 */         handleToolTipEvent(event);
/*      */       }
/*  987 */       break;
/*      */     case 32:
/*  989 */       if (isScrollBarEnabled()) {
/*  990 */         int eventTime = Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date()));
/*  991 */         int diff = eventTime - getPrevEventTime();
/*  992 */         if ((diff > 100) || (diff < -100)) {
/*  993 */           diff = eventTime - getToolTipTime();
/*  994 */           if (((diff > 1500) || (diff < -1500)) && 
/*  995 */             (getToolTip() != null) && (!getToolTip().isDisposed())) {
/*  996 */             getToolTip().dispose();
/*      */           }
/*      */         }
/*  999 */         setPrevEventTime(eventTime);
/*      */       }
/* 1001 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setPrevEventTime(int prevEventTime)
/*      */   {
/* 1011 */     this.prevEventTime = prevEventTime;
/*      */   }
/*      */ 
/*      */   private int getPrevEventTime()
/*      */   {
/* 1018 */     return this.prevEventTime;
/*      */   }
/*      */ 
/*      */   private void setToolTipTime(int toolTipTime)
/*      */   {
/* 1025 */     this.toolTipTime = toolTipTime;
/*      */   }
/*      */ 
/*      */   private int getToolTipTime()
/*      */   {
/* 1032 */     return this.toolTipTime;
/*      */   }
/*      */ 
/*      */   private void handleScrollBarEvent(Event event)
/*      */   {
/* 1039 */     double selectedX = getScrollbarSelection();
/* 1040 */     if (selectedX == 0.0D) {
/* 1041 */       selectedX = 0.0D;
/*      */     }
/* 1043 */     moveTo(selectedX);
/* 1044 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1045 */       getToolTip().dispose();
/* 1046 */     redraw();
/*      */   }
/*      */ 
/*      */   private void handleResizeEvent()
/*      */   {
/* 1056 */     IAxis xAxis = getAxisSet().getXAxis(0);
/* 1057 */     Range oldRange = xAxis.getRange();
/* 1058 */     double xMax = oldRange.upper;
/* 1059 */     double oldXMin = oldRange.lower;
/* 1060 */     double oldWidth = xMax - oldXMin;
/* 1061 */     double oldMultiplicator = getOptimalPixelWidthMultiplicator();
/* 1062 */     double oldPixelWidth = this.widthInPixels;
/* 1063 */     setPixelWidth();
/* 1064 */     if (oldPixelWidth != this.widthInPixels)
/*      */     {
/* 1066 */       double newWidth = oldWidth / oldMultiplicator * getOptimalPixelWidthMultiplicator();
/* 1067 */       double newXMin = xMax - newWidth;
/* 1068 */       if ((newXMin < 0.0D) || (oldXMin == 0.0D)) {
/* 1069 */         newXMin = 0.0D;
/* 1070 */         xMax = newWidth;
/*      */       }
/* 1072 */       Range range = new Range(newXMin, xMax);
/* 1073 */       if (isDebugPaused()) {
/* 1074 */         if ((isScrollBarEnabled()) && (getLatestTime() <= getStartTimeWidth() * getCurrentZoomfactor()))
/*      */         {
/* 1076 */           enableScrollBar(false);
/* 1077 */         } else if ((!isScrollBarEnabled()) && (getLatestTime() > getStartTimeWidth() * getCurrentZoomfactor()))
/*      */         {
/* 1079 */           enableScrollBar(true);
/*      */         }
/*      */       }
/*      */ 
/* 1083 */       getAxisSet().getXAxis(0).setRange(range);
/*      */     }
/* 1085 */     if (isScrollBarEnabled())
/*      */     {
/* 1087 */       enableScrollBar(false);
/* 1088 */       setNrOfScrollbarSteps();
/* 1089 */       enableScrollBar(true);
/*      */     }
/* 1091 */     if ((getToolTip() != null) && (!getToolTip().isDisposed()))
/* 1092 */       getToolTip().dispose();
/*      */   }
/*      */ 
/*      */   private void handleToolTipEvent(Event event) {
/* 1096 */     if ((getToolTip() != null) && (!getToolTip().isDisposed())) {
/* 1097 */       getToolTip().dispose();
/*      */     }
/* 1099 */     if (isDebugPaused())
/*      */     {
/* 1101 */       Double x = Double.valueOf(getAxisSet().getXAxis(0).getDataCoordinate(event.x));
/* 1102 */       long cycleTime = getScale() == 1 ? ()(x.doubleValue() * ()getCoreClock()) : x.longValue();
/* 1103 */       Data[] toolTipValues = toolTipData(cycleTime);
/* 1104 */       DecimalFormat noPlaces = getScale() == 1 ? new DecimalFormat("0.000") : new DecimalFormat("0");
/* 1105 */       String toolTipText = "  " + noPlaces.format(x) + " " + (getScale() == 1 ? Messages.SWVDataTraceChart_SECONDS : Messages.SWVDataTraceChart_CYCLES) + "\n";
/* 1106 */       setToolTip(new Shell(getShell(), 16388));
/*      */ 
/* 1108 */       getToolTip().setForeground(this.INFO_FOREGROUND_COLOR);
/* 1109 */       getToolTip().setBackground(this.INFO_BACKGROUND_COLOR);
/* 1110 */       this.toolTipLabel = new Label(getToolTip(), 0);
/* 1111 */       this.toolTipLabel.setForeground(this.INFO_FOREGROUND_COLOR);
/* 1112 */       this.toolTipLabel.setBackground(this.INFO_BACKGROUND_COLOR);
/* 1113 */       this.toolTipLabel.setText(toolTipText);
/*      */ 
/* 1115 */       this.toolTipTableViewer = new TableViewer(getToolTip(), 67584);
/*      */ 
/* 1117 */       TableViewerColumn tableViewerColumn = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 1118 */       TableColumn tblclmnComparator = tableViewerColumn.getColumn();
/* 1119 */       tblclmnComparator.setWidth(70);
/* 1120 */       tblclmnComparator.setText(Messages.SWVDataTraceChart_NAME);
/*      */ 
/* 1122 */       TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 1123 */       TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
/* 1124 */       tblclmnValue.setWidth(70);
/* 1125 */       tblclmnValue.setText(Messages.SWVDataTraceChart_VALUE);
/*      */ 
/* 1127 */       TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 1128 */       TableColumn tblclmnPc = tableViewerColumn_2.getColumn();
/* 1129 */       tblclmnPc.setWidth(70);
/* 1130 */       tblclmnPc.setText(Messages.SWVDataTraceChart_PC);
/*      */ 
/* 1132 */       TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 1133 */       TableColumn tblclmnCycles = tableViewerColumn_3.getColumn();
/* 1134 */       tblclmnCycles.setWidth(70);
/* 1135 */       tblclmnCycles.setText(Messages.SWVDataTraceChart_CYCLES);
/*      */ 
/* 1137 */       TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.toolTipTableViewer, 0);
/* 1138 */       TableColumn tblclmnSeconds = tableViewerColumn_4.getColumn();
/* 1139 */       tblclmnSeconds.setWidth(90);
/* 1140 */       tblclmnSeconds.setText(Messages.SWVDataTraceChart_SECONDS);
/*      */ 
/* 1142 */       this.toolTipTableViewer.setContentProvider(new SWVDataTraceChartToolTipContentProvider());
/* 1143 */       this.toolTipTableViewer.setLabelProvider(new SWVDataTraceChartToolTipLabelProvider());
/* 1144 */       if (toolTipValues != null) {
/* 1145 */         this.toolTipTableViewer.setInput(toolTipValues);
/*      */       }
/*      */ 
/* 1148 */       getToolTip().setLayout(new GridLayout());
/*      */ 
/* 1150 */       this.toolTipTable = this.toolTipTableViewer.getTable();
/* 1151 */       this.toolTipTable.setLinesVisible(true);
/* 1152 */       this.toolTipTable.setHeaderVisible(true);
/* 1153 */       this.toolTipTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 1154 */       this.toolTipTable.setForeground(this.INFO_FOREGROUND_COLOR);
/* 1155 */       this.toolTipTable.setBackground(this.INFO_BACKGROUND_COLOR);
/*      */ 
/* 1157 */       Point toolTipSize = getToolTip().computeSize(-1, -1);
/* 1158 */       Rectangle rect = getPlotArea().getBounds();
/* 1159 */       Point pt = toDisplay(rect.x, rect.y);
/* 1160 */       int xStart = pt.x + event.x + 2;
/* 1161 */       int xPlotSize = getPlotArea().getSize().x;
/* 1162 */       if (xStart + toolTipSize.x > xPlotSize + pt.x) {
/* 1163 */         xStart = xPlotSize + pt.x - toolTipSize.x;
/* 1164 */         if (xStart < 0) xStart = 0;
/*      */       }
/* 1166 */       int yStart = pt.y + event.y - toolTipSize.y - 9;
/* 1167 */       getToolTip().setBounds(xStart, yStart, toolTipSize.x + 2, toolTipSize.y + 1);
/* 1168 */       getToolTip().setVisible(true);
/* 1169 */       this.toolTipLabel.addListener(7, new Listener()
/*      */       {
/*      */         public void handleEvent(Event inner_event)
/*      */         {
/* 1173 */           Shell tip = ((Control)inner_event.widget).getShell();
/* 1174 */           switch (inner_event.type) {
/*      */           case 7:
/* 1176 */             tip.dispose();
/*      */           }
/*      */         }
/*      */       });
/* 1181 */       getToolTip().addListener(3, new Listener()
/*      */       {
/*      */         public void handleEvent(Event inner_event)
/*      */         {
/* 1185 */           inner_event.widget.dispose();
/*      */         }
/*      */       });
/* 1188 */       setToolTipTime(Integer.parseInt(this.MILISECONDS_FORMAT.format(new Date())));
/*      */     }
/*      */   }
/*      */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatraceTimeline.SWVDataTraceChart
 * JD-Core Version:    0.6.2
 */