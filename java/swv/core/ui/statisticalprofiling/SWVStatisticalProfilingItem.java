/*     */ package com.atollic.truestudio.swv.core.ui.statisticalprofiling;
/*     */ 
/*     */ public class SWVStatisticalProfilingItem
/*     */   implements Comparable<Object>
/*     */ {
/*     */   private String function;
/*     */   private long startAddress;
/*     */   private long endAddress;
/*     */   private long occurrences;
/*     */   private float percentInUse;
/*     */ 
/*     */   public SWVStatisticalProfilingItem(String functionName, long startAddress, long endAddress, long occurrences, float percentInUse)
/*     */   {
/*  24 */     this.function = functionName;
/*  25 */     this.startAddress = startAddress;
/*  26 */     this.endAddress = endAddress;
/*  27 */     this.occurrences = occurrences;
/*  28 */     this.percentInUse = percentInUse;
/*     */   }
/*     */ 
/*     */   public String getFunction()
/*     */   {
/*  35 */     return this.function;
/*     */   }
/*     */ 
/*     */   public void setFunction(String function)
/*     */   {
/*  42 */     this.function = function;
/*     */   }
/*     */ 
/*     */   public long getStartAddress()
/*     */   {
/*  49 */     return this.startAddress;
/*     */   }
/*     */ 
/*     */   public void setStartAddress(long startAddress)
/*     */   {
/*  56 */     this.startAddress = startAddress;
/*     */   }
/*     */ 
/*     */   public long getEndAddress()
/*     */   {
/*  63 */     return this.endAddress;
/*     */   }
/*     */ 
/*     */   public void setEndAddress(long endAddress)
/*     */   {
/*  70 */     this.endAddress = endAddress;
/*     */   }
/*     */ 
/*     */   public long getOccurrences()
/*     */   {
/*  77 */     return this.occurrences;
/*     */   }
/*     */ 
/*     */   public void setOccurrences(long occurrences)
/*     */   {
/*  84 */     this.occurrences = occurrences;
/*     */   }
/*     */ 
/*     */   public float getPercentInUse()
/*     */   {
/*  91 */     return this.percentInUse;
/*     */   }
/*     */ 
/*     */   public void setPercentInUse(float percentInUse)
/*     */   {
/*  98 */     this.percentInUse = percentInUse;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object arg0)
/*     */   {
/* 103 */     if ((arg0 instanceof SWVStatisticalProfilingItem)) {
/* 104 */       if (((SWVStatisticalProfilingItem)arg0).getPercentInUse() < getPercentInUse())
/* 105 */         return -1;
/* 106 */       if (((SWVStatisticalProfilingItem)arg0).getPercentInUse() > getPercentInUse())
/* 107 */         return 1;
/*     */     }
/* 109 */     return 0;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.statisticalprofiling.SWVStatisticalProfilingItem
 * JD-Core Version:    0.6.2
 */