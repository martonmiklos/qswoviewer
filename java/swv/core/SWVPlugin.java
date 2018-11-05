/*    */ package com.atollic.truestudio.swv.core;
/*    */ 
/*    */ import org.eclipse.core.runtime.ILog;
/*    */ import org.eclipse.core.runtime.IStatus;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
/*    */ 
/*    */ public class SWVPlugin extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "com.atollic.truestudio.swv.core";
/*    */   private static SWVPlugin plugin;
/*    */   private SessionManager fSessionManager;
/*    */ 
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 39 */     super.start(context);
/* 40 */     plugin = this;
/*    */   }
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 49 */     plugin = null;
/* 50 */     super.stop(context);
/*    */   }
/*    */ 
/*    */   public static SWVPlugin getDefault()
/*    */   {
/* 59 */     return plugin;
/*    */   }
/*    */ 
/*    */   public synchronized SessionManager getSessionManager()
/*    */   {
/* 67 */     if (this.fSessionManager == null) {
/* 68 */       this.fSessionManager = new SessionManager();
/*    */     }
/* 70 */     return this.fSessionManager;
/*    */   }
/*    */ 
/*    */   public static void log(IStatus status)
/*    */   {
/* 78 */     plugin.getLog().log(status);
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.SWVPlugin
 * JD-Core Version:    0.6.2
 */