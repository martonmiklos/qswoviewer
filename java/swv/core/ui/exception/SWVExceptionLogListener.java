/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.ui.SWVUtil;
/*    */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*    */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.jface.viewers.IStructuredSelection;
/*    */ import org.eclipse.jface.viewers.TableViewer;
/*    */ import org.eclipse.swt.events.MouseAdapter;
/*    */ import org.eclipse.swt.events.MouseEvent;
/*    */ 
/*    */ public class SWVExceptionLogListener extends MouseAdapter
/*    */ {
/*    */   private SWVExceptionLogView view;
/*    */ 
/*    */   public SWVExceptionLogListener(SWVExceptionLogView view)
/*    */   {
/* 24 */     this.view = view;
/*    */   }
/*    */ 
/*    */   public void mouseDoubleClick(MouseEvent e)
/*    */   {
/* 33 */     SWVClient client = this.view.getCurrentClient();
/* 34 */     if ((client != null) && (client.sessionSuspended()))
/*    */     {
/* 36 */       ISelection selection = this.view.getViewer().getSelection();
/* 37 */       if ((selection instanceof IStructuredSelection)) {
/* 38 */         IStructuredSelection sselection = (IStructuredSelection)selection;
/* 39 */         Object firstElement = sselection.getFirstElement();
/*    */ 
/* 41 */         if ((firstElement instanceof DWTExceptionEvent)) {
/* 42 */           DWTExceptionEvent exceptionEvent = (DWTExceptionEvent)firstElement;
/* 43 */           long address = Long.valueOf(exceptionEvent.printFunctionAddress(), 16).longValue();
/*    */ 
/* 46 */           if (-1L != address)
/* 47 */             SWVUtil.jumpToLine(address);
/*    */         }
/* 49 */         else if ((firstElement instanceof InterruptInfo)) {
/* 50 */           InterruptInfo interuptInfo = (InterruptInfo)firstElement;
/* 51 */           String functionAddress = interuptInfo.getFunctionAddress();
/* 52 */           if ((functionAddress != null) && (!functionAddress.isEmpty())) {
/* 53 */             long address = Long.valueOf(functionAddress, 16).longValue();
/*    */ 
/* 55 */             if (-1L != address)
/* 56 */               SWVUtil.jumpToLine(address);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionLogListener
 * JD-Core Version:    0.6.2
 */