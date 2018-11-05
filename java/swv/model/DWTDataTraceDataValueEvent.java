/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class DWTDataTraceDataValueEvent extends Event
/*    */ {
/*    */   private byte comparatorId;
/*    */   private byte access;
/*    */   private long dataValue;
/* 19 */   public static byte READ_ACCESS = 0;
/* 20 */   public static byte WRITE_ACCESS = 1;
/*    */ 
/*    */   public DWTDataTraceDataValueEvent(byte comparatorId, byte access, long dataValue) {
/* 23 */     this.comparatorId = comparatorId;
/* 24 */     this.access = access;
/* 25 */     this.dataValue = dataValue;
/*    */   }
/*    */ 
/*    */   public byte getComparatorId()
/*    */   {
/* 32 */     return this.comparatorId;
/*    */   }
/*    */ 
/*    */   public void setComparatorId(byte comparatorId)
/*    */   {
/* 38 */     this.comparatorId = comparatorId;
/*    */   }
/*    */ 
/*    */   public byte getAccess()
/*    */   {
/* 44 */     return this.access;
/*    */   }
/*    */ 
/*    */   public void setAccess(byte access)
/*    */   {
/* 50 */     this.access = access;
/*    */   }
/*    */ 
/*    */   public long getDataValue()
/*    */   {
/* 56 */     return this.dataValue;
/*    */   }
/*    */ 
/*    */   public void setDataValue(int dataValue)
/*    */   {
/* 62 */     this.dataValue = dataValue;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 67 */     return Messages.DWTDataTraceDataValueEvent_0 + " (" + Messages.MODEL_COMPARATOR + " " + this.comparatorId + ") (" + (this.access == READ_ACCESS ? Messages.DWTDataTraceDataValueEvent_R : Messages.DWTDataTraceDataValueEvent_W) + ")";
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 72 */     return getDataValue();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTDataTraceDataValueEvent
 * JD-Core Version:    0.6.2
 */