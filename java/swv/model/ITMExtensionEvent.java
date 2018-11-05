/*    */ package com.atollic.truestudio.swv.model;
/*    */ 
/*    */ public class ITMExtensionEvent extends Event
/*    */ {
/*    */   private byte page;
/*    */ 
/*    */   public ITMExtensionEvent(byte page)
/*    */   {
/* 18 */     this.page = page;
/*    */   }
/*    */ 
/*    */   public byte getPage()
/*    */   {
/* 25 */     return this.page;
/*    */   }
/*    */ 
/*    */   public String printType()
/*    */   {
/* 30 */     return Messages.ITMExtensionEvent_EXTENSION;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMExtensionEvent
 * JD-Core Version:    0.6.2
 */