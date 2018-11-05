/*     */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*     */ 
/*     */ import com.atollic.truestudio.common.utilities.exposed.StructuredViewerItemClipboardCopier;
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSProjectManager;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.model.DWTPCSampleEvent;
/*     */ import java.io.File;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.eclipse.cdt.core.IAddress;
/*     */ import org.eclipse.cdt.utils.Addr32;
/*     */ import org.eclipse.cdt.utils.elf.Elf;
/*     */ import org.eclipse.cdt.utils.elf.Elf.Symbol;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.variables.IStringVariableManager;
/*     */ import org.eclipse.core.variables.VariablesPlugin;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
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
/*     */ public class SWVStatisticalProfilingView extends SWVView
/*     */ {
/*     */   public Composite baseComposite;
/*     */   private Table table;
/*     */   private TableViewer tableViewer;
/*     */   private Label overflowLabel;
/*  57 */   private boolean viewDisposed = false;
/*  58 */   private SWVClient currentClient = null;
/*     */ 
/*  61 */   private Clipboard clipboard = null;
/*     */ 
/*  63 */   private ArrayList<SWVStatisticalProfilingItem> swvStatisticalProfilingItems = null;
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/*  71 */     super.createPartControl(parent);
/*  72 */     this.baseComposite = new Composite(parent, 536870912);
/*  73 */     this.baseComposite.setLayout(new GridLayout(1, false));
/*     */ 
/*  75 */     if (!ProductManager.hasPermission(TSFeature.SWV_STATISTICAL_PROFILING)) {
/*  76 */       DemoHelper.createDemoModeLabel(this.baseComposite, "swv-statistical-profiling");
/*     */     }
/*     */ 
/*  79 */     this.tableViewer = new TableViewer(this.baseComposite, 268503042);
/*  80 */     this.table = this.tableViewer.getTable();
/*  81 */     this.table.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*  82 */     this.table.setLinesVisible(true);
/*  83 */     this.table.setHeaderVisible(true);
/*  84 */     this.table.addMouseListener(new SWVStatisticalLogListener(this));
/*     */ 
/*  86 */     TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, 0);
/*  87 */     TableColumn tableColumn0 = tableViewerColumn.getColumn();
/*  88 */     tableColumn0.setWidth(150);
/*  89 */     tableColumn0.setText(Messages.SWVStatisticalProfilingView_FUNCTION);
/*  90 */     tableColumn0.setToolTipText(Messages.SWVStatisticalProfilingView_NAME_OF_FUNCTION_TO_JUMP_TO);
/*     */ 
/*  93 */     TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(this.tableViewer, 0);
/*  94 */     TableColumn tableColumn4 = tableViewerColumn_1.getColumn();
/*  95 */     tableColumn4.setWidth(100);
/*  96 */     tableColumn4.setText(Messages.SWVStatisticalProfilingView_PERCENTAGES_IN_USE);
/*     */ 
/*  98 */     TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(this.tableViewer, 0);
/*  99 */     TableColumn tableColumn3 = tableViewerColumn_3.getColumn();
/* 100 */     tableColumn3.setWidth(100);
/* 101 */     tableColumn3.setText(Messages.SWVStatisticalProfilingView_SAMPLES);
/*     */ 
/* 104 */     TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(this.tableViewer, 0);
/* 105 */     TableColumn tableColumn1 = tableViewerColumn_4.getColumn();
/* 106 */     tableColumn1.setWidth(100);
/* 107 */     tableColumn1.setText(Messages.SWVStatisticalProfilingView_START_ADDRESS);
/*     */ 
/* 109 */     TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(this.tableViewer, 0);
/* 110 */     TableColumn tableColumn2 = tableViewerColumn_2.getColumn();
/* 111 */     tableColumn2.setWidth(100);
/* 112 */     tableColumn2.setText(Messages.SWVStatisticalProfilingView_SIZE);
/*     */ 
/* 115 */     this.tableViewer.setLabelProvider(new SWVStatisticalProfilingViewLabelProvider());
/* 116 */     this.tableViewer.setContentProvider(new SWVStatisticalProfilingViewContentProvider());
/*     */ 
/* 118 */     tableViewerColumn_1.setLabelProvider(new SWVStatisticalProfilingViewStyledLabelProvider());
/*     */ 
/* 121 */     Composite labels = new Composite(this.baseComposite, 536870912);
/* 122 */     labels.setLayoutData(new GridData(4, 128, true, false, 1, 1));
/* 123 */     GridLayout gl_labels = new GridLayout(1, true);
/* 124 */     labels.setLayout(gl_labels);
/*     */ 
/* 126 */     this.overflowLabel = new Label(labels, 0);
/* 127 */     this.overflowLabel.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/* 128 */     this.overflowLabel.setText(Messages.SWVStatisticalProfilingView_OVERFLOW_PACKETS + ": 0     " + Messages.SWVStatisticalProfilingView_PC_SAMPLES + ": 0                              ");
/*     */ 
/* 132 */     createActions();
/*     */     try
/*     */     {
/* 136 */       this.clipboard = new Clipboard(getSite().getShell().getDisplay());
/* 137 */       getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), new StructuredViewerItemClipboardCopier(this.tableViewer, this.clipboard));
/*     */     } catch (Exception e) {
/* 139 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 143 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 144 */     SWVClient traceClient = sessionManager.getClient();
/* 145 */     this.currentClient = traceClient;
/*     */ 
/* 147 */     if (traceClient != null) {
/* 148 */       refreshData();
/* 149 */       this.tableViewer.setInput(this.swvStatisticalProfilingItems);
/* 150 */       this.tableViewer.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 166 */     this.viewDisposed = true;
/* 167 */     if ((this.clipboard != null) && 
/* 168 */       (!this.clipboard.isDisposed())) {
/* 169 */       this.clipboard.dispose();
/*     */     }
/* 171 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 176 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 187 */     this.currentClient = null;
/* 188 */     resetTable();
/*     */   }
/*     */ 
/*     */   private void resetTable() {
/* 192 */     this.tableViewer.setInput(new ArrayList());
/* 193 */     this.tableViewer.refresh();
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 198 */     super.handleTargetSuspended();
/*     */ 
/* 214 */     refreshData();
/* 215 */     this.tableViewer.setInput(this.swvStatisticalProfilingItems);
/*     */ 
/* 219 */     this.tableViewer.refresh();
/*     */   }
/*     */ 
/*     */   public void handleTargetResume()
/*     */   {
/* 230 */     super.handleTargetResume();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 236 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 237 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 239 */     if (traceClient != null)
/*     */     {
/* 241 */       if (!traceClient.equals(this.currentClient)) {
/* 242 */         this.currentClient = traceClient;
/* 243 */         refreshData();
/* 244 */         this.tableViewer.setInput(this.swvStatisticalProfilingItems);
/* 245 */         this.tableViewer.refresh();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 255 */     super.handleClearEvent();
/* 256 */     resetTable();
/*     */   }
/*     */ 
/*     */   public TableViewer getViewer() {
/* 260 */     return this.tableViewer;
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/* 266 */     if (this.currentClient == null) {
/* 267 */       return;
/*     */     }
/* 269 */     Object[] records = this.currentClient.getRxBuffer().getRecords();
/* 270 */     HashMap hashedAddresses = new HashMap();
/* 271 */     long total = 0L;
/* 272 */     if (records != null) {
/* 273 */       for (Object record : records) {
/* 274 */         if ((record instanceof DWTPCSampleEvent)) {
/* 275 */           total += 1L;
/* 276 */           Long address = Long.valueOf(((DWTPCSampleEvent)record).getPcAddress());
/* 277 */           if (hashedAddresses.containsKey(address))
/* 278 */             hashedAddresses.put(address, Long.valueOf(((Long)hashedAddresses.get(address)).longValue() + 1L));
/*     */           else {
/* 280 */             hashedAddresses.put(address, new Long(1L));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 287 */       String programPath = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration().getAttribute("org.eclipse.cdt.launch.PROGRAM_NAME", "");
/* 288 */       if (programPath != null) {
/* 289 */         programPath = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(programPath, true);
/*     */       }
/*     */ 
/* 293 */       File file1 = new File(programPath);
/* 294 */       if (!file1.isFile())
/*     */       {
/* 297 */         String project = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration().getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", "");
/* 298 */         IPath path = TSProjectManager.getProjectPath(project);
/*     */ 
/* 300 */         if (path != null) {
/* 301 */           programPath = path.append(programPath).toOSString();
/*     */         }
/*     */         else {
/* 304 */           return;
/*     */         }
/*     */       }
/* 307 */       Elf elf = SWVClient.getElf(programPath);
/*     */ 
/* 309 */       Iterator it = hashedAddresses.entrySet().iterator();
/* 310 */       HashMap functions = new HashMap();
/*     */ 
/* 312 */       while (it.hasNext()) {
/* 313 */         Map.Entry entry = (Map.Entry)it.next();
/*     */ 
/* 316 */         long addr = ((Long)entry.getKey()).longValue() | 1L;
/*     */ 
/* 319 */         Elf.Symbol symbol = elf.getSymbol(new Addr32(addr));
/* 320 */         if (symbol != null)
/*     */         {
/* 322 */           long startAddress = symbol.st_value.getValue().longValue();
/* 323 */           long endAddress = symbol.st_value.getValue().longValue() + symbol.st_size;
/*     */ 
/* 325 */           String functionName = Messages.SWVStatisticalProfilingView_UNKNOWN_FUNCTION;
/* 326 */           if ((addr >= startAddress) && (addr <= endAddress)) {
/* 327 */             functionName = symbol.toString() + "()";
/*     */           }
/*     */ 
/* 335 */           if (functions.containsKey(functionName)) {
/* 336 */             SWVStatisticalProfilingItem item = new SWVStatisticalProfilingItem(functionName, startAddress, endAddress, ((Long)entry.getValue()).longValue() + ((SWVStatisticalProfilingItem)functions.get(functionName)).getOccurrences(), (float)(((Long)entry.getValue()).longValue() + ((SWVStatisticalProfilingItem)functions.get(functionName)).getOccurrences()) * 100.0F / (float)total);
/* 337 */             functions.put(functionName, item);
/*     */           } else {
/* 339 */             functions.put(functionName, new SWVStatisticalProfilingItem(functionName, startAddress, endAddress, ((Long)entry.getValue()).longValue(), (float)((Long)entry.getValue()).longValue() * 100.0F / (float)total));
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 345 */       this.swvStatisticalProfilingItems = new ArrayList();
/* 346 */       Iterator funcList = functions.entrySet().iterator();
/* 347 */       while (funcList.hasNext()) {
/* 348 */         this.swvStatisticalProfilingItems.add((SWVStatisticalProfilingItem)((Map.Entry)funcList.next()).getValue());
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 353 */         Collections.sort(this.swvStatisticalProfilingItems);
/*     */       } catch (Exception e) {
/* 355 */         e.printStackTrace();
/*     */       }
/*     */ 
/* 359 */       if (this.currentClient != null) {
/* 360 */         String packets = Messages.SWVStatisticalProfilingView_OVERFLOW_PACKETS + ": " + this.currentClient.getOverflowPacketsCount();
/* 361 */         packets = packets + "     " + Messages.SWVStatisticalProfilingView_PC_SAMPLES + ": " + total + "     ";
/* 362 */         this.overflowLabel.setText(packets);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 368 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalProfilingView
 * JD-Core Version:    0.6.2
 */