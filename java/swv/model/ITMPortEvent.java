/*     */ package com.atollic.truestudio.swv.model;
/*     */ 
/*     */ public class ITMPortEvent extends Event
/*     */ {
/*     */   private byte pageNumber;
/*     */   private byte portNumber;
/*     */   private long data;
/*     */   private int size;
/*     */ 
/*     */   public ITMPortEvent(byte pageNumber, byte portNumber, long data, int size)
/*     */   {
/*  21 */     this.pageNumber = pageNumber;
/*  22 */     this.portNumber = portNumber;
/*  23 */     this.data = data;
/*  24 */     this.size = size;
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/*  31 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void setSize(int size)
/*     */   {
/*  38 */     this.size = size;
/*     */   }
/*     */ 
/*     */   public byte getPageNumber()
/*     */   {
/*  45 */     return this.pageNumber;
/*     */   }
/*     */ 
/*     */   public void setPageNumber(byte pageNumber)
/*     */   {
/*  52 */     this.pageNumber = pageNumber;
/*     */   }
/*     */ 
/*     */   public byte getPortNumber()
/*     */   {
/*  59 */     return this.portNumber;
/*     */   }
/*     */ 
/*     */   public void setPortNumber(byte portNumber)
/*     */   {
/*  66 */     this.portNumber = portNumber;
/*     */   }
/*     */ 
/*     */   public long getData()
/*     */   {
/*  73 */     return this.data;
/*     */   }
/*     */ 
/*     */   public void setData(long data)
/*     */   {
/*  80 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public String printData()
/*     */   {
/*  88 */     return getData();
/*     */   }
/*     */ 
/*     */   public String printType()
/*     */   {
/*  93 */     return Messages.ITMPortEvent_ITM_PORT + " " + getPortNumber();
/*     */   }
/*     */ 
/*     */   public String printExtraInfo()
/*     */   {
/*  98 */     String sup = super.printExtraInfo();
/*  99 */     if (getPageNumber() != 0)
/* 100 */       sup = sup + " " + Messages.ITMPortEvent_PAGE + " " + getPageNumber();
/* 101 */     return sup;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.ITMPortEvent
 * JD-Core Version:    0.6.2
 */