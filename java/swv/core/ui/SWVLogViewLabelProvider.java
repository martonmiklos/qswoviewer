/*    */ package com.atollic.truestudio.swv.core.ui;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import com.atollic.truestudio.swv.model.Event;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVLogViewLabelProvider
/*    */   implements ITableLabelProvider
/*    */ {
/*    */   private static final String DOT_DOT_DOT = "...";
/*    */   private static final int COL_INDEX_INDEX = 0;
/*    */   private static final int COL_INDEX_TYPE = 1;
/*    */   private static final int COL_INDEX_DATA = 2;
/*    */   private static final int COL_INDEX_CYCLES = 3;
/*    */   private static final int COL_INDEX_TIME = 4;
/*    */   private static final int COL_INDEX_EXTRA_INFO = 5;
/* 21 */   private final boolean hp = ProductManager.hasPermission(TSFeature.SWV_TRACE_LOG);
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
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 56 */     if ((element instanceof Event))
/*    */     {
/* 58 */       if (columnIndex == 0) {
/* 59 */         return Long.toString(((Event)element).getEventID());
/*    */       }
/* 61 */       if (columnIndex == 1) {
/* 62 */         return ((Event)element).printType();
/*    */       }
/* 64 */       if (!this.hp)
/* 65 */         return "...";
/* 66 */       if (columnIndex == 2)
/* 67 */         return ((Event)element).printData();
/* 68 */       if (columnIndex == 3)
/* 69 */         return ((Event)element).printCycles();
/* 70 */       if (columnIndex == 4)
/* 71 */         return ((Event)element).printTime();
/* 72 */       if (columnIndex == 5) {
/* 73 */         return ((Event)element).printExtraInfo();
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 78 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.SWVLogViewLabelProvider
 * JD-Core Version:    0.6.2
 */