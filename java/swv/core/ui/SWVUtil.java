/*     */ package com.atollic.truestudio.swv.core.ui;
/*     */ 
/*     */ import com.atollic.truestudio.dsf.mi.commands.CLIInfoLineInfo;
/*     */ import com.atollic.truestudio.swv.core.MemoryAccess;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.jface.text.IDocument;
/*     */ import org.eclipse.jface.text.IRegion;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.ide.IDE;
/*     */ import org.eclipse.ui.texteditor.IDocumentProvider;
/*     */ import org.eclipse.ui.texteditor.ITextEditor;
/*     */ 
/*     */ public class SWVUtil
/*     */ {
/*     */   private static final String EMPTY_STRING = "";
/*     */   public static final String OPEN_CONFIG_COMMAND_ID = "com.atollic.truestudio.swv.core.openconfig";
/*     */   public static final long DEMCR_BASE = 3758157308L;
/*     */   public static final long DEMCR_TRCENA_BITS = 16777216L;
/*     */   public static final long DMCU_CR_BASE = 3758366724L;
/*     */   public static final long DMCU_CR_TRACEIO_ENA_BITS = 32L;
/*     */   public static final long DMCU_CR_TRACEMODE_ASYNC_BITS = 0L;
/*     */   public static final long DMCU_CR_TRACEMODE_SYNC_1B_BITS = 64L;
/*     */   public static final long DMCU_CR_TRACEMODE_SYNC_2B_BITS = 128L;
/*     */   public static final long DMCU_CR_TRACEMODE_SYNC_4B_BITS = 192L;
/*     */   public static final long SPINR_BASE = 3758358768L;
/*     */   public static final long SPINR_SWO_BITS = 2L;
/*     */   public static final long ACPR_BASE = 3758358544L;
/*     */   public static final long DWT_CTRL = 3758100480L;
/*     */   public static final long DWT_CTRL_Reset = 0L;
/*     */   public static final long DWT_CTRL_NUMCOMP_BITS = 4026531840L;
/*     */   public static final long DWT_CTRL_CYC_EVT_ENA_BITS = 4194304L;
/*     */   public static final long DWT_CTRL_FOLD_EVT_ENA_BITS = 2097152L;
/*     */   public static final long DWT_CTRL_LSU_EVT_ENA_BITS = 1048576L;
/*     */   public static final long DWT_CTRL_SLEEP_EVT_ENA_BITS = 524288L;
/*     */   public static final long DWT_CTRL_EXCEPTION_EVT_ENA_BITS = 262144L;
/*     */   public static final long DWT_CTRL_CPI_EVT_ENA_BITS = 131072L;
/*     */   public static final long DWT_CTRL_EXCEPTION_TRC_ENA_BITS = 65536L;
/*     */   public static final long DWT_CTRL_PC_SAMPLE_ENA_BITS = 4096L;
/*     */   public static final long DWT_CTRL_PC_SAMPLE_SLOW_BITS = 512L;
/*     */   public static final long DWT_CTRL_SYNC_FAST_BITS = 1024L;
/*     */   public static final long DWT_CTRL_SYNC_MEDIUM_BITS = 2048L;
/*     */   public static final long DWT_CTRL_SYNC_SLOW_BITS = 3072L;
/*     */   public static final long DWT_CTRL_POSTCNT_INIT_LSHIFT = 5L;
/*     */   public static final long DWT_CTRL_POSTCNT_INIT_PRESHIFT_MASK = 15L;
/*     */   public static final long DWT_CTRL_POSTCNT_RELOAD_LSHIFT = 1L;
/*     */   public static final long DWT_CTRL_POSTCNT_RELOAD_PRESHIFT_MASK = 15L;
/*     */   public static final long DWT_CTRL_CYCCNT_ENA_BITS = 1L;
/*     */   public static final long DWT_CYCCNT = 3758100484L;
/*     */   public static final long DWT_CYCCNT_Reset = 0L;
/*     */   public static final long DWT_CPICNT = 3758100488L;
/*     */   public static final long DWT_EXCCNT = 3758100492L;
/*     */   public static final long DWT_SLEEPCNT = 3758100496L;
/*     */   public static final long DWT_LSUCNT = 3758100500L;
/*     */   public static final long DWT_FOLDCNT = 3758100504L;
/*     */   public static final long DWT_PCSR = 3758100508L;
/*     */   public static final long DWT_COMP0 = 3758100512L;
/*     */   public static final long DWT_MASK0 = 3758100516L;
/*     */   public static final long DWT_FUNCTION0 = 3758100520L;
/*     */   public static final long DWT_FUNCTION0_Reset = 0L;
/*     */   public static final long DWT_COMP1 = 3758100528L;
/*     */   public static final long DWT_MASK1 = 3758100532L;
/*     */   public static final long DWT_FUNCTION1 = 3758100536L;
/*     */   public static final long DWT_FUNCTION1_Reset = 0L;
/*     */   public static final long DWT_COMP2 = 3758100544L;
/*     */   public static final long DWT_MASK2 = 3758100548L;
/*     */   public static final long DWT_FUNCTION2 = 3758100552L;
/*     */   public static final long DWT_FUNCTION2_Reset = 0L;
/*     */   public static final long DWT_COMP3 = 3758100560L;
/*     */   public static final long DWT_MASK3 = 3758100564L;
/*     */   public static final long DWT_FUNCTION3 = 3758100568L;
/*     */   public static final long DWT_FUNCTION3_Reset = 0L;
/*     */   public static final long DWT_FUNCTION_DATAVMATCH_SIZE_BYTE_BITS = 0L;
/*     */   public static final long DWT_FUNCTION_DATAVMATCH_SIZE_HWORD_BITS = 16384L;
/*     */   public static final long DWT_FUNCTION_DATAVMATCH_SIZE_WORD_BITS = 32768L;
/*     */   public static final long DWT_FUNCTION_DATAVMATCH_ENA_BITS = 256L;
/*     */   public static final long DWT_FUNCTION_CYCMATCH_ENA_BITS = 128L;
/*     */   public static final long DWT_FUNCTION_EMITRANGE_BITS = 32L;
/*     */   public static final long ITM_Trace_Enable_Register = 3758099968L;
/*     */   public static final long ITM_TER_Reset = 0L;
/*     */   public static final long ITM_Trace_Privilege_Register = 3758100032L;
/*     */   public static final long ITM_TPR_Reset = 0L;
/*     */   public static final long ITM_Trace_Control_Register = 3758100096L;
/*     */   public static final long ITM_TCR_Reset = 0L;
/*     */   public static final long ITM_TCR_TRACE_BUS_ID_BITS = 65536L;
/*     */   public static final long ITM_TCR_GLOBAL_TIMESTAMP_ALWAYS_BITS = 3072L;
/*     */   public static final long ITM_TCR_TXENA_BITS = 8L;
/*     */   public static final long ITM_TCR_SYNCENA_BITS = 4L;
/*     */   public static final long ITM_TCR_LOCAL_TIMESTAMP_PRESCALER_LSHIFT = 8L;
/*     */   public static final long ITM_TCR_LOCAL_TIMESTAMP_ENA_BITS = 2L;
/*     */   public static final long ITM_TCR_ITMENA_BITS = 1L;
/*     */   public static final long ITM_Integration_Write = 3758100216L;
/*     */   public static final long ITM_Integration_Write_Reset = 0L;
/*     */   public static final long ITM_Integration_Read = 3758100220L;
/*     */   public static final long ITM_Integration_Read_Reset = 0L;
/*     */   public static final long ITM_Integration_Mode_Control = 3758100224L;
/*     */   public static final long ITM_Integration_Mode_Control_Reset = 0L;
/*     */   public static final long ITM_Lock_Access = 3758100400L;
/*     */   public static final long ITM_Lock_Access_Reset = 0L;
/*     */   public static final long CORESIGHT_ACCESS_KEY = 3316436565L;
/*     */   public static final long ITM_Lock_Status = 3758100404L;
/*     */   public static final long ITM_Lock_Status_Reset = 3L;
/*     */   public static final long TPIU_FFCR_BASE = 3758359300L;
/*     */   public static final long TPIU_FFCR_FRMTR_ENA_BITS = 1L;
/*     */   public static final long TPIU_FFCR_IND_TRIGIN_ASRT_BITS = 256L;
/*     */   public static final int CYCLE_SCALE = 0;
/*     */   public static final int TIME_SCALE = 1;
/*     */ 
/*     */   public static void jumpToLine(long address)
/*     */   {
/* 175 */     if (address == 0L) {
/* 176 */       return;
/*     */     }
/*     */ 
/* 179 */     CLIInfoLineInfo addressInfo = getAddressInfo(address);
/*     */ 
/* 181 */     if (addressInfo == null) {
/* 182 */       MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.SWVUtil_JUMP_TO_FILE, Messages.SWVUtil_NO_SOURCEFILE_AVAILABLE);
/*     */     }
/*     */ 
/* 185 */     if ((addressInfo != null) && (!addressInfo.isError()))
/*     */     {
/* 187 */       String fileName = addressInfo.getFileName();
/* 188 */       int lineNumber = addressInfo.getLineNumber();
/*     */ 
/* 190 */       if ((fileName != null) && (lineNumber > 0))
/*     */       {
/* 193 */         if (fileName.startsWith(".." + System.getProperty("file.separator"))) {
/* 194 */           fileName = fileName.substring(fileName.indexOf(System.getProperty("file.separator")));
/*     */         }
/*     */ 
/* 199 */         IProject project = getActiveProject();
/*     */ 
/* 201 */         if (project != null)
/*     */         {
/* 203 */           IFile fileToOpen = project.getFile(fileName);
/*     */ 
/* 206 */           if (fileToOpen.exists()) {
/* 207 */             IWorkbench workbench = PlatformUI.getWorkbench();
/* 208 */             IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
/*     */ 
/* 210 */             if (window != null) {
/* 211 */               IWorkbenchPage page = window.getActivePage();
/* 212 */               if (page != null)
/*     */                 try
/*     */                 {
/* 215 */                   IEditorPart editor = IDE.openEditor(page, fileToOpen);
/*     */ 
/* 218 */                   if ((editor instanceof ITextEditor)) {
/* 219 */                     ITextEditor textEditor = (ITextEditor)editor;
/* 220 */                     IDocument document = textEditor.getDocumentProvider().getDocument(editor.getEditorInput());
/* 221 */                     if (document != null)
/*     */                     {
/* 223 */                       IRegion regionInfo = document.getLineInformation(lineNumber - 1);
/* 224 */                       textEditor.selectAndReveal(regionInfo.getOffset(), regionInfo.getLength());
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 catch (Exception e1) {
/* 229 */                   e1.printStackTrace();
/*     */                 }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static CLIInfoLineInfo getAddressInfo(long address)
/*     */   {
/* 247 */     if (address == 0L) {
/* 248 */       return null;
/*     */     }
/* 250 */     SWVClient client = SWVPlugin.getDefault().getSessionManager().getClient();
/* 251 */     if (client != null) {
/* 252 */       CLIInfoLineInfo lineInfo = MemoryAccess.getLineForAddress(address, client.getSession());
/* 253 */       return lineInfo;
/*     */     }
/*     */ 
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   public static IProject getActiveProject()
/*     */   {
/* 265 */     ILaunchConfigurationWorkingCopy configuration = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration();
/*     */ 
/* 267 */     if (configuration != null)
/*     */     {
/* 269 */       String projectName = null;
/*     */       try {
/* 271 */         projectName = configuration.getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", "");
/* 272 */         if ((projectName != null) && (!projectName.isEmpty())) {
/* 273 */           IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
/* 274 */           if (project.exists())
/* 275 */             return project;
/*     */         }
/*     */       }
/*     */       catch (CoreException e)
/*     */       {
/* 280 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.SWVUtil
 * JD-Core Version:    0.6.2
 */