/*    */ package com.atollic.truestudio.swv.core.ui.datatrace;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*    */ 
/*    */ public class Data
/*    */ {
/*    */   private long dataValue;
/*    */   private long pc;
/*    */   private byte access;
/*    */   private long cycles;
/*    */   private DataTrace dataTrace;
/*    */   private String type;
/*    */ 
/*    */   Data(long dataValue, long pc, byte access, long cycles, DataTrace dataTrace)
/*    */   {
/* 32 */     this.dataValue = dataValue;
/* 33 */     this.pc = pc;
/* 34 */     this.access = access;
/* 35 */     this.cycles = cycles;
/* 36 */     this.dataTrace = dataTrace;
/*    */ 
/* 39 */     if (dataTrace.getComparatorConfig() != null)
/* 40 */       this.type = dataTrace.getComparatorConfig().getType();
/*    */   }
/*    */ 
/*    */   public long getDataValue()
/*    */   {
/* 48 */     return this.dataValue;
/*    */   }
/*    */ 
/*    */   public long getPC()
/*    */   {
/* 56 */     return this.pc;
/*    */   }
/*    */ 
/*    */   public void setPC(long pc)
/*    */   {
/* 64 */     this.pc = pc;
/*    */   }
/*    */ 
/*    */   public byte getAccess()
/*    */   {
/* 72 */     return this.access;
/*    */   }
/*    */ 
/*    */   public long getCycles()
/*    */   {
/* 80 */     return this.cycles;
/*    */   }
/*    */ 
/*    */   public DataTrace getDataTrace() {
/* 84 */     return this.dataTrace;
/*    */   }
/*    */ 
/*    */   public String getType() {
/* 88 */     return this.type;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatrace.Data
 * JD-Core Version:    0.6.2
 */