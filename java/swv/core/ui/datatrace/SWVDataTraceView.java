/*     */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*     */ 
/*     */ import com.atollic.truestudio.common.utilities.exposed.StructuredViewerItemClipboardCopier;
/*     */ import com.atollic.truestudio.oss.resources.SWTResourceManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTraceDataValueEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTracePCValueEvent;
/*     */ import com.atollic.truestudio.swv.model.Event;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.ISelectionChangedListener;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.SelectionChangedEvent;
/*     */ import org.eclipse.jface.viewers.StructuredSelection;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.jface.viewers.TableViewerColumn;
/*     */ import org.eclipse.swt.custom.SashForm;
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
/*     */ public class SWVDataTraceView extends SWVView
/*     */ {
/*     */   private static final int REFRESH_RATE = 1000;
/*     */   private HashMap<Byte, DataTrace> comparators;
/*  65 */   private SWVClient currentClient = null;
/*  66 */   private int indexCtr = 0;
/*  67 */   private boolean viewDisposed = false;
/*  68 */   private boolean autoScroll = true;
/*     */   private int valueFormat;
/*     */   private TableViewer watchTableViewer;
/*     */   private TableViewer historyTableViewer;
/*     */   private SashForm sashForm;
/*     */   private Action actionClearHistory;
/*     */   private Action actionScrollLock;
/*     */   private Label historyForLbl;
/*     */   private Action actionSetHex;
/*     */   private Action actionSetDec;
/*     */   private Action actionSetBin;
/*  87 */   private Clipboard clipboard = null;
/*     */ 
/*     */   public void setValueFormat(int format)
/*     */   {
/* 158 */     this.valueFormat = format;
/*     */   }
/*     */ 
/*     */   public int getValueFormat() {
/* 162 */     return this.valueFormat;
/*     */   }
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/* 167 */     super.createPartControl(parent);
/*     */ 
/* 169 */     Composite baseComposite = new Composite(parent, 536870912);
/* 170 */     baseComposite.setLayout(new GridLayout(1, false));
/* 171 */     baseComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/* 174 */     boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/* 175 */     if (!hasPerm) {
/* 176 */       DemoHelper.createDemoModeLabel(baseComposite, "swv-data-trace");
/*     */     }
/*     */ 
/* 180 */     this.sashForm = new SashForm(baseComposite, 512);
/* 181 */     this.sashForm.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/* 184 */     Composite watchComposite = new Composite(this.sashForm, 0);
/* 185 */     GridLayout gl_watchComposite = new GridLayout(1, false);
/* 186 */     gl_watchComposite.verticalSpacing = 0;
/* 187 */     gl_watchComposite.marginWidth = 0;
/* 188 */     gl_watchComposite.marginHeight = 0;
/* 189 */     watchComposite.setLayout(gl_watchComposite);
/* 190 */     watchComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/* 192 */     Label label_1 = new Label(watchComposite, 16777216);
/* 193 */     label_1.setAlignment(16384);
/* 194 */     label_1.setLayoutData(new GridData(4, 16777216, false, false, 1, 1));
/* 195 */     label_1.setText(Messages.SWVDataTraceView_WATCH);
/*     */ 
/* 198 */     this.watchTableViewer = new TableViewer(watchComposite, 67586);
/*     */ 
/* 201 */     this.watchTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
/*     */     {
/*     */       public void selectionChanged(SelectionChangedEvent event) {
/* 204 */         ISelection selection = event.getSelection();
/*     */ 
/* 206 */         if ((selection instanceof IStructuredSelection)) {
/* 207 */           IStructuredSelection sselection = (IStructuredSelection)selection;
/*     */ 
/* 209 */           Object dataTrace = sselection.getFirstElement();
/* 210 */           if ((dataTrace instanceof DataTrace))
/* 211 */             SWVDataTraceView.this.displayHistory((DataTrace)dataTrace);
/*     */         }
/*     */       }
/*     */     });
/* 218 */     Table watchTable = this.watchTableViewer.getTable();
/* 219 */     watchTable.setLinesVisible(true);
/* 220 */     watchTable.setHeaderVisible(true);
/* 221 */     watchTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*     */ 
/* 223 */     TableViewerColumn tableViewerColumn = new TableViewerColumn(this.watchTableViewer, 0);
/* 224 */     TableColumn tblclmnComparator = tableViewerColumn.getColumn();
/* 225 */     tblclmnComparator.setResizable(false);
/* 226 */     tblclmnComparator.setWidth(100);
/* 227 */     tblclmnComparator.setText(Messages.SWVDataTraceView_COMPARE);
/*     */ 
/* 229 */     TableViewerColumn tableViewerColumnName = new TableViewerColumn(this.watchTableViewer, 0);
/* 230 */     TableColumn tblclmnName = tableViewerColumnName.getColumn();
/* 231 */     tblclmnName.setWidth(100);
/* 232 */     tblclmnName.setText(Messages.SWVDataTraceView_NAME);
/*     */ 
/* 234 */     TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.watchTableViewer, 0);
/* 235 */     TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
/* 236 */     tblclmnValue.setWidth(100);
/* 237 */     tblclmnValue.setText(Messages.SWVDataTraceView_VALUE);
/*     */ 
/* 241 */     Composite historyComposite = new Composite(this.sashForm, 0);
/* 242 */     historyComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 243 */     GridLayout gl_historyComposite = new GridLayout(1, false);
/* 244 */     gl_historyComposite.verticalSpacing = 0;
/* 245 */     gl_historyComposite.marginHeight = 0;
/* 246 */     gl_historyComposite.marginWidth = 0;
/* 247 */     historyComposite.setLayout(gl_historyComposite);
/*     */ 
/* 250 */     this.historyForLbl = new Label(historyComposite, 16777216);
/* 251 */     this.historyForLbl.setAlignment(16384);
/*     */ 
/* 253 */     this.historyForLbl.setLayoutData(new GridData(4, 16777216, false, false, 1, 1));
/* 254 */     this.historyForLbl.setText(Messages.SWVDataTraceView_HISTORY);
/*     */ 
/* 256 */     this.historyTableViewer = new TableViewer(historyComposite, 268503042);
/* 257 */     Table historyTable = this.historyTableViewer.getTable();
/*     */ 
/* 259 */     if (hasPerm) {
/* 260 */       historyTable.addMouseListener(new HistoryTableListener(this));
/*     */     }
/*     */ 
/* 263 */     historyTable.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/* 264 */     historyTable.setLinesVisible(true);
/* 265 */     historyTable.setHeaderVisible(true);
/*     */ 
/* 267 */     TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(this.historyTableViewer, 0);
/* 268 */     TableColumn tblclmnAccess = tableViewerColumn_5.getColumn();
/* 269 */     tblclmnAccess.setWidth(100);
/* 270 */     tblclmnAccess.setText(Messages.SWVDataTraceView_ACCESS);
/*     */ 
/* 272 */     TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.historyTableViewer, 0);
/* 273 */     TableColumn tblclmnValue_1 = tableViewerColumn_4.getColumn();
/* 274 */     tblclmnValue_1.setWidth(100);
/* 275 */     tblclmnValue_1.setText(Messages.SWVDataTraceView_VALUE);
/*     */ 
/* 277 */     TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(this.historyTableViewer, 0);
/* 278 */     TableColumn tblclmnPc_1 = tableViewerColumn_6.getColumn();
/* 279 */     tblclmnPc_1.setWidth(100);
/* 280 */     tblclmnPc_1.setText(Messages.SWVDataTraceView_PC);
/*     */ 
/* 282 */     TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(this.historyTableViewer, 0);
/* 283 */     TableColumn tblclmnTime = tableViewerColumn_7.getColumn();
/* 284 */     tblclmnTime.setWidth(100);
/* 285 */     tblclmnTime.setText(Messages.SWVDataTraceView_CYCLES);
/*     */ 
/* 287 */     this.sashForm.setWeights(new int[] { 1, 3 });
/*     */ 
/* 291 */     this.watchTableViewer.setContentProvider(new SWVDataTraceContentProvider());
/* 292 */     this.watchTableViewer.setLabelProvider(new SWVDataTraceLabelProvider(this));
/* 293 */     this.historyTableViewer.setContentProvider(new SWVDataTraceHistoryContentProvider());
/* 294 */     this.historyTableViewer.setLabelProvider(new SWVDataTraceHistoryLabelProvider(this));
/*     */ 
/* 297 */     createActions();
/*     */     try
/*     */     {
/* 301 */       this.clipboard = new Clipboard(getSite().getShell().getDisplay());
/* 302 */       getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.watchTableViewer, this.clipboard));
/* 303 */       getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.historyTableViewer, this.clipboard));
/*     */     } catch (Exception e) {
/* 305 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 308 */     this.comparators = new HashMap();
/* 309 */     this.watchTableViewer.setInput(this.comparators);
/*     */ 
/* 312 */     SessionManager sessionManager = SWVPlugin.getDefault()
/* 313 */       .getSessionManager();
/* 314 */     SWVClient traceClient = sessionManager.getClient();
/* 315 */     this.currentClient = traceClient;
/*     */ 
/* 317 */     if (traceClient != null)
/* 318 */       handleSWVContext();
/*     */     else {
/* 320 */       handleNoSWVContext();
/*     */     }
/*     */ 
/* 324 */     startPeriodicUpdate(1000);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 332 */     IActionBars bars = getViewSite().getActionBars();
/* 333 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/* 334 */     IMenuManager menuManager = bars.getMenuManager();
/*     */ 
/* 337 */     this.actionScrollLock = new ScrollLockAction(this);
/* 338 */     this.actionClearHistory = new ClearHistoryAction(this);
/* 339 */     this.actionSetHex = new ValueFormatAction(Messages.SWVDataTraceView_HEX, 0);
/* 340 */     this.actionSetHex.setToolTipText(Messages.SWVDataTraceView_DISPLAY_AS_HEX);
/* 341 */     this.actionSetDec = new ValueFormatAction(Messages.SWVDataTraceView_DEC, 5);
/* 342 */     this.actionSetDec.setToolTipText(Messages.SWVDataTraceView_DISPLAY_AS_DEC);
/* 343 */     this.actionSetBin = new ValueFormatAction(Messages.SWVDataTraceView_BIN, 2);
/* 344 */     this.actionSetBin.setToolTipText(Messages.SWVDataTraceView_DISPLAY_AS_BIN);
/*     */ 
/* 347 */     toolBarManager.add(new Separator());
/* 348 */     toolBarManager.add(this.actionClearHistory);
/* 349 */     toolBarManager.add(this.actionScrollLock);
/*     */ 
/* 352 */     menuManager.add(this.actionSetHex);
/* 353 */     menuManager.add(this.actionSetDec);
/* 354 */     menuManager.add(this.actionSetBin);
/*     */ 
/* 356 */     bars.updateActionBars();
/*     */ 
/* 359 */     this.actionSetDec.setChecked(true);
/* 360 */     setValueFormat(5);
/*     */ 
/* 362 */     this.actionClearHistory.setEnabled(false);
/*     */   }
/*     */ 
/*     */   private synchronized void updateData(SWVBuffer rxBuffer)
/*     */   {
/* 371 */     Object[] records = rxBuffer.getRecords();
/*     */ 
/* 374 */     if ((records == null) || (this.indexCtr == records.length)) {
/* 375 */       return;
/*     */     }
/*     */ 
/* 379 */     for (int index = this.indexCtr; index < records.length; index++) {
/* 380 */       Event event = (Event)records[index];
/*     */ 
/* 382 */       DWTDataTraceDataValueEvent dataEvent = null;
/* 383 */       DWTDataTracePCValueEvent pcEvent = null;
/* 384 */       byte comparator = -1;
/*     */ 
/* 387 */       if ((event instanceof DWTDataTraceDataValueEvent)) {
/* 388 */         dataEvent = (DWTDataTraceDataValueEvent)event; } else {
/* 389 */         if (!(event instanceof DWTDataTracePCValueEvent)) continue;
/* 390 */         pcEvent = (DWTDataTracePCValueEvent)event;
/*     */       }
/*     */ 
/* 396 */       comparator = dataEvent != null ? dataEvent.getComparatorId() : pcEvent.getComparatorId();
/*     */ 
/* 399 */       if ((comparator >= 0) && (comparator < 4))
/*     */       {
/* 404 */         SWVComparatorConfig cmpConfig = this.currentClient.getComparatorConfig(comparator);
/* 405 */         if (cmpConfig != null)
/*     */         {
/* 410 */           DataTrace dataTrace = (DataTrace)this.comparators.get(Byte.valueOf(comparator));
/*     */ 
/* 413 */           if (dataTrace == null) {
/* 414 */             dataTrace = new DataTrace(comparator);
/* 415 */             this.comparators.put(Byte.valueOf(comparator), dataTrace);
/*     */           }
/*     */ 
/* 420 */           dataTrace.setComparatorConfig(cmpConfig);
/*     */ 
/* 424 */           if (dataEvent != null) {
/* 425 */             dataTrace.addDataValue(dataEvent.getDataValue(), dataEvent.getAccess(), dataEvent.getCycles());
/*     */           }
/*     */           else {
/* 428 */             dataTrace.addPC(pcEvent.getPcAddress());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 434 */     this.indexCtr = records.length;
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 450 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 451 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 453 */     if (traceClient != null)
/*     */     {
/* 455 */       updateData(traceClient.getRxBuffer());
/* 456 */       this.watchTableViewer.refresh();
/* 457 */       this.historyTableViewer.refresh();
/*     */ 
/* 460 */       ISelection selection = this.watchTableViewer.getSelection();
/* 461 */       if ((selection.isEmpty()) && (this.comparators.size() > 0)) {
/* 462 */         this.watchTableViewer.getTable().setSelection(0);
/* 463 */         selection = this.watchTableViewer.getSelection();
/* 464 */         if ((selection instanceof IStructuredSelection)) {
/* 465 */           StructuredSelection ss = (StructuredSelection)selection;
/* 466 */           DataTrace dt = (DataTrace)ss.getFirstElement();
/*     */ 
/* 468 */           displayHistory(dt);
/*     */         }
/*     */       }
/*     */ 
/* 472 */       if (this.autoScroll)
/*     */       {
/* 474 */         Table table = this.historyTableViewer.getTable();
/* 475 */         int itemCount = table.getItemCount();
/*     */ 
/* 477 */         if (itemCount > 0)
/* 478 */           table.showItem(table.getItem(itemCount - 1));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resetView()
/*     */   {
/* 489 */     this.indexCtr = 0;
/* 490 */     this.comparators = new HashMap();
/* 491 */     this.watchTableViewer.setInput(this.comparators);
/*     */ 
/* 494 */     displayHistory(null);
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 499 */     this.currentClient = null;
/* 500 */     resetView();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 506 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 507 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 509 */     if (traceClient != null)
/*     */     {
/* 511 */       if (!traceClient.equals(this.currentClient)) {
/* 512 */         this.currentClient = traceClient;
/* 513 */         resetView();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 523 */     super.handleClearEvent();
/* 524 */     resetView();
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 537 */     this.viewDisposed = true;
/* 538 */     SWTResourceManager.dispose(SWVPlugin.getDefault().getBundle());
/* 539 */     if ((this.clipboard != null) && 
/* 540 */       (!this.clipboard.isDisposed())) {
/* 541 */       this.clipboard.dispose();
/*     */     }
/* 543 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 548 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public void setAutoScroll(boolean autoScroll)
/*     */   {
/* 556 */     this.autoScroll = autoScroll;
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/* 561 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public TableViewer getHistoryTable() {
/* 565 */     return this.historyTableViewer;
/*     */   }
/*     */ 
/*     */   private void displayHistory(DataTrace dataTrace)
/*     */   {
/* 574 */     if (dataTrace == null) {
/* 575 */       this.historyForLbl.setText(Messages.SWVDataTraceView_HISTORY);
/* 576 */       this.historyTableViewer.setInput(new Object[0]);
/* 577 */       this.actionClearHistory.setEnabled(true);
/*     */     } else {
/* 579 */       SWVComparatorConfig cmpConfig = dataTrace.getComparatorConfig();
/* 580 */       if (cmpConfig != null)
/*     */       {
/* 582 */         String symbol = cmpConfig.getSymbol();
/* 583 */         if (symbol != null) {
/* 584 */           this.historyForLbl.setText(Messages.SWVDataTraceView_HISTORY + " (" + symbol + ")");
/*     */         }
/*     */ 
/* 587 */         this.historyTableViewer.setInput(dataTrace);
/* 588 */         this.actionClearHistory.setEnabled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClearHistoryAction extends Action
/*     */   {
/*     */     public ClearHistoryAction(SWVDataTraceView view)
/*     */     {
/* 119 */       setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(SWVPlugin.getDefault().getBundle(), "clear.gif")));
/* 120 */       setToolTipText(Messages.SWVDataTraceView_CLEAR_HISTORY);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 125 */       ISelection selection = SWVDataTraceView.this.watchTableViewer.getSelection();
/*     */ 
/* 127 */       if ((selection instanceof IStructuredSelection)) {
/* 128 */         IStructuredSelection sselection = (IStructuredSelection)selection;
/* 129 */         Object element = sselection.getFirstElement();
/* 130 */         if ((element instanceof DataTrace)) {
/* 131 */           DataTrace dataTrace = (DataTrace)element;
/* 132 */           dataTrace.clearHistory();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ScrollLockAction extends Action
/*     */   {
/*     */     private SWVDataTraceView view;
/*     */ 
/*     */     public ScrollLockAction(SWVDataTraceView view)
/*     */     {
/*  99 */       super(2);
/* 100 */       this.view = view;
/* 101 */       setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(SWVPlugin.getDefault().getBundle(), "lock_co.gif")));
/* 102 */       setToolTipText(Messages.SWVDataTraceView_SCROLL_LOCK);
/* 103 */       setChecked(false);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 108 */       this.view.setAutoScroll(!isChecked());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ValueFormatAction extends Action
/*     */   {
/*     */     private int valueFormat;
/*     */ 
/*     */     public ValueFormatAction(String label, int format)
/*     */     {
/* 147 */       super(8);
/* 148 */       this.valueFormat = format;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 153 */       SWVDataTraceView.this.setValueFormat(this.valueFormat);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceView
 * JD-Core Version:    0.6.2
 */