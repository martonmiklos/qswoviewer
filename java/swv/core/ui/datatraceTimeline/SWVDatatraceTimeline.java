/*     */ package com.atollic.truestudio.swv.core.ui.datatraceTimeline;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.Messages;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView.PeriodicUpdate;
/*     */ import com.atollic.truestudio.swv.core.ui.datatrace.DataTrace;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTraceDataValueEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTracePCValueEvent;
/*     */ import com.atollic.truestudio.swv.model.Event;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.custom.ScrolledComposite;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.ScrollBar;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ 
/*     */ public class SWVDatatraceTimeline extends SWVView
/*     */ {
/*     */   private Action actionOpenSaveTimelineChart;
/*     */   private Image actionOpenSaveTimelineChartImage;
/*     */   private Action actionSwitchScale;
/*     */   private Image actionSwitchScaleImage;
/*     */   private Action zoomInAction;
/*     */   private Image zoomInActionImage;
/*     */   private Action zoomOutAction;
/*     */   private Image zoomOutActionImage;
/*     */   private Action actionAdjustYaxis;
/*     */   private Image actionAdjustYaxisImage;
/*     */   private SWVClient currentClient;
/*  61 */   private int indexCtr = 0;
/*  62 */   private long time = 0L;
/*     */   private HashMap<Byte, DataTrace> comparators;
/*     */   private SWVDataTraceChart chart;
/*     */   private ILaunchConfigurationWorkingCopy currentConfig;
/*     */   private boolean timeStampIsEnabled;
/*     */   SWVView.PeriodicUpdate periodicUpdate;
/*  69 */   private boolean viewDisposed = false;
/*     */   private ScrolledComposite chartComposite;
/*  74 */   private boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  81 */     super.createPartControl(parent);
/*     */ 
/*  83 */     parent.setLayout(new GridLayout(1, false));
/*  84 */     parent.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/*     */ 
/*  86 */     Composite baseComposite = new Composite(parent, 536870912);
/*  87 */     baseComposite.setLayout(new GridLayout(1, false));
/*  88 */     baseComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/*  91 */     if (!this.hasPerm) {
/*  92 */       DemoHelper.createDemoModeLabel(baseComposite, "swv-data-trace");
/*     */     }
/*     */ 
/*  95 */     this.chartComposite = new ScrolledComposite(baseComposite, 256);
/*  96 */     this.chartComposite.setExpandVertical(true);
/*  97 */     this.chartComposite.setAlwaysShowScrollBars(true);
/*  98 */     this.chartComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*  99 */     this.chartComposite.setExpandHorizontal(true);
/*     */ 
/* 102 */     this.chart = new SWVDataTraceChart(this.chartComposite, 0);
/* 103 */     this.chartComposite.setContent(this.chart);
/* 104 */     this.chartComposite.setMinSize(this.chart.computeSize(-1, -1));
/*     */ 
/* 108 */     ScrollBar hBar = this.chartComposite.getHorizontalBar();
/* 109 */     Listener[] old_list = hBar.getListeners(13);
/* 110 */     for (Listener list : old_list) {
/* 111 */       hBar.removeListener(13, list);
/*     */     }
/*     */ 
/* 114 */     hBar.addListener(13, this.chart);
/*     */ 
/* 118 */     createActions();
/*     */ 
/* 120 */     this.comparators = new HashMap();
/*     */ 
/* 122 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 123 */     setCurrentConfig(sessionManager.getActiveConfiguration());
/*     */ 
/* 125 */     if (sessionManager.getClient() != null)
/* 126 */       handleSWVContext();
/*     */     else {
/* 128 */       handleNoSWVContext();
/*     */     }
/*     */ 
/* 132 */     startPeriodicUpdate(200);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 138 */     IActionBars bars = getViewSite().getActionBars();
/* 139 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/*     */ 
/* 141 */     this.zoomOutAction = new ZoomAction(this, 1);
/* 142 */     this.zoomOutActionImage = getImage("time_zoom_out.gif");
/* 143 */     this.zoomOutAction.setImageDescriptor(ImageDescriptor.createFromImage(this.zoomOutActionImage));
/* 144 */     this.zoomOutAction.setToolTipText(Messages.LONGER_TIME_STEPS);
/* 145 */     this.zoomOutAction.setText(Messages.LONGER_TIME_STEPS);
/* 146 */     this.zoomOutAction.setId("zoomOut");
/* 147 */     toolBarManager.insertBefore("openConf", this.zoomOutAction);
/* 148 */     this.zoomOutAction.setEnabled(true);
/*     */ 
/* 150 */     this.zoomInAction = new ZoomAction(this, 0);
/* 151 */     this.zoomInActionImage = getImage("time_zoom_in.gif");
/* 152 */     this.zoomInAction.setImageDescriptor(ImageDescriptor.createFromImage(this.zoomInActionImage));
/* 153 */     this.zoomInAction.setToolTipText(Messages.SHORTER_TIME_STEPS);
/* 154 */     this.zoomInAction.setText(Messages.SHORTER_TIME_STEPS);
/* 155 */     this.zoomInAction.setId("zoomIn");
/* 156 */     toolBarManager.insertBefore("zoomOut", this.zoomInAction);
/* 157 */     this.zoomInAction.setEnabled(true);
/*     */ 
/* 159 */     this.actionAdjustYaxis = new OpenAdjustYAxis(this);
/* 160 */     this.actionAdjustYaxisImage = getImage("alignheight.gif");
/* 161 */     this.actionAdjustYaxis.setImageDescriptor(ImageDescriptor.createFromImage(this.actionAdjustYaxisImage));
/* 162 */     this.actionAdjustYaxis.setToolTipText(Messages.ADJUST_Y_AXIS);
/* 163 */     this.actionAdjustYaxis.setId("actionAdjustYaxis");
/* 164 */     toolBarManager.insertBefore("zoomIn", this.actionAdjustYaxis);
/*     */ 
/* 166 */     this.actionSwitchScale = new SwitchScaleAction(this);
/* 167 */     this.actionSwitchScaleImage = getImage("time_obj.png");
/* 168 */     this.actionSwitchScale.setImageDescriptor(ImageDescriptor.createFromImage(this.actionSwitchScaleImage));
/* 169 */     this.actionSwitchScale.setToolTipText(Messages.SWITCH_BETWEEN_TIME_AND_CYCLE_SCALE);
/* 170 */     this.actionSwitchScale.setText(Messages.SWITCH_BETWEEN_TIME_AND_CYCLE_SCALE);
/* 171 */     this.actionSwitchScale.setId("switchScale");
/* 172 */     toolBarManager.insertBefore("actionAdjustYaxis", this.actionSwitchScale);
/*     */ 
/* 174 */     this.actionOpenSaveTimelineChart = new OpenSaveTimelineChart(this);
/* 175 */     this.actionOpenSaveTimelineChartImage = getImage("capturescreen.gif");
/* 176 */     this.actionOpenSaveTimelineChart.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenSaveTimelineChartImage));
/* 177 */     this.actionOpenSaveTimelineChart.setToolTipText(Messages.SAVE_CHART_AS_IMAGE);
/* 178 */     this.actionOpenSaveTimelineChart.setText(Messages.SAVE_CHART_AS_IMAGE);
/* 179 */     this.actionOpenSaveTimelineChart.setId("actionOpenSaveTimelineChart");
/* 180 */     toolBarManager.insertBefore("switchScale", this.actionOpenSaveTimelineChart);
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   private synchronized boolean updateData(SWVBuffer rxBuffer)
/*     */   {
/* 195 */     Object[] records = rxBuffer.getRecords();
/* 196 */     boolean newData = false;
/*     */ 
/* 199 */     if ((records == null) || (this.indexCtr == records.length)) {
/* 200 */       return false;
/*     */     }
/*     */ 
/* 204 */     for (int index = this.indexCtr; index < records.length; index++) {
/* 205 */       Event event = (Event)records[index];
/*     */ 
/* 207 */       DWTDataTraceDataValueEvent dataEvent = null;
/* 208 */       DWTDataTracePCValueEvent pcEvent = null;
/* 209 */       byte comparator = -1;
/*     */ 
/* 212 */       if ((event instanceof DWTDataTraceDataValueEvent)) {
/* 213 */         dataEvent = (DWTDataTraceDataValueEvent)event; } else {
/* 214 */         if (!(event instanceof DWTDataTracePCValueEvent)) continue;
/* 215 */         pcEvent = (DWTDataTracePCValueEvent)event;
/*     */       }
/*     */ 
/* 221 */       comparator = dataEvent != null ? dataEvent.getComparatorId() : pcEvent.getComparatorId();
/*     */ 
/* 224 */       if ((comparator >= 0) && (comparator <= 4))
/*     */       {
/* 227 */         SWVComparatorConfig cmpConfig = this.currentClient.getComparatorConfig(comparator);
/* 228 */         if (cmpConfig != null)
/*     */         {
/* 233 */           DataTrace dataTrace = (DataTrace)this.comparators.get(Byte.valueOf(comparator));
/*     */ 
/* 236 */           if (dataTrace == null) {
/* 237 */             dataTrace = new DataTrace(comparator);
/* 238 */             this.comparators.put(Byte.valueOf(comparator), dataTrace);
/*     */           }
/*     */ 
/* 243 */           dataTrace.setComparatorConfig(cmpConfig);
/* 244 */           dataTrace.setName(cmpConfig.getSymbol());
/*     */ 
/* 247 */           if (dataEvent != null) {
/* 248 */             if (dataEvent.getCycles() != 0L) {
/* 249 */               byte dataValue = 0;
/* 250 */               if (this.hasPerm) {
/* 251 */                 dataValue = dataEvent.getAccess();
/*     */               }
/* 253 */               dataTrace.addDataValue(dataEvent.getDataValue(), dataValue, dataEvent.getCycles());
/* 254 */               newData = true;
/*     */             }
/*     */           }
/*     */           else {
/* 258 */             dataTrace.addPC(pcEvent.getPcAddress());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 264 */     this.indexCtr = records.length;
/*     */ 
/* 267 */     return newData;
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 278 */     SWVClient client = getCurrentClient();
/* 279 */     if (client != null)
/*     */     {
/* 282 */       boolean newData = updateData(client.getRxBuffer());
/*     */ 
/* 284 */       if (newData)
/*     */       {
/* 286 */         this.chart.refresh2();
/*     */       }
/*     */ 
/* 290 */       long clientTime = client.getCycles();
/* 291 */       if (clientTime > this.time) {
/* 292 */         this.time = clientTime;
/* 293 */         this.chart.timeChanged(clientTime);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleTargetResume()
/*     */   {
/* 308 */     super.handleTargetResume();
/*     */ 
/* 310 */     this.chart.setDebugIsPaused(false);
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 326 */     super.handleTargetSuspended();
/*     */ 
/* 328 */     this.chart.setDebugIsPaused(true);
/*     */   }
/*     */ 
/*     */   public boolean isDebugPaused()
/*     */   {
/* 338 */     SWVClient client = getCurrentClient();
/* 339 */     if ((client == null) || (client.sessionSuspended()))
/*     */     {
/* 341 */       this.chart.setDebugIsPaused(true);
/* 342 */       return true;
/*     */     }
/* 344 */     this.chart.setDebugIsPaused(false);
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void resetView()
/*     */   {
/* 357 */     this.indexCtr = 0;
/*     */ 
/* 359 */     this.time = 0L;
/*     */ 
/* 362 */     this.comparators = new HashMap();
/*     */ 
/* 365 */     this.chart.setInput(null);
/* 366 */     this.chart.refresh2();
/* 367 */     if ((this.chart.getToolTip() != null) && (!this.chart.getToolTip().isDisposed()))
/* 368 */       this.chart.getToolTip().dispose();
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 378 */     setCurrentClient(null);
/*     */ 
/* 381 */     resetView();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 391 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 392 */     SWVClient client = sessionManager.getClient();
/*     */ 
/* 394 */     if ((client != null) && (!client.equals(getCurrentClient())))
/*     */     {
/* 396 */       setCurrentConfig(sessionManager.getActiveConfiguration());
/* 397 */       setCurrentClient(client);
/*     */ 
/* 400 */       resetView();
/*     */ 
/* 403 */       this.chart.enableScrollBar(client.sessionSuspended());
/*     */ 
/* 406 */       this.chart.setCoreClock(client.getSWVCoreClock());
/*     */ 
/* 409 */       this.chart.setInput(this.comparators);
/*     */     }
/*     */ 
/* 412 */     setButtonEnabled(true);
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 420 */     super.handleClearEvent();
/* 421 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 422 */     SWVClient client = sessionManager.getClient();
/* 423 */     resetView();
/* 424 */     if (this.chart != null) {
/* 425 */       this.chart.timeChanged(this.time);
/*     */ 
/* 427 */       this.chart.enableScrollBar(client.sessionSuspended());
/*     */ 
/* 430 */       this.chart.setCoreClock(client.getSWVCoreClock());
/*     */ 
/* 433 */       this.chart.setInput(this.comparators);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setButtonEnabled(boolean onOff)
/*     */   {
/* 443 */     if (this.actionSwitchScale != null) {
/* 444 */       this.actionSwitchScale.setEnabled(onOff);
/*     */     }
/* 446 */     this.actionOpenSaveTimelineChart.setEnabled(onOff);
/* 447 */     this.zoomInAction.setEnabled(onOff);
/* 448 */     this.zoomOutAction.setEnabled(onOff);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 456 */     this.viewDisposed = true;
/* 457 */     this.chart.dispose();
/* 458 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 463 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   private void setCurrentConfig(ILaunchConfigurationWorkingCopy activeConfiguration) {
/* 467 */     this.currentConfig = activeConfiguration;
/*     */ 
/* 469 */     if (this.currentConfig == null)
/* 470 */       this.timeStampIsEnabled = false;
/*     */     else
/*     */       try {
/* 473 */         if (this.currentConfig.getAttribute("com.atollic.truestudio.swv.core.timestamps", "0:").startsWith("1"))
/* 474 */           this.timeStampIsEnabled = true;
/*     */       }
/*     */       catch (CoreException e) {
/* 477 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isTimestampEnabled()
/*     */   {
/* 483 */     return this.timeStampIsEnabled;
/*     */   }
/*     */ 
/*     */   public SWVDataTraceChart getChart() {
/* 487 */     return this.chart;
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/* 492 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public void setCurrentClient(SWVClient client) {
/* 496 */     this.currentClient = client;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatraceTimeline.SWVDatatraceTimeline
 * JD-Core Version:    0.6.2
 */