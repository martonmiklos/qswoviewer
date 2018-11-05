/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class ITMGlobalTimestampEvent extends Event
/*    */ {
/*    */   private long timestamp;
/*    */   private byte clkch;
/*    */   private byte wrap;
/*    */   private byte packageFormat;
/*    */ 
/*    */   public ITMGlobalTimestampEvent(long timestamp, byte clkch, byte wrap, byte packageFormat)
/*    */   {
/* 27 */     this.timestamp = timestamp;
/* 28 */     this.clkch = clkch;
/* 29 */     this.wrap = wrap;
/* 30 */     this.packageFormat = packageFormat;
/*    */   }
/*    */ 
/*    */   public long getTimestamp()
/*    */   {
/* 37 */     return this.timestamp;
/*    */   }
/*    */ 
/*    */   public byte getClkch()
/*    */   {
/* 44 */     return this.clkch;
/*    */   }
/*    */ 
/*    */   public byte getWrap()
/*    */   {
/* 51 */     return this.wrap;
/*    */   }
/*    */ 
/*    */   public byte getPackageFormat()
/*    */   {
/* 61 */     return this.packageFormat;
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 66 */     return "0x" + Long.toString(getTimestamp(), 16);
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 71 */     return Messages.ITMGlobalTimestampEvent_GLOBAL_TIMESTAMP_BITS + " " + (getPackageFormat() == 1 ? "[25:0]" : "[47:26]");
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMGlobalTimestampEvent
 * JD-Core Version:    0.6.2
 */