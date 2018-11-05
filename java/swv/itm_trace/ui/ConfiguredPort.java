/*    */ package com.atollic.truestudio.swv.itm_trace.ui;
/*    */ 
/*    */ import org.eclipse.swt.custom.StyledText;
/*    */ 
/*    */ public class ConfiguredPort
/*    */ {
/*    */   private static final String EMPTY_STRING = "";
/*    */   private int itmPortNumber;
/*    */   private StyledText console;
/*    */   private StringBuffer textBuffer;
/*    */ 
/*    */   public ConfiguredPort(int itmPortNumber, StyledText console)
/*    */   {
/* 23 */     this.itmPortNumber = itmPortNumber;
/* 24 */     this.console = console;
/* 25 */     this.textBuffer = new StringBuffer("");
/*    */   }
/*    */ 
/*    */   public int getItmPortNumber()
/*    */   {
/* 33 */     return this.itmPortNumber;
/*    */   }
/*    */ 
/*    */   public void append(String data)
/*    */   {
/* 40 */     this.textBuffer.append(data);
/*    */   }
/*    */ 
/*    */   public void flush(boolean autoScroll)
/*    */   {
/* 50 */     if (autoScroll) {
/* 51 */       this.console.append(this.textBuffer.toString());
/* 52 */       this.console.setCaretOffset(this.console.getCharCount());
/* 53 */       this.console.showSelection();
/*    */     } else {
/* 55 */       this.console.append(this.textBuffer.toString());
/*    */     }
/*    */ 
/* 59 */     this.textBuffer = new StringBuffer("");
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 66 */     this.textBuffer = new StringBuffer("");
/* 67 */     this.console.setText("");
/*    */   }
/*    */ 
/*    */   public StyledText getConsole()
/*    */   {
/* 75 */     return this.console;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     return "ConfiguredPort [itmPortNumber=" + this.itmPortNumber + "]";
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.itm_trace_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.itm_trace.ui.ConfiguredPort
 * JD-Core Version:    0.6.2
 */