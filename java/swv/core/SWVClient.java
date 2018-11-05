/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.TSProjectManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVUtil;
/*     */ import com.atollic.truestudio.swv.core.ui.statisticalprofiling.Messages;
/*     */ import com.atollic.truestudio.swv.model.DWTCounterEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTraceAddressOffsetEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTraceDataValueEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTDataTracePCValueEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*     */ import com.atollic.truestudio.swv.model.DWTPCSampleEvent;
/*     */ import com.atollic.truestudio.swv.model.Event;
/*     */ import com.atollic.truestudio.swv.model.ITMExtensionEvent;
/*     */ import com.atollic.truestudio.swv.model.ITMGlobalTimestampEvent;
/*     */ import com.atollic.truestudio.swv.model.ITMLocalTimestampEvent;
/*     */ import com.atollic.truestudio.swv.model.ITMOverflowEvent;
/*     */ import com.atollic.truestudio.swv.model.ITMPortEvent;
/*     */ import com.atollic.truestudio.swv.model.ITMSyncEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.Socket;
/*     */ import org.eclipse.cdt.core.IAddress;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.cdt.utils.Addr32;
/*     */ import org.eclipse.cdt.utils.elf.Elf;
/*     */ import org.eclipse.cdt.utils.elf.Elf.Symbol;
/*     */ import org.eclipse.cdt.utils.elf.ElfHelper;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.ILog;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.variables.IStringVariableManager;
/*     */ import org.eclipse.core.variables.VariablesPlugin;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ 
/*     */ public class SWVClient extends Thread
/*     */ {
/*     */   private static final String KOLON = ":";
/*     */   private static final String EMPTY_STRING = "";
/*  51 */   private SWVComparatorConfig[] comparatorConfig = new SWVComparatorConfig[4];
/*     */   private SWVBuffer rxBuffer;
/*     */   private SWVBuffer exceptionBuffer;
/*     */   private DsfSession session;
/*     */   private boolean tracing;
/*     */   private boolean fSuspended;
/*  58 */   private Socket socket = null;
/*  59 */   private InputStream iStream = null;
/*  60 */   private boolean disposed = false;
/*     */   private String host;
/*     */   private int port;
/*     */   private int swvCoreClock;
/*     */   private int swvTraceDiv;
/*     */   private int swoClock;
/*  66 */   private int timePrescaler = 1;
/*  67 */   private boolean TRACE_ON = false;
/*  68 */   private int TRACE_TYPE = 6;
/*  69 */   private final int TRACE_WARNING = 4;
/*  70 */   private final int TRACE_ERROR = 2;
/*  71 */   private final int TRACE_MSG = 1;
/*  72 */   private byte ITMPortPage = 0;
/*     */   private static final int EOF = -1;
/*     */   private static final int BROKEN_PACKET = -2;
/*  75 */   private ILaunchConfiguration launchConfiguration = null;
/*     */ 
/*  77 */   private long cycles = 0L;
/*  78 */   private long clientCreationTime = 0L;
/*     */ 
/*  80 */   private final int DBGHW_BUFFER_SIZE = 4096;
/*  81 */   private boolean DBGHW_BUFFER_BOUNDARY_DANGEROUS = false;
/*  82 */   private boolean WAIT_FOR_SYNC = false;
/*  83 */   private int bytesLostDueToSyncWait = 0;
/*     */ 
/*  85 */   private long totalBytesLost = 0L;
/*     */ 
/*  87 */   private SWVInterruptParser sWVInterruptParser = null;
/*  88 */   private static ElfHelper elfHelper = null;
/*     */ 
/*  91 */   private long overflowPackets = 0L;
/*     */ 
/* 277 */   int[] buffer = new int[102400];
/* 278 */   long sizeRead = 0L;
/*     */ 
/*     */   public SWVClient(DsfSession session, ILaunchConfiguration config)
/*     */   {
/*  95 */     this.rxBuffer = new SWVBuffer(this);
/*  96 */     this.exceptionBuffer = new SWVBuffer(this);
/*  97 */     this.session = session;
/*  98 */     this.clientCreationTime = System.currentTimeMillis();
/*  99 */     this.launchConfiguration = config;
/*     */     try
/*     */     {
/* 102 */       this.DBGHW_BUFFER_BOUNDARY_DANGEROUS = this.launchConfiguration.getAttribute("com.atollic.hardwaredebug.launch.swv_wait_for_sync", false);
/*     */     } catch (Exception e) {
/* 104 */       this.DBGHW_BUFFER_BOUNDARY_DANGEROUS = false;
/* 105 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 114 */     boolean clientWasTracing = isTracing();
/* 115 */     setTracing(false);
/* 116 */     this.rxBuffer.clear();
/* 117 */     this.exceptionBuffer.clear();
/* 118 */     this.overflowPackets = 0L;
/* 119 */     this.cycles = 0L;
/* 120 */     if (clientWasTracing) {
/* 121 */       setTracing(true);
/*     */     }
/* 123 */     if (elfHelper != null) {
/* 124 */       elfHelper.dispose();
/* 125 */       elfHelper = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public DsfSession getSession()
/*     */   {
/* 133 */     return this.session;
/*     */   }
/*     */ 
/*     */   public ILaunchConfiguration getLaunchConfiguration()
/*     */   {
/* 140 */     return this.launchConfiguration;
/*     */   }
/*     */ 
/*     */   public void connect(String host, int port, int swoClock, int swvCoreClock, int swvTraceDiv)
/*     */     throws Exception
/*     */   {
/* 152 */     if (isAlive()) {
/* 153 */       throw new Exception("SWV Client cannot connect again because thread has already been started.");
/*     */     }
/*     */ 
/* 156 */     this.host = host;
/* 157 */     this.port = port;
/* 158 */     this.swvCoreClock = swvCoreClock;
/* 159 */     this.swvTraceDiv = swvTraceDiv;
/* 160 */     this.swoClock = swoClock;
/*     */ 
/* 162 */     connect();
/*     */ 
/* 165 */     start();
/*     */   }
/*     */ 
/*     */   private synchronized void connect() throws IOException, SecurityException, IllegalArgumentException {
/*     */     try {
/* 170 */       cleanSocket();
/*     */ 
/* 172 */       this.socket = new Socket(this.host, this.port);
/* 173 */       this.iStream = this.socket.getInputStream();
/*     */     } catch (IOException|SecurityException|IllegalArgumentException e) {
/* 175 */       this.iStream = null;
/* 176 */       this.socket = null;
/* 177 */       SWVPlugin.log(new Status(4, "com.atollic.truestudio.swv.core", "SWVClient - Failed to connect to Host: " + this.host + " Port: " + this.port));
/* 178 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SWVBuffer getRxBuffer() {
/* 183 */     return this.rxBuffer;
/*     */   }
/*     */ 
/*     */   public SWVBuffer getExceptionBuffer() {
/* 187 */     return this.exceptionBuffer;
/*     */   }
/*     */ 
/*     */   public void changeTimePrescaler()
/*     */   {
/* 198 */     int prescaler = 1;
/*     */     try {
/* 200 */       String prescalerStr = this.launchConfiguration.getAttribute("com.atollic.truestudio.swv.core.timestamps", "1:1");
/* 201 */       prescalerStr = prescalerStr.split(":")[1];
/* 202 */       prescaler = Integer.parseInt(prescalerStr);
/*     */     } catch (CoreException e) {
/* 204 */       SWVPlugin.log(new Status(2, "com.atollic.truestudio.swv.core", "SWVClient - Unable to set Prescaler."));
/* 205 */       e.printStackTrace();
/*     */     }
/* 207 */     this.timePrescaler = prescaler;
/*     */   }
/*     */ 
/*     */   public boolean isTimestampsEnabled() {
/* 211 */     String enabled = "1:1";
/*     */     try {
/* 213 */       enabled = this.launchConfiguration.getAttribute("com.atollic.truestudio.swv.core.timestamps", "1:1");
/*     */     }
/*     */     catch (CoreException e) {
/* 216 */       e.printStackTrace();
/* 217 */       return false;
/*     */     }
/* 219 */     if (!enabled.contains(":")) {
/* 220 */       return false;
/*     */     }
/* 222 */     String[] enas = enabled.split(":");
/* 223 */     return enas[0].equals("1");
/*     */   }
/*     */ 
/*     */   public long getCycles()
/*     */   {
/* 231 */     return this.cycles;
/*     */   }
/*     */ 
/*     */   public int getSWVTraceDiv() {
/* 235 */     return this.swvTraceDiv;
/*     */   }
/*     */ 
/*     */   public int getSWOScalar()
/*     */   {
/* 242 */     if (this.swvTraceDiv < 0) {
/* 243 */       SWVPlugin.getDefault().getLog().log(new Status(2, "com.atollic.truestudio.swv.core", "Bad SWV trace divider, using default."));
/* 244 */       return 128;
/*     */     }
/*     */ 
/* 254 */     return this.swvTraceDiv - 1;
/*     */   }
/*     */ 
/*     */   public int getSWVCoreClock() {
/* 258 */     return this.swvCoreClock;
/*     */   }
/*     */ 
/*     */   public int getSWOClock() {
/* 262 */     return this.swoClock;
/*     */   }
/*     */ 
/*     */   public long getClientCreationTime() {
/* 266 */     return this.clientCreationTime;
/*     */   }
/*     */ 
/*     */   public long getOverflowPacketsCount() {
/* 270 */     return this.overflowPackets;
/*     */   }
/*     */ 
/*     */   private int getNextByte(boolean shouldBeHeader)
/*     */   {
/*     */     try
/*     */     {
/* 281 */       return readByte(shouldBeHeader);
/*     */     } catch (IOException e) {
/* 283 */       SWVPlugin.getDefault().getLog().log(new Status(4, "com.atollic.truestudio.swv.core", "SWV Client received I/O Exception: " + e.getMessage() + "... trying to reconnect"));
/*     */       try
/*     */       {
/* 286 */         connect();
/* 287 */         return readByte(shouldBeHeader);
/*     */       } catch (IOException|SecurityException|IllegalArgumentException e2) {
/* 289 */         SWVPlugin.getDefault().getLog().log(new Status(4, "com.atollic.truestudio.swv.core", "Failed to reconnect and read data", e2));
/*     */       }
/*     */     }
/* 292 */     return -1;
/*     */   }
/*     */ 
/*     */   private synchronized int readByte(boolean shouldBeHeader) throws IOException {
/* 296 */     int read = this.iStream.read();
/* 297 */     if (-1 == read) {
/* 298 */       System.out.println("SWV Client received EOF");
/* 299 */       return -1;
/*     */     }
/* 301 */     this.buffer[((int)this.sizeRead % 102400)] = read;
/* 302 */     this.sizeRead += 1L;
/* 303 */     if ((this.DBGHW_BUFFER_BOUNDARY_DANGEROUS) && (this.sizeRead != 1L) && ((this.sizeRead - 1L) % 4096L == 0L)) {
/* 304 */       this.WAIT_FOR_SYNC = true;
/* 305 */       if (!shouldBeHeader) {
/* 306 */         return -2;
/*     */       }
/*     */     }
/*     */ 
/* 310 */     return read;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 320 */       setName("SWV client thread");
/*     */ 
/* 322 */       int c = -1;
/* 323 */       int syncbytes = 0;
/*     */ 
/* 325 */       while (!this.disposed) {
/* 326 */         c = getNextByte(true);
/*     */ 
/* 329 */         if (-1 == c)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 335 */         if (c == 0)
/*     */         {
/* 337 */           syncbytes++;
/* 338 */           TRACE(1, "Sync packet received, 0x" + Integer.toString(c, 16));
/*     */         }
/*     */         else
/*     */         {
/* 343 */           if ((syncbytes > 5) || ((syncbytes == 5) && (c == 128)))
/*     */           {
/* 345 */             TRACE(1, "Sync packet complete, size " + syncbytes + ", 0x" + Integer.toString(c, 16));
/*     */ 
/* 348 */             if (this.WAIT_FOR_SYNC) {
/* 349 */               this.WAIT_FOR_SYNC = false;
/* 350 */               this.totalBytesLost += this.bytesLostDueToSyncWait;
/*     */ 
/* 352 */               this.bytesLostDueToSyncWait = 0;
/*     */ 
/* 354 */               ITMOverflowEvent o = new ITMOverflowEvent(true);
/* 355 */               this.rxBuffer.addRecord(o);
/* 356 */               this.exceptionBuffer.addRecord(o, false);
/* 357 */               if (isTracing()) {
/* 358 */                 this.overflowPackets += 1L;
/*     */               }
/* 360 */               TRACE(1, "OVERFLOW DUE TO SYNC...\n");
/*     */             }
/*     */ 
/* 364 */             this.ITMPortPage = 0;
/* 365 */             ITMSyncEvent e = new ITMSyncEvent((syncbytes + 1) * 8);
/* 366 */             this.rxBuffer.addRecord(e);
/*     */ 
/* 368 */             syncbytes = 0;
/*     */ 
/* 388 */             int onebits = 0;
/* 389 */             for (int i = 0; i < 8; i++)
/* 390 */               if ((c & 1 << i) > 0)
/* 391 */                 onebits++;
/* 392 */             if (onebits == 1)
/* 393 */               continue;
/* 394 */           } else if (syncbytes != 0) {
/* 395 */             TRACE(4, "WARNING: incomplete sync packet discarded, size " + syncbytes);
/* 396 */             syncbytes = 0;
/*     */           }
/*     */ 
/* 400 */           if (this.WAIT_FOR_SYNC) {
/* 401 */             this.bytesLostDueToSyncWait += 1;
/*     */           }
/* 405 */           else if ((c & 0x3) == 0) {
/* 406 */             if (createProtocolPacket(c) == -1)
/* 407 */               break;
/*     */           }
/* 409 */           else if ((c & 0x3) != 0)
/*     */           {
/* 411 */             if ((c & 0x4) == 0)
/*     */             {
/* 413 */               if (createInstrumentationPacket(c) == -1) {
/* 414 */                 break;
/*     */               }
/*     */             }
/* 417 */             else if (createHardwareSourcePacket(c) == -1) {
/* 418 */               break;
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 424 */             TRACE(2, "Unhandled packet! 0x" + Integer.toString(c, 16));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 430 */       cleanSocket();
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void cleanSocket()
/*     */   {
/*     */     try {
/* 437 */       if (this.iStream != null) {
/* 438 */         this.iStream.close();
/* 439 */         this.iStream = null;
/*     */       }
/*     */     } catch (IOException e) {
/* 442 */       SWVPlugin.getDefault().getLog().log(new Status(4, "com.atollic.truestudio.swv.core", "Could not close the stream", e));
/*     */     }
/*     */     try
/*     */     {
/* 446 */       if (this.socket != null) {
/* 447 */         this.socket.close();
/* 448 */         this.socket = null;
/*     */       }
/*     */     } catch (IOException e) {
/* 451 */       SWVPlugin.getDefault().getLog().log(new Status(4, "com.atollic.truestudio.swv.core", "Could not close the SWV socket", e));
/*     */     }
/*     */   }
/*     */ 
/*     */   private long getContinuationPacket()
/*     */   {
/* 461 */     long val = 0L;
/* 462 */     int c = 0;
/*     */ 
/* 464 */     for (int i = 0; i < 4; i++) {
/* 465 */       c = getNextByte(false);
/* 466 */       if (c == -2) {
/* 467 */         return -2L;
/*     */       }
/* 469 */       if (c == -1) {
/* 470 */         return -1L;
/*     */       }
/* 472 */       val |= (c & 0x7F) << 7 * i;
/*     */ 
/* 474 */       if ((c & 0x80) == 0)
/*     */       {
/* 476 */         return val;
/*     */       }
/*     */     }
/* 479 */     TRACE(4, "WARNING: continuation packet may be too long, last byte: 0x" + Integer.toString(c, 16));
/* 480 */     return val;
/*     */   }
/*     */ 
/*     */   private int getSizeFromSSBits(int ssbits)
/*     */   {
/* 495 */     if (ssbits == 3)
/* 496 */       return 4;
/* 497 */     if ((ssbits > 3) || (ssbits < 1))
/* 498 */       return 0;
/* 499 */     return ssbits;
/*     */   }
/*     */ 
/*     */   private int createProtocolPacket(int c)
/*     */   {
/* 508 */     TRACE(1, "Protocol packet received, 0x" + Integer.toString(c, 16));
/*     */ 
/* 511 */     if (c == 112)
/*     */     {
/* 515 */       ITMOverflowEvent e = new ITMOverflowEvent(false);
/* 516 */       this.rxBuffer.addRecord(e);
/* 517 */       this.exceptionBuffer.addRecord(e, false);
/* 518 */       if (isTracing()) {
/* 519 */         this.overflowPackets += 1L;
/*     */       }
/* 521 */       TRACE(1, "OVERFLOW ...\n");
/* 522 */       return 0;
/*     */     }
/*     */ 
/* 525 */     switch (c & 0xF)
/*     */     {
/*     */     case 0:
/* 529 */       if (((c & 0xF0) == 0) || ((c & 0xF0) == 112))
/*     */       {
/* 531 */         TRACE(2, "Invalid timestamp packet 0x" + Integer.toString(c, 16));
/* 532 */         return 1;
/*     */       }
/*     */ 
/* 535 */       if ((c & 0xC0) == 192)
/*     */       {
/* 537 */         byte timecontrol = (byte)((c & 0x30) >> 4);
/* 538 */         long payload = getContinuationPacket();
/* 539 */         if (payload == -1L)
/* 540 */           return -1;
/* 541 */         if (payload == -2L) {
/* 542 */           return -2;
/*     */         }
/* 544 */         ITMLocalTimestampEvent e = new ITMLocalTimestampEvent(timecontrol, payload * this.timePrescaler);
/* 545 */         if (isTracing()) {
/* 546 */           this.cycles += e.getTimeStamp();
/*     */         }
/* 548 */         e.setCycles(this.cycles);
/* 549 */         e.setTimes(this.cycles / this.swvCoreClock);
/* 550 */         this.rxBuffer.setTimestampOnLastAndInterpolateBack(this.cycles, this.cycles / this.swvCoreClock);
/* 551 */         Event event = (Event)this.rxBuffer.getLastRecord();
/* 552 */         if ((event != null) && 
/* 553 */           (event.getLocalTimestamp() == null)) {
/* 554 */           event.setLocalTimestamp(e);
/*     */         }
/*     */       }
/* 557 */       else if ((c & 0x80) == 0)
/*     */       {
/* 560 */         long timestamp = (c & 0x70) >> 4;
/* 561 */         ITMLocalTimestampEvent e = new ITMLocalTimestampEvent((byte)0, timestamp * this.timePrescaler);
/* 562 */         if (isTracing()) {
/* 563 */           this.cycles += e.getTimeStamp();
/*     */         }
/* 565 */         e.setCycles(this.cycles);
/* 566 */         e.setTimes(this.cycles / this.swvCoreClock);
/* 567 */         this.rxBuffer.setTimestampOnLastAndInterpolateBack(this.cycles, this.cycles / this.swvCoreClock);
/* 568 */         Event event = (Event)this.rxBuffer.getLastRecord();
/* 569 */         if ((event != null) && 
/* 570 */           (event.getLocalTimestamp() == null))
/* 571 */           event.setLocalTimestamp(e);
/*     */       }
/*     */       else
/*     */       {
/* 575 */         TRACE(2, "Invalid local timestamp format 0x" + Integer.toString(c, 16));
/*     */       }
/* 577 */       break;
/*     */     case 4:
/* 581 */       if ((c & 0xDF) == 148)
/*     */       {
/* 583 */         long payload = getContinuationPacket();
/* 584 */         if (payload == -1L) {
/* 585 */           return -1;
/*     */         }
/* 587 */         if ((c & 0xFF) == 148)
/*     */         {
/* 589 */           byte clkch = (byte)(int)((payload & 0x400000) >> 25);
/* 590 */           byte wrap = (byte)(int)((payload & 0x800000) >> 26);
/* 591 */           ITMGlobalTimestampEvent e = new ITMGlobalTimestampEvent(payload * this.timePrescaler, clkch, wrap, (byte)1);
/* 592 */           this.rxBuffer.addRecord(e);
/*     */         }
/* 594 */         else if ((c & 0xFF) == 180)
/*     */         {
/* 596 */           ITMGlobalTimestampEvent e = new ITMGlobalTimestampEvent(payload * this.timePrescaler, (byte)0, (byte)0, (byte)2);
/* 597 */           this.rxBuffer.addRecord(e);
/*     */         } else {
/* 599 */           TRACE(2, "Invalid global timestamp packet, 0x" + Integer.toString(c, 16));
/*     */         }
/*     */       } else {
/* 602 */         TRACE(2, "Invalid global timestamp packet, 0x" + Integer.toString(c, 16));
/* 603 */         return 1;
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 8:
/* 609 */       if ((c & 0x80) != 0) {
/* 610 */         TRACE(2, "Invalid ITM extension packet received, 0x" + Integer.toString(c, 16));
/* 611 */         return 1;
/*     */       }
/* 613 */       this.ITMPortPage = ((byte)((c & 0x70) >> 4));
/* 614 */       ITMExtensionEvent e = new ITMExtensionEvent(this.ITMPortPage);
/* 615 */       this.rxBuffer.addRecord(e);
/* 616 */       break;
/*     */     default:
/* 625 */       TRACE(4, "WARNING: unknown protocol packet 0x" + Integer.toString(c, 16));
/*     */     }
/*     */ 
/* 628 */     return 0;
/*     */   }
/*     */ 
/*     */   private int createInstrumentationPacket(int packetheader)
/*     */   {
/* 637 */     byte port = (byte)((packetheader & 0xF8) >> 3);
/* 638 */     int size = getSizeFromSSBits(packetheader & 0x3);
/* 639 */     long payload = 0L;
/*     */ 
/* 641 */     if (size == 0)
/*     */     {
/* 643 */       TRACE(2, "Invalid packet header for this type Instrumentation: 0x" + Integer.toString(packetheader, 16));
/* 644 */       return 0;
/*     */     }
/*     */ 
/* 648 */     for (int i = 0; i < size; i++) {
/* 649 */       long c = getNextByte(false);
/* 650 */       if (c == -2L) {
/* 651 */         return -2;
/*     */       }
/* 653 */       if (c == -1L)
/* 654 */         return -1;
/* 655 */       payload |= c << 8 * i;
/*     */     }
/* 657 */     ITMPortEvent e = new ITMPortEvent(this.ITMPortPage, port, payload, size);
/* 658 */     this.rxBuffer.addRecord(e);
/*     */ 
/* 660 */     TRACE(1, "Instrumentation packet created, size " + size + ", port " + port + ", payload 0x" + Long.toString(payload, 16));
/*     */ 
/* 662 */     return 0;
/*     */   }
/*     */ 
/*     */   private int createHardwareSourcePacket(int packetheader)
/*     */   {
/* 670 */     int id = (packetheader & 0xF8) >> 3;
/* 671 */     int size = getSizeFromSSBits(packetheader & 0x3);
/* 672 */     long payload = 0L;
/*     */ 
/* 674 */     if (size == 0)
/*     */     {
/* 676 */       TRACE(2, "Invalid packet header payload size for this type Hardware source: 0x" + Integer.toString(packetheader, 16));
/* 677 */       return 0;
/*     */     }
/*     */ 
/* 681 */     if (id == 0)
/*     */     {
/* 684 */       if (size != 1) {
/* 685 */         TRACE(2, "Invalid Event counter packet 0x" + Integer.toString(packetheader, 16));
/* 686 */         return 1;
/*     */       }
/* 688 */     } else if (id == 1)
/*     */     {
/* 691 */       if (size != 2) {
/* 692 */         TRACE(2, "Invalid exception tracing packet 0x" + Integer.toString(packetheader, 16));
/* 693 */         return 1;
/*     */       }
/* 695 */     } else if (id == 2)
/*     */     {
/* 697 */       TRACE(1, "PC sampling packet");
/* 698 */     } else if ((id >= 8) && (id <= 23))
/*     */     {
/* 700 */       byte dataPacketType = (byte)((packetheader & 0xC0) >> 6);
/* 701 */       if (dataPacketType == 1)
/*     */       {
/* 703 */         if ((packetheader & 0x8) == 0)
/*     */         {
/* 705 */           if (size != 4) {
/* 706 */             TRACE(2, "Invalid PC value payload size 0x" + Integer.toString(packetheader, 16));
/* 707 */             return 1;
/*     */           }
/*     */ 
/*     */         }
/* 711 */         else if (size != 2) {
/* 712 */           TRACE(2, "Invalid Address offset payload size 0x" + Integer.toString(packetheader, 16));
/* 713 */           return 1;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 719 */       TRACE(2, "Invalid hardware source packet id");
/* 720 */       return 1;
/*     */     }
/*     */ 
/* 724 */     for (int i = 0; i < size; i++)
/*     */     {
/* 726 */       long c = getNextByte(false);
/* 727 */       if (c == -2L) {
/* 728 */         return -2;
/*     */       }
/* 730 */       if (c == -1L)
/* 731 */         return -1;
/* 732 */       payload |= c << 8 * i;
/*     */     }
/*     */ 
/* 736 */     if (id == 0)
/*     */     {
/* 738 */       byte counters = (byte)(int)(payload & 0x3F);
/* 739 */       DWTCounterEvent e = new DWTCounterEvent(counters);
/* 740 */       this.rxBuffer.addRecord(e);
/*     */     }
/* 742 */     else if (id == 1)
/*     */     {
/* 744 */       int exceptionNumber = (int)(payload & 0x1FF);
/* 745 */       byte function = (byte)(int)((payload & 0x3000) >> 12);
/*     */ 
/* 747 */       if (function == 0) {
/* 748 */         TRACE(4, "WARNING: Reserved function for exception trace packet used 0x00");
/*     */       }
/*     */ 
/* 751 */       DWTExceptionEvent e = new DWTExceptionEvent(exceptionNumber, function);
/* 752 */       this.rxBuffer.addRecord(e);
/* 753 */       this.exceptionBuffer.addRecord(e, false);
/*     */     }
/* 755 */     else if (id == 2)
/*     */     {
/* 757 */       byte type = 0;
/* 758 */       if ((size == 1) && (payload == 0L)) {
/* 759 */         type = 1;
/*     */       }
/* 761 */       DWTPCSampleEvent e = new DWTPCSampleEvent(type, payload);
/* 762 */       this.rxBuffer.addRecord(e);
/* 763 */     } else if ((id >= 8) && (id <= 23))
/*     */     {
/* 765 */       byte comparator = (byte)((packetheader & 0x30) >> 4);
/* 766 */       byte dataPacketType = (byte)((packetheader & 0xC0) >> 6);
/* 767 */       if (dataPacketType == 1)
/*     */       {
/* 769 */         if ((packetheader & 0x8) == 0)
/*     */         {
/* 771 */           DWTDataTracePCValueEvent e = new DWTDataTracePCValueEvent(comparator, payload);
/* 772 */           this.rxBuffer.addRecord(e);
/*     */         }
/*     */         else {
/* 775 */           DWTDataTraceAddressOffsetEvent e = new DWTDataTraceAddressOffsetEvent(comparator, (int)(payload & 0xFFFF));
/* 776 */           this.rxBuffer.addRecord(e);
/*     */         }
/* 778 */       } else if (dataPacketType == 2)
/*     */       {
/* 780 */         byte access = (byte)((packetheader & 0x8) >> 3);
/* 781 */         DWTDataTraceDataValueEvent e = new DWTDataTraceDataValueEvent(comparator, access, payload);
/* 782 */         this.rxBuffer.addRecord(e);
/*     */       } else {
/* 784 */         TRACE(4, "WARNING: invalid dataPacketType for data trace packet 0x" + Integer.toString(packetheader, 16));
/* 785 */         return 1;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 790 */       TRACE(2, "Invalid hardware source packet id");
/*     */     }
/*     */ 
/* 793 */     TRACE(1, "Hardware source packet created, size " + size + ", id " + id + ", payload 0x" + Long.toString(payload, 16));
/*     */ 
/* 795 */     return 0;
/*     */   }
/*     */ 
/*     */   private void TRACE(int type, String msg) {
/* 799 */     if ((this.TRACE_ON) && 
/* 800 */       ((this.TRACE_TYPE & type) > 0))
/* 801 */       System.out.println("listid: (" + this.sizeRead + ") (" + this.rxBuffer.size() + ") " + msg);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 811 */     this.disposed = true;
/* 812 */     interrupt();
/* 813 */     if (isParsingInterrupts()) {
/* 814 */       stopParsingInterrupts();
/*     */     }
/* 816 */     if (elfHelper != null) {
/* 817 */       elfHelper.dispose();
/* 818 */       elfHelper = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTracing(boolean tracing)
/*     */   {
/* 827 */     this.tracing = tracing;
/*     */   }
/*     */ 
/*     */   public boolean isTracing()
/*     */   {
/* 836 */     return this.tracing;
/*     */   }
/*     */ 
/*     */   public void setComparatorConfig(SWVComparatorConfig config, byte cmpId) {
/* 840 */     this.comparatorConfig[cmpId] = config;
/*     */   }
/*     */ 
/*     */   public SWVComparatorConfig getComparatorConfig(byte cmpId) {
/* 844 */     return this.comparatorConfig[cmpId];
/*     */   }
/*     */ 
/*     */   public boolean sessionSuspended()
/*     */   {
/* 852 */     return this.fSuspended;
/*     */   }
/*     */ 
/*     */   public void setSuspended(boolean suspended)
/*     */   {
/* 860 */     this.fSuspended = suspended;
/*     */   }
/*     */ 
/*     */   public boolean isParsingInterrupts() {
/* 864 */     if (this.sWVInterruptParser == null) return false;
/* 865 */     return true;
/*     */   }
/*     */ 
/*     */   public SWVInterruptParser startParsingInterrupts()
/*     */   {
/* 873 */     if (this.sWVInterruptParser == null) {
/* 874 */       this.sWVInterruptParser = new SWVInterruptParser(this);
/*     */     }
/*     */ 
/* 877 */     return this.sWVInterruptParser;
/*     */   }
/*     */ 
/*     */   public void stopParsingInterrupts() {
/* 881 */     this.sWVInterruptParser.dispose();
/* 882 */     this.sWVInterruptParser = null;
/*     */   }
/*     */ 
/*     */   public SWVInterruptParser getInterruptParser() {
/* 886 */     return this.sWVInterruptParser;
/*     */   }
/*     */ 
/*     */   public String getFunctionName(long addr)
/*     */   {
/*     */     try
/*     */     {
/* 898 */       String programPath = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration().getAttribute("org.eclipse.cdt.launch.PROGRAM_NAME", "");
/* 899 */       if (programPath != null) {
/* 900 */         programPath = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(programPath, true);
/*     */       }
/*     */ 
/* 903 */       String projectName = SWVUtil.getActiveProject().getName();
/* 904 */       IPath path = TSProjectManager.getProjectPath(projectName);
/* 905 */       if (path != null)
/* 906 */         programPath = path.append(programPath).toOSString();
/*     */       else {
/* 908 */         return "";
/*     */       }
/*     */ 
/* 911 */       Elf elf = getElf(programPath);
/* 912 */       if (elf == null) {
/* 913 */         return "";
/*     */       }
/*     */ 
/* 916 */       Elf.Symbol symbol = elf.getSymbol(new Addr32(addr));
/* 917 */       String functionName = Messages.SWVStatisticalProfilingView_UNKNOWN_FUNCTION;
/* 918 */       if ((addr >= symbol.st_value.getValue().longValue()) && (addr <= symbol.st_value.getValue().longValue() + symbol.st_size));
/* 919 */       return symbol.toString() + "()";
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 927 */       e.printStackTrace();
/* 928 */     }return Messages.SWVStatisticalProfilingView_UNKNOWN_FUNCTION;
/*     */   }
/*     */ 
/*     */   public static Elf getElf(String programPath)
/*     */   {
/* 940 */     Elf elf = null;
/* 941 */     if (elfHelper == null) {
/*     */       try {
/* 943 */         elfHelper = new ElfHelper(programPath);
/*     */       } catch (IOException e) {
/* 945 */         e.printStackTrace();
/* 946 */         return null;
/*     */       }
/*     */     }
/* 949 */     if (elfHelper == null) {
/* 950 */       return null;
/*     */     }
/* 952 */     elf = elfHelper.getElf();
/* 953 */     if (elf == null)
/* 954 */       return null;
/*     */     try
/*     */     {
/* 957 */       elf.loadSymbols();
/*     */     } catch (IOException e) {
/* 959 */       e.printStackTrace();
/* 960 */       return null;
/*     */     }
/* 962 */     return elf;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.SWVClient
 * JD-Core Version:    0.6.2
 */