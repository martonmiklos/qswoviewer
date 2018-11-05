/*    */ package com.atollic.truestudio.swv.core.ui;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.model.DWTDataTracePCValueEvent;
/*    */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*    */ import com.atollic.truestudio.swv.model.DWTPCSampleEvent;
/*    */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.jface.viewers.IStructuredSelection;
/*    */ import org.eclipse.jface.viewers.TableViewer;
/*    */ import org.eclipse.swt.events.MouseAdapter;
/*    */ import org.eclipse.swt.events.MouseEvent;
/*    */ 
/*    */ public class SWVLogViewListener extends MouseAdapter
/*    */ {
/*    */   private SWVLogView view;
/*    */ 
/*    */   public SWVLogViewListener(SWVLogView view)
/*    */   {
/* 26 */     this.view = view;
/*    */   }
/*    */ 
/*    */   public void mouseDoubleClick(MouseEvent e)
/*    */   {
/* 35 */     SWVClient client = this.view.getCurrentClient();
/* 36 */     if ((client != null) && (client.sessionSuspended()))
/*    */     {
/* 38 */       ISelection selection = this.view.getViewer().getSelection();
/* 39 */       if ((selection instanceof IStructuredSelection)) {
/* 40 */         IStructuredSelection sselection = (IStructuredSelection)selection;
/* 41 */         Object firstElement = sselection.getFirstElement();
/*    */ 
/* 43 */         if ((firstElement instanceof DWTDataTracePCValueEvent)) {
/* 44 */           DWTDataTracePCValueEvent element = (DWTDataTracePCValueEvent)firstElement;
/* 45 */           long pcAddress = element.getPcAddress();
/* 46 */           if ((pcAddress != 0L) && (pcAddress != -1L))
/* 47 */             SWVUtil.jumpToLine(pcAddress);
/*    */         }
/* 49 */         else if ((firstElement instanceof DWTExceptionEvent)) {
/* 50 */           DWTExceptionEvent exceptionEvent = (DWTExceptionEvent)firstElement;
/* 51 */           long address = Long.valueOf(exceptionEvent.printFunctionAddress(), 16).longValue();
/*    */ 
/* 53 */           if (-1L != address)
/* 54 */             SWVUtil.jumpToLine(address);
/*    */         }
/* 56 */         else if ((firstElement instanceof InterruptInfo)) {
/* 57 */           InterruptInfo interuptInfo = (InterruptInfo)firstElement;
/* 58 */           String functionAddress = interuptInfo.getFunctionAddress();
/* 59 */           if ((functionAddress != null) && (!functionAddress.isEmpty())) {
/* 60 */             long address = Long.valueOf(functionAddress, 16).longValue();
/*    */ 
/* 62 */             if (-1L != address)
/* 63 */               SWVUtil.jumpToLine(address);
/*    */           }
/*    */         }
/* 66 */         else if ((firstElement instanceof DWTPCSampleEvent)) {
/* 67 */           DWTPCSampleEvent element = (DWTPCSampleEvent)firstElement;
/* 68 */           long pcAddress = element.getPcAddress();
/* 69 */           if ((pcAddress != 0L) && (pcAddress != -1L))
/* 70 */             SWVUtil.jumpToLine(pcAddress);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.SWVLogViewListener
 * JD-Core Version:    0.6.2
 */