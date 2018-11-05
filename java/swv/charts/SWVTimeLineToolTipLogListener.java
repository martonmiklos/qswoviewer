/*    */ package com.atollic.truestudio.swv.charts;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import com.atollic.truestudio.swv.core.SessionManager;
/*    */ import com.atollic.truestudio.swv.core.ui.SWVUtil;
/*    */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.jface.viewers.IStructuredSelection;
/*    */ import org.eclipse.jface.viewers.TableViewer;
/*    */ import org.eclipse.swt.events.MouseAdapter;
/*    */ import org.eclipse.swt.events.MouseEvent;
/*    */ 
/*    */ public class SWVTimeLineToolTipLogListener extends MouseAdapter
/*    */ {
/*    */   private TableViewer tableViewer;
/*    */ 
/*    */   public SWVTimeLineToolTipLogListener(TableViewer tableViewer)
/*    */   {
/* 25 */     this.tableViewer = tableViewer;
/*    */   }
/*    */ 
/*    */   public void mouseDoubleClick(MouseEvent e)
/*    */   {
/* 34 */     SWVClient client = SWVPlugin.getDefault().getSessionManager().getClient();
/* 35 */     if ((client != null) && (client.sessionSuspended()))
/*    */     {
/* 37 */       ISelection selection = this.tableViewer.getSelection();
/* 38 */       if ((selection instanceof IStructuredSelection)) {
/* 39 */         IStructuredSelection sselection = (IStructuredSelection)selection;
/* 40 */         Object firstElement = sselection.getFirstElement();
/*    */ 
/* 42 */         if ((firstElement instanceof DWTExceptionEvent)) {
/* 43 */           DWTExceptionEvent exceptionEvent = (DWTExceptionEvent)firstElement;
/* 44 */           long address = Long.valueOf(exceptionEvent.printFunctionAddress(), 16).longValue();
/*    */ 
/* 47 */           if (address > 0L)
/* 48 */             SWVUtil.jumpToLine(address);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVTimeLineToolTipLogListener
 * JD-Core Version:    0.6.2
 */