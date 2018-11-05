/*    */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ 
/*    */ public class SWVStatisticalProfilingViewContentProvider
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
/* 20 */     if ((inputElement instanceof List))
/*    */     {
/* 22 */       List items = (List)inputElement;
/* 23 */       return items.toArray();
/*    */     }
/*    */ 
/* 26 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalProfilingViewContentProvider
 * JD-Core Version:    0.6.2
 */