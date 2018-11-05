/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import com.atollic.truestudio.swv.core.SessionManager;
/*    */ 
/*    */ public class DWTDataTracePCValueEvent extends Event
/*    */ {
/*    */   private long pcAddress;
/*    */   private byte comparatorId;
/* 22 */   private String function = null;
/*    */ 
/*    */   public DWTDataTracePCValueEvent(byte comparatorId, long pcAddress) {
/* 25 */     this.comparatorId = comparatorId;
/* 26 */     this.pcAddress = pcAddress;
/*    */   }
/*    */ 
/*    */   public long getPcAddress()
/*    */   {
/* 33 */     return this.pcAddress;
/*    */   }
/*    */ 
/*    */   public void setPcAddress(long pcAddress)
/*    */   {
/* 39 */     this.pcAddress = pcAddress;
/*    */   }
/*    */ 
/*    */   public String getFunction()
/*    */   {
/* 47 */     if (this.function == null)
/*    */     {
/* 49 */       SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 50 */       SWVClient traceClient = sessionManager.getClient();
/*    */ 
/* 52 */       this.function = traceClient.getFunctionName(getPcAddress());
/*    */     }
/* 54 */     return this.function;
/*    */   }
/*    */ 
/*    */   public void setFunction(String function) {
/* 58 */     this.function = function;
/*    */   }
/*    */ 
/*    */   public byte getComparatorId()
/*    */   {
/* 65 */     return this.comparatorId;
/*    */   }
/*    */ 
/*    */   public void setComparatorId(byte comparatorId)
/*    */   {
/* 71 */     this.comparatorId = comparatorId;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 76 */     return Messages.DWTDataTracePCValueEvent_DATA_PC_VALUE + " (" + Messages.MODEL_COMPARATOR + " " + this.comparatorId + ")";
/*    */   }
/*    */ 
/*    */   public String printData()
/*    */   {
/* 81 */     return "0x" + Long.toString(getPcAddress(), 16) + "  " + getFunction();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.DWTDataTracePCValueEvent
 * JD-Core Version:    0.6.2
 */