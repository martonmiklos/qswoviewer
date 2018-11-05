/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class DWTPCSampleEvent extends Event
/*    */ {
/*    */   public static final byte SAMPLE_TYPE_FULL = 0;
/*    */   public static final byte SAMPLE_TYPE_SLEEP = 1;
/*    */   private byte type;
/*    */   private long pcAddress;
/*    */ 
/*    */   public DWTPCSampleEvent(byte type, long pcAddress)
/*    */   {
/* 22 */     this.type = type;
/* 23 */     this.pcAddress = pcAddress;
/*    */   }
/*    */ 
/*    */   public byte getType()
/*    */   {
/* 30 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setType(byte type)
/*    */   {
/* 36 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public long getPcAddress()
/*    */   {
/* 42 */     return this.pcAddress;
/*    */   }
/*    */ 
/*    */   public void setPcAddress(long pcAddress)
/*    */   {
/* 48 */     this.pcAddress = pcAddress;
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 53 */     return "0x" + Long.toString(getPcAddress(), 16);
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 58 */     return Messages.DWTPCSampleEvent_PC_SAMPLE;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTPCSampleEvent
 * JD-Core Version:    0.6.2
 */