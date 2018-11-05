/*    */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ 
/*    */ public class SWVDataTraceContentProvider
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
/* 35 */     if ((inputElement instanceof HashMap)) {
/* 36 */       HashMap comparators = (HashMap)inputElement;
/* 37 */       return comparators.values().toArray();
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceContentProvider
 * JD-Core Version:    0.6.2
 */