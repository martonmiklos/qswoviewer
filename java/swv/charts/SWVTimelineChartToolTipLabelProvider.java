/*    */ package com.atollic.truestudio.swv.charts;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import com.atollic.truestudio.swv.model.Event;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVTimelineChartToolTipLabelProvider
/*    */   implements ITableLabelProvider
/*    */ {
/*    */   private static final String DOT_DOT_DOT = "...";
/* 18 */   protected final boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV);
/*    */   private static final int COL_INDEX_INDEX = 0;
/*    */   private static final int COL_INDEX_TYPE = 1;
/*    */   private static final int COL_INDEX_DATA = 2;
/*    */   private static final int COL_INDEX_CYCLES = 3;
/*    */   private static final int COL_INDEX_TIME = 4;
/*    */   private static final int COL_INDEX_EXTRA_INFO = 5;
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
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 60 */     if ((element instanceof Event))
/*    */     {
/* 62 */       if (columnIndex == 0)
/* 63 */         return Long.toString(((Event)element).getEventID());
/* 64 */       if (columnIndex == 1)
/* 65 */         return ((Event)element).printType();
/* 66 */       if (columnIndex == 2) {
/* 67 */         if (this.hasPerm) {
/* 68 */           return ((Event)element).printData();
/*    */         }
/* 70 */         return "0";
/* 71 */       }if (columnIndex == 3)
/* 72 */         return ((Event)element).printCycles();
/* 73 */       if (columnIndex == 4)
/* 74 */         return ((Event)element).printTime();
/* 75 */       if (columnIndex == 5) {
/* 76 */         if (this.hasPerm) {
/* 77 */           return ((Event)element).printExtraInfo();
/*    */         }
/* 79 */         return "...";
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 84 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVTimelineChartToolTipLabelProvider
 * JD-Core Version:    0.6.2
 */