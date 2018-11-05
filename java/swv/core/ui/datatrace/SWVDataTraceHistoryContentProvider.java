/*    */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ 
/*    */ public class SWVDataTraceHistoryContentProvider
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
/* 36 */     if ((inputElement instanceof DataTrace)) {
/* 37 */       DataTrace traceInfo = (DataTrace)inputElement;
/* 38 */       ArrayList dataVals = traceInfo.getHistory();
/* 39 */       if (dataVals != null) {
/* 40 */         Object[] test = dataVals.toArray();
/* 41 */         return test;
/*    */       }
/*    */     }
/* 44 */     return new Object[0];
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceHistoryContentProvider
 * JD-Core Version:    0.6.2
 */