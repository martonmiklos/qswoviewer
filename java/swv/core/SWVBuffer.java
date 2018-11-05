/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductConfig;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.swv.model.Event;
/*     */ import com.atollic.truestudio.swv.model.ITMLocalTimestampEvent;
/*     */ import com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.jface.preference.IPreferenceStore;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class SWVBuffer
/*     */ {
/*     */   private ArrayList<Event> swvData;
/*     */   Semaphore dataSemaphore;
/*  34 */   public boolean TRACE_ON = false;
/*     */   private int maxBufferSize;
/*  36 */   private boolean notified = false;
/*  37 */   private SWVClient currentClient = null;
/*     */   SWVStatisticalTimelineBuckets buckets;
/*     */ 
/*     */   public SWVBuffer(SWVClient SwvClient)
/*     */   {
/*  43 */     this.swvData = new ArrayList(10000);
/*  44 */     this.dataSemaphore = new Semaphore(1, true);
/*  45 */     setCurrentClient(SwvClient);
/*     */ 
/*  47 */     if ((ProductConfig.getInstance().getProductModel() == 1) && (ProductConfig.getInstance().getProductTarget() != ProductManager.PRODUCT_TARGET_ARM)) {
/*  48 */       this.maxBufferSize = 10;
/*     */     }
/*     */     else {
/*  51 */       IPreferenceStore store = SWVPlugin.getDefault().getPreferenceStore();
/*  52 */       this.maxBufferSize = store.getInt("com.atollic.truestudio.swv.core.pref_buff_size");
/*     */ 
/*  54 */       if (this.maxBufferSize == 0)
/*  55 */         this.maxBufferSize = 2000000;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient()
/*     */   {
/*  64 */     return this.currentClient;
/*     */   }
/*     */ 
/*     */   public void setCurrentClient(SWVClient currentClient)
/*     */   {
/*  71 */     this.currentClient = currentClient;
/*     */   }
/*     */ 
/*     */   public void addRecord(Event event, boolean updateEventID)
/*     */   {
/*  81 */     if ((getCurrentClient() != null) && (!getCurrentClient().isTracing())) {
/*  82 */       return;
/*     */     }
/*  84 */     if (this.swvData.size() >= this.maxBufferSize) {
/*  85 */       if (!this.notified) {
/*  86 */         this.notified = true;
/*  87 */         Display.getDefault().asyncExec(new Runnable()
/*     */         {
/*     */           public void run() {
/*  90 */             if (ProductConfig.getInstance().getProductModel() != 1)
/*     */             {
/*  93 */               MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Serial Wire Viewer", Messages.SWVBuffer_TRACE_BUFFER_FULL);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */ 
/*  99 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 103 */       this.dataSemaphore.acquire();
/* 104 */       if (updateEventID) {
/* 105 */         event.setEventID(this.swvData.size());
/*     */       }
/* 107 */       this.swvData.add(event);
/* 108 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addRecord(Event event)
/*     */   {
/* 121 */     addRecord(event, true);
/*     */   }
/*     */ 
/*     */   public Object getLastRecord()
/*     */   {
/* 128 */     Event event = null;
/*     */     try {
/* 130 */       this.dataSemaphore.acquire();
/* 131 */       if (this.swvData.size() != 0) {
/* 132 */         event = (Event)this.swvData.get(this.swvData.size() - 1);
/*     */       }
/* 134 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/* 139 */     return event;
/*     */   }
/*     */ 
/*     */   public Object[] getRecords()
/*     */   {
/* 147 */     Object[] tmpData = null;
/*     */     try
/*     */     {
/* 150 */       this.dataSemaphore.acquire();
/* 151 */       tmpData = this.swvData.toArray();
/* 152 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */ 
/* 158 */     return tmpData;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 165 */     int size = 0;
/*     */     try {
/* 167 */       this.dataSemaphore.acquire();
/* 168 */       size = this.swvData.size();
/* 169 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/* 174 */     return size;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*     */     try
/*     */     {
/* 183 */       this.dataSemaphore.acquire();
/* 184 */       this.swvData.clear();
/* 185 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTimestampOnLastAndInterpolateBack(long cycles, double times)
/*     */   {
/* 198 */     if (this.swvData.isEmpty())
/* 199 */       return;
/*     */     try
/*     */     {
/* 202 */       this.dataSemaphore.acquire();
/*     */ 
/* 204 */       for (int i = 0; i < this.swvData.size(); i++) {
/* 205 */         Event e = (Event)this.swvData.get(this.swvData.size() - i - 1);
/* 206 */         if ((e instanceof ITMLocalTimestampEvent)) {
/*     */           break;
/*     */         }
/* 209 */         if (e.getCycles() != 0L) break;
/* 210 */         e.setCycles(cycles);
/* 211 */         e.setTimes(times);
/*     */ 
/* 214 */         if (i != 0) {
/* 215 */           e.setCyclesValueIsNotReal(true);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 222 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getDeltaRecordsByIndex(int index)
/*     */   {
/* 232 */     Object[] tmpDataEvents = null;
/*     */     try
/*     */     {
/* 235 */       this.dataSemaphore.acquire();
/*     */ 
/* 237 */       if ((index < 0) || (this.swvData.size() < 1)) {
/* 238 */         this.dataSemaphore.release();
/* 239 */         return null;
/*     */       }
/*     */ 
/* 242 */       tmpDataEvents = this.swvData.subList(index, this.swvData.size()).toArray();
/* 243 */       this.dataSemaphore.release();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */ 
/* 249 */     return tmpDataEvents;
/*     */   }
/*     */ 
/*     */   public SWVStatisticalTimelineBuckets createCondensedStatisticalTimeline(long starttime, long endtime, long timeslot, int cloc_speed, Class eventClass)
/*     */   {
/* 265 */     this.buckets = new SWVStatisticalTimelineBuckets(cloc_speed, eventClass);
/* 266 */     if ((endtime - starttime <= 0L) || (timeslot <= 0L))
/*     */     {
/* 268 */       this.buckets.addValueSet(0.0D, 0.0D);
/* 269 */       this.buckets.setXMin(0.0D);
/* 270 */       return this.buckets;
/*     */     }
/* 272 */     this.buckets.setXMin(starttime);
/* 273 */     this.buckets.setXMax(endtime);
/*     */ 
/* 275 */     double ymax = 0.0D;
/* 276 */     Event[] firstEvents = new Event[1];
/* 277 */     ConcurrentHashMap spanEventStatistics = new ConcurrentHashMap(20);
/*     */ 
/* 279 */     long start = starttime - starttime % timeslot;
/* 280 */     long end = endtime - endtime % timeslot + timeslot;
/* 281 */     long size = (end - start) / timeslot;
/*     */ 
/* 283 */     int bufferSize = this.swvData.size() - 1;
/*     */     int ep;
/*     */     int sp;
/*     */     int ep;
/* 285 */     if (start == 0L)
/*     */     {
/* 287 */       int sp = 0;
/* 288 */       ep = bufferSize;
/*     */     }
/*     */     else
/*     */     {
/* 292 */       sp = findPacket(start);
/*     */ 
/* 294 */       ep = findPacket(end);
/*     */     }
/* 296 */     if (this.TRACE_ON) {
/* 297 */       System.out.println("PACKETS TO PARSE " + (ep - sp) + "(" + sp + ", " + ep + ") out of " + bufferSize);
/*     */     }
/* 299 */     if (ep < 0)
/*     */     {
/* 301 */       return this.buckets;
/*     */     }
/*     */ 
/* 305 */     double prevValue = 0.0D;
/*     */ 
/* 309 */     boolean isFirstBucket = true;
/* 310 */     boolean isLastBucket = false;
/* 311 */     long t1 = System.currentTimeMillis();
/* 312 */     for (int i = 0; i < size; i++) {
/* 313 */       long startspan = start + timeslot * i;
/* 314 */       if ((i == 0) || (i == size - 1L) || (prevValue != 0.0D) || (((Event)this.swvData.get(sp)).getCycles() <= startspan + timeslot))
/*     */       {
/* 318 */         double yValue = 0.0D;
/* 319 */         if (!this.buckets.isBucketGeneric()) {
/* 320 */           firstEvents = new Event[40];
/*     */         }
/* 322 */         spanEventStatistics = new ConcurrentHashMap(20);
/* 323 */         while (sp < ep) {
/* 324 */           Event theEvent = (Event)this.swvData.get(sp);
/* 325 */           Class curClass = theEvent.getClass();
/* 326 */           if ((eventClass == null) || (curClass == eventClass)) {
/* 327 */             long cycl = theEvent.getCycles();
/* 328 */             if (cycl >= startspan + timeslot) break;
/* 329 */             yValue += 1.0D;
/* 330 */             if ((!this.buckets.isBucketGeneric()) && (yValue <= 40.0D)) {
/* 331 */               firstEvents[((int)yValue - 1)] = theEvent;
/*     */             }
/* 333 */             String statisticIndex = theEvent.printType();
/* 334 */             Integer oldVal = (Integer)spanEventStatistics.get(statisticIndex);
/* 335 */             if (oldVal == null) {
/* 336 */               spanEventStatistics.put(statisticIndex, Integer.valueOf(1));
/*     */             }
/*     */             else
/*     */             {
/* 338 */               Integer newVal = Integer.valueOf(oldVal.intValue() + 1);
/* 339 */               while (!spanEventStatistics.replace(statisticIndex, oldVal, newVal));
/*     */             }
/*     */           }
/* 345 */           sp++;
/* 346 */           if (sp >= ep) {
/* 347 */             isLastBucket = true;
/*     */           }
/*     */         }
/*     */ 
/* 351 */         if ((yValue > 0.0D) || (prevValue > 0.0D) || (isFirstBucket) || (isLastBucket)) {
/* 352 */           if (!this.buckets.isBucketGeneric()) {
/* 353 */             this.buckets.eventSerie.add(firstEvents);
/*     */           }
/* 355 */           this.buckets.addValueSet(startspan, yValue, spanEventStatistics);
/* 356 */           isFirstBucket = false;
/* 357 */           isLastBucket = false;
/*     */         }
/* 359 */         prevValue = yValue;
/* 360 */         if (yValue > ymax)
/* 361 */           ymax = yValue;
/*     */       }
/*     */     }
/* 364 */     this.buckets.setYMax(ymax);
/* 365 */     if (this.TRACE_ON) {
/* 366 */       System.out.println(" buckets created " + this.buckets.getXValueSet().size() + " time " + (System.currentTimeMillis() - t1));
/*     */     }
/*     */ 
/* 369 */     return this.buckets;
/*     */   }
/*     */ 
/*     */   public int findPacket(long time)
/*     */   {
/* 379 */     int low = 0;
/* 380 */     int high = this.swvData.size() - 1;
/* 381 */     int mid = -1;
/*     */ 
/* 383 */     while (low <= high) {
/* 384 */       mid = (low + high) / 2;
/* 385 */       long cycl = ((Event)this.swvData.get(mid)).getCycles();
/* 386 */       if (cycl - time < 0L)
/* 387 */         low = mid + 1;
/* 388 */       else if (cycl - time > 0L)
/* 389 */         high = mid - 1;
/*     */       else {
/* 391 */         return mid;
/*     */       }
/*     */     }
/* 394 */     return mid;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.SWVBuffer
 * JD-Core Version:    0.6.2
 */