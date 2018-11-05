/*    */ package com.atollic.truestudio.swv.charts;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*    */ import com.atollic.truestudio.swv.core.ui.datatrace.Data;
/*    */ import com.atollic.truestudio.swv.core.ui.datatrace.DataTrace;
/*    */ import com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceUtil;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVDataTraceChartToolTipLabelProvider
/*    */   implements ITableLabelProvider
/*    */ {
/* 16 */   private static final boolean hasPerm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/*    */   private static final int COL_INDEX_NAME = 0;
/*    */   private static final int COL_INDEX_VALUE = 1;
/*    */   private static final int COL_INDEX_PC = 2;
/*    */   private static final int COL_INDEX_CYCLES = 3;
/*    */   private static final int COL_INDEX_SECONDS = 4;
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
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 57 */     if ((element instanceof Data)) {
/* 58 */       Data data = (Data)element;
/* 59 */       String type = data.getType();
/* 60 */       long value = data.getDataValue();
/* 61 */       if (columnIndex == 0)
/* 62 */         return data.getDataTrace().getComparatorConfig().getSymbol();
/* 63 */       if (columnIndex == 1) {
/* 64 */         if (hasPerm) {
/* 65 */           return SWVDataTraceUtil.convert(value, type, 5);
/*    */         }
/* 67 */         return "...";
/* 68 */       }if (columnIndex == 2) {
/* 69 */         long pc = data.getPC();
/* 70 */         if ((pc <= 0L) || (!hasPerm)) {
/* 71 */           return "";
/*    */         }
/* 73 */         return Long.toString(data.getPC());
/* 74 */       }if (columnIndex == 3)
/* 75 */         return Long.toString(data.getCycles());
/* 76 */       if (columnIndex == 4) {
/* 77 */         Double v = Double.valueOf(data.getCycles() / data.getDataTrace().getSWVCoreClock());
/* 78 */         return v.toString();
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 83 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.charts.SWVDataTraceChartToolTipLabelProvider
 * JD-Core Version:    0.6.2
 */