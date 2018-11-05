/*     */ package com.atollic.truestudio.swv.model;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*     */ import com.atollic.truestudio.tsp.sfr.SfrHexOrDecimal;
/*     */ import com.atollic.truestudio.tsp.sfr.SfrPeripheralIRQ;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class InterruptInfo
/*     */   implements Comparable<Object>
/*     */ {
/*     */   public static final int TOT_ID = 999999;
/*     */   private int id;
/*     */   private String sfrName;
/*     */   private String realName;
/*     */   private String peripheral;
/*     */   private String functionAddress;
/*     */   private String function;
/*     */   private boolean isAccessed;
/*     */   private boolean hasAllInfo;
/*  28 */   private long numberOf = 0L;
/*  29 */   private long numberOfTotalyCorrect = 0L;
/*  30 */   private float percentNumberOf = 0.0F;
/*  31 */   private long firstCycles = 0L;
/*  32 */   private String firstTime = "";
/*  33 */   private long latestCycles = 0L;
/*  34 */   private String latestTime = "";
/*  35 */   private long latestEnterCycles = 0L;
/*  36 */   private boolean latestWasEntry = false;
/*  37 */   private long minRun = 0L;
/*  38 */   private long maxRun = 0L;
/*  39 */   private long totalExceptionRun = 0L;
/*  40 */   private float percentTotalExceptionRun = 0.0F;
/*     */   private static InterruptInfo defaultInterruptInfo;
/*     */ 
/*     */   public InterruptInfo(int id)
/*     */   {
/*  44 */     this.id = id;
/*  45 */     this.sfrName = Messages.MODEL_UNKNOWN;
/*  46 */     this.realName = null;
/*  47 */     this.peripheral = "";
/*  48 */     this.functionAddress = null;
/*  49 */     this.function = null;
/*  50 */     this.isAccessed = false;
/*  51 */     this.hasAllInfo = false;
/*     */   }
/*     */ 
/*     */   public InterruptInfo(SfrPeripheralIRQ sfrIrqInfo) {
/*  55 */     this(sfrIrqInfo.getValue().intValue().intValue());
/*  56 */     this.sfrName = sfrIrqInfo.getName();
/*  57 */     this.peripheral = sfrIrqInfo.getPeripheral();
/*     */   }
/*     */ 
/*     */   public InterruptInfo(long totalNumberOf, long totalExceptionRun)
/*     */   {
/*  67 */     this.id = 999999;
/*  68 */     this.sfrName = Messages.InterruptInfo_TOTAL_FOR_ALL;
/*  69 */     this.realName = Messages.InterruptInfo_TOTAL_FOR_ALL;
/*  70 */     this.peripheral = Messages.MODEL_NA;
/*  71 */     this.functionAddress = null;
/*  72 */     this.function = null;
/*  73 */     this.isAccessed = true;
/*  74 */     this.hasAllInfo = true;
/*  75 */     this.totalExceptionRun = totalExceptionRun;
/*  76 */     this.numberOf = totalNumberOf;
/*  77 */     this.numberOfTotalyCorrect = totalNumberOf;
/*     */   }
/*     */ 
/*     */   public static synchronized InterruptInfo getDefaultInterruptInfo()
/*     */   {
/*  84 */     if (defaultInterruptInfo != null) {
/*  85 */       return defaultInterruptInfo;
/*     */     }
/*  87 */     defaultInterruptInfo = new InterruptInfo(513);
/*  88 */     return defaultInterruptInfo;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  95 */     this.numberOf = 0L;
/*  96 */     this.numberOfTotalyCorrect = 0L;
/*  97 */     this.percentNumberOf = 0.0F;
/*  98 */     this.firstCycles = 0L;
/*  99 */     this.firstTime = "";
/* 100 */     this.latestCycles = 0L;
/* 101 */     this.latestTime = "";
/* 102 */     this.latestEnterCycles = 0L;
/* 103 */     this.latestWasEntry = false;
/* 104 */     this.minRun = 0L;
/* 105 */     this.maxRun = 0L;
/* 106 */     this.totalExceptionRun = 0L;
/* 107 */     this.percentTotalExceptionRun = 0.0F;
/* 108 */     this.isAccessed = false;
/*     */   }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 115 */     return this.id;
/*     */   }
/*     */ 
/*     */   public boolean isTotalInfoObject() {
/* 119 */     return getId() == 999999;
/*     */   }
/*     */ 
/*     */   public boolean isAccessed() {
/* 123 */     return this.isAccessed;
/*     */   }
/*     */ 
/*     */   public void setAccessed()
/*     */   {
/* 130 */     if (this.id < SWVInterruptParser.NR_OF_INTERUPTS.intValue())
/* 131 */       this.isAccessed = true;
/*     */   }
/*     */ 
/*     */   public boolean hasAllInfo()
/*     */   {
/* 139 */     return this.hasAllInfo;
/*     */   }
/*     */ 
/*     */   public void setHasAllInfo()
/*     */   {
/* 147 */     this.hasAllInfo = true;
/*     */   }
/*     */ 
/*     */   public String getSfrName()
/*     */   {
/* 154 */     return this.sfrName;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 161 */     if (this.realName != null) {
/* 162 */       return this.realName;
/*     */     }
/* 164 */     int NrOfExceptionNames = DWTExceptionEvent.EXCEPTION_NAMES.length;
/* 165 */     String exceptionName = "";
/* 166 */     int num = getId();
/* 167 */     if (num >= 0) {
/* 168 */       if (num < NrOfExceptionNames) {
/* 169 */         exceptionName = DWTExceptionEvent.EXCEPTION_NAMES[num];
/* 170 */         this.realName = (exceptionName + " (" + Messages.InterruptInfo_EXC + " " + num + ")");
/*     */       } else {
/* 172 */         exceptionName = getSfrName();
/* 173 */         if ((exceptionName == null) || (exceptionName.isEmpty())) {
/* 174 */           exceptionName = String.valueOf(num);
/*     */         }
/* 176 */         int irqNum = num - NrOfExceptionNames;
/* 177 */         this.realName = (exceptionName + " (" + Messages.InterruptInfo_IRQ + " " + String.valueOf(irqNum) + ")");
/*     */       }
/*     */     }
/* 180 */     if ((this.realName == null) || (this.realName.isEmpty())) {
/* 181 */       this.realName = String.valueOf(num);
/*     */     }
/* 183 */     return this.realName;
/*     */   }
/*     */ 
/*     */   public String getPeripheral()
/*     */   {
/* 190 */     return this.peripheral;
/*     */   }
/*     */ 
/*     */   public String getFunctionAddress()
/*     */   {
/* 197 */     return this.functionAddress;
/*     */   }
/*     */ 
/*     */   public void setFunctionAddress(String functionAddress)
/*     */   {
/* 204 */     if ((functionAddress == null) || (functionAddress.equals("0")) || (functionAddress.equals("-1"))) {
/* 205 */       System.out.println("Error: Null -address given " + functionAddress);
/*     */     }
/* 207 */     this.functionAddress = functionAddress;
/*     */   }
/*     */ 
/*     */   public String getFunction()
/*     */   {
/* 215 */     return hasAllInfo() ? this.function : null;
/*     */   }
/*     */ 
/*     */   public void setFunction(String function)
/*     */   {
/* 222 */     this.function = function;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 227 */     if (getFunction() == null) {
/* 228 */       return this.sfrName + " - " + this.peripheral;
/*     */     }
/* 230 */     return this.sfrName + " - " + getFunction() + " - " + this.peripheral;
/*     */   }
/*     */ 
/*     */   public void countOneMoreInterrupt()
/*     */   {
/* 243 */     this.numberOf += 1L;
/*     */   }
/*     */ 
/*     */   public void countOneMoreTotalyCorrectInterrupt()
/*     */   {
/* 255 */     this.numberOfTotalyCorrect += 1L;
/*     */   }
/*     */ 
/*     */   public synchronized void updateStatistics(DWTExceptionEvent interruptEvent)
/*     */   {
/* 265 */     if ((interruptEvent.isIncludedInStatistics()) || (interruptEvent.isReturnEvent()))
/* 266 */       return;
/* 267 */     if (interruptEvent.isEntryEvent()) {
/* 268 */       countOneMoreInterrupt();
/*     */     }
/* 270 */     long cycles = interruptEvent.getCycles();
/*     */ 
/* 272 */     if ((cycles <= 0L) && (!interruptEvent.isCyclesValueIsNotReal())) {
/* 273 */       return;
/*     */     }
/* 275 */     if (interruptEvent.isEntryEvent()) {
/* 276 */       if ((cycles < this.firstCycles) || (this.firstCycles == 0L) || (this.firstTime == "?")) {
/* 277 */         this.firstCycles = cycles;
/* 278 */         this.firstTime = interruptEvent.printTime();
/*     */       }
/* 280 */       if (cycles > this.latestCycles) {
/* 281 */         this.latestCycles = cycles;
/* 282 */         this.latestTime = interruptEvent.printTime();
/*     */       }
/* 284 */       this.latestEnterCycles = cycles;
/* 285 */       this.latestWasEntry = true;
/* 286 */     } else if ((interruptEvent.isExitEvent()) && (this.latestWasEntry) && (this.latestEnterCycles > 0L)) {
/* 287 */       this.latestWasEntry = false;
/* 288 */       long run = cycles - this.latestEnterCycles;
/*     */ 
/* 290 */       if (run > 0L) {
/* 291 */         if ((this.minRun == 0L) || (this.minRun > run))
/* 292 */           this.minRun = run;
/* 293 */         if (run > this.maxRun)
/* 294 */           this.maxRun = run;
/* 295 */         this.totalExceptionRun += run;
/* 296 */         countOneMoreTotalyCorrectInterrupt();
/*     */       }
/*     */     }
/* 299 */     interruptEvent.setIncludedInStatistics();
/*     */   }
/*     */ 
/*     */   public long getNumberOf()
/*     */   {
/* 306 */     return this.numberOf;
/*     */   }
/*     */ 
/*     */   public long getNumberOfTotalyCorrect()
/*     */   {
/* 313 */     return this.numberOfTotalyCorrect;
/*     */   }
/*     */ 
/*     */   public float getPercentNumberOf()
/*     */   {
/* 321 */     return this.percentNumberOf;
/*     */   }
/*     */ 
/*     */   public void setPercentNumberOf(long totalNumberOf)
/*     */   {
/* 328 */     if ((this.numberOf <= 0L) || (totalNumberOf <= 0L))
/*     */     {
/* 330 */       this.percentNumberOf = 0.0F;
/*     */     }
/* 332 */     else this.percentNumberOf = ((float)(this.numberOf * 100.0D / totalNumberOf));
/*     */   }
/*     */ 
/*     */   public long getFirstCycles()
/*     */   {
/* 340 */     return this.firstCycles;
/*     */   }
/*     */ 
/*     */   public String getFirstTime()
/*     */   {
/* 347 */     return this.firstTime;
/*     */   }
/*     */ 
/*     */   public long getLatestCycles()
/*     */   {
/* 354 */     return this.latestCycles;
/*     */   }
/*     */ 
/*     */   public String getLatestTime()
/*     */   {
/* 361 */     return this.latestTime;
/*     */   }
/*     */ 
/*     */   public long getMinRun()
/*     */   {
/* 368 */     return this.minRun;
/*     */   }
/*     */ 
/*     */   public long getMaxRun()
/*     */   {
/* 375 */     return this.maxRun;
/*     */   }
/*     */ 
/*     */   public long getTotalRun()
/*     */   {
/* 382 */     return this.totalExceptionRun;
/*     */   }
/*     */ 
/*     */   public float getPercentTotalExceptionRun()
/*     */   {
/* 389 */     return this.percentTotalExceptionRun;
/*     */   }
/*     */ 
/*     */   public void setPercentTotalExceptionRun(long totalTotalRun)
/*     */   {
/* 397 */     if ((totalTotalRun == 0L) || (this.numberOf <= 0L))
/* 398 */       this.percentTotalExceptionRun = 0.0F;
/*     */     else
/* 400 */       this.percentTotalExceptionRun = ((float)(getTotalRun() * 100.0D / totalTotalRun));
/*     */   }
/*     */ 
/*     */   public int compareTo(Object arg0)
/*     */   {
/* 406 */     if ((arg0 instanceof InterruptInfo)) {
/* 407 */       if (((InterruptInfo)arg0).getNumberOf() < getNumberOf())
/* 408 */         return -1;
/* 409 */       if (((InterruptInfo)arg0).getNumberOf() > getNumberOf())
/* 410 */         return 1;
/*     */     }
/* 412 */     return 0;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.model.InterruptInfo
 * JD-Core Version:    0.6.2
 */