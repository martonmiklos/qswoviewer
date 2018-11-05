/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class DWTCounterEvent extends Event
/*    */ {
/* 17 */   public static byte CTR_CPI = 1;
/* 18 */   public static byte CTR_EXC = 2;
/* 19 */   public static byte CTR_SLEEP = 4;
/* 20 */   public static byte CTR_LSU = 8;
/* 21 */   public static byte CTR_FOLD = 16;
/* 22 */   public static byte CTR_CYC = 32;
/*    */   private byte counters;
/*    */ 
/*    */   public DWTCounterEvent(byte counters)
/*    */   {
/* 27 */     this.counters = counters;
/*    */   }
/*    */ 
/*    */   public byte getCounters()
/*    */   {
/* 34 */     return this.counters;
/*    */   }
/*    */ 
/*    */   public void setCounters(byte counters)
/*    */   {
/* 41 */     this.counters = counters;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 46 */     String ctr = "";
/*    */ 
/* 48 */     if (this.counters == CTR_CPI)
/* 49 */       ctr = Messages.DWTCounterEvent_CPI;
/* 50 */     else if (this.counters == CTR_EXC)
/* 51 */       ctr = Messages.DWTCounterEvent_EXC;
/* 52 */     else if (this.counters == CTR_SLEEP)
/* 53 */       ctr = Messages.DWTCounterEvent_SLEEP;
/* 54 */     else if (this.counters == CTR_LSU)
/* 55 */       ctr = Messages.DWTCounterEvent_LSU;
/* 56 */     else if (this.counters == CTR_FOLD)
/* 57 */       ctr = Messages.DWTCounterEvent_FOLD;
/* 58 */     else if (this.counters == CTR_CYC)
/* 59 */       ctr = Messages.DWTCounterEvent_CYC;
/*    */     else {
/* 61 */       ctr = Messages.MODEL_UNKNOWN;
/*    */     }
/* 63 */     return Messages.DWTCounterEvent_COUNTER_OVERFLOW + " (" + ctr + ")";
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTCounterEvent
 * JD-Core Version:    0.6.2
 */