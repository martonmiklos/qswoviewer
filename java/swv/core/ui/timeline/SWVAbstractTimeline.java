/*     */ package com.atollic.truestudio.swv.core.ui.timeline;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.Messages;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView.PeriodicUpdate;
/*     */ import com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.custom.ScrolledComposite;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.layout.FillLayout;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.ScrollBar;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ 
/*     */ public abstract class SWVAbstractTimeline extends SWVView
/*     */ {
/*  39 */   protected final boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV);
/*     */   private Action actionOpenSaveTimelineChart;
/*     */   private Image actionOpenSaveTimelineChartImage;
/*     */   private Action actionOpenSwitchScale;
/*     */   private Image actionOpenSwitchScaleImage;
/*     */   private Action actionOpenEnableTitles;
/*     */   private Image actionOpenDisableTitlesImage;
/*     */   private Action actionOpenShorterYAxis;
/*     */   private Image actionOpenShorterYAxisImage;
/*     */   private Action actionOpenLongerYAxis;
/*     */   private Image actionOpenLongerYAxisImage;
/*     */   private Action actionOpenAdjustYAxis;
/*     */   private Image actionOpenAdjustYAxisImage;
/*     */   private Action actionOpenLessTimeInBars;
/*     */   private Image actionOpenLessTimeInBarsImage;
/*     */   private Action actionOpenMoreTimeInBars;
/*     */   private Image actionOpenMoreTimeInBarsImage;
/*     */   private Composite my_parent;
/*     */   private SWVClient currentClient;
/*     */   private ILaunchConfigurationWorkingCopy currentConfig;
/*     */   private boolean timeStampIsEnabled;
/*     */   SWVView.PeriodicUpdate periodicUpdate;
/*  63 */   private boolean viewDisposed = false;
/*     */   private boolean valuesIsInitialized;
/*  65 */   private long initial_run_size = 500000000L;
/*     */   private long current_run_size;
/*     */   public static final double ZOOM_IN_FACTOR = 0.6666666666666666D;
/*     */   public static final double ZOOM_OUT_FACTOR = 1.5D;
/*     */   private static final int OPTIMAL_NR_BARS = 150;
/*  71 */   private long timeSlot = this.initial_run_size / 150L;
/*     */   public static final long MIN_TIME_SLOT = 5L;
/*     */   public static final long MAX_TIME_SLOT = 480000000L;
/*  74 */   private static final long DEFAULT_GRAPH_WIDTH_IN_MICROSECONDS = SWVTimelineChart.OPTIMAL_GRAPH_WIDTH_IN_MICROSECONDS_AT_1600_PIXELS;
/*     */   public static final double MIN_TIME_SPAN_IN_VIEW = 1.1D;
/*     */   private ScrolledComposite chartComposite;
/*     */   private SWVTimelineChart chart;
/*     */   private SWVStatisticalTimelineBuckets buckets;
/*     */   private String oldCacheHash;
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  88 */     this.my_parent = parent;
/*  89 */     super.createPartControl(this.my_parent);
/*     */ 
/*  91 */     createActions();
/*  92 */     parent.setLayout(new FillLayout(512));
/*     */ 
/*  94 */     Composite baseComposite = new Composite(parent, 536870912);
/*  95 */     baseComposite.setLayout(new GridLayout(1, false));
/*  96 */     baseComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/*  99 */     if (!this.hasPerm) {
/* 100 */       DemoHelper.createDemoModeLabel(baseComposite, "swv-generic");
/*     */     }
/*     */ 
/* 103 */     this.chartComposite = new ScrolledComposite(baseComposite, 256);
/* 104 */     this.chartComposite.setExpandVertical(true);
/* 105 */     this.chartComposite.setAlwaysShowScrollBars(true);
/* 106 */     this.chartComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 107 */     this.chartComposite.setExpandHorizontal(true);
/*     */ 
/* 109 */     this.chart = new SWVTimelineChart(this.chartComposite, 1, title(), getGraphColor());
/* 110 */     this.chart.getPlotArea().addListener(8, new OpenAdjustTimeInBars(this, 0.6666666666666666D));
/* 111 */     this.chart.setHasPerm(this.hasPerm);
/* 112 */     this.chartComposite.setContent(this.chart);
/* 113 */     this.chartComposite.setMinSize(this.chart.computeSize(-1, -1));
/*     */ 
/* 116 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 117 */     setCurrentClient(sessionManager.getClient());
/* 118 */     this.chart.setDebugIsPaused(false);
/*     */ 
/* 120 */     this.valuesIsInitialized = false;
/*     */ 
/* 124 */     ScrollBar hBar = this.chartComposite.getHorizontalBar();
/* 125 */     Listener[] old_list = hBar.getListeners(13);
/* 126 */     for (Listener list : old_list) {
/* 127 */       hBar.removeListener(13, list);
/*     */     }
/*     */ 
/* 130 */     hBar.addListener(13, this.chart);
/*     */ 
/* 134 */     if (getCurrentClient() != null)
/* 135 */       handleSWVContext();
/*     */     else {
/* 137 */       handleNoSWVContext();
/*     */     }
/*     */ 
/* 140 */     setCurrentConfig(sessionManager.getActiveConfiguration());
/*     */ 
/* 143 */     startPeriodicUpdate(150);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 152 */     IActionBars bars = getViewSite().getActionBars();
/* 153 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/*     */ 
/* 155 */     this.actionOpenMoreTimeInBars = new OpenAdjustTimeInBars(this, 1.5D);
/* 156 */     this.actionOpenMoreTimeInBarsImage = getImage("time_zoom_out.gif");
/* 157 */     this.actionOpenMoreTimeInBars.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenMoreTimeInBarsImage));
/* 158 */     this.actionOpenMoreTimeInBars.setToolTipText(Messages.LONGER_TIME_STEPS);
/* 159 */     this.actionOpenMoreTimeInBars.setText(Messages.LONGER_TIME_STEPS);
/* 160 */     this.actionOpenMoreTimeInBars.setId("openMoreTimeInBars");
/* 161 */     toolBarManager.insertBefore("openConf", this.actionOpenMoreTimeInBars);
/* 162 */     this.actionOpenMoreTimeInBars.setEnabled(false);
/*     */ 
/* 164 */     this.actionOpenLessTimeInBars = new OpenAdjustTimeInBars(this, 0.6666666666666666D);
/* 165 */     this.actionOpenLessTimeInBarsImage = getImage("time_zoom_in.gif");
/* 166 */     this.actionOpenLessTimeInBars.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenLessTimeInBarsImage));
/* 167 */     this.actionOpenLessTimeInBars.setToolTipText(Messages.SHORTER_TIME_STEPS);
/* 168 */     this.actionOpenLessTimeInBars.setText(Messages.SHORTER_TIME_STEPS);
/* 169 */     this.actionOpenLessTimeInBars.setId("openLessTimeInBars");
/* 170 */     toolBarManager.insertBefore("openMoreTimeInBars", this.actionOpenLessTimeInBars);
/* 171 */     this.actionOpenLessTimeInBars.setEnabled(false);
/*     */ 
/* 173 */     this.actionOpenLongerYAxis = new OpenChangeYAxis(this, 0.1D);
/* 174 */     this.actionOpenLongerYAxisImage = getImage("y_increase.gif");
/* 175 */     this.actionOpenLongerYAxis.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenLongerYAxisImage));
/* 176 */     this.actionOpenLongerYAxis.setToolTipText(Messages.LONGER_Y_AXIS);
/* 177 */     this.actionOpenLongerYAxis.setText(Messages.LONGER_Y_AXIS);
/* 178 */     this.actionOpenLongerYAxis.setId("OpenLongerYAxis");
/* 179 */     toolBarManager.insertBefore("openLessTimeInBars", this.actionOpenLongerYAxis);
/* 180 */     this.actionOpenLongerYAxis.setEnabled(false);
/*     */ 
/* 182 */     this.actionOpenShorterYAxis = new OpenChangeYAxis(this, -0.1D);
/* 183 */     this.actionOpenShorterYAxisImage = getImage("y_reduce.gif");
/* 184 */     this.actionOpenShorterYAxis.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenShorterYAxisImage));
/* 185 */     this.actionOpenShorterYAxis.setToolTipText(Messages.SHORTER_Y_AXIS);
/* 186 */     this.actionOpenShorterYAxis.setText(Messages.SHORTER_Y_AXIS);
/* 187 */     this.actionOpenShorterYAxis.setId("OpenShorterYAxis");
/* 188 */     toolBarManager.insertBefore("OpenLongerYAxis", this.actionOpenShorterYAxis);
/* 189 */     this.actionOpenShorterYAxis.setEnabled(false);
/*     */ 
/* 191 */     this.actionOpenAdjustYAxis = new OpenAdjustYAxis(this);
/* 192 */     this.actionOpenAdjustYAxisImage = getImage("alignheight.gif");
/* 193 */     this.actionOpenAdjustYAxis.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenAdjustYAxisImage));
/* 194 */     this.actionOpenAdjustYAxis.setToolTipText(Messages.ADJUST_Y_AXIS);
/* 195 */     this.actionOpenAdjustYAxis.setText(Messages.ADJUST_Y_AXIS);
/* 196 */     this.actionOpenAdjustYAxis.setId("OpenAdjustYAxis");
/* 197 */     toolBarManager.insertBefore("OpenShorterYAxis", this.actionOpenAdjustYAxis);
/*     */ 
/* 199 */     this.actionOpenSwitchScale = new OpenSwitchScale(this);
/* 200 */     this.actionOpenSwitchScaleImage = getImage("time_obj.png");
/* 201 */     this.actionOpenSwitchScale.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenSwitchScaleImage));
/* 202 */     this.actionOpenSwitchScale.setToolTipText(Messages.SWITCH_BETWEEN_TIME_AND_CYCLE_SCALE);
/* 203 */     this.actionOpenSwitchScale.setText(Messages.SWITCH_BETWEEN_TIME_AND_CYCLE_SCALE);
/* 204 */     this.actionOpenSwitchScale.setId("actionOpenSwitchScale");
/* 205 */     toolBarManager.insertBefore("OpenAdjustYAxis", this.actionOpenSwitchScale);
/*     */ 
/* 207 */     this.actionOpenSaveTimelineChart = new OpenSaveTimelineChart(this);
/* 208 */     this.actionOpenSaveTimelineChartImage = getImage("capturescreen.gif");
/* 209 */     this.actionOpenSaveTimelineChart.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenSaveTimelineChartImage));
/* 210 */     this.actionOpenSaveTimelineChart.setToolTipText(Messages.SAVE_CHART_AS_IMAGE);
/* 211 */     this.actionOpenSaveTimelineChart.setText(Messages.SAVE_CHART_AS_IMAGE);
/* 212 */     this.actionOpenSaveTimelineChart.setId("actionOpenSaveTimelineChart");
/* 213 */     toolBarManager.insertBefore("actionOpenSwitchScale", this.actionOpenSaveTimelineChart);
/*     */ 
/* 215 */     if (!title().equals("")) {
/* 216 */       this.actionOpenEnableTitles = new OpenEnableTitles(this, getImage("title_enable.png"), getImage("title_disable.png"));
/* 217 */       this.actionOpenDisableTitlesImage = getImage("title_disable.png");
/* 218 */       this.actionOpenEnableTitles.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenDisableTitlesImage));
/* 219 */       this.actionOpenEnableTitles.setToolTipText(Messages.DISABLE_TITLES);
/* 220 */       this.actionOpenEnableTitles.setText(Messages.DISABLE_TITLES);
/* 221 */       this.actionOpenEnableTitles.setId("actionOpenEnableTitles");
/* 222 */       toolBarManager.insertBefore("actionOpenSaveTimelineChart", this.actionOpenEnableTitles);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 236 */     if ((getCurrentClient() != null) && (this.chart != null) && (!this.chart.isDisposed())) {
/* 237 */       if (!isTimestampEnabled()) {
/* 238 */         handleNoSWVContext();
/*     */       } else {
/* 240 */         if (this.chart.isSizeToBeChanged()) {
/* 241 */           this.chart.setPixelWidth();
/* 242 */           this.chart.setSpan(this.chart.getXMin(), this.chart.getXMin() + this.chart.getOptimalWidth());
/* 243 */           this.chart.setSizeIsToBeChanged(false);
/*     */         }
/* 245 */         if (isDebugPaused())
/*     */         {
/* 247 */           this.chart.setSeriesFromBuckets(getBucketsCache(0L));
/* 248 */           this.chart.setDebugIsPaused(true);
/*     */         }
/*     */         else {
/* 251 */           double timeSpanInView = this.chart.getOptimalWidthInSeconds();
/* 252 */           if (timeSpanInView < 1.1D)
/*     */           {
/* 254 */             new OpenAdjustTimeInBars(this, 1.5D).zoomOut();
/*     */           }
/* 256 */           this.current_run_size = (()(getCurrentClient().getSWVCoreClock() * timeSpanInView));
/* 257 */           this.chart.setSeriesFromBuckets(getBucketsCache(this.current_run_size));
/* 258 */           this.chart.showAll();
/* 259 */           this.chart.setDebugIsPaused(false);
/*     */         }
/* 261 */         this.chart.update();
/*     */       }
/* 263 */       if (!this.actionOpenLongerYAxis.isEnabled())
/* 264 */         setButtonEnabled(true);
/*     */     }
/* 266 */     else if (this.actionOpenLongerYAxis.isEnabled()) {
/* 267 */       setButtonEnabled(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleTargetResume()
/*     */   {
/* 274 */     super.handleTargetResume();
/* 275 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 276 */     setCurrentConfig(sessionManager.getActiveConfiguration());
/* 277 */     if ((this.chart.getToolTip() != null) && (!this.chart.getToolTip().isDisposed()))
/* 278 */       this.chart.getToolTip().dispose();
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 284 */     super.handleTargetSuspended();
/* 285 */     if (getCurrentClient() != null) {
/* 286 */       SWVStatisticalTimelineBuckets local_buckets = getBucketsCache(0L);
/* 287 */       this.chart.setSeriesFromBuckets(local_buckets);
/*     */     }
/* 289 */     this.chart.moveToEnd();
/* 290 */     this.chart.adjustYAxis();
/*     */   }
/*     */ 
/*     */   public boolean isDebugPaused()
/*     */   {
/* 298 */     SWVClient client = getCurrentClient();
/* 299 */     if ((client == null) || (client.sessionSuspended()))
/*     */     {
/* 301 */       return true;
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/* 311 */     if ((this.chart != null) && (this.chart.getPlotArea() != null)) {
/* 312 */       if (this.chart.isDisposed()) {
/* 313 */         this.chart = new SWVTimelineChart(this.my_parent, 0, title(), getGraphColor());
/*     */       }
/* 315 */       this.chart.getPlotArea().setFocus();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 322 */     setCurrentClient(null);
/* 323 */     this.chart.setInitialSeries(this.chart.getScale());
/* 324 */     this.chart.initializeValues(title());
/* 325 */     this.chart.setSpan(0.0D, this.chart.getOptimalWidth());
/* 326 */     this.chart.moveTo(0.0D);
/* 327 */     setButtonEnabled(false);
/* 328 */     this.chart.createGraphs();
/* 329 */     this.chart.redraw();
/* 330 */     setCurrentClient(null);
/* 331 */     if ((this.chart.getToolTip() != null) && (!this.chart.getToolTip().isDisposed()))
/* 332 */       this.chart.getToolTip().dispose();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 341 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 342 */     SWVClient client = sessionManager.getClient();
/* 343 */     if ((client != null) && (!client.equals(getCurrentClient()))) {
/* 344 */       setCurrentConfig(sessionManager.getActiveConfiguration());
/* 345 */       setCurrentClient(client);
/*     */ 
/* 347 */       initializeTimeValues(client);
/* 348 */       if (this.chart != null) {
/* 349 */         this.chart.setInitialSeries(this.chart.getScale());
/* 350 */         this.chart.initializeValues(title());
/* 351 */         this.chart.setSpan(0.0D, this.chart.getOptimalWidth());
/* 352 */         this.chart.createGraphs();
/*     */       }
/*     */     }
/* 355 */     if ((client != null) && (!this.valuesIsInitialized) && (this.chart != null)) {
/* 356 */       initializeTimeValues(client);
/*     */     }
/* 358 */     setButtonEnabled(true);
/* 359 */     if ((this.chart != null) && (client != null)) {
/* 360 */       if (isDebugPaused()) {
/* 361 */         handleTargetSuspended();
/*     */       }
/* 363 */       if ((this.chart.getToolTip() != null) && (!this.chart.getToolTip().isDisposed()))
/* 364 */         this.chart.getToolTip().dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 373 */     super.handleClearEvent();
/* 374 */     if (this.chart != null) {
/* 375 */       this.chart.setSpan(0.0D, this.chart.getOptimalWidth());
/* 376 */       this.chart.moveTo(0.0D);
/* 377 */       this.chart.redraw();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setButtonEnabled(boolean onOff) {
/* 382 */     this.actionOpenLongerYAxis.setEnabled(onOff);
/* 383 */     this.actionOpenShorterYAxis.setEnabled(onOff);
/* 384 */     this.actionOpenMoreTimeInBars.setEnabled(onOff);
/* 385 */     this.actionOpenLessTimeInBars.setEnabled(onOff);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 393 */     super.dispose();
/* 394 */     if (this.chart != null) {
/* 395 */       this.chart.dispose();
/*     */     }
/* 397 */     this.viewDisposed = true;
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 402 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   private void setCurrentConfig(ILaunchConfigurationWorkingCopy activeConfiguration) {
/* 406 */     this.currentConfig = activeConfiguration;
/* 407 */     if (this.currentConfig == null)
/* 408 */       this.timeStampIsEnabled = false;
/*     */     else
/*     */       try {
/* 411 */         if (this.currentConfig.getAttribute("com.atollic.truestudio.swv.core.timestamps", "0:").startsWith("1"))
/* 412 */           this.timeStampIsEnabled = true;
/*     */       }
/*     */       catch (CoreException e) {
/* 415 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isTimestampEnabled()
/*     */   {
/* 421 */     return this.timeStampIsEnabled;
/*     */   }
/*     */ 
/*     */   public SWVTimelineChart getViewer() {
/* 425 */     return this.chart;
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/* 430 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public void setCurrentClient(SWVClient client) {
/* 434 */     this.currentClient = client;
/*     */   }
/*     */ 
/*     */   public long getCurrentTimeSlot() {
/* 438 */     return this.timeSlot;
/*     */   }
/*     */ 
/*     */   public boolean hasPerm()
/*     */   {
/* 445 */     return this.hasPerm;
/*     */   }
/*     */ 
/*     */   public void multiplyCurrentTimeSlot(double multiplier)
/*     */   {
/* 452 */     this.timeSlot = (()(this.timeSlot * multiplier));
/*     */   }
/*     */ 
/*     */   private void initializeTimeValues(SWVClient client)
/*     */   {
/* 457 */     if ((this.chart != null) && (!this.chart.isDisposed())) {
/* 458 */       this.initial_run_size = (client.getSWVCoreClock() * ()this.chart.getOptimalWidthInSeconds());
/* 459 */       this.timeSlot = (this.initial_run_size / this.chart.getOptimalNrBars());
/*     */     } else {
/* 461 */       this.initial_run_size = (client.getSWVCoreClock() * DEFAULT_GRAPH_WIDTH_IN_MICROSECONDS / 1000000L);
/* 462 */       this.timeSlot = (this.initial_run_size / 150L);
/*     */     }
/* 464 */     if (this.timeSlot < 5L) {
/* 465 */       this.timeSlot = 5L;
/*     */     }
/* 467 */     if (this.timeSlot > 480000000L) {
/* 468 */       this.timeSlot = 480000000L;
/*     */     }
/* 470 */     this.valuesIsInitialized = true;
/*     */   }
/*     */ 
/*     */   public SWVStatisticalTimelineBuckets getBucketsCache(long number_of_cykles_to_retrieved)
/*     */   {
/* 477 */     if (getCurrentClient() == null) {
/* 478 */       return null;
/*     */     }
/* 480 */     long lastCycle = getCurrentClient().getCycles();
/*     */     long endCycle;
/*     */     long startCycle;
/* 481 */     if (number_of_cykles_to_retrieved == 0L)
/*     */     {
/* 483 */       long startCycle = 0L;
/* 484 */       long currentPosition = this.chart.getXMinAsCycles();
/* 485 */       startCycle = currentPosition - this.chart.getOptimalWidthInCycles();
/* 486 */       if (startCycle < 0L)
/* 487 */         startCycle = 0L;
/* 488 */       long endCycle = currentPosition + 2L * this.chart.getOptimalWidthInCycles();
/* 489 */       if (endCycle > lastCycle)
/* 490 */         endCycle = lastCycle;
/*     */     } else {
/* 492 */       endCycle = lastCycle;
/* 493 */       startCycle = endCycle - number_of_cykles_to_retrieved < 0L ? 0L : endCycle - number_of_cykles_to_retrieved;
/*     */     }
/* 495 */     String newCacheHash = String.format("%d%d%d%s", new Object[] { Long.valueOf(startCycle), Long.valueOf(endCycle), Long.valueOf(getCurrentTimeSlot()), getEventClass() });
/* 496 */     if ((this.buckets != null) && (newCacheHash.equals(this.oldCacheHash)))
/*     */     {
/* 498 */       return this.buckets;
/*     */     }
/* 500 */     this.buckets = getBuckets(startCycle, endCycle);
/* 501 */     if (number_of_cykles_to_retrieved == 0L)
/*     */     {
/* 503 */       this.buckets.addNewFirstValueset();
/* 504 */       this.buckets.addValueSet(lastCycle, 0.0D);
/*     */     }
/* 506 */     this.oldCacheHash = newCacheHash;
/* 507 */     return this.buckets;
/*     */   }
/*     */ 
/*     */   protected String title()
/*     */   {
/* 517 */     return "";
/*     */   }
/*     */ 
/*     */   abstract Color getGraphColor();
/*     */ 
/*     */   abstract Class getEventClass();
/*     */ 
/*     */   public abstract SWVStatisticalTimelineBuckets getBuckets(long paramLong1, long paramLong2);
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.SWVAbstractTimeline
 * JD-Core Version:    0.6.2
 */