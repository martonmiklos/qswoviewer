/*    */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVDataTraceLabelProvider
/*    */   implements ITableLabelProvider
/*    */ {
/*    */   private static final String EMPTY_STRING = "";
/*    */   private static final int COLUMN_COMPARATOR = 0;
/*    */   private static final int COLUMN_VALUE = 2;
/*    */   private static final int COLUMN_NAME = 1;
/* 29 */   private final boolean perm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/*    */   private SWVDataTraceView view;
/*    */ 
/*    */   public SWVDataTraceLabelProvider(SWVDataTraceView view)
/*    */   {
/* 34 */     this.view = view;
/*    */   }
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
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 69 */     if ((element instanceof DataTrace)) {
/* 70 */       DataTrace dataTrace = (DataTrace)element;
/*    */ 
/* 72 */       if (columnIndex == 0)
/* 73 */         return Byte.toString(dataTrace.getComparatorId());
/* 74 */       if (2 == columnIndex) {
/* 75 */         if (!this.perm) {
/* 76 */           return "...";
/*    */         }
/*    */ 
/* 79 */         Data lastData = dataTrace.getLastData();
/* 80 */         return SWVDataTraceUtil.convert(lastData.getDataValue(), lastData.getType(), this.view.getValueFormat());
/* 81 */       }if (1 == columnIndex) {
/* 82 */         SWVComparatorConfig cmpConfig = dataTrace.getComparatorConfig();
/* 83 */         return cmpConfig == null ? "" : dataTrace.getComparatorConfig().getSymbol();
/*    */       }
/*    */     }
/*    */ 
/* 87 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceLabelProvider
 * JD-Core Version:    0.6.2
 */