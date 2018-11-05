/*    */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.ui.SWVUtil;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.jface.viewers.IStructuredSelection;
/*    */ import org.eclipse.jface.viewers.TableViewer;
/*    */ import org.eclipse.swt.events.MouseAdapter;
/*    */ import org.eclipse.swt.events.MouseEvent;
/*    */ 
/*    */ public class SWVStatisticalLogListener extends MouseAdapter
/*    */ {
/*    */   private SWVStatisticalProfilingView view;
/*    */ 
/*    */   public SWVStatisticalLogListener(SWVStatisticalProfilingView view)
/*    */   {
/* 22 */     this.view = view;
/*    */   }
/*    */ 
/*    */   public void mouseDoubleClick(MouseEvent e)
/*    */   {
/* 31 */     SWVClient client = this.view.getCurrentClient();
/* 32 */     if ((client != null) && (client.sessionSuspended()))
/*    */     {
/* 34 */       ISelection selection = this.view.getViewer().getSelection();
/* 35 */       if ((selection instanceof IStructuredSelection)) {
/* 36 */         IStructuredSelection sselection = (IStructuredSelection)selection;
/* 37 */         Object firstElement = sselection.getFirstElement();
/*    */ 
/* 39 */         if ((firstElement instanceof SWVStatisticalProfilingItem)) {
/* 40 */           SWVStatisticalProfilingItem exceptionEvent = (SWVStatisticalProfilingItem)firstElement;
/* 41 */           long address = exceptionEvent.getStartAddress();
/*    */ 
/* 44 */           if (-1L != address)
/* 45 */             SWVUtil.jumpToLine(address);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalLogListener
 * JD-Core Version:    0.6.2
 */