/*     */ package com.atollic.truestudio.swv.core.ui.exception;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*     */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*     */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ 
/*     */ public class SWVExceptionStatisticViewLabelProvider
/*     */   implements ITableLabelProvider, SWVExceptionStatisticsViewLabelProviderInterface
/*     */ {
/*     */   private static final String DOT_DOT_DOT = "...";
/*     */   private static final String ZERO = "0";
/*     */   private static final String EMPTY_STRING = "";
/*  17 */   private final boolean prm = ProductManager.hasPermission(TSFeature.SWV_EXCEPTION_TRACE_LOG);
/*     */ 
/*     */   public void addListener(ILabelProviderListener listener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isLabelProperty(Object element, String property)
/*     */   {
/*  34 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeListener(ILabelProviderListener listener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Image getColumnImage(Object element, int columnIndex)
/*     */   {
/*  46 */     return null;
/*     */   }
/*     */ 
/*     */   public String getColumnText(Object infoElement, int columnIndex)
/*     */   {
/*  51 */     if (infoElement == null)
/*  52 */       return null;
/*  53 */     if (((infoElement instanceof InterruptInfo)) && (((InterruptInfo)infoElement).getId() != 0)) {
/*  54 */       InterruptInfo ii = (InterruptInfo)infoElement;
/*  55 */       if ((ii.isAccessed()) && (!ii.getName().isEmpty())) {
/*  56 */         if (ii.isTotalInfoObject())
/*     */         {
/*  58 */           if (columnIndex == 0)
/*  59 */             return ii.getName();
/*  60 */           if (columnIndex == 3)
/*  61 */             return Long.toString(ii.getNumberOf());
/*  62 */           if (columnIndex == 6)
/*  63 */             return Long.toString(ii.getTotalRun());
/*  64 */           if (columnIndex == 7) {
/*  65 */             if (ii.getNumberOfTotalyCorrect() > 0L) {
/*  66 */               return ii.getTotalRun() / ii.getNumberOfTotalyCorrect();
/*     */             }
/*  68 */             return "";
/*     */           }
/*     */         }
/*     */         else {
/*  72 */           if (this.prm) {
/*  73 */             if (columnIndex == 0)
/*  74 */               return ii.getName();
/*  75 */             if (columnIndex == 1) {
/*  76 */               String func = ii.getFunction();
/*  77 */               if ((func == null) || (func.isEmpty())) return "";
/*  78 */               return func + "()";
/*     */             }
/*     */ 
/*     */           }
/*  82 */           else if ((columnIndex == 0) || (columnIndex == 1)) {
/*  83 */             return "...";
/*     */           }
/*     */ 
/*  87 */           if (columnIndex == 3)
/*  88 */             return Long.toString(ii.getNumberOf());
/*  89 */           if (columnIndex == 6)
/*  90 */             return Long.toString(ii.getTotalRun());
/*  91 */           if (columnIndex == 7) {
/*  92 */             if (ii.getNumberOfTotalyCorrect() > 0L) {
/*  93 */               return ii.getTotalRun() / ii.getNumberOfTotalyCorrect();
/*     */             }
/*  95 */             return "0";
/*     */           }
/*  97 */           if (columnIndex == 8)
/*  98 */             return Long.toString(ii.getMinRun());
/*  99 */           if (columnIndex == 9)
/* 100 */             return Long.toString(ii.getMaxRun());
/* 101 */           if (columnIndex == 10)
/* 102 */             return Long.toString(ii.getFirstCycles());
/* 103 */           if (columnIndex == 11)
/* 104 */             return ii.getFirstTime();
/* 105 */           if (columnIndex == 12)
/* 106 */             return Long.toString(ii.getLatestCycles());
/* 107 */           if (columnIndex == 13) {
/* 108 */             return ii.getLatestTime();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 114 */     return null;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionStatisticViewLabelProvider
 * JD-Core Version:    0.6.2
 */