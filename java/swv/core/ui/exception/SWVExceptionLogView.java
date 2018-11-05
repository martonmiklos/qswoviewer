/*     */ package com.atollic.truestudio.swv.core.ui.exception;
/*     */ 
/*     */ import com.atollic.truestudio.common.utilities.exposed.StructuredViewerItemClipboardCopier;
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*     */ import com.atollic.truestudio.swv.model.Event;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.jface.viewers.TableViewerColumn;
/*     */ import org.eclipse.swt.custom.CTabFolder;
/*     */ import org.eclipse.swt.custom.CTabItem;
/*     */ import org.eclipse.swt.dnd.Clipboard;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Table;
/*     */ import org.eclipse.swt.widgets.TableColumn;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ import org.eclipse.ui.IWorkbenchPartSite;
/*     */ import org.eclipse.ui.actions.ActionFactory;
/*     */ 
/*     */ public class SWVExceptionLogView extends SWVView
/*     */ {
/*     */   public Composite baseComposite;
/*     */   private Table eventTable;
/*     */   private TableViewer eventTableViewer;
/*     */   private Table statisticsTable;
/*     */   private TableViewer statisticsTableViewer;
/*     */   private Label overflowLabel;
/*  54 */   private boolean viewDisposed = false;
/*  55 */   private boolean autoScroll = true;
/*  56 */   private SWVClient currentClient = null;
/*  57 */   private int currentScale = 0;
/*     */   private Action actionScrollLock;
/*  63 */   private Clipboard clipboard = null;
/*     */   private CTabFolder tabFolder;
/*  66 */   private static String DATA = Messages.SWVExceptionLogView_DATA;
/*  67 */   private static String STATISTICS = Messages.SWVExceptionLogView_STATISTICS;
/*  68 */   private int statisticsIndex = 0;
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  76 */     super.createPartControl(parent);
/*  77 */     this.baseComposite = new Composite(parent, 536870912);
/*  78 */     this.baseComposite.setLayout(new GridLayout(1, false));
/*     */ 
/*  81 */     boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV_EXCEPTION_TRACE_LOG);
/*  82 */     if (!hasPerm) {
/*  83 */       DemoHelper.createDemoModeLabel(this.baseComposite, "swv-exception-trace");
/*     */     }
/*     */ 
/*  86 */     this.tabFolder = new CTabFolder(this.baseComposite, 8390656);
/*     */ 
/*  88 */     this.tabFolder.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*  89 */     this.tabFolder.setSimple(false);
/*     */ 
/*  94 */     CTabItem dataTab = new CTabItem(this.tabFolder, 0);
/*  95 */     dataTab.setText(DATA);
/*  96 */     dataTab.setToolTipText(Messages.SWVExceptionLogView_INCOMING_EXCEPTIONS);
/*     */ 
/*  98 */     this.tabFolder.setSelection(dataTab);
/*     */ 
/* 100 */     this.eventTableViewer = new TableViewer(this.tabFolder, 268503042);
/*     */ 
/* 102 */     this.eventTable = this.eventTableViewer.getTable();
/* 103 */     dataTab.setControl(this.eventTable);
/*     */ 
/* 105 */     if (hasPerm) {
/* 106 */       this.eventTable.addMouseListener(new SWVExceptionLogListener(this));
/*     */     }
/*     */ 
/* 109 */     this.eventTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 110 */     this.eventTable.setLinesVisible(true);
/* 111 */     this.eventTable.setHeaderVisible(true);
/*     */ 
/* 113 */     TableViewerColumn eTableViewerColumn = new TableViewerColumn(this.eventTableViewer, 0);
/* 114 */     TableColumn eTableColumn0 = eTableViewerColumn.getColumn();
/* 115 */     eTableColumn0.setWidth(80);
/* 116 */     eTableColumn0.setText(Messages.SWVExceptionLogView_INDEX);
/*     */ 
/* 118 */     TableViewerColumn eTableViewerColumn_4 = new TableViewerColumn(this.eventTableViewer, 0);
/* 119 */     TableColumn eTableColumn1 = eTableViewerColumn_4.getColumn();
/* 120 */     eTableColumn1.setWidth(100);
/* 121 */     eTableColumn1.setText(Messages.SWVExceptionLogView_TYPE);
/*     */ 
/* 123 */     TableViewerColumn eTableViewerColumn_1 = new TableViewerColumn(this.eventTableViewer, 0);
/* 124 */     TableColumn eTableColumn4 = eTableViewerColumn_1.getColumn();
/* 125 */     eTableColumn4.setWidth(120);
/* 126 */     eTableColumn4.setText(Messages.SWVExceptionLogView_NAME);
/*     */ 
/* 128 */     TableViewerColumn eTableViewerColumn_7 = new TableViewerColumn(this.eventTableViewer, 0);
/* 129 */     TableColumn eTableColumn7 = eTableViewerColumn_7.getColumn();
/* 130 */     eTableColumn7.setWidth(100);
/* 131 */     eTableColumn7.setText(Messages.SWVExceptionLogView_PERIPHERAL);
/*     */ 
/* 133 */     TableViewerColumn eTableViewerColumn_6 = new TableViewerColumn(this.eventTableViewer, 0);
/* 134 */     TableColumn eTableColumn6 = eTableViewerColumn_6.getColumn();
/* 135 */     eTableColumn6.setWidth(120);
/* 136 */     eTableColumn6.setText(Messages.SWVExceptionLogView_FUNCTION);
/* 137 */     eTableColumn6.setToolTipText(Messages.SWVExceptionLogView_NAME_OF_FUNCTION_TO_JUMP_TO);
/*     */ 
/* 139 */     TableViewerColumn eTableViewerColumn_3 = new TableViewerColumn(this.eventTableViewer, 0);
/* 140 */     TableColumn eTableColumn2 = eTableViewerColumn_3.getColumn();
/* 141 */     eTableColumn2.setWidth(110);
/* 142 */     eTableColumn2.setText(Messages.SWVExceptionLogView_CYCLES);
/*     */ 
/* 144 */     TableViewerColumn eTableViewerColumn_2 = new TableViewerColumn(this.eventTableViewer, 0);
/* 145 */     TableColumn eTableColumn3 = eTableViewerColumn_2.getColumn();
/* 146 */     eTableColumn3.setWidth(160);
/* 147 */     eTableColumn3.setText(Messages.SWVExceptionLogView_TIME);
/*     */ 
/* 149 */     TableViewerColumn eTableViewerColumn_5 = new TableViewerColumn(this.eventTableViewer, 0);
/* 150 */     TableColumn eTableColumn5 = eTableViewerColumn_5.getColumn();
/* 151 */     eTableColumn5.setWidth(250);
/* 152 */     eTableColumn5.setText(Messages.SWVExceptionLogView_EXTRA_INFO);
/*     */ 
/* 154 */     this.eventTableViewer.setLabelProvider(new SWVExceptionLogViewLabelProvider());
/* 155 */     this.eventTableViewer.setContentProvider(new SWVExceptionLogViewContentProvider());
/*     */ 
/* 161 */     CTabItem statisticsTab = new CTabItem(this.tabFolder, 0);
/* 162 */     statisticsTab.setText(STATISTICS);
/* 163 */     statisticsTab.setToolTipText(Messages.SWVExceptionLogView_WHAT_DID_WE_RECEIVE);
/*     */ 
/* 165 */     this.statisticsTableViewer = new TableViewer(this.tabFolder, 268503042);
/*     */ 
/* 167 */     this.statisticsTable = this.statisticsTableViewer.getTable();
/* 168 */     statisticsTab.setControl(this.statisticsTable);
/*     */ 
/* 170 */     this.statisticsTable.addMouseListener(new SWVExceptionLogListener(this));
/* 171 */     this.statisticsTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 172 */     this.statisticsTable.setLinesVisible(true);
/* 173 */     this.statisticsTable.setHeaderVisible(true);
/*     */ 
/* 175 */     TableViewerColumn sTableViewerColumn_2 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 176 */     TableColumn sTableColumn2 = sTableViewerColumn_2.getColumn();
/* 177 */     sTableColumn2.setWidth(120);
/* 178 */     sTableColumn2.setText(Messages.SWVExceptionLogView_EXCEPTION);
/*     */ 
/* 180 */     TableViewerColumn sTableViewerColumn_3 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 181 */     TableColumn sTableColumn3 = sTableViewerColumn_3.getColumn();
/* 182 */     sTableColumn3.setWidth(150);
/* 183 */     sTableColumn3.setText(Messages.SWVExceptionLogView_HANDLER);
/* 184 */     sTableColumn3.setToolTipText(Messages.SWVExceptionLogView_NAME_OF_FUNCTION_OR_HANDLER);
/*     */ 
/* 186 */     TableViewerColumn sTableViewerColumn_e = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 187 */     TableColumn sTableColumne = sTableViewerColumn_e.getColumn();
/* 188 */     sTableColumne.setWidth(80);
/* 189 */     sTableColumne.setText(Messages.SWVExceptionLogView_PERCENTAGE_OF);
/* 190 */     sTableColumne.setToolTipText(Messages.SWVExceptionLogView_PERCENTAGE_OF_ALL);
/*     */ 
/* 192 */     TableViewerColumn sTableViewerColumn_4 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 193 */     TableColumn sTableColumn4 = sTableViewerColumn_4.getColumn();
/* 194 */     sTableColumn4.setWidth(80);
/* 195 */     sTableColumn4.setText(Messages.SWVExceptionLogView_NUMBER_OF);
/* 196 */     sTableColumn4.setToolTipText(Messages.SWVExceptionLogView_TOTAL_NUMBER_OF);
/*     */ 
/* 198 */     TableViewerColumn sTableViewerColumn_d = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 199 */     TableColumn sTableColumnd = sTableViewerColumn_d.getColumn();
/* 200 */     sTableColumnd.setWidth(110);
/* 201 */     sTableColumnd.setText(Messages.SWVExceptionLogView_PERCENTAGE_OF_TIME);
/* 202 */     sTableColumnd.setToolTipText(Messages.SWVExceptionLogView_RUNTAME_OF_TOTAL_RUNTIME);
/*     */ 
/* 204 */     TableViewerColumn sTableViewerColumn_f = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 205 */     TableColumn sTableColumnf = sTableViewerColumn_f.getColumn();
/* 206 */     sTableColumnf.setWidth(110);
/* 207 */     sTableColumnf.setText(Messages.SWVExceptionLogView_PERCENTAGE_OF_DEBUG_TIME);
/* 208 */     sTableColumnf.setToolTipText(Messages.SWVExceptionLogView_RUNTIME_COMPARE_TO_TOTAL_DEBUG_TIME);
/*     */ 
/* 211 */     TableViewerColumn sTableViewerColumn_a = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 212 */     TableColumn sTableColumna = sTableViewerColumn_a.getColumn();
/* 213 */     sTableColumna.setWidth(90);
/* 214 */     sTableColumna.setText(Messages.SWVExceptionLogView_TOTAL_RUNTIME);
/* 215 */     sTableColumna.setToolTipText(Messages.SWVExceptionLogView_TOTAL_RUNTIME_IN_CYCLES_FOR_THIS_EXCEPTION);
/*     */ 
/* 217 */     TableViewerColumn sTableViewerColumn_9 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 218 */     TableColumn sTableColumn9 = sTableViewerColumn_9.getColumn();
/* 219 */     sTableColumn9.setWidth(90);
/* 220 */     sTableColumn9.setText(Messages.SWVExceptionLogView_AVG_RUNTIME);
/* 221 */     sTableColumn9.setToolTipText(Messages.SWVExceptionLogView_AVG_RUNTIME_IN_CYCLES);
/*     */ 
/* 223 */     TableViewerColumn sTableViewerColumn_7 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 224 */     TableColumn sTableColumn7 = sTableViewerColumn_7.getColumn();
/* 225 */     sTableColumn7.setWidth(60);
/* 226 */     sTableColumn7.setText(Messages.SWVExceptionLogView_FASTEST);
/* 227 */     sTableColumn7.setToolTipText(Messages.SWVExceptionLogView_FASTEST_IN_CYCLES);
/*     */ 
/* 229 */     TableViewerColumn sTableViewerColumn_8 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 230 */     TableColumn sTableColumn8 = sTableViewerColumn_8.getColumn();
/* 231 */     sTableColumn8.setWidth(60);
/* 232 */     sTableColumn8.setText(Messages.SWVExceptionLogView_SLOWEST);
/* 233 */     sTableColumn8.setToolTipText(Messages.SWVExceptionLogView_SLOWEST_IN_CYCLES);
/*     */ 
/* 235 */     TableViewerColumn sTableViewerColumn_5 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 236 */     TableColumn sTableColumn5 = sTableViewerColumn_5.getColumn();
/* 237 */     sTableColumn5.setWidth(70);
/* 238 */     sTableColumn5.setText(Messages.SWVExceptionLogView_FIRST);
/* 239 */     sTableColumn5.setToolTipText(Messages.SWVExceptionLogView_FIRST_IN_CYCLES);
/*     */ 
/* 241 */     TableViewerColumn sTableViewerColumn_b = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 242 */     TableColumn sTableColumnb = sTableViewerColumn_b.getColumn();
/* 243 */     sTableColumnb.setWidth(140);
/* 244 */     sTableColumnb.setText(Messages.SWVExceptionLogView_FIRST_S);
/* 245 */     sTableColumnb.setToolTipText(Messages.SWVExceptionLogView_FIRST_IN_SECONDS);
/*     */ 
/* 247 */     TableViewerColumn sTableViewerColumn_6 = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 248 */     TableColumn sTableColumn6 = sTableViewerColumn_6.getColumn();
/* 249 */     sTableColumn6.setWidth(80);
/* 250 */     sTableColumn6.setText(Messages.SWVExceptionLogView_LATEST);
/* 251 */     sTableColumn6.setToolTipText(Messages.SWVExceptionLogView_LATEST_IN_CYCLES);
/*     */ 
/* 253 */     TableViewerColumn sTableViewerColumn_c = new TableViewerColumn(this.statisticsTableViewer, 0);
/* 254 */     TableColumn sTableColumnc = sTableViewerColumn_c.getColumn();
/* 255 */     sTableColumnc.setWidth(130);
/* 256 */     sTableColumnc.setText(Messages.SWVExceptionLogView_LATEST_S);
/* 257 */     sTableColumnc.setToolTipText(Messages.SWVExceptionLogView_LATEST_IN_SECONDS);
/*     */ 
/* 259 */     this.statisticsTableViewer.setContentProvider(new SWVExceptionStatisticViewContentProvider());
/* 260 */     this.statisticsTableViewer.setLabelProvider(new SWVExceptionStatisticViewLabelProvider());
/* 261 */     sTableViewerColumn_e.setLabelProvider(new SWVExceptionStatisticViewStyledLabelProviderForNumberOf());
/* 262 */     sTableViewerColumn_d.setLabelProvider(new SWVExceptionStatisticViewStyledLabelProviderForTotalExceptionRun());
/* 263 */     sTableViewerColumn_f.setLabelProvider(new SWVExceptionStatisticViewStyledLabelProviderForTotalRun());
/*     */ 
/* 267 */     Composite labels = new Composite(this.baseComposite, 536870912);
/* 268 */     labels.setLayoutData(new GridData(16384, 128, true, false, 1, 1));
/* 269 */     GridLayout gl_labels = new GridLayout(1, true);
/* 270 */     labels.setLayout(gl_labels);
/*     */ 
/* 272 */     this.overflowLabel = new Label(labels, 0);
/* 273 */     this.overflowLabel.setText(Messages.SWVExceptionLogView_OVERFLOW_PACKETS + ": 0                    ");
/*     */ 
/* 276 */     createActions();
/*     */     try
/*     */     {
/* 280 */       this.clipboard = new Clipboard(getSite().getShell().getDisplay());
/* 281 */       getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.eventTableViewer, this.clipboard));
/*     */     } catch (Exception e) {
/* 283 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 287 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 288 */     SWVClient traceClient = sessionManager.getClient();
/* 289 */     this.currentClient = traceClient;
/*     */ 
/* 291 */     if (traceClient != null) {
/* 292 */       this.eventTableViewer.setInput(traceClient.getExceptionBuffer());
/* 293 */       this.statisticsTableViewer.setInput(getInterruptParser());
/*     */     }
/*     */ 
/* 297 */     startPeriodicUpdate(500);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 305 */     this.actionScrollLock = new ScrollLockAction(this);
/*     */ 
/* 307 */     IActionBars bars = getViewSite().getActionBars();
/* 308 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/* 309 */     toolBarManager.add(new Separator());
/* 310 */     toolBarManager.add(this.actionScrollLock);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 319 */     this.viewDisposed = true;
/* 320 */     if ((this.clipboard != null) && 
/* 321 */       (!this.clipboard.isDisposed())) {
/* 322 */       this.clipboard.dispose();
/*     */     }
/* 324 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 329 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 340 */     if (getCurrentClient() != null) {
/* 341 */       if (getCurrentClient().isParsingInterrupts()) {
/* 342 */         getCurrentClient().stopParsingInterrupts();
/*     */       }
/* 344 */       this.currentClient = null;
/* 345 */       resetTable();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resetTable()
/*     */   {
/* 353 */     getEventViewer().setInput(new SWVBuffer(getCurrentClient()));
/* 354 */     getEventViewer().refresh();
/* 355 */     getStatisticViewer().setInput(null);
/*     */ 
/* 357 */     getStatisticViewer().refresh();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 362 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 363 */     if (sessionManager != null) {
/* 364 */       SWVClient newTraceClient = sessionManager.getClient();
/*     */ 
/* 366 */       if (newTraceClient != null)
/*     */       {
/* 368 */         if (!newTraceClient.equals(getCurrentClient())) {
/* 369 */           this.currentClient = newTraceClient;
/*     */         }
/* 371 */         if (this.currentClient != null) {
/* 372 */           getEventViewer().setInput(newTraceClient.getExceptionBuffer());
/* 373 */           SWVInterruptParser ip = getInterruptParser();
/* 374 */           getStatisticViewer().setInput(ip);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 382 */     updateStatistics();
/* 383 */     super.handleTargetSuspended();
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 391 */     super.handleClearEvent();
/* 392 */     this.statisticsIndex = 0;
/* 393 */     resetTable();
/* 394 */     handleSWVContext();
/*     */   }
/*     */ 
/*     */   public TableViewer getEventViewer() {
/* 398 */     return this.eventTableViewer;
/*     */   }
/*     */ 
/*     */   public TableViewer getStatisticViewer() {
/* 402 */     return this.statisticsTableViewer;
/*     */   }
/*     */ 
/*     */   public TableViewer getViewer() {
/* 406 */     if (isStatisticTabSelected()) {
/* 407 */       return getStatisticViewer();
/*     */     }
/* 409 */     return getEventViewer();
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/* 415 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 425 */     if (isStatisticTabSelected()) {
/* 426 */       updateStatistics();
/*     */     }
/* 428 */     TableViewer currentViewer = getViewer();
/* 429 */     if (currentViewer != null) {
/* 430 */       int[] indices = currentViewer.getTable().getSelectionIndices();
/* 431 */       currentViewer.setSelection(null);
/* 432 */       if (!isDisposed()) {
/* 433 */         currentViewer.refresh();
/*     */       }
/* 435 */       currentViewer.getTable().select(indices);
/*     */     }
/* 437 */     if (getCurrentClient() != null) {
/* 438 */       String packets = Messages.SWVExceptionLogView_OVERFLOW_PACKETS + ": " + getCurrentClient().getOverflowPacketsCount();
/* 439 */       this.overflowLabel.setText(packets);
/* 440 */       this.overflowLabel.pack();
/*     */     }
/* 442 */     if (this.autoScroll)
/*     */     {
/* 444 */       Table table = getEventViewer().getTable();
/* 445 */       int itemCount = table.getItemCount();
/*     */ 
/* 447 */       if (itemCount > 0)
/* 448 */         table.showItem(table.getItem(itemCount - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAutoScroll(boolean autoScroll)
/*     */   {
/* 457 */     this.autoScroll = autoScroll;
/*     */   }
/*     */ 
/*     */   private boolean isStatisticTabSelected() {
/* 461 */     CTabItem tabItem = this.tabFolder.getSelection();
/* 462 */     return (tabItem != null) && (tabItem.getText().equals(STATISTICS));
/*     */   }
/*     */ 
/*     */   private SWVInterruptParser getInterruptParser() {
/* 466 */     if (getCurrentClient() == null)
/* 467 */       return null;
/* 468 */     if (!getCurrentClient().isParsingInterrupts()) {
/* 469 */       return getCurrentClient().startParsingInterrupts();
/*     */     }
/* 471 */     return getCurrentClient().getInterruptParser();
/*     */   }
/*     */ 
/*     */   public synchronized void updateStatistics()
/*     */   {
/* 480 */     SWVClient client = getCurrentClient();
/* 481 */     if (client == null)
/* 482 */       return;
/* 483 */     SWVBuffer rxBuffer = client.getExceptionBuffer();
/* 484 */     if (rxBuffer == null)
/* 485 */       return;
/* 486 */     Object[] records = rxBuffer.getRecords();
/*     */ 
/* 489 */     if ((records == null) || (this.statisticsIndex == records.length)) {
/* 490 */       return;
/*     */     }
/* 492 */     SWVInterruptParser parser = getInterruptParser();
/* 493 */     if (parser == null) {
/* 494 */       return;
/*     */     }
/*     */ 
/* 497 */     for (int index = this.statisticsIndex; index < records.length; index++) {
/* 498 */       Event event = (Event)records[index];
/* 499 */       if ((event instanceof DWTExceptionEvent)) {
/* 500 */         DWTExceptionEvent exception = (DWTExceptionEvent)records[index];
/* 501 */         parser.addToStatistics(exception);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 506 */     this.statisticsIndex = records.length;
/*     */   }
/*     */ 
/*     */   public void switchScale()
/*     */   {
/* 514 */     if (this.currentScale == 0)
/* 515 */       this.currentScale = 1;
/*     */     else
/* 517 */       this.currentScale = 0;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionLogView
 * JD-Core Version:    0.6.2
 */