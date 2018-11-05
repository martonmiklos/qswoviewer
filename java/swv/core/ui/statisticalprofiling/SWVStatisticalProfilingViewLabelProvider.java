/*    */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*    */ 
/*    */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*    */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*    */ import java.text.DecimalFormat;
/*    */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*    */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class SWVStatisticalProfilingViewLabelProvider
/*    */   implements ITableLabelProvider, SWVStatisticalProfilingViewLabelProviderInterface
/*    */ {
/* 14 */   private final boolean hp = ProductManager.hasPermission(TSFeature.SWV_STATISTICAL_PROFILING);
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
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   public void removeListener(ILabelProviderListener listener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Image getColumnImage(Object element, int columnIndex)
/*    */   {
/* 43 */     return null;
/*    */   }
/*    */ 
/*    */   public String getColumnText(Object element, int columnIndex)
/*    */   {
/* 49 */     if ((element instanceof SWVStatisticalProfilingItem))
/*    */     {
/* 51 */       if (columnIndex == 0) {
/* 52 */         if (this.hp) {
/* 53 */           return ((SWVStatisticalProfilingItem)element).getFunction();
/*    */         }
/* 55 */         return Messages.SWVStatisticalProfilingView_DEMO_MODE;
/*    */       }
/* 57 */       if (columnIndex == 3) {
/* 58 */         if (this.hp) {
/* 59 */           return "0x" + Long.toHexString(((SWVStatisticalProfilingItem)element).getStartAddress());
/*    */         }
/* 61 */         return Messages.SWVStatisticalProfilingView_DEMO_MODE;
/*    */       }
/* 63 */       if (columnIndex == 4)
/* 64 */         return "0x" + Long.toHexString(((SWVStatisticalProfilingItem)element).getEndAddress() - ((SWVStatisticalProfilingItem)element).getStartAddress());
/* 65 */       if (columnIndex == 2)
/* 66 */         return ((SWVStatisticalProfilingItem)element).getOccurrences();
/* 67 */       if (columnIndex == 1) {
/* 68 */         float f = ((SWVStatisticalProfilingItem)element).getPercentInUse();
/* 69 */         DecimalFormat dFormat = new DecimalFormat("#.##");
/* 70 */         dFormat.setMinimumFractionDigits(2);
/* 71 */         return dFormat.format(f) + "%";
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 76 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalProfilingViewLabelProvider
 * JD-Core Version:    0.6.2
 */