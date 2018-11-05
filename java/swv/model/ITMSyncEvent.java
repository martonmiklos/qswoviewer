/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class ITMSyncEvent extends Event
/*    */ {
/*    */   private int bitLength;
/*    */ 
/*    */   public ITMSyncEvent(int bitLength)
/*    */   {
/* 17 */     this.bitLength = bitLength;
/*    */   }
/*    */ 
/*    */   public int getBitLength() {
/* 21 */     return this.bitLength;
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 26 */     return getBitLength();
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 31 */     return Messages.ITMSyncEvent_SYNC;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMSyncEvent
 * JD-Core Version:    0.6.2
 */