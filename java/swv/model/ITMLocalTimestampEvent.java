/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class ITMLocalTimestampEvent extends Event
/*    */ {
/* 15 */   public static byte TYPE_MASK = 3;
/* 16 */   public static byte VALUE_SYNC = 0;
/* 17 */   public static byte VALUE_DELAYED_REL_ITM_DWT = 1;
/* 18 */   public static byte VALUE_DELAYED_REL_ASS_EV = 2;
/* 19 */   public static byte VALUE_DELAYED_REL_ASS_EV_ITM_DWT = 3;
/*    */   private byte timeControl;
/*    */   private long timeStamp;
/*    */ 
/*    */   public ITMLocalTimestampEvent(byte timeControl, long timeStamp)
/*    */   {
/* 25 */     this.timeControl = timeControl;
/* 26 */     this.timeStamp = timeStamp;
/*    */   }
/*    */ 
/*    */   public byte getTimeControl()
/*    */   {
/* 33 */     return this.timeControl;
/*    */   }
/*    */ 
/*    */   public long getTimeStamp()
/*    */   {
/* 40 */     return this.timeStamp;
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 45 */     return "0x" + Long.toString(getTimeStamp(), 16);
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMLocalTimestampEvent
 * JD-Core Version:    0.6.2
 */