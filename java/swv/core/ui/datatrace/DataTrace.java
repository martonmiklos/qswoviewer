/*     */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTraceDataValueEvent;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class DataTrace
/*     */ {
/*     */   private byte comparatorId;
/*     */   private String name;
/*     */   private ArrayList<Data> dataValues;
/*  26 */   private long lastPC = -1L;
/*     */   private SWVComparatorConfig config;
/*     */   private int SWVCoreClock;
/*     */ 
/*     */   public DataTrace(byte comparatorId)
/*     */   {
/*  31 */     this.comparatorId = comparatorId;
/*  32 */     this.dataValues = new ArrayList();
/*     */   }
/*     */ 
/*     */   public byte getComparatorId()
/*     */   {
/*  40 */     return this.comparatorId;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  44 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/*  48 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public Data getFirstData()
/*     */   {
/*  56 */     if (this.dataValues.size() > 0) {
/*  57 */       return (Data)this.dataValues.get(0);
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   public Data getLastData()
/*     */   {
/*  68 */     int recCtr = this.dataValues.size();
/*  69 */     if (recCtr > 0) {
/*  70 */       return (Data)this.dataValues.get(recCtr - 1);
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   public void addDataValue(long dataValue, byte access, long cycles)
/*     */   {
/*  84 */     this.dataValues.add(new Data(dataValue, this.lastPC, access, cycles, this));
/*  85 */     this.lastPC = -1L;
/*     */   }
/*     */ 
/*     */   public void addNewFirstData()
/*     */   {
/*  92 */     this.dataValues.add(0, new Data(0L, this.lastPC, DWTDataTraceDataValueEvent.READ_ACCESS, 0L, this));
/*  93 */     this.lastPC = -1L;
/*     */   }
/*     */ 
/*     */   public void addPC(long pc)
/*     */   {
/* 102 */     this.lastPC = pc;
/*     */   }
/*     */ 
/*     */   public ArrayList<Data> getHistory()
/*     */   {
/* 110 */     return this.dataValues;
/*     */   }
/*     */ 
/*     */   public int findDataId(long cycleTime)
/*     */   {
/* 146 */     int low = 0;
/* 147 */     int high = this.dataValues.size() - 1;
/* 148 */     int mid = -1;
/*     */ 
/* 150 */     while (low <= high) {
/* 151 */       mid = (low + high) / 2;
/* 152 */       long cycl = ((Data)this.dataValues.get(mid)).getCycles();
/* 153 */       if (cycl - cycleTime < 0L)
/* 154 */         low = mid + 1;
/* 155 */       else if (cycl - cycleTime > 0L)
/* 156 */         high = mid - 1;
/*     */       else {
/* 158 */         return mid;
/*     */       }
/*     */     }
/* 161 */     return high;
/*     */   }
/*     */ 
/*     */   public void clearHistory()
/*     */   {
/* 169 */     this.dataValues = new ArrayList();
/* 170 */     this.lastPC = -1L;
/*     */   }
/*     */ 
/*     */   public void setComparatorConfig(SWVComparatorConfig config) {
/* 174 */     this.config = config;
/*     */   }
/*     */ 
/*     */   public SWVComparatorConfig getComparatorConfig() {
/* 178 */     return this.config;
/*     */   }
/*     */ 
/*     */   public int getSWVCoreClock()
/*     */   {
/* 186 */     return this.SWVCoreClock;
/*     */   }
/*     */ 
/*     */   public void setSWVCoreClock(int coreClock)
/*     */   {
/* 194 */     this.SWVCoreClock = coreClock;
/*     */   }
/*     */ 
/*     */   public double getXMin(int swvScaleType)
/*     */   {
/* 203 */     double x_max = getFirstData().getCycles();
/* 204 */     if (swvScaleType == 0) {
/* 205 */       return x_max;
/*     */     }
/* 207 */     return x_max / getSWVCoreClock();
/*     */   }
/*     */ 
/*     */   public double getXMax(int swvScaleType)
/*     */   {
/* 217 */     double x_max = getLastData().getCycles();
/* 218 */     if (swvScaleType == 0) {
/* 219 */       return x_max;
/*     */     }
/* 221 */     return x_max / getSWVCoreClock();
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.DataTrace
 * JD-Core Version:    0.6.2
 */