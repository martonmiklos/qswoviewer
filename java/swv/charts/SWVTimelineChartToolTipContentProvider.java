/*    */ package com.atollic.truestudio.swv.charts;
/*    */ 
/*    */ import com.atollic.truestudio.swv.model.Event;
/*    */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ 
/*    */ public class SWVTimelineChartToolTipContentProvider
/*    */   implements IStructuredContentProvider
/*    */ {
/*    */   public void dispose()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object[] getElements(Object inputElement)
/*    */   {
/* 25 */     if ((inputElement instanceof Event[])) {
/* 26 */       return (Object[])inputElement;
/*    */     }
/* 28 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVTimelineChartToolTipContentProvider
 * JD-Core Version:    0.6.2
 */