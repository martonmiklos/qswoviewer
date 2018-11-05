/*     */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import org.eclipse.jface.viewers.ILabelProviderListener;
/*     */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ 
/*     */ public class SWVDataTraceHistoryLabelProvider
/*     */   implements ITableLabelProvider
/*     */ {
/*     */   private static final String DOT_DOT_DOT = "...";
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final int COLUMN_ACCESS = 0;
/*     */   private static final int COLUMN_VALUE = 1;
/*     */   private static final int COLUMN_PC = 2;
/*     */   private static final int COLUMN_CYCLES = 3;
/*     */   private SWVDataTraceView view;
/*  31 */   private final boolean perm = ProductManager.hasPermission(TSFeature.SWV_DATA_TRACE_LOG);
/*     */ 
/*     */   public SWVDataTraceHistoryLabelProvider(SWVDataTraceView view) {
/*  34 */     this.view = view;
/*     */   }
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
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeListener(ILabelProviderListener listener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Image getColumnImage(Object element, int columnIndex)
/*     */   {
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   public String getColumnText(Object element, int columnIndex)
/*     */   {
/*  69 */     if ((element instanceof Data)) {
/*  70 */       Data record = (Data)element;
/*     */ 
/*  72 */       if (1 == columnIndex) {
/*  73 */         if (this.perm) {
/*  74 */           return SWVDataTraceUtil.convert(record.getDataValue(), record.getType(), this.view.getValueFormat());
/*     */         }
/*  76 */         return "...";
/*     */       }
/*  78 */       if (columnIndex == 0)
/*     */       {
/*  80 */         if (record.getAccess() == 0) {
/*  81 */           return Messages.SWVDataTraceHistoryLabelProvider_READ;
/*     */         }
/*  83 */         return Messages.SWVDataTraceHistoryLabelProvider_WRITE;
/*     */       }
/*     */ 
/*  86 */       if (2 == columnIndex) {
/*  87 */         if (this.perm) {
/*  88 */           long pc = record.getPC();
/*  89 */           if (-1L == pc) {
/*  90 */             return "";
/*     */           }
/*  92 */           return "0x" + Long.toHexString(pc);
/*     */         }
/*     */ 
/*  95 */         return "...";
/*     */       }
/*     */ 
/*  98 */       if (3 == columnIndex)
/*     */       {
/* 100 */         return Long.toString(record.getCycles());
/*     */       }
/*     */     }
/*     */ 
/* 104 */     return null;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceHistoryLabelProvider
 * JD-Core Version:    0.6.2
 */