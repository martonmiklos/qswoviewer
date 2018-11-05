/*    */ package com.atollic.truestudio.swv.core.ui;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import com.atollic.truestudio.swv.core.SessionManager;
/*    */ import com.atollic.truestudio.swv.core.ui.config.SWVConfigDialog;
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class OpenSWVConfig extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 29 */     SessionManager manager = SWVPlugin.getDefault().getSessionManager();
/*    */ 
/* 31 */     ILaunchConfigurationWorkingCopy configuration = manager.getActiveConfiguration();
/* 32 */     SWVClient client = manager.getClient();
/*    */ 
/* 34 */     if ((configuration != null) && (client != null)) {
/* 35 */       SWVConfigDialog.open(PlatformUI.getWorkbench().getDisplay().getActiveShell(), client, configuration);
/*    */     }
/* 37 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.OpenSWVConfig
 * JD-Core Version:    0.6.2
 */