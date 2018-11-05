/*     */ package com.atollic.truestudio.swv.core.ui.exception;
/*     */ 
/*     */ import com.atollic.truestudio.common.utilities.exposed.StructuredViewerItemClipboardCopier;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
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
/*     */ public class NewSWVExceptionLogView extends SWVView
/*     */ {
/*     */   public Composite baseComposite;
/*     */   private Table table;
/*     */   private TableViewer tableViewer;
/*     */   private Label overflowLabel;
/*  41 */   private boolean viewDisposed = false;
/*  42 */   private boolean autoScroll = true;
/*  43 */   private SWVClient currentClient = null;
/*     */ 
/*  49 */   private Clipboard clipboard = null;
/*     */   private CTabFolder tabFolder;
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  59 */     super.createPartControl(parent);
/*  60 */     this.baseComposite = new Composite(parent, 536870912);
/*  61 */     this.baseComposite.setLayout(new GridLayout(1, false));
/*     */ 
/*  63 */     this.tabFolder = new CTabFolder(this.baseComposite, 8390656);
/*  64 */     this.tabFolder.setLayoutData(new GridData(16384, 4, true, true, 1, 1));
/*  65 */     this.tabFolder.setSimple(false);
/*  66 */     CTabItem tabItem = new CTabItem(this.tabFolder, 0);
/*  67 */     tabItem.setText(Messages.SWVExceptionLogView_DATA + " ");
/*  68 */     tabItem.setToolTipText(Messages.NewSWVExceptionLogView_THE_DATATRACE);
/*  69 */     this.tabFolder.setSelection(tabItem);
/*     */ 
/*  75 */     this.tableViewer = new TableViewer(this.tabFolder, 268503042);
/*  76 */     this.table = this.tableViewer.getTable();
/*  77 */     tabItem.setControl(this.table);
/*     */ 
/*  79 */     this.table.setLinesVisible(true);
/*  80 */     this.table.setHeaderVisible(true);
/*     */ 
/*  82 */     TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, 0);
/*  83 */     TableColumn tableColumn0 = tableViewerColumn.getColumn();
/*  84 */     tableColumn0.setWidth(80);
/*  85 */     tableColumn0.setText(Messages.SWVExceptionLogView_INDEX);
/*     */ 
/*  87 */     TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.tableViewer, 0);
/*  88 */     TableColumn tableColumn1 = tableViewerColumn_4.getColumn();
/*  89 */     tableColumn1.setWidth(100);
/*  90 */     tableColumn1.setText(Messages.SWVExceptionLogView_TYPE);
/*     */ 
/*  92 */     TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.tableViewer, 0);
/*  93 */     TableColumn tableColumn4 = tableViewerColumn_1.getColumn();
/*  94 */     tableColumn4.setWidth(120);
/*  95 */     tableColumn4.setText(Messages.SWVExceptionLogView_NAME);
/*     */ 
/*  97 */     TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(this.tableViewer, 0);
/*  98 */     TableColumn tableColumn7 = tableViewerColumn_7.getColumn();
/*  99 */     tableColumn7.setWidth(80);
/* 100 */     tableColumn7.setText(Messages.SWVExceptionLogView_PERIPHERAL);
/*     */ 
/* 102 */     TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(this.tableViewer, 0);
/* 103 */     TableColumn tableColumn6 = tableViewerColumn_6.getColumn();
/* 104 */     tableColumn6.setWidth(100);
/* 105 */     tableColumn6.setText(Messages.SWVExceptionLogView_FUNCTION);
/*     */ 
/* 107 */     TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(this.tableViewer, 0);
/* 108 */     TableColumn tableColumn2 = tableViewerColumn_3.getColumn();
/* 109 */     tableColumn2.setWidth(100);
/* 110 */     tableColumn2.setText(Messages.SWVExceptionLogView_CYCLES);
/*     */ 
/* 112 */     TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(this.tableViewer, 0);
/* 113 */     TableColumn tableColumn3 = tableViewerColumn_2.getColumn();
/* 114 */     tableColumn3.setWidth(110);
/* 115 */     tableColumn3.setText(Messages.SWVExceptionLogView_TIME);
/*     */ 
/* 117 */     TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(this.tableViewer, 0);
/* 118 */     TableColumn tableColumn5 = tableViewerColumn_5.getColumn();
/* 119 */     tableColumn5.setWidth(80);
/* 120 */     tableColumn5.setText(Messages.SWVExceptionLogView_EXTRA_INFO);
/*     */ 
/* 122 */     this.tableViewer.setLabelProvider(new SWVExceptionLogViewLabelProvider());
/* 123 */     this.tableViewer.setContentProvider(new SWVExceptionLogViewContentProvider());
/* 124 */     getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.tableViewer, this.clipboard));
/*     */ 
/* 126 */     Composite labels = new Composite(this.baseComposite, 536870912);
/* 127 */     labels.setLayoutData(new GridData(16384, 128, true, false, 1, 1));
/* 128 */     GridLayout gl_labels = new GridLayout(1, true);
/* 129 */     labels.setLayout(gl_labels);
/*     */ 
/* 131 */     this.overflowLabel = new Label(labels, 0);
/* 132 */     this.overflowLabel.setText(Messages.SWVExceptionLogView_OVERFLOW_PACKETS + ": 0                    ");
/*     */ 
/* 135 */     createActions();
/*     */     try
/*     */     {
/* 139 */       this.clipboard = new Clipboard(getSite().getShell().getDisplay());
/*     */     } catch (Exception e) {
/* 141 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 145 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 146 */     SWVClient traceClient = sessionManager.getClient();
/* 147 */     this.currentClient = traceClient;
/*     */ 
/* 149 */     if (traceClient != null) {
/* 150 */       this.tableViewer.setInput(traceClient.getExceptionBuffer());
/*     */     }
/*     */ 
/* 154 */     startPeriodicUpdate(500);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 176 */     this.viewDisposed = true;
/* 177 */     if ((this.clipboard != null) && 
/* 178 */       (!this.clipboard.isDisposed())) {
/* 179 */       this.clipboard.dispose();
/*     */     }
/* 181 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 186 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 195 */     this.currentClient = null;
/* 196 */     resetTable();
/*     */   }
/*     */ 
/*     */   private void resetTable() {
/* 200 */     this.tableViewer.setInput(new SWVBuffer(getCurrentClient()));
/* 201 */     this.tableViewer.refresh();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 207 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 208 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 210 */     if (traceClient != null)
/*     */     {
/* 212 */       if (!traceClient.equals(getCurrentClient())) {
/* 213 */         this.tableViewer.setInput(traceClient.getExceptionBuffer());
/* 214 */         this.currentClient = traceClient;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 221 */     super.handleTargetSuspended();
/*     */   }
/*     */ 
/*     */   public TableViewer getViewer() {
/* 225 */     return this.tableViewer;
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/* 230 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 240 */     int[] indices = getViewer().getTable().getSelectionIndices();
/* 241 */     getViewer().setSelection(null);
/* 242 */     getViewer().refresh();
/* 243 */     getViewer().getTable().select(indices);
/* 244 */     if (getCurrentClient() != null) {
/* 245 */       String packets = Messages.SWVExceptionLogView_OVERFLOW_PACKETS + ": " + getCurrentClient().getOverflowPacketsCount();
/* 246 */       this.overflowLabel.setText(packets);
/* 247 */       this.overflowLabel.pack();
/*     */     }
/* 249 */     if (this.autoScroll)
/*     */     {
/* 251 */       Table table = getViewer().getTable();
/* 252 */       int itemCount = table.getItemCount();
/*     */ 
/* 254 */       if (itemCount > 0)
/* 255 */         table.showItem(table.getItem(itemCount - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAutoScroll(boolean autoScroll)
/*     */   {
/* 264 */     this.autoScroll = autoScroll;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.NewSWVExceptionLogView
 * JD-Core Version:    0.6.2
 */