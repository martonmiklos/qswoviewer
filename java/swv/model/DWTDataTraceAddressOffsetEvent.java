/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class DWTDataTraceAddressOffsetEvent extends Event
/*    */ {
/*    */   private int addressOffset;
/*    */   private byte comparatorId;
/*    */ 
/*    */   public DWTDataTraceAddressOffsetEvent(byte comparatorId, int addressOffset)
/*    */   {
/* 20 */     this.comparatorId = comparatorId;
/* 21 */     this.addressOffset = addressOffset;
/*    */   }
/*    */ 
/*    */   public int getAddressOffset()
/*    */   {
/* 28 */     return this.addressOffset;
/*    */   }
/*    */ 
/*    */   public void setAddressOffset(int addressOffset)
/*    */   {
/* 34 */     this.addressOffset = addressOffset;
/*    */   }
/*    */ 
/*    */   public byte getComparatorId()
/*    */   {
/* 40 */     return this.comparatorId;
/*    */   }
/*    */ 
/*    */   public void setComparatorId(byte comparatorId)
/*    */   {
/* 46 */     this.comparatorId = comparatorId;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 51 */     return Messages.DWTDataTraceAddressOffsetEvent_DATA_TRACE_ADDRESS_OFFSET + "  (" + Messages.MODEL_COMPARATOR + " " + this.comparatorId + ")";
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 56 */     return "0x" + Integer.toString(getAddressOffset(), 16);
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTDataTraceAddressOffsetEvent
 * JD-Core Version:    0.6.2
 */