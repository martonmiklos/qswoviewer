/*     */ package com.atollic.truestudio.swv.core.ui;
/*     */ 
/*     */ import com.atollic.truestudio.common.utilities.exposed.StructuredViewerItemClipboardCopier;
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.jface.viewers.TableViewerColumn;
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
/*     */ public class SWVLogView extends SWVView
/*     */ {
/*     */   public Composite baseComposite;
/*     */   private Table table;
/*     */   private TableViewer tableViewer;
/*     */   private Label overflowLabel;
/*  45 */   private boolean viewDisposed = false;
/*  46 */   private boolean autoScroll = true;
/*  47 */   private SWVClient currentClient = null;
/*     */   private Action actionScrollLock;
/*  53 */   private Clipboard clipboard = null;
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  64 */     super.createPartControl(parent);
/*  65 */     this.baseComposite = new Composite(parent, 536870912);
/*  66 */     this.baseComposite.setLayout(new GridLayout(1, false));
/*     */ 
/*  69 */     boolean hasPermission = ProductManager.hasPermission(TSFeature.SWV_TRACE_LOG);
/*     */ 
/*  71 */     if (!hasPermission)
/*     */     {
/*  73 */       DemoHelper.createDemoModeLabel(this.baseComposite, "swv-generic");
/*     */     }
/*     */ 
/*  76 */     this.tableViewer = new TableViewer(this.baseComposite, 268503042);
/*  77 */     this.table = this.tableViewer.getTable();
/*  78 */     this.table.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*  79 */     this.table.setLinesVisible(true);
/*  80 */     this.table.setHeaderVisible(true);
/*     */ 
/*  82 */     this.table.addMouseListener(new SWVLogViewListener(this));
/*     */ 
/*  84 */     TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, 0);
/*  85 */     TableColumn tableColumn0 = tableViewerColumn.getColumn();
/*  86 */     tableColumn0.setWidth(80);
/*  87 */     tableColumn0.setText(Messages.SWVLogView_INDEX);
/*     */ 
/*  89 */     TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.tableViewer, 0);
/*  90 */     TableColumn tableColumn1 = tableViewerColumn_4.getColumn();
/*  91 */     tableColumn1.setWidth(110);
/*  92 */     tableColumn1.setText(Messages.SWVLogView_TYPE);
/*     */ 
/*  94 */     TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.tableViewer, 0);
/*  95 */     TableColumn tableColumn4 = tableViewerColumn_1.getColumn();
/*  96 */     tableColumn4.setWidth(130);
/*  97 */     if (hasPermission)
/*  98 */       tableColumn4.setText(Messages.SWVLogView_DATA);
/*     */     else {
/* 100 */       tableColumn4.setText(Messages.SWVLogView_DATA);
/*     */     }
/*     */ 
/* 103 */     TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(this.tableViewer, 0);
/* 104 */     TableColumn tableColumn2 = tableViewerColumn_3.getColumn();
/* 105 */     tableColumn2.setWidth(100);
/* 106 */     if (hasPermission)
/* 107 */       tableColumn2.setText(Messages.SWVLogView_CYCLES);
/*     */     else {
/* 109 */       tableColumn2.setText(Messages.SWVLogView_CYCLES);
/*     */     }
/*     */ 
/* 112 */     TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(this.tableViewer, 0);
/* 113 */     TableColumn tableColumn3 = tableViewerColumn_2.getColumn();
/* 114 */     tableColumn3.setWidth(100);
/* 115 */     if (hasPermission)
/* 116 */       tableColumn3.setText(Messages.SWVLogView_TIME);
/*     */     else {
/* 118 */       tableColumn3.setText(Messages.SWVLogView_TIME);
/*     */     }
/*     */ 
/* 121 */     TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(this.tableViewer, 0);
/* 122 */     TableColumn tableColumn5 = tableViewerColumn_5.getColumn();
/* 123 */     tableColumn5.setWidth(150);
/* 124 */     if (hasPermission)
/* 125 */       tableColumn5.setText(Messages.SWVLogView_EXTRA_INFO);
/*     */     else {
/* 127 */       tableColumn5.setText(Messages.SWVLogView_EXTRA_INFO);
/*     */     }
/*     */ 
/* 130 */     this.tableViewer.setLabelProvider(new SWVLogViewLabelProvider());
/* 131 */     this.tableViewer.setContentProvider(new SWVLogViewContentProvider());
/*     */ 
/* 133 */     Composite labels = new Composite(this.baseComposite, 536870912);
/* 134 */     labels.setLayoutData(new GridData(16384, 128, true, false, 1, 1));
/* 135 */     GridLayout gl_labels = new GridLayout(1, true);
/* 136 */     labels.setLayout(gl_labels);
/*     */ 
/* 138 */     this.overflowLabel = new Label(labels, 0);
/* 139 */     this.overflowLabel.setText(Messages.SWVLogView_OVERFLOW_PACKAGES + ": 0                    ");
/*     */     try
/*     */     {
/* 143 */       this.clipboard = new Clipboard(getSite().getShell().getDisplay());
/* 144 */       getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.tableViewer, this.clipboard));
/*     */     } catch (Exception e) {
/* 146 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 150 */     createActions();
/*     */ 
/* 153 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 154 */     SWVClient traceClient = sessionManager.getClient();
/* 155 */     this.currentClient = traceClient;
/*     */ 
/* 157 */     if (traceClient != null) {
/* 158 */       this.tableViewer.setInput(traceClient.getRxBuffer());
/*     */     }
/*     */ 
/* 162 */     startPeriodicUpdate(500);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 170 */     this.actionScrollLock = new ScrollLockAction(this);
/*     */ 
/* 172 */     IActionBars bars = getViewSite().getActionBars();
/* 173 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/* 174 */     toolBarManager.add(new Separator());
/* 175 */     toolBarManager.add(this.actionScrollLock);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 184 */     this.viewDisposed = true;
/* 185 */     if ((this.clipboard != null) && 
/* 186 */       (!this.clipboard.isDisposed())) {
/* 187 */       this.clipboard.dispose();
/*     */     }
/* 189 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 194 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 205 */     this.currentClient = null;
/* 206 */     resetTable();
/*     */   }
/*     */ 
/*     */   private void resetTable() {
/* 210 */     this.tableViewer.setInput(new SWVBuffer(getCurrentClient()));
/* 211 */     this.tableViewer.refresh();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 217 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 218 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 220 */     if (traceClient != null)
/*     */     {
/* 222 */       if (!traceClient.equals(this.currentClient)) {
/* 223 */         this.tableViewer.setInput(traceClient.getRxBuffer());
/* 224 */         this.currentClient = traceClient;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public TableViewer getViewer() {
/* 230 */     return this.tableViewer;
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 241 */     int[] indices = getViewer().getTable().getSelectionIndices();
/* 242 */     getViewer().setSelection(null);
/* 243 */     getViewer().refresh();
/* 244 */     getViewer().getTable().select(indices);
/* 245 */     if (this.currentClient != null) {
/* 246 */       String packets = Messages.SWVLogView_OVERFLOW_PACKAGES + ": " + this.currentClient.getOverflowPacketsCount();
/* 247 */       this.overflowLabel.setText(packets);
/* 248 */       this.overflowLabel.pack();
/*     */     }
/* 250 */     if (this.autoScroll)
/*     */     {
/* 252 */       Table table = getViewer().getTable();
/* 253 */       int itemCount = table.getItemCount();
/*     */ 
/* 255 */       if (itemCount > 0)
/* 256 */         table.showItem(table.getItem(itemCount - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAutoScroll(boolean autoScroll)
/*     */   {
/* 265 */     this.autoScroll = autoScroll;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.SWVLogView
 * JD-Core Version:    0.6.2
 */