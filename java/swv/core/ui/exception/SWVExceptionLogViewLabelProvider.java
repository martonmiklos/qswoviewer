/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*    */ import com.atollic.truestudio.swv.model.Event;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVExceptionLogViewLabelProvider
/*    */   implements ITableLabelProvider
/*    */ {
/*    */   private static final String DOT_DOT_DOT = "...";
/*    */   private static final int COL_INDEX_INDEX = 0;
/*    */   private static final int COL_INDEX_TYPE = 1;
/*    */   private static final int COL_INDEX_NAME = 2;
/*    */   private static final int COL_INDEX_PERIPHERAL = 3;
/*    */   private static final int COL_INDEX_FUNC = 4;
/*    */   private static final int COL_INDEX_CYCLES = 5;
/*    */   private static final int COL_INDEX_TIME = 6;
/*    */   private static final int COL_INDEX_EXTRA_INFO = 7;
/* 24 */   private final boolean prm = ProductManager.hasPermission(TSFeature.SWV_EXCEPTION_TRACE_LOG);
/*    */ 
/*    */   public void addListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void dispose()
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean isLabelProperty(Object element, String property)
/*    */   {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 59 */     if ((element instanceof Event))
/*    */     {
/* 61 */       if (columnIndex == 0)
/* 62 */         return Long.toString(((Event)element).getEventID());
/* 63 */       if (columnIndex == 1) {
/* 64 */         return ((Event)element).printType();
/*    */       }
/* 66 */       if (this.prm) {
/* 67 */         if (columnIndex == 2)
/* 68 */           return ((Event)element).printData();
/* 69 */         if (columnIndex == 5)
/* 70 */           return ((Event)element).printCycles();
/* 71 */         if (columnIndex == 6)
/* 72 */           return ((Event)element).printTime();
/* 73 */         if (columnIndex == 7)
/* 74 */           return ((Event)element).printExtraInfo();
/* 75 */         if ((element instanceof DWTExceptionEvent)) {
/* 76 */           if (columnIndex == 3)
/* 77 */             return ((DWTExceptionEvent)element).printPeripheral();
/* 78 */           if (columnIndex == 4)
/* 79 */             return ((DWTExceptionEvent)element).printFunc();
/*    */         }
/*    */       }
/*    */       else {
/* 83 */         return "...";
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 88 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionLogViewLabelProvider
 * JD-Core Version:    0.6.2
 */