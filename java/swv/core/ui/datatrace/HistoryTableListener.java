/*     */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*     */ 
/*     */ import com.atollic.truestudio.dsf.mi.commands.CLIInfoLineInfo;
/*     */ import com.atollic.truestudio.swv.core.MemoryAccess;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.jface.text.IDocument;
/*     */ import org.eclipse.jface.text.IRegion;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.swt.events.MouseAdapter;
/*     */ import org.eclipse.swt.events.MouseEvent;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.ide.IDE;
/*     */ import org.eclipse.ui.texteditor.IDocumentProvider;
/*     */ import org.eclipse.ui.texteditor.ITextEditor;
/*     */ 
/*     */ public class HistoryTableListener extends MouseAdapter
/*     */ {
/*     */   private SWVDataTraceView view;
/*     */   private static final String PROPERTY_FILE_SEPARATOR = "file.separator";
/*     */ 
/*     */   public HistoryTableListener(SWVDataTraceView view)
/*     */   {
/*  41 */     this.view = view;
/*     */   }
/*     */ 
/*     */   public void mouseDoubleClick(MouseEvent e)
/*     */   {
/*  51 */     ISelection selection = this.view.getHistoryTable().getSelection();
/*  52 */     if ((selection instanceof IStructuredSelection)) {
/*  53 */       IStructuredSelection sselection = (IStructuredSelection)selection;
/*  54 */       Object firstElement = sselection.getFirstElement();
/*     */ 
/*  56 */       if ((firstElement instanceof Data)) {
/*  57 */         Data traceRecord = (Data)firstElement;
/*  58 */         long pc = traceRecord.getPC();
/*     */ 
/*  61 */         if (-1L != pc)
/*     */         {
/*  64 */           CLIInfoLineInfo addressInfo = getAddressInfo(pc);
/*     */ 
/*  66 */           if ((addressInfo != null) && (!addressInfo.isError()))
/*     */           {
/*  68 */             String fileName = addressInfo.getFileName();
/*  69 */             int lineNumber = addressInfo.getLineNumber();
/*     */ 
/*  71 */             if ((fileName != null) && (lineNumber > 0))
/*     */             {
/*  74 */               if (fileName.startsWith(".." + System.getProperty("file.separator"))) {
/*  75 */                 fileName = fileName.substring(fileName.indexOf(System.getProperty("file.separator")));
/*     */               }
/*     */ 
/*  80 */               IProject project = getProject();
/*     */ 
/*  82 */               if (project != null)
/*     */               {
/*  84 */                 IFile fileToOpen = project.getFile(fileName);
/*     */ 
/*  87 */                 if (fileToOpen.exists()) {
/*  88 */                   IWorkbench workbench = PlatformUI.getWorkbench();
/*  89 */                   IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
/*     */ 
/*  91 */                   if (window != null) {
/*  92 */                     IWorkbenchPage page = window.getActivePage();
/*  93 */                     if (page != null)
/*     */                       try
/*     */                       {
/*  96 */                         IEditorPart editor = IDE.openEditor(page, fileToOpen);
/*     */ 
/*  99 */                         if ((editor instanceof ITextEditor)) {
/* 100 */                           ITextEditor textEditor = (ITextEditor)editor;
/* 101 */                           IDocument document = textEditor.getDocumentProvider().getDocument(editor.getEditorInput());
/* 102 */                           if (document != null) {
/* 103 */                             IRegion regionInfo = document.getLineInformation(lineNumber - 1);
/* 104 */                             textEditor.selectAndReveal(regionInfo.getOffset(), regionInfo.getLength());
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                       catch (Exception e1) {
/* 109 */                         e1.printStackTrace();
/*     */                       }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private CLIInfoLineInfo getAddressInfo(long address)
/*     */   {
/* 128 */     SWVClient client = this.view.getCurrentClient();
/* 129 */     if (client != null) {
/* 130 */       DsfSession session = client.getSession();
/* 131 */       if ((session != null) && 
/* 132 */         (client.sessionSuspended())) {
/* 133 */         CLIInfoLineInfo lineInfo = MemoryAccess.getLineForAddress(address, session);
/* 134 */         return lineInfo;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   private IProject getProject()
/*     */   {
/* 148 */     ILaunchConfigurationWorkingCopy configuration = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration();
/*     */ 
/* 150 */     if (configuration != null)
/*     */     {
/* 152 */       String projectName = null;
/*     */       try {
/* 154 */         projectName = configuration.getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", "");
/* 155 */         if ((projectName != null) && (!projectName.isEmpty())) {
/* 156 */           IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
/* 157 */           if (project.exists())
/* 158 */             return project;
/*     */         }
/*     */       }
/*     */       catch (CoreException e)
/*     */       {
/* 163 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.HistoryTableListener
 * JD-Core Version:    0.6.2
 */