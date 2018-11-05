/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class ITMOverflowEvent extends Event
/*    */ {
/*    */   private boolean generatedBySync;
/*    */ 
/*    */   public ITMOverflowEvent(boolean generatedBySync)
/*    */   {
/* 18 */     this.generatedBySync = generatedBySync;
/*    */   }
/*    */ 
/*    */   public boolean isGeneratedBySync() {
/* 22 */     return this.generatedBySync;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 27 */     return Messages.ITMOverflowEvent_OVERFLOW;
/*    */   }
/*    */ 
/*    */   public String printExtraInfo()
/*    */   {
/* 32 */     String ret = super.printExtraInfo();
/* 33 */     if (this.generatedBySync)
/* 34 */       ret = ret + Messages.ITMOverflowEvent_GENERATED_DUE_TO_WAITING_FOR_SYNC + ". ";
/* 35 */     return ret;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMOverflowEvent
 * JD-Core Version:    0.6.2
 */