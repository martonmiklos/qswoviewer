/*    */ package com.atollic.truestudio.swv.core.ui;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import com.atollic.truestudio.swv.core.SessionManager;
/*    */ import org.eclipse.core.expressions.PropertyTester;
/*    */ 
/*    */ public class StartTraceTester extends PropertyTester
/*    */ {
/*    */   public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
/*    */   {
/* 16 */     if ("isActive".equals(property)) {
/* 17 */       return !SWVPlugin.getDefault().getSessionManager().isTracing();
/*    */     }
/*    */ 
/* 21 */     if ("isEnabled".equals(property)) {
/* 22 */       return SWVPlugin.getDefault().getSessionManager().couldTrace();
/*    */     }
/*    */ 
/* 25 */     return false;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.StartTraceTester
 * JD-Core Version:    0.6.2
 */